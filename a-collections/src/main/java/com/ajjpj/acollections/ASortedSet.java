package com.ajjpj.acollections;

import com.ajjpj.acollections.util.AOption;

import java.util.Comparator;
import java.util.Set;


/**
 * This interface represents a {@link java.util.SortedSet} with additional API (mostly inherited from {@link ASet}). Every ASortedSet
 *  has in inherent {@link Comparator} defining an ordering (and equality); this is exposed via {@link #comparator()}. ASortedSet has some
 *  methods in addition to {@link ASet} which are based on the ordering defined by the comparator.
 *
 * @param <T> the set's element type
 */
public interface ASortedSet<T> extends ASet<T> {
    /**
     * Returns the comparator defining this set's ordering and equality.
     *
     * @return this set's comparator
     */
    Comparator<T> comparator();

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
    int countInRange (AOption<T> from, AOption<T> to);

    /**
     * Returns a subset containing the elements greater than or equal to a lower bound and less than an upper bound. Both bounds are optional.
     * TODO exception, out of bounds, ...
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param from  the optional lower bound
     * @param until the optional upper bound
     * @return the subset of elements between the two bounds
     */
    ASortedSet<T> range (AOption<T> from, AOption<T> until);

    /**
     * Returns this set without the {@code n} smallest elements.
     * TODO exception, out of bounds, ...
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param n the number of smallest elements to drop
     * @return this set without the {@code n} smallest elements
     */
    ASortedSet<T> drop (int n);

    /**
     * Returns a subset of this set containing the {@code n} smallest elements.
     * TODO exception, out of bounds, ...
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param n the number of smallest elements to take
     * @return a subset containing the {@code n} smallest elements.
     */
    ASortedSet<T> take (int n);

    /**
     * Returns a subset of this set, dropping the first {@code from} elements and then taking the next {@code until - from} elements.
     *
     * TODO exception, out of bounds, ...
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param from  the first index to take
     * @param until the first index not to take
     * @return a subset of this set with elements in the index from from {@code from} until {@code until}.
     */
    ASortedSet<T> slice (int from, int until);

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
     * Returns an {@link AIterator} starting at a lower bound. More precisely, the returned iterator starts at the smallest element
     *  that is greater or equal to the lower bound. The lower bound is optional.
     *
     * //TODO optional upper bound?
     *
     * @param start the lower bound for the iterator
     * @return an iterator starting at a given lower bound
     */
    AIterator<T> iterator(AOption<T> start);

    AIterator<? extends ASortedSet<T>> subsets ();
    AIterator<? extends ASortedSet<T>> subsets (int len);
}
