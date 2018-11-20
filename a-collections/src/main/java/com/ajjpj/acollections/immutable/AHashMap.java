package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AMapDefaults;
import com.ajjpj.acollections.internal.AMapSupport;
import com.ajjpj.acollections.util.AOption;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * This class implements immutable maps using a hash trie. Keys must implement a valid {@link Object#hashCode()} method; in particular,
 *  the hash code must be <em>stable</em> and <em>consistent with equality</em>.
 *
 * <p> Since this is an immutable class, it does not support modifying methods from {@link java.util.Map}: Those methods return
 *  {@code boolean} or a previous element, but in order to "modify" an immutable collection, they would need to return the new collection
 *  instance.
 *
 * <p> So instances of AHashMap rely on methods like {@link #plus(Object, Object)} or {@link #minus(Object)} that return the modified
 *  collection to add or remove entries. For details and sample code, see {@link AMap}.
 *
 * <p> Implementation note: This is a port of Scala's standard library {@code HashMap}. It uses some optimization ideas from
 *  the <a href="https://github.com/andrewoma/dexx">Dexx collections library</a>.
 *
 * @param <K> the map's key type
 * @param <V> the map's value type
 */
public class AHashMap<K,V> extends AbstractImmutableMap<K,V> implements ACollectionDefaults<Map.Entry<K,V>, AHashMap<K,V>>, AMapDefaults<K,V,AHashMap<K,V>>, Serializable {
    private final CompactHashMap<MapEntryWithEquals> compactHashMap;

    /**
     * Convenience method for creating an empty {@link AHashMap}. This can later be modified by calling {@link #plus(Object,Object)} or
     * {@link #minus(Object)}. For creating a map with known elements, calling one of the {@code of} factory methods is usually more concise.
     *
     * @param <K> the new map's key type
     * @param <V> the new map's value type
     * @return an empty {@link AHashMap}
     */
    public static <K,V> AHashMap<K,V> empty() {
        //noinspection unchecked
        return new AHashMap<>(CompactHashMap.EMPTY);
    }

    /**
     * Creates a new {@link AHashMap} based on a {@link java.util.Map}'s elements.
     *
     * @param m the {@link Map} from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K,V> AHashMap<K,V> fromMap(Map<K,V> m) {
        return from(m.entrySet());
    }

    /**
     * Creates a new {@link AHashMap} based on an {@link Iterable}'s elements.
     *
     * @param coll the {@link Iterable} from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K,V> AHashMap<K,V> from(Iterable<Map.Entry<K,V>> coll) {
        return AHashMap.<K,V>builder().addAll(coll).build();
    }

    /**
     * Creates a new {@link AHashMap} based on an {@link Iterator}'s elements.
     *
     * @param it the {@link Iterator} from which the new map is initialized
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new map
     */
    public static <K,V> AHashMap<K,V> fromIterator(Iterator<Entry<K,V>> it) {
        return AHashMap.<K,V> builder().addAll(it).build();
    }

    /**
     * This is an alias for {@link #empty()} for consistency with Java 9 conventions - it creates an empty {@link AHashMap}.
     *
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return an empty {@link AHashMap}
     */
    public static <K,V> AHashMap<K,V> of() {
        return empty();
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
    public static <K,V> AHashMap<K,V> of(K k1, V v1) {
        return AHashMap.<K,V>builder().add(k1, v1).build();
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
    public static <K,V> AHashMap<K,V> of(K k1, V v1, K k2, V v2) {
        return AHashMap.<K,V>builder().add(k1, v1).add(k2, v2).build();
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
    public static <K,V> AHashMap<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return AHashMap.<K,V>builder().add(k1, v1).add(k2, v2).add(k3,v3).build();
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
    public static <K,V> AHashMap<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return AHashMap.<K,V>builder().add(k1, v1).add(k2, v2).add(k3,v3).add(k4,v4).build();
    }

    /**
     * This is an alias for {@link #from(Iterable)} for consistency with Java 9 conventions - it creates an AHashMap from an Iterable of
     * {@link Map.Entry}.
     *
     * @param coll the entries
     * @param <K> the map's key type
     * @param <V> the map's value type
     * @return the new {@link AHashMap}
     */
    public static <K,V> AHashMap<K,V> ofEntries(Iterable<Map.Entry<K,V>> coll) {
        return from(coll);
    }

    private AHashMap () {
        this (new CompactHashMap<>());
    }
    private AHashMap (CompactHashMap<MapEntryWithEquals> compactHashMap) {
        this.compactHashMap = compactHashMap;
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new SerializationProxy(this);
    }

    @Override public AHashMap<K, V> plus (K key, V value) {
        return new AHashMap<>(compactHashMap.updated0(new MapEntryWithEquals<>(key, value), 0));
    }
    @Override public AHashMap<K, V> minus (K key) {
        return new AHashMap<>(compactHashMap.removed0(new MapEntryWithEquals<>(key, null), 0));
    }


    public static <K,V> Builder<K,V> builder() {
        return new Builder<>();
    }

    @Override public int size () {
        return compactHashMap.size();
    }

    @Override public AVector<Entry<K, V>> toVector () {
        return AVector.from(this); // discard equality
    }
    @Override public ALinkedList<Entry<K, V>> toLinkedList () {
        return ALinkedList.from(this); // discard equality
    }
    @Override public AHashSet<Entry<K, V>> toSet () {
        return AHashSet.from(this); // discard equality
    }

    @Override public boolean isEmpty() {
        return compactHashMap.isEmpty();
    }

    @Override public boolean containsKey (Object key) {
        //noinspection unchecked
        MapEntryWithEquals result = new MapEntryWithEquals<>((K) key, (V) null);
        return compactHashMap.get0(result, 0) != null;
    }

    @Override public V get (Object key) {
        //noinspection unchecked
        MapEntryWithEquals<K,V> result = new MapEntryWithEquals<>((K) key, null);
        //noinspection unchecked
        final MapEntryWithEquals<K,V> raw = compactHashMap.get0(result, 0);
        if (raw != null)
            return raw.getValue();
        else
            return null;
    }

    @Override public AOption<V> getOptional (K key) {
        //noinspection unchecked
        MapEntryWithEquals result = new MapEntryWithEquals<>(key, (V) null);
        //noinspection unchecked
        final MapEntryWithEquals<K,V> raw = compactHashMap.get0(result, 0);
        return AOption.of(raw).map(MapEntryWithEquals::getValue);
    }

    @Override public AIterator<Entry<K, V>> iterator () {
        //noinspection unchecked
        return (AIterator) compactHashMap.iterator();
    }

    @Override public AIterator<K> keysIterator () {
        return iterator().map(Entry::getKey); //TODO this can be optimized
    }

    @Override public AIterator<V> valuesIterator () {
        return iterator().map(Entry::getValue); //TODO this can be optimized
    }

    @Override public AHashMap<K, V> filter (Predicate<Entry<K, V>> f) {
        return AHashMap.fromIterator(iterator().filter(f));
    }

    @Override public AHashMap<K, V> filterNot (Predicate<Entry<K, V>> f) {
        return filter(f.negate());
    }

    @Override public <K1, V1> ACollectionBuilder<Entry<K1, V1>, AHashMap<K1, V1>> newEntryBuilder () {
        return builder();
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

    @Override public <V1> AHashMap<K, V1> mapValues (Function<V, V1> f) {
        return (AHashMap<K,V1>) AMapDefaults.super.mapValues(f);
    }

    @Override public <K1> AMap<K1, AHashMap<K, V>> groupBy (Function<Entry<K, V>, K1> keyExtractor) {
        //noinspection unchecked
        return (AMap<K1, AHashMap<K, V>>) AMapSupport.groupBy(this, keyExtractor);
    }

    @Override public boolean contains (Object o) {
        return AMapSupport.containsEntry(this, o);
    }

    @Override public boolean containsValue (Object value) {
        return exists(kv -> Objects.equals(kv.getValue(), value));
    }

    @Override public ASet<K> keySet () {
        return new AMapSupport.KeySet<>(this);
    }

    @Override public ACollection<V> values () {
        return new AMapSupport.ValuesCollection<>(this);
    }

    @Override public ASet<Entry<K, V>> entrySet () {
        return new AMapSupport.EntrySet<>(this);
    }

    public static class Builder<K,V> implements ACollectionBuilder<Map.Entry<K,V>, AHashMap<K,V>> {
        private AHashMap<K,V> result;

        Builder() {
            this.result = empty();
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
            while (it.hasNext()) add(it.next());
            return this;
        }

        @Override public Builder<K, V> addAll (Iterable<? extends Entry<K, V>> coll) {
            return addAll(coll.iterator());
        }

        @Override public Builder<K, V> addAll (Entry<K, V>[] coll) {
            return addAll(Arrays.asList(coll).iterator());
        }

        @Override public AHashMap<K, V> build () {
            return result;
        }
    }


    private static class MapEntryWithEquals<K,V> extends AbstractMap.SimpleImmutableEntry<K,V> implements CompactHashMap.EntryWithEquality {
        private int keyHash = -123; // 'safe data race' - see String.hashCode() implementation

        MapEntryWithEquals (K key, V value) { super(key, value); }

        @Override public boolean hasEqualKey (CompactHashMap.EntryWithEquality other) {
            // compare hash for safety and as an optimization
            return keyHash() == other.keyHash() && Objects.equals(getKey(), ((MapEntryWithEquals) other).getKey());
        }

        private int improve(int hashCode) {
            int h = hashCode + ~(hashCode << 9);
            h = h ^ (h >>> 14);
            h = h + (h << 4);
            return h ^ (h >>> 10);
        }


        @Override public int keyHash () {
            if (keyHash == -123) {
                keyHash = improve(Objects.hashCode(getKey()));
            }
            return keyHash;
        }
    }

    /**
     * proxy class to allow deserializing an immutable hash map without relying on hash codes remaining the same
     */
    private static class SerializationProxy implements Serializable {
        private transient AHashMap<?,?> orig;

        SerializationProxy (AHashMap<?,?> orig) {
            this.orig = orig;
        }

        private static final long serialVersionUID = 2L;

        private void writeObject(ObjectOutputStream oos) throws IOException {
            oos.writeInt(orig.size());
            for (Map.Entry<?,?> e: orig) {
                oos.writeObject(e.getKey());
                oos.writeObject(e.getValue());
            }
        }

        private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            final Builder<Object,Object> builder = builder();
            final int size = ois.readInt();
            for (int i=0; i<size; i++) {
                builder.add(ois.readObject(), ois.readObject());
            }
            orig = builder.build();
        }

        private Object readResolve() {
            return orig;
        }
    }
}
