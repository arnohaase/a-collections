package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.ALinkedList;
import com.ajjpj.acollections.immutable.ATreeSet;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.mutable.AMutableListWrapper;
import com.ajjpj.acollections.mutable.AMutableSetWrapper;
import com.ajjpj.acollections.util.AOption;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * This is an iterator with a rich additional API (along the lines of what {@link java.util.stream.Stream} has). AIterator extends
 *  {@link java.util.Iterator}, and an AIterator can be used wherever a {@link java.util.Iterator} is expected, allowing seamless
 *  integration.
 *
 * <p> There is a factory method {@link #wrap(Iterator)} to add AIterator API to any {@link Iterator} in a simple and efficient manner.
 *  That allows using AIterator methods on any {@link Iterator} in an ad hoc fashion without requiring far-reaching code changes, e.g.
 *
 * <p> {@code Iterator<String> it = ...;}
 * <p> {@code int minOdd = AIterator.wrap(it).filter(n -> n%2==0).min();}
 *
 * <p> There are convenience methods to create an empty iterator ({@link #empty()}) or an iterator with a single element
 *  ({@link #single(Object)}), or for concatenating several iterators into a single iterator ({@link #concat(Iterator)}, {@link #concat(Iterator[])}).
 *
 * <p> There are also convenience methods to collect an AIterator into a collection: {@link #toVector()}, {@link #toLinkedList()}, {@link #toSet()},
 *  {@link #toSortedSet()}, {@link #toMutableSet()} and {@link #toMutableList()}.
 *
 * @param <T> the iterator's element type
 */
public interface AIterator<T> extends Iterator<T> {
    /**
     * Returns an {@link AIterator} representing the same elements as a given {@link Iterator}, wrapping that iterator in a cheap and lazy
     *  fashion. This allows using {@link AIterator} API on any given iterator in an ad hoc fashion.
     *
     * @param inner the {@link Iterator} to be wrapped
     * @param <T> the iterator's element type
     * @return an AIterator wrapping the Iterator
     */
    static <T> AIterator<T> wrap(Iterator<T> inner) {
        if (inner instanceof AIterator) return (AIterator<T>) inner;
        return new AIteratorWrapper<>(inner);
    }

    /**
     * Returns an empty AIterator, i.e. an iterator for which {@link #hasNext()} is always {@code false}.
     *
     * @param <T> the iterator's element type
     * @return an empty iterator
     */
    static <T> AIterator<T> empty() {
        //noinspection unchecked
        return (AIterator<T>) AbstractAIterator.empty;
    }

    /**
     * Returns an AIterator containing exactly one element.
     *
     * @param o   the iterator's single element
     * @param <T> the iterator's element type
     * @return an iterator containing exactly one element
     */
    static <T> AIterator<T> single(T o) {
        return new AbstractAIterator.Single<>(o);
    }

    /**
     * Returns an {@link AVector} containing this iterator's elements
     * @return an {@link AVector} containing this iterator's elements
     */
    default AVector<T> toVector() {
        return AVector.fromIterator(this);
    }

    /**
     * Returns an {@link ALinkedList} containing this iterator's elements
     * @return an {@link ALinkedList} containing this iterator's elements
     */
    default ALinkedList<T> toLinkedList() {
        return ALinkedList.fromIterator(this);
    }

    /**
     * Returns an {@link AHashSet} containing this iterator's elements
     * @return an {@link AHashSet} containing this iterator's elements
     */
    default AHashSet<T> toSet() {
        return AHashSet.fromIterator(this);
    }

    /**
     * Returns an {@link ATreeSet} containing this iterator's elements
     * @return an {@link ATreeSet} containing this iterator's elements
     */
    default ATreeSet<T> toSortedSet() {
        //noinspection unchecked
        return ATreeSet.fromIterator(this, (Comparator) Comparator.naturalOrder());
    }

    /**
     * Returns an {@link AMutableListWrapper} containing this iterator's elements
     * @return an {@link AMutableListWrapper} containing this iterator's elements
     */
    default AMutableListWrapper toMutableList() {
        return AMutableListWrapper.fromIterator(this);
    }

    /**
     * Returns an {@link AMutableSetWrapper} containing this iterator's elements
     * @return an {@link AMutableSetWrapper} containing this iterator's elements
     */
    default AMutableSetWrapper toMutableSet() { //TODO test this
        return AMutableSetWrapper.fromIterator(this);
    }


    /**
     * Returns true if this iterator contains the same elements as another iterator in the same order. More formally, the method iterates
     *  over this iterator and another iterator, comparing elements pairwise using {@link Objects#equals(Object, Object)}, and returns true
     *  if and only if each pair of elements compares as equal and neither iterator has remaining elements when the other has reached its
     *  end.
     *
     * <p> Calling this method is the equivalent to (though more efficient than) {@code this.toVector().equals(that.toVector())}
     *
      * @param that the iterator to compare to
     * @return true if and only if the iterators correspond to each other
     */
    default boolean corresponds(Iterator<T> that) {
        return corresponds(that, Objects::equals);
    }

    /**
     * Returns true if this iterator contains equivalent elements (as defined by a {@link BiPredicate} that is passed in) as another
     *  iterator. More formally, the method iterates over this iterator and another iterator, comparing elements pairwise using
     *  the {@link BiPredicate} passed in as a parameter, and returns true if and only if the BiPredicate returns true for each pair of
     *  elements and neither iterator has remaining elements when the other has reached its end.
     *
     * <p> This method allows comparing iterators with different element types.
     *
     * <p> This method is a generalization of {@link #corresponds(Iterator)}, using a caller-provided BiPredicate instead of
     *  {@link Objects#equals(Object, Object)}.
     *
     * @param that the iterator to compare to
     * @param f    the BiPredicate used for element comparison
     * @param <U>  the other iterator's element type
     * @return true if and only if the iterators correspond to each other
     */
    default <U> boolean corresponds(Iterator<U> that, BiPredicate<T,U> f) {
        while (this.hasNext() && that.hasNext()) {
            if (!f.test(this.next(), that.next())) return false;
        }
        return this.hasNext() == that.hasNext();
    }

    /**
     * Returns a new iterator by applying a function of each of this iterator's elements. Example:
     *
     * <p> {@code AIterator<String> it = AVector.of("a", "bcd", "ef").iterator();}
     * <p> {@code AIterator<Integer> itLen = it.map(s -> s.length()); // 1, 3, 2}
     *
     * @param f   the transformation function for each element
     * @param <U> the new iterator's element type
     * @return a new iterator with this iterator's transformed elements
     * @see ACollectionOps#map(Function)
     */
    default <U> AIterator<U> map(Function<T,U> f) {
        final AIterator<T> inner = this;
        return new AbstractAIterator<U>() {
            @Override public boolean hasNext () {
                return inner.hasNext();
            }
            @Override public U next () {
                return f.apply(inner.next());
            }
        };
    }

    /**
     * Applies a function to each of this iterator's elements, concatenating the returned iterators.
     *
     * <p> This method is for when each element in an iterator produces zero or more elements, and the resulting elements should be
     *  treated as an iterator themselves. The following example turns an iterator of text snippets into an iterator of words by splitting
     *  each text snippet into its comprising words:
     *
     * <pre>
     * {@code Iterator<String> splitToWords(String s) {return AMutableArrayWrapper.wrap(s.split(" ")).iterator(); } }
     * {@code ...}
     * {@code AIterator<String> snippets = ...;}
     * {@code AIterator<String> words = snippets.flatMap(this::splitToWords);}
     * </pre>
     *
     * @param f   the extractor function providing the resulting elements for each of this iterator's elements
     * @param <U> the resulting iterator's element type
     * @return an iterator with the concatenated results of applying a function to each of this iterator's elements
     */
    <U> AIterator<U> flatMap(Function<? super T, ? extends Iterator<? extends U>> f);

    /**
     * Returns an iterator that contains only those elements of this iterator which match a given predicate
     *
     * @param f the filtering predicate
     * @return the filtered iterator
     * @see ACollectionOps#filter(Predicate)
     */
    AIterator<T> filter(Predicate<T> f);

    /**
     * Returns an iterator that contains only those elements of this iterator which <em>do not</em> match a given predicate
     *
     * @param f the filtering predicate
     * @return the filtered iterator
     * @see ACollectionOps#filterNot(Predicate)
     */
    default AIterator<T> filterNot(Predicate<T> f) {
        return filter(f.negate());
    }

    /**
     * Returns an iterator that is both filtered by a predicate and transformed by a transformation function. This method is equivalent to
     *  (though more efficient than) {@code this.filter(filter).map(f)}.
     *
     * @param filter the filtering predicate
     * @param f      the transformation function
     * @param <U>    the resulting iterator's element type
     * @return an iterator that is both filtered and transformed
     * @see ACollectionOps#collect(Predicate, Function)
     */
    default <U> AIterator<U> collect(Predicate<T> filter, Function<T,U> f) {
        return filter(filter).map(f);
    }

    /**
     * Returns the first element of {@link #collect(Predicate, Function)} (if any), or {@link AOption#none()} if no element passes the
     *  filter.
     *
     * @param filter the filtering predicate
     * @param f      the transformation function
     * @param <U>    the resulting element's type
     * @return the first element of {@code this.collect(filter, f)}
     * @see ACollectionOps#collectFirst(Predicate, Function)
     */
    default <U> AOption<U> collectFirst(Predicate<T> filter, Function<T,U> f) {
        final AIterator<U> it = collect(filter, f);
        if (it.hasNext())
            return AOption.some(it.next());
        return AOption.none();
    }

    /**
     * Skips the next {@code n} elements of this iterator.
     *
     * <p> If the iterator has fewer than {@code n} elements, that is silently ignored without throwing an exception.
     *
     * @param n the number of elements to drop
     * @return this iterator without its next {@code n} elements
     * @see AList#drop(int)
     */
    default AIterator<T> drop(int n) {
        for (int i=0; i<n; i++) {
            if (!hasNext()) break;
            next();
        }
        return this;
    }

    /**
     * Returns the first element matching a given predicate (if any), or {@link AOption#none()} if this iterator is exhausted without
     *  finding a match.
     *
     * @param f the predicate against which elements are matched
     * @return the first matching element
     * @see ACollectionOps#find(Predicate)
     */
    default AOption<T> find(Predicate<T> f) {
        while(hasNext()) {
            final T o = next();
            if (f.test(o)) return AOption.some(o);
        }
        return AOption.none();
    }

    /**
     * Returns true if and only if all of this iterator's elements match a given predicate.
     *
     * @param f the predicate all elements must match
     * @return true if and only if all of this iterator's elements match the predicate
     * @see ACollectionOps#forall(Predicate)
     */
    default boolean forall(Predicate<T> f) {
        while(hasNext()) {
            if (! f.test(next())) return false;
        }
        return true;
    }

    /**
     * Returns true if and only if at least one of this iterator's elements matches a given predicate.
     *
     * @param f the predicate against which elements are matched
     * @return true if at least one of this iterator's elements matches the predicate
     * @see ACollectionOps#exists(Predicate)
     */
    default boolean exists(Predicate<T> f) {
        while(hasNext()) {
            if (f.test(next())) return true;
        }
        return false;
    }

    /**
     * Returns the number of elements in this iterator that match a given predicate.
     *
     * @param f the predicate against which elements are matched
     * @return the number of matching elements
     * @see ACollectionOps#count(Predicate)
     */
    default int count(Predicate<T> f) {
        int result = 0;
        while(hasNext()) {
            if (f.test(next()))
                result += 1;
        }
        return result;
    }

    /**
     * "reduces" the elements of this iterator to a single value of the same type using a function that takes two values of the same type
     *  and returns their "combination".
     *
     * <p> One simple example is summing up the numbers in a collection: {@code AVector.of(2, 3, 4).iterator().reduce((a,b) -> a+b)} first
     *      calculates the sum of 2 and 3 (first call of reduction function), and then calls the function again with 5 (the result of the
     *      first call) and 4 (the remaining element), returning a total of {@code 9}.
     *
     * <p> If this iterator has only one element, that element ist returned. If the iterator is exhausted, a {@link java.util.NoSuchElementException}
     *      is thrown
     *
     * @throws java.util.NoSuchElementException if this iterator is exhausted
     * @param f the function to combine two values into one
     * @return the single value that results from passing all values to the reduction function
     * @see ACollectionOps#reduce(BiFunction)
     */
    default T reduce(BiFunction<T,T,T> f) {
        if (! hasNext()) throw new NoSuchElementException();

        final T first = next();
        if (! hasNext()) return first;

        final T second = next();
        T result = f.apply(first, second);

        while (hasNext()) {
            result = f.apply(result, next());
        }
        return result;
    }

    /**
     * Reduces this iterator's elements into a single value through pairwise combination. This is the same as {@link #reduce(BiFunction)}
     *  except that the result is wrapped in an {@link AOption}, so calling this on an exhausted iterator yields {@link AOption#none()}
     *  instead of throwing an exception.
     *
     * @param f the function to combine two values into one
     * @return the single value that results from passing all values to the reduction function
     * @see #reduce(BiFunction)
     * @see ACollectionOps#reduceOption(BiFunction)
     */
    default AOption<T> reduceOption(BiFunction<T,T,T> f) {
        if (! hasNext()) return AOption.none();
        return AOption.some(reduce(f));
    }

    /**
     * Aggregates this iterator's elements into a single element of a (potentially) different type, starting with a "zero" element
     *  passed in as a parameter. For each element, a folding function is called with the intermediate result and the new element, producing
     *  the next intermediate result.
     *
     * <p> For example, {@code AVector.of(1, 2, 3).iterator().fold("Numbers:", (agg,el) -> agg + " " + el)} produces the string {@code "Numbers: 1 2 3"}.
     *
     * @param zero the initial value that is used as the "previous" result for the first element, and is the overall result for an exhausted iterator
     * @param f    the aggregation function that combines the previous result and an element into the next result
     * @param <U>  the result type, which may be different from this iterator's element type
     * @return the aggregated result
     * @see ACollectionOps#fold(Object, BiFunction)
     */
    default <U> U fold(U zero, BiFunction<U,T,U> f) {
        U result = zero;
        while(hasNext()) {
            result = f.apply(result, next());
        }
        return result;
    }

    /**
     * Finds and returns the element which is smallest as determined by natural order. This method assumes that elements implement
     *  {@link Comparable}.
     *
     * @throws java.util.NoSuchElementException if this iterator is exhausted
     * @throws ClassCastException if elements do not implement {@link Comparable}
     * @return this iterator's smallest element (relative to natural order)
     * @see ACollectionOps#min()
     */
    default T min() {
        final Comparator comparator = Comparator.naturalOrder();
        //noinspection unchecked
        return min((Comparator<T>) comparator);
    }

    /**
     * Finds and returns the element which is smallest as determined by the {@link Comparator} that is passed in.
     *
     * @param comparator the {@link Comparator} determining the elements' ordering
     * @throws java.util.NoSuchElementException if this iterator is exhausted
     * @return the smallest element
     * @see ACollectionOps#min(Comparator)
     */
    default T min(Comparator<? super T> comparator) {
        return reduce((a, b) -> comparator.compare(a, b) < 0 ? a : b);
    }

    /**
     * Finds and returns the element which is biggest as determined by natural order. This method assumes that elements implement
     *  {@link Comparable}.
     *
     * @throws java.util.NoSuchElementException if this iterator is exhausted
     * @throws ClassCastException if elements do not implement {@link Comparable}
     * @return this collection's biggest element (relative to natural order)
     * @see ACollectionOps#max()
     */
    default T max() {
        final Comparator comparator = Comparator.naturalOrder();
        //noinspection unchecked
        return max((Comparator<T>) comparator);
    }

    /**
     * Finds and returns the element which is biggest as determined by the {@link Comparator} that is passed in.
     *
     * @param comparator the {@link Comparator} determining the elements' ordering
     * @throws java.util.NoSuchElementException if this iterator is exhausted
     * @return the biggest element
     * @see ACollectionOps#max(Comparator)
     */
    default T max(Comparator<? super T> comparator) {
        return reduce((a, b) -> comparator.compare(a, b) > 0 ? a : b);
    }

    /**
     * Concatenates this iterator's elements, inserting an infix string between them.
     *
     * <p> For example, {@code AVector.of(1, 2, 3).iterator().mkString("|")} returns the resulting string {@code "1|2|3"}.
     *
     * @param infix the string inserted between two consecutive elements
     * @return a string with this iterator's concatenated elements
     */
    default String mkString(String infix) {
        return mkString("", infix, "");
    }

    /**
     * Concatenates this iterator's elements, inserting an infix string between them and adding a prefix and suffix before and after
     *  the result.
     *
     * <p> For example, {@code AVector.of(1, 2, 3).iterator().mkString("<", "|", ">")} returns the resulting string {@code "<1|2|3>"}.
     *
     * @param prefix the string that is prefixed to the result
     * @param infix the string inserted between two consecutive elements
     * @param suffix the string that is suffixed to the result
     * @return a string with this iterator's concatenated elements
     */
    default String mkString(String prefix, String infix, String suffix) {
        final StringBuilder sb = new StringBuilder(prefix);

        if (hasNext()) {
            sb.append(next());
            while (hasNext()) {
                sb.append(infix);
                sb.append(next());
            }
        }

        sb.append(suffix);
        return sb.toString();
    }

    /**
     * Returns an iterator that appends another iterator's elements to this iterator's elements. For example,
     *
     * <p> {@code AIterator<Integer> it1 = AVector.of(1, 2, 3).iterator();}
     * <p> {@code AIterator<Integer> it2 = AVector.of(9, 8, 7).iterator();}
     * <p> {@code AIterator<Integer> itAll = it1.concat(it2); // 1, 2, 3, 9, 8, 7}
     *
     * <p> The resulting iterator pulls elements from the input iterators lazily; if elements are read from them directly after concat
     *  was called, they will not be visible in the concatenated iterator, which may be highly surprising behavior. Therefore it is
     *  strongly recommended (but not enforced) that both input iterators are discarded and only the concatenated iterator used.
     *
     * @param other the other iterator whose elements are appended
     * @return a new iterator with this iterator's elements followed by the other iterator's elements
     */
    default AIterator<T> concat(Iterator<? extends T> other) {
        return AIterator.concat(this, other);
    }

    /**
     * Returns an iterator that combines a list of iterators into a single iterator by returning their elements sequentially. For example,
     *
     * <p> {@code AIterator<Integer> it = AIterator.concat (}
     * <p> &nbsp;&nbsp; {@code AIterator.single(1),}
     * <p> &nbsp;&nbsp; {@code AVector.of(2, 3, 5).iterator(),}
     * <p> &nbsp;&nbsp; {@code AIterator.single(88)}
     * <p> {@code ); // 1, 2, 3, 5, 88}
     *
     * <p> The resulting iterator pulls elements from the input iterators lazily; if elements are read from them directly after concat
     *  was called, they will not be visible in the concatenated iterator, which may be highly surprising behavior. Therefore it is
     *  strongly recommended (but not enforced) that input iterators are discarded and only the concatenated iterator used.
     *
     * @param inner the iterators whose elements are concatenated
     * @param <T> the iterators' element type
     * @return a new iterator with all inner iterators' elements
     */
    @SafeVarargs
    static <T> AIterator<T> concat(Iterator<? extends T>... inner) {
        return new AbstractAIterator.ConcatIterator<>(inner);
    }
}
