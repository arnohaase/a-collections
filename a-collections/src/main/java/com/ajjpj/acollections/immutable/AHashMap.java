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


public abstract class AHashMap<K,V> extends AbstractImmutableMap<K,V> implements ACollectionDefaults<Map.Entry<K,V>, AHashMap<K,V>>, AMapDefaults<K,V,AHashMap<K,V>>, Serializable {
    private final CompactHashMap<MapEntryWithEquality> compactHashMap;

    @SuppressWarnings("unchecked")
    public static <K,V> AHashMap<K,V> empty() {
        return new AHashMapEquals<>(CompactHashMap.EMPTY);
    }

    public static <K,V> AHashMap<K,V> from(Map<K,V> m) {
        return from(m.entrySet());
    }
    public static <K,V> AHashMap<K,V> from(Iterable<Map.Entry<K,V>> coll) {
        return AHashMap.<K,V>builder().addAll(coll).build();
    }
    public static <K,V> AHashMap<K,V> fromIterator(Iterator<Entry<K,V>> iterator) {
        return AHashMap.<K,V> builder().addAll(iterator).build();
    }

    public static <K,V> AHashMap<K,V> of() {
        return empty();
    }
    public static <K,V> AHashMap<K,V> of(K k1, V v1) {
        return AHashMap.<K,V>builder().add(k1, v1).build();
    }
    public static <K,V> AHashMap<K,V> of(K k1, V v1, K k2, V v2) {
        return AHashMap.<K,V>builder().add(k1, v1).add(k2, v2).build();
    }
    public static <K,V> AHashMap<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3) {
        return AHashMap.<K,V>builder().add(k1, v1).add(k2, v2).add(k3,v3).build();
    }
    public static <K,V> AHashMap<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        return AHashMap.<K,V>builder().add(k1, v1).add(k2, v2).add(k3,v3).add(k4,v4).build();
    }
    public static <K,V> AHashMap<K,V> ofEntries(Iterable<Map.Entry<K,V>> coll) {
        return from(coll);
    }

    AHashMap () {
        this (new CompactHashMap<>());
    }
    AHashMap (CompactHashMap<MapEntryWithEquality> compactHashMap) {
        this.compactHashMap = compactHashMap;
    }

    protected Object writeReplace() throws ObjectStreamException {
        return new SerializationProxy(this);
    }

    public AHashMap<K, V> plus (K key, V value) {
        return newInstance(compactHashMap.updated0(newEntry(key, value), 0));
    }
    public AHashMap<K, V> minus (K key) {
        return newInstance(compactHashMap.removed0(newEntry(key, null), 0));
    }

    abstract AHashMap<K,V> newInstance(CompactHashMap<MapEntryWithEquality> compact);
    abstract MapEntryWithEquality newEntry(K key, V value);

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
        return compactHashMap.get0(newEntry((K)key, null), 0) != null;
    }

    @Override public V get (Object key) {
        //noinspection unchecked
        final MapEntryWithEquality<K,V> raw = compactHashMap.get0(newEntry((K)key, null), 0);
        if (raw != null)
            return raw.getValue();
        else
            return null;
    }

    @Override public AOption<V> getOptional (K key) {
        //noinspection unchecked
        final MapEntryWithEquality<K,V> raw = compactHashMap.get0(newEntry(key, null), 0);
        return AOption.of(raw).map(MapEntryWithEquality::getValue);
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

    static class AHashMapEquals<K,V> extends AHashMap<K,V> {
        AHashMapEquals (CompactHashMap<MapEntryWithEquality> compactHashMap) {
            super(compactHashMap);
        }

        @Override AHashMap<K,V> newInstance (CompactHashMap<MapEntryWithEquality> compactHashMap) {
            return new AHashMapEquals<>(compactHashMap);
        }

        @Override MapEntryWithEquality newEntry (K key, V value) {
            return new MapEntryWithEquals<>(key, value);
        }
    }


    static abstract class MapEntryWithEquality<K,V> extends AbstractMap.SimpleImmutableEntry<K,V> implements CompactHashMap.EntryWithEquality, Map.Entry<K,V> {
        MapEntryWithEquality (K key, V value) {
            super(key, value);
        }
    }

    private static class MapEntryWithEquals<K,V> extends MapEntryWithEquality<K,V> {
        private int keyHash = -123; // 'safe data race' - see String.hashCode() implementation

        MapEntryWithEquals (K key, V value) { super(key, value); }

        @Override public boolean hasEqualKey (CompactHashMap.EntryWithEquality other) {
            // compare hash for safety and as an optimization
            return keyHash() == other.keyHash() && Objects.equals(getKey(), ((MapEntryWithEquality) other).getKey());
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
