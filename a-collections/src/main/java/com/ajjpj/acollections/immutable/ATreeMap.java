package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.util.AOption;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;


public class ATreeMap<K,V> implements Iterable<Map.Entry<K,V>> {
    private final CompactRedBlackTree<K,V> impl;

    public static <K,V> ATreeMap<K,V> empty(Comparator<K> comparator) {
        return new ATreeMap<>(CompactRedBlackTree.empty(comparator));
    }

    private ATreeMap (CompactRedBlackTree<K, V> impl) {
        this.impl = impl;
    }

    public V get(K key) {
        return AOption
                .of(impl.get(key))
                .map(Map.Entry::getValue)
                .orNull();
    }
    public ATreeMap<K,V> updated(K key, V value) {
        return new ATreeMap<>(impl.updated(key, value));
    }
    public ATreeMap<K,V> removed(K key) {
        return new ATreeMap<>(impl.removed(key));
    }
    public Iterator<Map.Entry<K,V>> iterator() {
        return impl.iterator();
    }

    public int size() {
        return impl.size();
    }
}
