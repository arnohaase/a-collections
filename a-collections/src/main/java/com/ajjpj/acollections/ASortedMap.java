package com.ajjpj.acollections;

import com.ajjpj.acollections.util.AOption;

import java.util.Comparator;
import java.util.Map;


public interface ASortedMap<K,V> extends AMap<K,V> { //TODO implements NavigableMap
    Comparator<K> comparator();

    /**
     * Count all the nodes with keys greater than or equal to the lower bound and less than the upper bound.
     * The two bounds are optional.
     */
    int countInRange (AOption<K> from, AOption<K> to);

    ASortedMap<K,V> range (AOption<K> from, AOption<K> until);
    ASortedMap<K,V> drop (int n);
    ASortedMap<K,V> take (int n);
    ASortedMap<K,V> slice (int from, int until);

    AOption<Map.Entry<K,V>> smallest();
    AOption<Map.Entry<K,V>> greatest();

    ASortedSet<K> keySet();
    ASortedSet<Map.Entry<K,V>> entrySet();

    AIterator<Map.Entry<K,V>> iterator(AOption<K> start);
    AIterator<K> keysIterator (AOption<K> start);
    AIterator<V> valuesIterator (AOption<K> start);
}
