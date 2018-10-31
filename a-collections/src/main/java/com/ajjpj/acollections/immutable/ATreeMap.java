package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AMapDefaults;
import com.ajjpj.acollections.internal.AMapSupport;
import com.ajjpj.acollections.util.AOption;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


public class ATreeMap<K,V> extends AbstractImmutableMap<K,V> implements ASortedMap<K,V>, ACollectionDefaults<Map.Entry<K,V>, ATreeMap<K,V>>, AMapDefaults<K,V,ATreeMap<K,V>>, Serializable {
    private final RedBlackTree.Tree<K,V> root;
    private final Comparator<K> comparator;

    public static <K,V> ATreeMap<K,V> empty(Comparator<K> comparator) {
        return new ATreeMap<>(null, comparator);
    }

    private ATreeMap (RedBlackTree.Tree<K,V> root, Comparator<K> comparator) {
        this.root = root;
        this.comparator = comparator;
    }

    public static <K extends Comparable<K>,V> ATreeMap<K,V> fromIterator(Iterator<Entry<K,V>> iterator) {
        return fromIterator(iterator, Comparator.naturalOrder());
    }
    public static <K,V> ATreeMap<K,V> fromIterator(Iterator<Entry<K,V>> iterator, Comparator<K> comparator) {
        return ATreeMap.<K,V> builder(comparator).addAll(iterator).build();
    }
    public static <K extends Comparable<K>,V> ATreeMap<K,V> fromMap(Map<K,V> m) {
        return from(m.entrySet());
    }
    public static <K extends Comparable<K>,V> ATreeMap<K,V> from(Iterable<Entry<K,V>> iterable) {
        return from(iterable, Comparator.naturalOrder());
    }
    public static <K,V> ATreeMap<K,V> from(Iterable<Entry<K,V>> iterator, Comparator<K> comparator) {
        return ATreeMap.<K,V> builder(comparator).addAll(iterator).build();
    }

    public static <K extends Comparable<K>,V> ATreeMap<K,V> of() {
        return empty(Comparator.<K>naturalOrder());
    }
    public static <K extends Comparable<K>,V> ATreeMap<K,V> of(K k1, V v1) {
        return ATreeMap.<K,V>builder().add(k1, v1).build();
    }
    public static <K extends Comparable<K>,V> ATreeMap<K,V> of(K k1, V v1, K k2, V v2) {
        return ATreeMap.<K,V>builder().add(k1, v1).add(k2, v2).build();
    }
    public static <K extends Comparable<K>,V> ATreeMap<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return ATreeMap.<K,V>builder().add(k1, v1).add(k2, v2).add(k3,v3).build();
    }
    public static <K extends Comparable<K>,V> ATreeMap<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return ATreeMap.<K,V>builder().add(k1, v1).add(k2, v2).add(k3,v3).add(k4,v4).build();
    }
    public static <K extends Comparable<K>,V> ATreeMap<K,V> ofEntries(Iterable<Map.Entry<K,V>> coll) {
        return from(coll);
    }


    public static <K extends Comparable<K>,V> Builder<K,V> builder() {
        return builder(Comparator.<K>naturalOrder());
    }
    public static <K,V> Builder<K,V> builder(Comparator<K> comparator) {
        return new Builder<>(comparator);
    }

    @Override public V get(Object key) {

        try {
            //noinspection unchecked
            return RedBlackTree.get(root, (K) key, comparator).orNull(); //TODO skip 'get'
        }
        catch (Exception e) {
            // handle 'wrong type' exceptions thrown by the comparator because Java's API weirdly accepts 'Object' rather than 'K' as the key's type...
            return null;
        }
    }
    @Override public ATreeMap<K,V> plus (K key, V value) {
        return new ATreeMap<>(RedBlackTree.update(root, key, value, true, comparator), comparator);
    }
    @Override public ATreeMap<K,V> minus (K key) {
        if (!RedBlackTree.contains(root, key, comparator)) return this;
        return new ATreeMap<>(RedBlackTree.delete(root, key, comparator), comparator);
    }
    @Override public AIterator<Entry<K,V>> iterator() {
        return RedBlackTree.iterator(root, AOption.none(), comparator);
    }

    @Override public int size() {
        return RedBlackTree.count(root);
    }

    @Override public boolean contains (Object o) {
        return AMapSupport.containsEntry(this, o);
    }

    @Override public boolean containsKey (Object key) {
        try {
            //noinspection unchecked
            return RedBlackTree.get(root, (K) key, comparator).nonEmpty(); //TODO skip 'get', use 'lookup' directly
        }
        catch (Exception e) {
            // handle 'wrong type' exceptions thrown by the comparator because Java's API weirdly accepts 'Object' rather than 'K' as the key's type...
            return false;
        }
    }

    @Override public boolean containsValue (Object value) {
        return RedBlackTree.valuesIterator(root, AOption.none(), comparator)
                .exists(v -> Objects.equals(v, value));
    }

    @Override public AOption<V> getOptional (K key) {
        return RedBlackTree.get(root, key, comparator);
    }

    @Override public <U> ACollection<U> map (Function<Entry<K, V>, U> f) {
        return ACollectionSupport.map(AVector.builder(), this, f);
    }
    @Override public <U> ACollection<U> flatMap (Function<Entry<K, V>, Iterable<U>> f) {
        return ACollectionSupport.flatMap(AVector.builder(), this, f);
    }
    @Override public <U> ACollection<U> collect (Predicate<Entry<K, V>> filter, Function<Entry<K, V>, U> f) {
        return ACollectionSupport.collect(AVector.builder(), this, filter, f);
    }

    @Override public <V1> ATreeMap<K, V1> mapValues (Function<V, V1> f) {
        return (ATreeMap<K,V1>) AMapDefaults.super.mapValues(f);
    }

    @Override public ATreeMap<K, V> filter (Predicate<Entry<K, V>> f) {
        return ATreeMap.<K,V>builder(comparator).addAll(iterator().filter(f)).build();
    }

    @Override public ATreeMap<K, V> filterNot (Predicate<Entry<K, V>> f) {
        return filter(f.negate());
    }

    @Override public <K1> AMap<K1, ATreeMap<K, V>> groupBy (Function<Entry<K, V>, K1> keyExtractor) {
        //noinspection unchecked
        return (AMap<K1, ATreeMap<K, V>>) AMapSupport.groupBy(this, keyExtractor);
    }

    @Override public boolean isEmpty () {
        return root == null;
    }

    @Override public ASortedSet<K> keySet () {
        return new AMapSupport.SortedKeySet<>(this);
    }

    @Override public ACollection<V> values () {
        return new AMapSupport.ValuesCollection<>(this);
    }

    @Override public ASortedSet<Entry<K, V>> entrySet () { //TODO ASortedSet
        return new AMapSupport.SortedEntrySet<>(this);
    }

    @Override public Comparator<K> comparator () {
        return comparator;
    }

    @Override public int countInRange (AOption<K> from, AOption<K> to) {
        return RedBlackTree.countInRange(root, from, to, comparator);
    }

    @Override public ATreeMap<K, V> range (AOption<K> from, AOption<K> until) {
        return new ATreeMap<>(RedBlackTree.rangeImpl(root, from, until, comparator), comparator);
    }

    @Override public ATreeMap<K, V> drop (int n) {
        return new ATreeMap<>(RedBlackTree.drop(root, n), comparator);
    }

    @Override public ATreeMap<K, V> take (int n) {
        return new ATreeMap<>(RedBlackTree.take(root, n), comparator);
    }

    @Override public ATreeMap<K, V> slice (int from, int until) {
        return new ATreeMap<>(RedBlackTree.slice(root, from, until), comparator);
    }

    @Override public AOption<Entry<K, V>> smallest () {
        if (root == null) return AOption.none();
        return AOption.some(RedBlackTree.smallest(root).entry());
    }

    @Override public AOption<Entry<K, V>> greatest () {
        if (root == null) return AOption.none();
        return AOption.some(RedBlackTree.greatest(root).entry());
    }

    @Override public AIterator<K> keysIterator () {
        return keysIterator(AOption.none());
    }
    @Override public AIterator<V> valuesIterator () {
        return valuesIterator(AOption.none());
    }

    @Override public AIterator<Entry<K, V>> iterator (AOption<K> start) {
        return RedBlackTree.iterator(root, start, comparator);
    }
    @Override public AIterator<K> keysIterator (AOption<K> start) {
        return RedBlackTree.keysIterator(root, start, comparator);
    }
    @Override public AIterator<V> valuesIterator (AOption<K> start) {
        return RedBlackTree.valuesIterator(root, start, comparator);
    }

    @Override public <K1, V1> ACollectionBuilder<Entry<K1, V1>, ATreeMap<K1, V1>> newEntryBuilder () {
        //noinspection unchecked
        return new Builder(Comparator.naturalOrder());
    }

    public static class Builder<K,V> implements ACollectionBuilder<Map.Entry<K,V>, ATreeMap<K,V>> {
        private ATreeMap<K,V> result;

        Builder (Comparator<K> comparator) {
            this.result = ATreeMap.empty(comparator);
        }

        public Builder<K, V> add (K key, V value) {
            result = result.plus(key, value);
            return this;
        }

        @Override public Builder<K, V> add (Entry<K, V> el) {
            result = result.plus(el.getKey(), el.getValue());
            return this;
        }

        @Override public Builder<K, V> addAll (Iterator<? extends Entry<K, V>> it) {
            while(it.hasNext()) add(it.next());
            return this;
        }

        @Override public Builder<K, V> addAll (Iterable<? extends Entry<K, V>> coll) {
            return addAll(coll.iterator());
        }

        @Override public Builder<K, V> addAll (Entry<K, V>[] coll) {
            return addAll(Arrays.asList(coll));
        }

        @Override public ATreeMap<K, V> build () {
            return result;
        }
    }
}
