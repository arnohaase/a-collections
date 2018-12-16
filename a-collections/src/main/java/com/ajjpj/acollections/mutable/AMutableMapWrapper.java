package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.immutable.*;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AMapDefaults;
import com.ajjpj.acollections.internal.AMapSupport;
import com.ajjpj.acollections.util.AOption;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;


/**
 * This class wraps any {@link java.util.Map} as an {@link AMap}. All modifying operations - both those from {@link java.util.Map} and
 *  those added by {@link AMap} - write through to the wrapped map.
 *
 * <p> This is a simple way to start using a-collections: Wrap an existing {@link java.util.Map} to add a rich API while maintaining
 *  100% backwards compatibility: operations on the wrapper are write-through, i.e. all changes are applied to the underlying {@code Map}.
 *  This class makes it simple to use {@link AMap}'s rich API on any map in an ad-hoc fashion.
 *
 * <p> The wrapped map with all modifications applied to it can always be retrieved by calling {@link #getInner()}, though there is
 *  usually no reason for unwrapping: {@code AMutableMapWrapper} implements {@link java.util.Map}, so any method accepting
 *  {@link java.util.Map} will also accept an {@code AMutableMapWrapper} as is.
 *
 * @param <K> the map's key type
 * @param <V> the map's value type
 */
public class AMutableMapWrapper<K,V> implements AMapDefaults<K, V, AMutableMapWrapper<K,V>>, ACollectionDefaults<Map.Entry<K,V>, AMutableMapWrapper<K,V>>, Serializable {
    private final Map<K,V> inner;
    private Function<K,V> defaultProvider = null;

    /**
     * Convenience method for creating an empty map. For creating a map with known elements, calling one of the {@code of}
     *  factory methods is a more concise alternative.
     *
     * @param <K> the new map's key type
     * @param <V> the new map's value type
     * @return an empty map
     */
    public static <K,V> AMutableMapWrapper<K,V> empty() {
        return new AMutableMapWrapper<>(new HashMap<>());
    }

    /**
     * Creates a new map based on an {@link java.util.Map}'s elements.
     *
     * NB: This method has the same signature as {@link #wrap(Map)}, but it copies the other map's contents rather than wrapping it.
     *
     * @param m the map from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K,V> AMutableMapWrapper<K,V> fromMap(Map<K,V> m) {
        return from(m.entrySet());
    }

    /**
     * Creates a new map based on an Iterable's elements.
     *
     * @param coll the Iterable from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K,V> AMutableMapWrapper<K,V> from(Iterable<? extends Map.Entry<K,V>> coll) {
        return AMutableMapWrapper.<K,V>builder().addAll(coll).build();
    }

    /**
     * Creates a new map based on an iterator's elements.
     *
     * @param it the iterator from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new list
     */
    public static <K,V> AMutableMapWrapper<K,V> fromIterator(Iterator<? extends Map.Entry<K,V>> it) {
        return AMutableMapWrapper.<K,V>builder().addAll(it).build();
    }

    /**
     * This is an alias for {@link #empty()} for consistency with Java 9 conventions - it creates an empty map.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> map instance - that is the
     *  whole point of class {@link AMutableMapWrapper}. If you want immutable maps, use {@link AMap#of()}.
     *
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return an empty map
     */
    public static <K,V> AMutableMapWrapper<K,V> of() {
        return empty();
    }

    /**
     * Creates a map with exactly one entry.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> map instance - that is the
     *  whole point of class {@link AMutableMapWrapper}. If you want immutable maps, use {@link AMap#of()}.
     *
     * @param k1 the entry's key
     * @param v1 the entry's value
     *
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K,V> AMutableMapWrapper<K,V> of(K k1, V v1) {
        return AMutableMapWrapper.<K,V>builder().add(k1, v1).build();
    }

    /**
     * Creates a map with exactly two entries.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> map instance - that is the
     *  whole point of class {@link AMutableMapWrapper}. If you want immutable maps, use {@link AMap#of()}.
     *
     * @param k1 the first entry's key
     * @param v1 the first entry's value
     * @param k2 the second entry's key
     * @param v2 the second entry's value
     *
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K,V> AMutableMapWrapper<K,V> of(K k1, V v1, K k2, V v2) {
        return AMutableMapWrapper.<K,V>builder().add(k1, v1).add(k2, v2).build();
    }

    /**
     * Creates a map with exactly three entries.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> map instance - that is the
     *  whole point of class {@link AMutableMapWrapper}. If you want immutable maps, use {@link AMap#of()}.
     *
     * @param k1 the first entry's key
     * @param v1 the first entry's value
     * @param k2 the second entry's key
     * @param v2 the second entry's value
     * @param k3 the third entry's key
     * @param v3 the third entry's value
     *
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K,V> AMutableMapWrapper<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return AMutableMapWrapper.<K,V>builder().add(k1, v1).add(k2, v2).add(k3,v3).build();
    }

    /**
     * Creates a map with exactly four entries.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> map instance - that is the
     *  whole point of class {@link AMutableMapWrapper}. If you want immutable maps, use {@link AMap#of()}.
     *
     * @param k1 the first entry's key
     * @param v1 the first entry's value
     * @param k2 the second entry's key
     * @param v2 the second entry's value
     * @param k3 the third entry's key
     * @param v3 the third entry's value
     * @param k4 the fourth entry's key
     * @param v4 the fourth entry's value
     *
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K,V> AMutableMapWrapper<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return AMutableMapWrapper.<K,V>builder().add(k1, v1).add(k2, v2).add(k3,v3).add(k4,v4).build();
    }

    /**
     * This is an alias for {@link #from(Iterable)} for consistency with Java 9 conventions - it creates an AMap from an Iterable of
     * {@link Map.Entry}.
     *
     * @param coll the entries
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K,V> AMutableMapWrapper<K,V> ofEntries(Iterable<Map.Entry<K,V>> coll) {
        return from(coll);
    }

    /**
     * This factory method wraps an arbitrary (typically mutable) {@link java.util.Map} in an {@link AMutableMapWrapper}.
     *  This is a simple way to start using a-collections: Wrap an existing {@code Map} to add a rich API while maintaining 100% backwards
     *  compatibility: operations on the wrapper are write-through, i.e. all changes are applied to the underlying {@code Map}.
     *
     * @param inner the map being wrapped
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the wrapped map
     */
    public static <K,V> AMutableMapWrapper<K,V> wrap(Map<K,V> inner) {
        if (inner instanceof AMutableMapWrapper) return (AMutableMapWrapper<K, V>) inner;
        return new AMutableMapWrapper<>(inner);
    }

    private AMutableMapWrapper (Map<K, V> inner) {
        this.inner = Objects.requireNonNull(inner);
    }

    /**
     * Returns the wrapped map to which all modifications were applied.
     *
     * NB: AMutableMapWrapper implements {@link java.util.Map}, so usually there is no reason to unwrap it. Any API accepting
     *  {@link java.util.Map} accepts an {@link AMutableMapWrapper} as is.
     *
     * @return the wrapped map
     */
    public Map<K,V> getInner() {
        return inner;
    }

    @Override public AMutableMapWrapper<K, V> withDefaultValue (V defaultValue) {
        this.defaultProvider = new AMapSupport.SerializableConstantFunction<>(defaultValue);
        return this;
    }

    @Override public AMutableMapWrapper<K, V> withDerivedDefaultValue (Function<K, V> defaultProvider) {
        this.defaultProvider = defaultProvider;
        return this;
    }

    @Override public ATreeSet<Entry<K, V>> toSortedSet () {
        throw new UnsupportedOperationException("pass in a Comparator<Map.Entry> - Map.Entry has no natural order");
    }
    @Override public ATreeSet<Entry<K, V>> toMutableSortedSet () {
        throw new UnsupportedOperationException("pass in a Comparator<Map.Entry> - Map.Entry has no natural order");
    }

    @Override public boolean containsKey (Object key) {
        return inner.containsKey(key);
    }

    @Override public V get (Object key) {
        if (defaultProvider != null) //noinspection unchecked
            return getOptional((K) key).orElseGet(() -> defaultProvider.apply((K) key));
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
        return AMutableMapWrapper.fromIterator(iterator().filter(f)).withDerivedDefaultValue(defaultProvider);
    }
    @Override public AMutableMapWrapper<K, V> filterNot (Predicate<Entry<K, V>> f) {
        return AMutableMapWrapper.fromIterator(iterator().filterNot(f)).withDerivedDefaultValue(defaultProvider);
    }
    @Override public AMutableMapWrapper<K, V> filterKeys (Predicate<K> f) {
        return AMapDefaults.super.filterKeys(f).withDerivedDefaultValue(defaultProvider);
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

    /**
     * Returns a new {@link ACollectionBuilder} for building an AMutableMapWrapper efficiently and in a generic manner.
     *
     * @param <K> the builder's key type
     * @param <V> the builder's value type
     * @return an new {@link ACollectionBuilder}
     */
    public static <K,V> Builder<K,V> builder() {
        return new Builder<>();
    }

    public static class Builder<K,V> implements ACollectionBuilder<Map.Entry<K,V>, AMutableMapWrapper<K,V>> {
        private final Map<K,V> result = new HashMap<>();

        public Builder<K, V> add (K key, V value) {
            result.put(key, value);
            return this;
        }

        @Override public Builder<K, V> add (Entry<K, V> el) {
            result.put(el.getKey(), el.getValue());
            return this;
        }

        @Override public Builder<K, V> addAll (Iterator<? extends Entry<K, V>> it) {
            while(it.hasNext()) add(it.next());
            return this;
        }

        @Override public Builder<K, V> addAll (Iterable<? extends Entry<K, V>> coll) {
            return addAll(coll.iterator());
        }

        @Override public Builder<K, V> addAll (Entry<K, V>[] coll) {
            return addAll(Arrays.asList(coll));
        }

        @Override public AMutableMapWrapper<K, V> build () {
            return new AMutableMapWrapper<>(result);
        }
    }

}
