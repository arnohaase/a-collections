package com.ajjpj.acollections.util;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.immutable.AbstractImmutableCollection;

import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


public abstract class AOption<T> extends AbstractImmutableCollection<T> {
    @Override public AEquality equality () {
        return AEquality.EQUALS;
    }

    public abstract T get();
    public abstract T getOrElse(T defaultValue);
    public abstract T getOrElseCalc(Supplier<T> f);

    public static <T> AOption<T> some(T o) {
        return new ASome<>(o);
    }
    public static <T> AOption<T> none() {
        //noinspection unchecked
        return ANone;
    }

    private static class ASome<T> extends AOption<T> {
        private final T el;

        ASome (T el) {
            this.el = el;
        }

        @Override public T get () {
            return el;
        }

        @Override public T getOrElse (T defaultValue) {
            return el;
        }

        @Override public T getOrElseCalc (Supplier<T> f) {
            return el;
        }

        @Override public AIterator<T> iterator () {
            return AIterator.single(el);
        }

        @Override public <U> ACollection<U> map (Function<T, U> f) {
            return AOption.some(f.apply(el));
        }

        @Override public ACollection<T> filter (Predicate<T> f) {
            if (f.test(el)) return this;
            else return AOption.none();
        }

        @Override public <U> ACollection<U> collect (Predicate<T> filter, Function<T, U> f) {
            if (filter.test(el)) return map(f);
            else return AOption.none();
        }

        @Override public int size () {
            return 1;
        }

        @Override public boolean isEmpty () {
            return false;
        }
    }

    private static final AOption ANone = new AOption() {
        @Override public Object get () {
            throw new NoSuchElementException();
        }

        @Override public Object getOrElse (Object defaultValue) {
            return defaultValue;
        }

        @Override public Object getOrElseCalc (Supplier f) {
            return f.get();
        }

        @Override public AIterator iterator () {
            return AIterator.empty();
        }

        @Override public ACollection map (Function f) {
            return this;
        }

        @Override public ACollection filter (Predicate f) {
            return this;
        }

        @Override public ACollection collect (Predicate filter, Function f) {
            return this;
        }

        @Override public int size () {
            return 0;
        }

        @Override public boolean isEmpty () {
            return true;
        }
    };

}
