package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.ARange;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.mutable.AMutableListWrapper;
import com.ajjpj.acollections.util.AOption;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * This interface represents a {@link java.util.List} with additional API (mostly inherited from {@link ACollection}. {@code AList}
 *  implementations can be either mutable or immutable - see {@link ACollection} for details.
 *
 * <p> This interface provides a rich API for adding / removing data from immutable collections (which works for mutable implementations
 *  as well but is not as necessary there). All operations modifying a list return the modified collection to allow working with
 *  immutable collections.
 *
 * <p> There are methods {@link #prepend(Object)} and {@link #append(Object)} to add elements at the start and at the end of a list:
 *
 * <p> {@code AList<Integer> l1 = AList.of(1, 2, 3);}
 * <p> {@code AList<Integer> l2 = l1.prepend(7);}
 * <p> {@code AList<Integer> l3 = l2.append(8);}
 * <p> {@code System.out.println(l1); // 1, 2, 3}
 * <p> {@code System.out.println(l2); // 7, 1, 2, 3}
 * <p> {@code System.out.println(l3); // 7, 1, 2, 3, 8}
 *
 * <p> These methods can of course be chained:
 *
 * <p> {@code AList<Integer> l4 = l1.append(1).prepend(2);}
 * <p> {@code System.out.println(l4); // 2, 1, 2, 3, 1}
 *
 * <p> There are methods {@link #tail()} and {@link #init()} to remove the first and last element, respectively:
 *
 * <p> {@code System.out.println(l1.tail()); // 2, 3}
 * <p> {@code System.out.println(l1.init()); // 1, 2}
 *
 * <p> There is also a method {@link #updated(int, Object)} for replacing an element at a given offset:
 *
 * <p> {@code System.out.println(l1.updated(1, 8)); // 1, 8, 3}
 *
 * <p> All of these methods modify the original collection if it is mutable. For immutable collections, they leave a original alone and return
 *  a modified copy.
 *
 * <p> This interface has static factory methods (Java 9 style) for convenience creating instances. They create immutable {@link AVector}
 *  instances.
 *
 * @param <T> the list's element type
 */
public interface AList<T> extends ACollection<T>, List<T> {
    /**
     * This is a convenience factory method wrapping an arbitrary (typically mutable) {@link java.util.List} in an {@link AMutableListWrapper}.
     *  This is a simple way to start using a-collections: Wrap an existing {@code List} to add a rich API while maintaining 100% backwards
     *  compatibility: operations on the wrapper are write-through, i.e. all changes are applied to the underlying {@code List}.
     *
     * @param l the List being wrapped
     * @param <T> the List's element type
     * @return the wrapped List
     */
    static <T> AList<T> wrap(List<T> l) {
        return AMutableListWrapper.wrap(l);
    }

    /**
     * Creates a new {@link AVector} based on an Iterable's elements.
     *
     * @param that the Iterable from which the new list is initialized
     * @param <T> the list's element type
     * @return the new list
     */
    static <T> AVector<T> from(Iterable<T> that) {
        return AVector.from(that);
    }

    /**
     * Creates a new {@link AVector} based on an array's elements.
     *
     * @param that the array from which the new list is initialized
     * @param <T> the list's element type
     * @return the new list
     */
    static <T> AVector<T> from(T[] that) {
        return AVector.from(that);
    }

    /**
     * Creates a new {@link AVector} based on an iterator's elements.
     *
     * @param it the iterator from which the new list is initialized
     * @param <T> the list's element type
     * @return the new list
     */
    static <T> AVector<T> fromIterator(Iterator<T> it) {
        return AVector.fromIterator(it);
    }

    /**
     * Convenience method for creating an empty {@link AVector}. For creating a list with known elements, calling one of the {@code of}
     *  factory methods is a more concise alternative.
     *
     * @param <T> the new set's element type
     * @return an empty {@link AVector}
     */
    static<T> AVector<T> empty() {
        return AVector.empty();
    }

    /**
     * This is an alias for {@link #empty()} for consistency with Java 9 conventions - it creates an empty {@link AVector}.
     *
     * @param <T> the new list's element type
     * @return an empty {@link AVector}
     */
    static <T> AVector<T> of() {
        return AVector.of();
    }

    /**
     * Convenience factory method creating an {@link AVector} with exactly one element.
     *
     * @param o the single element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AVector}
     */
    static <T> AVector<T> of(T o) {
        return AVector.of(o);
    }

    /**
     * Convenience factory method creating an {@link AVector} with two elements.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AVector}
     */
    static <T> AVector<T> of(T o1, T o2) {
        return AVector.of(o1, o2);
    }

    /**
     * Convenience factory method creating an {@link AVector} with three elements.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param o3 the third element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AVector}
     */
    static <T> AVector<T> of(T o1, T o2, T o3) {
        return AVector.of(o1, o2, o3);
    }

    /**
     * Convenience factory method creating an {@link AVector} with four elements.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param o3 the third element for the new list
     * @param o4 the fourth element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AVector}
     */
    static <T> AVector<T> of(T o1, T o2, T o3, T o4) {
        return AVector.of(o1, o2, o3, o4);
    }

    /**
     * Convenience factory method creating an {@link AVector} with more than four elements.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param o3 the third element for the new list
     * @param o4 the fourth element for the new list
     * @param o5 the fifth element for the new list
     * @param others the (variable number of) additional elements
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AVector}
     */
    @SafeVarargs static <T> AVector<T> of(T o1, T o2, T o3, T o4, T o5, T... others) {
        return AVector.of(o1, o2, o3, o4, o5, others);
    }

    @Override <U> ACollectionBuilder<U, ? extends AList<U>> newBuilder();

    /**
     * Returns an AList with an additional element inserted at offset 0, shifting existing elements and increasing the list's size by 1.
     *  For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param o the new element to be inserted
     * @return the list with the new element inserted at offset 0
     */
    AList<T> prepend(T o);

    /**
     * Returns an AList with an additional element added at offset {@link #size()}, increasing the list's size by 1.
     *  For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param o the new element to be appended
     * @return the list with the appended element
     */
    AList<T> append(T o);

    /**
     * Returns an AList with an iterator's content added at the end.
     *  For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param that the iterator whose elements are to be appended
     * @return the list with the appended elements
     */
    AList<T> concat (Iterator<? extends T> that);

    /**
     * Returns an AList with an Iterable's content added at the end.
     *  For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param that the Iterable whose elements are to be appended
     * @return the list with the appended elements
     */
    AList<T> concat (Iterable<? extends T> that);

    /**
     * Returns an AList with the element at offset {@code idx} replaced by the new element {@code o}.
     *  For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param idx the index at which the element is replaced
     * @param o   the new element replacing the previous element at index {@code idx}
     *
     * @throws IndexOutOfBoundsException if idx &lt; 0 or idx &gt;= size
     * @return the AList with the updated element
     */
    AList<T> updated(int idx, T o);

    /**
     * Replaces a number of elements with the contents of a list passed in. More specifically, removes {@code numReplaced} elements
     *  starting at offset {@code idx}, and then inserts the contents of {@code patch} at offset {@code idx}. The list of new values
     *  {@code patch} may have {@code numReplaced} elements, but it may be longer or shorter as well:
     *
     * <p> {@code AList.of(1, 2, 3).patch(0, AList.of(4, 5, 6), 3); // 4, 5, 6}
     * <p> {@code AList.of(1, 2, 3).patch(1, AList.of(4), 1); // 1, 4, 3}
     * <p> {@code AList.of(1, 2, 3).patch(0, AList.of(9, 8, 7), 0); // 9, 8, 7, 1, 2, 3}
     * <p> {@code AList.of(1, 2, 3).patch(1, AList.of(9, 8, 7), 1); // 1, 9, 8, 7, 3}
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param idx         the index at which elements are replaced
     * @param patch       the list of elements to be inserted
     * @param numReplaced the number of existing elements to be removed
     * @throws IndexOutOfBoundsException if idx&lt;0 or idx&gt;size()
     * @return the AList with the applied patch
     */
    AList<T> patch(int idx, List<T> patch, int numReplaced);

    /**
     * Returns the last element (i.e. the element at index {@code size()-1}, if it exists
     *
     * @throws NoSuchElementException if this list is empty
     * @return this list's last element
     */
    T last();

    /**
     * For a non-empty list, returns the last element wrapped in {@link AOption#some(Object)}, and {@link AOption#none()} when called
     *  on an empty collection
     *
     * @return the list's last element
     */
    AOption<T> lastOption();

    /**
     * Returns this list without the last element, throwing {@code NoSuchElementException} if the list is empty.
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @throws NoSuchElementException when called on an empty list
     * @return this list without the last element
     */
    AList<T> init();

    /**
     * Returns this list without the first element, throwing {@code NoSuchElementException} if the list is empty.
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @throws NoSuchElementException when called on an empty list
     * @return this list without the first element
     */
    AList<T> tail();

    /**
     * Returns the first {@code n} elements of this list, or the complete list if it has fewer than {@code n} elements.
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param n the number of elements to take
     * @return the first {@code n} elements of this list, or fewer if the list is smaller than that
     */
    AList<T> take(int n);

    /**
     * Returns the last {@code n} elements of this list, or the complete list if it has fewer than {@code n} elements.
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param n the number of elements to take
     * @return the last {@code n} elements of this list, or fewer if the list is smaller than that
     */
    AList<T> takeRight(int n);

    /**
     * Returns a list with elements starting at the start until one element does not match the predicate. More formally,
     *  returns the longest sublist starting at index 0 with the predicate testing to {@code true} for all elements.
     *
     * <p> For example:
     * <p> {@code AList.of(1).takeWhile(n -> n < 10); // 1 }
     * <p> {@code AList.of(1).takeWhile(n -> n > 10); // empty list }
     * <p> {@code AList.of(1, 2, 1, 3, 1).takeWhile(n -> n < 3); // 1, 2, 1 }
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param f the condition to decide which elements are taken
     * @return this list's starting elements until one of them fails the condition
     */
    AList<T> takeWhile(Predicate<T> f);

    /**
     * Returns this list without its first {@code n} elements, or an empty list if {@code n &gt; size()}.
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param n the number of elements to drop
     * @return this list without its first {@code n} elements
     */
    AList<T> drop(int n);

    /**
     * Returns this list without its last {@code n} elements, or an empty list if {@code n &gt; size()}.
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param n the number of elements to drop
     * @return this list without its last {@code n} elements
     */
    AList<T> dropRight(int n);

    /**
     * Removes elements from this collection's start until an element does not match a condition or the collection is empty.
     *
     * <p> For example:
     * <p> {@code AList.of(1).dropWhile(n -> n < 10) // empty list}
     * <p> {@code AList.of(1).dropWhile(n -> n > 10) // 1}
     * <p> {@code AList.of(1, 2, 1, 3, 1).dropWhile(n -> n < 3) // 3, 1}
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param f the condition deciding which elements get dropped
     * @return this collection without the elements at its start that match a given condition
     */
    AList<T> dropWhile(Predicate<T> f);

    /**
     * Returns this list, but with inverted element order, i.e. first elements last and last elements first.
     *
     * <p> For example:
     * <p> {@code AList.of(1, 2, 3).reverse(); // 3, 2, 1}
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @return this list but with inverted element order.
     */
    AList<T> reverse();

    /**
     * Returns an iterator for this list's elements, but starting at the end (index {@code size()-1}) and finishing at the start (index {@code 0}).
     *
     * @return an iterator in reverse element order
     */
    AIterator<T> reverseIterator();

    /**
     * Sorts this list's elements based on a {@link Comparator}. The sort operation is <em>stable</em>, i.e. elements comparing as equal
     *  are not reordered (see {@link Collections#sort(List)}.
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param comparator the {@link Comparator} determining sort order
     * @return a sorted list
     */
    AList<T> sorted(Comparator<? super T> comparator);

    /**
     * Sorts this list's elements based on a {@link Comparator#naturalOrder()}. This requires that elements implement {@link Comparable}.
     *  The sort operation is <em>stable</em>, i.e. elements comparing as equal are not reordered (see {@link Collections#sort(List)}.
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @throws ClassCastException if the list's elements do not implement {@link Comparable}
     * @return a sorted list
     */
    AList<T> sorted();

    /**
     * Sorts this list's elements based on the results of a function that is applied to each element. This is typically used to sort
     *  by an attribute of each element.
     *
     * <p> For example, the following sorts a list of (hypothetical) {@code Person} instances by their last name:
     * <p> {@code AList<Person> persons = ...;}
     * <p> {@code AList<Person> sorted = persons.sortBy(p -> p.getLastName());}
     *
     * <p> The following sorts by last name and (in case of equal last names) first name:
     * <p> {@code AList<Person> sorted2 = persons.sortBy(p -> p.getLastName() + " " + p.getFirstName);}
     *
     * <p> The sort operation is <em>stable</em>, i.e. elements comparing as equal are not reordered (see {@link Collections#sort(List)}.
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param f   the function to extract to attribute to sort by
     * @param <X> the type of the attribute to sort by (typically derived from the function by the compiler)
     * @return the sorted list
     */
    <X extends Comparable<X>> AList<T> sortedBy(Function<T,X> f);

    /**
     * Returns a list with the same elements as {@code this} but in (pseudo) random order. This is the same as {@link #shuffled(Random)}
     *  but without explicit control over the {@link Random} instance.
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @see Collections#shuffle(List)
     * @return the shuffled list
     */
    AList<T> shuffled();

    /**
     * Returns a list with the same elements as {@code this} but in (pseudo) random order based on the {@link Random} instance passed in.
     *
     * <p> For mutable collections, this operation modifies the collection, for immutable collections it returns a modified copy.
     *
     * @param r the {@link Random} instance used to shuffle the elements. Note that both {@link java.util.concurrent.ThreadLocalRandom} and
     *          {@link java.security.SecureRandom} are subtypes of {@link Random} and can be used here.
     * @see Collections#shuffle(List, Random)
     * @return the shuffled list
     */
    AList<T> shuffled(Random r);

    boolean contains(Object o);

    /**
     * Returns true if and only if this list starts with the same elements in the same order that another list has. More formally,
     *  given a list {@code that} of length n, this method return true if and only if {@code this.size() >= n} and
     *  {@code Objects.equals(this.get(i), that.get(i)} for each index i in the range {@code 0 <= i < n}.
     *
     * @param that the list that this list's start is compared to
     * @return true if and only if this list starts with {@code that}
     */
    boolean startsWith(List<T> that);

    /**
     * Returns true if and only if this list ends with the same elements in the same order that another list has. More formally,
     *  given a list {@code that} of length n, this method return true if and only if {@code this.size() >= n} and
     *  {@code Objects.equals(this.get(size() - n + i), that.get(i)} for each index i in the range {@code 0 <= i < n}.
     *
     * @param that the list that this list's end is compared to
     * @return true if and only if this list ends with {@code that}
     */
    boolean endsWith(List<T> that);

    /**
     * Aggregates this collection's elements into a single element of a (potentially) different type, starting with a "zero" element
     *  passed in as a parameter. For each element, a folding function is called with the intermediate result and the new element, producing
     *  the next intermediate result. Elements are traversed in reverse iteration order (i.e. right-to-left).
     *
     * <p> This is the same as {@link ACollection#foldLeft(Object, BiFunction)} but with reverse (right-to-left) iteration order.
     *
     * @param zero the initial value that is used as the "previous" result for the first element, and is the overall result for an empty collection
     * @param f    the aggregation function that combines the previous result and an element into the next result
     * @param <U>  the result type, which may be different from this collection's element type
     * @return the aggregated result
     */
    <U> U foldRight(U zero, BiFunction<U,T,U> f);

    /**
     * Reduces the elements of this collection to a single value of the same type using a reduction function. This method is similar to
     *  {@link #reduce(BiFunction)}, the only exception being that reduceRight guarantees the function being applied in reverse iteration
     *  order (i.e. right-to-left).
     *
     * @throws java.util.NoSuchElementException if the collection is empty
     * @param f the reduction function
     * @return the reduced value
     */
    T reduceRight(BiFunction<T,T,T> f);

    /**
     * Reduces the elements of this collection to a single value of the same type using a reduction function, iterating in reverse (i.e.
     *  right-to-left) order.
     *
     * <p> This method is practically identical to {@link #reduceRight(BiFunction)}, except that it wraps the result in
     *  {@link AOption#some(Object)} for non-empty lists, and returns {@link AOption#none()} for empty lists rather than throwing an
     *  exception
     *
     * @param f the reduction function
     * @return the reduced value
     */
    AOption<T> reduceRightOption(BiFunction<T,T,T> f);

    default @Override ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override ListIterator<T> listIterator(int index);

    <U> AList<U> map(Function<T,U> f);
    <U> AList<U> flatMap(Function<T, Iterable<U>> f);
    AList<T> filter(Predicate<T> f);
    AList<T> filterNot(Predicate<T> f);
    <U> AList<U> collect(Predicate<T> filter, Function<T,U> f);

    /**
     * Returns a (cheap and efficient) list containing all of this list's valid indices.
     *
     * <p> Examples:
     * <p> {@code AList.empty().indices(); // empty list}
     * <p> {@code AList.of(9, 2, 17).indices(); // 0, 1, 2}
     * <p> {@code AList.of(6).indices(); // 0}
     *
     * @see ARange
     * @return a list containing all of this lists's valid indices
     */
    default ARange indices() {
        return ARange.create(0, size());
    }
}
