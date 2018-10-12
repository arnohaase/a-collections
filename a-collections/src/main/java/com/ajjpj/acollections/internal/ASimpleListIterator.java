package com.ajjpj.acollections.internal;

import java.util.Iterator;
import java.util.ListIterator;


/**
 * {@link ListIterator} is rarely used any more as other APIs like the stream API appeared. It is however used by some
 *  library code (e.g. {@link java.util.AbstractList#equals(Object)}, so we provide a rudimentary implementation.
 */
public class ASimpleListIterator<T> implements ListIterator<T> {
    private final Iterator<T> internalIterator;
    private int curIndex = -1;

    public ASimpleListIterator(Iterator<T> iterator, int seekIndex){
        internalIterator = iterator;
        curIndex = seekIndex-1;
        for (int i=0; i<seekIndex; i++) internalIterator.next();
    }

    @Override public boolean hasNext() {
        return internalIterator.hasNext();
    }

    @Override public T next() {
        curIndex += 1;
        return internalIterator.next();
    }

    @Override public void remove() {
        internalIterator.remove();
    }

    @Override public boolean hasPrevious() {
        throw new UnsupportedOperationException();
    }

    @Override public T previous() {
        throw new UnsupportedOperationException();
    }

    @Override public int nextIndex() {
        return curIndex+1;
    }

    @Override public int previousIndex() {
        return curIndex-1;
    }

    @Override public void set(T t) {
        throw new UnsupportedOperationException();
    }

    @Override public void add(T t) {
        throw new UnsupportedOperationException();
    }
}
