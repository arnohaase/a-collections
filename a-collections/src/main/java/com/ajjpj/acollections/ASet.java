package com.ajjpj.acollections;

import java.util.Collection;
import java.util.Set;


public interface ASet<T> extends ACollection<T>, Set<T> {
    ASet<T> added(T o);
    ASet<T> removed(T o);

    ASet<T> union(Iterable<T> that);
    ASet<T> intersect(Collection<T> that);
    ASet<T> diff(Iterable<T> that);

    AIterator<ASet<T>> subsets();
}
