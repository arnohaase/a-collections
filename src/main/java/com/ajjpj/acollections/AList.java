package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.ALinkedList;
import com.ajjpj.acollections.util.AOption;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;


public interface AList<T> extends ACollection<T>, List<T> {
    AList<T> prepend(T o);
    AList<T> prependAll(AList<T> l);
    AList<T> append(T o);

    AList<T> updated(int idx, T o);
    AList<T> patch(int idx, List<T> patch, int numReplaced);

    T last();
    default AOption<T> lastOption() {
        if (isEmpty()) return AOption.none();
        else return AOption.some(last());
    }
    AList<T> init();
    AList<T> tail();

    AList<T> take(int n);
    AList<T> takeRight(int n);
    AList<T> takeWhile(Predicate<T> f);
    AList<T> drop(int n);
    AList<T> dropRight(int n);
    AList<T> dropWhile(Predicate<T> f);

    AList<T> reverse();
    AIterator<T> reverseIterator();

    default boolean contains(Object o) {
        return exists(el -> equality().equals(el, o));
    }

    default boolean startsWith(AList<T> that) {
        if (that.size() > this.size()) return false;

        final Iterator<T> itThis = this.iterator();
        for (T aThat: that) {
            if (!equality().equals(itThis.next(), aThat))
                return false;
        }
        return true;
    }
    boolean endsWith(AList<T> that);

    default <U> U foldRight(U zero, BiFunction<U,T,U> f) {
        return reverseIterator().fold(zero, f);
    }
    default T reduceRight(BiFunction<T,T,T> f) {
        return reverseIterator().reduce(f);
    }
    default AOption<T> reduceRightOption(BiFunction<T,T,T> f) {
        if (isEmpty())
            return AOption.none();
        else
            return AOption.some(reduceRight(f));
    }

    //TODO permutations
    //TODO combinations
    //TODO reverseMap
    //TODO indexOfSlice (2x), lastIndexOfSlice (2x), containsSlice
    //TODO corresponds
    //TODO sortWith, sortBy, shuffle
    //TODO indices
    //TODO zip, zipWithIndex
    //TODO partition, groupBy, slice, splitAt, span, distinct

    default Object[] toArray () {
        return toArray(new Object[size()]);
    }
    default <T1> T1[] toArray (T1[] a) {
        final int s = size();

        @SuppressWarnings("unchecked")
        final T1[] result = a.length >= s ?
                a :
                (T1[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), s);
        if (result.length > s) {
            // if the array is longer than required, then the spec demands the data to be followed by <tt>null</tt>
            result[s] = null;
        }

        int idx=0;
        for (Object o: this) {
            //noinspection unchecked
            result[idx] = (T1) o;
            idx += 1;
        }
        return result;
    }

    default boolean containsAll (Collection<?> c) {
        for(Object o: c)
            if (! contains(o)) return false;
        return true;
    }
}
