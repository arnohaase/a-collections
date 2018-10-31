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


public class AMutableArrayWrapper<T> implements AList<T>, AListDefaults<T, AMutableArrayWrapper<T>>, Serializable {
    private T[] inner;

    public static <T> AMutableArrayWrapper<T> empty() {
        //noinspection unchecked
        return new AMutableArrayWrapper(new Object[0]);
    }

    public static <T> AMutableArrayWrapper<T> wrap(T[] inner) {
        return new AMutableArrayWrapper<>(inner);
    }

    public static <T> AMutableArrayWrapper<T> fromIterator (Iterator<T> it) {
        return AMutableArrayWrapper.<T>builder().addAll(it).build();
    }
    public static <T> AMutableArrayWrapper<T> from (Iterable<T> iterable) {
        return AMutableArrayWrapper.<T>builder().addAll(iterable).build();
    }
    public static <T> AMutableArrayWrapper<T> from (T[] that) {
        return AMutableArrayWrapper.<T>builder().addAll(that).build();
    }

    public static <T> AMutableArrayWrapper<T> of() {
        return empty();
    }
    public static <T> AMutableArrayWrapper<T> of(T o1) {
        return AMutableArrayWrapper.<T>builder().add(o1).build();
    }
    public static <T> AMutableArrayWrapper<T> of(T o1, T o2) {
        return AMutableArrayWrapper.<T>builder().add(o1).add(o2).build();
    }
    public static <T> AMutableArrayWrapper<T> of(T o1, T o2, T o3) {
        return AMutableArrayWrapper.<T>builder().add(o1).add(o2).add(o3).build();
    }
    public static <T> AMutableArrayWrapper<T> of(T o1, T o2, T o3, T o4) {
        return AMutableArrayWrapper.<T>builder().add(o1).add(o2).add(o3).add(o4).build();
    }
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


    //TODO static factories


    private AMutableArrayWrapper (T[] inner) {
        this.inner = inner;
    }

    @Override public <U> ACollectionBuilder<U, AMutableArrayWrapper<U>> newBuilder () {
        return builder();
    }

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
