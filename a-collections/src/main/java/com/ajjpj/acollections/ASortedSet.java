package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.ATreeSet;
import com.ajjpj.acollections.util.AOption;

import java.util.*;


/**
 * This interface represents a {@link java.util.NavigableSet} with additional API (mostly inherited from {@link ASet}). Every ASortedSet
 *  has in inherent {@link Comparator} defining an ordering (and equality); this is exposed via {@link #comparator()}. ASortedSet has some
 *  methods in addition to {@link ASet} which are based on the ordering defined by the comparator.
 *
 * @param <T> the set's element type
 */
public interface ASortedSet<T> extends ASet<T>, NavigableSet<T> {

    /**
     * Creates an empty {@link ATreeSet} with {@link Comparator#naturalOrder()}.
     * <p> This can later be modified by calling {@link #plus(Object)} or {@link #minus(Object)}. For creating a set with known elements,
     *  calling one of the {@code of} factory methods is usually more concise.
     *
     * @param <T> the new set's element type
     * @return an empty {@link ATreeSet}
     */
    static <T extends Comparable<T>> ATreeSet<T> empty() {
        return ATreeSet.empty();
    }

    /**
     * Creates an empty {@link ATreeSet} with a given {@link Comparator}.
     * <p> This can later be modified by calling {@link #plus(Object)} or {@link #minus(Object)}. For creating a set with known elements,
     *  calling one of the {@code of} factory methods is usually more concise.
     *
     * @param comparator the new set's comparator
     * @param <T> the new set's element type
     * @return an empty {@link ATreeSet}
     */
    static <T> ATreeSet<T> empty(Comparator<? super T> comparator) {
        return ATreeSet.empty(comparator);
    }

    /**
     * This is an alias for {@link #empty()} for consistency with Java 9 conventions - it creates an empty {@link ATreeSet} with
     *  {@link Comparator#naturalOrder()}.
     *
     * @param <T> the new set's element type
     * @return an empty {@link ATreeSet}
     */
    static <T extends Comparable<T>> ATreeSet<T> of() {
        return ATreeSet.of();
    }

    /**
     * Convenience factory method creating an {@link ATreeSet} with exactly one element and {@link Comparator#naturalOrder()}.
     *
     * @param o the single element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link ATreeSet}
     */
    static <T extends Comparable<T>> ATreeSet<T> of(T o) {
        return ATreeSet.of(o);
    }

    /**
     * Convenience factory method creating an {@link ATreeSet} with exactly two elements and {@link Comparator#naturalOrder()}.
     *
     * @param o1 the first element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param o2 the second element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link ATreeSet}
     */
    static <T extends Comparable<T>> ATreeSet<T> of(T o1, T o2) {
        return ATreeSet.of(o1, o2);
    }

    /**
     * Convenience factory method creating an {@link ATreeSet} with exactly three elements and {@link Comparator#naturalOrder()}.
     *
     * @param o1 the first element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param o2 the second element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param o3 the third element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link ATreeSet}
     */
    static <T extends Comparable<T>> ATreeSet<T> of(T o1, T o2, T o3) {
        return ATreeSet.of(o1, o2, o3);
    }

    /**
     * Convenience factory method creating an {@link ATreeSet} with exactly four elements and {@link Comparator#naturalOrder()}.
     *
     * @param o1 the first element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param o2 the second element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param o3 the third element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param o4 the fourth element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link ATreeSet}
     */
    static <T extends Comparable<T>> ATreeSet<T> of(T o1, T o2, T o3, T o4) {
        return ATreeSet.of(o1, o2, o3, o4);
    }

    /**
     * Convenience factory method creating an {@link ATreeSet} with more than four elements and {@link Comparator#naturalOrder()}.
     *
     * @param o1 the first element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param o2 the second element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param o3 the third element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param o4 the fourth element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param o5 the fifth element for the new set. It must implement {@link Comparable} to work with {@link Comparator#naturalOrder()}.
     * @param others the (variable number of) additional elements
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link ATreeSet}
     */
    @SafeVarargs static <T extends Comparable<T>> ATreeSet<T> of(T o1, T o2, T o3, T o4, T o5, T... others) {
        return ATreeSet.of(o1, o2, o3, o4, o5, others);
    }

    /**
     * Creates a new {@link ATreeSet} based on an array's elements.
     *
     * @param that the array from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    static <T extends Comparable<T>> ATreeSet<T> from(T[] that) {
        return ATreeSet.from(that);
    }

    /**
     * Creates a new {@link ATreeSet} based on an array's elements with a given {@link Comparator}.
     *
     * @param that the array from which the new set is initialized
     * @param comparator the new set's comparator
     * @param <T> the set's element type
     * @return the new set
     */
    static <T> ATreeSet<T> from(T[] that, Comparator<? super T> comparator) {
        return ATreeSet.from(that, comparator);
    }

    /**
     * Creates a new {@link ATreeSet} based on an {@link Iterable}'s elements.
     *
     * @param that the {@link Iterable} from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    static <T extends Comparable<T>> ATreeSet<T> from(Iterable<T> that) {
        return ATreeSet.from(that);
    }

    /**
     * Creates a new {@link ATreeSet} based on an {@link Iterable}'s elements with a given {@link Comparator}.
     *
     * @param that the {@link Iterable} from which the new set is initialized
     * @param comparator the new set's comparator
     * @param <T> the set's element type
     * @return the new set
     */
    static <T> ATreeSet<T> from(Iterable<T> that, Comparator<? super T> comparator) {
        return ATreeSet.from(that, comparator);
    }

    /**
     * Creates a new {@link ATreeSet} based on an {@link Iterator}'s elements.
     *
     * @param it the {@link Iterator} from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    static <T extends Comparable<T>> ATreeSet<T> fromIterator(Iterator<T> it) {
        return ATreeSet.fromIterator(it);
    }

    /**
     * Creates a new {@link ATreeSet} based on an {@link Iterator}'s elements with a given {@link Comparator}.
     *
     * @param it the {@link Iterator} from which the new set is initialized
     * @param comparator the new set's comparator
     * @param <T> the set's element type
     * @return the new set
     */
    static <T> ATreeSet<T> fromIterator(Iterator<T> it, Comparator<? super T> comparator) {
        return ATreeSet.fromIterator(it, comparator);
    }


    /**
     * Returns the comparator defining this set's ordering and equality.
     *
     * @return this set's comparator
     */
    Comparator<? super T> comparator();

    ASortedSet<T> plus (T o);
    ASortedSet<T> minus (T o);

    ASortedSet<T> union (Iterable<? extends T> that);
    ASortedSet<T> intersect (Set<T> that);
    ASortedSet<T> diff (Set<T> that);

    /**
     * Counts all the elements greater than or equal to a lower bound and less than an upper bound. Both bounds are optional.
     *
     * @param from the optional lower bound
     * @param to   the optional upper bound
     * @return the number of elements in the given range
     */
    default int countInRange (AOption<T> from, AOption<T> to) {
        return countInRange(from, true, to, false);
    }

    /**
     * Counts all the elements between a lower and upper bound. Flags explicitly control whether the bounds are inclusive or
     *  exclusive. Both bounds are optional.
     *
     * @param from          the optional lower bound
     * @param fromInclusive controls if the lower bound is inclusive or not
     * @param to            the optional upper bound
     * @param toInclusive   controls if the upper bound is inclusive or not
     * @return the number of elements in the given range
     */
    int countInRange (AOption<T> from, boolean fromInclusive, AOption<T> to, boolean toInclusive);

    /**
     * Returns a subset containing the elements between a lower and upper bound.
     *
     * <p> Flags control whether the bounds are inclusive or exclusive. Both bounds are optional.
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param from          the optional lower bound
     * @param fromInclusive controls whether the lower bound is inclusive or exclusive
     * @param to            the optional upper bound
     * @param toInclusive   controls whether the upper bound is inclusive or exclusive
     * @return the subset of elements between the two bounds
     */
    ASortedSet<T> range (AOption<T> from, boolean fromInclusive, AOption<T> to, boolean toInclusive);

    /**
     * Returns this set without the {@code n} smallest elements. Dropping a negative number of elements is valid and returns
     *  the original set. Dropping more than {@code this.size()} elements is valid and returns an empty set.
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param n the number of smallest elements to drop
     * @return this set without the {@code n} smallest elements
     */
    ASortedSet<T> drop (int n);

    /**
     * Returns a subset of this set containing the {@code n} smallest elements. Taking a negative number is valid and returns
     *  an empty set; taking more than {@code this.size()} elements is valid and returns the entire set.
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param n the number of smallest elements to take
     * @return a subset containing the {@code n} smallest elements.
     */
    ASortedSet<T> take (int n);

    /**
     * Returns this set's smallest element or {@link AOption#none()} if this set is empty.
     *
     * @return this set's smallest element
     */
    AOption<T> smallest();

    /**
     * Returns this set's greatest element or {@link AOption#none()} if this set is empty.
     *
     * @return this set's greatest element
     */
    AOption<T> greatest();

    /**
     * Returns an {@link AIterator} starting at a lower bound and ending at an upper bound.
     *
     * <p> Flags control whether the bounds are inclusive or exclusive, i.e. whether an element exactly equals to a bound
     *      is included in the returned iterator or not. Both bounds are optional.
     *
     * @param from          the lower bound for the iterator
     * @param fromInclusive controls whether the lower bound is inclusive or exclusive
     * @param to            the upper bound for the iterator
     * @param toInclusive   controls whether the upper bound is inclusive or exclusive
     * @return an iterator starting at a given lower bound
     */
    AIterator<T> iterator(AOption<T> from, boolean fromInclusive, AOption<T> to, boolean toInclusive);

    /**
     * Returns an {@link AIterator} iterating over this set in reverse order.
     *
     * @return an iterator in reverse element order
     */
    default AIterator<T> reverseIterator() {
        return descendingIterator();
    }

    AIterator<? extends ASortedSet<T>> subsets ();
    AIterator<? extends ASortedSet<T>> subsets (int len);

    /**
     * Returns the first element in iteration order (i.e. the smallest element).
     * @throws NoSuchElementException if this set is empty
     * @return the first / smallest element
     */
    @Override default T first () {
        return head();
    }

    /**
     * Returns the last element in iteration order (i.e. the greatest element).
     * @throws NoSuchElementException if this set is empty
     * @return the last / greatest element
     */
    @Override default T last () {
        return greatest().orElseThrow(NoSuchElementException::new);
    }

    @Override default ASortedSet<T> subSet (T fromElement, T toElement) {
        return range(AOption.some(fromElement), true, AOption.some(toElement), false);
    }

    @Override default ASortedSet<T> headSet (T toElement) {
        return range(AOption.none(), true, AOption.some(toElement), false);
    }

    @Override default ASortedSet<T> tailSet (T fromElement) {
        return range(AOption.some(fromElement), true, AOption.none(), false);
    }

    @Override ASortedSet<T> subSet(T fromElement, boolean fromInclusive, T toElement, boolean toInclusive);
    @Override ASortedSet<T> headSet(T toElement, boolean toInclusive);
    @Override ASortedSet<T> tailSet(T fromElement, boolean fromInclusive);

    @Override ASortedSet<T> descendingSet ();

    @Override AIterator<T> descendingIterator ();
}
