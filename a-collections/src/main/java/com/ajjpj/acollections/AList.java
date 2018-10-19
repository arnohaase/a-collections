package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.ARange;
import com.ajjpj.acollections.util.AOption;

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
        return new ARange(0, size());
    }
}
