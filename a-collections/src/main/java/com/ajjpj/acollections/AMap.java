package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AHashMap;
import com.ajjpj.acollections.mutable.AMutableMapWrapper;
import com.ajjpj.acollections.util.AOption;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;


public interface AMap<K,V> extends Map<K,V>, ACollectionOps<Map.Entry<K,V>>, Iterable<Map.Entry<K,V>> {
    static <K,V> AMap<K,V> wrap(Map<K,V> m) {
        return AMutableMapWrapper.wrap(m);
    }

    static <K,V> AHashMap<K,V> empty() {
        return AHashMap.empty();
    }
    static <K,V> AHashMap<K,V> from(Map<K,V> m) {
        return AHashMap.from(m);
    }
    static <K,V> AHashMap<K,V> from(Iterable<Map.Entry<K,V>> coll) {
        return AHashMap.from(coll);
    }
    static <K,V> AHashMap<K,V> fromIterator(Iterator<Entry<K,V>> it) {
        return AHashMap.fromIterator(it);
    }

    static <K,V> AHashMap<K,V> of() {
        return AHashMap.of();
    }
    static <K,V> AHashMap<K,V> of(K k1, V v1) {
        return AHashMap.of(k1, v1);
    }
    static <K,V> AHashMap<K,V> of(K k1, V v1, K k2, V v2) {
        return AHashMap.of(k1, v1, k2, v2);
    }
    static <K,V> AHashMap<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return AHashMap.of(k1, v1, k2, v2, k3, v3);
    }
    static <K,V> AHashMap<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return AHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }
    static <K,V> AHashMap<K,V> ofEntries(Iterable<Map.Entry<K,V>> coll) {
        return AHashMap.ofEntries(coll);
    }


    boolean containsKey(Object key);

    V get(Object key);
    AOption<V> getOptional(K key);

    AMap<K,V> plus(K key, V value);
    AMap<K,V> plus(Map.Entry<K,V> entry);
    AMap<K,V> minus(K key);

    default <K1 extends K, V1 extends V> AMap<K,V> plusAll (Map<K1, V1> other) {
        AMap<K,V> result = this;
        for (Map.Entry<K1,V1> e: other.entrySet()) {
            result = result.plus(e.getKey(), e.getValue());
        }
        return result;
    }

    @Override AMap<K, V> filter (Predicate<Entry<K, V>> f);
    @Override AMap<K, V> filterNot (Predicate<Entry<K, V>> f);

    AMap<K,V> filterKeys(Predicate<K> f);
    <U> AMap<K,U> mapValues(Function<V,U> f);

    AMap<K,V> withDefaultValue(V defaultValue);
    AMap<K,V> withDerivedDefaultValue(Function<K,V> defaultProvider);

    ASet<K> keySet();
    ACollection<V> values();
    ASet<Map.Entry<K,V>> entrySet();

    AIterator<K> keysIterator();
    AIterator<V> valuesIterator();
}
