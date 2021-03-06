package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AbstractAIterator;
import com.ajjpj.acollections.util.AOption;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiFunction;


class RedBlackTree {
    static boolean isEmpty (Tree<?, ?> tree) {
        return tree == null;
    }

    static <A> boolean contains (Tree<A, ?> tree, A x, Comparator<? super A> ordering) {
        return lookup(tree, x, ordering) != null;
    }

    static <A, B> AOption<B> get (Tree<A, B> tree, A x, Comparator<? super A> ordering) {
        return AOption.of(lookup(tree, x, ordering)).map(raw -> raw.value);
    }

    static <A, B> Tree<A, B> lookup (Tree<A, B> tree, A x, Comparator<? super A> ordering) {
        int cmp;
        while (tree != null && (cmp = ordering.compare(x, tree.key)) != 0) {
            if (cmp < 0) tree = tree.left;
            else tree = tree.right;
        }
        return tree;
    }

    static int count (Tree<?, ?> tree) {
        if (tree == null) return 0;
        return tree.count;
    }

    /**
     * Count all the nodes with keys greater than or equal to the lower bound and less than the upper bound.
     * The two bounds are optional.
     */
    static <A> int countInRange (Tree<A, ?> tree, AOption<A> from, boolean fromInclusive, AOption<A> to, boolean toInclusive, Comparator<? super A> ordering) {
        if (tree == null) return 0;

        // with no bounds use this node's count
        if (from.isEmpty() && to.isEmpty()) return tree.count;

        // if node is less than the lower bound, try the tree on the right, it might be in range
        if (fromInclusive && from.isDefined() && ordering.compare(tree.key, from.get()) < 0) return countInRange(tree.right, from, fromInclusive, to, toInclusive, ordering);
        if (!fromInclusive && from.isDefined() && ordering.compare(tree.key, from.get()) <= 0) return countInRange(tree.right, from, fromInclusive, to, toInclusive, ordering);

        // if node is greater than or equal to the upper bound, try the tree on the left, it might be in range
        if (toInclusive && to.isDefined() && ordering.compare(tree.key, to.get()) > 0) return countInRange(tree.left, from, fromInclusive, to, toInclusive, ordering);
        if (!toInclusive && to.isDefined() && ordering.compare(tree.key, to.get()) >= 0) return countInRange(tree.left, from, fromInclusive, to, toInclusive, ordering);

        // node is in range so the tree on the left will all be less than the upper bound and the tree on the
        // right will all be greater than or equal to the lower bound. So 1 for this node plus
        // count the subtrees by stripping off the bounds that we don't need any more
        return 1 + countInRange(tree.left, from, fromInclusive, AOption.none(), toInclusive, ordering) + countInRange(tree.right, AOption.none(), fromInclusive, to, toInclusive, ordering);
    }

    static <A, B> Tree<A, B> update (Tree<A, B> tree, A k, B v, boolean overwrite, Comparator<? super A> ordering) {
        return blacken(upd(tree, k, v, overwrite, ordering));
    }

    static <A, B> Tree<A, B> delete (Tree<A, B> tree, A k, Comparator<? super A> ordering) {
        return blacken(del(tree, k, ordering));
    }

    static <A, B> Tree<A, B> rangeImpl (Tree<A, B> tree, AOption<A> from, boolean fromInclusive, AOption<A> to, boolean toInclusive, Comparator<? super A> ordering) {
        if (from.isEmpty() && to.isEmpty()) return tree;
        if (from.isDefined() && to.isDefined()) return range(tree, from.get(), fromInclusive, to.get(), toInclusive, ordering);
        if (from.isDefined()) return from(tree, from.get(), fromInclusive, ordering);
        return to(tree, to.get(), toInclusive, ordering);
    }

    static <A, B> Tree<A, B> range (Tree<A, B> tree, A from, boolean fromInclusive, A to, boolean toInclusive, Comparator<? super A> ordering) {
        return blacken(doRange(tree, from, fromInclusive, to, toInclusive, ordering));
    }

    static <A, B> Tree<A, B> from (Tree<A, B> tree, A from, boolean fromInclusive, Comparator<? super A> ordering) {
        return blacken(doFrom(tree, from, fromInclusive, ordering));
    }

    static <A, B> Tree<A, B> to (Tree<A, B> tree, A to, boolean toInclusive, Comparator<? super A> ordering) {
        return blacken(doTo(tree, to, toInclusive, ordering));
    }

    static <A, B> Tree<A, B> drop (Tree<A, B> tree, int n) {
        return blacken(doDrop(tree, n));
    }

    static <A, B> Tree<A, B> take (Tree<A, B> tree, int n) {
        return blacken(doTake(tree, n));
    }

    static <A, B> Tree<A, B> slice (Tree<A, B> tree, int from, int until) {
        return blacken(doSlice(tree, from, until));
    }

    static <A, B> Tree<A, B> smallest (Tree<A, B> tree) {
        if (tree == null) return null;
        Tree<A, B> result = tree;
        while (result.left != null) result = result.left;
        return result;
    }

    static <A, B> Tree<A, B> greatest (Tree<A, B> tree) {
        if (tree == null) return null;
        Tree<A, B> result = tree;
        while (result.right != null) result = result.right;
        return result;
    }

    static <A, B> AIterator<Map.Entry<A, B>> iterator (Tree<A, B> tree, AOption<A> from, boolean fromInclusive, AOption<A> to, boolean toInclusive, Comparator<? super A> ordering) {
        return new EntriesIterator<>(tree, from, fromInclusive, to, toInclusive, ordering);
    }

    static <A> AIterator<A> keysIterator (Tree<A, ?> tree, AOption<A> from, boolean fromInclusive, AOption<A> to, boolean toInclusive, Comparator<? super A> ordering) {
        return new KeysIterator<>(tree, from, fromInclusive, to, toInclusive, ordering);
    }

    static <A, B> AIterator<B> valuesIterator (Tree<A, B> tree, AOption<A> from, boolean fromInclusive, AOption<A> to, boolean toInclusive, Comparator<? super A> ordering) {
        return new ValuesIterator<>(tree, from, fromInclusive, to, toInclusive, ordering);
    }

    static <A, B> AIterator<Map.Entry<A, B>> reverseIterator (Tree<A, B> tree, AOption<A> from, boolean fromInclusive, AOption<A> to, boolean toInclusive, Comparator<? super A> ordering) {
        return new ReverseEntriesIterator<>(tree, from, fromInclusive, to, toInclusive, ordering);
    }

    static <A, B> AIterator<A> reverseKeysIterator (Tree<A, B> tree, AOption<A> from, boolean fromInclusive, AOption<A> to, boolean toInclusive, Comparator<? super A> ordering) {
        return new ReverseKeysIterator<>(tree, from, fromInclusive, to, toInclusive, ordering);
    }

    private static boolean isRedTree (Tree<?, ?> tree) {
        return tree instanceof RedTree;
    } //TODO polymorphic 'isRed()'?

    private static boolean isBlackTree (Tree<?, ?> tree) {
        return tree instanceof BlackTree;
    } //TODO polymorphic 'isBlack()'?

    private static <A, B> Tree<A, B> blacken (Tree<A, B> t) {
        if (t == null) return null;
        return t.black();
    }

    private static <A, B> Tree<A, B> mkTree (boolean isBlack, A k, B v, Tree<A, B> l, Tree<A, B> r) {
        if (isBlack) return new BlackTree<>(k, v, l, r);
        else return new RedTree<>(k, v, l, r);
    }

    private static <A, B> Tree<A, B> balanceLeft (boolean isBlack, A z, B zv, Tree<A, B> l, Tree<A, B> d) {
        if (isRedTree(l) && isRedTree(l.left))
            return new RedTree<>(l.key, l.value, new BlackTree<>(l.left.key, l.left.value, l.left.left, l.left.right), new BlackTree<>(z, zv, l.right, d));
        if (isRedTree(l) && isRedTree(l.right))
            return new RedTree<>(l.right.key, l.right.value, new BlackTree<>(l.key, l.value, l.left, l.right.left), new BlackTree<>(z, zv, l.right.right, d));
        return mkTree(isBlack, z, zv, l, d);
    }

    private static <A, B> Tree<A, B> balanceRight (boolean isBlack, A x, B xv, Tree<A, B> a, Tree<A, B> r) {
        if (isRedTree(r) && isRedTree(r.left))
            return new RedTree<>(r.left.key, r.left.value, new BlackTree<>(x, xv, a, r.left.left), new BlackTree<>(r.key, r.value, r.left.right, r.right));
        if (isRedTree(r) && isRedTree(r.right))
            return new RedTree<>(r.key, r.value, new BlackTree<>(x, xv, a, r.left), new BlackTree<>(r.right.key, r.right.value, r.right.left, r.right.right));
        return mkTree(isBlack, x, xv, a, r);
    }

    private static <A, B> Tree<A, B> upd (Tree<A, B> tree, A k, B v, boolean overwrite, Comparator<? super A> ordering) {
        if (tree == null) return new RedTree<>(k, v, null, null);
        final int cmp = ordering.compare(k, tree.key);
        if (cmp < 0) return balanceLeft(isBlackTree(tree), tree.key, tree.value, upd(tree.left, k, v, overwrite, ordering), tree.right);
        if (cmp > 0) return balanceRight(isBlackTree(tree), tree.key, tree.value, tree.left, upd(tree.right, k, v, overwrite, ordering));
        if (overwrite || !Objects.equals(k, tree.key)) return mkTree(isBlackTree(tree), k, v, tree.left, tree.right); //TODO the 'equals' comparison should be superfluous
        return tree;
    }

    private static <A, B> Tree<A, B> updNth (Tree<A, B> tree, int idx, A k, B v, boolean overwrite) {
        if (tree == null) return new RedTree<>(k, v, null, null);
        final int rank = count(tree.left) + 1;
        if (idx < rank) return balanceLeft(isBlackTree(tree), tree.key, tree.value, updNth(tree.left, idx, k, v, overwrite), tree.right);
        if (idx > rank)
            return balanceRight(isBlackTree(tree), tree.key, tree.value, tree.left, updNth(tree.right, idx - rank, k, v, overwrite));
        if (overwrite) return mkTree(isBlackTree(tree), k, v, tree.left, tree.right);
        return tree;
    }

    /* Based on Stefan Kahrs' Haskell version of Okasaki's Red&Black Trees
     * Constructing Red-Black Trees, Ralf Hinze: [[http://www.cs.ox.ac.uk/ralf.hinze/publications/WAAAPL99b.ps.gz]]
     * Red-Black Trees in a Functional Setting, Chris Okasaki: [[https://wiki.rice.edu/confluence/download/attachments/2761212/Okasaki-Red-Black.pdf]]
     *
     * Inner methods are prefixed 'del_'
     */
    private static <A, B> Tree<A, B> del_balance (A x, B xv, Tree<A, B> tl, Tree<A, B> tr) {
        if (isRedTree(tl)) {
            if (isRedTree(tr)) return new RedTree<>(x, xv, tl.black(), tr.black());
            if (isRedTree(tl.left)) return new RedTree<>(tl.key, tl.value, tl.left.black(), new BlackTree<>(x, xv, tl.right, tr));
            if (isRedTree(tl.right))
                return new RedTree<>(tl.right.key, tl.right.value, new BlackTree<>(tl.key, tl.value, tl.left, tl.right.left), new BlackTree<>(x, xv, tl.right.right, tr));
            return new BlackTree<>(x, xv, tl, tr);
        }
        if (isRedTree(tr)) {
            if (isRedTree(tr.right)) return new RedTree<>(tr.key, tr.value, new BlackTree<>(x, xv, tl, tr.left), tr.right.black());
            if (isRedTree(tr.left))
                return new RedTree<>(tr.left.key, tr.left.value, new BlackTree<>(x, xv, tl, tr.left.left), new BlackTree<>(tr.key, tr.value, tr.left.right, tr.right));
            return new BlackTree<>(x, xv, tl, tr);
        }
        return new BlackTree<>(x, xv, tl, tr);
    }

    private static <A, B> Tree<A, B> del_subl (Tree<A, B> t) {
        if (! (t instanceof BlackTree)) throw new IllegalStateException("Defect: invariance violation; expected black, got " + t);
        return t.red();
    }

    private static <A, B> Tree<A, B> del_balLeft (A x, B xv, Tree<A, B> tl, Tree<A, B> tr) {
        if (isRedTree(tl)) return new RedTree<>(x, xv, tl.black(), tr);
        if (isBlackTree(tr)) return del_balance(x, xv, tl, tr.red());
        if (isRedTree(tr) && isBlackTree(tr.left))
            return new RedTree<>(tr.left.key, tr.left.value, new BlackTree<>(x, xv, tl, tr.left.left), del_balance(tr.key, tr.value, tr.left.right, del_subl(tr.right)));
        throw new IllegalStateException("Defect: invariance violation");
    }

    private static <A, B> Tree<A, B> del_balRight (A x, B xv, Tree<A, B> tl, Tree<A, B> tr) {
        if (isRedTree(tr)) return new RedTree<>(x, xv, tl, tr.black());
        if (isBlackTree(tl)) return del_balance(x, xv, tl.red(), tr);
        if (isRedTree(tl) && isBlackTree(tl.right))
            return new RedTree<>(tl.right.key, tl.right.value, del_balance(tl.key, tl.value, del_subl(tl.left), tl.right.left), new BlackTree<>(x, xv, tl.right.right, tr));
        throw new IllegalStateException("Defect: invariance violation");
    }

    private static <A, B> Tree<A, B> del_append (Tree<A, B> tl, Tree<A, B> tr) {
        if (tl == null) return tr;
        if (tr == null) return tl;
        if (isRedTree(tl) && isRedTree(tr)) {
            final Tree<A, B> bc = del_append(tl.right, tr.left);
            if (isRedTree(bc)) return new RedTree<>(bc.key, bc.value, new RedTree<>(tl.key, tl.value, tl.left, bc.left), new RedTree<>(tr.key, tr.value, bc.right, tr.right));
            else return new RedTree<>(tl.key, tl.value, tl.left, new RedTree<>(tr.key, tr.value, bc, tr.right));
        }
        if (isBlackTree(tl) && isBlackTree(tr)) {
            final Tree<A, B> bc = del_append(tl.right, tr.left);
            if (isRedTree(bc)) return new RedTree<>(bc.key, bc.value, new BlackTree<>(tl.key, tl.value, tl.left, bc.left), new BlackTree<>(tr.key, tr.value, bc.right, tr.right));
            else return del_balLeft(tl.key, tl.value, tl.left, new BlackTree<>(tr.key, tr.value, bc, tr.right));
        }
        if (isRedTree(tr)) return new RedTree<>(tr.key, tr.value, del_append(tl, tr.left), tr.right);
        if (isRedTree(tl)) return new RedTree<>(tl.key, tl.value, tl.left, del_append(tl.right, tr));
        throw new IllegalStateException("unmatched tree on append: " + tl + ", " + tr);
    }

    private static <A, B> Tree<A, B> del (Tree<A, B> tree, A k, Comparator<? super A> ordering) {
        if (tree == null) return null;

        final int cmp = ordering.compare(k, tree.key);
        if (cmp < 0) {
            if (isBlackTree(tree.left)) return del_balLeft(tree.key, tree.value, del(tree.left, k, ordering), tree.right);
            else return new RedTree<>(tree.key, tree.value, del(tree.left, k, ordering), tree.right);
        }
        if (cmp > 0) {
            if (isBlackTree(tree.right)) return del_balRight(tree.key, tree.value, tree.left, del(tree.right, k, ordering));
            else return new RedTree<>(tree.key, tree.value, tree.left, del(tree.right, k, ordering));
        }
        return del_append(tree.left, tree.right);
    }

    private static <A, B> Tree<A, B> doFrom (Tree<A, B> tree, A from, boolean fromInclusive, Comparator<? super A> ordering) {
        if (tree == null) return null;
        if ( fromInclusive && ordering.compare(tree.key, from) <  0) return doFrom(tree.right, from, fromInclusive, ordering);
        if (!fromInclusive && ordering.compare(tree.key, from) <= 0) return doFrom(tree.right, from, fromInclusive, ordering);
        final Tree<A, B> newLeft = doFrom(tree.left, from, fromInclusive, ordering);
        if (newLeft == tree.left) return tree;
        if (newLeft == null) return upd(tree.right, tree.key, tree.value, false, ordering);
        return rebalance(tree, newLeft, tree.right);
    }

    private static <A, B> Tree<A, B> doTo (Tree<A, B> tree, A to, boolean toInclusive, Comparator<? super A> ordering) {
        if (tree == null) return null;
        if ( toInclusive && ordering.compare(to, tree.key) <  0) return doTo(tree.left, to, toInclusive, ordering);
        if (!toInclusive && ordering.compare(to, tree.key) <= 0) return doTo(tree.left, to, toInclusive, ordering);
        final Tree<A, B> newRight = doTo(tree.right, to, toInclusive, ordering);
        if (newRight == tree.right) return tree;
        if (newRight == null) return upd(tree.left, tree.key, tree.value, false, ordering);
        return rebalance(tree, tree.left, newRight);
    }

    private static <A, B> Tree<A, B> doRange (Tree<A, B> tree, A from, boolean fromInclusive, A to, boolean toInclusive, Comparator<? super A> ordering) {
        if (tree == null) return null;
        if ( fromInclusive && ordering.compare(tree.key, from) <  0) return doRange(tree.right, from, fromInclusive, to, toInclusive, ordering);
        if (!fromInclusive && ordering.compare(tree.key, from) <= 0) return doRange(tree.right, from, fromInclusive, to, toInclusive, ordering);
        if ( toInclusive && ordering.compare(to, tree.key) <  0) return doRange(tree.left, from, fromInclusive, to, toInclusive, ordering);
        if (!toInclusive && ordering.compare(to, tree.key) <= 0) return doRange(tree.left, from, fromInclusive, to, toInclusive, ordering);
        final Tree<A, B> newLeft = doFrom(tree.left, from, fromInclusive, ordering);
        final Tree<A, B> newRight = doTo(tree.right, to, toInclusive, ordering);
        if (newLeft == tree.left && newRight == tree.right) return tree;
        if (newLeft == null) return upd(newRight, tree.key, tree.value, false, ordering);
        if (newRight == null) return upd(newLeft, tree.key, tree.value, false, ordering);
        return rebalance(tree, newLeft, newRight);
    }

    private static <A, B> Tree<A, B> doDrop (Tree<A, B> tree, int n) {
        if (n <= 0) return tree;
        if (n >= count(tree)) return null;
        final int count = count(tree.left);
        if (n > count) return doDrop(tree.right, n - count - 1);
        final Tree<A, B> newLeft = doDrop(tree.left, n);
        if (newLeft == tree.left) return tree;
        if (newLeft == null) return updNth(tree.right, n - count - 1, tree.key, tree.value, false);
        return rebalance(tree, newLeft, tree.right);
    }

    private static <A, B> Tree<A, B> doTake (Tree<A, B> tree, int n) {
        if (n <= 0) return null;
        if (n >= count(tree)) return tree;
        final int count = count(tree.left);
        if (n <= count) return doTake(tree.left, n);
        final Tree<A, B> newRight = doTake(tree.right, n - count - 1);
        if (newRight == tree.right) return tree;
        if (newRight == null) return updNth(tree.left, n, tree.key, tree.value, false);
        return rebalance(tree, tree.left, newRight);
    }

    private static <A, B> Tree<A, B> doSlice (Tree<A, B> tree, int from, int until) {
        if (tree == null) return null;
        final int count = count(tree.left);
        if (from > count) return doSlice(tree.right, from - count - 1, until - count - 1);
        if (until <= count) return doSlice(tree.left, from, until);
        final Tree<A, B> newLeft = doDrop(tree.left, from);
        final Tree<A, B> newRight = doTake(tree.right, until - count - 1);
        if (newLeft == tree.left && newRight == tree.right) return tree;
        if (newLeft == null) return updNth(newRight, from - count - 1, tree.key, tree.value, false);
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
    private static <A, B> CompareDepthResult<A, B> compareDepth (Tree<A, B> left, Tree<A, B> right) {
        return cd_unzipBoth(left, right);
    }

    private static class CompareDepthResult<A, B> {
        final NList<Tree<A, B>> zipper;
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
    private static <A, B> NList<Tree<A, B>> cd_unzip (NList<Tree<A, B>> zipper, boolean leftMost) {
        Tree<A, B> next;
        while ((next = leftMost ? zipper.head.left : zipper.head.right) != null) {
            zipper = new NList<>(next, zipper);
        }
        return zipper;
    }

    // Unzip left tree on the rightmost side and right tree on the leftmost side until one is
    // found to be deeper, or the bottom is reached
    private static <A, B> CompareDepthResult<A, B> cd_unzipBoth (Tree<A, B> left, Tree<A, B> right) {
        NList<Tree<A, B>> leftZipper = null;
        NList<Tree<A, B>> rightZipper = null;
        int smallerDepth = 0;

        while (true) {
            if (isBlackTree(left) && isBlackTree(right)) {
                left = left.right;
                right = right.left;
                leftZipper = new NList<>(left, leftZipper);
                rightZipper = new NList<>(right, rightZipper);
                smallerDepth += 1;
            }
            else if (isRedTree(left) && isRedTree(right)) {
                left = left.right;
                right = right.left;
                leftZipper = new NList<>(left, leftZipper);
                rightZipper = new NList<>(right, rightZipper);
            }
            else if (isRedTree(right)) {
                right = right.left;
                rightZipper = new NList<>(right, rightZipper);
            }
            else if (isRedTree(left)) { //TODO different structure in Scala bytecode
                left = left.right;
                leftZipper = new NList<>(left, leftZipper);
            }
            else {
                if (left == null && right == null) return new CompareDepthResult<>(null, true, false, smallerDepth);
                if (left == null && isBlackTree(right)) {
                    final boolean leftMost = true;
                    return new CompareDepthResult<>(cd_unzip(new NList<>(right, rightZipper), leftMost), false, leftMost, smallerDepth);
                }
                if (isBlackTree(left) && right == null) {
                    final boolean leftMost = false;
                    return new CompareDepthResult<>(cd_unzip(new NList<>(left, leftZipper), leftMost), false, leftMost, smallerDepth);
                }
                throw new IllegalStateException("unmatched trees in unzip: " + left + ", " + right);
            }
        }
    }

    // This is like drop(n-1), but only counting black nodes
    private static <A, B> NList<Tree<A, B>> rebalance_findDepth (NList<Tree<A, B>> zipper, int depth) {
        while (zipper != null) {
            if (isBlackTree(zipper.head)) {
                if (depth == 1)
                    return zipper;
                zipper = zipper.tail;
                depth -= 1;
            } else {
                zipper = zipper.tail;
            }
        }
        throw new IllegalStateException("Defect: unexpected empty zipper while computing range");
    }

    private static <A, B> Tree<A, B> rebalance (Tree<A, B> tree, Tree<A, B> newLeft, Tree<A, B> newRight) {
        // Blackening the smaller tree avoids balancing problems on union;
        // this can't be done later, though, or it would change the result of compareDepth
        final Tree<A, B> blkNewLeft = blacken(newLeft);
        final Tree<A, B> blkNewRight = blacken(newRight);
        final CompareDepthResult<A, B> cdResult = compareDepth(blkNewLeft, blkNewRight);

        if (cdResult.levelled) return new BlackTree<>(tree.key, tree.value, blkNewLeft, blkNewRight);

        final NList<Tree<A, B>> zipFrom = rebalance_findDepth(cdResult.zipper, cdResult.smallerDepth);
        final Tree<A, B> union;
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

        static <A, B> B foldLeft (NList<A> xs, B z, BiFunction<B, A, B> op) {
            B acc = z;
            NList<A> these = xs;
            while (these != null) {
                acc = op.apply(acc, these.head);
                these = these.tail;
            }
            return acc;
        }
    }

    static abstract class Tree<A, B> implements Serializable {
        final A key;
        final B value;
        final Tree<A, B> left, right;
        final int count;

        Tree (A key, B value, Tree<A, B> left, Tree<A, B> right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            this.count = 1 + count(left) + count(right);
        }

        Map.Entry<A,B> entry() {
            return new AbstractMap.SimpleImmutableEntry<>(key, value); //TODO make Tree implement Entry
        }

        abstract Tree<A, B> black ();
        abstract Tree<A, B> red ();
    }

    static class RedTree<A, B> extends Tree<A, B> {
        RedTree (A key, B value, Tree<A, B> left, Tree<A, B> right) {
            super(key, value, left, right);
        }

        @Override Tree<A, B> black () {
            return new BlackTree<>(key, value, left, right);
        }
        @Override Tree<A, B> red () {
            return this;
        }

        @Override
        public String toString () {
            return "RedTree{" +
                    "key=" + key +
                    ", value=" + value +
                    ", left=" + left +
                    ", right=" + right +
                    "}";
        }
    }

    static class BlackTree<A, B> extends Tree<A, B> {
        BlackTree (A key, B value, Tree<A, B> left, Tree<A, B> right) {
            super(key, value, left, right);
        }

        @Override
        Tree<A, B> black () {
            return this;
        }

        @Override
        Tree<A, B> red () {
            return new RedTree<>(key, value, left, right);
        }

        @Override
        public String toString () {
            return "BlackTree{" +
                    "key=" + key +
                    ", value=" + value +
                    ", left=" + left +
                    ", right=" + right +
                    "}";
        }
    }

    private static abstract class TreeIterator<A, B, R> extends AbstractAIterator<R> {
        private final AOption<A> to;
        private final boolean toInclusive;
        private final Comparator<? super A> ordering;

        private final ArrayDeque<Tree<A,B>> stackOfNexts = new ArrayDeque<>();
        private Tree<A, B> lookahead;

        TreeIterator (Tree<A, B> root, AOption<A> from, boolean fromInclusive, AOption<A> to, boolean toInclusive, Comparator<? super A> ordering) {
            this.to = to;
            this.toInclusive = toInclusive;
            this.ordering = ordering;
            this.lookahead = checkUpperBoundForLookahead(from.map(f -> startFrom(root, f, fromInclusive)).orElseGet(() -> findLeftMostOrPopOnEmpty(root)));
        }

        private Tree<A,B> checkUpperBoundForLookahead(Tree<A,B> newLookahead) {
            if (to.isEmpty() || newLookahead == null) return newLookahead;
            final int cmp = ordering.compare(newLookahead.key, to.get());
            if (toInclusive && cmp <= 0) return newLookahead;
            if (!toInclusive && cmp < 0) return newLookahead;
            return null;
        }

        abstract R nextResult (Tree<A, B> tree);

        @Override public boolean hasNext () {
            return lookahead != null;
        }

        @Override public R next () {
            if (lookahead == null) throw new NoSuchElementException("next on empty iterator");
            final Tree<A, B> oldLookahead = lookahead;
            lookahead = checkUpperBoundForLookahead(findLeftMostOrPopOnEmpty(goRight(oldLookahead)));
            return nextResult(oldLookahead);
        }

        private Tree<A, B> findLeftMostOrPopOnEmpty (Tree<A, B> tree) {
            while (tree != null) {
                if (tree.left == null) return tree;
                tree = goLeft(tree);
            }
            return popNext();
        }

        private Tree<A, B> popNext () {
            if (stackOfNexts.isEmpty()) return null;
            return stackOfNexts.pop();
        }


        /**
         * Find the leftmost subtree whose key is equal to the given key, or if no such thing,
         * the leftmost subtree with the key that would be "next" after it according
         * to the ordering. Along the way build up the iterator's path stack so that "next"
         * functionality works.
         */
        private Tree<A, B> startFrom (Tree<A,B> root, A key, boolean inclusive) {
            if (root == null) return null;

            Tree<A, B> tree = root;
            while (true) { // migrated @tailrec
                if (tree == null) return popNext();
                if (inclusive && ordering.compare(key, tree.key) <= 0) tree = goLeft(tree);
                else if (!inclusive && ordering.compare(key, tree.key) < 0) tree = goLeft(tree);
                else tree = goRight(tree);
            }
        }

        private Tree<A, B> goLeft (Tree<A, B> tree) {
            stackOfNexts.push(tree);
            return tree.left;
        }

        private Tree<A, B> goRight (Tree<A, B> tree) {
            return tree.right;
        }
    }

    private static class EntriesIterator<A, B> extends TreeIterator<A, B, Map.Entry<A, B>> {
        EntriesIterator (Tree<A, B> root, AOption<A> from, boolean fromInclusive, AOption<A> to, boolean toInclusive, Comparator<? super A> ordering) {
            super(root, from, fromInclusive, to, toInclusive, ordering);
        }

        @Override  Map.Entry<A, B> nextResult (Tree<A, B> tree) {
            return tree.entry();
        }
    }

    private static class KeysIterator<A, B> extends TreeIterator<A, B, A> {
        KeysIterator (Tree<A, B> root, AOption<A> from, boolean fromInclusive, AOption<A> to, boolean toInclusive, Comparator<? super A> ordering) {
            super(root, from, fromInclusive, to, toInclusive, ordering);
        }

        @Override A nextResult (Tree<A, B> tree) {
            return tree.key;
        }
    }

    private static class ValuesIterator<A, B> extends TreeIterator<A, B, B> {
        ValuesIterator (Tree<A, B> root, AOption<A> from, boolean fromInclusive, AOption<A> to, boolean toInclusive, Comparator<? super A> ordering) {
            super(root, from, fromInclusive, to, toInclusive, ordering);
        }

        @Override B nextResult (Tree<A, B> tree) {
            return tree.value;
        }
    }

    private static abstract class ReverseTreeIterator<A, B, R> extends AbstractAIterator<R> {
        private final AOption<A> to;
        private final boolean toInclusive;
        private final Comparator<? super A> ordering;

        private final ArrayDeque<Tree<A,B>> stackOfNexts = new ArrayDeque<>();
        private Tree<A, B> lookahead;

        ReverseTreeIterator (Tree<A, B> root, AOption<A> from, boolean fromInclusive, AOption<A> to, boolean toInclusive, Comparator<? super A> ordering) {
            this.to = to;
            this.toInclusive = toInclusive;
            this.ordering = ordering;
            this.lookahead = checkUpperBoundForLookahead(from.map(f -> startFrom(root, f, fromInclusive)).orElseGet(() -> findRightMostOrPopOnEmpty(root)));
        }

        private Tree<A,B> checkUpperBoundForLookahead(Tree<A,B> newLookahead) {
            if (to.isEmpty()) return newLookahead;
            final int cmp = ordering.compare(newLookahead.key, to.get());
            if (toInclusive && cmp <= 0) return newLookahead;
            if (!toInclusive && cmp < 0) return newLookahead;
            return null;
        }

        @Override public boolean hasNext () {
            return lookahead != null;
        }

        @Override public R next () {
            if (lookahead == null) throw new NoSuchElementException("next on empty iterator");
            final Tree<A, B> oldLookahead = lookahead;
            lookahead = checkUpperBoundForLookahead(findRightMostOrPopOnEmpty(goLeft(oldLookahead)));
            return nextResult(oldLookahead);
        }

        abstract R nextResult (Tree<A, B> tree);

        private Tree<A, B> findRightMostOrPopOnEmpty (Tree<A, B> tree) {
            while (tree != null) {
                if (tree.right == null) return tree;
                tree = goRight(tree);
            }
            return popNext();
        }

        private Tree<A, B> popNext () {
            if (stackOfNexts.isEmpty()) return null;
            return stackOfNexts.pop();
        }

        private Tree<A, B> startFrom (Tree<A,B> root, A key, boolean inclusive) {
            if (root == null) return null;

            Tree<A, B> tree = root;
            while (true) { // migrated @tailrec
                if (tree == null) return popNext();
                if (inclusive && ordering.compare(key, tree.key) >= 0) tree = goRight(tree);
                else if (!inclusive && ordering.compare(key, tree.key) > 0) tree = goRight(tree);
                else tree = goLeft(tree);
            }
        }

        private Tree<A, B> goLeft (Tree<A, B> tree) {
            return tree.left;
        }

        private Tree<A, B> goRight (Tree<A, B> tree) {
            stackOfNexts.push(tree);
            return tree.right;
        }
    }

    private static class ReverseEntriesIterator<A,B> extends ReverseTreeIterator<A,B,Map.Entry<A,B>> {
        ReverseEntriesIterator (Tree<A, B> root, AOption<A> from, boolean fromInclusive, AOption<A> to, boolean toInclusive, Comparator<? super A> ordering) {
            super(root, from, fromInclusive, to, toInclusive, ordering);
        }

        @Override Map.Entry<A, B> nextResult (Tree<A, B> tree) {
            return tree.entry();
        }
    }

    private static class ReverseKeysIterator<A,B> extends ReverseTreeIterator<A,B,A> {
        public ReverseKeysIterator (Tree<A, B> root, AOption<A> from, boolean fromInclusive, AOption<A> to, boolean toInclusive, Comparator<? super A> ordering) {
            super(root, from, fromInclusive, to, toInclusive, ordering);
        }

        @Override A nextResult (Tree<A, B> tree) {
            return tree.key;
        }
    }
}
