package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.immutable.s.Tree;

import java.util.Comparator;
import java.util.Objects;


class ProbJava2 {
    static <A extends Comparable<A>, B> Tree<A, B> update (Tree<A, B> tree, A k, B v) {
        return upd(tree, k, v);
    }

    private static <A, B> Tree<A, B> mkTree (A k, B v, Tree<A, B> l, Tree<A, B> r) {
        return new Tree<>(k, v, l, r);
    }

    private static <A extends Comparable<A>, B> Tree<A, B> upd (Tree<A, B> tree, A k, B v) {
        if (tree == null) return new Tree<>(k, v, null, null);
        final int cmp = Comparator.<A>naturalOrder().compare(k, tree.key());
        if (Objects.equals(k, tree.key())) return mkTree(k, v, tree.left(), tree.right());
        return tree;
    }
}
