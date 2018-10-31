package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.mutable.AMutableSetWrapper;

import java.util.Iterator;
import java.util.Set;


public interface ASet<T> extends ACollection<T>, Set<T> {
    static <T> ASet<T> wrap(Set<T> s) {
        return AMutableSetWrapper.wrap(s);
    }

    static<T> AHashSet<T> empty() {
        return AHashSet.empty();
    }

    static <T> AHashSet<T> of() {
        return AHashSet.of();
    }
    static <T> AHashSet<T> of(T o) {
        return AHashSet.of(o);
    }
    static <T> AHashSet<T> of(T o1, T o2) {
        return AHashSet.of (o1, o2);
    }
    static <T> AHashSet<T> of(T o1, T o2, T o3) {
        return AHashSet.of(o1, o2, o3);
    }
    static <T> AHashSet<T> of(T o1, T o2, T o3, T o4) {
        return AHashSet.of(o1, o2, o3, o4);
    }
    @SafeVarargs static <T> AHashSet<T> of(T o1, T o2, T o3, T o4, T o5, T... others) {
        return AHashSet.of(o1, o2, o3, o4, o5, others);
    }

    static <T> AHashSet<T> from(T[] that) {
        return AHashSet.from(that);
    }
    static <T> AHashSet<T> from(Iterable<T> that) {
        return AHashSet.from(that);
    }

    static <T> AHashSet<T> fromIterator(Iterator<T> it) {
        return AHashSet.fromIterator(it);
    }


    @Override <U> ACollectionBuilder<U, ? extends ASet<U>> newBuilder ();

    ASet<T> plus (T o);
    //TODO plusAll
    ASet<T> minus (T o);

    ASet<T> union(Iterable<T> that);
    ASet<T> intersect(Set<T> that);
    ASet<T> diff(Set<T> that);

    AIterator<? extends ASet<T>> subsets();
    AIterator<? extends ASet<T>> subsets(int len);
}
