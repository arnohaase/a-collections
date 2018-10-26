package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.util.AOption;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;


public class ATreeMap3<K,V> implements Iterable<Map.Entry<K,V>> {
    private final com.ajjpj.acollections.immutable.rbs.RedBlackTree.Tree<K,V> root;

    private final Comparator<K> comparator;

    public static <K,V> ATreeMap3<K,V> empty(Comparator<K> comparator) {
        return new ATreeMap3<>(null, comparator);
    }

    private ATreeMap3 (com.ajjpj.acollections.immutable.rbs.RedBlackTree.Tree<K,V> root, Comparator<K> comparator) {
        this.root = root;
        this.comparator = comparator;
    }

    public V get(K key) {
        return com.ajjpj.acollections.immutable.rbs.RedBlackTree.get(root, key, comparator).orNull();
    }
    public ATreeMap3<K,V> updated(K key, V value) {
        return new ATreeMap3<>(com.ajjpj.acollections.immutable.rbs.RedBlackTree.update(root, key, value, true, comparator), comparator);
    }
    public ATreeMap3<K,V> removed(K key) {
        if (!com.ajjpj.acollections.immutable.rbs.RedBlackTree.contains(root, key, comparator)) return this;
        return new ATreeMap3<>(com.ajjpj.acollections.immutable.rbs.RedBlackTree.delete(root, key, comparator), comparator);
    }
    public Iterator<Map.Entry<K,V>> iterator() {
        return com.ajjpj.acollections.immutable.rbs.RedBlackTree.iterator(root, AOption.none(), comparator);
    }

    public int size() {
        return com.ajjpj.acollections.immutable.rbs.RedBlackTree.count(root);
    }
}
