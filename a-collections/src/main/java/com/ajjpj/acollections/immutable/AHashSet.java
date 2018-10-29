package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AMap;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.ASetDefaults;
import com.ajjpj.acollections.internal.ASetSupport;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


public abstract class AHashSet<T> extends AbstractImmutableCollection<T> implements ACollectionDefaults<T, AHashSet<T>>, ASetDefaults<T, AHashSet<T>>, Serializable {
    private final CompactHashMap<SetEntryWithEquality<T>> compactHashMap;

    public static<T> AHashSet<T> empty() {
        return new EqualsHashSet<>(CompactHashMap.empty());
    }

    public static <T> AHashSet<T> of(T o) {
        return AHashSet
                .<T>builder()
                .add(o)
                .build();
    }
    public static <T> AHashSet<T> of(T o1, T o2) {
        return AHashSet
                .<T>builder()
                .add(o1)
                .add(o2)
                .build();
    }
    public static <T> AHashSet<T> of(T o1, T o2, T o3) {
        return AHashSet
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .build();
    }
    public static <T> AHashSet<T> of(T o1, T o2, T o3, T o4) {
        return AHashSet
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .add(o4)
                .build();
    }
    public static <T> AHashSet<T> of(T o1, T o2, T o3, T o4, T o5, T... others) {
        return AHashSet
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .add(o4)
                .add(o5)
                .addAll(others)
                .build();
    }

    public static <T> AHashSet<T> from(T[] that) {
        return fromIterator(Arrays.asList(that).iterator());
    }
    public static <T> AHashSet<T> from(Iterable<T> that) {
        return fromIterator(that.iterator());
    }

    public static <T> AHashSet<T> fromIterator(Iterator<T> it) {
        return AHashSet.<T>builder()
                .addAll(it)
                .build();
    }

    AHashSet (CompactHashMap<SetEntryWithEquality<T>> compactHashMap) {
        this.compactHashMap = compactHashMap;
    }

    protected Object writeReplace() {
        return new SerializationProxy(this);
    }

    abstract AHashSet<T> newInstance(CompactHashMap<SetEntryWithEquality<T>> compactHashMap);
    abstract SetEntryWithEquality<T> newEntry(Object o);
    @Override public abstract <U> ACollectionBuilder<U, AHashSet<U>> newBuilder();

    @Override public boolean equals (Object o) {
        return ASetSupport.equals(this, o);
    }
    @Override public int hashCode() {
        return ASetSupport.hashCode(this);
    }

    @Override public String toString () {
        return ACollectionSupport.toString(AHashSet.class, this);
    }

    @Override public AHashSet<T> toSet () {
        return this;
    }

    @Override public AHashSet<T> plus (T o) {
        return newInstance(compactHashMap.updated0(newEntry(o), 0));
    }

    @Override public AHashSet<T> minus (T o) {
        return newInstance(compactHashMap.removed0(newEntry(o), 0));
    }

    @Override public boolean contains (Object o) {
        return compactHashMap.get0(newEntry(o), 0) != null;
    }

    @Override public AHashSet<T> union (Iterable<T> that) {
        AHashSet<T> result = this;
        for (T o: that)
            result = result.plus(o);
        return result;
    }

    @Override public AHashSet<T> intersect (Set<T> that) {
        return filter(that::contains);
    }

    @Override public AHashSet<T> diff (Set<T> that) {
        AHashSet<T> result = this;
        for (T o: that)
            result = result.minus(o);
        return result;
    }

    @Override public AIterator<T> iterator () {
        return compactHashMap.iterator().map(e -> e.el);
    }

    @Override public boolean isEmpty () {
        return compactHashMap.isEmpty();
    }

    @Override public <U> AHashSet<U> map (Function<T, U> f) {
        return ACollectionSupport.map(newBuilder(), this, f);
    }

    @Override public <U> AHashSet<U> flatMap (Function<T, Iterable<U>> f) {
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }

    @Override public <U> AHashSet<U> collect (Predicate<T> filter, Function<T, U> f) {
        return ACollectionSupport.collect(newBuilder(), this, filter, f);
    }

    @Override public AHashSet<T> filter (Predicate<T> f) {
        return ACollectionDefaults.super.filter(f);
    }

    @Override public AHashSet<T> filterNot (Predicate<T> f) {
        return filter(f.negate());
    }

    @Override public <K> AMap<K, AHashSet<T>> groupBy (Function<T, K> keyExtractor) {
        return ACollectionDefaults.super.groupBy(keyExtractor);
    }

    @Override public int size () {
        return compactHashMap.size();
    }

    @Override public boolean containsAll (Collection<?> c) {
        return ACollectionDefaults.super.containsAll(c);
    }

    public static <T> Builder<T> builder() {
        return new EqualsBuilder<>();
    }

    static class EqualsHashSet<T> extends AHashSet<T> {
        EqualsHashSet (CompactHashMap<SetEntryWithEquality<T>> compactHashMap) {
            super(compactHashMap);
        }

        @Override AHashSet<T> newInstance (CompactHashMap<SetEntryWithEquality<T>> compactHashMap) {
            return new EqualsHashSet<>(compactHashMap);
        }

        @Override SetEntryWithEquality<T> newEntry (Object o) {
            //noinspection unchecked
            return new EqualsSetEntry<T>((T) o);
        }

        @Override public <U> ACollectionBuilder<U, AHashSet<U>> newBuilder () {
            return new EqualsBuilder<>();
        }
    }

    public abstract static class Builder<T> implements ACollectionBuilder<T, AHashSet<T>> {
        @SuppressWarnings("unchecked")
        CompactHashMap<SetEntryWithEquality<T>> result = CompactHashMap.EMPTY;

        abstract AHashSet<T> newInstance();
        abstract SetEntryWithEquality<T> newElement(T el);

        @Override public ACollectionBuilder<T, AHashSet<T>> add (T el) {
            result = result.updated0(newElement(el), 0);
            return this;
        }

        @Override public AHashSet<T> build () {
            return newInstance();
        }
    }
    static class EqualsBuilder<T> extends Builder<T> {
        @Override AHashSet<T> newInstance () {
            return new EqualsHashSet<>(result);
        }
        @Override SetEntryWithEquality<T> newElement (T el) {
            return new EqualsSetEntry<>(el);
        }
    }

    static abstract class SetEntryWithEquality<T> implements CompactHashMap.EntryWithEquality {
        T el;

        SetEntryWithEquality (T el) {
            this.el = el;
        }
    }

    private static class EqualsSetEntry<T> extends SetEntryWithEquality<T> {
        EqualsSetEntry (T el) {
            super(el);
        }

        @Override public boolean hasEqualKey (CompactHashMap.EntryWithEquality other) {
            return Objects.equals(el, ((SetEntryWithEquality) other).el);
        }

        @Override public int keyHash () {
            return Objects.hashCode(el);
        }
    }

    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 123L;

        private transient AHashSet<?> orig;

        SerializationProxy (AHashSet<?> orig) {
            this.orig = orig;
        }

        private void writeObject(ObjectOutputStream oos) throws IOException {
            oos.writeInt(orig.size());
            for (Object o: orig) {
                oos.writeObject(o);
            }
        }

        private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            final Builder<Object> builder = builder();
            final int size = ois.readInt();
            for (int i=0; i<size; i++) {
                builder.add(ois.readObject());
            }
            orig = builder.build();
        }

        private Object readResolve() {
            return orig;
        }
    }
}
