package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.ARange;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.mutable.AMutableListWrapper;
import com.ajjpj.acollections.util.AOption;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;


public interface AList<T> extends ACollection<T>, List<T> {
    static <T> AList<T> wrap(List<T> l) {
        return AMutableListWrapper.wrap(l);
    }

    static <T> AVector<T> from(Iterable<T> that) {
        return AVector.from(that);
    }
    static <T> AVector<T> from(T[] that) {
        return AVector.from(that);
    }
    static <T> AVector<T> fromIterator(Iterator<T> it) {
        return AVector.fromIterator(it);
    }
    public static <T> AVector<T> of() {
        return AVector.of();
    }
    static <T> AVector<T> of(T o) {
        return AVector.of(o);
    }
    static <T> AVector<T> of(T o1, T o2) {
        return AVector.of(o1, o2);
    }
    static <T> AVector<T> of(T o1, T o2, T o3) {
        return AVector.of(o1, o2, o3);
    }
    static <T> AVector<T> of(T o1, T o2, T o3, T o4) {
        return AVector.of(o1, o2, o3, o4);
    }
    @SafeVarargs static <T> AVector<T> of(T o1, T o2, T o3, T o4, T o5, T... others) {
        return AVector.of(o1, o2, o3, o4, o5, others);
    }


    @Override <U> ACollectionBuilder<U, ? extends AList<U>> newBuilder();

    AList<T> prepend(T o);
    AList<T> append(T o);
    AList<T> concat (Iterator<? extends T> that);
    AList<T> concat (Iterable<? extends T> that);

    AList<T> updated(int idx, T o);
    AList<T> patch(int idx, List<T> patch, int numReplaced);

    T last();
    AOption<T> lastOption();
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

    AList<T> sorted(Comparator<? super T> comparator);
    AList<T> sorted();
    <X extends Comparable<X>> AList<T> sortedBy(Function<T,X> f);

    AList<T> shuffled();
    AList<T> shuffled(Random r);

    boolean contains(Object o);

    boolean startsWith(List<T> that);
    boolean endsWith(List<T> that);

    <U> U foldRight(U zero, BiFunction<U,T,U> f);
    T reduceRight(BiFunction<T,T,T> f);
    AOption<T> reduceRightOption(BiFunction<T,T,T> f);

    default @Override ListIterator<T> listIterator() {
        return listIterator(0);
    }

    @Override ListIterator<T> listIterator(int index);

    <U> AList<U> map(Function<T,U> f);
    <U> AList<U> flatMap(Function<T, Iterable<U>> f);
    AList<T> filter(Predicate<T> f);
    AList<T> filterNot(Predicate<T> f);
    <U> AList<U> collect(Predicate<T> filter, Function<T,U> f);

    default AList<Integer> indices() {
        return ARange.create(0, size());
    }
}
