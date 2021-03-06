package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AList;
import com.ajjpj.acollections.AbstractAIterator;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AListDefaults;
import com.ajjpj.acollections.internal.AListIteratorWrapper;
import com.ajjpj.acollections.util.AOption;

import java.io.Serializable;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;


/**
 * This class wraps any {@link java.util.List} as an {@link AList}. All modifying operations - both those from {@link java.util.List} and
 *  those added by {@link AList} - write through to the wrapped list.
 *
 * <p> This is a simple way to start using a-collections: Wrap an existing {@link java.util.List} to add a rich API while maintaining
 *  100% backwards compatibility: operations on the wrapper are write-through, i.e. all changes are applied to the underlying {@code List}.
 *  This class makes it simple to use {@link AList}'s rich API on any list in an ad-hoc fashion.
 *
 * <p> The wrapped list with all modifications applied to it can always be retrieved by calling {@link #getInner()}, though there is
 *  usually no reason for unwrapping: {@code AMutableListWrapper} implements {@link java.util.List}, so any method accepting
 *  {@link java.util.List} will also accept an {@code AMutableListWrapper} as is.
 *
 * @param <T> the list's element type.
 */
public class AMutableListWrapper<T> implements AListDefaults<T, AMutableListWrapper<T>>, Serializable {
    private List<T> inner;

    /**
     * This factory method wraps an arbitrary (typically mutable) {@link java.util.List} in an {@link AMutableListWrapper}.
     *  This is a simple way to start using a-collections: Wrap an existing {@code List} to add a rich API while maintaining 100% backwards
     *  compatibility: operations on the wrapper are write-through, i.e. all changes are applied to the underlying {@code List}.
     *
     * @param inner the List being wrapped
     * @param <T> the List's element type
     * @return the wrapped List
     */
    public static <T> AMutableListWrapper<T> wrap(List<T> inner) {
        if (inner instanceof AMutableListWrapper) return (AMutableListWrapper<T>) inner;
        return new AMutableListWrapper<>(inner);
    }

    private AMutableListWrapper (List<T> inner) {
        this.inner = Objects.requireNonNull(inner);
    }

    /**
     * Returns the wrapped list to which all modifications were applied.
     *
     * NB: AMutableListWrapper implements {@link java.util.List}, so usually there is no reason to unwrap it. Any API accepting
     *  {@link java.util.List} accepts an {@link AMutableListWrapper} as is.
     *
     * @return the wrapped list
     */
    public List<T> getInner() {
        return inner;
    }

    /**
     * Convenience method for creating an empty list. For creating a list with known elements, calling one of the {@code of}
     *  factory methods is a more concise alternative.
     *
     * @param <T> the new list's element type
     * @return an empty list
     */
    public static <T> AMutableListWrapper<T> empty() {
        return new AMutableListWrapper<>(new ArrayList<>());
    }

    /**
     * Creates a new list based on an iterator's elements.
     *
     * @param it the iterator from which the new list is initialized
     * @param <T> the list's element type
     * @return the new list
     */
    public static <T> AMutableListWrapper<T> fromIterator (Iterator<T> it) {
        return AMutableListWrapper.<T>builder().addAll(it).build();
    }

    /**
     * Creates a new list based on an Iterable's elements.
     *
     * @param that the Iterable from which the new list is initialized
     * @param <T> the list's element type
     * @return the new list
     */
    public static <T> AMutableListWrapper<T> from (Iterable<T> that) {
        return AMutableListWrapper.<T>builder().addAll(that).build();
    }

    /**
     * Creates a new list based on an array's elements.
     *
     * @param that the array from which the new list is initialized
     * @param <T> the list's element type
     * @return the new list
     */
    public static <T> AMutableListWrapper<T> from (T[] that) {
        return AMutableListWrapper.<T>builder().addAll(that).build();
    }

    /**
     * This is an alias for {@link #empty()} for consistency with Java 9 conventions - it creates an empty list.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> list instance - that is the
     *  whole point of class {@link AMutableListWrapper}. If you want immutable lists, use {@link AList#of()}.
     *
     * @param <T> the new list's element type
     * @return an empty list
     */
    public static <T> AMutableListWrapper<T> of() {
        return empty();
    }

    /**
     * Convenience factory method creating a list with exactly one element.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> list instance - that is the
     *  whole point of class {@link AMutableListWrapper}. If you want immutable lists, use {@link AList#of()}.
     *
     * @param o the single element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new list
     */
    public static <T> AMutableListWrapper<T> of(T o) {
        return AMutableListWrapper.<T>builder().add(o).build();
    }

    /**
     * Convenience factory method creating a list with two elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> list instance - that is the
     *  whole point of class {@link AMutableListWrapper}. If you want immutable lists, use {@link AList#of()}.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new list
     */
    public static <T> AMutableListWrapper<T> of(T o1, T o2) {
        return AMutableListWrapper.<T>builder().add(o1).add(o2).build();
    }

    /**
     * Convenience factory method creating a list with three elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> list instance - that is the
     *  whole point of class {@link AMutableListWrapper}. If you want immutable lists, use {@link AList#of()}.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param o3 the third element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new list
     */
    public static <T> AMutableListWrapper<T> of(T o1, T o2, T o3) {
        return AMutableListWrapper.<T>builder().add(o1).add(o2).add(o3).build();
    }

    /**
     * Convenience factory method creating a list with four elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> list instance - that is the
     *  whole point of class {@link AMutableListWrapper}. If you want immutable lists, use {@link AList#of()}.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param o3 the third element for the new list
     * @param o4 the fourth element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new list
     */
    public static <T> AMutableListWrapper<T> of(T o1, T o2, T o3, T o4) {
        return AMutableListWrapper.<T>builder().add(o1).add(o2).add(o3).add(o4).build();
    }

    /**
     * Convenience factory method creating a list with more than four elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> list instance - that is the
     *  whole point of class {@link AMutableListWrapper}. If you want immutable lists, use {@link AList#of()}.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param o3 the third element for the new list
     * @param o4 the fourth element for the new list
     * @param o5 the fifth element for the new list
     * @param others the (variable number of) additional elements
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new list
     */
    @SafeVarargs public static <T> AMutableListWrapper<T> of(T o1, T o2, T o3, T o4, T o5, T... others) {
        return AMutableListWrapper
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .add(o4)
                .add(o5)
                .addAll(others)
                .build();
    }


    @Override public <U> ACollectionBuilder<U, AMutableListWrapper<U>> newBuilder () {
        return builder();
    }

    @Override public AMutableListWrapper<T> toMutableList () {
        return this;
    }

    @Override
    public AOption<T> lastOption () {
        if (isEmpty()){
            return AOption.none();
        }
        return AOption.of(inner.get(inner.size()-1));
    }

    @Override
    public boolean contains (Object o) {
        return inner.contains(o);
    }

    @Override public <U> AMutableListWrapper<U> flatMap (Function<T, Iterable<U>> f) {
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }

    @Override public Object[] toArray () {
        return ACollectionSupport.toArray(this);
    }
    @Override public <T1> T1[] toArray (T1[] a) {
        return ACollectionSupport.toArray(this, a);
    }

    @Override public AMutableListWrapper<T> prepend (T o) {
        inner.add(0, o);
        return this;
    }
    @Override public AMutableListWrapper<T> append (T o) {
        inner.add(o);
        return this;
    }

    @Override public AMutableListWrapper<T> updated (int idx, T o) {
        inner.set(idx, o);
        return this;
    }

    @Override public AMutableListWrapper<T> concat(Iterator<? extends T> that) {
        while (that.hasNext()) {
            inner.add(that.next());
        }
        return this;
    }
    @Override public AMutableListWrapper<T> concat(Iterable<? extends T> that) {
        return concat(that.iterator());
    }

    @Override public AMutableListWrapper<T> patch (int idx, List<T> patch, int numReplaced) {
        for (int i = 0; i<numReplaced; i++)
            inner.remove(idx);
        inner.addAll(idx, patch);
        return this;
    }

    @Override public AMutableListWrapper<T> takeRight (int n) {
        if (n <= 0)
            clear();
        else if (n < size()) {
            inner = AMutableListWrapper.<T>builder()
                    .addAll(inner.listIterator(size()-n))
                    .build()
                    .getInner();
        }
        return this;
    }
    @Override public AMutableListWrapper<T> dropRight (int n) {
        for (int i=0; i<n; i++) {
            if (inner.size() > 0) inner.remove(size()-1);
        }
        return this;
    }

    @Override public AIterator<T> reverseIterator () {
        return new AbstractAIterator<T>() {
            final ListIterator<T> it = inner.listIterator(size());

            @Override public boolean hasNext () {
                return it.hasPrevious();
            }

            @Override public T next () {
                return it.previous();
            }
        };
    }

    @Override public AMutableListWrapper<T> sorted (Comparator<? super T> comparator) {
        inner.sort(comparator);
        return this;
    }

    @Override public AMutableListWrapper<T> shuffled (Random r) {
        Collections.shuffle(inner, r);
        return this;
    }

    @Override public <U> AList<U> collect (Predicate<T> filter, Function<T, U> f) {
        return ACollectionSupport.collect(newBuilder(), this, filter, f);
    }

    @Override public AMutableListWrapper<T> take (int n) {
        return dropRight(size() - n);
    }
    @Override public AMutableListWrapper<T> drop (int n) {
        for (int i=0; i<n; i++) {
            if (inner.size() > 0) inner.remove(0);
        }
        return this;
    }

    @Override public AIterator<T> iterator () {
        return new AListIteratorWrapper<>(inner.listIterator());
    }

    @Override public <U> AMutableListWrapper<U> map (Function<T, U> f) {
        return ACollectionSupport.map(newBuilder(), this, f);
    }


    @Override public boolean addAll (int index, Collection<? extends T> c) {
        return inner.addAll(index, c);
    }

    @Override public T get (int index) {
        return inner.get(index);
    }

    @Override public T set (int index, T element) {
        return inner.set(index, element);
    }

    @Override public void add (int index, T element) {
        inner.add(index, element);
    }

    @Override public T remove (int index) {
        return inner.remove(index);
    }

    @Override public int indexOf (Object o) {
        return inner.indexOf(o);
    }

    @Override public int lastIndexOf (Object o) {
        return inner.lastIndexOf(o);
    }

    @Override public AListIteratorWrapper<T> listIterator () {
        return new AListIteratorWrapper<>(inner.listIterator());
    }

    @Override public AListIteratorWrapper<T> listIterator (int index) {
        return new AListIteratorWrapper<>(inner.listIterator());
    }

    @Override public List<T> subList (int fromIndex, int toIndex) {
        return new AMutableListWrapper<>(inner.subList(fromIndex,toIndex ));
    }

    @Override public int size () {
        return inner.size();
    }

    @Override public boolean isEmpty () {
        return inner.isEmpty();
    }

    @Override public boolean add (T t) {
        return inner.add(t);
    }

    @Override public boolean remove (Object o) {
        return inner.remove(o);
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

    @Override public void replaceAll (UnaryOperator<T> operator) {
        inner.replaceAll(operator);
    }
    @Override public void sort (Comparator<? super T> c) {
        inner.sort(c);
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

    @Override public boolean equals(Object obj) {
        return inner.equals(obj);
    }

    @Override public int hashCode() {
        return inner.hashCode();
    }

    /**
     * Returns a {@link Collector} to collect {@link java.util.stream.Stream} elements into an AMutableListWrapper.
     *
     * @param <T> the stream's element type
     * @return a {@link Collector} to collect a stream's elements into an AMutableListWrapper
     */
    public static <T> Collector<T, Builder<T>, AMutableListWrapper<T>> streamCollector() {
        final Supplier<Builder<T>> supplier = AMutableListWrapper::builder;
        final BiConsumer<Builder<T>, T> accumulator = AMutableListWrapper.Builder::add;
        final BinaryOperator<Builder<T>> combiner = (b1, b2) -> {
            b1.addAll(b2.build());
            return b1;
        };
        final Function<Builder<T>, AMutableListWrapper<T>> finisher = Builder::build;

        return Collector.of(supplier, accumulator, combiner, finisher);
    }

    /**
     * Returns a new {@link ACollectionBuilder} for building an AMutableListWrapper efficiently and in a generic manner.
     *
     * @param <T> the builder's element type
     * @return an new {@link ACollectionBuilder}
     */
    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> implements ACollectionBuilder<T, AMutableListWrapper<T>> {
        private final List<T> inner = new ArrayList<>();

        @Override public ACollectionBuilder<T, AMutableListWrapper<T>> add (T el) {
            inner.add(el);
            return this;
        }

        @Override public AMutableListWrapper<T> build () {
            return AMutableListWrapper.wrap(inner);
        }
    }
}
