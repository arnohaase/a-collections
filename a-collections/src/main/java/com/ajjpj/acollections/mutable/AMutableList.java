package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AList;
import com.ajjpj.acollections.util.AEquality;
import com.ajjpj.acollections.util.AOption;

import java.util.ArrayList;
import java.util.Collection;
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
        return null;
    }

    @Override
    public AList<T> prependAll (AList<T> l) {
        return null;
    }

    @Override
    public AList<T> append (T o) {
        return null;
    }

    @Override
    public AList<T> updated (int idx, T o) {
        return null;
    }

    @Override
    public AList<T> patch (int idx, List<T> patch, int numReplaced) {
        return null;
    }

    @Override
    public T last () {
        return null;
    }

    @Override
    public AOption<T> lastOption () {
        return null;
    }

    @Override
    public AList<T> init () {
        return null;
    }

    @Override
    public AList<T> tail () {
        return null;
    }

    @Override
    public AList<T> takeRight (int n) {
        return null;
    }

    @Override
    public AList<T> dropRight (int n) {
        return null;
    }

    @Override
    public AIterator<T> reverseIterator () {
        return null;
    }

    @Override
    public boolean startsWith (AList<T> that) {
        return false;
    }

    @Override
    public boolean endsWith (AList<T> that) {
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
    public boolean contains (Object o) {
        return false;
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
    public boolean add (T t) {
        return false;
    }

    @Override
    public boolean remove (Object o) {
        return false;
    }

    @Override
    public boolean containsAll (Collection<?> c) {
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
}
