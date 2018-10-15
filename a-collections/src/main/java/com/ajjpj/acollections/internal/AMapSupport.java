package com.ajjpj.acollections.internal;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.immutable.AbstractImmutableCollection;
import com.ajjpj.acollections.util.AEquality;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;


public class AMapSupport {
    public static class KeySet<T> extends AbstractImmutableCollection<T> implements ASet<T>, ACollectionDefaults<T, AHashSet<T>> {
        private final AMap<T,?> map;

        public KeySet (AMap<T, ?> map) {
            this.map = map;
        }

        @Override public AHashSet<T> added (T o) {
            return AHashSet.from(this).added(o);
        }

        @Override public AHashSet<T> removed (T o) {
            return AHashSet.from(this).removed(o);
        }

        @Override public AHashSet<T> union (Iterable<T> that) {
            return AHashSet.from(this).union(that);
        }

        @Override public AHashSet<T> intersect (Collection<T> that) {
            return AHashSet.from(this).intersect(that);
        }

        @Override public ASet<T> diff (Iterable<T> that) {
            return AHashSet.from(this).diff(that);
        }

        @Override public AIterator<ASet<T>> subsets () {
            return null; //TODO subsets
        }

        @Override public AEquality equality () {
            return map.keyEquality();
        }

        @Override public AIterator<T> iterator () {
            return map.iterator().map(Map.Entry::getKey);
        }

        @Override public <U> ACollectionBuilder<U, AHashSet<U>> newBuilder () {
            return AHashSet.builder(map.keyEquality());
        }

        @Override public boolean isEmpty () {
            return map.isEmpty();
        }

        @Override public <U> ACollection<U> map (Function<T, U> f) {
            return ACollectionSupport.map(newBuilder(), this, f);
        }

        @Override public <U> ACollection<U> flatMap (Function<T, Iterable<U>> f) {
            return ACollectionSupport.flatMap(newBuilder(), this, f);
        }

        @Override public <U> ACollection<U> collect (Predicate<T> filter, Function<T, U> f) {
            return ACollectionSupport.collect(newBuilder(), this, filter, f);
        }

        @Override public AHashSet<T> filter (Predicate<T> f) {
            return ACollectionDefaults.super.filter(f);
        }

        @Override public AHashSet<T> filterNot (Predicate<T> f) {
            return ACollectionDefaults.super.filterNot(f);
        }

        @Override public int size () {
            return map.size();
        }

        @Override public Object[] toArray () {
            return ACollectionSupport.toArray(this);
        }

        @Override public <T1> T1[] toArray (T1[] a) {
            return ACollectionSupport.toArray(this, a);
        }

        @Override public boolean contains (Object o) {
            //noinspection SuspiciousMethodCalls
            return map.containsKey(o);
        }

        @Override public boolean containsAll (Collection<?> c) {
            return ACollectionDefaults.super.containsAll(c);
        }
    }

    public static class ValueCollection<T> extends AbstractImmutableCollection<T> implements ACollection<T>, ACollectionDefaults<T, AVector<T>> {
        private final AMap<?,T> map;

        public ValueCollection (AMap<?, T> map) {
            this.map = map;
        }

        @Override public AEquality equality () {
            return AEquality.EQUALS;
        }

        @Override public AIterator<T> iterator () {
            return map.iterator().map(Map.Entry::getValue);
        }

        @Override public <U> ACollectionBuilder<U, AVector<U>> newBuilder () {
            return AVector.builder();
        }

        @Override public boolean isEmpty () {
            return map.isEmpty();
        }

        @Override public <U> ACollection<U> map (Function<T, U> f) {
            return ACollectionSupport.map(newBuilder(), this, f);
        }

        @Override public <U> ACollection<U> flatMap (Function<T, Iterable<U>> f) {
            return ACollectionSupport.flatMap(newBuilder(), this, f);
        }

        @Override public <U> ACollection<U> collect (Predicate<T> filter, Function<T, U> f) {
            return ACollectionSupport.collect(newBuilder(), this, filter, f);
        }

        @Override public AVector<T> filter (Predicate<T> f) {
            return ACollectionDefaults.super.filter(f);
        }

        @Override public AVector<T> filterNot (Predicate<T> f) {
            return ACollectionDefaults.super.filterNot(f);
        }

        @Override public int size () {
            return map.size();
        }

        @Override public Object[] toArray () {
            return ACollectionSupport.toArray(this);
        }

        @Override public <T1> T1[] toArray (T1[] a) {
            return ACollectionSupport.toArray(this, a);
        }

        @Override public boolean contains (Object o) {
            return ACollectionDefaults.super.contains(o);
        }

        @Override public boolean containsAll (Collection<?> c) {
            return ACollectionDefaults.super.containsAll(c);
        }
    }

    public static class EntrySet<K,V> extends AbstractImmutableCollection<Map.Entry<K,V>> implements ASet<Map.Entry<K,V>>, ACollectionDefaults<Map.Entry<K,V>, AHashSet<Map.Entry<K,V>>> {
        private final AMap<K,V> map;

        public EntrySet (AMap<K, V> map) {
            this.map = map;
        }

        @Override public AHashSet<Map.Entry<K,V>> added (Map.Entry<K,V> o) {
            return AHashSet.from(this).added(o);
        }

        @Override public AHashSet<Map.Entry<K,V>> removed (Map.Entry<K,V> o) {
            return AHashSet.from(this).removed(o);
        }

        @Override public AHashSet<Map.Entry<K,V>> union (Iterable<Map.Entry<K,V>> that) {
            return AHashSet.from(this).union(that);
        }

        @Override public AHashSet<Map.Entry<K,V>> intersect (Collection<Map.Entry<K,V>> that) {
            return AHashSet.from(this).intersect(that);
        }

        @Override public ASet<Map.Entry<K,V>> diff (Iterable<Map.Entry<K,V>> that) {
            return AHashSet.from(this).diff(that);
        }

        @Override public AIterator<ASet<Map.Entry<K,V>>> subsets () {
            return null; //TODO subsets
        }

        @Override public AEquality equality () {
            return map.keyEquality();
        }

        @Override public AIterator<Map.Entry<K,V>> iterator () {
            return map.iterator();
        }

        @Override public <U> ACollectionBuilder<U, AHashSet<U>> newBuilder () {
            return AHashSet.builder(map.keyEquality());
        }

        @Override public boolean isEmpty () {
            return map.isEmpty();
        }

        @Override public <U> ACollection<U> map (Function<Map.Entry<K,V>, U> f) {
            return ACollectionSupport.map(newBuilder(), this, f);
        }

        @Override public <U> ACollection<U> flatMap (Function<Map.Entry<K,V>, Iterable<U>> f) {
            return ACollectionSupport.flatMap(newBuilder(), this, f);
        }

        @Override public <U> ACollection<U> collect (Predicate<Map.Entry<K,V>> filter, Function<Map.Entry<K,V>, U> f) {
            return ACollectionSupport.collect(newBuilder(), this, filter, f);
        }

        @Override public AHashSet<Map.Entry<K,V>> filter (Predicate<Map.Entry<K,V>> f) {
            return ACollectionDefaults.super.filter(f);
        }

        @Override public AHashSet<Map.Entry<K,V>> filterNot (Predicate<Map.Entry<K,V>> f) {
            return ACollectionDefaults.super.filterNot(f);
        }

        @Override public int size () {
            return map.size();
        }

        @Override public Object[] toArray () {
            return ACollectionSupport.toArray(this);
        }

        @Override public <T1> T1[] toArray (T1[] a) {
            return ACollectionSupport.toArray(this, a);
        }

        @Override public boolean contains (Object o) {
            //noinspection SuspiciousMethodCalls
            return map.containsKey(o);
        }

        @Override public boolean containsAll (Collection<?> c) {
            return ACollectionDefaults.super.containsAll(c);
        }
    }

}
