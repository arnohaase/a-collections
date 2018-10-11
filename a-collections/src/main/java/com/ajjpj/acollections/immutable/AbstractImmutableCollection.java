package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollection;
import java.util.Collection;


abstract class AbstractImmutableCollection<T> implements ACollection<T> {

    @Override
    public boolean equals (Object o) {
        return false; //TODO
    }

    @Override
    public int hashCode () {
        return 0; //TODO
    }

    @Override public boolean add (T t) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public boolean remove (Object o) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public boolean addAll (Collection<? extends T> c) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public boolean removeAll (Collection<?> c) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public boolean retainAll (Collection<?> c) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public void clear () { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
}
