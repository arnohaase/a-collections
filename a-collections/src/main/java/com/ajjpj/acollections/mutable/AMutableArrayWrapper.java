package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AList;
import com.ajjpj.acollections.AbstractAIterator;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AListDefaults;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * This class wraps an array, providing {@link AList}'s rich API for reading and modifying it. While it may feel strange to work with
 *  arrays through a 'regular' collection API, there is really no reason not to when you think about it. You can always extract the
 *  wrapped array by calling {@link #getInner()}
 *
 * <p> All modifications done through the API write through to the underlying array. Calls like {@link List#set(int, Object)} or
 *  {@link AList#updated(int, Object)} that do not affect the size of the array just modify the existing array. Other operations like
 *  {@link AList#append(Object)} create a new array, copying the old array's data as needed. While that can be less efficient than using
 *  a 'real' collection, it is possible.
 *
 * <p> The reason for using arrays in the first place is typically that some API requires or returns them. To create an array, you can
 *  use one of the {@link #of()} factories or, for more complex cases, create a collection and then call {@link Collection#toArray()} on
 *  it, or use {@link #builder()} to build one.
 *
 * <p> When working with an array that was returned by an API, {@code AMutableArrayWrapper} really comes into its own: It allows you to
 *  use the range of {@link AList} operations without adding the overhead of converting the array to a regular collection.
 *
 * @param <T>
 */
public class AMutableArrayWrapper<T> implements AList<T>, AListDefaults<T, AMutableArrayWrapper<T>>, Serializable {
    private T[] inner;

    /**
     * Convenience method for creating an empty list. For creating a list with known elements, calling one of the {@code of}
     *  factory methods is a more concise alternative.
     *
     * @param <T> the new list's element type
     * @return an empty list
     */
    public static <T> AMutableArrayWrapper<T> empty() {
        //noinspection unchecked
        return new AMutableArrayWrapper(new Object[0]);
    }

    /**
     * This factory method wraps an arbitrary array in an {@link AMutableArrayWrapper}.
     *
     * @param inner the array being wrapped
     * @param <T> the array's element type
     * @return the wrapped array
     */
    public static <T> AMutableArrayWrapper<T> wrap(T[] inner) {
        return new AMutableArrayWrapper<>(inner);
    }

    /**
     * Creates a new list based on an iterator's elements.
     *
     * @param it the iterator from which the new list is initialized
     * @param <T> the list's element type
     * @return the new list
     */
    public static <T> AMutableArrayWrapper<T> fromIterator (Iterator<T> it) {
        return AMutableArrayWrapper.<T>builder().addAll(it).build();
    }

    /**
     * Creates a new list based on an Iterable's elements.
     *
     * @param that the Iterable from which the new list is initialized
     * @param <T> the list's element type
     * @return the new list
     */
    public static <T> AMutableArrayWrapper<T> from (Iterable<T> that) {
        return AMutableArrayWrapper.<T>builder().addAll(that).build();
    }

    /**
     * Creates a new list based on an array's elements. This method has the same signature as {@link #wrap(Object[])}, but it copies
     *  the original array's contents rather than just wrapping it.
     *
     * @param that the array from which the new list is initialized
     * @param <T> the list's element type
     * @return the new list
     */
    public static <T> AMutableArrayWrapper<T> from (T[] that) {
        return AMutableArrayWrapper.<T>builder().addAll(that).build();
    }

    /**
     * This is an alias for {@link #empty()} for consistency with Java 9 conventions - it creates an empty list.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> list instance - that is the
     *  whole point of class {@link AMutableArrayWrapper}. If you want immutable lists, use {@link AList#of()}.
     *
     * @param <T> the new list's element type
     * @return an empty list
     */
    public static <T> AMutableArrayWrapper<T> of() {
        return empty();
    }

    /**
     * Convenience factory method creating a list with exactly one element.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> list instance - that is the
     *  whole point of class {@link AMutableArrayWrapper}. If you want immutable lists, use {@link AList#of()}.
     *
     * @param o the single element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new list
     */
    public static <T> AMutableArrayWrapper<T> of(T o) {
        return AMutableArrayWrapper.<T>builder().add(o).build();
    }

    /**
     * Convenience factory method creating a list with two elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> list instance - that is the
     *  whole point of class {@link AMutableArrayWrapper}. If you want immutable lists, use {@link AList#of()}.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new list
     */
    public static <T> AMutableArrayWrapper<T> of(T o1, T o2) {
        return AMutableArrayWrapper.<T>builder().add(o1).add(o2).build();
    }

    /**
     * Convenience factory method creating a list with three elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> list instance - that is the
     *  whole point of class {@link AMutableArrayWrapper}. If you want immutable lists, use {@link AList#of()}.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param o3 the third element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new list
     */
    public static <T> AMutableArrayWrapper<T> of(T o1, T o2, T o3) {
        return AMutableArrayWrapper.<T>builder().add(o1).add(o2).add(o3).build();
    }

    /**
     * Convenience factory method creating a list with four elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> list instance - that is the
     *  whole point of class {@link AMutableArrayWrapper}. If you want immutable lists, use {@link AList#of()}.
     *
     * @param o1 the first element for the new list
     * @param o2 the second element for the new list
     * @param o3 the third element for the new list
     * @param o4 the fourth element for the new list
     * @param <T> the new list's element type (can often be inferred from the parameter by the compiler)
     * @return the new list
     */
    public static <T> AMutableArrayWrapper<T> of(T o1, T o2, T o3, T o4) {
        return AMutableArrayWrapper.<T>builder().add(o1).add(o2).add(o3).add(o4).build();
    }

    /**
     * Convenience factory method creating a list with more than four elements.
     *
     * <p> NB: Other than Java's 'of' methods in collection interfaces, this method creates a <em>mutable</em> list instance - that is the
     *  whole point of class {@link AMutableArrayWrapper}. If you want immutable lists, use {@link AList#of()}.
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
    @SafeVarargs public static <T> AMutableArrayWrapper<T> of(T o1, T o2, T o3, T o4, T o5, T... others) {
        return AMutableArrayWrapper
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .add(o4)
                .add(o5)
                .addAll(others)
                .build();
    }

    private AMutableArrayWrapper (T[] inner) {
        this.inner = inner;
    }

    @Override public <U> ACollectionBuilder<U, AMutableArrayWrapper<U>> newBuilder () {
        return builder();
    }

    /**
     * Returns the wrapped array to which all modifications were applied.
     *
     * @return the wrapped array
     */
    public T[] getInner() {
        return inner;
    }

    @Override public AMutableArrayWrapper<T> prepend (T o) {
        //noinspection unchecked
        final T[] result = (T[]) new Object[size()+1];
        System.arraycopy(inner, 0, result, 1, size());
        result[0] = o;
        inner = result;
        return this;
    }

    @Override public AMutableArrayWrapper<T> append (T o) {
        //noinspection unchecked
        final T[] result = (T[]) new Object[size()+1];
        System.arraycopy(inner, 0, result, 0, size());
        result[size()] = o;
        inner = result;
        return this;
    }

    @Override public AMutableArrayWrapper<T> concat (Iterator<? extends T> that) {
        inner = this.<T>newBuilder().addAll(this).addAll(that).build().inner;
        return this;
    }

    @Override public AMutableArrayWrapper<T> concat (Iterable<? extends T> that) {
        inner = this.<T>newBuilder().addAll(this).addAll(that).build().inner;
        return this;
    }

    @Override public AMutableArrayWrapper<T> updated (int idx, T o) {
        inner[idx] = o;
        return this;
    }

    @Override public T last () {
        if (isEmpty()) throw new NoSuchElementException();
        return inner[inner.length-1];
    }

    @Override public AMutableArrayWrapper<T> take (int n) {
        if (n >= size()) return this;

        final Object[] result = new Object[n];
        System.arraycopy(inner, 0, result, 0, n);
        //noinspection unchecked
        return new AMutableArrayWrapper(result);
    }

    @Override public AMutableArrayWrapper<T> takeRight (int n) {
        if (n >= size()) return this;

        final Object[] result = new Object[n];
        System.arraycopy(inner, size()-n, result, 0, n);
        //noinspection unchecked
        return new AMutableArrayWrapper(result);
    }

    @Override public AMutableArrayWrapper<T> drop (int n) {
        if (n >= size()) return empty();
        return takeRight(size()-n);
    }

    @Override public AMutableArrayWrapper<T> dropRight (int n) {
        if (n >= size()) return empty();
        return take(size()-n);
    }

    @Override public AMutableArrayWrapper<T> patch (int idx, List<T> patch, int numReplaced) {
        this.inner = AListDefaults.super.patch(idx, patch, numReplaced).inner;
        return this;
    }

    @Override public AIterator<T> iterator () {
        return new AbstractAIterator<T>() {
            int nextIdx=0;

            @Override public boolean hasNext () {
                return nextIdx < inner.length;
            }

            @Override public T next () {
                if (!hasNext()) throw new NoSuchElementException();
                return inner[nextIdx++];
            }
        };
    }

    @Override public AIterator<T> reverseIterator () {
        return new AbstractAIterator<T>() {
            int nextIdx = inner.length-1;

            @Override public boolean hasNext () {
                return nextIdx >= 0;
            }

            @Override public T next () {
                if (!hasNext()) throw new NoSuchElementException();
                return inner[nextIdx--];
            }
        };
    }

    @Override public <U> AMutableArrayWrapper<U> map (Function<T, U> f) {
        final Object[] result = new Object[size()];
        for (int i: indices()) result[i] = f.apply(inner[i]);
        //noinspection unchecked
        return new AMutableArrayWrapper(result);
    }

    @Override public <U> AMutableArrayWrapper<U> flatMap (Function<T, Iterable<U>> f) {
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }

    @Override public <U> AMutableArrayWrapper<U> collect (Predicate<T> filter, Function<T, U> f) {
        return ACollectionSupport.collect(newBuilder(), this, filter, f);
    }

    @Override public boolean isEmpty () {
        return size() == 0;
    }

    @Override public boolean addAll (int index, Collection<? extends T> c) {
        if (c.isEmpty()) return false;

        //noinspection unchecked
        final T[] result = (T[]) new Object[size() + c.size()];
        System.arraycopy(inner, 0, result, 0, index);

        final Iterator<? extends T> it = c.iterator();
        for (int i=0; i<c.size(); i++) result[index+i] = it.next();

        System.arraycopy(inner, index, result, index+c.size(), size()-index);
        inner = result;
        return true;
    }

    @Override public T get (int index) {
        return inner[index];
    }

    @Override public T set (int index, T element) {
        final T prev = get(index);
        inner[index] = element;
        return prev;
    }

    @Override public void add (int index, T element) {
        updated(index, element);
    }

    @Override public T remove (int index) {
        final T result = get(index);
        //noinspection unchecked
        final T[] newInner = (T[]) new Object[size()-1];
        System.arraycopy(inner, 0, newInner, 0, index);
        System.arraycopy(inner, index+1, newInner, index, size()-index-1);
        inner = newInner;
        return result;
    }

    @Override public AList<T> subList (int fromIndex, int toIndex) {
        //noinspection unchecked
        final T[] newInner = (T[]) new Object[toIndex-fromIndex]; //TODO potential for optimization
        System.arraycopy(inner, fromIndex, newInner, 0, newInner.length);
        return new AMutableArrayWrapper<>(newInner);
    }

    @Override public int size () {
        return inner.length;
    }

    @Override public Object[] toArray () {
        return inner;
    }

    @Override public <T1> T1[] toArray (T1[] a) {
        return ACollectionSupport.toArray(this, a);
    }

    @Override public boolean add (T t) {
        append(t);
        return true;
    }

    @Override public boolean remove (Object o) {
        final int idx = indexOf(o);
        if (idx == -1) return false;
        remove(idx);
        return true;
    }

    @Override public boolean addAll (Collection<? extends T> c) {
        if (c.isEmpty()) return false;

        //noinspection unchecked
        final T[] result = (T[]) new Object[size() + c.size()];
        System.arraycopy(inner, 0, result, 0, inner.length);

        final Iterator<? extends T> it = c.iterator();
        for (int i=0; i<c.size(); i++) result[inner.length+i] = it.next();

        inner = result;
        return true;
    }

    @Override public boolean removeAll (Collection<?> c) {
        final List<T> result = new ArrayList<>(this);
        if (result.removeAll(c)) {
            //noinspection unchecked
            inner = (T[]) result.toArray();
            return true;
        }
        else {
            return false;
        }
    }

    @Override public boolean retainAll (Collection<?> c) {
        final List<T> result = new ArrayList<>(this);
        if (result.retainAll(c)) {
            //noinspection unchecked
            inner = (T[]) result.toArray();
            return true;
        }
        else {
            return false;
        }
    }

    @Override public void clear () {
        //noinspection unchecked
        inner = (T[]) new Object[0];
    }

    @Override public String toString () {
        return ACollectionSupport.toString(AMutableArrayWrapper.class, this);
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override public boolean equals (Object o) {
        return Arrays.asList(inner).equals(o);
    }

    @Override public int hashCode () {
        //TODO extract to ACollectionSupport / AbstractMutableCollection
        int result = 1;
        for (T o: this)
            result = 31*result + Objects.hashCode(o);

        return result;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> implements ACollectionBuilder<T, AMutableArrayWrapper<T>> {
        private final ArrayList<T> l = new ArrayList<>();

        @Override public ACollectionBuilder<T, AMutableArrayWrapper<T>> add (T el) {
            l.add(el);
            return this;
        }

        @Override public AMutableArrayWrapper<T> build () {
            //noinspection unchecked
            return new AMutableArrayWrapper(l.toArray(new Object[0]));
        }
    }
}
