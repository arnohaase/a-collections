package com.ajjpj.acollections.internal;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.ACollectionOps;
import com.ajjpj.acollections.AMap;
import com.ajjpj.acollections.immutable.*;
import com.ajjpj.acollections.mutable.AMutableListWrapper;
import com.ajjpj.acollections.mutable.AMutableSetWrapper;
import com.ajjpj.acollections.util.AOption;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This interface contains default implementations for many {@link ACollection} methods. It is typed in both element type and concrete
 *  collection type to allow concrete collection classes to implement it without overriding "just to get the return type right". This
 *  interface is separate from ACollection itself to keep that API clean and simple.
 *
 * For {@link #map(Function)}, {@link #flatMap(Function)} and {@link #collect(Predicate, Function)} that reaches the limits of Java's
 *  type system though: Their return type is the collection type but with a different element type, and Java can not express this.
 *  Implementing classes need to override and typecast :-(.
 *
 * @param <T> the collections's element type
 * @param <C> the collection class' concrete type
 */
public interface ACollectionDefaults<T, C extends ACollectionOps<T>> extends ACollectionOps<T>, Iterable<T> {
    default boolean nonEmpty() {
        return ! isEmpty();
    }

    @Override default T head() {
        return iterator().next();
    }
    @Override default AOption<T> headOption() {
        if (iterator().hasNext())
            return AOption.some(iterator().next());
        else
            return AOption.none();
    }

    @Override default ALinkedList<T> toLinkedList() {
        return ALinkedList.from(this);
    }
    @Override default AVector<T> toVector() {
        return AVector.from(this);
    }
    @Override default AHashSet<T> toSet() {
        return AHashSet.from(this);
    }
    @Override default ATreeSet<T> toSortedSet(Comparator<T> comparator) {
        return ATreeSet.from(this, comparator);
    }
    @Override default <K, V> AMap<K, V> toMap () {
        //noinspection unchecked
        return AHashMap.from((Iterable<Map.Entry<K,V>>)this);
    }

    @Override default AMutableListWrapper<T> toMutableList () {
        return AMutableListWrapper.from(this);
    }
    @Override default AMutableSetWrapper<T> toMutableSet () {
        return AMutableSetWrapper.from(this);
    }

    @Override default C filter(Predicate<T> f) {
        final ACollectionBuilder<T, ? extends ACollectionOps<T>> builder = newBuilder();
        for (T o: this) if (f.test(o)) builder.add(o);
        //noinspection unchecked
        return (C) builder.build();
    }
    @Override default C filterNot(Predicate<T> f) {
        return filter(f.negate());
    }

    @Override default <U> AOption<U> collectFirst(Predicate<T> filter, Function<T,U> f) {
        return iterator().collectFirst(filter, f);
    }

    @Override default AOption<T> find(Predicate<T> f) {
        return iterator().find(f);
    }

    @Override default boolean forall(Predicate<T> f) {
        return iterator().forall(f);
    }
    @Override default boolean exists(Predicate<T> f) {
        return iterator().exists(f);
    }
    @Override default int count(Predicate<T> f) {
        return iterator().count(f);
    }

    @Override default T reduce(BiFunction<T,T,T> f) {
        return reduceLeft(f);
    }
    @Override default T reduceLeft(BiFunction<T,T,T> f) {
        return iterator().reduce(f);
    }
    @Override default AOption<T> reduceLeftOption(BiFunction<T,T,T> f) {
        return iterator().reduceOption(f);
    }
    @Override default <U> U fold(U zero, BiFunction<U,T,U> f) {
        return iterator().fold(zero, f);
    }
    @Override default <U> U foldLeft(U zero, BiFunction<U,T,U> f) {
        return iterator().fold(zero, f);
    }

    @Override default <K1> AMap<K1, C> groupBy (Function<T, K1> keyExtractor) {
        Map<K1,ACollectionBuilder<T,C>> builders = new HashMap<>();
        for(T o: this) {
            //noinspection unchecked
            builders.computeIfAbsent(keyExtractor.apply(o), x -> (ACollectionBuilder) newBuilder())
                    .add(o);
        }

        AHashMap.Builder<K1,C> result = AHashMap.builder();
        for (Map.Entry<K1, ACollectionBuilder<T, C>> e: builders.entrySet())
            result.add(e.getKey(), e.getValue().build());
        return result.build();
    }

    @Override default T min() {
        return iterator().min();
    }
    @Override default T min(Comparator<T> comparator) {
        return iterator().min(comparator);
    }
    @Override default T max() {
        return iterator().max();
    }
    @Override default T max(Comparator<T> comparator) {
        return iterator().max(comparator);
    }

    @Override default String mkString(String infix) {
        return iterator().mkString(infix);
    }
    @Override default String mkString(String prefix, String infix, String suffix) {
        return iterator().mkString(prefix, infix, suffix);
    }

    /**
     * returns true if and only if this collection contains all elements in the collection that is passed in as a parameter.
     *
     * @param c the collection whose element this collection is supposed to contain
     * @return true if and only if this collections contains all of c's elements
     */
    default boolean containsAll (Collection<?> c) {
        for(Object o: c)
            if (! contains(o)) return false;
        return true;
    }
}
