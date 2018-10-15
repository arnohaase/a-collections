package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AMap;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.util.AEquality;
import com.ajjpj.acollections.util.AOption;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


public abstract class AHashMap<K,V> implements AMap<K,V>, ACollectionDefaults<Map.Entry<K,V>, AHashMap<K,V>> {
    private final CompactHashMap<K,V,EntryWithEquality<K,V>> compactHashMap;

    public static <K,V> AHashMap<K,V> empty() {
        return empty(AEquality.EQUALS);
    }
    @SuppressWarnings("unchecked")
    public static <K,V> AHashMap<K,V> empty(AEquality equality) {
        if (equality == AEquality.EQUALS) return new AHashMapEquals<>(CompactHashMap.EMPTY);
        if (equality == AEquality.IDENTITY) return new AHashMapIdentity<>(CompactHashMap.EMPTY);
        return new AHashMapCustom<>(CompactHashMap.EMPTY, equality);
    }

    AHashMap () {
        this (new CompactHashMap<>());
    }
    AHashMap (CompactHashMap<K,V,EntryWithEquality<K,V>> compactHashMap) {
        this.compactHashMap = compactHashMap;
    }

    public AHashMap<K, V> updated(K key, V value) {
        return newInstance(compactHashMap.updated0(newEntry(key, value), 0));
    }
    public AHashMap<K, V> removed(K key) {
        return newInstance(compactHashMap.removed0(newEntry(key, null), 0));
    }

    abstract AHashMap<K,V> newInstance(CompactHashMap<K,V,EntryWithEquality<K,V>> compact);
    abstract EntryWithEquality<K,V> newEntry(K key, V value);

    public static <K,V> ACollectionBuilder<Map.Entry<K,V>, AHashMap<K,V>> builder() {
        return builder(AEquality.EQUALS);
    }
    public static <K,V> ACollectionBuilder<Map.Entry<K,V>, AHashMap<K,V>> builder(AEquality equality) {
        return new Builder<>(equality);
    }

    @Override public int size () {
        return compactHashMap.size();
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
        final EntryWithEquality<K,V> raw = compactHashMap.get0(newEntry((K)key, null), 0);
        if (raw != null)
            return raw.getValue();
        else
            return null;
    }

    @Override public AOption<V> getOptional (K key) {
        final EntryWithEquality<K,V> raw = compactHashMap.get0(newEntry(key, null), 0);
        return AOption.of(raw).map(EntryWithEquality::getValue);
    }

    @Override public AIterator<Entry<K, V>> iterator () {
        //noinspection unchecked
        return (AIterator) compactHashMap.iterator();
    }

    @Override public AHashMap<K, V> filter (Predicate<Entry<K, V>> f) {
        CompactHashMap result = CompactHashMap.EMPTY;

        for (Map.Entry<K,V> kv: this) {
            if(f.test(kv)) {
                //noinspection unchecked
                result = result.updated0((EntryWithEquality) kv, 0);
            }
        }
        //noinspection unchecked
        return newInstance(result);
    }

    @Override public AHashMap<K, V> filterNot (Predicate<Entry<K, V>> f) {
        return filter(f.negate());
    }

    @Override public <U> ACollectionBuilder<U, ? extends ACollection<U>> newBuilder () {
        throw new UnsupportedOperationException("Implementing this well goes beyond the boundaries of Java's type system. Use static AHashMap.builder() instead.");
    }
    @Override public <U> ACollection<U> map (Function<Entry<K, V>, U> f) {
        return ACollectionSupport.map(AVector.builder(equality()), this, f);
    }
    @Override public <U> ACollection<U> flatMap (Function<Entry<K, V>, Iterable<U>> f) {
        return ACollectionSupport.flatMap(AVector.builder(equality()), this, f);
    }
    @Override public <U> ACollection<U> collect (Predicate<Entry<K, V>> filter, Function<Entry<K, V>, U> f) {
        return ACollectionSupport.collect(AVector.builder(equality()), this, filter, f);
    }

    @Override public boolean containsValue (Object value) {
        return containsValue(value, AEquality.EQUALS);
    }
    @Override public boolean containsValue (Object value, AEquality equality) {
        return exists(kv -> equality.equals(kv.getValue(), value));
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

    @Override public Set<K> keySet () {
        return null; //TODO
    }

    @Override public Collection<V> values () {
        return null; //TODO
    }

    @Override public Set<Entry<K, V>> entrySet () {
        return null; //TODO
    }

    static class Builder<K,V> implements ACollectionBuilder<Map.Entry<K,V>, AHashMap<K,V>> {
        private AHashMap<K,V> result;

        Builder(AEquality equality) {
            result = empty(equality);
        }

        @Override public ACollectionBuilder<Entry<K, V>, AHashMap<K, V>> add (Entry<K, V> el) {
            result = result.updated(el.getKey(), el.getValue());
            return this;
        }

        @Override public AHashMap<K, V> build () {
            return result;
        }
    }


    static class AHashMapEquals<K,V> extends AHashMap<K,V> {
        AHashMapEquals (CompactHashMap<K, V, EntryWithEquality<K, V>> compactHashMap) {
            super(compactHashMap);
        }

        @Override AHashMap<K, V> newInstance (CompactHashMap<K, V, EntryWithEquality<K, V>> compactHashMap) {
            return new AHashMapEquals<>(compactHashMap);
        }

        @Override EntryWithEquality<K, V> newEntry (K key, V value) {
            return new EntryWithEquals<>(key, value);
        }

        @Override public AEquality keyEquality () {
            return AEquality.EQUALS;
        }
    }
    static class AHashMapIdentity<K,V> extends AHashMap<K,V> {
        AHashMapIdentity (CompactHashMap<K, V, EntryWithEquality<K, V>> compactHashMap) {
            super(compactHashMap);
        }

        @Override AHashMap<K, V> newInstance (CompactHashMap<K, V, EntryWithEquality<K, V>> compactHashMap) {
            return new AHashMapIdentity<>(compactHashMap);
        }

        @Override EntryWithEquality<K, V> newEntry (K key, V value) {
            return new EntryWithIdentity<>(key, value);
        }

        @Override public AEquality keyEquality () {
            return AEquality.IDENTITY;
        }
    }
    static class AHashMapCustom<K,V> extends AHashMap<K,V> {
        private final AEquality keyEquality;

        AHashMapCustom (CompactHashMap<K, V, EntryWithEquality<K, V>> compactHashMap, AEquality keyEquality) {
            super(compactHashMap);
            this.keyEquality = keyEquality;
        }

        @Override AHashMap<K, V> newInstance (CompactHashMap<K, V, EntryWithEquality<K, V>> compactHashMap) {
            return new AHashMapCustom<>(compactHashMap, keyEquality);
        }

        @Override EntryWithEquality<K, V> newEntry (K key, V value) {
            return new EntryWithConfiguredEquality<>(key, value, keyEquality);
        }

        @Override public AEquality keyEquality () {
            return keyEquality;
        }
    }


    private static abstract class EntryWithEquality<K,V> implements CompactHashMap.MapEntryWithEquality<K,V> {
        final K key;
        final V value;

        EntryWithEquality (K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey () { return key; }
        public V getValue () { return value; }
    }

    private static class EntryWithConfiguredEquality<K,V> extends EntryWithEquality<K,V> {
        private final AEquality equality;

        public EntryWithConfiguredEquality (K key, V value, AEquality equality) {
            super(key, value);
            this.equality = equality;
        }

        @Override public boolean hasEqualKey (CompactHashMap.MapEntryWithEquality<K, V> other) {
            return equality.equals(this.getKey(), other.getKey());
        }

        @Override public int keyHash () {
            return equality.hashCode(getKey());
        }
    }

    private static class EntryWithEquals<K,V> extends EntryWithIdentity<K,V> {
        private int keyHash = -123; // 'safe data race' - see String.hashCode() implementation

        EntryWithEquals (K key, V value) { super(key, value); }

        @Override public boolean hasEqualKey (CompactHashMap.MapEntryWithEquality<K, V> other) {
            // compare hash for safety and as an optimization
            return keyHash() == other.keyHash() && Objects.equals(key, other.getKey());
        }

        private int improve(int hashCode) {
            int h = hashCode + ~(hashCode << 9);
            h = h ^ (h >>> 14);
            h = h + (h << 4);
            return h ^ (h >>> 10);
        }


        @Override public int keyHash () {
            if (keyHash == -123) {
                keyHash = improve(Objects.hashCode(key));
            }
            return keyHash;
        }
    }
    private static class EntryWithIdentity<K,V> extends EntryWithEquality<K,V> {
        EntryWithIdentity (K key, V value) { super (key, value); }

        @Override public boolean hasEqualKey (CompactHashMap.MapEntryWithEquality<K, V> other) {
            return this.key == other.getKey();
        }

        @Override public int keyHash () {
            return AEquality.IDENTITY.hashCode(key);
        }
    }
}
