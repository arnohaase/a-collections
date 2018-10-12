package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.ALinkedList;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.util.AEquality;
import com.ajjpj.acollections.util.AOption;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;


public interface ACollection<T> extends Collection<T> {
    AEquality equality();
    @Override AIterator<T> iterator ();

    /**
     * This is public API, but it was added largely for internal use: Having this method allows generically implementing transformation
     *  methods like {@link #map(Function)}.
     */
    <U> ACollectionBuilder<U, ? extends ACollection<U>> newBuilder();

    boolean nonEmpty();

    T head();
    AOption<T> headOption();

    ALinkedList<T> toLinkedList();
    AVector<T> toVector();

    <U> ACollection<U> map(Function<T,U> f);
    <U> ACollection<U> flatMap(Function<T, Iterable<U>> f);
    <U> ACollection<U> collect(Predicate<T> filter, Function<T,U> f);
    <U> AOption<U> collectFirst(Predicate<T> filter, Function<T,U> f);

    ACollection<T> filter(Predicate<T> f);
    default ACollection<T> filterNot(Predicate<T> f) {
        return filter(f.negate());
    }

    //TODO flatten

    AOption<T> find(Predicate<T> f);
    boolean forall(Predicate<T> f);
    boolean exists(Predicate<T> f);
    int count(Predicate<T> f);
    boolean contains(Object o);

    default T reduce(BiFunction<T,T,T> f) {
        return reduceLeft(f);
    }
    T reduceLeft(BiFunction<T,T,T> f);
    AOption<T> reduceLeftOption(BiFunction<T,T,T> f);

    default <U> U fold(U zero, BiFunction<U,T,U> f) {
        return foldLeft(zero, f);
    }
    <U> U foldLeft(U zero, BiFunction<U,T,U> f);

    T min();
    T min(Comparator<T> comparator);
    T max();
    T max(Comparator<T> comparator);

    String mkString(String infix);
    String mkString(String prefix, String infix, String suffix);
}
