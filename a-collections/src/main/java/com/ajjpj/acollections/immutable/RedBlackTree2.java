package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.util.AOption;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;


class RedBlackTree2 {
    static boolean isEmpty(Tree<?,?> tree) {
        return tree == null;
    }
    static <A> boolean contains(Tree<A,?> tree, A x, Comparator<A> ordering) {
        return lookup(tree, x, ordering) != null;
    }
    static <A,B> AOption<B> get(Tree<A,B> tree, A x, Comparator<A> ordering) {
        final Tree<A,B> raw = lookup(tree, x, ordering);
        if (raw == null) return null;
        return AOption.some(raw.value);
    }

    static <A,B> Tree<A,B> lookup(Tree<A,B> tree, A x, Comparator<A> ordering) {
        while (true) {
            if (tree == null) return null;
            final int cmp = ordering.compare(x, tree.key);
            if (cmp < 0) tree = tree.left;
            else if (cmp > 0) tree = tree.right;
            else return tree;
        }
    }

    static int count(Tree<?,?> tree) {
        if (tree == null) return 0;
        return tree.count;
    }

    static <A> int countInRange(Tree<A,?> tree, AOption<A> from, AOption<A> to, Comparator<A> ordering) {
        if (tree == null) return 0;
        if (from.isEmpty() && to.isEmpty()) return tree.count;
        if (from.isDefined() && ordering.compare(tree.key, from.get()) < 0) return countInRange(tree.right, from, to, ordering);
        if (to.isDefined() && ordering.compare(tree.key, to.get()) >= 0) return countInRange(tree.left, from, to, ordering);
        return 1 + countInRange(tree.left, from, AOption.none(), ordering) + countInRange(tree.right, AOption.none(), to, ordering);
    }

    static <A,B> Tree<A,B> update(Tree<A,B> tree, A k, B v, boolean overwrite, Comparator<A> ordering) {
        return blacken(upd(tree, k, v, overwrite, ordering));
    }
    static <A,B> Tree<A,B> delete(Tree<A,B> tree, A k, Comparator<A> ordering) {
        return blacken(del(tree, k, ordering));
    }
    static <A,B> Tree<A,B> rangeImpl(Tree<A,B> tree, AOption<A> from, AOption<A> until, Comparator<A> ordering) {
        if (from.isEmpty() && until.isEmpty()) return tree;
        if (from.isDefined() && until.isDefined()) return range(tree, from.get(), until.get(), ordering);
        if (from.isDefined()) return from(tree, from.get(), ordering);
        return until(tree, until.get(), ordering);
    }
    static <A,B> Tree<A,B> range(Tree<A,B> tree, A from, A until, Comparator<A> ordering) {
        return blacken(doRange(tree, from, until, ordering));
    }
    static <A,B> Tree<A,B> from(Tree<A,B> tree, A from, Comparator<A> ordering) {
        return blacken(doFrom(tree, from, ordering));
    }
    static <A,B> Tree<A,B> to(Tree<A,B> tree, A to, Comparator<A> ordering) {
        return blacken(doTo(tree, to, ordering));
    }
    static <A,B> Tree<A,B> until(Tree<A,B> tree, A until, Comparator<A> ordering) {
        return blacken(doUntil(tree, until, ordering));
    }

    static <A,B> Tree<A,B> drop(Tree<A,B> tree, int n) {
        return blacken(doDrop(tree, n));
    }
    static <A,B> Tree<A,B> take(Tree<A,B> tree, int n) {
        return blacken(doTake(tree, n));
    }
    static <A,B> Tree<A,B> slice(Tree<A,B> tree, int from, int until) {
        return blacken(doSlice(tree, from, until));
    }

    static <A,B> Tree<A,B> smallest(Tree<A,B> tree) {
        if (tree == null) throw new NoSuchElementException("empty map");
        Tree<A,B> result = tree;
        while (result.left != null) result = result.left;
        return result;
    }
    static <A,B> Tree<A,B> greatest(Tree<A,B> tree) {
        if (tree == null) throw new NoSuchElementException("empty map");
        Tree<A,B> result = tree;
        while (result.right != null) result = result.right;
        return result;
    }

    static <A,B> void foreach(Tree<A,B> tree, BiConsumer<A,B> f) {
        if (tree != null) _foreach(tree, f);
    }
    private static <A,B> void _foreach(Tree<A,B> tree, BiConsumer<A,B> f) {
        if (tree.left != null) _foreach(tree.left, f);
        f.accept(tree.key, tree.value);
        if (tree.right != null) _foreach(tree.right, f);
    }

    static <A> void foreachKey(Tree<A,?> tree, Consumer<A> f) {
        if (tree != null) _foreachKey(tree, f);
    }
    private static <A,B> void _foreachKey(Tree<A,?> tree, Consumer<A> f) {
        if (tree.left != null) _foreachKey(tree.left, f);
        f.accept(tree.key);
        if (tree.right != null) _foreachKey(tree.right, f);
    }

    static <A,B> Iterator<Map.Entry<A,B>> iterator(Tree<A,B> tree, AOption<A> start, Comparator<A> ordering) {
        return new EntriesIterator(tree, start, ordering);
    }
    static <A> Iterator<A> keysIterator(Tree<A,?> tree, AOption<A> start, Comparator<A> ordering) {
        return new KeysIterator(tree, start, ordering);
    }
    static <A,B> Iterator<B> valuesIterator(Tree<A,B> tree, AOption<A> start, Comparator<A> ordering) {
        return new ValuesIterator(tree, start, ordering);
    }

    static <A,B> Tree<A,B> nth(Tree<A,B> tree, int n) {
        while (true) {
            final int count = count(tree.left);
            if (n < count) tree = tree.left;
            else if (n > count) {
                tree = tree.right;
                n -= count+1;
            }
            else return tree;
        }
    }

    static boolean isBlack(Tree<?,?> tree) {
        return tree == null || isBlackTree(tree);
    }

    private static boolean isRedTree(Tree<?,?> tree) {
        return tree instanceof RedTree;
    }
    private static boolean isBlackTree(Tree<?,?> tree) {
        return tree instanceof BlackTree;
    }

    private static <A,B> Tree<A,B> blacken(Tree<A,B> t) {
        if (t == null) return null;
        return t.black();
    }

    private static <A,B> Tree<A,B> mkTree(boolean isBlack, A k, B v, Tree<A,B> l, Tree<A,B> r) {
        if (isBlack) return new BlackTree<>(k, v, l, r);
        else return new RedTree<>(k, v, l, r);
    }


    private static <A,B> Tree<A,B> balanceLeft(boolean isBlack, A z, B zv, Tree<A,B> l, Tree<A,B> d) {
        if (isRedTree(l) && isRedTree(l.left)) return new RedTree<>(l.key, l.value, new BlackTree<>(l.left.key, l.left.value, l.left.left, l.left.right), new BlackTree<>(z, zv, l.right, d));
        if (isRedTree(l) && isRedTree(l.right)) return new RedTree<>(l.right.key, l.right.value, new BlackTree<>(l.key, l.value, l.left, l.right.left), new BlackTree<>(z, zv, l.right.right, d));
        return mkTree(isBlack, z, zv, l, d);
    }
    private static <A,B> Tree<A,B> balanceRight(boolean isBlack, A x, B xv, Tree<A,B> a, Tree<A,B> r) {
        if (isRedTree(r) && isRedTree(r.left)) return new RedTree<>(r.left.key, r.left.value, new BlackTree<>(x, xv, a, r.left.left), new BlackTree<>(r.key, r.value, r.left.right, r.right));
        if (isRedTree(r) && isRedTree(r.right)) return new RedTree<>(r.key, r.value, new BlackTree<>(x, xv, a, r.left), new BlackTree<>(r.right.key, r.right.value, r.right.left, r.right.right));
        return mkTree(isBlack, x, xv, a, r);
    }
    private static <A,B> Tree<A,B> upd(Tree<A,B> tree, A k, B v, boolean overwrite, Comparator<A> ordering) {
        if (tree == null) return new RedTree<>(k, v, null, null);
        final int cmp = ordering.compare(k, tree.key);
        if (cmp < 0) return balanceLeft(isBlackTree(tree), tree.key, tree.value, upd(tree.left, k, v, overwrite, ordering), tree.right);
        if (cmp > 0) return balanceRight(isBlackTree(tree), tree.key, tree.value, tree.left, upd(tree.right, k, v, overwrite, ordering));
        if (overwrite || !Objects.equals(k, tree.key)) return mkTree(isBlackTree(tree), k, v, tree.left, tree.right);
        return tree;
    }
    private static <A,B> Tree<A,B> updNth(Tree<A,B> tree, int idx, A k, B v, boolean overwrite) {
        if (tree == null) return new RedTree<>(k, v, null, null);
        final int rank = count(tree.left) + 1;
        if (idx < rank) return balanceLeft(isBlackTree(tree), tree.key, tree.value, updNth(tree.left, idx, k, v, overwrite), tree.right);
        if (idx > rank) return balanceRight(isBlackTree(tree), tree.key, tree.value, tree.left, updNth(tree.right, idx-rank, k, v, overwrite));
        if (overwrite) return mkTree(isBlackTree(tree), k, v, tree.left, tree.right);
        return tree;
    }

    /* Based on Stefan Kahrs' Haskell version of Okasaki's Red&Black Trees
     * Constructing Red-Black Trees, Ralf Hinze: [[http://www.cs.ox.ac.uk/ralf.hinze/publications/WAAAPL99b.ps.gz]]
     * Red-Black Trees in a Functional Setting, Chris Okasaki: [[https://wiki.rice.edu/confluence/download/attachments/2761212/Okasaki-Red-Black.pdf]] */
    private static <A,B> Tree<A,B> del_balance(A x, B xv, Tree<A,B> tl, Tree<A,B> tr) {
        if (isRedTree(tl)) {
            if (isRedTree(tr)) return new RedTree<>(x, xv, tl.black(), tr.black());
            if (isRedTree(tl.left)) return new RedTree<>(tl.key, tl.value, tl.left.black(), new BlackTree<>(x, xv, tl.right, tr));
            if (isRedTree(tl.right)) return new RedTree<>(tl.right.key, tl.right.value, new BlackTree<>(tl.key, tl.value, tl.left, tl.right.left), new BlackTree<>(x, xv, tl.right.right, tr));
            return new BlackTree<>(x, xv, tl, tr);
        }
        else if (isRedTree(tr)) {
            if (isRedTree(tr.right)) return new RedTree<>(tr.key, tr.value, new BlackTree<>(x, xv, tl, tr.left), tr.right.black());
            if (isRedTree(tr.left)) return new RedTree<>(tr.left.key, tr.left.value, new BlackTree<>(x, xv, tl, tr.left.left), new BlackTree<>(tr.key, tr.value, tr.left.right, tr.right));
            return new BlackTree<>(x, xv, tl, tr);
        }
        return new BlackTree<>(x, xv, tl, tr);
    }

    private static <A,B> Tree<A,B> del_subl(Tree<A,B> t) {
        if (t instanceof BlackTree) return t.red();
        throw new IllegalStateException("Defect: invariance violation; expected black, got " + t);
    }

    private static <A,B> Tree<A,B> del_balLeft(A x, B xv, Tree<A,B> tl, Tree<A,B> tr) {
        if (isRedTree(tl)) return new RedTree<>(x, xv, tl.black(), tr);
        if (isBlackTree(tr)) return del_balance(x, xv, tl, tr.red());
        if (isRedTree(tr) && isBlackTree(tr.left)) return new RedTree<>(tr.left.key, tr.left.value, new BlackTree<>(x, xv, tl, tr.left.left), del_balance(tr.key, tr.value, tr.left.right, del_subl(tr.right)));
        throw new IllegalStateException("Defect: invariance violation");
    }

    private static <A,B> Tree<A,B> del_balRight(A x, B xv, Tree<A,B> tl, Tree<A,B> tr) {
        if (isRedTree(tr)) return new RedTree<>(x, xv, tl, tr.black());
        if (isBlackTree(tl)) return del_balance(x, xv, tl.red(), tr);
        if (isRedTree(tl) && isBlackTree(tl.right)) return new RedTree<>(tl.right.key, tl.right.value, del_balance(tl.key, tl.value, del_subl(tl.left), tl.right.left), new BlackTree<>(x, xv, tl.right.right, tr));
        throw new IllegalStateException("Defect: invariance violation");
    }

    private static <A,B> Tree<A,B> del_append(Tree<A,B> tl, Tree<A,B> tr) {
        if (tl == null) return tr;
        if (tr == null) return tl;
        if (isRedTree(tl) && isRedTree(tr)) {
            final Tree<A,B> bc = del_append(tl.right, tr.left);
            if (isRedTree(bc)) return new RedTree<>(bc.key, bc.value, new RedTree<>(tl.key, tl.value, tl.left, bc.left), new RedTree<>(tr.key, tr.value, bc.right, tr.right));
            else return new RedTree<>(tl.key, tl.value, tl.left, new RedTree<>(tr.key, tr.value, bc, tr.right));
        }
        if (isBlackTree(tl) && isBlackTree(tr)) {
            final Tree<A,B> bc = del_append(tl.right, tr.left);
            if (isRedTree(bc)) return new RedTree<>(bc.key, bc.value, new BlackTree<>(tl.key, tl.value, tl.left, bc.left), new BlackTree<>(tr.key, tr.value, bc.right, tr.right));
            else return del_balLeft(tl.key, tl.value, tl.left, new BlackTree<>(tr.key, tr.value, bc, tr.right));
        }
        if (isRedTree(tr)) return new RedTree<>(tr.key, tr.value, del_append(tl, tr.left), tr.right);
        if (isRedTree(tl)) return new RedTree<>(tl.key, tl.value, tl.left, del_append(tl.right, tr));
        throw new IllegalStateException("unmatched tree on append: " + tl + ", " + tr);
    }

    private static <A,B> Tree<A,B> del(Tree<A,B> tree, A k, Comparator<A> ordering) {
        final int cmp = ordering.compare(k, tree.key);
        if (cmp < 0) {
            if (isBlackTree(tree.left)) return del_balLeft(tree.key, tree.value, del(tree.left, k, ordering), tree.right);
            return new RedTree<>(tree.key, tree.value, del(tree.left, k, ordering), tree.right);
        }
        if (cmp > 0) {
            if (isBlackTree(tree.right)) return del_balRight(tree.key, tree.value, tree.left, del(tree.right, k, ordering));
            return new RedTree<>(tree.key, tree.value, tree.left, del(tree.right, k, ordering));
        }
        return del_append(tree.left, tree.right);
    }

    private static <A,B> Tree<A,B> doFrom(Tree<A,B> tree, A from, Comparator<A> ordering) {
        if (tree == null) return null;
        if (ordering.compare(tree.key, from) < 0) return doFrom(tree.right, from, ordering);
        final Tree<A,B> newLeft = doFrom(tree.left, from, ordering);
        if (newLeft == tree.left) return tree;
        if (newLeft == null) return upd(tree.right, tree.key, tree.value, false, ordering);
        return rebalance(tree, newLeft, tree.right);
    }

    private static <A,B> Tree<A,B> doTo(Tree<A,B> tree, A to, Comparator<A> ordering) {
        if (tree == null) return null;
        if (ordering.compare(to, tree.key) < 0) return doTo(tree.left, to, ordering);
        final Tree<A,B> newRight = doTo(tree.right, to, ordering);
        if (newRight == tree.right) return tree;
        if (newRight == null) return upd(tree.left, tree.key, tree.value, false, ordering);
        return rebalance(tree, tree.left, newRight);
    }

    private static <A,B> Tree<A,B> doUntil(Tree<A,B> tree, A until, Comparator<A> ordering) {
        if (tree == null) return null;
        if (ordering.compare(until, tree.key) <= 0) return doUntil(tree.left, until, ordering);
        final Tree<A,B> newRight = doUntil(tree.right, until, ordering);
        if (newRight == tree.right) return tree;
        if (newRight == null) return upd(tree.left, tree.key, tree.value, false, ordering);
        return rebalance(tree, tree.left, newRight);
    }

    private static <A,B> Tree<A,B> doRange(Tree<A,B> tree, A from, A until, Comparator<A> ordering) {
        if (tree == null) return null;
        if (ordering.compare(tree.key, from) < 0) return doRange(tree.right, from, until, ordering);
        if (ordering.compare(until, tree.key) <= 0) return doRange(tree.left, from, until, ordering);
        final Tree<A,B> newLeft = doFrom(tree.left, from, ordering);
        final Tree<A,B> newRight = doUntil(tree.right, until, ordering);
        if (newLeft == tree.left && newRight == tree.right) return tree;
        if (newLeft == null) return upd(newRight, tree.key, tree.value, false, ordering);
        if (newRight == null) return upd(newLeft, tree.key, tree.value, false, ordering);
        return rebalance(tree, newLeft, newRight);
    }

    private static <A,B> Tree<A,B> doDrop(Tree<A,B> tree, int n) {
        if (n <= 0) return tree;
        if (n >= count(tree)) return null;
        final int count = count(tree.left);
        if (n > count) return doDrop(tree.right, n-count-1);
        final Tree<A,B> newLeft = doDrop(tree.left, n);
        if (newLeft == tree.left) return tree;
        if (newLeft == null) return updNth(tree.right, n-count-1, tree.key, tree.value, false);
        return rebalance(tree, newLeft, tree.right);
    }

    private static <A,B> Tree<A,B> doTake(Tree<A,B> tree, int n) {
        if (n<=0) return null;
        if (n >= count(tree)) return tree;
        final int count = count(tree.left);
        if (n <= count) return doTake(tree.left, n);
        final Tree<A,B> newRight = doTake(tree.right, n-count-1);
        if (newRight == tree.right) return tree;
        if (newRight == null) return updNth(tree.left, n, tree.key, tree.value, false);
        return rebalance(tree, tree.left, newRight);
    }

    private static <A,B> Tree<A,B> doSlice(Tree<A,B> tree, int from, int until) {
        if (tree == null) return null;
        final int count = count(tree.left);
        if (from > count) return doSlice(tree.right, from-count-1, until-count-1);
        if (until <= count) return doSlice(tree.left, from, until);
        final Tree<A,B> newLeft = doDrop(tree.left, from);
        final Tree<A,B> newRight = doTake(tree.right, until-count-1);
        if (newLeft == tree.left && newRight == tree.right) return tree;
        if (newLeft == null) return updNth(newRight, from-count-1, tree.key, tree.value, false);
        if (newRight == null) return updNth(newLeft, until, tree.key, tree.value, false);
        return rebalance(tree, newLeft, newRight);
    }

    // The zipper returned might have been traversed left-most (always the left child)
    // or right-most (always the right child). Left trees are traversed right-most,
    // and right trees are traversed leftmost.

    // Returns the zipper for the side with deepest black nodes depth, a flag
    // indicating whether the trees were unbalanced at all, and a flag indicating
    // whether the zipper was traversed left-most or right-most.

    // If the trees were balanced, returns an empty zipper
    private static <A,B> CompareDepthResult<A,B> compareDepth(Tree<A,B> left, Tree<A,B> right) {
        return cd_unzipBoth(left, right, null, null, 0);
    }

    private static class CompareDepthResult<A,B> {
        final NList<Tree<A,B>> zipper;
        final boolean levelled;
        final boolean leftMost;
        final int smallerDepth;

        CompareDepthResult (NList<Tree<A, B>> zipper, boolean levelled, boolean leftMost, int smallerDepth) {
            this.zipper = zipper;
            this.levelled = levelled;
            this.leftMost = leftMost;
            this.smallerDepth = smallerDepth;
        }
    }

    // Once a side is found to be deeper, unzip it to the bottom
    private static <A,B> NList<Tree<A,B>> cd_unzip(NList<Tree<A,B>> zipper, boolean leftMost) {
        final Tree<A,B> next;
        if (leftMost) next = zipper.head.left;
        else next = zipper.head.right;
        if (next == null) return zipper;
        else return cd_unzip(zipper.prepend(next), leftMost); //TODO tail recursion
    }

    // Unzip left tree on the rightmost side and right tree on the leftmost side until one is
    // found to be deeper, or the bottom is reached
    private static <A,B> CompareDepthResult<A,B> cd_unzipBoth(Tree<A,B> left, Tree<A,B> right, NList<Tree<A,B>> leftZipper, NList<Tree<A,B>> rightZipper, int smallerDepth) {
        if (isBlackTree(left) && isBlackTree(right)) return cd_unzipBoth(left.right, right.left, leftZipper.prepend(left), rightZipper.prepend(right), smallerDepth + 1);
        if (isRedTree(left) && isRedTree(right)) return cd_unzipBoth(left.right, right.left, leftZipper.prepend(left), rightZipper.prepend(right), smallerDepth);
        if (isRedTree(right)) return cd_unzipBoth(left, right.left, leftZipper, rightZipper.prepend(right), smallerDepth);
        if (isRedTree(left)) return cd_unzipBoth(left.right, right, leftZipper.prepend(left), rightZipper, smallerDepth);
        if (left == null && right == null) return new CompareDepthResult<>(null, true, false, smallerDepth);
        if (left == null && isBlackTree(right)) {
            final boolean leftMost = true;
            return new CompareDepthResult<>(cd_unzip(rightZipper.prepend(right), leftMost), false, leftMost, smallerDepth);
        }
        if (isBlackTree(left) && right == null) {
            final boolean leftMost = false;
            return new CompareDepthResult<>(cd_unzip(leftZipper.prepend(left), leftMost), false, leftMost, smallerDepth);
        }
        throw new IllegalStateException("unmatched trees in unzip: " + left + ", " + right);
    }

    private static <A,B> NList<Tree<A,B>> rebalance_findDepth(NList<Tree<A,B>> zipper, int depth) {
        while (true) {
            if (zipper == null) throw new IllegalStateException("Defect: unexpected empty zipper while computing range");
            if (isBlackTree(zipper.head)) {
                if (depth == 1) return zipper;
                zipper = zipper.tail;
                depth -= 1;
            }
            else {
                zipper = zipper.tail;
            }
        }
    }

    private static <A,B> Tree<A,B> rebalance(Tree<A,B> tree, Tree<A,B> newLeft, Tree<A,B> newRight) {
        // Blackening the smaller tree avoids balancing problems on union;
        // this can't be done later, though, or it would change the result of compareDepth
        final Tree<A,B> blkNewLeft = blacken(newLeft);
        final Tree<A,B> blkNewRight = blacken(newRight);
        final CompareDepthResult<A,B> cdResult = compareDepth(blkNewLeft, blkNewRight);

        if (cdResult.levelled) return new BlackTree<>(tree.key, tree.value, blkNewLeft, blkNewRight);

        final NList<Tree<A,B>> zipFrom = rebalance_findDepth(cdResult.zipper, cdResult.smallerDepth);
        final Tree<A,B> union;
        if (cdResult.leftMost) union = new RedTree<>(tree.key, tree.value, blkNewLeft, zipFrom.head);
        else union = new RedTree<>(tree.key, tree.value, zipFrom.head, blkNewRight);
        return NList.foldLeft(zipFrom.tail, union, (tr, node) -> {
            if (cdResult.leftMost) return balanceLeft(isBlackTree(node), node.key, node.value, tr, node.right);
            else return balanceRight(isBlackTree(node), node.key, node.value, node.left, tr);
        });
    }

    // Null optimized list implementation for tree rebalancing. null presents Nil.
    private static class NList<A> {
        final A head;
        final NList<A> tail;

        NList (A head, NList<A> tail) {
            this.head = head;
            this.tail = tail;
        }

        NList<A> prepend(A x) {
            return new NList<>(x, this);
        }


        static <A,B> B foldLeft(NList<A> xs, B z, BiFunction<B,A,B> op) {
            B acc = z;
            NList<A> these = xs;
            while (these != null) {
                acc = op.apply(acc, these.head);
                these = these.tail;
            }
            return acc;
        }
    }

    /*
     * Forcing direct fields access using the @inline annotation helps speed up
     * various operations (especially smallest/greatest and update/delete).
     *
     * Unfortunately the direct field access is not guaranteed to work (but
     * works on the current implementation of the Scala compiler).
     *
     * An alternative is to implement the these classes using plain old Java code...
     */
    abstract static class Tree<A,B> {
        final A key;
        final B value;
        final Tree<A,B> left, right;
        final int count;

        Tree (A key, B value, Tree<A, B> left, Tree<A, B> right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            this.count = 1 + count(left) + count(right);
        }

        abstract Tree<A,B> black();
        abstract Tree<A,B> red();
    }
    static class RedTree<A,B> extends Tree<A,B> {
        RedTree (A key, B value, Tree<A, B> left, Tree<A, B> right) {
            super(key, value, left, right);
        }

        @Override Tree<A, B> black () {
            return new BlackTree<>(key, value, left, right);
        }

        @Override Tree<A, B> red () {
            return this;
        }
    }
    static class BlackTree<A,B> extends Tree<A,B> {
        BlackTree (A key, B value, Tree<A, B> left, Tree<A, B> right) {
            super(key, value, left, right);
        }

        @Override Tree<A, B> black () {
            return this;
        }

        @Override Tree<A, B> red () {
            return new RedTree<>(key, value, left, right);
        }
    }

    private static abstract class TreeIterator<A,B,R> implements Iterator<R> {
        final Tree<A,B> root;
        final AOption<A> start;
        final Comparator<A> ordering;

        private Tree[] stackOfNexts = null;
        private int index = 0;
        private Tree<A,B> lookahead;

        TreeIterator (Tree<A, B> root, AOption<A> start, Comparator<A> ordering) {
            this.root = root;
            this.start = start;
            this.ordering = ordering;
            this.lookahead = start.map(this::startFrom).orElseGet(() -> findLeftMostOrPopOnEmpty(root));

            if (root != null) {
                final int  maximumHeight = 2 * (32 - Integer.numberOfLeadingZeros(root.count + 2 - 1)) - 2;
                stackOfNexts = new Tree[maximumHeight];
            }
        }

        protected abstract R nextResult(Tree<A,B> tree);
        @Override public boolean hasNext() {
            return lookahead != null;
        }

        @Override public R next() {
            if (lookahead == null) throw new NoSuchElementException("next on empty iterator");
            final Tree<A,B> oldLookahead = lookahead;
            lookahead = findLeftMostOrPopOnEmpty(goRight(oldLookahead));
            return nextResult(oldLookahead);
        }


        private Tree<A,B> findLeftMostOrPopOnEmpty(Tree<A,B> tree) {
            while (true) {
                if (tree == null) return popNext();
                if (tree.left == null) return tree;
                tree = goLeft(tree);
            }
        }

        private void pushNext(Tree<A,B> tree) {
                stackOfNexts[index] = tree;
                index += 1;
        }
        private Tree<A,B> popNext() {
            if (index == 0) return null;
            index -= 1;
            //noinspection unchecked
            return stackOfNexts[index];
        }


        /**
         * Find the leftmost subtree whose key is equal to the given key, or if no such thing,
         * the leftmost subtree with the key that would be "next" after it according
         * to the ordering. Along the way build up the iterator's path stack so that "next"
         * functionality works.
         */
        private Tree<A,B> startFrom(A key) {
            if (root == null) return null;

            Tree<A,B> result = root;
            while (true) {
                if (result == null) return popNext();
                if (ordering.compare(key, result.key) <= 0) result = goLeft(result);
                else result = goRight(result);
            }
        }

        private Tree<A,B> goLeft(Tree<A,B> tree) {
            pushNext(tree);
            return tree.left;
        }
        private Tree<A,B> goRight(Tree<A,B> tree) {
            return tree.right;
        }
    }

    private static class EntryImpl<A,B> implements Map.Entry<A,B> {
        private final A key;
        private final B value;

        EntryImpl (A key, B value) {
            this.key = key;
            this.value = value;
        }

        @Override public A getKey () { return key; }
        @Override public B getValue () { return value; }
        @Override public B setValue (B value) { throw new UnsupportedOperationException(); }
    }

    private static class EntriesIterator<A,B> extends TreeIterator<A,B,Map.Entry<A,B>> {
        EntriesIterator (Tree<A, B> root, AOption<A> start, Comparator<A> ordering) {
            super(root, start, ordering);
        }

        @Override protected Map.Entry<A, B> nextResult (Tree<A, B> tree) {
            return new EntryImpl<>(tree.key, tree.value);
        }
    }

    private static class KeysIterator<A,B> extends TreeIterator<A,B,A> {
        KeysIterator (Tree<A, B> root, AOption<A> start, Comparator<A> ordering) {
            super(root, start, ordering);
        }

        @Override protected A nextResult (Tree<A, B> tree) {
            return tree.key;
        }
    }

    private static class ValuesIterator<A,B> extends TreeIterator<A,B,B> {
        ValuesIterator (Tree<A, B> root, AOption<A> start, Comparator<A> ordering) {
            super(root, start, ordering);
        }

        @Override protected B nextResult (Tree<A, B> tree) {
            return tree.value;
        }
    }
}
