package com.ajjpj.acollections.internal;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.ALinkedList;
import com.ajjpj.acollections.immutable.ATreeSet;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.mutable.AMutableListWrapper;
import com.ajjpj.acollections.mutable.AMutableSetWrapper;
import com.ajjpj.acollections.util.AOption;

import java.util.Comparator;
import java.util.Map;
import java.util.Spliterator;
import java.util.function.*;


abstract class AbstractDelegatingMap<K,V> implements AMap<K,V> {
    final AMap<K,V> inner;

    public AbstractDelegatingMap (AMap<K, V> inner) {
        this.inner = inner;
    }

    protected abstract AMap<K,V> wrap(AMap<K,V> inner);

    @Override
    public boolean containsKey (Object key) {
        return inner.containsKey(key);
    }

    @Override
    public V get (Object key) {
        return inner.get(key);
    }

    @Override
    public AOption<V> getOptional (K key) {
        return inner.getOptional(key);
    }

    @Override
    public AMap<K, V> plus (K key, V value) {
        return wrap(inner.plus(key, value));
    }

    @Override
    public AMap<K, V> minus (K key) {
        return wrap(inner.minus(key));
    }

    @Override
    public <K1 extends K, V1 extends V> AMap<K, V> plusAll (Map<K1, V1> other) {
        return wrap(inner.plusAll(other));
    }

    @Override public AMap<K, V> filterKeys (Predicate<K> f) {
        return wrap(inner.filterKeys(f));
    }

    //TODO javadoc: you lose the wrapping here
    @Override
    public <U> AMap<K, U> mapValues (Function<V, U> f) {
        return inner.mapValues(f);
    }

    @Override
    public ASet<K> keySet () {
        return inner.keySet();
    }

    @Override
    public ACollection<V> values () {
        return inner.values();
    }

    @Override
    public ASet<Entry<K, V>> entrySet () {
        return inner.entrySet();
    }

    @Override
    public AIterator<K> keysIterator () {
        return inner.keysIterator();
    }

    @Override
    public AIterator<V> valuesIterator () {
        return inner.valuesIterator();
    }

    @Override
    public AMap<K, V> withDefaultValue (V defaultValue) {
        return inner.withDefaultValue(defaultValue);
    }

    @Override
    public AMap<K, V> withDerivedDefaultValue (Function<K, V> defaultProvider) {
        return inner.withDerivedDefaultValue(defaultProvider);
    }

    @Override
    public int size () {
        return inner.size();
    }

    @Override
    public boolean isEmpty () {
        return inner.isEmpty();
    }

    @Override
    public boolean containsValue (Object value) {
        return inner.containsValue(value);
    }

    @Override
    public V put (K key, V value) {
        return inner.put(key, value);
    }

    @Override
    public V remove (Object key) {
        return inner.remove(key);
    }

    @Override
    public void putAll (Map<? extends K, ? extends V> m) {
        inner.putAll(m);
    }

    @Override
    public void clear () {
        inner.clear();
    }

    @Override
    public boolean equals (Object o) {
        return inner.equals(o);
    }

    @Override
    public int hashCode () {
        return inner.hashCode();
    }

    @Override
    public V getOrDefault (Object key, V defaultValue) {
        return inner.getOrDefault(key, defaultValue);
    }

    @Override
    public void forEach (BiConsumer<? super K, ? super V> action) {
        inner.forEach(action);
    }

    @Override
    public void replaceAll (BiFunction<? super K, ? super V, ? extends V> function) {
        inner.replaceAll(function);
    }

    @Override
    public V putIfAbsent (K key, V value) {
        return inner.putIfAbsent(key, value);
    }

    @Override
    public boolean remove (Object key, Object value) {
        return inner.remove(key, value);
    }

    @Override
    public boolean replace (K key, V oldValue, V newValue) {
        return inner.replace(key, oldValue, newValue);
    }

    @Override
    public V replace (K key, V value) {
        return inner.replace(key, value);
    }

    @Override
    public V computeIfAbsent (K key, Function<? super K, ? extends V> mappingFunction) {
        return inner.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public V computeIfPresent (K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return inner.computeIfPresent(key, remappingFunction);
    }

    @Override
    public V compute (K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return inner.compute(key, remappingFunction);
    }

    @Override
    public V merge (K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return inner.merge(key, value, remappingFunction);
    }

    @Override
    public AIterator<Entry<K, V>> iterator () {
        return inner.iterator();
    }

    @Override
    public <U> ACollectionBuilder<U, ? extends ACollectionOps<U>> newBuilder () {
        return inner.newBuilder();
    }

    @Override
    public <K, V> ACollectionBuilder<Entry<K, V>, ? extends ACollectionOps<Entry<K, V>>> newEntryBuilder () {
        return inner.newEntryBuilder();
    }

    @Override
    public boolean nonEmpty () {
        return inner.nonEmpty();
    }

    @Override
    public Entry<K, V> head () {
        return inner.head();
    }

    @Override
    public AOption<Entry<K, V>> headOption () {
        return inner.headOption();
    }

    @Override
    public ALinkedList<Entry<K, V>> toLinkedList () {
        return inner.toLinkedList();
    }

    @Override
    public AVector<Entry<K, V>> toVector () {
        return inner.toVector();
    }

    @Override
    public AHashSet<Entry<K, V>> toSet () {
        return inner.toSet();
    }

    @Override
    public ATreeSet<Entry<K, V>> toSortedSet () {
        return inner.toSortedSet();
    }

    @Override
    public ATreeSet<Entry<K, V>> toSortedSet (Comparator<Entry<K, V>> comparator) {
        return inner.toSortedSet(comparator);
    }

    @Override
    public <K1, V1> AMap<K1, V1> toMap () {
        return inner.toMap();
    }

    @Override
    public AMutableListWrapper<Entry<K, V>> toMutableList () {
        return inner.toMutableList();
    }

    @Override
    public AMutableSetWrapper<Entry<K, V>> toMutableSet () {
        return inner.toMutableSet();
    }

    @Override
    public <U> ACollection<U> map (Function<Entry<K, V>, U> f) {
        return inner.map(f);
    }

    @Override
    public <U> ACollection<U> flatMap (Function<Entry<K, V>, Iterable<U>> f) {
        return inner.flatMap(f);
    }

    @Override
    public <U> ACollection<U> collect (Predicate<Entry<K, V>> filter, Function<Entry<K, V>, U> f) {
        return inner.collect(filter, f);
    }

    @Override
    public <U> AOption<U> collectFirst (Predicate<Entry<K, V>> filter, Function<Entry<K, V>, U> f) {
        return inner.collectFirst(filter, f);
    }

    @Override
    public AMap<K, V> filter (Predicate<Entry<K, V>> f) {
        return wrap(inner.filter(f));
    }

    @Override
    public AMap<K, V> filterNot (Predicate<Entry<K, V>> f) {
        return wrap(inner.filterNot(f));
    }

    @Override
    public AOption<Entry<K, V>> find (Predicate<Entry<K, V>> f) {
        return inner.find(f);
    }

    @Override
    public boolean forall (Predicate<Entry<K, V>> f) {
        return inner.forall(f);
    }

    @Override
    public boolean exists (Predicate<Entry<K, V>> f) {
        return inner.exists(f);
    }

    @Override
    public int count (Predicate<Entry<K, V>> f) {
        return inner.count(f);
    }

    @Override
    public boolean contains (Object o) {
        return inner.contains(o);
    }

    @Override
    public Entry<K, V> reduce (BiFunction<Entry<K, V>, Entry<K, V>, Entry<K, V>> f) {
        return inner.reduce(f);
    }

    @Override
    public Entry<K, V> reduceLeft (BiFunction<Entry<K, V>, Entry<K, V>, Entry<K, V>> f) {
        return inner.reduceLeft(f);
    }

    @Override
    public AOption<Entry<K, V>> reduceLeftOption (BiFunction<Entry<K, V>, Entry<K, V>, Entry<K, V>> f) {
        return inner.reduceLeftOption(f);
    }

    @Override
    public <U> U fold (U zero, BiFunction<U, Entry<K, V>, U> f) {
        return inner.fold(zero, f);
    }

    @Override
    public <U> U foldLeft (U zero, BiFunction<U, Entry<K, V>, U> f) {
        return inner.foldLeft(zero, f);
    }

    @Override
    public <K1> AMap<K1, ? extends ACollectionOps<Entry<K, V>>> groupBy (Function<Entry<K, V>, K1> keyExtractor) {
        return inner.groupBy(keyExtractor);
    }

    @Override
    public Entry<K, V> min () {
        return inner.min();
    }

    @Override
    public Entry<K, V> min (Comparator<Entry<K, V>> comparator) {
        return inner.min(comparator);
    }

    @Override
    public Entry<K, V> max () {
        return inner.max();
    }

    @Override
    public Entry<K, V> max (Comparator<Entry<K, V>> comparator) {
        return inner.max(comparator);
    }

    @Override
    public String mkString (String infix) {
        return inner.mkString(infix);
    }

    @Override
    public String mkString (String prefix, String infix, String suffix) {
        return inner.mkString(prefix, infix, suffix);
    }

    @Override
    public void forEach (Consumer<? super Entry<K, V>> action) {
        inner.forEach(action);
    }

    @Override
    public Spliterator<Entry<K, V>> spliterator () {
        return inner.spliterator();
    }
}
