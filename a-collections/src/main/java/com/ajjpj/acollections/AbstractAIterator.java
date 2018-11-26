package com.ajjpj.acollections;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;


public abstract class AbstractAIterator<T> implements AIterator<T> {

    @Override public AIterator<T> filter (Predicate<T> f) {
        return filter(this, f);
    }

    @Override public <U> AIterator<U> flatMap (Function<? super T, ? extends Iterator<? extends U>> f) {
        return flatMap(this, f);
    }

    public static <T> AIterator<T> filter(Iterator<T> it, Predicate<T> f) {
        return new AbstractAIterator<T>() {
            private T next;
            private boolean hasNext;

            {
                advance();
            }

            private void advance() {
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

    public static <T,U> AIterator<U> flatMap(Iterator<T> it, Function<? super T, ? extends Iterator<? extends U>> f) {
        return new FlatMapIterator<T,U>(it, f);
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

    static class ConcatIterator<T> extends AbstractAIterator<T> {
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

    static class FlatMapIterator<T,U> extends AbstractAIterator<U> {
        private final Iterator<T> outer;
        private final Function<? super T, ? extends Iterator<? extends U>> f;
        private Iterator<? extends U> inner;

        public FlatMapIterator (Iterator<T> outer, Function<? super T, ? extends Iterator<? extends U>> f) {
            this.outer = outer;
            this.f = f;
        }

        @Override public boolean hasNext () {
            while (inner == null || !inner.hasNext()) {
                if (! outer.hasNext()) return false;
                inner = f.apply(outer.next());
            }
            return true;
        }

        @Override public U next () {
            if (!hasNext()) throw new NoSuchElementException();
            // the previous check ensures that we are now on a non-exhausted iterator
            return inner.next();
        }
    }
}
