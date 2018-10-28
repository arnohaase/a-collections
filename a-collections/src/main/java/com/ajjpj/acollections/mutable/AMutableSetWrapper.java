package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.ASetDefaults;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;


public class AMutableSetWrapper<T> implements ASet<T>, ACollectionDefaults<T, AMutableSetWrapper<T>>, ASetDefaults<T, AMutableSetWrapper<T>> {
    private final Set<T> inner;

    public static<T> AMutableSetWrapper<T> wrap(Set<T> inner) {
        return new AMutableSetWrapper<>(inner);
    }

    public static <T> AMutableSetWrapper<T> from(Iterator<T> it) {
        return AMutableSetWrapper.<T>builder().addAll(it).build();
    }
    public static <T> AMutableSetWrapper<T> from(Iterable<T> iterable) {
        return AMutableSetWrapper.<T>builder().addAll(iterable).build();
    }

    public static <T> AMutableSetWrapper<T> of(T o1) {
        return AMutableSetWrapper.<T>builder().add(o1).build();
    }

    //TODO static factories, empty

    public AMutableSetWrapper (Set<T> inner) {
        this.inner = inner;
    }

    @Override public AMutableSetWrapper<T> toMutableSet () {
        return this;
    }

    @Override public ASet<T> plus (T o) {
        inner.add(o);
        return this;
    }

    @Override public AMutableSetWrapper<T> minus (T o) {
        inner.remove(o);
        return this;
    }

    @Override public AMutableSetWrapper<T> union (Iterable<T> that) {
        for (T o: that) inner.add(o);
        return this;
    }

    @Override public AMutableSetWrapper<T> intersect (Set<T> that) {
        inner.retainAll(that);
        return this;
    }

    @Override public AMutableSetWrapper<T> diff (Set<T> that) {
        inner.removeAll(that);
        return this;
    }

    @Override public AIterator<T> iterator () {
        return AIterator.wrap(inner.iterator());
    }

    @Override public <U> ACollectionBuilder<U, ? extends ASet<U>> newBuilder () {
        return builder();
    }

    @Override public boolean isEmpty () {
        return inner.isEmpty();
    }

    @Override public <U> ACollection<U> map (Function<T, U> f) {
        return ACollectionSupport.map(newBuilder(), this, f);
    }

    @Override public <U> ACollection<U> flatMap (Function<T, Iterable<U>> f) {
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }

    @Override public <U> ACollection<U> collect (Predicate<T> filter, Function<T, U> f) {
        return ACollectionSupport.collect(newBuilder(), this, filter, f);
    }

    @Override public AMutableSetWrapper<T> filter (Predicate<T> f) {
        return ACollectionDefaults.super.filter(f);
    }

    @Override public AMutableSetWrapper<T> filterNot (Predicate<T> f) {
        return ACollectionDefaults.super.filterNot(f);
    }

    @Override public <K> AMap<K, AMutableSetWrapper<T>> groupBy (Function<T, K> keyExtractor) {
        return ACollectionDefaults.super.groupBy(keyExtractor);
    }

    @Override public int size () {
        return inner.size();
    }

    @Override public Object[] toArray () {
        return inner.toArray();
    }

    @Override public <T1> T1[] toArray (T1[] a) {
        return inner.toArray(a);
    }

    @Override public boolean add (T t) {
        return inner.add(t);
    }

    @Override public boolean remove (Object o) {
        return inner.remove(o);
    }

    @Override public boolean contains (Object o) {
        return inner.contains(o);
    }

    @Override public boolean containsAll (Collection<?> c) {
        return inner.containsAll(c);
    }

    @Override public boolean addAll (Collection<? extends T> c) {
        return inner.addAll(c);
    }

    @Override public boolean removeAll (Collection<?> c) {
        return inner.removeAll(c);
    }

    @Override public boolean retainAll (Collection<?> c) {
        return inner.retainAll(c);
    }

    @Override public void clear () {
        inner.clear();
    }

    @Override public Spliterator<T> spliterator () {
        return inner.spliterator();
    }
    @Override public boolean removeIf (Predicate<? super T> filter) {
        return inner.removeIf(filter);
    }
    @Override public Stream<T> stream () {
        return inner.stream();
    }
    @Override public Stream<T> parallelStream () {
        return inner.parallelStream();
    }
    @Override public void forEach (Consumer<? super T> action) {
        inner.forEach(action);
    }

    @Override public String toString () {
        return getClass().getSimpleName() + ":" + inner;
    }

    @Override public boolean equals (Object obj) {
        return inner.equals(obj);
    }

    @Override public int hashCode () {
        return inner.hashCode();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> implements ACollectionBuilder<T, AMutableSetWrapper<T>> {
        private Set<T> result = new HashSet<>();

        @Override public ACollectionBuilder<T, AMutableSetWrapper<T>> add (T el) {
            result.add(el);
            return this;
        }

        @Override public AMutableSetWrapper<T> build () {
            return AMutableSetWrapper.wrap(result);
        }
    }
}
