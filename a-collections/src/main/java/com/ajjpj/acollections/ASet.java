package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.mutable.AMutableSetWrapper;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


/**
 * This interface represents a {@link java.util.Set} with additional API (mostly inherited from {@link ACollection}). {@code ASet}
 *  implementations can be either mutable or immutable - see {@link ACollection} for details.
 *
 * <p> This interface defines methods {@link #plus(Object)} and {@link #minus(Object)} for adding / removing elements to an immutable
 *  ASet. (They work on mutable instances as well, but they are not necessary there.) These methods return new sets with the new elements,
 *  leaving the original unmodified:
 *
 * <p>{@code ASet<Integer> s0 = ASet.of(1, 2, 3);}
 * <p>{@code ASet<Integer> s1 = s0.plus(5);}
 * <p>{@code ASet<Integer> s2 = s1.minus(2);}
 * <p>{@code System.out.println(s0); // 1, 2, 3 }
 * <p>{@code System.out.println(s1); // 1, 2, 3, 5 }
 * <p>{@code System.out.println(s2); // 1, 3, 5 }
 *
 * <p> These calls can of course be chained:

 * <p>{@code ASet<Integer> s3 = s2.plus(8).plus(9).minus(3); }
 * <p>{@code System.out.println(s3); // 1, 5, 8, 9 }
 *
 * <p> This interface has static factory methods (Java 9 style) for convenience creating instances. They create immutable {@link AHashSet}
 *  instances.
 *
 * @param <T> The ASet's element type
 */
public interface ASet<T> extends ACollection<T>, Set<T> {
    /**
     * This is a convenience factory method wrapping an arbitrary (typically mutable) {@link java.util.Set} in an {@link AMutableSetWrapper}.
     *  This is a simple way to start using a-collections: Wrap an existing {@code Set} to add a rich API while maintaining 100% backwards
     *  compatibility: operations on the wrapper are write-through, i.e. all changes are applied to the underlying {@code Set}.
     *
     * @param s the Set being wrapped
     * @param <T> the Set's element type
     * @return the wrapped Set
     */
    static <T> AMutableSetWrapper<T> wrap(Set<T> s) {
        return AMutableSetWrapper.wrap(s);
    }

    /**
     * Convenience method for creating an empty {@link AHashSet}. This can later be modified by calling {@link #plus(Object)} or
     * {@link #minus(Object)}. For creating a set with known elements, calling one of the {@code of} factory methods is usually more concise.
     *
     * @param <T> the new set's element type
     * @return an empty {@link AHashSet}
     */
    static<T> AHashSet<T> empty() {
        return AHashSet.empty();
    }

    /**
     * This is an alias for {@link #empty()} for consistency with Java 9 conventions - it creates an empty {@link AHashSet}.
     *
     * @param <T> the new set's element type
     * @return an empty {@link AHashSet}
     */
    static <T> AHashSet<T> of() {
        return AHashSet.of();
    }

    /**
     * Convenience factory method creating an {@link AHashSet} with exactly one element.
     *
     * @param o the single element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AHashSet}
     */
    static <T> AHashSet<T> of(T o) {
        return AHashSet.of(o);
    }

    /**
     * Convenience factory method creating an {@link AHashSet} with two elements.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AHashSet}
     */
    static <T> AHashSet<T> of(T o1, T o2) {
        return AHashSet.of (o1, o2);
    }

    /**
     * Convenience factory method creating an {@link AHashSet} with three elements.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param o3 the third element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AHashSet}
     */
    static <T> AHashSet<T> of(T o1, T o2, T o3) {
        return AHashSet.of(o1, o2, o3);
    }

    /**
     * Convenience factory method creating an {@link AHashSet} with four elements.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param o3 the third element for the new set
     * @param o4 the fourth element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AHashSet}
     */
    static <T> AHashSet<T> of(T o1, T o2, T o3, T o4) {
        return AHashSet.of(o1, o2, o3, o4);
    }

    /**
     * Convenience factory method creating an {@link AHashSet} with more than four elements.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param o3 the third element for the new set
     * @param o4 the fourth element for the new set
     * @param o5 the fifth element for the new set
     * @param others the (variable number of) additional elements
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AHashSet}
     */
    @SafeVarargs static <T> AHashSet<T> of(T o1, T o2, T o3, T o4, T o5, T... others) {
        return AHashSet.of(o1, o2, o3, o4, o5, others);
    }

    /**
     * Creates a new {@link AHashSet} based on an array's elements.
     *
     * @param that the array from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    static <T> AHashSet<T> from(T[] that) {
        return AHashSet.from(that);
    }

    /**
     * Creates a new {@link AHashSet} based on an {@link Iterable}'s elements.
     *
     * @param that the {@link Iterable} from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    static <T> AHashSet<T> from(Iterable<T> that) {
        return AHashSet.from(that);
    }

    /**
     * Creates a new {@link AHashSet} based on an {@link Iterator}'s elements.
     *
     * @param it the {@link Iterator} from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    static <T> AHashSet<T> fromIterator(Iterator<T> it) {
        return AHashSet.fromIterator(it);
    }


    @Override <U> ACollectionBuilder<U, ? extends ASet<U>> newBuilder ();

    /**
     * Returns an ASet containing all this set's elements as well as an additional element.
     *  More formally, adds the specified element
     *  <tt>o</tt> to this set if the set contains no element <tt>e2</tt>
     *  such that
     *  <tt>(o==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;o.equals(e2))</tt>.
     *  If this set already contains the element, the call returns this set
     *  unchanged. This ensures that sets never contain duplicate elements.
     *
     * <p> For a mutable ASet, this is equivalent to calling {@link java.util.Set#add(Object)}; for an immutable set, the method
     *  returns a new instance with the new element.
     *
     * @param o the element to be added
     * @return the (potentially) modified set
     */
    ASet<T> plus (T o);
    //TODO plusAll

    /**
     * Returns an ASet containing all this set's elements without the element
     *  passed to the call.
     * More formally, removes an element <tt>e</tt>
     * such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, if
     * this set contains such an element, returning the (potentially) modified set.
     * This returned set will not contain the element.
     *
     * <p> For a mutable ASet, this is equivalent to calling {@link java.util.Set#remove(Object)}; for an immutable set, the method
     *  returns a new instance with the new element.
     *
     * @param o the element to be added
     * @return the (potentially) modified set
     */
    ASet<T> minus (T o);

    /**
     * Returns a set containing all of this set's elements as well as those of an
     * {@link Iterable} passed in as a parameter. Duplicates are removed based on
     * equality, i.e. the resulting set is guaranteed not to contain two elements a, b
     * for which {@code Objects.equals(a,b) == true}.
     *
     * <p> For a mutable set, this method is equivalent to {@link java.util.Set#addAll(Collection)},
     *  adding all of {@code that}'s element to this. For an immutable set, the call creates a
     *  new set, leaving the original untouched.
     *
     * @param that the {@link Iterable} whose elements are added
     * @return the set containing both collections' elements
     */
    ASet<T> union(Iterable<T> that);

    /**
     * Returns a set containing all of this' elements also contained in another set.
     *
     * <p> For a mutable set, this method is equivalent to {@link java.util.Set#retainAll(Collection)},
     *  removing all elements not present in {@code that} from {@code this}. For an immutable set, the call
     *  creates a new set, leaving the original untouched.
     *
     * @param that the set with which the intersection is done
     * @return the set containing the intersection
     */
    ASet<T> intersect(Set<T> that);

    /**
     * Returns a set containing all of this' elements not contained in another set.
     *
     * <p> For a mutable set, this method is equivalent to {@link java.util.Set#removeAll(Collection)},
     *  removing all of {@code that}'s elements from {@code this}. For an immutable set, the call creates
     *  a new set, leaving the original untouched.
     *
     * @param that the set whose elements are removed
     * @return the set containing the diff
     */
    ASet<T> diff(Set<T> that);

    /**
     * Returns an iterator with all of this set's subsets. The number of subsets can be huge compared to the
     *  number of elements in the set (e.g. &gt; 1.000.000 subsets for a set of 20 elements), which is the reason
     *  the method returns an iterator rather than a collection.
     *
     * <p> The method loops of subset length, starting with 0 and ending with {@link #size()}, iterating
     *  over subsets of one length before starting with the next. So subsets are guaranteed to be returned
     *  "shortest first".
     *
     * <p> This method calls {@link #subsets(int)} for any given length, so it provides the same overhead
     *  and performance guarantees described there.
     *
     * @return an iterator with all of this set's subsets
     */
    AIterator<? extends ASet<T>> subsets();

    /**
     * Returns an iterator with all of this set's subsets with a given number of elements. The number of
     *  subsets can be huge compared to the number of elements in the set (e.g. &gt; 184.000 subsets of length 10
     *  for a set of 20 elements), which is the reason the method returns an iterator rather than a collection.
     *
     * <p> Going from one subset to the next (i.e. calling {@link AIterator#next()} on the result) is cheap,
     *  taking O(n) plus the overhead of creating the set, and has negligible memory overhead.
     *
     * @param len the number of entries for which subsets should be returned
     * @return an iterator with all of this set's subsets of a given length
     */
    AIterator<? extends ASet<T>> subsets(int len);
}
