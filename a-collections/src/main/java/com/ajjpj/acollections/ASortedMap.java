package com.ajjpj.acollections;

import com.ajjpj.acollections.mutable.AMutableMapWrapper;
import com.ajjpj.acollections.mutable.AMutableSortedMapWrapper;
import com.ajjpj.acollections.util.AOption;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


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
public interface ASortedMap<K,V> extends AMap<K,V>, NavigableMap<K,V> {
    /**
     * This is a convenience factory method wrapping an arbitrary (typically mutable) {@link java.util.NavigableMap} in an {@link AMutableSortedMapWrapper}.
     *  This is a simple way to start using a-collections: Wrap an existing {@code NavigableMap} to add a rich API while maintaining 100% backwards
     *  compatibility: operations on the wrapper are write-through, i.e. all changes are applied to the underlying {@code NavigableMap}.
     *
     * @param m the Map being wrapped
     * @param <K> the Map's key type
     * @param <V> the Map's value type
     * @return the wrapped Map
     */
    static <K,V> AMutableSortedMapWrapper<K,V> wrap(NavigableMap<K,V> m) {
        return AMutableSortedMapWrapper.wrap(m);
    }


    /**
     * Returns the comparator used to compare keys to determine element ordering and equality
     *
     * @return the comparator used by this map
     */
    Comparator<? super K> comparator();

    @Override ASortedMap<K, V> filter (Predicate<Entry<K, V>> f);
    @Override ASortedMap<K, V> filterNot (Predicate<Entry<K, V>> f);
    @Override ASortedMap<K, V> filterKeys (Predicate<K> f);

    @Override ASortedMap<K, V> withDefaultValue (V defaultValue);
    @Override ASortedMap<K, V> withDerivedDefaultValue (Function<K, V> defaultProvider);

    /**
     * Counts all the nodes with keys greater than or equal to a lower bound and less than an upper bound. Both bounds are optional.
     *
     * @param from the lower bound
     * @param to   the upper bound
     * @return the number of entries in the given key range
     */
    default int countInRange (AOption<K> from, AOption<K> to) {
        return countInRange(from, true, to, false);
    }

    /**
     * Counts all the nodes with keys between a lower and upper bound. Flags control whether the bounds are inclusive or exclusive.
     *  Both bounds are optional.
     *
     * @param from          the lower bound
     * @param fromInclusive controls if the lower bound is inclusive or not
     * @param to            the upper bound
     * @param toInclusive   controls if the upper bound is inclusive or not
     * @return the number of entries in the given key range
     */
    int countInRange (AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive);

    /**
     * Returns a map containing only entries with between a lower and an upper bound.
     *
     * <p> Flags control whether the bounds are inclusive or exclusive. Both bounds are optional.
     *
     * @param from         the lower bound
     * @param fromInclusive controls whether the lower bound is inclusive or exclusive
     * @param to            the upper bound
     * @param toInclusive   controls whether the lower bound is inclusive or exclusive
     * @return the map with keys in the given range
     */
    ASortedMap<K,V> range (AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive);

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
     * This method returns a map with entries starting with index {@code from} up to index {@code to}
     *  (exclusively). More formally, this method is equivalent to
     *
     * <pre>
     * {@code this.drop(from).take(to - Math.max(from, 0))}
     * </pre>
     *
     * <p> {@code from} may be negative and {@code to} may be greater than {@code this.size()}, in which case they
     *  are treated as {@code 0} and {@code this.size()}, respectively.
     *
     * @param from  the first 'index' to keep
     * @param to the upper bound (exclusive) of 'indices' to keep
     * @return a map with elements from {@code from} up to {@code to}
     */
    ASortedMap<K,V> slice (int from, int to);

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
     * Returns an iterator over this map's entries with keys between a lower and an upper bound.
     *
     * <p> Flags control whether the bounds are inclusive or exclusive. Both bounds are optional.
     *
     * @param from          the lower bound for keys to iterate over
     * @param fromInclusive controls whether the lower bound is inclusive or exclusive
     * @param to            the upper bound for keys to iterate over
     * @param toInclusive   controls whether the upper bound is inclusive or exclusive
     * @return an iterator over this map's entries
     */
    AIterator<Map.Entry<K,V>> iterator(AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive);

    /**
     * Returns an iterator over this map's keys between a lower and an upper bound.
     *
     * <p> Flags control whether the bounds are inclusive or exclusive. Both bounds are optional.
     *
     * @param from          the lower bound for keys to iterate over
     * @param fromInclusive controls whether the lower bound is inclusive or exclusive
     * @param to            the upper bound for keys to iterate over
     * @param toInclusive   controls whether the upper bound is inclusive or exclusive
     * @return an iterator over this map's keys
     */
    AIterator<K> keysIterator (AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive);

    /**
     * Returns an iterator over this map's values for which the corresponding keys are greater than or equal than an (optional) lower bound
     *  and smaller than an (optional) upper bound.
     *
     * @param from the lower bound for keys to iterate over
     * @param to the upper bound for keys to iterate over
     * @return an iterator over this map's values
     */
    AIterator<V> valuesIterator (AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive);

    @Override ASortedMap<K, V> descendingMap ();

    @Override ASortedSet<K> navigableKeySet ();

    @Override ASortedSet<K> descendingKeySet ();

    @Override ASortedMap<K, V> subMap (K fromKey, boolean fromInclusive, K toKey, boolean toInclusive);

    @Override ASortedMap<K, V> headMap (K toKey, boolean inclusive);

    @Override ASortedMap<K, V> tailMap (K fromKey, boolean inclusive);

    @Override ASortedMap<K, V> subMap (K fromKey, K toKey);

    @Override ASortedMap<K, V> headMap (K toKey);

    @Override ASortedMap<K, V> tailMap (K fromKey);
}
