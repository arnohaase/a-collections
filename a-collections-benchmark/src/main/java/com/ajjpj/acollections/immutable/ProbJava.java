package com.ajjpj.acollections.immutable;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Comparator;
import java.util.Objects;


class ProbJava {

    static <A, B> Tree<A, B> update (Tree<A, B> tree, A k, B v, boolean overwrite, Comparator<A> ordering) {
        return upd(tree, k, v, overwrite, ordering);
    }

    private static boolean isBlackTree (Tree<?, ?> tree) {
        return tree instanceof BlackTree;
    } //TODO polymorphic 'isBlack()'?

    private static <A, B> Tree<A, B> mkTree (boolean isBlack, A k, B v, Tree<A, B> l, Tree<A, B> r) {
        if (isBlack) return new BlackTree<>(k, v, l, r);
        else return new RedTree<>(k, v, l, r);
    }

    private static <A, B> Tree<A, B> upd (Tree<A, B> tree, A k, B v, boolean overwrite, Comparator<A> ordering) {
        if (tree == null) return new RedTree<>(k, v, null, null);
        final int cmp = ordering.compare(k, tree.key);
        if (overwrite || !Objects.equals(k, tree.key)) return mkTree(isBlackTree(tree), k, v, tree.left, tree.right); //TODO the 'equals' comparison should be superfluous
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
