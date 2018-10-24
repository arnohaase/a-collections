package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AMapSupport;
import com.ajjpj.acollections.util.AOption;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


public abstract class AHashMap<K,V> implements AMap<K,V>, ACollectionDefaults<Map.Entry<K,V>, AHashMap<K,V>> {
    private final CompactHashMap<MapEntryWithEquality> compactHashMap;

    @SuppressWarnings("unchecked")
    public static <K,V> AHashMap<K,V> empty() {
        return new AHashMapEquals<>(CompactHashMap.EMPTY);
//        if (equality == AEquality.IDENTITY) return new AIdentityHashMap<>(CompactHashMap.EMPTY);
    }

    //TODO fromIterator, fromIterable, of, fromMap, ...

    AHashMap () {
        this (new CompactHashMap<>());
    }
    AHashMap (CompactHashMap<MapEntryWithEquality> compactHashMap) {
        this.compactHashMap = compactHashMap;
    }

    public AHashMap<K, V> updated(K key, V value) {
        return newInstance(compactHashMap.updated0(newEntry(key, value), 0));
    }
    public AHashMap<K, V> removed(K key) {
        return newInstance(compactHashMap.removed0(newEntry(key, null), 0));
    }

    abstract AHashMap<K,V> newInstance(CompactHashMap<MapEntryWithEquality> compact);
    abstract MapEntryWithEquality newEntry(K key, V value);

    public static <K,V> AHashMap<K,V> fromIterator(Iterator<Entry<K,V>> iterator) {
        return AHashMap.<K,V> builder().addAll(iterator).build();
    }
    public static <K,V> AHashMap<K,V> fromIterable(Iterable<Entry<K,V>> iterable) {
        return AHashMap.<K,V> builder().addAll(iterable).build();
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
    @Override public ATreeSet<Entry<K, V>> toSortedSet () {
        throw new UnsupportedOperationException("pass in a Comparator<Map.Entry> - Map.Entry has no natural order");
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

    @Override public <U> ACollectionBuilder<U, ? extends ACollection<U>> newBuilder () {
        throw new UnsupportedOperationException("Implementing this well goes beyond the boundaries of Java's type system. Use static AHashMap.builder() instead.");
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

    @Override public boolean contains (Object o) { //TODO extract to AbstractImmutableMap
        if (! (o instanceof Map.Entry)) return false;
        //noinspection unchecked
        final Map.Entry<K,V> e = (Entry<K, V>) o;
        return getOptional(e.getKey()).contains(e.getValue());
    }

    @Override public boolean containsValue (Object value) {
        return exists(kv -> Objects.equals(kv.getValue(), value));
    }

    @Override public V put (K key, V value) {
        throw new UnsupportedOperationException("use 'updated' for persistent collection");
    }

    @Override public V remove (Object key) {
        throw new UnsupportedOperationException("use 'removed' for persistent collection");
    }

    @Override public void putAll (Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("unsupported for persistent collection");
    }

    @Override public void clear () {
        throw new UnsupportedOperationException("unsupported for persistent collection");
    }

    @Override public ASet<K> keySet () {
        return new AMapSupport.KeySet<>(this);
    }

    @Override public ACollection<V> values () {
        return new AMapSupport.ValueCollection<>(this);
    }

    @Override public ASet<Entry<K, V>> entrySet () {
        return new AMapSupport.EntrySet<>(this);
    }

    @Override public boolean equals(Object o) {
        if (o instanceof Map) {
            final Map<?,?> that = (Map<?, ?>) o;
            return this.entrySet().equals(that.entrySet());
        }

        if (! (o instanceof Collection)) return false;
        final Collection<?> that = (Collection<?>) o;
        return this.size() == that.size() && this.containsAll(that);
    }

    //TODO hashCode

    @Override public String toString () { //TODO move to AbstractImmutableMap
        final StringBuilder sb = new StringBuilder(AHashMap.class.getSimpleName()).append("{");
        boolean isFirst = true;
        for (Map.Entry<K,V> e: this) {
            if (isFirst) isFirst = false;
            else sb.append(",");

            sb.append(e.getKey()).append("=").append(e.getValue());
        }

        sb.append("}");
        return sb.toString();
    }

    abstract static class AbstractBuilder<K,V> implements ACollectionBuilder<Map.Entry<K,V>, AHashMap<K,V>> {
        private AHashMap<K,V> result;

        AbstractBuilder(AHashMap<K,V> empty) {
            this.result = empty;
        }

        public ACollectionBuilder<Entry<K, V>, AHashMap<K, V>> add (K key, V value) {
            result = result.updated(key, value);
            return this;
        }

        @Override public ACollectionBuilder<Entry<K, V>, AHashMap<K, V>> add (Entry<K, V> el) {
            result = result.updated(el.getKey(), el.getValue());
            return this;
        }

        @Override public AHashMap<K, V> build () {
            return result;
        }
    }

    public static class Builder<K,V> extends AbstractBuilder<K,V> {
        public Builder () {
            super(empty());
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
}
