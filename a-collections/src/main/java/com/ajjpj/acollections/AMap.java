package com.ajjpj.acollections;

import com.ajjpj.acollections.util.AOption;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;


public interface AMap<K,V> extends Map<K,V>, ACollectionOps<Map.Entry<K,V>>, Iterable<Map.Entry<K,V>> {
    boolean containsKey(Object key);

    V get(Object key);
    AOption<V> getOptional(K key);

    AMap<K,V> plus(K key, V value);
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

    <U> AMap<K,U> mapValues(Function<V,U> f);

    AMap<K,V> withDefaultValue(V defaultValue);
    AMap<K,V> withDerivedDefaultValue(Function<K,V> defaultProvider);

    ASet<K> keySet();
    ACollection<V> values();
    ASet<Map.Entry<K,V>> entrySet();

    AIterator<K> keysIterator();
    AIterator<V> valuesIterator();
}
