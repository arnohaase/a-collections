package com.ajjpj.acollections.immutable.rbs;

import com.ajjpj.acollections.util.AOption;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class RedBlackTree {
    public static boolean isEmpty(final Tree<?, ?> tree) {
        return tree == null;
    }

    public static <A> boolean contains(final Tree<A, ?> tree, final A x, final Comparator<A> evidence$1) {
        return lookup(tree, x, evidence$1) != null;
    }

    public static <A, B> AOption<B> get(final Tree<A, B> tree, final A x, final Comparator<A> evidence$2) {
        Tree<A,B> var5 = lookup(tree, x, evidence$2);
        AOption<B> var4;
        if (var5 == null) {
            var4 = AOption.none();
        } else {
            var4 = AOption.some(var5.value());
        }

        return var4;
    }

    public static <A, B> Tree<A, B> lookup(Tree<A, B> tree, final A x, final Comparator<A> ordering) {
        while(tree != null) {
            int cmp = ordering.compare(x, tree.key());
            Tree var10000;
            if (cmp < 0) {
                var10000 = tree.left();
                tree = var10000;
            } else {
                if (cmp <= 0) {
                    return tree;
                }

                var10000 = tree.right();
                tree = var10000;
            }
        }

        return null;
    }

    public static int count(final Tree<?, ?> tree) {
        return tree == null ? 0 : tree.count();
    }

    public static <A> int countInRange(Tree<A, ?> tree, final AOption<A> from, final AOption<A> to, final Comparator<A> ordering) {
        while(tree != null) {
            int var5;
            if (from.isEmpty() && to.isEmpty()) {
                var5 = tree.count();
            } else {
                if (from.isDefined()) {
                    A lb = from.get();
                    if (ordering.compare(tree.key(), lb) < 0) {
                        tree = tree.right();
                        continue;
                    }
                }

                if (to.isDefined()) {
                    A ub = to.get();
                    if (ordering.compare(tree.key(), ub) >= 0) {
                        tree = tree.left();
                        continue;
                    }
                }

                var5 = 1 + countInRange(tree.left(), from, AOption.none(), ordering) + countInRange(tree.right(), AOption.none(), to, ordering);
            }

            return var5;
        }

        return 0;
    }

    public static <A, B> Tree<A, B> update(final Tree<A, B> tree, final A k, final B v, final boolean overwrite, final Comparator<A> evidence$3) {
        return blacken(upd(tree, k, v, overwrite, evidence$3));
    }

    public static <A, B> Tree<A, B> delete(final Tree<A, B> tree, final A k, final Comparator<A> evidence$4) {
        return blacken(del(tree, k, evidence$4));
    }

    public static <A, B> Tree<A, B> rangeImpl(final Tree<A, B> tree, final AOption<A> from, final AOption<A> until, final Comparator<A> evidence$5) {
        if (from.isDefined()) {
            A _from = from.get();
            if (until.isDefined()) {
                A _until = until.get();
                return range(tree, _from, _until, evidence$5);
            }
        }

        if (from.isDefined()) {
            A _from = from.get();
            if (until.isEmpty()) {
                return from(tree, _from, evidence$5);
            }
        }

        if (from.isEmpty() && until.isDefined()) {
            A _until = until.get();
            return until(tree, _until, evidence$5);
        }

        return tree;
    }

    public static <A, B> Tree<A, B> range(final Tree<A, B> tree, final A from, final A until, final Comparator<A> evidence$6) {
        return blacken(doRange(tree, from, until, evidence$6));
    }

    public static <A, B> Tree<A, B> from(final Tree<A, B> tree, final A from, final Comparator<A> evidence$7) {
        return blacken(doFrom(tree, from, evidence$7));
    }

    public static <A, B> Tree<A, B> to(final Tree<A, B> tree, final A to, final Comparator<A> evidence$8) {
        return blacken(doTo(tree, to, evidence$8));
    }

    public static <A, B> Tree<A, B> until(final Tree<A, B> tree, final A key, final Comparator<A> evidence$9) {
        return blacken(doUntil(tree, key, evidence$9));
    }

    public static <A, B> Tree<A, B> drop(final Tree<A, B> tree, final int n, final Comparator<A> evidence$10) {
        return blacken(doDrop(tree, n));
    }

    public static <A, B> Tree<A, B> take(final Tree<A, B> tree, final int n, final Comparator<A> evidence$11) {
        return blacken(doTake(tree, n));
    }

    public static <A, B> Tree<A, B> slice(final Tree<A, B> tree, final int from, final int until, final Comparator<A> evidence$12) {
        return blacken(doSlice(tree, from, until));
    }

    public static <A, B> Tree<A, B> smallest(final Tree<A, B> tree) {
        if (tree == null) {
            throw new NoSuchElementException("empty map");
        } else {
            Tree result;
            for(result = tree; result.left() != null; result = result.left()) {
                ;
            }

            return result;
        }
    }

    public static <A, B> Tree<A, B> greatest(final Tree<A, B> tree) {
        if (tree == null) {
            throw new NoSuchElementException("empty map");
        } else {
            Tree result;
            for(result = tree; result.right() != null; result = result.right()) {
                ;
            }

            return result;
        }
    }

    public static <A, B, U> void foreach(final Tree<A, B> tree, final BiFunction<A, B, U> f) {
        if (tree != null) {
            _foreach(tree, f);
        }

    }

    private static <A, B, U> void _foreach(Tree<A, B> tree, final BiFunction<A, B, U> f) {
        while(true) {
            if (tree.left() != null) {
                _foreach(tree.left(), f);
            }

            f.apply(tree.key(), tree.value());
            if (tree.right() == null) {
                return;
            }

            tree = tree.right();
        }
    }

    public static <A, U> void foreachKey(final Tree<A, ?> tree, final Function<A, U> f) {
        if (tree != null) {
            _foreachKey(tree, f);
        }

    }

    private static <A, U> void _foreachKey(Tree<A, ?> tree, final Function<A, U> f) {
        while(true) {
            if (tree.left() != null) {
                _foreachKey(tree.left(), f);
            }

            f.apply(tree.key());
            if (tree.right() == null) {
                return;
            }

            tree = tree.right();
        }
    }

    public static <A, B> Iterator<Map.Entry<A, B>> iterator(final Tree<A, B> tree, final AOption<A> start, final Comparator<A> evidence$13) {
        return new EntriesIterator(tree, start, evidence$13);
    }

    public static <A> Iterator<A> keysIterator(final Tree<A, ?> tree, final AOption<A> start, final Comparator<A> evidence$14) {
        return new KeysIterator(tree, start, evidence$14);
    }

    public static <A, B> Iterator<B> valuesIterator(final Tree<A, B> tree, final AOption<A> start, final Comparator<A> evidence$15) {
        return new ValuesIterator(tree, start, evidence$15);
    }

    public static <A, B> Tree<A, B> nth(Tree<A, B> tree, int n) {
        while(true) {
            int count = count(tree.left());
            Tree var10000;
            if (n < count) {
                var10000 = tree.left();
                tree = var10000;
            } else {
                if (n <= count) {
                    return tree;
                }

                var10000 = tree.right();
                n = n - count - 1;
                tree = var10000;
            }
        }
    }

    public static boolean isBlack(final Tree<?, ?> tree) {
        return tree == null || isBlackTree(tree);
    }

    private static boolean isRedTree(final Tree<?, ?> tree) {
        return tree instanceof RedTree;
    }

    private static boolean isBlackTree(final Tree<?, ?> tree) {
        return tree instanceof BlackTree;
    }

    private static <A, B> Tree<A, B> blacken(final Tree<A, B> t) {
        return t == null ? null : t.black();
    }

    private static <A, B> Tree<A, B> mkTree(final boolean isBlack, final A k, final B v, final Tree<A, B> l, final Tree<A, B> r) {
        if (isBlack) {
            return new BlackTree<>(k, v, l, r);
        } else {
            return new RedTree<>(k, v, l, r);
        }
    }

    private static <A, B> Tree<A, B> balanceLeft(final boolean isBlack, final A z, final B zv, final Tree<A, B> l, final Tree<A, B> d) {
        Object var10001;
        Object var10002;
        Object var10004;
        Object var10005;
        Tree var10006;
        if (isRedTree(l) && isRedTree(l.left())) {
            var10004 = l.left().key();
            var10005 = l.left().value();
            var10006 = l.left().left();
            Object apply_value = var10005;
            Object apply_key = var10004;
            {
                BlackTree var10 = new BlackTree(apply_key, apply_value, var10006, l.left().right());
                {
                    BlackTree var12 = new BlackTree(z, zv, l.right(), d);
                    return new RedTree<>(l.key(), l.value(), var10, var12);
                }
            }
        } else if (isRedTree(l) && isRedTree(l.right())) {
            var10001 = l.right().key();
            var10002 = l.right().value();
            var10004 = l.key();
            var10005 = l.value();
            var10006 = l.left();
            Tree apply_right = l.right().left();
            Tree apply_left = var10006;
            Object apply_value = var10005;
            Object apply_key = var10004;
            BlackTree var19 = new BlackTree(apply_key, apply_value, apply_left, apply_right);
            BlackTree var21 = new BlackTree(z, zv, l.right().right(), d);
            return new RedTree(var10001, var10002, var19, var21);
        } else {
            return mkTree(isBlack, z, zv, l, d);
        }
    }

    private static <A, B> Tree<A, B> balanceRight(final boolean isBlack, final A x, final B xv, final Tree<A, B> a, final Tree<A, B> r) {
        Object var10001;
        Object var10002;
        Object var10004;
        Object var10005;
        Tree var10006;
        if (isRedTree(r) && isRedTree(r.left())) {
            var10001 = r.left().key();
            var10002 = r.left().value();
            Tree apply_right = r.left().left();
            BlackTree var7 = new BlackTree(x, xv, a, apply_right);
            var10004 = r.key();
            var10005 = r.value();
            var10006 = r.left().right();
            Tree apply_left = var10006;
            Object apply_value = var10005;
            Object apply_key = var10004;
            BlackTree var12 = new BlackTree(apply_key, apply_value, apply_left, r.right());
            return new RedTree(var10001, var10002, var7, var12);
        } else if (isRedTree(r) && isRedTree(r.right())) {
            var10001 = r.key();
            var10002 = r.value();
            Tree apply_right = r.left();
            BlackTree var16 = new BlackTree(x, xv, a, apply_right);
            var10004 = r.right().key();
            var10005 = r.right().value();
            var10006 = r.right().left();
            BlackTree var21 = new BlackTree(var10004, var10005, var10006, r.right().right());
            return new RedTree(var10001, var10002, var16, var21);
        } else {
            return mkTree(isBlack, x, xv, a, r);
        }
    }

    private static <A, B> Tree<A,B> upd(final Tree<A, B> tree, final A k, final B v, final boolean overwrite, final Comparator<A> ordering) {
        if (tree == null) {
            return new RedTree<>(k, v, null, null);
        } else {
            int cmp = ordering.compare(k, tree.key());
            if (cmp < 0) {
                return balanceLeft(isBlackTree(tree), tree.key(), tree.value(), upd(tree.left(), k, v, overwrite, ordering), tree.right());
            } else if (cmp > 0) {
                return balanceRight(isBlackTree(tree), tree.key(), tree.value(), tree.left(), upd(tree.right(), k, v, overwrite, ordering));
            } else {
                return !overwrite && Objects.equals(k, tree.key()) ? tree : mkTree(isBlackTree(tree), k, v, tree.left(), tree.right());
            }
        }
    }

    private static <A, B> Tree<A,B> updNth(final Tree<A, B> tree, final int idx, final A k, final B v, final boolean overwrite) {
        if (tree == null) {
            return new RedTree<>(k, v, null, null);
        } else {
            int rank = count(tree.left()) + 1;
            if (idx < rank) {
                return balanceLeft(isBlackTree(tree), tree.key(), tree.value(), updNth(tree.left(), idx, k, v, overwrite), tree.right());
            } else if (idx > rank) {
                return balanceRight(isBlackTree(tree), tree.key(), tree.value(), tree.left(), updNth(tree.right(), idx - rank, k, v, overwrite));
            } else {
                return overwrite ? mkTree(isBlackTree(tree), k, v, tree.left(), tree.right()) : tree;
            }
        }
    }

    private static <A, B> Tree<A, B> del(final Tree<A, B> tree, final A k, final Comparator<A> ordering) {
        if (tree == null) {
            return null;
        } else {
            int cmp = ordering.compare(k, tree.key());
            if (cmp < 0) {
                return delLeft$1(tree, k, ordering);
            } else {
                return cmp > 0 ? delRight$1(tree, k, ordering) : append$1(tree.left(), tree.right());
            }
        }
    }

    private static <A, B> Tree<A, B> doFrom(final Tree<A, B> tree, final A from, final Comparator<A> ordering) {
        if (tree == null) {
            return null;
        } else if (ordering.compare(tree.key(), from) < 0) {
            return doFrom(tree.right(), from, ordering);
        } else {
            Tree<A,B> newLeft = doFrom(tree.left(), from, ordering);
            if (newLeft == tree.left()) {
                return tree;
            } else {
                return newLeft == null ? upd(tree.right(), tree.key(), tree.value(), false, ordering) : rebalance(tree, newLeft, tree.right());
            }
        }
    }

    private static <A, B> Tree<A, B> doTo(final Tree<A, B> tree, final A to, final Comparator<A> ordering) {
        if (tree == null) {
            return null;
        } else if (ordering.compare(to, tree.key()) < 0) {
            return doTo(tree.left(), to, ordering);
        } else {
            Tree<A,B> newRight = doTo(tree.right(), to, ordering);
            if (newRight == tree.right()) {
                return tree;
            } else {
                return newRight == null ? upd(tree.left(), tree.key(), tree.value(), false, ordering) : rebalance(tree, tree.left(), newRight);
            }
        }
    }

    private static <A, B> Tree<A, B> doUntil(final Tree<A, B> tree, final A until, final Comparator<A> ordering) {
        if (tree == null) {
            return null;
        } else if (ordering.compare(until, tree.key()) <= 0) {
            return doUntil(tree.left(), until, ordering);
        } else {
            Tree<A,B> newRight = doUntil(tree.right(), until, ordering);
            if (newRight == tree.right()) {
                return tree;
            } else {
                return newRight == null ? upd(tree.left(), tree.key(), tree.value(), false, ordering) : rebalance(tree, tree.left(), newRight);
            }
        }
    }

    private static <A, B> Tree<A, B> doRange(final Tree<A, B> tree, final A from, final A until, final Comparator<A> ordering) {
        if (tree == null) {
            return null;
        } else if (ordering.compare(tree.key(), from) < 0) {
            return doRange(tree.right(), from, until, ordering);
        } else if (ordering.compare(until, tree.key()) <= 0) {
            return doRange(tree.left(), from, until, ordering);
        } else {
            Tree newLeft = doFrom(tree.left(), from, ordering);
            Tree newRight = doUntil(tree.right(), until, ordering);
            if (newLeft == tree.left() && newRight == tree.right()) {
                return tree;
            } else if (newLeft == null) {
                return upd(newRight, tree.key(), tree.value(), false, ordering);
            } else {
                return newRight == null ? upd(newLeft, tree.key(), tree.value(), false, ordering) : rebalance(tree, newLeft, newRight);
            }
        }
    }

    private static <A, B> Tree<A, B> doDrop(final Tree<A, B> tree, final int n) {
        if (n <= 0) {
            return tree;
        } else if (n >= count(tree)) {
            return null;
        } else {
            int count = count(tree.left());
            if (n > count) {
                return doDrop(tree.right(), n - count - 1);
            } else {
                Tree newLeft = doDrop(tree.left(), n);
                if (newLeft == tree.left()) {
                    return tree;
                } else {
                    return newLeft == null ? updNth(tree.right(), n - count - 1, tree.key(), tree.value(), false) : rebalance(tree, newLeft, tree.right());
                }
            }
        }
    }

    private static <A, B> Tree<A, B> doTake(final Tree<A, B> tree, final int n) {
        if (n <= 0) {
            return null;
        } else if (n >= count(tree)) {
            return tree;
        } else {
            int count = count(tree.left());
            if (n <= count) {
                return doTake(tree.left(), n);
            } else {
                Tree newRight = doTake(tree.right(), n - count - 1);
                if (newRight == tree.right()) {
                    return tree;
                } else {
                    return newRight == null ? updNth(tree.left(), n, tree.key(), tree.value(), false) : rebalance(tree, tree.left(), newRight);
                }
            }
        }
    }

    private static <A, B> Tree<A, B> doSlice(final Tree<A, B> tree, final int from, final int until) {
        if (tree == null) {
            return null;
        } else {
            int count = count(tree.left());
            if (from > count) {
                return doSlice(tree.right(), from - count - 1, until - count - 1);
            } else if (until <= count) {
                return doSlice(tree.left(), from, until);
            } else {
                Tree newLeft = doDrop(tree.left(), from);
                Tree newRight = doTake(tree.right(), until - count - 1);
                if (newLeft == tree.left() && newRight == tree.right()) {
                    return tree;
                } else if (newLeft == null) {
                    return updNth(newRight, from - count - 1, tree.key(), tree.value(), false);
                } else {
                    return newRight == null ? updNth(newLeft, until, tree.key(), tree.value(), false) : rebalance(tree, newLeft, newRight);
                }
            }
        }
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

    private static <A, B> CompareDepthResult compareDepth(final Tree<A, B> left, final Tree<A, B> right) {
        return unzipBoth$1(left, right, null, null, 0);
    }

    private static <A, B> Tree<A, B> rebalance(final Tree<A, B> tree, final Tree<A, B> newLeft, final Tree<A, B> newRight) {
        Tree blkNewLeft = blacken(newLeft);
        Tree blkNewRight = blacken(newRight);
        CompareDepthResult var6 = compareDepth(blkNewLeft, blkNewRight);
        NList zipper = var6.zipper;
        boolean levelled = var6.levelled;
        boolean leftMost = var6.leftMost;
        int smallerDepth = var6.smallerDepth;
        if (levelled) {
            return new BlackTree(tree.key(), tree.value(), blkNewLeft, blkNewRight);
        } else {
            NList zipFrom = findDepth$1(zipper, smallerDepth);
            RedTree var26;
            if (leftMost) {
                Tree apply_right = (Tree)zipFrom.head();
                var26 = new RedTree(tree.key(), tree.value(), blkNewLeft, apply_right);
            } else {
                Tree apply_left = (Tree)zipFrom.head();
                var26 = new RedTree(tree.key(), tree.value(), apply_left, blkNewRight);
            }

            RedTree union = var26;
            NList foldLeft_xs = zipFrom.tail();
            Object foldLeft_acc = union;

            for(NList<Tree<A,B>> foldLeft_these = foldLeft_xs; foldLeft_these != null; foldLeft_these = foldLeft_these.tail()) {
                final Tree<A,B> node = foldLeft_these.head();
                foldLeft_acc = leftMost ? balanceLeft(isBlackTree(node), node.key, node.value, tree, node.right) : balanceRight(isBlackTree(node), node.key, node.value, node.left, tree);
            }

            return (Tree)foldLeft_acc;
        }
    }

    private static final Tree balance$1(final Object x, final Object xv, final Tree tl, final Tree tr) {
        if (isRedTree(tl)) {
            if (isRedTree(tr)) {
                Tree var35 = tl.black();
                Tree apply_right = tr.black();
                Tree apply_left = var35;
                return new RedTree(x, xv, apply_left, apply_right);
            } else if (isRedTree(tl.left())) {
                Tree var36 = tl.left().black();
                Tree apply_left = tl.right();
                BlackTree var8 = new BlackTree(x, xv, apply_left, tr);
                return new RedTree(tl.key(), tl.value(), var36, var8);
            } else if (isRedTree(tl.right())) {
                BlackTree var16 = new BlackTree(tl.key(), tl.value(), tl.left(), tl.right().left());
                BlackTree var18 = new BlackTree(x, xv, tl.right().right(), tr);
                return new RedTree(tl.right().key(), tl.right().value(), var16, var18);
            } else {
                return new BlackTree(x, xv, tl, tr);
            }
        } else if (isRedTree(tr)) {
            if (isRedTree(tr.right())) {
                BlackTree var22 = new BlackTree(x, xv, tl, tr.left());
                return new RedTree(tr.key(), tr.value(), var22, tr.right().black());
            } else if (isRedTree(tr.left())) {
                BlackTree var27 = new BlackTree(x, xv, tl, tr.left().left());
                BlackTree var32 = new BlackTree(tr.key(), tr.value(), tr.left().right(), tr.right());
                return new RedTree(tr.left().key(), tr.left().value(), var27, var32);
            } else {
                return new BlackTree(x, xv, tl, tr);
            }
        } else {
            return new BlackTree(x, xv, tl, tr);
        }
    }

    private static final Tree subl$1(final Tree t) {
        if (t instanceof BlackTree) {
            return t.red();
        } else {
            throw new IllegalStateException((new StringBuilder(50)).append("Defect: invariance violation; expected black, got ").append(t).toString());
        }
    }

    private static final Tree balLeft$1(final Object x, final Object xv, final Tree tl, final Tree tr) {
        if (isRedTree(tl)) {
            Tree apply_left = tl.black();
            return new RedTree(x, xv, apply_left, tr);
        } else if (isBlackTree(tr)) {
            return balance$1(x, xv, tl, tr.red());
        } else if (isRedTree(tr) && isBlackTree(tr.left())) {
            BlackTree var7 = new BlackTree(x, xv, tl, tr.left().left());
            return new RedTree(tr.left().key(), tr.left().value(), var7, balance$1(tr.key(), tr.value(), tr.left().right(), subl$1(tr.right())));
        } else {
            throw new IllegalStateException("Defect: invariance violation");
        }
    }

    private static final Tree balRight$1(final Object x, final Object xv, final Tree tl, final Tree tr) {
        if (isRedTree(tr)) {
            Tree apply_right = tr.black();
            return new RedTree(x, xv, tl, apply_right);
        } else if (isBlackTree(tl)) {
            return balance$1(x, xv, tl.red(), tr);
        } else if (isRedTree(tl) && isBlackTree(tl.right())) {
            Tree var10003 = balance$1(tl.key(), tl.value(), subl$1(tl.left()), tl.right().left());
            BlackTree var7 = new BlackTree(x, xv, tl.right().right(), tr);
            return new RedTree(tl.right().key(), tl.right().value(), var10003, var7);
        } else {
            throw new IllegalStateException("Defect: invariance violation");
        }
    }

    private static final Tree delLeft$1(final Tree tree$1, final Object k$1, final Comparator ordering$1) {
        if (isBlackTree(tree$1.left())) {
            return balLeft$1(tree$1.key(), tree$1.value(), del(tree$1.left(), k$1, ordering$1), tree$1.right());
        } else {
            Object var10001 = tree$1.key();
            Object var10002 = tree$1.value();
            Tree var10003 = del(tree$1.left(), k$1, ordering$1);
            Tree apply_right = tree$1.right();
            Tree apply_left = var10003;
            Object apply_value = var10002;
            Object apply_key = var10001;
            return new RedTree(apply_key, apply_value, apply_left, apply_right);
        }
    }

    private static final Tree delRight$1(final Tree tree$1, final Object k$1, final Comparator ordering$1) {
        if (isBlackTree(tree$1.right())) {
            return balRight$1(tree$1.key(), tree$1.value(), tree$1.left(), del(tree$1.right(), k$1, ordering$1));
        } else {
            Object var10001 = tree$1.key();
            Object var10002 = tree$1.value();
            Tree var10003 = tree$1.left();
            Tree apply_right = del(tree$1.right(), k$1, ordering$1);
            Tree apply_left = var10003;
            Object apply_value = var10002;
            Object apply_key = var10001;
            return new RedTree(apply_key, apply_value, apply_left, apply_right);
        }
    }

    private static final Tree append$1(final Tree tl, final Tree tr) {
        if (tl == null) {
            return tr;
        } else if (tr == null) {
            return tl;
        } else {
            if (isRedTree(tl) && isRedTree(tr)) {
                Tree bc = append$1(tl.right(), tr.left());
                if (isRedTree(bc)) {
                    RedTree var9 = new RedTree(tl.key(), tl.value(), tl.left(), bc.left());
                    RedTree var14 = new RedTree(tr.key(), tr.value(), bc.right(), tr.right());
                    return new RedTree(bc.key(), bc.value(), var9, var14);
                } else {
                    RedTree var20 = new RedTree(tr.key(), tr.value(), bc, tr.right());
                    return new RedTree(tl.key(), tl.value(), tl.left(), var20);
                }
            } else if (isBlackTree(tl) && isBlackTree(tr)) {
                Tree bc = append$1(tl.right(), tr.left());
                if (isRedTree(bc)) {
                    BlackTree var28 = new BlackTree(tl.key(), tl.value(), tl.left(), bc.left());
                    BlackTree var33 = new BlackTree(tr.key(), tr.value(), bc.right(), tr.right());
                    return new RedTree(bc.key(), bc.value(), var28, var33);
                } else {
                    return balLeft$1(tl.key(), tl.value(), tl.left(), new BlackTree(tr.key(), tr.value(), bc, tr.right()));
                }
            } else if (isRedTree(tr)) {
                return new RedTree(tr.key(), tr.value(), append$1(tl, tr.left()), tr.right());
            } else if (isRedTree(tl)) {
                return new RedTree(tl.key(), tl.value(), tl.left(), append$1(tl.right(), tr));
            } else {
                throw new IllegalStateException((new StringBuilder(28)).append("unmatched tree on append: ").append(tl).append(", ").append(tr).toString());
            }
        }
    }

    private static final NList unzip$1(NList zipper, boolean leftMost) {
        while(true) {
            Tree next = leftMost ? ((Tree)zipper.head()).left() : ((Tree)zipper.head()).right();
            if (next == null) {
                return zipper;
            }

            NList var10000 = NList.cons(next, zipper);
            zipper = var10000;
        }
    }

    private static final CompareDepthResult unzipBoth$1(Tree left, Tree right, NList leftZipper, NList rightZipper, int smallerDepth) {
        while(true) {
            Tree var10000;
            Tree var10001;
            NList var10002;
            NList var10003;
            if (isBlackTree(left) && isBlackTree(right)) {
                var10000 = left.right();
                var10001 = right.left();
                var10002 = NList.cons(left, leftZipper);
                var10003 = NList.cons(right, rightZipper);
                ++smallerDepth;
                rightZipper = var10003;
                leftZipper = var10002;
                right = var10001;
                left = var10000;
            } else if (isRedTree(left) && isRedTree(right)) {
                var10000 = left.right();
                var10001 = right.left();
                var10002 = NList.cons(left, leftZipper);
                var10003 = NList.cons(right, rightZipper);
                smallerDepth = smallerDepth;
                rightZipper = var10003;
                leftZipper = var10002;
                right = var10001;
                left = var10000;
            } else if (isRedTree(right)) {
                var10001 = right.left();
                var10003 = NList.cons(right, rightZipper);
                smallerDepth = smallerDepth;
                rightZipper = var10003;
                leftZipper = leftZipper;
                right = var10001;
                left = left;
            } else {
                if (!isRedTree(left)) {
                    if (left == null && right == null) {
                        return new CompareDepthResult<>(null, true, false, smallerDepth);
                    }

                    if (left == null && isBlackTree(right)) {
                        boolean leftMost = true;
                        return new CompareDepthResult<>(unzip$1(NList.cons(right, rightZipper), leftMost), false, leftMost, smallerDepth);
                    }

                    if (isBlackTree(left) && right == null) {
                        boolean leftMost = false;
                        return new CompareDepthResult<>(unzip$1(NList.cons(left, leftZipper), leftMost), false, leftMost, smallerDepth);
                    }

                    throw new IllegalStateException((new StringBuilder(28)).append("unmatched trees in unzip: ").append(left).append(", ").append(right).toString());
                }

                leftZipper = NList.cons(left, leftZipper);
                left = left.right();
            }
        }
    }

    private static final NList findDepth$1(NList zipper, int depth) {
        while(zipper != null) {
            NList var10000;
            if (isBlackTree((Tree)zipper.head())) {
                if (depth == 1) {
                    return zipper;
                }

                var10000 = zipper.tail();
                --depth;
                zipper = var10000;
            } else {
                var10000 = zipper.tail();
                depth = depth;
                zipper = var10000;
            }
        }

        throw new IllegalStateException("Defect: unexpected empty zipper while computing range");
    }

    public static final class NList<A> {
        private final A head;
        private final NList<A> tail;

        public A head() {
            return head;
        }

        public NList<A> tail() {
            return tail;
        }

        public NList(final A head, final NList<A> tail) {
            this.head = head;
            this.tail = tail;
        }

        public static <B> NList<B> cons(final B x, final NList<B> xs) {
            return new NList(x, xs);
        }

        public static <A, B> B foldLeft(final NList<A> xs, final B z, final BiFunction<B, A, B> op) {
            B acc = z;

            for(NList<A> these = xs; these != null; these = these.tail()) {
                acc = op.apply(acc, these.head());
            }

            return acc;
        }
    }


    public abstract static class Tree<A, B> {
        private final A key;
        private final B value;
        private final Tree<A, B> left;
        private final Tree<A, B> right;
        private final int count;

        public final A key() {
            return this.key;
        }

        public final B value() {
            return this.value;
        }

        public final Tree<A, B> left() {
            return this.left;
        }

        public final Tree<A, B> right() {
            return this.right;
        }

        public final int count() {
            return this.count;
        }

        public abstract Tree<A, B> black();

        public abstract Tree<A, B> red();

        public Tree(final A key, final B value, final Tree<A, B> left, final Tree<A, B> right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            this.count = 1 + RedBlackTree.count(left) + RedBlackTree.count(right);
        }
    }

    public static final class RedTree<A, B> extends Tree<A, B> {
        public Tree<A, B> black() {
            return new BlackTree(key(), value(), left(), right());
        }

        public Tree<A, B> red() {
            return this;
        }

        public String toString() {
            return (new StringBuilder(15)).append("RedTree(").append(super.key()).append(", ").append(super.value()).append(", ").append(super.left()).append(", ").append(super.right()).append(")").toString();
        }

        public RedTree(final A key, final B value, final Tree<A, B> left, final Tree<A, B> right) {
            super(key, value, left, right);
        }

        public static <A, B> RedTree<A, B> apply(final A key, final B value, final Tree<A, B> left, final Tree<A, B> right) {
            return new RedTree(key, value, left, right);
        }
    }

    public static final class BlackTree<A, B> extends Tree<A, B> {
        public Tree<A, B> black() {
            return this;
        }

        public Tree<A, B> red() {
            return new RedTree<>(key(), value(), left(), right());
        }

        public String toString() {
            return (new StringBuilder(17)).append("BlackTree(").append(super.key()).append(", ").append(super.value()).append(", ").append(super.left()).append(", ").append(super.right()).append(")").toString();
        }

        public BlackTree(final A key, final B value, final Tree<A, B> left, final Tree<A, B> right) {
            super(key, value, left, right);
        }

        public static <A, B> BlackTree<A, B> apply(final A key, final B value, final Tree<A, B> left, final Tree<A, B> right) {
            return new BlackTree(key, value, left, right);
        }
    }

    public abstract static class TreeIterator<A, B, R> implements Iterator<R> {
        private final Tree<A, B> root;
        private final Comparator<A> ordering;
        private Tree<A, B>[] stackOfNexts;
        private int index;
        private Tree<A, B> lookahead;

        public abstract R nextResult(final Tree<A, B> tree);

        public boolean hasNext() {
            return this.lookahead != null;
        }

        public R next() {
            Tree<A,B> var1 = this.lookahead;
            if (var1 == null) {
                throw new NoSuchElementException("next on empty iterator");
            } else {
                this.lookahead = this.findLeftMostOrPopOnEmpty(this.goRight(var1));
                return this.nextResult(var1);
            }
        }

        private Tree<A, B> findLeftMostOrPopOnEmpty(Tree<A, B> tree) {
            while(tree != null) {
                if (tree.left() == null) {
                    return tree;
                }

                tree = this.goLeft(tree);
            }

            return this.popNext();
        }

        private void pushNext(final Tree<A, B> tree) {
            this.stackOfNexts[this.index] = tree;
            ++this.index;
        }

        private Tree<A, B> popNext() {
            if (this.index == 0) {
                return null;
            } else {
                --this.index;
                return this.stackOfNexts[this.index];
            }
        }

        private Tree<A, B> startFrom(final A key) {
            return this.root == null ? null : this.find$1(this.root, key);
        }

        private Tree<A, B> goLeft(final Tree<A, B> tree) {
            this.pushNext(tree);
            return tree.left();
        }

        private Tree<A, B> goRight(final Tree<A, B> tree) {
            return tree.right();
        }

        private final Tree find$1(Tree<A,B> tree, final A key$1) {
            while(tree != null) {
                tree = this.ordering.compare(key$1, tree.key()) <= 0 ? this.goLeft(tree) : this.goRight(tree);
            }

            return this.popNext();
        }

        public TreeIterator(final Tree<A, B> root, final AOption<A> start, final Comparator<A> ordering) {
            this.root = root;
            this.ordering = ordering;
            this.stackOfNexts = root == null ? null : new Tree[2 * (32 - Integer.numberOfLeadingZeros(root.count() + 2 - 1)) - 2];
            this.index = 0;

            this.lookahead = start.map(this::startFrom).orElseGet(() -> findLeftMostOrPopOnEmpty(root));
        }
    }

    private static class EntryImpl<A, B> implements Map.Entry<A, B> {
        private final A key;
        private final B value;

        EntryImpl (A key, B value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public A getKey () {
            return key;
        }

        @Override
        public B getValue () {
            return value;
        }

        @Override
        public B setValue (B value) {
            throw new UnsupportedOperationException();
        }
    }

    public static class EntriesIterator<A, B> extends TreeIterator<A, B, Map.Entry<A, B>> {
        public Map.Entry<A, B> nextResult(final Tree<A, B> tree) {
            return new EntryImpl<>(tree.key(), tree.value());
        }

        public EntriesIterator(final Tree<A, B> tree, final AOption<A> focus, final Comparator<A> evidence$16) {
            super(tree, focus, evidence$16);
        }
    }

    public static class KeysIterator<A, B> extends TreeIterator<A, B, A> {
        public A nextResult(final Tree<A, B> tree) {
            return tree.key();
        }

        public KeysIterator(final Tree<A, B> tree, final AOption<A> focus, final Comparator<A> evidence$17) {
            super(tree, focus, evidence$17);
        }
    }

    public static class ValuesIterator<A, B> extends TreeIterator<A, B, B> {
        public B nextResult(final Tree<A, B> tree) {
            return tree.value();
        }

        public ValuesIterator(final Tree<A, B> tree, final AOption<A> focus, final Comparator<A> evidence$18) {
            super(tree, focus, evidence$18);
        }
    }

}
