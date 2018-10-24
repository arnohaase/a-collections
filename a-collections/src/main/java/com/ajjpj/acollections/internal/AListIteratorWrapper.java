package com.ajjpj.acollections.internal;

import com.ajjpj.acollections.AbstractAIterator;

import java.util.ListIterator;


public class AListIteratorWrapper<T> extends AbstractAIterator<T> implements ListIterator<T> {
    private final  ListIterator<T> inner;

    public AListIteratorWrapper (ListIterator<T> iterator){
        this.inner = iterator;
    }

    @Override
    public boolean hasNext() {
        return inner.hasNext();
    }

    @Override
    public T next() {
        return inner.next();
    }

    @Override
    public boolean hasPrevious() {
        return inner.hasPrevious();
    }

    @Override
    public T previous() {
        return inner.previous();
    }

    @Override
    public int nextIndex() {
        return inner.nextIndex();
    }

    @Override
    public int previousIndex() {
        return inner.previousIndex();
    }

    @Override
    public void remove() {
        inner.remove();
    }

    @Override
    public void set(T t) {
        inner.set(t);
    }

    @Override
    public void add(T t) {
        inner.add(t);
    }
}
