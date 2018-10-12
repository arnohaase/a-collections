package com.ajjpj.acollections;

import com.ajjpj.acollections.internal.ASimpleListIterator;
import com.ajjpj.acollections.util.AOption;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;


public interface AList<T> extends ACollection<T>, List<T> {
    @Override <U> ACollectionBuilder<U, ? extends AList<U>> newBuilder();

    AList<T> prepend(T o);
    AList<T> append(T o);
    AList<T> concat (Iterator<? extends T> that);
    AList<T> concat (Iterable<? extends T> that);

    AList<T> updated(int idx, T o);
    default AList<T> patch(int idx, List<T> patch, int numReplaced) {
        final ACollectionBuilder<T, ? extends AList<T>> builder = newBuilder();

        AList<T> l = this;
        for(int i=0; i<idx; i++) {
            builder.add(l.head());
            l = l.tail();
        }

        for (T el: patch)
            builder.add(el);
        for (int i=0; i<numReplaced; i++)
            l = l.tail();

        for (T el: l)
            builder.add(el);
        return builder.build();
    }

    T last();
    default AOption<T> lastOption() {
        if (isEmpty()) return AOption.none();
        else return AOption.some(last());
    }
    AList<T> init();
    AList<T> tail();

    AList<T> take(int n);
    AList<T> takeRight(int n);
    default AList<T> takeWhile(Predicate<T> f) {
        final ACollectionBuilder<T, ? extends AList<T>> builder = newBuilder();
        for (T o: this) {
            if (!f.test(o)) break;
            builder.add(o);
        }
        return builder.build();
    }

    AList<T> drop(int n);
    AList<T> dropRight(int n);
    default AList<T> dropWhile(Predicate<T> f) {
        final ACollectionBuilder<T, ? extends AList<T>> builder = newBuilder();
        boolean go = false;
        for (T o: this) {
            if (!go && !f.test(o)) go = true;
            if (go) builder.add(o);
        }
        return builder.build();
    }

    default AList<T> reverse() {
        return this.<T>newBuilder()
                .addAll(reverseIterator())
                .build();
    }
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
    default boolean endsWith(List<T> that) {
        final Iterator<T> i = this.iterator().drop(size() - that.size());
        final Iterator<T> j = that.iterator();
        while (i.hasNext() && j.hasNext())
            if (! equality().equals(i.next(), j.next()))
                return false;

        return ! j.hasNext();
    }

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

    @Override default ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override default ListIterator<T> listIterator(int index) {
        return new ASimpleListIterator<T>(this.iterator(), index);
    }

    default <U> AList<U> map(Function<T,U> f) {
        return (AList<U>) ACollection.super.map(f);
    }
    default <U> AList<U> flatMap(Function<T, Iterable<U>> f) {
        return (AList<U>) ACollection.super.flatMap(f);
    }
    default AList<T> filter(Predicate<T> f) {
        return (AList<T>) ACollection.super.filter(f);
    }
    default AList<T> filterNot(Predicate<T> f) {
        return filter(f.negate());
    }
    default <U> AList<U> collect(Predicate<T> filter, Function<T,U> f) {
        return (AList<U>) ACollection.super.collect(filter, f);
    }

    @Override default int indexOf (Object o) {
        int result = 0;
        for (T el: this) {
            if (equality().equals(el, o)) return result;
            result += 1;
        }
        return -1;
    }

    @Override default int lastIndexOf (Object o) {
        int result = size()-1;
        final Iterator<T> it = reverseIterator();
        while (it.hasNext()) {
            if (equality().equals(it.next(), o)) return result;
            result -= 1;
        }
        return -1;
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
