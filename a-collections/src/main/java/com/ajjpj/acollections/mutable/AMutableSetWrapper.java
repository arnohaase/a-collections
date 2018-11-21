package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.ASetDefaults;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;


/**
 * This class wraps any {@link java.util.Set} as an {@link ASet}. All modifying operations - both those from {@link java.util.Set} and
 *  those added by {@link ASet} - write through to the wrapped set.
 *
 * <p> This is a simple way to start using a-collections: Wrap an existing {@link java.util.Set} to add a rich API while maintaining
 *  100% backwards compatibility: operations on the wrapper are write-through, i.e. all changes are applied to the underlying {@code Set}.
 *  This class makes it simple to use {@link ASet}'s rich API on any list in an ad-hoc fashion.
 *
 * <p> The wrapped set with all modifications applied to it can always be retrieved by calling {@link #getInner()}, though there is
 *  usually no reason for unwrapping: {@code AMutableSetWrapper} implements {@link java.util.Set}, so any method accepting
 *  {@link java.util.Set} will also accept an {@code AMutableSetWrapper} as is.
 *
 * @param <T> the set's element type.
 */
public class AMutableSetWrapper<T> implements ASet<T>, ACollectionDefaults<T, AMutableSetWrapper<T>>, ASetDefaults<T, AMutableSetWrapper<T>>, Serializable {
    private final Set<T> inner;

    /**
     * This factory method wraps an arbitrary (typically mutable) {@link java.util.Set} in an {@link AMutableSetWrapper}.
     *  This is a simple way to start using a-collections: Wrap an existing {@code Set} to add a rich API while maintaining 100% backwards
     *  compatibility: operations on the wrapper are write-through, i.e. all changes are applied to the underlying {@code Set}.
     *
     * @param inner the Set being wrapped
     * @param <T> the Set's element type
     * @return the wrapped Set
     */
    public static<T> AMutableSetWrapper<T> wrap(Set<T> inner) {
        if (inner instanceof AMutableSetWrapper) return (AMutableSetWrapper<T>) inner;
        return new AMutableSetWrapper<>(inner);
    }

    /**
     * Creates a new set based on an iterator's elements.
     *
     * @param it the iterator from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    public static <T> AMutableSetWrapper<T> fromIterator(Iterator<T> it) {
        return AMutableSetWrapper.<T>builder().addAll(it).build();
    }

    /**
     * Creates a new set based on an array's elements.
     *
     * @param that the array from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    public static <T> AMutableSetWrapper<T> from(T[] that) {
        return fromIterator(Arrays.asList(that).iterator());
    }

    /**
     * Creates a new set based on an Iterable's elements.
     *
     * @param that the Iterable from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    public static <T> AMutableSetWrapper<T> from(Iterable<T> that) {
        return fromIterator(that.iterator());
    }

    /**
     * Convenience method for creating an empty set. For creating a set with known elements, calling one of the {@code of}
     *  factory methods is a more concise alternative.
     *
     * @param <T> the new set's element type
     * @return an empty set
     */
    public static <T> AMutableSetWrapper<T> empty() {
        return new AMutableSetWrapper<>(new HashSet<>());
    }

    /**
     * This is an alias for {@link #empty()} for consistency with Java 9 conventions - it creates an empty set.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> set instance - that is the
     *  whole point of class {@link AMutableSetWrapper}. If you want immutable sets, use {@link ASet#of()}.
     *
     * @param <T> the new set's element type
     * @return an empty set
     */
    public static <T> AMutableSetWrapper<T> of() {
        return empty();
    }

    /**
     * Convenience factory method creating a set with exactly one element.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> set instance - that is the
     *  whole point of class {@link AMutableSetWrapper}. If you want immutable lists, use {@link ASet#of()}.
     *
     * @param o the single element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new set
     */
    public static <T> AMutableSetWrapper<T> of(T o) {
        return AMutableSetWrapper.<T>builder().add(o).build();
    }

    /**
     * Convenience factory method creating a set with two elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> set instance - that is the
     *  whole point of class {@link AMutableSetWrapper}. If you want immutable sets, use {@link ASet#of()}.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new set
     */
    public static <T> AMutableSetWrapper<T> of(T o1, T o2) {
        return AMutableSetWrapper.<T>builder().add(o1).add(o2).build();
    }

    /**
     * Convenience factory method creating a set with three elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> set instance - that is the
     *  whole point of class {@link AMutableSetWrapper}. If you want immutable set, use {@link ASet#of()}.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param o3 the third element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new set
     */
    public static <T> AMutableSetWrapper<T> of(T o1, T o2, T o3) {
        return AMutableSetWrapper.<T>builder().add(o1).add(o2).add(o3).build();
    }

    /**
     * Convenience factory method creating a set with four elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> set instance - that is the
     *  whole point of class {@link AMutableListWrapper}. If you want immutable sets, use {@link ASet#of()}.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param o3 the third element for the new set
     * @param o4 the fourth element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new set
     */
    public static <T> AMutableSetWrapper<T> of(T o1, T o2, T o3, T o4) {
        return AMutableSetWrapper.<T>builder().add(o1).add(o2).add(o3).add(o4).build();
    }

    /**
     * Convenience factory method creating a set with more than four elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> set instance - that is the
     *  whole point of class {@link AMutableSetWrapper}. If you want immutable sets, use {@link ASet#of()}.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param o3 the third element for the new set
     * @param o4 the fourth element for the new set
     * @param o5 the fifth element for the new set
     * @param others the (variable number of) additional elements
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new set
     */
    @SafeVarargs public static <T> AMutableSetWrapper<T> of(T o1, T o2, T o3, T o4, T o5, T... others) {
        return AMutableSetWrapper
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .add(o4)
                .add(o5)
                .addAll(others)
                .build();
    }

    private AMutableSetWrapper (Set<T> inner) {
        this.inner = inner;
    }

    /**
     * Returns the wrapped Set to which all modifications were applied.
     *
     * NB: AMutableSetWrapper implements {@link java.util.Set}, so usually there is no reason to unwrap it. Any API accepting
     *  {@link java.util.Set} accepts an {@link AMutableSetWrapper} as is.
     *
     * @return the wrapped Set
     */
    public Set<T> getInner() {
        return inner;
    }

    @Override public AMutableSetWrapper<T> toMutableSet () {
        return this;
    }

    @Override public ASet<T> plus (T o) {
        inner.add(o);
        return this;
    }

    @Override public AMutableSetWrapper<T> minus (T o) {
        inner.remove(o);
        return this;
    }

    @Override public AMutableSetWrapper<T> union (Iterable<? extends T> that) {
        for (T o: that) inner.add(o);
        return this;
    }

    @Override public AMutableSetWrapper<T> intersect (Set<T> that) {
        inner.retainAll(that);
        return this;
    }

    @Override public AMutableSetWrapper<T> diff (Set<T> that) {
        inner.removeAll(that);
        return this;
    }

    @Override public AIterator<T> iterator () {
        return AIterator.wrap(inner.iterator());
    }

    @Override public <U> ACollectionBuilder<U, ? extends ASet<U>> newBuilder () {
        return builder();
    }

    @Override public boolean isEmpty () {
        return inner.isEmpty();
    }

    @Override public <U> ACollection<U> map (Function<T, U> f) {
        return ACollectionSupport.map(newBuilder(), this, f);
    }

    @Override public <U> ACollection<U> flatMap (Function<T, Iterable<U>> f) {
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }

    @Override public <U> ACollection<U> collect (Predicate<T> filter, Function<T, U> f) {
        return ACollectionSupport.collect(newBuilder(), this, filter, f);
    }

    @Override public AMutableSetWrapper<T> filter (Predicate<T> f) {
        return ACollectionDefaults.super.filter(f);
    }

    @Override public AMutableSetWrapper<T> filterNot (Predicate<T> f) {
        return ACollectionDefaults.super.filterNot(f);
    }

    @Override public <K> AMap<K, AMutableSetWrapper<T>> groupBy (Function<T, K> keyExtractor) {
        return ACollectionDefaults.super.groupBy(keyExtractor);
    }

    @Override public int size () {
        return inner.size();
    }

    @Override public Object[] toArray () {
        return inner.toArray();
    }

    @Override public <T1> T1[] toArray (T1[] a) {
        return inner.toArray(a);
    }

    @Override public boolean add (T t) {
        return inner.add(t);
    }

    @Override public boolean remove (Object o) {
        return inner.remove(o);
    }

    @Override public boolean contains (Object o) {
        return inner.contains(o);
    }

    @Override public boolean containsAll (Collection<?> c) {
        return inner.containsAll(c);
    }

    @Override public boolean addAll (Collection<? extends T> c) {
        return inner.addAll(c);
    }

    @Override public boolean removeAll (Collection<?> c) {
        return inner.removeAll(c);
    }

    @Override public boolean retainAll (Collection<?> c) {
        return inner.retainAll(c);
    }

    @Override public void clear () {
        inner.clear();
    }

    @Override public Spliterator<T> spliterator () {
        return inner.spliterator();
    }
    @Override public boolean removeIf (Predicate<? super T> filter) {
        return inner.removeIf(filter);
    }
    @Override public Stream<T> stream () {
        return inner.stream();
    }
    @Override public Stream<T> parallelStream () {
        return inner.parallelStream();
    }
    @Override public void forEach (Consumer<? super T> action) {
        inner.forEach(action);
    }

    @Override public String toString () {
        return getClass().getSimpleName() + ":" + inner;
    }

    @Override public boolean equals (Object obj) {
        return inner.equals(obj);
    }

    @Override public int hashCode () {
        return inner.hashCode();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> implements ACollectionBuilder<T, AMutableSetWrapper<T>> {
        private Set<T> result = new HashSet<>();

        @Override public ACollectionBuilder<T, AMutableSetWrapper<T>> add (T el) {
            result.add(el);
            return this;
        }

        @Override public AMutableSetWrapper<T> build () {
            return AMutableSetWrapper.wrap(result);
        }
    }
}
