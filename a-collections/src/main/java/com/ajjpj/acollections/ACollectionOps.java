package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.*;
import com.ajjpj.acollections.mutable.AMutableListWrapper;
import com.ajjpj.acollections.mutable.AMutableSetWrapper;
import com.ajjpj.acollections.util.AOption;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * This interface defines a rich API of collection methods applicable to all kinds of collections (i.e. lists, sets, ...). It is separate
 *  from {@link ACollection} so that {@link AMap} can inherit {@link java.util.Map} and be an {@link Iterable} with
 *  {@link ACollectionOps}{@code <}{@link Map.Entry}{@code >} at the same time.
 *
 * <p> Application code will not usually see or use this interface but rather the actual collection interfaces {@link ACollection}
 *  (or more specifically {@link ASet} or {@link AList}, or {@link AMap}. But it defines many of the most widely used collection methods.
 */
public interface ACollectionOps<T> {
    /**
     * Returns an iterator to this collection, just like {@link Collection#iterator()}. The returned iterator however implements
     *  {@link AIterator} which adds a rich API to {@link java.util.Iterator}.
     */
    AIterator<T> iterator ();

    /**
     * This returns a builder for a collection of the same type (and with the same configuration, e.g. comparator in case of sorted
     *  collections).
     *
     * <p> This is public API, but it was added largely for internal use: Having this method allows generically implementing transformation
     *  methods like {@link #map(Function)}.
     */
    <U> ACollectionBuilder<U, ? extends ACollectionOps<U>> newBuilder();

    /**
     * This is a special case of {@link #newBuilder()} which restricts the collection's type to {@link Map.Entry}. This method only exists
     *  for maps and some special cases surrounding them: If you see a map as a collection, it can only have elements of type {@link Map.Entry}.
     */
    default <K,V> ACollectionBuilder<Map.Entry<K,V>, ? extends ACollectionOps<Map.Entry<K,V>>> newEntryBuilder() {
        return newBuilder();
    };

    /**
     * @return the number of elements in this collection.
     */
    int size();

    /**
     * @return true if and only if this collection has no elements
     */
    boolean isEmpty();

    /**
     * @return true if and only if this collection has at least one element
     */
    default boolean nonEmpty() {
        return ! isEmpty();
    }

    /**
     * @return the collection's first element, of {@code null} if it is empty. 'First' element means first element in iteration order,
     *  which differs a lot for different kinds of collections:
     *  <ul>
     *      <li> For an {@link AList}, it is the element at index 0.
     *      <li> For an {@link ASortedSet} or {@link ASortedMap}, it is the 'smallest' element (as defined by its sort order)
     *      <li> For an {@link AHashSet} or {@link AHashMap}, it can be any element.
     *  </ul>
     */
    T head();

    /**
     * This is the same as {@link #head()}, but returning {@link AOption#some(Object)} for non-empty collections and {@link AOption#none()}
     *  for empty collections.
     */
    AOption<T> headOption();

    /**
     * This is a convenience method to convert any collection into an {@link ALinkedList}. If the collection is an {@link ALinkedList}
     *  already, this is a no-op, otherwise a new instance is created.
     *
     * @return an {@link ALinkedList} containing this collection's elements in iteration order
     */
    ALinkedList<T> toLinkedList();
    /**
     * This is a convenience method to convert any collection into an {@link AVector}. If the collection is an {@link AVector}
     *  already, this is a no-op, otherwise a new instance is created.
     *
     * @return an {@link AVector} containing this collection's elements in iteration order
     */
    AVector<T> toVector();
    /**
     * This is a convenience method to convert any collection into an {@link AHashSet}. If the collection is an {@link AHashSet}
     *  already, this is a no-op, otherwise a new instance is created.
     *
     * @return an {@link AHashSet} containing this collection's elements
     */
    AHashSet<T> toSet();
    /**
     * This is the same as {@link #toSortedSet(Comparator)}, but using {@link Comparator#naturalOrder()} implicitly. This method relies
     *  on the collection's elements implementing {@link Comparable}, throwing a {@link ClassCastException} if they don't.
     */
    default ATreeSet<T> toSortedSet() {
        //noinspection unchecked
        return toSortedSet((Comparator) Comparator.naturalOrder());
    }

    /**
     * This is a convenience method to convert any collection into an {@link ATreeSet} based on a {@link Comparator} passed in as a
     *  parameter.
     *
     * @return an {@link ATreeSet} containing this collection's elements
     */
    ATreeSet<T> toSortedSet(Comparator<T> comparator);

    /**
     * This is a convenience method converting this collection into an immutable {@link AMap}. It expects the collection to consist of
     *  {@link Map.Entry}, throwing a {@link ClassCastException} otherwise.
     */
    <K,V> AMap<K,V> toMap();

    /**
     * This is a convenience method to convert any collection into a mutable {@link AList} (more specifically, an {@link AMutableListWrapper}
     *  around a {@link java.util.ArrayList}) with this collection's elements in iteration order.
     *
     * @return an {@link AMutableListWrapper} containing this collection's elements
     */
    AMutableListWrapper<T> toMutableList();
    /**
     * This is a convenience method to convert any collection into a mutable {@link ASet} (more specifically, an {@link AMutableSetWrapper}
     *  around a {@link java.util.HashSet}) with this collection's elements.
     *
     * @return an {@link AMutableSetWrapper} containing this collection's elements
     */
    AMutableSetWrapper<T> toMutableSet();
    //TODO toMutableMap

    /**
     * creates a new collection of the same collection type by applying a function to each element. The new collection's element type
     *  does not have to be the same as this collection's.
     *
     * <p>Examples:
     * <p>{@code AVector.of(1, 2, 3).map(x -> 2*x)} returns the collection {@code AVector.of(2, 4, 6)}
     * <p>{@code AHashSet.of(1, 2, 3).map(String::valueOf} returns the collection {@code AHashSet.of("1", "2", "3")}
     *
     * <p> The resulting collection is the same kind of collection as this, i.e. calling the method on an AVector returns an AVector etc.
     *
     * @param f the function that is applied to each element
     * @param <U> the resulting collection's element type
     * @return an {@link ACollection} of the same type
     */
    <U> ACollection<U> map(Function<T,U> f);

    /**
     * creates a new collection of the same collection type by applying a function to each element. The difference to {@link #map(Function)}
     *  is that for flatMap(Function), the function returns an {@link Iterable} which is then unwrapped, i.e. the function returns a
     *  collection of new elements for each element which is then flattened into a single collection.
     *
     * <p> Examples:
     * <ul>
     * <li> {@code AVector.of(2, 3, 4).flatMap(n -> ARange.create(0, n))} returns the collection {@code AVector.of(0, 1, 0, 1, 2, 0, 1, 2, 3)}
     * <li> For a more comprehensive example, let us assume a fictional business application with classes {@code Customer} and {@code Order}
     *       and a method {@code ordersFor(Customer)} which returns all orders for a given customer. Then the following code creates
     *       an {@link ASet} with all orders for a given group of customers:
     *       <p>{@code ASet<Customer> customers = ...;}
     *       <p>{@code ASet<Order> orders = customers.flatMap(c -> ordersFor(c);}
     * <li> {@link AOption} is iterable, and using flatMap() with a function returning {@link AOption} can be a concise way of filtering
     *       and transforming the contents of a collection.
     *       <p> Let us assume there is a method {@code AOption<LocalDate> birthdayOf(String person)} that returns a person's birthday
     *       if it is known. Then the following code will provide all birthdays for a given group of persons:
     *       <p> {@code ASet<String> persons = ...;}
     *       <p> {@code ASet<LocalDate> birthdays = persons.flatMap(p -> birthdayOf(p);}
     * </ul>
     *
     * <p> The resulting collection is the same kind of collection as this, i.e. calling the method on an AVector returns an AVector etc.
     *
     * As an aside, flatMap() is one of the key ingredients of monads (see https://en.wikipedia.org/wiki/Monad_(functional_programming).
     *  That is of no importance to a-collections, but it is worth a mention to avoid confusion.
     *
     * @param f the function providing new element(s) for each element
     * @param <U> the new elements' type
     * @return a new collection containing the flattened contents of the function's results for all elements
     */
    <U> ACollection<U> flatMap(Function<T, Iterable<U>> f);

    /**
     * Filters the contents of this collection and applies a function to those elements passing the filter, so
     *  {@code coll.collect(filter, function)} is equivalent to {@code coll.filter(filter).map(function)} but can be more expressive and
     *  efficient.
     *
     * <p> The resulting collection is the same kind of collection as this, i.e. calling the method on an AVector returns an AVector etc.
     *
     * @param filter the predicate filtering elements
     * @param f      the transformation function for those elements passing the filter
     * @param <U>    the new collection's element type.
     * @return a new collection containing the transformed elements that passed the filter
     */
    <U> ACollection<U> collect(Predicate<T> filter, Function<T,U> f);

    /**
     * Applies a transformation function to the first element (in iteration order) matching a predicate.
     *  {@code coll.collectFirst(filter, function)} is equivalent to {@code coll.find(filter).map(function)} but can be more expressive and
     *  efficient.
     *
     * @param filter the condition used to find an element
     * @param f      the transformation function applied to that element
     * @param <U>    the result's element type
     * @return an {@link AOption} containing the transformed matching element (if any), and {@link AOption#none()} if no element matches
     *         the filter
     */
    <U> AOption<U> collectFirst(Predicate<T> filter, Function<T,U> f);

    /**
     * Creates a new collection containing only those elements of this collection that match a given condition. The new collection is of
     *  the same kind as this collection, i.e. calling it on an AVector returns an AVector. For ordered collections, iteration order is
     *  maintained.
     *
     * @param f the condition that elements must meet in order to be in the resulting collection.
     * @return the filtered collection
     */
    ACollectionOps<T> filter(Predicate<T> f);

    /**
     * The same as {@link #filter(Predicate)} except that elements must <em>not</em> meet the condition in order to be included in the result
     *
     * @param f the condition that elements must not meet in order to be included in the result
     * @return the filtered collection
     */
    ACollectionOps<T> filterNot(Predicate<T> f);

    /**
     * Returns an element that matches a given condition, or {@link AOption#none()} if no such element exists.
     *
     * @param f the criterion a returned element must meet
     * @return an element matching the criterion, or {@link AOption#none()} if no such element exists.
     */
    AOption<T> find(Predicate<T> f);

    /**
     * Returns true if and only if all elements fulfil a given condition (though shortcut evaluation is in order: if one element fails the test,
     *  implementations are not required to test other elements). This is sort of a counterpart to {@link #exists(Predicate)}.
     * <p> This method name is somewhat to {@link Collection#forEach(Consumer)} which does something entirely different. This similarity is
     *  somewhat unfortunate, but both names are so well established that living with the similarity seemed a viable trade-off.
     *
     * @param f the condition the elements are checked against
     * @return true if and only if all elements meet the criterion
     */
    boolean forall(Predicate<T> f);

    /**
     * Returns true if and only if at least one element fulfil a given condition. This is sort of a counterpart to {@link #forall(Predicate)}.
     *
     * @param f the condition the elements are checked against
     * @return true if and only if at least one element meets the criterion
     */
    boolean exists(Predicate<T> f);

    /**
     * Returns the number of elements meeting a given criterion.
     *
     * @param f the criterion an element must meet in order to be counted
     * @return the number of elements meeting the criterion
     */
    int count(Predicate<T> f);

    /**
     * Returns <tt>true</tt> if this collection contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this collection
     * contains at least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param o element whose presence in this collection is to be tested
     * @return <tt>true</tt> if this collection contains the specified
     *         element
     */
    boolean contains(Object o);

    /**
     * "reduces" the elements of this collection to a single value of the same type using a function that takes two values of the same type
     *  and returns their "combination".
     *
     * <p> One simple example is summing up the numbers in a collection: {@code AVector.of(1, 2, 3).reduce((a,b) -> a+b)} first creates the
     *      sum of two of the numbers (first call of the reducing function), and then calls the function again with the result of the first
     *      call and the remaining element, returning a total of {@code 6}.
     *
     * <p> The order in which elements are passed to the function is not specified.
     * <p> If the collection has only one element, that element ist returned. If the collection is empty, a {@link java.util.NoSuchElementException}
     *      is thrown
     *
     * @throws java.util.NoSuchElementException if the collection is empty
     * @param f the function to combine two values into one
     * @return the single value that results from passing all values to the reduction function
     */
    default T reduce(BiFunction<T,T,T> f) {
        return reduceLeft(f);
    }

    /**
     * Reduces the elements of this collection to a single value of the same type using a reduction function. This is the same as
     *  {@link #reduce(BiFunction)} except that the result is wrapped in an {@link AOption}, with {@link AOption#none()} being returned
     *  for an empty collection.
     *
     * @param f the reduction function
     * @return the reduced value, or {@link AOption#none()} if this collection is empty
     */
    default AOption<T> reduceOption(BiFunction<T,T,T> f) { //TODO junit ttest
        return reduceLeftOption(f);
    }

    /**
     * Reduces the elements of this collection to a single value of the same type using a reduction function. This method is similar to
     *  {@link #reduce(BiFunction)}, the only exception being that reduceLeft guarantees the function being applied in iteration order (which
     *  often does not make any difference).
     *
     * @throws java.util.NoSuchElementException if the collection is empty
     * @param f the reduction function
     * @return the reduced value
     */
    T reduceLeft(BiFunction<T,T,T> f);

    /**
     * Reduces the elements of this collection to a single value of the same type using a reduction function. This is the same as
     *  {@link #reduceLeft(BiFunction)} except that the result is wrapped in an {@link AOption}, with {@link AOption#none()} being returned
     *  for an empty collection.
     *
     * @param f the reduction function
     * @return the reduced value, or {@link AOption#none()} if this collection is empty
     */
    AOption<T> reduceLeftOption(BiFunction<T,T,T> f);

    /**
     * Aggregates this collection's elements into a single element of a (potentially) different type, starting with a "zero" element
     *  passed in as a parameter. This is the same as {@link #foldLeft(Object, BiFunction)} except that the traversal order is not specified.
     *
     * @param zero the initial value that is used as the "previous" result for the first element, and is the overall result for an empty collection
     * @param f    the aggregation function that combines the previous result and an element into the next result
     * @param <U>  the result type, which may be different from this collection's element type
     * @return the aggregated result
     */
    default <U> U fold(U zero, BiFunction<U,T,U> f) {
        return foldLeft(zero, f);
    }

    /**
     * Aggregates this collection's elements into a single element of a (potentially) different type, starting with a "zero" element
     *  passed in as a parameter. For each element, a folding function is called with the intermediate result and the new element, producing
     *  the next intermediate result. Elements are traversed in iteration order.
     *
     * <p> For example, {@code AVector.of(1, 2, 3).fold("Numbers:", (agg,el) -> agg + " " + el)} produces the string {@code "Numbers: 1 2 3"}.
     *
     * @param zero the initial value that is used as the "previous" result for the first element, and is the overall result for an empty collection
     * @param f    the aggregation function that combines the previous result and an element into the next result
     * @param <U>  the result type, which may be different from this collection's element type
     * @return the aggregated result
     */
    <U> U foldLeft(U zero, BiFunction<U,T,U> f);

    /**
     * Creates an {@link AMap} from this collection, grouping elements by key. Each element's key is determined by calling a 'key extractor'
     *  function on the element. Values in the resulting map are collections of the same kind as this collection.
     *
     * <p> For example, {@code AVector.of("Martin", "Mike", "Arno").groupBy(n -> n.charAt(0))} creates the result
     *  {@code AMap.of('A', AVector.of("Arno"), 'M', AVector.of("Martin", "Mike")) }
     * <p> Or numbers can be grouped by their even-ness: {@code AVector.of(1, 2, 3, 4).groupBy(n -> n%2==0) } returns the map
     *  {@code AMap.of(true, AVector.of(2, 4), false, AVector.of(1, 3))}
     *
     * @param keyExtractor the function that is applied to each element to determine the corresponding key for the resulting map
     * @param <K1>         the new map's key type
     * @return a map containing all of this collection's elements grouped by key
     */
    <K1> AMap<K1,? extends ACollectionOps<T>> groupBy(Function<T,K1> keyExtractor);

    /**
     * Finds and returns the element which is smallest as determined by natural order. This method assumes that elements implement
     *  {@link Comparable}.
     *
     * @throws java.util.NoSuchElementException if this collection is empty
     * @throws ClassCastException if elements do not implement {@link Comparable}
     * @return this collection's smallest element (relative to natural order)
     */
    T min();

    /**
     * Finds and returns the element which is smallest as determined by the {@link Comparator} that is passed in.
     *
     * @param comparator the {@link Comparator} determining the elements' ordering
     * @throws java.util.NoSuchElementException if this collection is empty
     * @return the smallest element
     */
    T min(Comparator<T> comparator);

    /**
     * Finds and returns the element which is biggest as determined by natural order. This method assumes that elements implement
     *  {@link Comparable}.
     *
     * @throws java.util.NoSuchElementException if this collection is empty
     * @throws ClassCastException if elements do not implement {@link Comparable}
     * @return this collection's biggest element (relative to natural order)
     */
    T max();

    /**
     * Finds and returns the element which is biggest as determined by the {@link Comparator} that is passed in.
     *
     * @param comparator the {@link Comparator} determining the elements' ordering
     * @throws java.util.NoSuchElementException if this collection is empty
     * @return the biggest element
     */
    T max(Comparator<T> comparator);

    /**
     * Concatenates this collection's elements, inserting an infix string between them.
     *
     * <p> For example, {@code AVector.of(1, 2, 3).mkString("|")} returns the resulting string {@code "1|2|3"}.
     *
     * @param infix the string inserted between two consecutive elements
     * @return a string with this collection's concatenated elements
     */
    String mkString(String infix);

    /**
     * Concatenates this collection's elements, inserting an infix string between them and adding a prefix and suffix before and after
     *  the result.
     *
     * <p> For example, {@code AVector.of(1, 2, 3).mkString("<", "|", ">")} returns the resulting string {@code "<1|2|3>"}.
     *
     * @param prefix the string that is prefixed to the result
     * @param infix the string inserted between two consecutive elements
     * @param suffix the string that is suffixed to the result
     * @return a string with this collection's concatenated elements
     */
    String mkString(String prefix, String infix, String suffix);
}
