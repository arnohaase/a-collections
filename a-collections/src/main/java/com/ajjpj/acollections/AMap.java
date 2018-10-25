package com.ajjpj.acollections;

import com.ajjpj.acollections.util.AOption;

import java.util.Map;
import java.util.function.Function;


public interface AMap<K,V> extends Map<K,V>, ACollectionOps<Map.Entry<K,V>>, Iterable<Map.Entry<K,V>> {
    boolean containsKey(Object key);

    V get(Object key);
    AOption<V> getOptional(K key);

    AMap<K,V> updated(K key, V value);
    AMap<K,V> removed(K key);

    <U> AMap<K,U> mapValues(Function<V,U> f);

    ASet<K> keySet();
    ACollection<V> values();
    ASet<Map.Entry<K,V>> entrySet();

    AIterator<K> keysIterator();
    AIterator<V> valuesIterator();
}
