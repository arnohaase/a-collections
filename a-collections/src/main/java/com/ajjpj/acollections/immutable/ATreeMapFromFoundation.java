package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.util.AOption;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;


public class ATreeMapFromFoundation<K,V> implements Iterable<Map.Entry<K,V>> {
    private static class Entry<K,V> implements CompactRedBlackTree.EntryWithComparator, Map.Entry<K,V> {
        final K key;
        final V value;
        final Comparator<K> comparator;

        Entry (K key, V value, Comparator<K> comparator) {
            this.key = key;
            this.value = value;
            this.comparator = comparator;
        }

        @Override public int compareTo (CompactRedBlackTree.EntryWithComparator other) {
            //noinspection unchecked
            return comparator.compare(key, ((Entry<K,V>)other).key);
        }

        @Override public K getKey () {
            return key;
        }
        @Override public V getValue () {
            return value;
        }
        @Override public V setValue (V value) {
            throw new UnsupportedOperationException();
        }
    }

    private final CompactRedBlackTree<Entry<K,V>> impl;
    private final Comparator<K> comparator;

    public static <K,V> ATreeMapFromFoundation<K,V> empty(Comparator<K> comparator) {
        return new ATreeMapFromFoundation<>(CompactRedBlackTree.empty(), comparator);
    }

    private ATreeMapFromFoundation (CompactRedBlackTree<Entry<K,V>> impl, Comparator<K> comparator) {
        this.impl = impl;
        this.comparator = comparator;
    }

    public V get(K key) {
        return AOption
                .of(impl.get(new Entry<>(key, null, comparator)))
                .map(e -> e.value)
                .orNull();
    }
    public ATreeMapFromFoundation<K,V> updated(K key, V value) {
        return new ATreeMapFromFoundation<>(impl.updated(new Entry<>(key, value, comparator)), comparator);
    }
    public ATreeMapFromFoundation<K,V> removed(K key) {
        if (impl.get(new Entry<>(key, null, comparator)) == null) return this;
        return new ATreeMapFromFoundation<>(impl.removed(new Entry<>(key, null, comparator)), comparator);
    }
    public Iterator<Map.Entry<K,V>> iterator() {
        //noinspection unchecked
        return (Iterator) impl.iterator();
    }

    public int size() {
        return impl.size();
    }
}
