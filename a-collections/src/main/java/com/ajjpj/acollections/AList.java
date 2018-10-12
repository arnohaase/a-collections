package com.ajjpj.acollections;

import com.ajjpj.acollections.util.AOption;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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

    @Override
    default ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override
    default ListIterator<T> listIterator(int index) {
        return new AListIterator<T>(this.iterator(), index);
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

    class AListIterator<T> implements ListIterator<T> {

        Iterator<T> internalIterator;

        AListIterator(Iterator<T> iterator, int seekIndex){
            internalIterator = iterator;
            for (int i=0; i<seekIndex; i++) internalIterator.next();
        }

        @Override
        public boolean hasNext() {
            return internalIterator.hasNext();
        }

        @Override
        public T next() {
            return internalIterator.next();
        }

        @Override
        public void remove() {
            internalIterator.remove();
        }

        @Override
        public boolean hasPrevious() {
            throw new UnsupportedOperationException();
        }

        @Override
        public T previous() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int nextIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(T t) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(T t) {
            throw new UnsupportedOperationException();

        }
    }
}
