package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AList;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.util.AEquality;
import com.ajjpj.acollections.util.AOption;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;
import java.util.function.Predicate;


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

    //TODO unimplemented below this point


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
        return AVector.from(this).reverseIterator();
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
    public <U> ACollection<U> collect (Predicate<T> filter, Function<T, U> f) {
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
    public <U> ACollection<U> map (Function<T, U> f) {
        return null;
    }

    @Override
    public ACollection<T> filter (Predicate<T> f) {
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
        return null;
    }

    @Override
    public ListIterator<T> listIterator (int index) {
        return null;
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
