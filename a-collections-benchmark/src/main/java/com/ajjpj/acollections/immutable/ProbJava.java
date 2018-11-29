package com.ajjpj.acollections.immutable;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;


class ProbJava {

    static <A extends Comparable<A>, B> Tree<A, B> update (Tree<A, B> tree, A k, B v) {
        return upd(tree, k, v);
    }

    private static boolean isBlackTree (Tree<?, ?> tree) {
        return tree instanceof BlackTree;
    }

    private static <A, B> Tree<A, B> mkTree (boolean isBlack, A k, B v, Tree<A, B> l, Tree<A, B> r) {
        if (isBlack) return new BlackTree<>(k, v, l, r);
        else return new RedTree<>(k, v, l, r);
    }

    private static <A extends Comparable<A>, B> Tree<A, B> upd (Tree<A, B> tree, A k, B v) {
        if (tree == null) return new RedTree<>(k, v, null, null);
        final int cmp = Comparator.<A>naturalOrder().compare(k, tree.key);
        if (Objects.equals(k, tree.key)) return mkTree(isBlackTree(tree), k, v, tree.left, tree.right);
        return tree;
    }

    static abstract class Tree<A, B> implements Serializable {
        final A key;
        final B value;
        final Tree<A, B> left, right;

        Tree (A key, B value, Tree<A, B> left, Tree<A, B> right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    static class RedTree<A, B> extends Tree<A, B> {
        RedTree (A key, B value, Tree<A, B> left, Tree<A, B> right) {
            super(key, value, left, right);
        }
    }

    static class BlackTree<A, B> extends Tree<A, B> {
        BlackTree (A key, B value, Tree<A, B> left, Tree<A, B> right) {
            super(key, value, left, right);
        }
    }
}
