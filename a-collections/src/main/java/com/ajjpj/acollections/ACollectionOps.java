package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.ALinkedList;
import com.ajjpj.acollections.immutable.ATreeSet;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.util.AEquality;
import com.ajjpj.acollections.util.AOption;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * This interface separates out the ACollection specific operations without extending {@link Collection} itself, allowing more flexible
 *  use (e.g. for {@link AMap}.
 */
public interface ACollectionOps<T> {

    AEquality equality();
    AIterator<T> iterator ();

    /**
     * This is public API, but it was added largely for internal use: Having this method allows generically implementing transformation
     *  methods like {@link #map(Function)}.
     */
    <U> ACollectionBuilder<U, ? extends ACollection<U>> newBuilder();

    boolean isEmpty();
    default boolean nonEmpty() {
        return ! isEmpty();
    }

    T head();
    AOption<T> headOption();

    ALinkedList<T> toLinkedList();
    AVector<T> toVector();
    AHashSet<T> toSet();
    default ATreeSet<T> toSortedSet() {
        //noinspection unchecked
        return toSortedSet((Comparator) Comparator.naturalOrder());
    }
    ATreeSet<T> toSortedSet(Comparator<T> comparator);

    <U> ACollection<U> map(Function<T,U> f);
    <U> ACollection<U> flatMap(Function<T, Iterable<U>> f);
    <U> ACollection<U> collect(Predicate<T> filter, Function<T,U> f);
    <U> AOption<U> collectFirst(Predicate<T> filter, Function<T,U> f);

    ACollectionOps<T> filter(Predicate<T> f);
    ACollectionOps<T> filterNot(Predicate<T> f);

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
