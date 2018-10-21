package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.ASet;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.util.AEquality;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;


public class AMutableSetWrapper<T> implements ASet<T>, ACollectionDefaults<T, AMutableSetWrapper<T>> {
    private final Set<T> inner;

    public static<T> AMutableSetWrapper<T> wrap(Set<T> inner) {
        return new AMutableSetWrapper<>(inner);
    }

    public AMutableSetWrapper (Set<T> inner) {
        this.inner = inner;
    }

    @Override public ASet<T> added (T o) {
        inner.add(o);
        return this;
    }

    @Override public AMutableSetWrapper<T> removed (T o) {
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

    @Override public AIterator<? extends ASet<T>> subsets () {
        return null; //TODO
    }

    @Override public AEquality equality () {
        return AEquality.EQUALS;
    }

    @Override public AIterator<T> iterator () {
        return AIterator.wrap(inner.iterator());
    }

    @Override public <U> ACollectionBuilder<U, ? extends ACollection<U>> newBuilder () {
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

    @Override public String toString () {
        return getClass().getSimpleName() + "@" + inner;
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

        @Override public AEquality equality () {
            return AEquality.EQUALS;
        }
    }
}
