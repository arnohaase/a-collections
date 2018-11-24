package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AHashMap;
import com.ajjpj.acollections.mutable.AMutableMapWrapper;
import com.ajjpj.acollections.util.AOption;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * This interface represents a {@link java.util.Map} with additional API. Some of the additional API is map specific, but an AMap also
 *  has {@link ACollectionOps} &lt; {@link Map.Entry} &gt;, i.e. for all intents and purposes behaves like a collection of Map entries.
 * {@code AMap}implementations can be either mutable or immutable. Implementation note: There was no way for AMap to extend both
 * java.util.Map and java.util.Collection because of API clashes, which is why {@link ACollectionOps} was introduced in the first place.
 *
 * <p> This interface defines methods {@link #plus(Object,Object)} and {@link #minus(Object)} for adding / removing elements to an immutable
 *  AMap. (They work on mutable instances as well, but they are not necessary there.) For immutable maps, these methods return new maps with
 *  the new elements, leaving the original unmodified:
 *
 * <p>{@code AMap<Integer,Integer> s0 = AMap.of(1, 11, 2, 22, 3, 33);}
 * <p>{@code ASet<Integer> s1 = s0.plus(5, 55);}
 * <p>{@code ASet<Integer> s2 = s1.minus(2);}
 * <p>{@code System.out.println(s0); // 1->11, 2->22, 3->33 }
 * <p>{@code System.out.println(s1); // 1->11, 2->22, 3->33, 5->55 }
 * <p>{@code System.out.println(s2); // 1->11, 3->33, 5->55 }
 *
 * <p> These calls can of course be chained:

 * <p>{@code ASet<Integer> s3 = s2.plus(8, 88).plus(9, 99).minus(3); }
 * <p>{@code System.out.println(s3); // 1->11, 5->55, 8->88, 9->99 }
 *
 * <p> This interface has static factory methods (Java 9 style) for convenience creating instances. They create immutable {@link AHashMap}
 *  instances.
 *
 * @param <K> The AMap's key type
 * @param <V> The AMap's value type
 */
public interface AMap<K,V> extends Map<K,V>, ACollectionOps<Map.Entry<K,V>>, Iterable<Map.Entry<K,V>> {
    /**
     * This is a convenience factory method wrapping an arbitrary (typically mutable) {@link java.util.Map} in an {@link AMutableMapWrapper}.
     *  This is a simple way to start using a-collections: Wrap an existing {@code Map} to add a rich API while maintaining 100% backwards
     *  compatibility: operations on the wrapper are write-through, i.e. all changes are applied to the underlying {@code Map}.
     *
     * @param m the Map being wrapped
     * @param <K> the Map's key type
     * @param <V> the Map's value type
     * @return the wrapped Map
     */
    static <K,V> AMap<K,V> wrap(Map<K,V> m) {
        return AMutableMapWrapper.wrap(m);
    }

    /**
     * Convenience method for creating an empty {@link AHashMap}. This can later be modified by calling {@link #plus(Object,Object)} or
     * {@link #minus(Object)}. For creating a map with known elements, calling one of the {@code of} factory methods is usually more concise.
     *
     * @param <K> the new map's key type
     * @param <V> the new map's value type
     * @return an empty {@link AHashMap}
     */
    static <K,V> AHashMap<K,V> empty() {
        return AHashMap.empty();
    }

    /**
     * Creates a new {@link AHashMap} based on a {@link java.util.Map}'s elements.
     *
     * @param m the {@link Map} from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    static <K,V> AHashMap<K,V> fromMap(Map<K,V> m) {
        return AHashMap.fromMap(m);
    }

    /**
     * Creates a new {@link AHashMap} based on an {@link Iterable}'s elements.
     *
     * @param coll the {@link Iterable} from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    static <K,V> AHashMap<K,V> from(Iterable<? extends Map.Entry<K,V>> coll) {
        return AHashMap.from(coll);
    }

    /**
     * Creates a new {@link AHashMap} based on an {@link Iterator}'s elements.
     *
     * @param it the {@link Iterator} from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    static <K,V> AHashMap<K,V> fromIterator(Iterator<? extends Entry<K,V>> it) {
        return AHashMap.fromIterator(it);
    }

    /**
     * This is an alias for {@link #empty()} for consistency with Java 9 conventions - it creates an empty {@link AHashMap}.
     *
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return an empty {@link AHashMap}
     */
    static <K,V> AHashMap<K,V> of() {
        return AHashMap.of();
    }

    /**
     * Convenience factory method creating an {@link AHashMap} with exactly one entry.
     *
     * @param k1 the single entry's key
     * @param v1 the single entry's value
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new {@link AHashMap}
     */
    static <K,V> AHashMap<K,V> of(K k1, V v1) {
        return AHashMap.of(k1, v1);
    }

    /**
     * Convenience factory method creating an {@link AHashMap} with exactly two entries.
     *
     * @param k1 the first entry's key
     * @param v1 the first entry's value
     * @param k2 the second entry's key
     * @param v2 the second entry's value
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new {@link AHashMap}
     */
    static <K,V> AHashMap<K,V> of(K k1, V v1, K k2, V v2) {
        return AHashMap.of(k1, v1, k2, v2);
    }

    /**
     * Convenience factory method creating an {@link AHashMap} with three entries.
     *
     * @param k1 the first entry's key
     * @param v1 the first entry's value
     * @param k2 the second entry's key
     * @param v2 the second entry's value
     * @param k3 the third entry's key
     * @param v3 the third entry's value
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new {@link AHashMap}
     */
    static <K,V> AHashMap<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return AHashMap.of(k1, v1, k2, v2, k3, v3);
    }

    /**
     * Convenience factory method creating an {@link AHashMap} with four entries.
     *
     * @param k1 the first entry's key
     * @param v1 the first entry's value
     * @param k2 the second entry's key
     * @param v2 the second entry's value
     * @param k3 the third entry's key
     * @param v3 the third entry's value
     * @param k4 the fourth entry's key
     * @param v4 the fourth entry's value
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new {@link AHashMap}
     */
    static <K,V> AHashMap<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return AHashMap.of(k1, v1, k2, v2, k3, v3, k4, v4);
    }

    /**
     * This is an alias for {@link #from(Iterable)} for consistency with Java 9 conventions - it creates an AMap from an Iterable of
     * {@link Map.Entry}.
     *
     * @param coll the entries
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new {@link AHashMap}
     */
    static <K,V> AHashMap<K,V> ofEntries(Iterable<Map.Entry<K,V>> coll) {
        return AHashMap.ofEntries(coll);
    }

    boolean containsKey(Object key);

    V get(Object key);

    /**
     * Returns the value this map holds for a given key (if any), wrapped in {@link AOption#some(Object)}, or {@link AOption#none()} if
     *  this Map holds no entry for the key.
     *
     * <p> This method allows callers to reliably distinguish whether this map holds an entry with {@code null} as a value, or holds no
     *  entry at all for a given key.
     *
     * @param key the key for which the entry is looked up
     * @return the value this map holds for the key
     */
    AOption<V> getOptional(K key);

    /**
     * Adds an entry to this map, replacing an existing entry if it exists with an equal key, returning the modified map.
     *
     * <p> For a mutable AMap, this is equivalent to calling {@link java.util.Map#put(Object, Object)}; for an immutable AMap, the method
     *  returns a new instance with the new entry.
     *
     * @param key   the key
     * @param value the value
     * @return the map with the new entry
     */
    AMap<K,V> plus(K key, V value);

    /**
     * Adds an entry to this map, replacing an existing entry if it exists with an equal key, returning the modified map.
     *
     * <p> For a mutable AMap, this is equivalent to calling {@link java.util.Map#put(Object, Object)}; for an immutable AMap, the method
     *  returns a new instance with the new entry.
     *
     * @param entry the new key/value pair
     * @return the map with the new entry
     */
    AMap<K,V> plus(Map.Entry<K,V> entry);

    /**
     * Removes an entry from this map (if there is an entry for a given key), returning the modified map.
     *
     * <p> For a mutable AMap, this is equivalent to calling {@link java.util.Map#remove(Object)}; for an immutable AMap, the method
     *  returns a new instance with the new entry.
     *
     * @param key   the key for which the entry is removed if it exists
     * @return the (potentially) modified map
     */
    AMap<K,V> minus(K key);

    /**
     * Adds entries to this map, replacing existing entries if they exists, returning the modified map.
     *
     * <p> For a mutable AMap, this is equivalent to calling {@link java.util.Map#putAll(Map)}; for an immutable AMap, the method
     *  returns a new instance with the new entry.
     *
     * @param other the map whose entries are added to this map
     * @param <K1> the other map's key type
     * @param <V1> the other map's value type
     * @return the map with the new entries
     */
    default <K1 extends K, V1 extends V> AMap<K,V> plusAll (Map<K1, V1> other) {
        AMap<K,V> result = this;
        for (Map.Entry<K1,V1> e: other.entrySet()) {
            result = result.plus(e.getKey(), e.getValue());
        }
        return result;
    }

    @Override AMap<K, V> filter (Predicate<Entry<K, V>> f);
    @Override AMap<K, V> filterNot (Predicate<Entry<K, V>> f);

    /**
     * Filters this map by testing its keys against a predicate. Entries are retained if and only if their key matches the predicate. This
     *  is a simpler alternative to {@link #filter(Predicate)} for predicates that depend only on an entry's key and not on its value.
     *
     * <p> This method creates and returns a new map.
     *
     * @param f the predicate to test against
     * @return the filtered map
     * @see #filter(Predicate)
     */
    AMap<K,V> filterKeys(Predicate<K> f);

    /**
     * Creates a new AMap, transforming the value associated with each key. This is a simpler alternative to {@link #map} for cases where
     *  only the value is transformed. More specifically, this method is equivalent to calling
     * {@code this.map(e -> new AbstractMap.SimpleEntry(e.getKey(), f.apply(e.getValue()))}.
     *
     * @param f   the transformation method for each entry's value
     * @param <U> the new values' type
     * @return the transformed AMap
     * @see #map(Function)
     */
    <U> AMap<K,U> mapValues(Function<V,U> f);

    /**
     * Returns a new AMap containing the same entries as this AMap, but which returns a default value when {@link #get(Object)} or
     *  {@link #getOptional(Object)} are called for a key it does not contain.
     *
     * <p> The method does not modify the map's contents, and calling {@link #get(Object)} for a non-existing key does not add the default
     *  value to the map.
     *
     * <p> {@code AMap<Integer,Integer> m = AMap.of(1, 11);}
     * <p> {@code AMap<Integer,Integer> wr = m.withDefaultValue(99);}
     * <p> {@code m.get(2); // null}
     * <p> {@code m.getOptional(2); // AOption.none()}
     * <p> {@code wr.get(2); // 99}
     * <p> {@code wr.getOptional(2); // AOption.some(99)}
     * <p> {@code wr.containsKey(2); // still false}
     * <p> {@code wr = wr.plus(3, 17);}
     * <p> {@code wr.get(5); // 99: the default value still applies after the update}
     *
     * <p> This method is useful e.g. for aggregating or counting data for a given key. The following snippet counts occurrences of words
     *  in a text:
     *
     * <p> {@code AMap<String,Integer> wordCount = AMutableMapWrapper.<String,Integer>empty().withDefaultValue(0);}
     * <p> {@code String text = ...;}
     * <p> {@code for (String word: text.split(" ")) wordCount.put(word, wordCount.get(word)+1); }
     *
     * @param defaultValue the value that should be returned if the map does not contain a key
     * @return the wrapped AMap returning the default value
     */
    AMap<K,V> withDefaultValue(V defaultValue);

    /**
     * Returns a new AMap containing the same entries as this AMap, but which calculates and returns a default value based on the key
     *  when {@link #get(Object)} or {@link #getOptional(Object)} are called for a key it does not contain.
     *
     * <p> The method does not modify the map's contents, and calling {@link #get(Object)} for a non-existing key does not add the default
     *  value to the map. The map does not "remember" the provided default for a key - it recalculates the default value for each call to
     *  {@link #get(Object)}.
     *
     * <p> {@code AMap<Integer,Integer> m = AMap.of(1, 5).withDerivedDefaultValue(n -> n*11);}
     * <p> {@code m.get(2); // 22}
     * <p> {@code m.getOptional(2); // AOption.some(22)}
     * <p> {@code m.containsKey(2); // still false}
     * <p> {@code m = m.plus(2, 9);}
     * <p> {@code m.get(2); // 9}
     * <p> {@code m.containsKey(2); // true}
     * <p> {@code m.get(3); // 33: the default value still applies after the update}
     *
     * @param defaultProvider the function called to calculate the value for unknown keys
     * @return the wrapped AMap returning the default values
     */

    AMap<K,V> withDerivedDefaultValue(Function<K,V> defaultProvider);

    ASet<K> keySet();
    ACollection<V> values();
    ASet<Map.Entry<K,V>> entrySet();

    /**
     * Returns an {@link AIterator} over this map's keys.
     *
     * @return an iterator over this map's keys
     */
    AIterator<K> keysIterator();

    /**
     * Returns an {@link AIterator} over this map's values. Iteration order is the same as for {@link #iterator()}; more specifically
     *  calling this method is equivalent to (though potentially more efficient than) calling {@code this.iterator().map(e -> e.getValue())}.
     *  If this map contains the same value for different keys, the iterator returned by this method will contain the same value several
     *  times.
     *
     * @return an iterator over this map's values
     */
    AIterator<V> valuesIterator();
}
