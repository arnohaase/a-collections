package com.ajjpj.acollections;

import com.ajjpj.acollections.util.AEquality;
import com.ajjpj.acollections.util.AOption;

import java.util.Map;


public interface AMap<K,V> extends Map<K,V>, ACollectionOps<Map.Entry<K,V>>, Iterable<Map.Entry<K,V>> {
    AEquality keyEquality ();

    @Override default AEquality equality () {
        return keyEquality();
    }

    boolean containsKey(Object key);
    boolean containsValue(V value, AEquality equality);

    V get(Object key);
    AOption<V> getOptional(K key);

    AMap<K,V> updated(K key, V value);
    AMap<K,V> removed(K key);

    ASet<K> keySet();
    ACollection<V> values();

    AIterator<K> keysIterator();
    AIterator<V> valuesIterator();

    class AMapEntry<K,V> implements Map.Entry<K,V> {
        private final K key;
        private final V value;

        public AMapEntry (K key, V value) {
            this.key = key;
            this.value = value;
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

        @Override public String toString () {
            return "AMapEntry{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }
}
