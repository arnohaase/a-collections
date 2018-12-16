package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.ASetSupport;
import com.ajjpj.acollections.util.AOption;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;


/**
 * This class wraps any {@link java.util.NavigableSet} as an {@link ASortedSet}. All modifying operations - both those from
 *  {@link java.util.NavigableSet} and those added by {@link ASortedSet} - write through to the wrapped set.
 *
 * <p> This is a simple way to start using a-collections: Wrap an existing {@link java.util.NavigableSet} to add a rich API while maintaining
 *  100% backwards compatibility: operations on the wrapper are write-through, i.e. all changes are applied to the underlying
 *  {@code NavigableSet}. This class makes it simple to use {@link ASortedSet}'s rich API on any list in an ad-hoc fashion.
 *
 * <p> The wrapped set with all modifications applied to it can always be retrieved by calling {@link #getInner()}, though there is
 *  usually no reason for unwrapping: {@code AMutableSetWrapper} implements {@link java.util.NavigableSet}, so any method accepting
 *  {@link java.util.NavigableSet} will also accept an {@code AMutableSortedSetWrapper} as is.
 *
 * @param <T> the set's element type.
 */
public class AMutableSortedSetWrapper<T> implements ASortedSet<T>, ACollectionDefaults<T, AMutableSortedSetWrapper<T>>, Serializable {

    private final NavigableSet<T> inner;

    /**
     * This factory method wraps an arbitrary (typically mutable) {@link java.util.NavigableSet} in an {@link AMutableSortedSetWrapper}.
     *  This is a simple way to start using a-collections: Wrap an existing {@code SortedSet} to add a rich API while maintaining 100% backwards
     *  compatibility: operations on the wrapper are write-through, i.e. all changes are applied to the underlying {@code SortedSet}.
     *
     * @param inner the SortedSet being wrapped
     * @param <T> the SortedSet's element type
     * @return the wrapped Set
     */
    public static<T> AMutableSortedSetWrapper<T> wrap(NavigableSet<T> inner) {
        if (inner instanceof AMutableSortedSetWrapper) return (AMutableSortedSetWrapper<T>) inner;
        return new AMutableSortedSetWrapper<>(inner);
    }

    /**
     * Creates a new set based on an iterator's elements based on a {@link Comparator} passed in.
     *
     * @param it the iterator from which the new set is initialized
     * @param comparator the new set's comparator
     * @param <T> the set's element type
     * @return the new set
     */
    public static <T> AMutableSortedSetWrapper<T> fromIterator(Iterator<T> it, Comparator<T> comparator) {
        return AMutableSortedSetWrapper.<T>builder(comparator).addAll(it).build();
    }

    /**
     * Creates a new set based on an iterator's elements assuming {@link Comparator#naturalOrder()}.
     *
     * @param it the iterator from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    public static <T extends Comparable<T>> AMutableSortedSetWrapper<T> fromIterator(Iterator<T> it) {
        return AMutableSortedSetWrapper.<T>builder(Comparator.naturalOrder()).addAll(it).build();
    }

    /**
     * Creates a new set based on an array's elements based on a {@link Comparator} passed in.
     *
     * @param that the array from which the new set is initialized
     * @param comparator the new set's comparator
     * @param <T> the set's element type
     * @return the new set
     */
    public static <T> AMutableSortedSetWrapper<T> from(T[] that, Comparator<T> comparator) {
        return fromIterator(Arrays.asList(that).iterator(), comparator);
    }

    /**
     * Creates a new set based on an array's elements assuming {@link Comparator#naturalOrder()}.
     *
     * @param that the array from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    public static <T extends Comparable<T>> AMutableSortedSetWrapper<T> from(T[] that) {
        return fromIterator(Arrays.asList(that).iterator());
    }

    /**
     * Creates a new set based on an Iterable's elements using a {@link Comparator} passed in.
     *
     * @param that the Iterable from which the new set is initialized
     * @param comparator the new set's comparator
     * @param <T> the set's element type
     * @return the new set
     */
    public static <T> AMutableSortedSetWrapper<T> from(Iterable<T> that, Comparator<T> comparator) {
        return fromIterator(that.iterator(), comparator);
    }

    /**
     * Creates a new set based on an Iterable's elements assuming {@link Comparator#naturalOrder()}.
     *
     * @param that the Iterable from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    public static <T extends Comparable<T>> AMutableSortedSetWrapper<T> from(Iterable<T> that) {
        return fromIterator(that.iterator());
    }

    /**
     * Convenience method for creating an empty set. For creating a set with known elements, calling one of the {@code of}
     *  factory methods is a more concise alternative.
     *
     * @param <T> the new set's element type
     * @return an empty set
     */
    public static <T extends Comparable<T>> AMutableSortedSetWrapper<T> empty() {
        return AMutableSortedSetWrapper.<T>builder(Comparator.naturalOrder()).build();
    }

    /**
     * Creates an empty {@link AMutableSortedSetWrapper} with a given {@link Comparator}. For creating a set
     *  with known elements, calling one of the {@code of} factory methods is a more concise alternative.
     *
     * @param comparator the new set's comparator
     * @param <T> the new set's element type
     * @return an empty {@link AMutableSortedSetWrapper}
     */
    public static <T> AMutableSortedSetWrapper<T> empty(Comparator<? super T> comparator) {
        return AMutableSortedSetWrapper.<T>builder(comparator).build();
    }

    /**
     * This is an alias for {@link #empty()} for consistency with Java 9 conventions - it creates an empty set.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> set instance - that is the
     *  whole point of class {@link AMutableSortedSetWrapper}. If you want immutable sets, use {@link ASet#of()}.
     *
     * @param <T> the new set's element type
     * @return an empty set
     */
    public static <T extends Comparable<T>> AMutableSortedSetWrapper<T> of() {
        return empty();
    }

    /**
     * Convenience factory method creating a set with exactly one element.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> set instance - that is the
     *  whole point of class {@link AMutableSortedSetWrapper}. If you want immutable lists, use {@link ASet#of()}.
     *
     * @param o the single element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new set
     */
    public static <T extends Comparable<T>> AMutableSortedSetWrapper<T> of(T o) {
        return AMutableSortedSetWrapper.<T>builder(Comparator.naturalOrder()).add(o).build();
    }

    /**
     * Convenience factory method creating a set with two elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> set instance - that is the
     *  whole point of class {@link AMutableSortedSetWrapper}. If you want immutable sets, use {@link ASet#of()}.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new set
     */
    public static <T extends Comparable<T>> AMutableSortedSetWrapper<T> of(T o1, T o2) {
        return AMutableSortedSetWrapper.<T>builder(Comparator.naturalOrder()).add(o1).add(o2).build();
    }

    /**
     * Convenience factory method creating a set with three elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> set instance - that is the
     *  whole point of class {@link AMutableSortedSetWrapper}. If you want immutable set, use {@link ASet#of()}.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param o3 the third element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new set
     */
    public static <T extends Comparable<T>> AMutableSortedSetWrapper<T> of(T o1, T o2, T o3) {
        return AMutableSortedSetWrapper.<T>builder(Comparator.naturalOrder()).add(o1).add(o2).add(o3).build();
    }

    /**
     * Convenience factory method creating a set with four elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> set instance - that is the
     *  whole point of class {@link AMutableSortedSetWrapper}. If you want immutable sets, use {@link ASet#of()}.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param o3 the third element for the new set
     * @param o4 the fourth element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new set
     */
    public static <T extends Comparable<T>> AMutableSortedSetWrapper<T> of(T o1, T o2, T o3, T o4) {
        return AMutableSortedSetWrapper.<T>builder(Comparator.naturalOrder()).add(o1).add(o2).add(o3).add(o4).build();
    }

    /**
     * Convenience factory method creating a set with more than four elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> set instance - that is the
     *  whole point of class {@link AMutableSortedSetWrapper}. If you want immutable sets, use {@link ASet#of()}.
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
    @SafeVarargs public static <T extends Comparable<T>> AMutableSortedSetWrapper<T> of(T o1, T o2, T o3, T o4, T o5, T... others) {
        return AMutableSortedSetWrapper
                .<T>builder(Comparator.naturalOrder())
                .add(o1)
                .add(o2)
                .add(o3)
                .add(o4)
                .add(o5)
                .addAll(others)
                .build();
    }

    private AMutableSortedSetWrapper (NavigableSet<T> inner) {
        this.inner = inner;
    }

    @Override public Comparator<? super T> comparator() {
        return inner.comparator();
    }

    @Override public AMutableSortedSetWrapper<T> plus(T o) {
        inner.add(o);
        return this;
    }

    @Override public AMutableSortedSetWrapper<T> plusAll(Iterable<? extends T> that) {
        for (T t : that) {
            inner.add(t);
        }
        return this;
    }

    @Override public AMutableSortedSetWrapper<T> minus(T o) {
        inner.remove(o);
        return this;
    }

    @Override public AMutableSortedSetWrapper<T> union(Iterable<? extends T> that) {
        return plusAll(that);
    }

    @Override public AMutableSortedSetWrapper<T> intersect(Set<T> that) {
        retainAll(that);
        return this;
    }

    @Override public AMutableSortedSetWrapper<T> diff(Set<T> that) {
        removeAll(that);
        return this;
    }

    @Override public int countInRange(AOption<T> from, AOption<T> to) {
        return countInRange(from, true, to, false);
    }

    @Override public int countInRange(AOption<T> from, boolean fromInclusive, AOption<T> to, boolean toInclusive) {
        return range(from, fromInclusive, to,toInclusive ).size();
    }

    @Override public AMutableSortedSetWrapper<T> drop(int n) {
        if (inner.size() <= n) {
            inner.clear();
        }
        else {
            for (int i = 0; i<n; i++){
                inner.remove(inner.first());
            }
        }
        return this;
    }

    @Override public AMutableSortedSetWrapper<T> take(int n) {
        if (n >= size()) return this;

        Iterator<T> iterator = inner.iterator();
        for (int i = 0; i<n; i++){
            iterator.next();
        }
        while(iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        return this;
    }

    @Override public AOption<T> smallest() {
        if (inner.isEmpty()){
            return AOption.none();
        }
        return AOption.some(inner.first());
    }

    @Override public AOption<T> greatest() {
        if (inner.isEmpty()){
            return AOption.none();
        }
        return AOption.some(inner.last());
    }

    @Override public AIterator<T> iterator(AOption<T> from, boolean fromInclusive, AOption<T> to, boolean toInclusive) {
        return range(from,fromInclusive, to, toInclusive).iterator();
    }

    @Override public AIterator<T> reverseIterator() {
        return AIterator.wrap(inner.descendingIterator());
    }

    @Override public AIterator<? extends ASortedSet<T>> subsets () {
        //noinspection unchecked
        return ASetSupport.subsets(this, () -> ((ACollectionBuilder) newBuilder()));
    }

    @Override public AIterator<? extends ASortedSet<T>> subsets (int len) {
        //noinspection unchecked
        return ASetSupport.subsets(len, this, () -> ((ACollectionBuilder) newBuilder()));
    }

    @Override public T first() {
        return inner.first();
    }

    @Override public T last() {
        return inner.last();
    }

    @Override public AMutableSortedSetWrapper<T> subSet(T fromElement, T toElement) {
        return subSet(fromElement, true, toElement, false);
    }

    @Override public AMutableSortedSetWrapper<T> headSet(T toElement) {
        return headSet(toElement, false);
    }

    @Override public AMutableSortedSetWrapper<T> tailSet(T fromElement) {
        return tailSet(fromElement, true);
    }

    @Override public AMutableSortedSetWrapper<T> headSet(T toElement, boolean inclusive) {
        return new AMutableSortedSetWrapper<>(inner.headSet(toElement, inclusive));
    }

    @Override public AMutableSortedSetWrapper<T> tailSet(T fromElement, boolean fromInclusive) {
        return new AMutableSortedSetWrapper<>(inner.tailSet(fromElement, fromInclusive));
    }

    @Override public AMutableSortedSetWrapper<T> range(AOption<T> from, boolean fromInclusive, AOption<T> to, boolean toInclusive) {
        if (!from.isDefined() && !to.isDefined()) return this;
        if (!from.isDefined()) return headSet(to.get(), toInclusive);
        if (!to.isDefined()) return tailSet(from.get(), fromInclusive);
        return subSet(from.get(), fromInclusive, to.get(), toInclusive);
    }

    @Override public AMutableSortedSetWrapper<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
        if (comparator().compare(fromElement, toElement) > 0) return AMutableSortedSetWrapper.<T>empty(comparator());
        NavigableSet<T> rangeView = inner.subSet(fromElement, fromInclusive, toElement, toInclusive);
        return new AMutableSortedSetWrapper<>(rangeView);
    }

    @Override public AMutableSortedSetWrapper<T> descendingSet() {
        return new AMutableSortedSetWrapper<>(inner.descendingSet());
    }

    @Override public AIterator<T> descendingIterator() {
        return AIterator.wrap(inner.descendingIterator());
    }

    @Override public <U> AMutableSortedSetWrapper.Builder<U> newBuilder() {
        //TODO this is somewhat hacky - but is there a better meaningful way to do this?
        //noinspection unchecked
        return AMutableSortedSetWrapper.builder((Comparator) comparator());
    }

    @Override public AIterator<T> iterator() {
        return AIterator.wrap(inner.iterator());
    }

    @Override public int size() {
        return inner.size();
    }

    @Override public boolean isEmpty() {
        return inner.isEmpty();
    }

    @Override public <U> AMutableSortedSetWrapper<U> map(Function<T, U> f) {
        return ACollectionSupport.map(newBuilder(), this, f);
    }

    @Override public <U> AMutableSortedSetWrapper<U> flatMap(Function<T, Iterable<U>> f) {
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }

    @Override public <U> AMutableSortedSetWrapper<U> collect(Predicate<T> filter, Function<T, U> f) {
        return ACollectionSupport.collect(newBuilder(), this, filter, f);
    }

    @Override public <K1> AMap<K1, AMutableSortedSetWrapper<T>> groupBy (Function<T, K1> keyExtractor) {
        return ACollectionDefaults.super.groupBy(keyExtractor);
    }

    @Override public AMutableSortedSetWrapper<T> filter (Predicate<T> f) {
        return ACollectionDefaults.super.filter(f);
    }

    @Override public AMutableSortedSetWrapper<T> filterNot (Predicate<T> f) {
        return ACollectionDefaults.super.filterNot(f);
    }

    @Override public boolean contains(Object o) {
        return inner.contains(o);
    }

    @Override public T lower(T t) {
        return inner.lower(t);
    }

    @Override public T floor(T t) {
        return inner.floor(t);
    }

    @Override public T ceiling(T t) {
        return inner.ceiling(t);
    }

    @Override public T higher(T t) {
        return inner.higher(t);
    }

    @Override public T pollFirst() {
        return inner.pollFirst();
    }

    @Override public T pollLast() {
        return inner.pollLast();
    }

    @Override public Object[] toArray () {
        return inner.toArray();
    }

    @Override public <T1> T1[] toArray (T1[] a) {
        //noinspection SuspiciousToArrayCall
        return inner.toArray(a);
    }

    @Override public boolean add(T t) {
        return inner.add(t);
    }

    @Override public boolean remove(Object o) {
        return inner.remove(o);
    }

    @Override public boolean containsAll(Collection<?> c) {
        return inner.containsAll(c);
    }

    @Override public boolean addAll(Collection<? extends T> c) {
        return inner.addAll(c);
    }

    @Override public boolean removeAll(Collection<?> c) {
        return inner.removeAll(c);
    }

    @Override public boolean retainAll(Collection<?> c) {
        return inner.retainAll(c);
    }

    @Override public void clear() {
        inner.clear();
    }

    @Override public int hashCode() {
        return inner.hashCode();
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override public boolean equals (Object obj) {
        return inner.equals(obj);
    }

    @Override public String toString () {
        return getClass().getSimpleName() + ":" + inner;
    }

    /**
     * Returns the wrapped {@link NavigableSet} to which all modifications were applied.
     *
     * NB: AMutableSortedSetWrapper implements {@link java.util.NavigableSet}, so usually there is no reason to unwrap it. Any API accepting
     *  {@link java.util.NavigableSet} accepts an {@link AMutableSortedSetWrapper} as is.
     *
     * @return the wrapped NavigableSet
     */
    public NavigableSet<T> getInner() {
        return inner;
    }

    /**
     * Returns a {@link Collector} to collect {@link java.util.stream.Stream} elements into an AMutableSetWrapper.
     *
     * @param <T> the stream's element type
     * @param comparator the comparator used for the underlying wrapped sorted set
     * @return a {@link Collector} to collect a stream's elements into an AMutableSetWrapper
     */
    public static <T> Collector<T, AMutableSortedSetWrapper.Builder<T>, AMutableSortedSetWrapper<T>> streamCollector(Comparator<? super T> comparator) {
        final Supplier<AMutableSortedSetWrapper.Builder<T>> supplier = () -> AMutableSortedSetWrapper.builder(comparator);
        final BiConsumer<AMutableSortedSetWrapper.Builder<T>, T> accumulator = AMutableSortedSetWrapper.Builder::add;
        final BinaryOperator<AMutableSortedSetWrapper.Builder<T>> combiner = (b1, b2) -> {
            b1.addAll(b2.build());
            return b1;
        };
        final Function<AMutableSortedSetWrapper.Builder<T>, AMutableSortedSetWrapper<T>> finisher = AMutableSortedSetWrapper.Builder::build;

        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Returns a new {@link ACollectionBuilder} for building an AMutableSortedSetWrapper efficiently and in a generic manner.
     *
     * @param <U> the builder's element type
     * @return an new {@link ACollectionBuilder}
     */
    public static <U> AMutableSortedSetWrapper.Builder<U> builder(Comparator<? super U> comparator) {
        return new AMutableSortedSetWrapper.Builder<>(comparator);
    }

    public static class Builder<T> implements ACollectionBuilder<T, AMutableSortedSetWrapper<T>> {
        private final NavigableSet<T> result;

        Builder(Comparator<? super T> comparator){
            result = new TreeSet<>(comparator);
        }

        @Override public ACollectionBuilder<T, AMutableSortedSetWrapper<T>> add (T el) {
            result.add(el);
            return this;
        }

        @Override public AMutableSortedSetWrapper<T> build () {
            return AMutableSortedSetWrapper.wrap(result);
        }
    }



}
