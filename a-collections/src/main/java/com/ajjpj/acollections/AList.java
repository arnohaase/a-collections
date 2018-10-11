package com.ajjpj.acollections;

import com.ajjpj.acollections.util.AOption;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;


public interface AList<T> extends ACollection<T>, List<T> {
    AList<T> prepend(T o);
    AList<T> append(T o);
    AList<T> concat (Iterator<? extends T> that);
    AList<T> concat (Iterable<? extends T> that);

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

    default boolean startsWith(List<T> that) {
        if (that.size() > this.size()) return false;

        final Iterator<T> itThis = this.iterator();
        for (T aThat: that) {
            if (!equality().equals(itThis.next(), aThat))
                return false;
        }
        return true;
    }
    boolean endsWith(List<T> that);

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
    //TODO combinations (--> ACollection?)
    //TODO reverseMap
    //TODO indexOfSlice (2x), lastIndexOfSlice (2x), containsSlice
    //TODO corresponds
    //TODO sortWith, sortBy, shuffle
    //TODO indices
    //TODO zip, zipWithIndex
    //TODO partition (ACollection?!), groupBy (ACollection?), slice, splitAt, span, distinct (ACollection?!)

    @Override default Object[] toArray () {
        return ACollection.super.toArray();
    }
    @Override default <T1> T1[] toArray (T1[] a) {
        //noinspection SuspiciousToArrayCall
        return ACollection.super.toArray(a);
    }

    default boolean containsAll (Collection<?> c) {
        for(Object o: c)
            if (! contains(o)) return false;
        return true;
    }
}
