package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.immutable.*;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AMapSupport;
import com.ajjpj.acollections.util.AOption;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * TODO javadoc
 */
public class AMutableSortedMapWrapper<K,V> implements ASortedMap<K,V>, ACollectionDefaults<Map.Entry<K,V>, AMutableSortedMapWrapper<K,V>>, Serializable {
    private final NavigableMap<K,V> inner;
    private Function<K,V> defaultProvider = null;

    private AMutableSortedMapWrapper (NavigableMap<K, V> inner) {
        this.inner = Objects.requireNonNull(inner);
    }

    public static <K,V> AMutableSortedMapWrapper<K,V> wrap(NavigableMap<K,V> inner) {
        return new AMutableSortedMapWrapper<>(inner);
    }

    public static <K extends Comparable<K>,V> Builder<K,V> builder() {
        return builder(Comparator.naturalOrder());
    }

    public static <K,V> Builder<K,V> builder(Comparator<? super K> comparator) {
        return new Builder<>(comparator);
    }

    /**
     * Convenience method for creating an empty {@link AMutableSortedMapWrapper} with {@link Comparator#naturalOrder()}. This can later be modified by
     *  calling {@link #plus(Object,Object)} or {@link #minus(Object)}. For creating a map with known elements, calling one of the
     *  {@code of} factory methods is usually more concise.
     *
     * @param <K> the new map's key type
     * @param <V> the new map's value type
     * @return an empty {@link AHashMap}
     */
    public static <K extends Comparable<K>,V> AMutableSortedMapWrapper<K,V> empty() {
        return new AMutableSortedMapWrapper<>(new TreeMap<>(Comparator.<K>naturalOrder()));
    }

    /**
     * Convenience method for creating an empty {@link AMutableSortedMapWrapper}. This can later be modified by calling {@link #plus(Object,Object)} or
     * {@link #minus(Object)}. For creating a map with known elements, calling one of the {@code of} factory methods is usually more concise.
     *
     * @param comparator the tree map's comparator
     *
     * @param <K> the new map's key type
     * @param <V> the new map's value type
     * @return an empty {@link AHashMap}
     */
    public static <K,V> AMutableSortedMapWrapper<K,V> empty(Comparator<? super K> comparator) {
        return new AMutableSortedMapWrapper<>(new TreeMap<>(comparator));
    }

    /**
     * Creates a new {@link AMutableSortedMapWrapper} based on an {@link Iterator}'s elements using {@link Comparator#naturalOrder()}.
     *
     * @param it the {@link Iterator} from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K extends Comparable<K>,V> AMutableSortedMapWrapper<K,V> fromIterator(Iterator<? extends Entry<K,V>> it) {
        return fromIterator(it, Comparator.naturalOrder());
    }

    /**
     * Creates a new {@link AMutableSortedMapWrapper} based on an {@link Iterator}'s elements using a comparator provided by the caller.
     *
     * @param it         the {@link Iterator} from which the new map is initialized
     * @param comparator the key comparator to use
     *
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K,V> AMutableSortedMapWrapper<K,V> fromIterator(Iterator<? extends Entry<K,V>> it, Comparator<? super K> comparator) {
        return AMutableSortedMapWrapper.<K,V> builder(comparator).addAll(it).build();
    }

    /**
     * Creates a new {@link AMutableSortedMapWrapper} based on a {@link java.util.Map}'s elements using {@link Comparator#naturalOrder()}.
     *
     * @param m the {@link Map} from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K extends Comparable<K>,V> AMutableSortedMapWrapper<K,V> fromMap(Map<K,V> m) {
        return from(m.entrySet());
    }

    /**
     * Creates a new {@link AMutableSortedMapWrapper} based on a {@link java.util.Map}'s elements using a comparator provided by the caller.
     *
     * @param m          the {@link Map} from which the new map is initialized
     * @param comparator the key comparator to use
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K extends Comparable<K>,V> AMutableSortedMapWrapper<K,V> fromMap(Map<K,V> m, Comparator<? super K> comparator) {
        return from(m.entrySet(), comparator);
    }

    /**
     * Creates a new {@link AMutableSortedMapWrapper} based on an {@link Iterable}'s elements using {@link Comparator#naturalOrder()}.
     *
     * @param coll the {@link Iterable} from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K extends Comparable<K>,V> AMutableSortedMapWrapper<K,V> from(Iterable<? extends Entry<K,V>> coll) {
        return from(coll, Comparator.naturalOrder());
    }

    /**
     * Creates a new {@link AMutableSortedMapWrapper} based on an {@link Iterator}'s elements using a comparator provided by the caller.
     *
     * @param it         the {@link Iterator} from which the new map is initialized
     * @param comparator the key comparator to use
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K,V> AMutableSortedMapWrapper<K,V> from(Iterable<? extends Entry<K,V>> it, Comparator<? super K> comparator) {
        return AMutableSortedMapWrapper.<K,V> builder(comparator).addAll(it).build();
    }

    /**
     * This is an alias for {@link #empty()} for consistency with Java 9 conventions - it creates an empty {@link AMutableSortedMapWrapper}
     *  using {@link Comparator#naturalOrder()}.
     *
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return an empty {@link AMutableSortedMapWrapper}
     */
    public static <K extends Comparable<K>,V> AMutableSortedMapWrapper<K,V> of() {
        return empty(Comparator.<K>naturalOrder());
    }

    /**
     * Convenience factory method creating an {@link AMutableSortedMapWrapper} with exactly one entry using {@link Comparator#naturalOrder()}.
     *
     * @param k1 the single entry's key
     * @param v1 the single entry's value
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new {@link AMutableSortedMapWrapper}
     */
    public static <K extends Comparable<K>,V> AMutableSortedMapWrapper<K,V> of(K k1, V v1) {
        return AMutableSortedMapWrapper.<K,V>builder().add(k1, v1).build();
    }

    /**
     * Convenience factory method creating an {@link AMutableSortedMapWrapper} with exactly two entries using {@link Comparator#naturalOrder()}.
     *
     * @param k1 the first entry's key
     * @param v1 the first entry's value
     * @param k2 the second entry's key
     * @param v2 the second entry's value
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new {@link AMutableSortedMapWrapper}
     */
    public static <K extends Comparable<K>,V> AMutableSortedMapWrapper<K,V> of(K k1, V v1, K k2, V v2) {
        return AMutableSortedMapWrapper.<K,V>builder().add(k1, v1).add(k2, v2).build();
    }

    /**
     * Convenience factory method creating an {@link AMutableSortedMapWrapper} with three entries using {@link Comparator#naturalOrder()}.
     *
     * @param k1 the first entry's key
     * @param v1 the first entry's value
     * @param k2 the second entry's key
     * @param v2 the second entry's value
     * @param k3 the third entry's key
     * @param v3 the third entry's value
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new {@link AMutableSortedMapWrapper}
     */
    public static <K extends Comparable<K>,V> AMutableSortedMapWrapper<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return AMutableSortedMapWrapper.<K,V>builder().add(k1, v1).add(k2, v2).add(k3,v3).build();
    }

    /**
     * Convenience factory method creating an {@link AMutableSortedMapWrapper} with four entries using {@link Comparator#naturalOrder()}.
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
     * @return the new {@link AMutableSortedMapWrapper}
     */
    public static <K extends Comparable<K>,V> AMutableSortedMapWrapper<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return AMutableSortedMapWrapper.<K,V>builder().add(k1, v1).add(k2, v2).add(k3,v3).add(k4,v4).build();
    }

    /**
     * This is an alias for {@link #from(Iterable)} for consistency with Java 9 conventions - it creates an AMutableSortedMapWrapper from an Iterable of
     * {@link Map.Entry} using {@link Comparator#naturalOrder()}.
     *
     * @param coll the entries
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new {@link AMutableSortedMapWrapper}
     */
    public static <K extends Comparable<K>,V> AMutableSortedMapWrapper<K,V> ofEntries(Iterable<Map.Entry<K,V>> coll) {
        return from(coll);
    }
    @Override public Comparator<? super K> comparator () {
        return inner.comparator();
    }

    @Override public int countInRange (AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive) {
        return iterator(from, fromInclusive, to, toInclusive).count(x -> true);
    }

    @Override public AMutableSortedMapWrapper<K, V> range (AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive) {
        if (from.isDefined() && to.isDefined()) return subMap(from.get(), fromInclusive, to.get(), toInclusive);
        if (from.isDefined()) return tailMap(from.get(), fromInclusive);
        if (to.isDefined()) return headMap(to.get(), toInclusive);
        return this;
    }

    @Override public AMutableSortedMapWrapper<K, V> drop (int n) {
        if (inner.size() <= n) {
            inner.clear();
        }
        else {
            for (int i=0; i<n; i++) {
                inner.remove(inner.firstKey());
            }
        }
        return this;
    }

    @Override public AMutableSortedMapWrapper<K, V> take (int n) {
        if (n >= size()) return this;

        Iterator<Map.Entry<K,V>> iterator = inner.entrySet().iterator();
        for (int i = 0; i< n; i++){
            iterator.next();
        }
        while(iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        return this;
    }

    @Override public AMutableSortedMapWrapper<K, V> slice (int from, int to) {
        return drop(from).take(to - Math.max(from, 0));
    }

    @Override public AOption<Entry<K, V>> smallest () {
        return isEmpty() ? AOption.none() : AOption.some(firstEntry());
    }

    @Override public AOption<Entry<K, V>> greatest () {
        return isEmpty() ? AOption.none() : AOption.some(lastEntry());
    }

    @Override public ASortedSet<K> keySet () {
        return AMutableSortedSetWrapper.wrap(inner.navigableKeySet());
    }

    @Override public ASortedSet<Entry<K, V>> entrySet () {
        return ASortedSet.from(inner.entrySet(), new AMapSupport.EntryComparator<>(comparator()));
    }

    @Override public AIterator<Entry<K, V>> iterator (AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive) {
        if (from.isDefined() && to.isDefined()){
            return AIterator.wrap(inner.subMap(from.get(), fromInclusive, to.get(), toInclusive).entrySet().iterator());
        }
        if (from.isDefined()){
            return AIterator.wrap(inner.tailMap(from.get(), fromInclusive).entrySet().iterator());
        }
        if (to.isDefined()){
            return AIterator.wrap(inner.headMap(to.get(), toInclusive).entrySet().iterator());
        }
        return AIterator.wrap(inner.entrySet().iterator());
    }

    @Override //TODO
    public AIterator<K> keysIterator (AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive) {
        if (from.isDefined() && to.isDefined()){
            return AIterator.wrap(inner.subMap(from.get(), fromInclusive, to.get(), toInclusive).keySet().iterator());
        }
        if (from.isDefined()){
            return AIterator.wrap(inner.tailMap(from.get(), fromInclusive).keySet().iterator());
        }
        if (to.isDefined()){
            return AIterator.wrap(inner.headMap(to.get(), toInclusive).keySet().iterator());
        }
        return AIterator.wrap(inner.keySet().iterator());
    }

    @Override
    public AIterator<V> valuesIterator (AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive) {
        if (from.isDefined() && to.isDefined()){
            return AIterator.wrap(inner.subMap(from.get(), fromInclusive, to.get(), toInclusive).values().iterator());
        }
        if (from.isDefined()){
            return AIterator.wrap(inner.tailMap(from.get(), fromInclusive).values().iterator());
        }
        if (to.isDefined()){
            return AIterator.wrap(inner.headMap(to.get(), toInclusive).values().iterator());
        }
        return AIterator.wrap(inner.values().iterator());
    }

    @Override public AMutableSortedMapWrapper<K, V> descendingMap () {
        return AMutableSortedMapWrapper.wrap(inner.descendingMap()).withDerivedDefaultValue(defaultProvider);
    }

    @Override public AMutableSortedSetWrapper<K> navigableKeySet () {
        return AMutableSortedSetWrapper.wrap(inner.navigableKeySet());
    }

    @Override public AMutableSortedSetWrapper<K> descendingKeySet () {
        return AMutableSortedSetWrapper.wrap(inner.descendingKeySet());
    }

    @Override public AMutableSortedMapWrapper<K, V> subMap (K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return AMutableSortedMapWrapper.wrap(inner.subMap(fromKey, fromInclusive, toKey, toInclusive)).withDerivedDefaultValue(defaultProvider);
    }

    @Override public AMutableSortedMapWrapper<K, V> headMap (K toKey, boolean inclusive) {
        return AMutableSortedMapWrapper.wrap(inner.headMap(toKey, inclusive)).withDerivedDefaultValue(defaultProvider);
    }

    @Override public AMutableSortedMapWrapper<K, V> tailMap (K fromKey, boolean inclusive) {
        return AMutableSortedMapWrapper.wrap(inner.tailMap(fromKey, inclusive)).withDerivedDefaultValue(defaultProvider);
    }

    @Override public AMutableSortedMapWrapper<K, V> subMap (K fromKey, K toKey) {
        return AMutableSortedMapWrapper.wrap(inner.subMap(fromKey, true, toKey, false)).withDerivedDefaultValue(defaultProvider);
    }

    @Override public AMutableSortedMapWrapper<K, V> headMap (K toKey) {
        return AMutableSortedMapWrapper.wrap(inner.headMap(toKey, false)).withDerivedDefaultValue(defaultProvider);
    }

    @Override public AMutableSortedMapWrapper<K, V> tailMap (K fromKey) {
        return AMutableSortedMapWrapper.wrap(inner.tailMap(fromKey, true)).withDerivedDefaultValue(defaultProvider);
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

    @Override public AMutableSortedMapWrapper<K, V> plus (K key, V value) {
        inner.put(key, value);
        return this;
    }

    @Override public AMutableSortedMapWrapper<K, V> plus (Entry<K, V> entry) {
        inner.put(entry.getKey(), entry.getValue());
        return this;
    }

    @Override public AMutableSortedMapWrapper<K, V> minus (K key) {
        inner.remove(key);
        return this;
    }

    @Override public AMutableSortedMapWrapper<K, V> filterKeys (Predicate<K> f) {
        return AMutableSortedMapWrapper.fromIterator(iterator().filter(e -> f.test(e.getKey())), comparator()).withDerivedDefaultValue(defaultProvider);
    }

    @Override public <U> AMutableSortedMapWrapper<K, U> mapValues (Function<V, U> f) {
        return AMutableSortedMapWrapper.fromIterator(iterator().map(e -> new AbstractMap.SimpleImmutableEntry<>(e.getKey(), f.apply(e.getValue()))), comparator());
    }

    @Override public AMutableSortedMapWrapper<K, V> withDefaultValue (V defaultValue) {
        this.defaultProvider = new AMapSupport.SerializableConstantFunction<>(defaultValue);
        return this;
    }

    @Override public AMutableSortedMapWrapper<K, V> withDerivedDefaultValue (Function<K, V> defaultProvider) {
        this.defaultProvider = defaultProvider;
        return this;
    }

    @Override public ACollection<V> values () {
        return new AMapSupport.ValuesCollection<>(this);
    }

    @Override public AIterator<K> keysIterator () {
        return AIterator.wrap(inner.keySet().iterator());
    }

    @Override public AIterator<V> valuesIterator () {
        return AIterator.wrap(inner.values().iterator());
    }

    @Override public AIterator<Entry<K, V>> iterator () {
        return AIterator.wrap(inner.entrySet().iterator());
    }

    @Override public <U> ACollectionBuilder<U, ? extends ACollectionOps<U>> newBuilder () {
        throw new UnsupportedOperationException("Implementing this well goes beyond the boundaries of Java's type system.");
    }

    @Override public <K1, V1> ACollectionBuilder<Entry<K1, V1>, ? extends ACollectionOps<Entry<K1, V1>>> newEntryBuilder () {
        //noinspection unchecked
        return new Builder(comparator());
    }

    @Override public Entry<K, V> head () {
        return smallest().get();
    }

    @Override public AOption<Entry<K, V>> headOption () {
        return smallest();
    }

    @Override public Entry<K, V> min () {
        throw new UnsupportedOperationException("Map.Entry is not Comparable - first() may be what you want.");
    }

    @Override public Entry<K, V> max () {
        throw new UnsupportedOperationException("Map.Entry is not Comparable - last() may be what you want.");
    }

    @Override public ALinkedList<Entry<K, V>> toLinkedList () {
        return ALinkedList.from(this);
    }

    @Override public AVector<Entry<K, V>> toVector () {
        return AVector.from(this);
    }

    @Override public AHashSet<Entry<K, V>> toSet () {
        return ASet.from(this);
    }

    @Override public ATreeSet<Entry<K, V>> toSortedSet () {
        throw new UnsupportedOperationException("There is no natural order for Map.Entry");
    }
    @Override public ATreeSet<Entry<K, V>> toSortedSet (Comparator<Entry<K, V>> comparator) {
        return ASortedSet.from(this, comparator);
    }
    @Override public ATreeSet<Entry<K, V>> toMutableSortedSet () {
        throw new UnsupportedOperationException("There is no natural order for Map.Entry");
    }
    @Override public AMutableSortedSetWrapper<Entry<K, V>> toMutableSortedSet (Comparator<Entry<K, V>> comparator) {
        return AMutableSortedSetWrapper.from(this, comparator);
    }

    @Override public <K1, V1> AMap<K1, V1> toMap () {
        //noinspection unchecked
        return (AMap<K1, V1>) this;
    }

    @Override public AMutableListWrapper<Entry<K, V>> toMutableList () {
        return AMutableListWrapper.from(this);
    }

    @Override public AMutableSetWrapper<Entry<K, V>> toMutableSet () {
        return AMutableSetWrapper.from(this);
    }

    @Override public AMutableSortedMapWrapper<K, V> filter(Predicate<Entry<K, V>> f) {
        return AMutableSortedMapWrapper.fromIterator(iterator().filter(f), comparator()).withDerivedDefaultValue(defaultProvider);
    }
    @Override public AMutableSortedMapWrapper<K, V> filterNot (Predicate<Entry<K, V>> f) {
        return AMutableSortedMapWrapper.fromIterator(iterator().filterNot(f), comparator()).withDerivedDefaultValue(defaultProvider);
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

    @Override public <K1> AMap<K1, AMutableSortedMapWrapper<K, V>> groupBy (Function<Entry<K, V>, K1> keyExtractor) {
        //noinspection unchecked
        return (AMap<K1, AMutableSortedMapWrapper<K, V>>) AMapSupport.groupBy(this, keyExtractor);
    }

    @Override public boolean contains (Object o) {
        if (! (o instanceof Map.Entry)) return false;
        final Map.Entry e = (Entry) o;
        if (! containsKey(e.getKey())) return false;
        return Objects.equals(e.getValue(), get(e.getKey()));
    }

    @Override public Entry<K, V> lowerEntry (K key) {
        return inner.lowerEntry(key);
    }

    @Override public K lowerKey (K key) {
        return inner.lowerKey(key);
    }

    @Override public Entry<K, V> floorEntry (K key) {
        return inner.floorEntry(key);
    }

    @Override public K floorKey (K key) {
        return inner.floorKey(key);
    }

    @Override public Entry<K, V> ceilingEntry (K key) {
        return inner.ceilingEntry(key);
    }

    @Override public K ceilingKey (K key) {
        return inner.ceilingKey(key);
    }

    @Override public Entry<K, V> higherEntry (K key) {
        return inner.higherEntry(key);
    }

    @Override public K higherKey (K key) {
        return inner.higherKey(key);
    }

    @Override public Entry<K, V> firstEntry () {
        return inner.firstEntry();
    }

    @Override public Entry<K, V> lastEntry () {
        return inner.lastEntry();
    }

    @Override public Entry<K, V> pollFirstEntry () {
        return inner.pollFirstEntry();
    }

    @Override public Entry<K, V> pollLastEntry () {
        return inner.pollLastEntry();
    }

    @Override public K firstKey () {
        return inner.firstKey();
    }

    @Override public K lastKey () {
        return inner.lastKey();
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

    public NavigableMap<K,V> getInner() {
        return inner;
    }

    @Override public int hashCode () {
        return inner.hashCode();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override public boolean equals (Object obj) {
        return inner.equals(obj);
    }

    @Override public String toString () {
        return getClass().getSimpleName() + ":" + inner;
    }

    public static class Builder<K,V> implements ACollectionBuilder<Map.Entry<K,V>, AMutableSortedMapWrapper<K,V>> {
        private final NavigableMap<K,V> inner;

        public Builder (Comparator<? super K> comparator) {
            this.inner = new TreeMap<>(comparator);
        }

        public Builder<K, V> add (K key, V value) {
            inner.put(key, value);
            return this;
        }

        @Override public Builder<K, V> add (Entry<K, V> el) {
            inner.put(el.getKey(), el.getValue());
            return this;
        }

        @Override public AMutableSortedMapWrapper<K, V> build () {
            return new AMutableSortedMapWrapper<>(inner);
        }
    }
}