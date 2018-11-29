package com.ajjpj.acollections.immutable.j;


public class Tree<A, B> {
    private final A key;
    private final B value;
    private final Tree<A, B> left, right;

    public Tree (A key, B value, Tree<A, B> left, Tree<A, B> right) {
        this.key = key;
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public A key() {
        return key;
    }
    public B value() {
        return value;
    }
    public Tree<A,B> left() {
        return left;
    }
    public Tree<A,B> right() {
        return right;
    }
}
