package com.ajjpj.acollections.util;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.immutable.AbstractImmutableCollection;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


public abstract class AOption<T> extends AbstractImmutableCollection<T> implements ACollectionDefaults<T, AOption<T>> {
    @Override public AEquality equality () {
        return AEquality.EQUALS;
    }

    public abstract T get();
    public abstract T orElse(T defaultValue);
    public abstract T orElseGet(Supplier<T> f);
    public abstract T orNull();
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

    public boolean isDefined() {
        return nonEmpty();
    }

    @Override public abstract <U> AOption<U> map (Function<T,U> f);
    @Override public <U> AOption<U> flatMap (Function<T, Iterable<U>> f) {
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }
    @Override public abstract <U> AOption<U> collect (Predicate<T> filter, Function<T, U> f);

    @Override public abstract AOption<T> filter (Predicate<T> f);
    @Override public AOption<T> filterNot (Predicate<T> f) {
        return ACollectionDefaults.super.filterNot(f);
    }

    @Override public Object[] toArray () {
        return ACollectionSupport.toArray(this);
    }

    @Override public <T1> T1[] toArray (T1[] a) {
        return ACollectionSupport.toArray(this, a);
    }

    @Override public abstract boolean contains(Object o);
    
    @Override public boolean containsAll (Collection<?> c) {
        return ACollectionDefaults.super.containsAll(c);
    }

    @Override public <U> ACollectionBuilder<U, AOption<U>> newBuilder () {
        // Using a builder for AOption may look weird and is not very efficient, but there is no reason not to have one for compatibility
        //  reasons. There should however be optimized implementation for all generic, builder-based transformation methods.

        return new ACollectionBuilder<U, AOption<U>>() {
            private AOption<U> result = none();

            @Override public ACollectionBuilder<U, AOption<U>> add (U el) {
                if (result.nonEmpty()) throw new IllegalStateException("an AOption can hold at most one element");
                result = some(el);
                return this;
            }

            @Override public AOption<U> build () {
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

        @Override public T orNull() {
            return el;
        }

        @Override public Optional<T> toOptional () {
            return Optional.of(el);
        }

        @Override public AIterator<T> iterator () {
            return AIterator.single(el);
        }

        @Override public <U> AOption<U> map (Function<T, U> f) {
            return AOption.some(f.apply(el));
        }

        @Override public AOption<T> filter (Predicate<T> f) {
            if (f.test(el)) return this;
            else return AOption.none();
        }

        @Override public <U> AOption<U> collect (Predicate<T> filter, Function<T, U> f) {
            if (filter.test(el)) return map(f);
            else return AOption.none();
        }

        @Override public boolean contains (Object o) {
            return Objects.equals(el, o);
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

        @Override public Object orNull() {
            return null;
        }


        @Override public Optional<Object> toOptional () {
            return Optional.empty();
        }

        @Override public AIterator<Object> iterator () {
            return AIterator.empty();
        }

        @SuppressWarnings("unchecked")
        @Override public AOption map (Function f) {
            return this;
        }

        @Override public AOption<Object> filter (Predicate f) {
            return this;
        }
        @Override public AOption<Object> filterNot (Predicate f) {
            return this;
        }

        @SuppressWarnings("unchecked")
        @Override public AOption collect (Predicate filter, Function f) {
            return this;
        }

        @Override public boolean contains (Object o) {
            return false;
        }

        @Override public int size () {
            return 0;
        }

        @Override public boolean isEmpty () {
            return true;
        }
    };

}
