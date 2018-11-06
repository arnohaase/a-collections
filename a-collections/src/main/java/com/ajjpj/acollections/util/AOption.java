package com.ajjpj.acollections.util;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.immutable.AbstractImmutableCollection;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;

import java.io.Serializable;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;


/**
 * An {@code AOption} is a wrapper type, containing either one actual element or none. It represents a element that is either present or not,
 *  and it can be used instead of {@code null} in many cases.
 *
 * <p> AOption exists although Java's standard library contains {@link java.util.Optional}. The reason is that {@code Optional} has a very
 *  limited API and is intended for a very limited range of use cases, whereas {@code AOption} is designed for wide-spread use (e.g. as
 *  parameter type in APIs) and implements {@link ACollection}, giving it a rich API for working with its elements.
 *
 * <p> The following examples illustrate some typical uses of {@code AOption} that go beyond simple wrapping / unwrapping:
 * <ul>
 *     <li> Fetch the corresponding 'Person' object for an optional 'person ID' using {@link #map(Function)}:
 *         <p> {@code AOption<Long> personId = ...;}
 *         <p> {@code AOption<Person> = personId.map(this::fetchPerson);}
*      <li> Given a method returning the (optional) address for a person, resolve an optional person to their optional address using
 *          {@link #flatMap(Function)}:
 *         <p> {@code AOption<Address> addressFor(Person p) {...}}
 *         <p> {@code AOption<Person> person = ...;}
 *         <p> {@code AOption<Address> address = person.flatMap(this::addressFor);}
 *     <li> Given an optional order, send a confirmation email if there actually is an order using {@link #forEach(Consumer)}:
 *         <p> {@code AOption<Order> optOrder = ...;}
 *         <p> {@code optOrder.forEach(this::sendConfirmationEmail);}
 * </ul>
 *
 * Using {@link ACollection#flatMap(Function)} with a function returning an {@code AOption} is also useful to filter out those elements
 *  for which there is no result. The following code for example transforms a list of persons into a list of their addresses, leaving out
 *  those persons for which no address is available (using 'addressFor' from the above example):
 * <p> {@code AList<Person> persons = ...;}
 * <p> {@code AList<Address> addresses = persons.flatMap(this::addressFor);}
 *
 * <p><b>Null Handling:</b> AOption can hold {@code null} elements and distinguishes between {@code some(null)} and {@code none()}. This
 *  decision was made because it is a meaningful distinction in many situations:
 * <ul>
 *     <li> Some Map classes allow {@code null} elements, in which case there is a difference between 'no entry for a given key' and 'an element
 *           of {@code null} for that key'.
 *     <li> In a JSON object, there is a formal difference between a key with element null and that key not being present
 *     <li> ...
 * </ul>
 *
 * We are aware that there some controversy as to whether an 'option' type should allow null elements or not, and users of AOption may not
 *  want to use it that way. The above examples however are the reason why AOption allows elements of {@code null}.
 *
 * @param <T> the element type
 */
public abstract class AOption<T> extends AbstractImmutableCollection<T> implements ACollectionDefaults<T, AOption<T>>, Serializable {
    /**
     * Returns the contained element (if any), throwing a NoSuchElementException if this AOption is empty.
     * @throws NoSuchElementException if this AOption is empty
     * @return the contained element
     */
    public abstract T get();

    /**
     * Returns the contained element, or a default element if this AOption is empty.
     * @param defaultValue the default element that is returned if this AOption is empty
     * @return the contained element, or a default element if this AOption is empty
     */
    public abstract T orElse(T defaultValue);

    /**
     * Returns the contained element, or the result of calling the {@link Supplier} passed in as a parameter. The supplier is called only
     *  if this AOption is empty.
     * @param f the supplier providing a default element if this AOption is empty.
     * @return the contained element, or the result of calling the {@link Supplier} passed in as a parameter if this AOption is empty.
     */
    public abstract T orElseGet(Supplier<T> f);

    /**
     * Returns the contained element, or null if this AOption is empty.
     * @return the contained element, or null if this AOption is empty.
     */
    public abstract T orNull();

    /**
     * Returns the contained element, or throws an exception provided by the {@link Supplier} passed in as a parameter. The supplier is called
     *  only if this AOption is empty.
     * @param exceptionSupplier is called if this AOption is empty, providing the exception that is then thrown.
     * @param <X>               the exception's type that is thrown if this AOption is empty
     * @return the contained element, if any
     * @throws X if this AOption is empty
     */
    public abstract <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X;

    /**
     * Returns the corresponding {@link Optional} for compatibility with other libraries
     * @return the corresponding {@link Optional}
     */
    public abstract Optional<T> toOptional();

    /**
     * Creates a new AOption for a given element, returning {@code some(o)} for non-null elements, and {@code none()} if a null element is passed
     *  in.
     * @param o   the element for which an AOption is created
     * @param <T> the element's type
     * @return {@code some(o)} for non-null elements, and {@code none()} if null is passed in
     */
    public static <T> AOption<T> of(T o) {
        if (o != null) return some(o);
        else return none();
    }

    /**
     * Wraps a given element in an AOption, even if the element is {@code null}. This method never returns an empty AOption.
     * @param o   the element that is wrapped in an AOption
     * @param <T> the AOption's element type
     * @return an AOption containing the element that was passed in.
     */
    public static <T> AOption<T> some(T o) {
        return new ASome<>(o);
    }

    /**
     * Returns the empty AOption.
     * @param <T> The result's element type
     * @return the empty AOption
     */
    public static <T> AOption<T> none() {
        //noinspection unchecked
        return (AOption<T>) ANone.INSTANCE;
    }

    @Override public String toString () {
        return ACollectionSupport.toString(AOption.class, this);
    }

    /**
     * Returns true if and only if this AOption holds a element.
     * @return true if and only if this AOption holds a element.
     */
    public boolean isDefined() {
        return nonEmpty();
    }

    /**
     * Returns this AOption itself because that matches the inherited semantics of {@link ACollectionOps#headOption()}.
     * @return this AOption itself
     */
    @Override public AOption<T> headOption() {
        return this;
    }

    /**
     * Applies a function to this AOption's element (if any), wrapping it in a new AOption.
     * @param f the function that is applied to the element
     * @param <U> the result's element type
     * @return an AOption containing the result of applying a function to this AOption's element.
     */
    @Override public abstract <U> AOption<U> map (Function<T,U> f);

    /**
     * If called on an {@link AOption#none()}, this method returns {@link AOption#none()}. If this AOption contains an element, flatMap
     *  calls the collection-valued function that is passed in on that element.
     * <ul>
     *     <li> If the result is empty, flatMap() returns {@link AOption#none()}.
     *     <li> If the result contains exactly one element, flatMap() wraps that element in {@link AOption#some(Object)}.
     *     <li> If the result contains more than one element, an IllegalArgumentException is thrown.
     * </ul>
     *
     * @param f the function providing new element(s) for each element
     * @param <U> the result's element type
     * @return an AOption containing the function call's result only element
     */
    @Override public <U> AOption<U> flatMap (Function<T, Iterable<U>> f) {
        //TODO optimized implementation
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }

    @Override public abstract <U> AOption<U> collect (Predicate<T> filter, Function<T, U> f);

    /**
     * Uses a predicate fo filter this AOption's element (if any), turning an {@link AOption#some(Object)} into {@link AOption#none()} if
     *  the element does not pass the filter.
     * @param f the predicate used as a filter
     * @return the filtered AOption
     */
    @Override public abstract AOption<T> filter (Predicate<T> f);

    /**
     * Uses a predicate fo filter this AOption's element (if any), turning an {@link AOption#some(Object)} into {@link AOption#none()} if
     *  the element <b>passes</b> the filter.
     * @param f the predicate used as a filter
     * @return the filtered AOption
     */
    @Override public AOption<T> filterNot (Predicate<T> f) {
        return ACollectionDefaults.super.filterNot(f);
    }

    @Override public <K> AMap<K, AOption<T>> groupBy (Function<T, K> keyExtractor) {
        return ACollectionDefaults.super.groupBy(keyExtractor);
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
                if (result.nonEmpty()) throw new IllegalArgumentException("an AOption can hold at most one element");
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

    private static class ANone extends AOption<Object> {
        static final ANone INSTANCE = new ANone();

        private Object readResolve() {
            return INSTANCE;
        }

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
