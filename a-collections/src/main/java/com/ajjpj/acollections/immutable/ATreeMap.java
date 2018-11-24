package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AMapDefaults;
import com.ajjpj.acollections.internal.AMapSupport;
import com.ajjpj.acollections.util.AOption;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * This class implements immutable maps using a red-black tree, sorting entries by key based on a {@link Comparator}.
 *
 * <p> Since this is an immutable class, it does not support modifying methods from {@link java.util.Map}: Those methods return
 *  {@code boolean} or a previous element, but in order to "modify" an immutable collection, they would need to return the new collection
 *  instance.
 *
 * <p> So instances of AHashMap rely on methods like {@link #plus(Object, Object)} or {@link #minus(Object)} that return the modified
 *  collection to add or remove entries. For details and sample code, see {@link AMap}.
 *
 * <p> Implementation note: This is a port of Scala's standard library {@code TreeMap}.
 *
 * @param <K> the map's key type
 * @param <V> the map's value type
 */
public class ATreeMap<K,V> extends AbstractImmutableMap<K,V> implements ASortedMap<K,V>, ACollectionDefaults<Map.Entry<K,V>, ATreeMap<K,V>>, AMapDefaults<K,V,ATreeMap<K,V>>, Serializable {
    private final RedBlackTree.Tree<K,V> root;
    private final Comparator<K> comparator;

    /**
     * Convenience method for creating an empty {@link ATreeMap} with {@link Comparator#naturalOrder()}. This can later be modified by
     *  calling {@link #plus(Object,Object)} or {@link #minus(Object)}. For creating a map with known elements, calling one of the
     *  {@code of} factory methods is usually more concise.
     *
     * @param <K> the new map's key type
     * @param <V> the new map's value type
     * @return an empty {@link AHashMap}
     */
    public static <K extends Comparable<K>,V> ATreeMap<K,V> empty() {
        return new ATreeMap<>(null, Comparator.<K>naturalOrder());
    }

    /**
     * Convenience method for creating an empty {@link ATreeMap}. This can later be modified by calling {@link #plus(Object,Object)} or
     * {@link #minus(Object)}. For creating a map with known elements, calling one of the {@code of} factory methods is usually more concise.
     *
     * @param comparator the tree map's comparator
     *
     * @param <K> the new map's key type
     * @param <V> the new map's value type
     * @return an empty {@link AHashMap}
     */
    public static <K,V> ATreeMap<K,V> empty(Comparator<K> comparator) {
        return new ATreeMap<>(null, comparator);
    }

    private ATreeMap (RedBlackTree.Tree<K,V> root, Comparator<K> comparator) {
        this.root = root;
        this.comparator = comparator;
    }

    /**
     * Creates a new {@link ATreeMap} based on an {@link Iterator}'s elements using {@link Comparator#naturalOrder()}.
     *
     * @param it the {@link Iterator} from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K extends Comparable<K>,V> ATreeMap<K,V> fromIterator(Iterator<? extends Entry<K,V>> it) {
        return fromIterator(it, Comparator.naturalOrder());
    }

    /**
     * Creates a new {@link ATreeMap} based on an {@link Iterator}'s elements using a comparator provided by the caller.
     *
     * @param it         the {@link Iterator} from which the new map is initialized
     * @param comparator the key comparator to use
     *
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K,V> ATreeMap<K,V> fromIterator(Iterator<? extends Entry<K,V>> it, Comparator<K> comparator) {
        return ATreeMap.<K,V> builder(comparator).addAll(it).build();
    }

    /**
     * Creates a new {@link ATreeMap} based on a {@link java.util.Map}'s elements using {@link Comparator#naturalOrder()}.
     *
     * @param m the {@link Map} from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K extends Comparable<K>,V> ATreeMap<K,V> fromMap(Map<K,V> m) {
        return from(m.entrySet());
    }

    /**
     * Creates a new {@link ATreeMap} based on a {@link java.util.Map}'s elements using a comparator provided by the caller.
     *
     * @param m          the {@link Map} from which the new map is initialized
     * @param comparator the key comparator to use
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K extends Comparable<K>,V> ATreeMap<K,V> fromMap(Map<K,V> m, Comparator<K> comparator) {
        return from(m.entrySet(), comparator);
    }

    /**
     * Creates a new {@link ATreeMap} based on an {@link Iterable}'s elements using {@link Comparator#naturalOrder()}.
     *
     * @param coll the {@link Iterable} from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K extends Comparable<K>,V> ATreeMap<K,V> from(Iterable<? extends Entry<K,V>> coll) {
        return from(coll, Comparator.naturalOrder());
    }

    /**
     * Creates a new {@link ATreeMap} based on an {@link Iterator}'s elements using a comparator provided by the caller.
     *
     * @param it         the {@link Iterator} from which the new map is initialized
     * @param comparator the key comparator to use
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K,V> ATreeMap<K,V> from(Iterable<? extends Entry<K,V>> it, Comparator<K> comparator) {
        return ATreeMap.<K,V> builder(comparator).addAll(it).build();
    }

    /**
     * This is an alias for {@link #empty()} for consistency with Java 9 conventions - it creates an empty {@link ATreeMap}
     *  using {@link Comparator#naturalOrder()}.
     *
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return an empty {@link ATreeMap}
     */
    public static <K extends Comparable<K>,V> ATreeMap<K,V> of() {
        return empty(Comparator.<K>naturalOrder());
    }

    /**
     * Convenience factory method creating an {@link ATreeMap} with exactly one entry using {@link Comparator#naturalOrder()}.
     *
     * @param k1 the single entry's key
     * @param v1 the single entry's value
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new {@link ATreeMap}
     */
    public static <K extends Comparable<K>,V> ATreeMap<K,V> of(K k1, V v1) {
        return ATreeMap.<K,V>builder().add(k1, v1).build();
    }

    /**
     * Convenience factory method creating an {@link ATreeMap} with exactly two entries using {@link Comparator#naturalOrder()}.
     *
     * @param k1 the first entry's key
     * @param v1 the first entry's value
     * @param k2 the second entry's key
     * @param v2 the second entry's value
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new {@link ATreeMap}
     */
    public static <K extends Comparable<K>,V> ATreeMap<K,V> of(K k1, V v1, K k2, V v2) {
        return ATreeMap.<K,V>builder().add(k1, v1).add(k2, v2).build();
    }

    /**
     * Convenience factory method creating an {@link ATreeMap} with three entries using {@link Comparator#naturalOrder()}.
     *
     * @param k1 the first entry's key
     * @param v1 the first entry's value
     * @param k2 the second entry's key
     * @param v2 the second entry's value
     * @param k3 the third entry's key
     * @param v3 the third entry's value
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new {@link ATreeMap}
     */
    public static <K extends Comparable<K>,V> ATreeMap<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return ATreeMap.<K,V>builder().add(k1, v1).add(k2, v2).add(k3,v3).build();
    }

    /**
     * Convenience factory method creating an {@link ATreeMap} with four entries using {@link Comparator#naturalOrder()}.
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
     * @return the new {@link ATreeMap}
     */
    public static <K extends Comparable<K>,V> ATreeMap<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return ATreeMap.<K,V>builder().add(k1, v1).add(k2, v2).add(k3,v3).add(k4,v4).build();
    }

    /**
     * This is an alias for {@link #from(Iterable)} for consistency with Java 9 conventions - it creates an ATreeMap from an Iterable of
     * {@link Map.Entry} using {@link Comparator#naturalOrder()}.
     *
     * @param coll the entries
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new {@link ATreeMap}
     */
    public static <K extends Comparable<K>,V> ATreeMap<K,V> ofEntries(Iterable<Map.Entry<K,V>> coll) {
        return from(coll);
    }


    public static <K extends Comparable<K>,V> Builder<K,V> builder() {
        return builder(Comparator.<K>naturalOrder());
    }
    public static <K,V> Builder<K,V> builder(Comparator<K> comparator) {
        return new Builder<>(comparator);
    }

    @Override public V get(Object key) {

        try {
            //noinspection unchecked
            return RedBlackTree.get(root, (K) key, comparator).orNull(); //TODO skip 'get'
        }
        catch (Exception e) {
            // handle 'wrong type' exceptions thrown by the comparator because Java's API weirdly accepts 'Object' rather than 'K' as the key's type...
            return null;
        }
    }
    @Override public ATreeMap<K,V> plus (K key, V value) {
        return new ATreeMap<>(RedBlackTree.update(root, key, value, true, comparator), comparator);
    }
    @Override public ATreeMap<K,V> minus (K key) {
        if (!RedBlackTree.contains(root, key, comparator)) return this;
        return new ATreeMap<>(RedBlackTree.delete(root, key, comparator), comparator);
    }
    @Override public AIterator<Entry<K,V>> iterator() {
        return RedBlackTree.iterator(root, AOption.none(), comparator);
    }

    @Override public int size() {
        return RedBlackTree.count(root);
    }

    @Override public boolean contains (Object o) {
        return AMapSupport.containsEntry(this, o);
    }

    @Override public boolean containsKey (Object key) {
        try {
            //noinspection unchecked
            return RedBlackTree.get(root, (K) key, comparator).nonEmpty(); //TODO skip 'get', use 'lookup' directly
        }
        catch (Exception e) {
            // handle 'wrong type' exceptions thrown by the comparator because Java's API weirdly accepts 'Object' rather than 'K' as the key's type...
            return false;
        }
    }

    @Override public boolean containsValue (Object value) {
        return RedBlackTree.valuesIterator(root, AOption.none(), comparator)
                .exists(v -> Objects.equals(v, value));
    }

    @Override public AOption<V> getOptional (K key) {
        return RedBlackTree.get(root, key, comparator);
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

    @Override public <V1> ATreeMap<K, V1> mapValues (Function<V, V1> f) {
        return (ATreeMap<K,V1>) AMapDefaults.super.mapValues(f);
    }

    @Override public ATreeMap<K, V> filter (Predicate<Entry<K, V>> f) {
        return ATreeMap.<K,V>builder(comparator).addAll(iterator().filter(f)).build();
    }

    @Override public ATreeMap<K, V> filterNot (Predicate<Entry<K, V>> f) {
        return filter(f.negate());
    }

    @Override public <K1> AMap<K1, ATreeMap<K, V>> groupBy (Function<Entry<K, V>, K1> keyExtractor) {
        //noinspection unchecked
        return (AMap<K1, ATreeMap<K, V>>) AMapSupport.groupBy(this, keyExtractor);
    }

    @Override public boolean isEmpty () {
        return root == null;
    }

    @Override public ASortedSet<K> keySet () {
        return new AMapSupport.SortedKeySet<>(this);
    }

    @Override public ACollection<V> values () {
        return new AMapSupport.ValuesCollection<>(this);
    }

    @Override public ASortedSet<Entry<K, V>> entrySet () { //TODO ASortedSet
        return new AMapSupport.SortedEntrySet<>(this);
    }

    @Override public Comparator<K> comparator () {
        return comparator;
    }

    @Override public int countInRange (AOption<K> from, AOption<K> to) {
        return RedBlackTree.countInRange(root, from, to, comparator);
    }

    @Override public ATreeMap<K, V> range (AOption<K> from, AOption<K> until) {
        return new ATreeMap<>(RedBlackTree.rangeImpl(root, from, until, comparator), comparator);
    }

    @Override public ATreeMap<K, V> drop (int n) {
        return new ATreeMap<>(RedBlackTree.drop(root, n), comparator);
    }

    @Override public ATreeMap<K, V> take (int n) {
        return new ATreeMap<>(RedBlackTree.take(root, n), comparator);
    }

    @Override public ATreeMap<K, V> slice (int from, int until) {
        return new ATreeMap<>(RedBlackTree.slice(root, from, until), comparator);
    }

    @Override public AOption<Entry<K, V>> smallest () {
        if (root == null) return AOption.none();
        return AOption.some(RedBlackTree.smallest(root).entry());
    }

    @Override public AOption<Entry<K, V>> greatest () {
        if (root == null) return AOption.none();
        return AOption.some(RedBlackTree.greatest(root).entry());
    }

    @Override public AIterator<K> keysIterator () {
        return keysIterator(AOption.none());
    }
    @Override public AIterator<V> valuesIterator () {
        return valuesIterator(AOption.none());
    }

    @Override public AIterator<Entry<K, V>> iterator (AOption<K> start) {
        return RedBlackTree.iterator(root, start, comparator);
    }
    @Override public AIterator<K> keysIterator (AOption<K> start) {
        return RedBlackTree.keysIterator(root, start, comparator);
    }
    @Override public AIterator<V> valuesIterator (AOption<K> start) {
        return RedBlackTree.valuesIterator(root, start, comparator);
    }

    @Override public <K1, V1> ACollectionBuilder<Entry<K1, V1>, ATreeMap<K1, V1>> newEntryBuilder () {
        //noinspection unchecked
        return new Builder(Comparator.naturalOrder());
    }

    public static class Builder<K,V> implements ACollectionBuilder<Map.Entry<K,V>, ATreeMap<K,V>> {
        private ATreeMap<K,V> result;

        Builder (Comparator<K> comparator) {
            this.result = ATreeMap.empty(comparator);
        }

        public Builder<K, V> add (K key, V value) {
            result = result.plus(key, value);
            return this;
        }

        @Override public Builder<K, V> add (Entry<K, V> el) {
            result = result.plus(el.getKey(), el.getValue());
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

        @Override public ATreeMap<K, V> build () {
            return result;
        }
    }
}
