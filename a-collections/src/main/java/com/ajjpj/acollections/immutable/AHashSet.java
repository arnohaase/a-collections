package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.ASet;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.util.AEquality;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;
import java.util.function.Predicate;


public abstract class AHashSet<T> extends AbstractImmutableCollection<T> implements ASet<T>, ACollectionDefaults<T, AHashSet<T>> {
    private final CompactHashMap<SetEntryWithEquality<T>> compactHashMap;

    public static<T> AHashSet<T> empty() {
        return empty(AEquality.EQUALS);
    }
    public static<T> AHashSet<T> empty(AEquality equality) {
        if (equality == AEquality.EQUALS) return new EqualsHashSet<>(CompactHashMap.empty());
        if (equality == AEquality.IDENTITY) return new IdentityHashSet<>(CompactHashMap.empty());
        return new CustomHashSet<>(CompactHashMap.empty(), equality);
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

    public static <T> AHashSet<T> from(Iterable<T> that) {
        return from(that, AEquality.EQUALS);
    }
    public static <T> AHashSet<T> from(Iterable<T> that, AEquality equality) {
        return AHashSet.<T>builder(equality)
                .addAll(that)
                .build();
    }

    public static <T> AHashSet<T> fromIterator(Iterator<T> it) {
        return fromIterator(it, AEquality.EQUALS);
    }
    public static <T> AHashSet<T> fromIterator(Iterator<T> it, AEquality equality) {
        return AHashSet.<T>builder(equality)
                .addAll(it)
                .build();
    }

    AHashSet (CompactHashMap<SetEntryWithEquality<T>> compactHashMap) {
        this.compactHashMap = compactHashMap;
    }

    abstract AHashSet<T> newInstance(CompactHashMap<SetEntryWithEquality<T>> compactHashMap);
    abstract SetEntryWithEquality<T> newEntry(Object o);
    @Override public abstract <U> ACollectionBuilder<U, AHashSet<U>> newBuilder();

    @Override public AHashSet<T> toSet () {
        return this;
    }

    @Override public AHashSet<T> added (T o) {
        return newInstance(compactHashMap.updated0(newEntry(o), 0));
    }

    @Override public AHashSet<T> removed (T o) {
        return newInstance(compactHashMap.removed0(newEntry(o), 0));
    }

    @Override public boolean contains (Object o) {
        return compactHashMap.get0(newEntry(o), 0) != null;
    }

    @Override public AHashSet<T> union (Iterable<T> that) {
        AHashSet<T> result = this;
        for (T o: that)
            result = result.added(o);
        return result;
    }

    @Override public AHashSet<T> intersect (Collection<T> that) {
        return filter(that::contains);
    }

    @Override public AHashSet<T> diff (Iterable<T> that) {
        AHashSet<T> result = this;
        for (T o: that)
            result = result.removed(o);
        return result;
    }

    @Override public AIterator<ASet<T>> subsets () {
        return null; //TODO
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

    @Override public int size () {
        return compactHashMap.size();
    }

    @Override public Object[] toArray () {
        return ACollectionSupport.toArray(this);
    }

    @Override public <T1> T1[] toArray (T1[] a) {
        return ACollectionSupport.toArray(this, a);
    }

    @Override public boolean containsAll (Collection<?> c) {
        return ACollectionDefaults.super.containsAll(c);
    }

    public static <T> ACollectionBuilder<T, AHashSet<T>> builder() {
        return builder(AEquality.EQUALS);
    }
    public static <T> ACollectionBuilder<T, AHashSet<T>> builder(AEquality equality) {
        if (equality == AEquality.EQUALS) return new EqualsBuilder<>();
        if (equality == AEquality.IDENTITY) return new IdentityBuilder<>();
        return new CustomBuilder<>(equality);
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

        @Override public AEquality equality () {
            return AEquality.EQUALS;
        }

        @Override public <U> ACollectionBuilder<U, AHashSet<U>> newBuilder () {
            return new EqualsBuilder<>();
        }
    }
    static class IdentityHashSet<T> extends AHashSet<T> {
        IdentityHashSet (CompactHashMap<SetEntryWithEquality<T>> compactHashMap) {
            super(compactHashMap);
        }

        @Override AHashSet<T> newInstance (CompactHashMap<SetEntryWithEquality<T>> compactHashMap) {
            return new IdentityHashSet<>(compactHashMap);
        }

        @Override SetEntryWithEquality<T> newEntry (Object o) {
            //noinspection unchecked
            return new IdentitySetEntry<T>((T) o);
        }

        @Override public AEquality equality () {
            return AEquality.IDENTITY;
        }

        @Override public <U> ACollectionBuilder<U, AHashSet<U>> newBuilder () {
            return new IdentityBuilder<>();
        }
    }
    static class CustomHashSet<T> extends AHashSet<T> {
        private final AEquality equality;
        CustomHashSet (CompactHashMap<SetEntryWithEquality<T>> compactHashMap, AEquality equality) {
            super(compactHashMap);
            this.equality = equality;
        }

        @Override AHashSet<T> newInstance (CompactHashMap<SetEntryWithEquality<T>> compactHashMap) {
            return new CustomHashSet<>(compactHashMap, equality);
        }

        @Override SetEntryWithEquality<T> newEntry (Object o) {
            //noinspection unchecked
            return new CustomSetEntry<T>((T) o, equality);
        }

        @Override public AEquality equality () {
            return equality;
        }

        @Override public <U> ACollectionBuilder<U, AHashSet<U>> newBuilder () {
            return new CustomBuilder<>(equality);
        }
    }

    abstract static class Builder<T> implements ACollectionBuilder<T, AHashSet<T>> {
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
    static class IdentityBuilder<T> extends Builder<T> {
        @Override AHashSet<T> newInstance () {
            return new IdentityHashSet<>(result);
        }

        @Override SetEntryWithEquality<T> newElement (T el) {
            return new IdentitySetEntry<>(el);
        }
    }
    static class CustomBuilder<T> extends Builder<T> {
        private final AEquality equality;
        CustomBuilder (AEquality equality) {
            this.equality = equality;
        }

        @Override AHashSet<T> newInstance () {
            return new CustomHashSet<>(result, equality);
        }

        @Override SetEntryWithEquality<T> newElement (T el) {
            return new CustomSetEntry<>(el, equality);
        }
    }

    private static abstract class SetEntryWithEquality<T> implements CompactHashMap.EntryWithEquality {
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
            return AEquality.EQUALS.equals(el, ((SetEntryWithEquality) other).el);
        }

        @Override public int keyHash () {
            return AEquality.EQUALS.hashCode(el);
        }
    }

    private static class IdentitySetEntry<T> extends SetEntryWithEquality<T> {
        IdentitySetEntry (T el) {
            super(el);
        }

        @Override public boolean hasEqualKey (CompactHashMap.EntryWithEquality other) {
            return AEquality.IDENTITY.equals(el, ((SetEntryWithEquality) other).el);
        }

        @Override public int keyHash () {
            return AEquality.IDENTITY.hashCode(el);
        }
    }

    private static class CustomSetEntry<T> extends SetEntryWithEquality<T> {
        final AEquality equality;

        CustomSetEntry (T el, AEquality equality) {
            super(el);
            this.equality = equality;
        }

        @Override public boolean hasEqualKey (CompactHashMap.EntryWithEquality other) {
            return equality.equals(el, ((SetEntryWithEquality) other).el);
        }

        @Override public int keyHash () {
            return equality.hashCode(el);
        }
    }
}
