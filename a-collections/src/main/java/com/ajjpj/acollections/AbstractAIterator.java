package com.ajjpj.acollections;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiPredicate;
import java.util.function.Predicate;


public abstract class AbstractAIterator<T> implements AIterator<T> {

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

    static final AIterator<Object> empty = new AbstractAIterator<Object>() {
        @Override public boolean hasNext () {
            return false;
        }
        @Override public Object next () {
            throw new NoSuchElementException();
        }
    };

    static class Single<T> extends AbstractAIterator<T> {
        private final T el;
        private boolean hasNext = true;

        Single (T el) {
            this.el = el;
        }

        @Override public boolean hasNext () {
            return hasNext;
        }

        @Override public T next () {
            if (!hasNext)
                throw new NoSuchElementException();

            hasNext = false;
            return el;
        }
    }

    static class ConcatIterator<T> implements Iterator<T> {
        private final Iterator<? extends T>[] itit;
        private int curIt = 0;

        @SafeVarargs
        ConcatIterator (Iterator<? extends T>... inner) {
            itit = inner;
        }

        @Override public boolean hasNext () {
            while(curIt < itit.length) {
                if (itit[curIt].hasNext()) return true;
                curIt += 1;
            }
            return false;
        }

        @Override public T next () {
            if (!hasNext()) throw new NoSuchElementException();
            // the previous check ensures that we are now on a non-exhausted iterator
            return itit[curIt].next();
        }
    }
}
