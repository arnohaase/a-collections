package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.util.AOption;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;


public class ATreeMap2<K,V> implements Iterable<Map.Entry<K,V>> {
    private final RedBlackTree.Tree<K,V> root;

    private final Comparator<K> comparator;

    public static <K,V> ATreeMap2<K,V> empty(Comparator<K> comparator) {
        return new ATreeMap2<>(null, comparator);
    }

    private ATreeMap2 (RedBlackTree.Tree<K,V> root, Comparator<K> comparator) {
        this.root = root;
        this.comparator = comparator;
    }

    public V get(K key) {
        return RedBlackTree.get(root, key, comparator).orNull();
    }
    public ATreeMap2<K,V> updated(K key, V value) {
        return new ATreeMap2<>(RedBlackTree.update(root, key, value, true, comparator), comparator);
    }
    public ATreeMap2<K,V> removed(K key) {
        return new ATreeMap2<>(RedBlackTree.delete(root, key, comparator), comparator);
    }
    public Iterator<Map.Entry<K,V>> iterator() {
        return RedBlackTree.iterator(root, AOption.none(), comparator);
    }

    public int size() {
        return RedBlackTree.count(root);
    }
}
