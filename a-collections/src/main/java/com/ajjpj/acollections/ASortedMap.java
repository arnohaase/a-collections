package com.ajjpj.acollections;

import com.ajjpj.acollections.util.AOption;

import java.util.Comparator;
import java.util.Map;


/**
 * This interface represents an {@link AMap} with an inherent element ordering. Every ASortedMap instance has a fixed {@link #comparator()}
 *  defining element ordering. All keys must be comparable via this comparator, and two keys which compare as 0 are treated as equal (see
 *  {@link Comparator} documentation).
 *
 * <p> If the key type implements {@link Comparable}, {@link Comparator#naturalOrder()} can be used.
 *
 * @param <K> The AMap's key type
 * @param <V> The AMap's value type
 */
public interface ASortedMap<K,V> extends AMap<K,V> { //TODO implements NavigableMap
    /**
     * Returns the comparator used to compare keys to determine element ordering and equality
     *
     * @return the comparator used by this map
     */
    Comparator<K> comparator();

    /**
     * Counts all the nodes with keys greater than or equal to a lower bound and less than an upper bound. Both bounds are optional.
     *
     * @param from the lower bound
     * @param to   the upper bound
     * @return the number of entries in the given key range
     */
    int countInRange (AOption<K> from, AOption<K> to);

    /**
     * Returns a map containing only entries with keys greater than or equal to a lower bound and less than an upper bound. Both bounds
     *  are optional.
     *
     * @param from  the lower bound
     * @param until the upper bound
     * @return the map with keys in the given range
     */
    ASortedMap<K,V> range (AOption<K> from, AOption<K> until);

    /**
     * Returns a map containing the same entries as this map, but without the first (i.e. smallest key) {@code n} entries, or an empty
     *  map if {@code this.size() < n}.
     *
     * @param n the number of smallest entries to drop
     * @return a map without the first {@code n} entries
     * @see AList#drop(int)
     */
    ASortedMap<K,V> drop (int n);

    /**
     * Returns a map containing only the first (i.e. smallest key) {@code n} entries, or this map if {@code this.size() < n}.
     *
     * @param n the number of entries to take
     * @return a map with only the first {@code n} entries
     * @see AList#take(int)
     */
    ASortedMap<K,V> take (int n);

    /**
     * Figuratively speaking, this method returns a map with entries starting with 'index' {@code from} up to 'index' {@code until}
     *  (exclusively). More formally, this method is equivalent to
     *
     * <p> {@code this.drop(from).take(until - from)}
     *
     * @param from  the first 'index' to keep
     * @param until the upper bound (exclusive) of 'indices' to keep
     * @return a map with elements from {@code from} up to {@code until}
     */
    ASortedMap<K,V> slice (int from, int until);

    /**
     * Returns the smallest element (if any) or {@link AOption#none()} if this map is empty
     * @return the smallest element
     */
    AOption<Map.Entry<K,V>> smallest();

    /**
     * Returns the greatest element (if any) or {@link AOption#none()} if this map is empty
     * @return the greatest element
     */
    AOption<Map.Entry<K,V>> greatest();

    ASortedSet<K> keySet();
    ASortedSet<Map.Entry<K,V>> entrySet();

    /**
     * Returns an iterator over this map's entries, starting at an optional lower bound
     *
     * @param start the lower bound for keys to iterate over
     * @return an iterator over this map's entries
     */
    AIterator<Map.Entry<K,V>> iterator(AOption<K> start); //TODO upper bound?!

    /**
     * Returns an iterator over this map's keys, starting at an optional lower bound
     *
     * @param start the lower bound for keys to iterate over
     * @return an iterator over this map's keys
     */
    AIterator<K> keysIterator (AOption<K> start);

    /**
     * Returns an iterator over this map's values for which the corresponding keys are greater than or equal than an (optional) lower bound.
     *
     * @param start the lower bound for keys to iterate over
     * @return an iterator over this map's values
     */
    AIterator<V> valuesIterator (AOption<K> start);
}
