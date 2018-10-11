package com.ajjpj.acollections;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;


class AIteratorWrapper<T> extends AbstractAIterator<T> {
    private final Iterator<T> inner;

    AIteratorWrapper (Iterator<T> inner) {
        this.inner = inner;
    }

    @Override public boolean hasNext () {
        return inner.hasNext();
    }

    @Override public T next () {
        return inner.next();
    }
}
