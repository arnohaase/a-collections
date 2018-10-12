package com.ajjpj.acollections.util;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.immutable.AbstractImmutableCollection;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


public abstract class AOption<T> extends AbstractImmutableCollection<T> {
    @Override public AEquality equality () {
        return AEquality.EQUALS;
    }

    public abstract T get();
    public abstract T orElse(T defaultValue);
    public abstract T orElseGet(Supplier<T> f);
    public abstract <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;
    public abstract Optional<T> toOptional();

    public static <T> AOption<T> of(T o) {
        if (o != null) return some(o);
        else return none();
    }

    public static <T> AOption<T> some(T o) {
        return new ASome<>(o);
    }
    public static <T> AOption<T> none() {
        //noinspection unchecked
        return ANone;
    }

    @Override public ACollectionBuilder<T, AOption<T>> newBuilder () {
        // Using a builder for AOption may look weird and is not very efficient, but there is no reason not to have one for compatibility
        //  reasons. There should however be optimized implementation for all generic, builder-based transformation methods.

        return new ACollectionBuilder<T, AOption<T>>() {
            private AOption<T> result = none();

            @Override public ACollectionBuilder<T, AOption<T>> add (T el) {
                if (result.nonEmpty()) throw new IllegalStateException("an AOption can hold at most one element");
                result = some(el);
                return this;
            }

            @Override public AOption<T> build () {
                return result;
            }
        };
    }

    private static class ASome<T> extends AOption<T> {
        private final T el;

        ASome (T el) {
            this.el = el;
        }

        @Override public boolean equals (Object o) {
            if (o == this) return true;
            if (! (o instanceof ASome)) return false;
            return Objects.equals(el, ((ASome) o).el);
        }

        @Override public int hashCode () {
            return 1 + 31*Objects.hashCode(el);
        }

        @Override public T get () {
            return el;
        }

        @Override public T orElse (T defaultValue) {
            return el;
        }

        @Override public T orElseGet (Supplier<T> f) {
            return el;
        }

        @Override public <X extends Throwable> T orElseThrow (Supplier<? extends X> exceptionSupplier) throws X {
            return el;
        }

        @Override public Optional<T> toOptional () {
            return Optional.of(el);
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

    private static final AOption ANone = new AOption<Object>() {
        @Override public boolean equals (Object o) {
            return o == this;
        }

        @Override public int hashCode () {
            return -31;
        }

        @Override public Object get () {
            throw new NoSuchElementException();
        }

        @Override public Object orElse (Object defaultValue) {
            return defaultValue;
        }

        @Override public Object orElseGet (Supplier f) {
            return f.get();
        }

        @Override public <X extends Throwable> Object orElseThrow (Supplier<? extends X> exceptionSupplier) throws X {
            throw exceptionSupplier.get();
        }

        @Override public Optional<Object> toOptional () {
            return Optional.empty();
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
