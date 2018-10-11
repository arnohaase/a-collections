package com.ajjpj.acollections;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;


public abstract class AbstractAIterator<T> implements AIterator<T> {
    static final AIterator<Object> empty = new AbstractAIterator<Object>() {
        @Override public boolean hasNext () {
            return false;
        }
        @Override public Object next () {
            throw new NoSuchElementException();
        }
    };

    public abstract boolean hasNext();

    @Override public AIterator<T> filter (Predicate<T> f) {
        return filter(this, f);
    }

    public static <T> AIterator<T> filter(Iterator<T> it, Predicate<T> f) {
        return new AbstractAIterator<T>() {
            private T next;
            private boolean hasNext;

            {
                advance();
            }

            private void advance() {
                //noinspection StatementWithEmptyBody
                while (it.hasNext()) {
                    final T candidate = it.next();
                    if (f.test(candidate)) {
                        hasNext = true;
                        next = candidate;
                        return;
                    }
                }
                next = null;
                hasNext = false;
            }

            @Override public boolean hasNext () {
                return hasNext;
            }

            @Override public T next () {
                if (! hasNext) throw new NoSuchElementException();
                final T result = next;
                advance();
                return result;
            }
        };

    }

    //TODO concat
}
