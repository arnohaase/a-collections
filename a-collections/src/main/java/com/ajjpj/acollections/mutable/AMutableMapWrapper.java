package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.immutable.*;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AMapDefaults;
import com.ajjpj.acollections.internal.AMapSupport;
import com.ajjpj.acollections.util.AOption;

import java.util.*;
import java.util.function.*;


public class AMutableMapWrapper<K,V> implements AMapDefaults<K, V, AMutableMapWrapper<K,V>>, ACollectionDefaults<Map.Entry<K,V>, AMutableMapWrapper<K,V>> {
    private final Map<K,V> inner;

    //TODO static factories

    public static <K,V> AMutableMapWrapper<K,V> fromIterator(Iterator<Map.Entry<K,V>> it) {
        return AMutableMapWrapper.<K,V>builder().addAll(it).build();
    }

    public static <K,V> AMutableMapWrapper<K,V> wrap(Map<K,V> inner) {
        return new AMutableMapWrapper<>(inner);
    }

    AMutableMapWrapper (Map<K, V> inner) {
        this.inner = inner;
    }

    @Override public ATreeSet<Entry<K, V>> toSortedSet () {
        throw new UnsupportedOperationException("pass in a Comparator<Map.Entry> - Map.Entry has no natural order");
    }

    @Override public boolean containsKey (Object key) {
        return inner.containsKey(key);
    }

    @Override public V get (Object key) {
        return inner.get(key);
    }

    @Override public AOption<V> getOptional (K key) {
        return inner.containsKey(key) ? AOption.some(inner.get(key)) : AOption.none();
    }

    @Override public AMap<K, V> plus (K key, V value) {
        inner.put(key, value);
        return this;
    }

    @Override public AMap<K, V> minus (K key) {
        inner.remove(key);
        return this;
    }

    @Override public AMutableMapWrapper<K,V> filter (Predicate<Entry<K, V>> f) {
        return AMutableMapWrapper.fromIterator(iterator().filter(f));
    }
    @Override public AMutableMapWrapper<K, V> filterNot (Predicate<Entry<K, V>> f) {
        return AMutableMapWrapper.fromIterator(iterator().filterNot(f));
    }
    @Override public AMutableMapWrapper<K, V> filterKeys (Predicate<K> f) {
        return AMapDefaults.super.filterKeys(f);
    }

    @Override public <U> AMutableMapWrapper<K,U> mapValues (Function<V, U> f) {
        return (AMutableMapWrapper<K,U>) AMapDefaults.super.mapValues(f);
    }
    @Override public <K1> AMap<K1, AMutableMapWrapper<K, V>> groupBy (Function<Entry<K, V>, K1> keyExtractor) {
        //noinspection unchecked
        return (AMap<K1, AMutableMapWrapper<K, V>>) AMapSupport.groupBy(this, keyExtractor);
    }

    @Override public ASet<K> keySet () {
        return AMutableSetWrapper.wrap(inner.keySet());
    }
    @Override public ACollection<V> values () {
        return new AMapSupport.ValuesCollection<>(this);
    }
    @Override public ASet<Entry<K, V>> entrySet () {
        return AMutableSetWrapper.wrap(inner.entrySet());
    }
    @Override public AIterator<K> keysIterator () {
        return AIterator.wrap(inner.keySet().iterator());
    }
    @Override public AIterator<V> valuesIterator () {
        return AIterator.wrap(inner.values().iterator());
    }
    @Override public AIterator<Entry<K, V>> iterator () {
        return entrySet().iterator();
    }

    @Override public <U> ACollectionBuilder<U, ? extends ACollectionOps<U>> newBuilder () {
        throw new UnsupportedOperationException("Implementing this well goes beyond the boundaries of Java's type system.");
    }

    @Override public Entry<K, V> head () {
        return iterator().next();
    }
    @Override public AOption<Entry<K, V>> headOption () {
        return iterator().hasNext() ? AOption.some(iterator().next()) : AOption.none();
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

    @Override public Entry<K, V> min () {
        throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
    }
    @Override public Entry<K, V> max () {
        throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
    }

    @Override public boolean contains (Object o) {
        return AMapSupport.containsEntry(this, o);
    }

    @Override public int size () {
        return inner.size();
    }
    @Override public boolean isEmpty () {
        return inner.isEmpty();
    }

    @Override public boolean containsValue (Object value) {
        return inner.containsValue(value);
    }
    @Override public V put (K key, V value) {
        return inner.put(key, value);
    }
    @Override public V remove (Object key) {
        return inner.remove(key);
    }
    @Override public void putAll (Map<? extends K, ? extends V> m) {
        inner.putAll(m);
    }
    @Override public void clear () {
        inner.clear();
    }

    @Override public <K1 extends K, V1 extends V> AMap<K, V> plusAll (Map<K1, V1> other) {
        putAll(other);
        return this;
    }

    @Override public <K1,V1> ACollectionBuilder<Entry<K1,V1>, AMutableMapWrapper<K1,V1>> newEntryBuilder () {
        return new ACollectionBuilder<Map.Entry<K1,V1>, AMutableMapWrapper<K1,V1>>() {
            final HashMap<Object,Object> result = new HashMap<>();

            @Override public ACollectionBuilder<Entry<K1,V1>, AMutableMapWrapper<K1,V1>> add (Entry<K1,V1> el) {
                //noinspection unchecked
                final Map.Entry<Object,Object> e = (Entry<Object, Object>) el;
                result.put(e.getKey(), e.getValue());
                return this;
            }

            @Override public AMutableMapWrapper<K1,V1> build () {
                //noinspection unchecked
                return (AMutableMapWrapper<K1,V1>) AMutableMapWrapper.wrap(result);
            }
        };
    }

    @Override public V getOrDefault (Object key, V defaultValue) {
        return inner.getOrDefault(key, defaultValue);
    }
    @Override public void forEach (BiConsumer<? super K, ? super V> action) {
        inner.forEach(action);
    }
    @Override public void replaceAll (BiFunction<? super K, ? super V, ? extends V> function) {
        inner.replaceAll(function);
    }
    @Override public V putIfAbsent (K key, V value) {
        return inner.putIfAbsent(key, value);
    }
    @Override public boolean remove (Object key, Object value) {
        return inner.remove(key, value);
    }
    @Override public boolean replace (K key, V oldValue, V newValue) {
        return inner.replace(key, oldValue, newValue);
    }
    @Override public V replace (K key, V value) {
        return inner.replace(key, value);
    }
    @Override public V computeIfAbsent (K key, Function<? super K, ? extends V> mappingFunction) {
        return inner.computeIfAbsent(key, mappingFunction);
    }
    @Override public V computeIfPresent (K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return inner.computeIfPresent(key, remappingFunction);
    }
    @Override public V compute (K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return inner.compute(key, remappingFunction);
    }
    @Override public V merge (K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return inner.merge(key, value, remappingFunction);
    }

    @Override public boolean equals(Object o) {
        return inner.equals(o);
    }
    @Override public int hashCode () {
        return inner.hashCode();
    }

    @Override public String toString () {
        return getClass().getSimpleName() + "@" + inner;
    }

    public static <K,V> Builder<K,V> builder() {
        return new Builder<>();
    }

    public static class Builder<K,V> implements ACollectionBuilder<Map.Entry<K,V>, AMutableMapWrapper<K,V>> {
        private final Map<K,V> result = new HashMap<>();

        public ACollectionBuilder<Entry<K, V>, AMutableMapWrapper<K, V>> add (K key, V value) {
            result.put(key, value);
            return this;
        }

        @Override public ACollectionBuilder<Entry<K, V>, AMutableMapWrapper<K, V>> add (Entry<K, V> el) {
            result.put(el.getKey(), el.getValue());
            return this;
        }

        @Override public AMutableMapWrapper<K, V> build () {
            return new AMutableMapWrapper<>(result);
        }
    }

}
