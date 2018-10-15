package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AList;
import com.ajjpj.acollections.immutable.ALinkedList;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.util.AEquality;
import com.ajjpj.acollections.util.AOption;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class AMutableList<T> implements AList<T> {
    private final AEquality equality;
    private final List<T> inner;

    public AMutableList () { // TODO static factory methods instead of ctors
        this(AEquality.EQUALS);
    }
    public AMutableList (List<T> inner) {
        this(inner, AEquality.EQUALS);
    }
    public AMutableList (AEquality equality) {
        this(new ArrayList<>(), equality);
    }
    public AMutableList (List<T> inner, AEquality equality) {
        this.equality = equality;
        this.inner = inner;
    }

    @Override public AEquality equality () { return equality; }
    public List<T> inner() {
        return inner;
    }

    @Override public ACollectionBuilder<T, ? extends ACollection<T>> newBuilder () {
        //TODO differentiate based on inner type, build mutable collection and wrap that, ...

        return AVector.builder(); //TODO this is a temporary work-around
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
                if (equality.notEquals(thatIterator.next(), innerIterator.next())){
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
    public <U> AList<U> flatMap (Function<T, Iterable<U>> f) {
        Function<T, Stream<U>> g = t -> StreamSupport.stream(f.apply(t).spliterator(), false);
        List<U> mappedInner = inner.stream()
             .flatMap(g)
             .collect(Collectors.toList());
        return new AMutableList<>(mappedInner, equality);
    }

    @Override
    public AList<T> filterNot (Predicate<T> f) {
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
    public ALinkedList<T> toLinkedList () {
        return ALinkedList.from(this, equality);
    }

    @Override
    public AVector<T> toVector () {
        return AVector.from(this, equality);
    }

    //TODO unimplemented below this point
    @Override
    public <U> AOption<U> collectFirst (Predicate<T> filter, Function<T, U> f) {
        return null;
    }

    @Override
    public AOption<T> find (Predicate<T> f) {
        return null;
    }

    @Override
    public boolean forall (Predicate<T> f) {
        return false;
    }

    @Override
    public boolean exists (Predicate<T> f) {
        return false;
    }

    @Override
    public int count (Predicate<T> f) {
        return 0;
    }

    @Override
    public T reduceLeft (BiFunction<T, T, T> f) {
        return null;
    }

    @Override
    public AOption<T> reduceLeftOption (BiFunction<T, T, T> f) {
        return null;
    }

    @Override
    public <U> U foldLeft (U zero, BiFunction<U, T, U> f) {
        return null;
    }

    @Override
    public T min () {
        return null;
    }

    @Override
    public T min (Comparator<T> comparator) {
        return null;
    }

    @Override
    public T max () {
        return null;
    }

    @Override
    public T max (Comparator<T> comparator) {
        return null;
    }

    @Override
    public String mkString (String infix) {
        return null;
    }

    @Override
    public String mkString (String prefix, String infix, String suffix) {
        return null;
    }

    @Override
    public Object[] toArray () {
        return new Object[0];
    }

    @Override
    public <T1> T1[] toArray (T1[] a) {
        return null;
    }

    @Override
    public boolean containsAll (Collection<?> c) {
        return false;
    }

    @Override
    public AList<T> prepend (T o) {
        inner.add(0, o);
        return this;
    }

    @Override
    public AList<T> append (T o) {
        inner.add(o);
        return this;
    }

    @Override
    public AList<T> updated (int idx, T o) {
        inner.set(idx, o);
        return this;
    }


    @Override
    public AList<T> concat(Iterator<? extends T> that) {
        while (that.hasNext()) {
            inner.add(that.next());
        }
        return this;
    }

    @Override
    public AList<T> concat(Iterable<? extends T> that) {
        return concat(that.iterator());
    }

    @Override
    public AList<T> patch (int idx, List<T> patch, int numReplaced) {
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
    public AList<T> init () {
        return drop(1);
    }

    @Override
    public AList<T> tail () {
        return dropRight(1);
    }

    @Override
    public AList<T> takeRight (int n) {
        return new AMutableList<>(inner.subList(inner.size()-n, inner.size()));
    }

    @Override
    public AList<T> dropRight (int n) {
        return new AMutableList<>(inner.subList(0, n+1));
    }

    @Override
    public AIterator<T> reverseIterator () {
        return AVector.from(inner).reverseIterator();
    }

    @Override
    public boolean endsWith (List<T> that) {
        return false;
    }

    @Override
    public AList<T> takeWhile (Predicate<T> f) {
        return null;
    }

    @Override
    public AList<T> dropWhile (Predicate<T> f) {
        return null;
    }

    @Override
    public <U> AList<U> collect (Predicate<T> filter, Function<T, U> f) {
        return null;
    }

    @Override
    public AList<T> take (int n) {
        return null;
    }

    @Override
    public AList<T> drop (int n) {
        return null;
    }

    @Override
    public AList<T> reverse () {
        return null;
    }


    @Override
    public AIterator<T> iterator () {
        return null;
    }

    @Override
    public <U> AList<U> map (Function<T, U> f) {
        return null;
    }

    @Override
    public AList<T> filter (Predicate<T> f) {
        return null;
    }

    @Override
    public boolean addAll (int index, Collection<? extends T> c) {
        return false;
    }

    @Override
    public T get (int index) {
        return null;
    }

    @Override
    public T set (int index, T element) {
        return null;
    }

    @Override
    public void add (int index, T element) {

    }

    @Override
    public T remove (int index) {
        return null;
    }

    @Override
    public int indexOf (Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf (Object o) {
        return 0;
    }

    @Override
    public ListIterator<T> listIterator () {
        return inner.listIterator();
    }

    @Override
    public ListIterator<T> listIterator (int index) {
        return inner.listIterator(index);
    }

    @Override
    public List<T> subList (int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public int size () {
        return 0;
    }

    @Override
    public boolean isEmpty () {
        return false;
    }

    @Override
    public boolean add (T t) {
        return false;
    }

    @Override
    public boolean remove (Object o) {
        return false;
    }

    @Override
    public boolean addAll (Collection<? extends T> c) {
        return false;
    }

    @Override
    public boolean removeAll (Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll (Collection<?> c) {
        return false;
    }

    @Override
    public void clear () {

    }

    @Override
    public boolean equals(Object obj) {
        return inner.equals(obj);
    }

    @Override
    public int hashCode() {
        return inner.hashCode();
    }
}
