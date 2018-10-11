package com.ajjpj.acollections;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;


public abstract class AbstractAIterator<T> implements AIterator<T> {
    private boolean isBeforeFirst = true;
    private T current;

    public abstract boolean hasNext();
    protected abstract T doNext();

    @Override public final T next () {
        final T result = doNext();
        isBeforeFirst = false;
        current = result;
        return result;
    }

    @Override public T current () {
        if (isBeforeFirst) throw new IllegalStateException();
        return current;
    }

    @Override public AIterator<T> filter (Predicate<T> f) {
        final AIterator<T> outer = this;
        return new AbstractAIterator<T>() {
            private T next;
            private boolean hasNext;

            {
                advance();
            }

            private void advance() {
                //noinspection StatementWithEmptyBody
                while (outer.hasNext()) {
                    final T candidate = outer.next();
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

            @Override public T doNext () {
                if (! hasNext) throw new NoSuchElementException();
                final T result = next;
                advance();
                return result;
            }
        };
    }

    //TODO concat
}
