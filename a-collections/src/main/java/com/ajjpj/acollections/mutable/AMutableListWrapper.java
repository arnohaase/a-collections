package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AList;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AListDefaults;
import com.ajjpj.acollections.internal.AListIterator;
import com.ajjpj.acollections.util.AOption;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


/**
 * Always AEquality.EQUALS --> least surprise
 *
 */
public class AMutableListWrapper<T> implements AListDefaults<T, AMutableListWrapper<T>> {
    private final List<T> inner;

    public static <T> AMutableListWrapper<T> wrap(List<T> inner) {
        return new AMutableListWrapper<>(inner);
    }

    private AMutableListWrapper (List<T> inner) {
        this.inner = inner;
    }

    public List<T> inner() {
        return inner;
    }

    public static <T> AMutableListWrapper<T> empty() {
        return new AMutableListWrapper<>(new ArrayList<>());
    }

    public static <T> AMutableListWrapper<T> from(Iterator<T> it) {
        return AMutableListWrapper.<T>builder().addAll(it).build();
    }
    public static <T> AMutableListWrapper<T> from(Iterable<T> iterable) {
        return AMutableListWrapper.<T>builder().addAll(iterable).build();
    }

    public static <T> AMutableListWrapper<T> of(T o1) {
        return AMutableListWrapper.<T>builder().add(o1).build();
    }

    //TODO static factories

    @Override public <U> ACollectionBuilder<U, AMutableListWrapper<U>> newBuilder () {
        return builder();
    }

    @Override public AMutableListWrapper<T> toMutableList () {
        return this;
    }

    @Override
    public AOption<T> lastOption () {
        if (isEmpty()){
            return AOption.none();
        }
        return AOption.of(inner.get(inner.size()-1));
    }

    @Override
    public boolean contains (Object o) {
        return inner.contains(o);
    }

    @Override
    public boolean startsWith (List<T> that) {
        if (that.size()<=inner.size()){
            Iterator<T> thatIterator = that.iterator();
            Iterator<T> innerIterator = inner.iterator();
            while (that.iterator().hasNext()){
                if (! Objects.equals(thatIterator.next(), innerIterator.next())){
                    return false;
                }
            }
            return true;
        }else {
            return false;
        }
    }

    @Override
    public <U> U foldRight (U zero, BiFunction<U, T, U> f) {
        return reverseIterator().fold(zero, f);
    }

    @Override
    public T reduceRight (BiFunction<T, T, T> f) {
        return reverseIterator().reduce(f);
    }

    @Override
    public AOption<T> reduceRightOption (BiFunction<T, T, T> f) {
        return reverseIterator().reduceOption(f);
    }

    @Override
    public <U> AMutableListWrapper<U> flatMap (Function<T, Iterable<U>> f) {
        Function<T, Stream<U>> g = t -> StreamSupport.stream(f.apply(t).spliterator(), false);
        List<U> mappedInner = inner.stream()
             .flatMap(g)
             .collect(Collectors.toList());
        return new AMutableListWrapper<>(mappedInner);
    }

    @Override
    public AMutableListWrapper<T> filterNot (Predicate<T> f) {
        return filter(f.negate());
    }

    @Override
    public boolean nonEmpty () {
        return !isEmpty();
    }

    @Override
    public T head () {
        return iterator().next();
    }

    @Override
    public AOption<T> headOption () {
        if (isEmpty()){
            return AOption.none();
        }
        return AOption.some(iterator().next());
    }

    @Override
    public <U> AOption<U> collectFirst (Predicate<T> filter, Function<T, U> f) {
        return iterator().collectFirst(filter, f);
    }

    @Override
    public AOption<T> find (Predicate<T> f) {
        return iterator().find(f);
    }

    @Override
    public boolean forall (Predicate<T> f) {
        return iterator().forall(f);
    }

    @Override
    public boolean exists (Predicate<T> f) {
        return iterator().exists(f);
    }

    @Override
    public int count (Predicate<T> f) {
        return iterator().count(f);
    }

    @Override
    public T reduceLeft (BiFunction<T, T, T> f) {
        return iterator().reduce(f);
    }

    @Override
    public AOption<T> reduceLeftOption (BiFunction<T, T, T> f) {
        return iterator().reduceOption(f);
    }

    @Override
    public <U> U foldLeft (U zero, BiFunction<U, T, U> f) {
        return iterator().fold(zero, f);
    }

    @Override
    public T min () {
        return iterator().min();
    }

    @Override
    public T min (Comparator<T> comparator) {
        return iterator().min(comparator);
    }

    @Override
    public T max () {
        return iterator().max();
    }

    @Override
    public T max (Comparator<T> comparator) {
        return iterator().max(comparator);
    }

    @Override
    public String mkString (String infix) {
        return iterator().mkString(infix);
    }

    @Override
    public String mkString (String prefix, String infix, String suffix) {
        return iterator().mkString(prefix, infix, suffix);
    }

    @Override
    public Object[] toArray () {
        return ACollectionSupport.toArray(this);
    }

    @Override
    public <T1> T1[] toArray (T1[] a) {
        return ACollectionSupport.toArray(this, a);
    }

    @Override
    public boolean containsAll (Collection<?> c) {
        return inner.containsAll(c);
    }

    @Override
    public AMutableListWrapper<T> prepend (T o) {
        inner.add(0, o);
        return this;
    }

    @Override
    public AMutableListWrapper<T> append (T o) {
        inner.add(o);
        return this;
    }

    @Override
    public AMutableListWrapper<T> updated (int idx, T o) {
        inner.set(idx, o);
        return this;
    }


    @Override
    public AMutableListWrapper<T> concat(Iterator<? extends T> that) {
        while (that.hasNext()) {
            inner.add(that.next());
        }
        return this;
    }

    @Override
    public AMutableListWrapper<T> concat(Iterable<? extends T> that) {
        return concat(that.iterator());
    }

    @Override
    public AMutableListWrapper<T> patch (int idx, List<T> patch, int numReplaced) {
        for (int i = 0; i<numReplaced; i++)
            inner.remove(idx);
        inner.addAll(idx, patch);
        return this;
    }

    @Override
    public T last () {
        return inner.get(inner.size()-1);
    }

    @Override
    public AMutableListWrapper<T> init () {
        return drop(1);
    }

    @Override
    public AMutableListWrapper<T> tail () {
        return dropRight(1);
    }

    @Override
    public AMutableListWrapper<T> takeRight (int n) {
        return new AMutableListWrapper<>(inner.subList(inner.size()-n, inner.size()));
    }

    @Override
    public AMutableListWrapper<T> dropRight (int n) {
        return new AMutableListWrapper<>(inner.subList(0, n+1));
    }

    @Override
    public AIterator<T> reverseIterator () {
        return AVector.from(inner).reverseIterator(); //TODO custom implementation --> optimize
    }

    @Override
    public boolean endsWith (List<T> that) {
        if (inner.size() <that.size())return false;
        final Iterator<T> i = inner.listIterator(inner.size() - that.size());
        final Iterator<T> j = that.iterator();
        while (i.hasNext() && j.hasNext())
            if (! Objects.equals(i.next(), j.next()))
                return false;

        return ! j.hasNext();
    }

    @Override
    public AMutableListWrapper<T> takeWhile (Predicate<T> f) {
        List<T> updatedInner = new ArrayList<>();
        for (T o: this) {
            if (!f.test(o)) break;
            updatedInner.add(o);
        }
        return new AMutableListWrapper<>(updatedInner);
    }

    @Override public AMutableListWrapper<T> dropWhile (Predicate<T> f) {
        List<T> updatedInner = new ArrayList<>();
        boolean go = false;
        for (T o: this) {
            if (!go && !f.test(o)) go = true;
            if (go) updatedInner.add(o);
        }
        return new AMutableListWrapper<>(updatedInner);
    }

    @Override
    public <U> AList<U> collect (Predicate<T> filter, Function<T, U> f) {
        List<U> updatedInner = inner.stream()
                .filter(filter)
                .map(f)
                .collect(Collectors.toList());
        return new AMutableListWrapper<>(updatedInner);
    }

    @Override
    public AMutableListWrapper<T> take (int n) {
        return new AMutableListWrapper<>(inner.subList(0, n-1));
    }

    @Override
    public AMutableListWrapper<T> drop (int n) {
        return new AMutableListWrapper<>(inner.subList(n, inner.size()-1));
    }

    @Override
    public AMutableListWrapper<T> reverse () {
        List<T> updatedInner = new ArrayList<>();
        for (T e: inner) updatedInner.add(0, e);
        return new AMutableListWrapper<>(updatedInner);
    }


    @Override
    public AIterator<T> iterator () {
        return new AListIterator<>(inner.listIterator());
    }

    @Override
    public <U> AMutableListWrapper<U> map (Function<T, U> f) {
        List<U> updatedInner = inner.stream().map(f).collect(Collectors.toList());
        return new AMutableListWrapper<>(updatedInner);
    }

    @Override
    public AMutableListWrapper<T> filter (Predicate<T> f) {
        List<T> updatedInner = inner.stream().filter(f).collect(Collectors.toList());
        return new AMutableListWrapper<>(updatedInner);
    }

    @Override
    public boolean addAll (int index, Collection<? extends T> c) {
        return inner.addAll(index, c);
    }

    @Override
    public T get (int index) {
        return inner.get(index);
    }

    @Override
    public T set (int index, T element) {
        return inner.set(index, element);
    }

    @Override
    public void add (int index, T element) {
        inner.add(index, element);
    }

    @Override
    public T remove (int index) {
        return inner.remove(index);
    }

    @Override
    public int indexOf (Object o) {
        return inner.indexOf(o);
    }

    @Override
    public int lastIndexOf (Object o) {
        return inner.lastIndexOf(o);
    }

    @Override
    public AListIterator<T> listIterator () {
        return new AListIterator<>(inner.listIterator());
    }

    @Override
    public AListIterator<T> listIterator (int index) {
        return new AListIterator<>(inner.listIterator());
    }

    @Override
    public List<T> subList (int fromIndex, int toIndex) {
        return new AMutableListWrapper<>(inner.subList(fromIndex,toIndex ));
    }

    @Override
    public int size () {
        return inner.size();
    }

    @Override
    public boolean isEmpty () {
        return inner.isEmpty();
    }

    @Override
    public boolean add (T t) {
        return inner.add(t);
    }

    @Override
    public boolean remove (Object o) {
        return inner.remove(o);
    }

    @Override
    public boolean addAll (Collection<? extends T> c) {
        return inner.addAll(c);
    }

    @Override
    public boolean removeAll (Collection<?> c) {
        return inner.removeAll(c);
    }

    @Override
    public boolean retainAll (Collection<?> c) {
        return inner.retainAll(c);
    }

    @Override
    public void clear () {
        inner.clear();
    }

    @Override public String toString () {
        return getClass().getSimpleName() + ":" + inner;
    }

    @Override public boolean equals(Object obj) {
        return inner.equals(obj);
    }

    @Override public int hashCode() {
        return inner.hashCode();
    }


    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> implements ACollectionBuilder<T, AMutableListWrapper<T>> {
        private final List<T> inner = new ArrayList<>();

        @Override public ACollectionBuilder<T, AMutableListWrapper<T>> add (T el) {
            inner.add(el);
            return this;
        }

        @Override public AMutableListWrapper<T> build () {
            return AMutableListWrapper.wrap(inner);
        }
    }
}
