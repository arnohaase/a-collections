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

    default boolean nonEmpty() {
        return ! isEmpty();
    }

    default T head() {
        return iterator().next();
    }
    default AOption<T> headOption() {
        if (iterator().hasNext())
            return AOption.some(iterator().next());
        else
            return AOption.none();
    }

    default ALinkedList<T> toLinkedList() {
        return ALinkedList.fromIterator(iterator());
    }

    default AVector<T> toVector() {
        return AVector.<T>builder().addAll(iterator()).build();
    }

    <U> ACollection<U> map(Function<T,U> f);

    ACollection<T> filter(Predicate<T> f);
    default ACollection<T> filterNot(Predicate<T> f) {
        return filter(f.negate());
    }

    <U> ACollection<U> collect(Predicate<T> filter, Function<T,U> f);
    default <U> AOption<U> collectFirst(Predicate<T> filter, Function<T,U> f) {
        return iterator().collectFirst(filter, f);
    }

    default AOption<T> find(Predicate<T> f) {
        return iterator().find(f);
    }

    default boolean forall(Predicate<T> f) {
        return iterator().forall(f);
    }
    default boolean exists(Predicate<T> f) {
        return iterator().exists(f);
    }
    default int count(Predicate<T> f) {
        return iterator().count(f);
    }
    default boolean contains(Object o) {
        return exists(el -> equality().equals(el, o));
    }

    default T reduce(BiFunction<T,T,T> f) {
        return reduceLeft(f);
    }
    default T reduceLeft(BiFunction<T,T,T> f) {
        return iterator().reduce(f);
    }
    default AOption<T> reduceLeftOption(BiFunction<T,T,T> f) {
        return iterator().reduceOption(f);
    }
    default <U> U fold(U zero, BiFunction<U,T,U> f) {
        return iterator().fold(zero, f);
    }
    default <U> U foldLeft(U zero, BiFunction<U,T,U> f) {
        return iterator().fold(zero, f);
    }

    default T min() {
        return iterator().min();
    }
    default T min(Comparator<T> comparator) {
        return iterator().min(comparator);
    }
    default T max() {
        return iterator().max();
    }
    default T max(Comparator<T> comparator) {
        return iterator().max(comparator);
    }

    default String mkString(String infix) {
        return iterator().mkString(infix);
    }
    default String mkString(String prefix, String infix, String suffix) {
        return iterator().mkString(prefix, infix, suffix);
    }

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
