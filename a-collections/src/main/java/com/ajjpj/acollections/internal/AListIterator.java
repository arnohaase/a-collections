package com.ajjpj.acollections.internal;

import com.ajjpj.acollections.AbstractAIterator;

import java.util.ListIterator;

public class AListIterator<T> extends AbstractAIterator<T> implements ListIterator<T> {

    private ListIterator<T> listIterator;

    public AListIterator(ListIterator<T> iterator){
        this.listIterator = iterator;
    }

    @Override
    public boolean hasNext() {
        return listIterator.hasNext();
    }

    @Override
    public T next() {
        return listIterator.next();
    }

    @Override
    public boolean hasPrevious() {
        return listIterator.hasPrevious();
    }

    @Override
    public T previous() {
        return listIterator.previous();
    }

    @Override
    public int nextIndex() {
        return listIterator.nextIndex();
    }

    @Override
    public int previousIndex() {
        return listIterator.previousIndex();
    }

    @Override
    public void remove() {
        listIterator.remove();
    }

    @Override
    public void set(T t) {
        listIterator.set(t);
    }

    @Override
    public void add(T t) {
        listIterator.add(t);
    }
}
