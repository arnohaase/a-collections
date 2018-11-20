package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AList;
import com.ajjpj.acollections.AbstractAIterator;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AListDefaults;
import com.ajjpj.acollections.internal.AListSupport;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * ALinkedList is a class for immutable singly-linked lists representing ordered collections. It is optimal for last-in-first-out (LIFO),
 *  stack-like access patterns. If you need another access pattern, for example, random access or FIFO, {@link AVector} is probably the
 *  better alternative.
 *
 * <p> Since this is an immutable class, it does not support modifying methods from {@link java.util.List}: Those methods return
 *  {@code boolean} or a previous element, but in order to "modify" an immutable collection, they would need to return the new collection
 *  instance.
 *
 * <p> Performance Considerations:
 * <ul>
 *     <li> Time: ALinkedList has O(1) prepend and head/tail access. Most other operations are O(n) on the number of elements in the list.
 *           This includes the index-based lookup of elements, {@link #size()}, {@link #append(Object)} and {@link #reverse()}.
 *     <li> Space: ALinkedList implements "structural sharing" of the tail list. This means that many operations are either zero- or
 *           constant-memory cost.
 *           <p> {@code AList<Integer> mainList = ALinkedList.of(3, 2, 1);}
 *           <p> {@code AList<Integer> with4    = mainList.prepend(4);  // re-uses mainList, costs only one additional link for '4'}
 *           <p> {@code AList<Integer> with42   = mainList.prepend(42); // also re-uses mainList, costs only one additional link for '42'}
 *           <p> {@code AList<Integer> shorter  = mainList.tail();      // costs nothing because the tail sublist is already contained in mainList}
 * </ul>
 *
 * <p> The functional list is characterized by persistence and structural sharing, thus offering considerable
 *       performance and space consumption benefits in some scenarios if used correctly.
 *       However, note that objects having multiple references into the same functional list (that is,
 *       objects that rely on structural sharing), will be serialized and deserialized with multiple lists, one for
 *       each reference to it. I.e. structural sharing is lost after serialization/deserialization.
 *
 * <p> Implementation note: This class is a port of Scala's standard library 'List'.
 *
 * @param <T> the list's element type
 */
public abstract class ALinkedList<T> extends AbstractImmutableCollection<T> implements AListDefaults<T, ALinkedList<T>>, Serializable {
    /**
     * Creates a new {@link ALinkedList} based on an array's elements.
     *
     * @param that the array from which the new list is initialized
     * @param <T> the list's element type
     * @return the new list
     */
    public static <T> ALinkedList<T> from(T[] that) {
        return fromIterator(Arrays.asList(that).iterator());
    }

    /**
     * Creates a new {@link ALinkedList} based on an Iterable's elements.
     *
     * @param that the Iterable from which the new list is initialized
     * @param <T> the list's element type
     * @return the new list
     */
    public static <T> ALinkedList<T> from(Iterable<T> that) {
        return fromIterator(that.iterator());
    }

    /**
     * Creates a new {@link ALinkedList} based on an iterator's elements.
     *
     * @param it the iterator from which the new list is initialized
     * @param <T> the list's element type
     * @return the new list
     */
    public static <T> ALinkedList<T> fromIterator(Iterator<T> it) {
        return ALinkedList
                .<T>builder()
                .addAll(it)
                .build();
    }

    /**
     * This is an alias for {@link #empty()} for consistency with Java 9 conventions - it creates an empty {@link ALinkedList}.
     *
     * @param <T> the new list's element type
     * @return an empty {@link ALinkedList}
     */
    public static <T> ALinkedList<T> of() {
        return empty();
    }

    /**
     * Convenience factory method creating an {@link ALinkedList} with exactly one element.
     *
     * @param o the single element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link ALinkedList}
     */
    public static <T> ALinkedList<T> of(T o) {
        return ALinkedList.<T>empty().prepend(o);
    }

    /**
     * Convenience factory method creating an {@link ALinkedList} with two elements.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link ALinkedList}
     */
    public static <T> ALinkedList<T> of(T o1, T o2) {
        return ALinkedList.<T>empty().prepend(o2).prepend(o1);
    }

    /**
     * Convenience factory method creating an {@link ALinkedList} with three elements.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param o3 the third element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link ALinkedList}
     */
    public static <T> ALinkedList<T> of(T o1, T o2, T o3) {
        return ALinkedList.<T>empty().prepend(o3).prepend(o2).prepend(o1);
    }

    /**
     * Convenience factory method creating an {@link ALinkedList} with four elements.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param o3 the third element for the new list
     * @param o4 the fourth element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link ALinkedList}
     */
    public static <T> ALinkedList<T> of(T o1, T o2, T o3, T o4) {
        return ALinkedList.<T>empty().prepend(o4).prepend(o3).prepend(o2).prepend(o1);
    }

    /**
     * Convenience factory method creating an {@link ALinkedList} with more than four elements.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param o3 the third element for the new list
     * @param o4 the fourth element for the new list
     * @param o5 the fifth element for the new list
     * @param others the (variable number of) additional elements
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link ALinkedList}
     */
    @SafeVarargs public static <T> ALinkedList<T> of(T o1, T o2, T o3, T o4, T o5, T... others) {
        return ALinkedList
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .add(o4)
                .add(o5)
                .addAll(others)
                .build();
    }

    /**
     * Convenience method for creating an empty {@link ALinkedList}. For creating a list with known elements, calling one of the {@code of}
     *  factory methods is a more concise alternative.
     *
     * @param <T> the new set's element type
     * @return an empty {@link ALinkedList}
     */
    public static <T> ALinkedList<T> empty() {
        //noinspection unchecked
        return Nil.INSTANCE;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override public boolean equals (Object o) {
        return AListSupport.equals(this, o);
    }
    @Override public int hashCode() {
        return AListSupport.hashCode(this);
    }

    @Override public <U> Builder<U> newBuilder () {
        return builder();
    }

    @Override public ALinkedList<T> prepend(T o) {
        return new HeadTail<>(o, this);
    }

    @Override public ALinkedList<T> append(T o) {
        return ALinkedList
                .<T>builder()
                .addAll(this)
                .add(o)
                .build();
    }

    @Override public ALinkedList<T> concat (Iterator<? extends T> that) {
        return ALinkedList
                .<T>builder()
                .addAll(this)
                .addAll(that)
                .build();
    }
    @Override public AList<T> concat (Iterable<? extends T> that) {
        return concat(that.iterator());
    }

    @Override public ALinkedList<T> updated (int idx, T o) {
        if (idx < 0) throw new IndexOutOfBoundsException();
        try {
            final Builder<T> builder = new Builder<>();

            ALinkedList<T> l = this;
            for(int i=0; i<idx; i++) {
                builder.add(l.head());
                l = l.tail();
            }

            builder.add(o);
            l = l.tail();

            for (T el: l)
                builder.add(el);
            return builder.build();
        }
        catch (NoSuchElementException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override public T last () {
        if (isEmpty()) throw new NoSuchElementException();
        ALinkedList<T> l = this;
        while(l.tail().nonEmpty())
            l = l.tail();
        return l.head();
    }

    @Override public ALinkedList<T> init () {
        if(isEmpty()) throw new NoSuchElementException();
        return dropRight(1);
    }

    @Override public ALinkedList<T> take (int n) {
        if (n < 0) return empty();
        final Builder<T> builder = new Builder<>();
        ALinkedList<T> l = this;
        for(int i=0; i<n; i++) {
            if (l.isEmpty()) break;
            builder.add(l.head());
            l = l.tail();
        }
        return builder.build();
    }

    @Override public ALinkedList<T> takeRight (int n) {
        if (n < 0) throw new IllegalArgumentException();
        return drop(size() - n);
    }

    @Override public ALinkedList<T> drop (int n) {
        if (n < 0) return this; //throw new IllegalArgumentException();
        ALinkedList<T> l = this;
        for (int i=0; i<n; i++) {
            if(l.isEmpty()) return empty();
            l = l.tail();
        }
        return l;
    }

    @Override public ALinkedList<T> dropRight (int n) {
        if (n < 0) return this;
        return take(size() - n);
    }

    @Override public ALinkedList<T> dropWhile (Predicate<T> f) {
        // this is a more efficient implementation than the generic, builder-based code from AListDefaults
        ALinkedList<T> l = this;
        while (l.nonEmpty() && f.test(l.head()))
            l = l.tail();
        return l;
    }

    @Override public <U> ALinkedList<U> collect (Predicate<T> filter, Function<T, U> f) {
        return ACollectionSupport.collect(newBuilder(), this, filter, f);
    }

    @Override public ALinkedList<T> reverse () {
        // This is a more efficient implementation than the generic builder-based code from AListDefaults
        ALinkedList<T> result = empty();
        for (T o: this) {
            result = result.prepend(o);
        }
        return result;
    }

    @Override public AIterator<T> reverseIterator () {
        return toVector().reverseIterator();
    }

    @Override public ALinkedList<T> subList (int fromIndex, int toIndex) {
        if (fromIndex < 0) throw new IndexOutOfBoundsException();
        final ALinkedList<T> result = drop(fromIndex).take(toIndex - fromIndex);
        if (result.size() != toIndex - fromIndex) throw new IndexOutOfBoundsException();
        return result;
    }

    @Override public boolean addAll (int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override public T get (int index) {
        ALinkedList<T> l = this;
        for (int i=0; i<index; i++) {
            l = l.tail();
        }
        return l.head();
    }

    @Override public T set (int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override public void add (int index, Object element) {
        throw new UnsupportedOperationException();
    }

    @Override public T remove (int index) {
        throw new UnsupportedOperationException();
    }

    @Override public int lastIndexOf (Object o) {
        // this is inefficient but more efficient than traversing in reverse order...
        int idx=0;
        int result = -1;
        for (T candidate: this) {
            if (Objects.equals(candidate, o))
                result = idx;
            idx += 1;
        }
        return result;
    }

    public abstract ALinkedList<T> tail();

    @Override public <U> ALinkedList<U> map (Function<T, U> f) {
        return ACollectionSupport.map(newBuilder(), this, f);
    }
    @Override public <U> ALinkedList<U> flatMap(Function<T, Iterable<U>> f) {
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }

    @Override public ALinkedList<T> toLinkedList() {
        return this;
    }

    @Override public String toString () {
        return ACollectionSupport.toString(ALinkedList.class, this);
    }

    private static class HeadTail<T> extends ALinkedList<T> {
        private final T head;
        private ALinkedList<T> tail; // mutable only during construction - this allows for some optimizations

        HeadTail (T head, ALinkedList<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        @Override public boolean isEmpty () {
            return false;
        }

        @Override public T head () {
            return head;
        }

        @Override public ALinkedList<T> tail () {
            return tail;
        }

        @Override public AIterator<T> iterator () {
            return new AbstractAIterator<T>() {
                private ALinkedList<T> next = HeadTail.this;

                @Override public boolean hasNext () {
                    return next.nonEmpty();
                }

                @Override public T next () {
                    if (next.isEmpty()) throw new NoSuchElementException();
                    final T result = next.head();
                    next = next.tail();
                    return result;
                }
            };
        }

        @Override public int size () {
            int result = 1;
            ALinkedList<T> l = this;
            while (l.tail().nonEmpty()) {
                l = l.tail();
                result += 1;
            }
            return result;
        }
    }

    private static class Nil<T> extends ALinkedList<T> {
        static final Nil INSTANCE = new Nil();

        private Nil() {
        }

        @Override public boolean isEmpty () {
            return true;
        }

        @Override public T head () {
            throw new NoSuchElementException();
        }
        @Override public ALinkedList<T> tail () {
            throw new NoSuchElementException();
        }
        @Override public AIterator<T> iterator () {
            return AIterator.empty();
        }

        @Override public <U> ALinkedList<U> map (Function<T, U> f) {
            //noinspection unchecked
            return (ALinkedList<U>) this;
        }

        @Override public int size () {
            return 0;
        }

        @Override public boolean contains (Object o) {
            return false;
        }

        private Object readResolve() {
            return INSTANCE;
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> implements ACollectionBuilder<T, ALinkedList<T>> {
        private ALinkedList<T> result = empty();
        private HeadTail<T> last;
        private boolean wasBuilt=false;

        public Builder<T> add(T o) {
            if (wasBuilt) throw new IllegalStateException();
            if(result.isEmpty()) {
                last = new HeadTail<>(o, result);
                result = last;
            }
            else {
                final HeadTail<T> newLast = new HeadTail<>(o, last.tail());
                last.tail = newLast;
                last = newLast;
            }
            return this;
        }
        public Builder<T> addAll(Iterator<? extends T> it) {
            while(it.hasNext()) add(it.next());
            return this;
        }
        public Builder<T> addAll(Iterable<? extends T> coll) {
            return addAll(coll.iterator());
        }


        public ALinkedList<T> build() {
            if (wasBuilt) throw new IllegalStateException();
            wasBuilt = true;
            return result;
        }
    }
}
