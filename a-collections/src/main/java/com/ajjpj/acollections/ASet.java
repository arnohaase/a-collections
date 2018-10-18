package com.ajjpj.acollections;

import java.util.Set;


public interface ASet<T> extends ACollection<T>, Set<T> {
    ASet<T> added(T o);
    ASet<T> removed(T o);

    ASet<T> union(Iterable<T> that);
    ASet<T> intersect(Set<T> that);
    ASet<T> diff(Set<T> that);

    AIterator<? extends ASet<T>> subsets();
}
