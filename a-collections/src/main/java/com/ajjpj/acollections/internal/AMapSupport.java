package com.ajjpj.acollections.internal;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.immutable.*;
import com.ajjpj.acollections.util.AEquality;
import com.ajjpj.acollections.util.AOption;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


public class AMapSupport {
    public static class KeySet<T> extends AbstractImmutableCollection<T> implements ASet<T>, ACollectionDefaults<T, AHashSet<T>>, ASetDefaults<T, AHashSet<T>> {
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

        @Override public AHashSet<T> intersect (Set<T> that) {
            return AHashSet.fromIterator(iterator().filter(that::contains), equality());
        }

        @Override public ASet<T> diff (Set<T> that) {
            return AHashSet.fromIterator(iterator().filterNot(that::contains), equality());
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

        @Override public boolean contains (Object o) {
            //noinspection SuspiciousMethodCalls
            return map.containsKey(o);
        }

        @Override public boolean containsAll (Collection<?> c) {
            return ACollectionDefaults.super.containsAll(c);
        }

        @Override public boolean equals (Object o) {
            return ASetSupport.equals(this, o);
        }

        @Override public String toString () {
            return ACollectionSupport.toString(KeySet.class, this);
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

        @Override public boolean contains (Object o) {
            return ACollectionDefaults.super.contains(o);
        }

        @Override public boolean containsAll (Collection<?> c) {
            return ACollectionDefaults.super.containsAll(c);
        }

        @Override
        public boolean equals (Object o) {
            if (o == this) return true;
            if (! (o instanceof Collection)) return false;

            //noinspection unchecked
            final Collection<T> that = (Collection<T>) o;
            if (this.size() != that.size()) return false;
            return containsAll(that);
        }


        @Override public String toString () {
            return ACollectionSupport.toString(ValueCollection.class, this);
        }
    }

    public static class EntrySet<K,V> extends AbstractImmutableCollection<Map.Entry<K,V>> implements ASet<Map.Entry<K,V>>, ACollectionDefaults<Map.Entry<K,V>, AHashSet<Map.Entry<K,V>>>, ASetDefaults<Map.Entry<K,V>, AHashSet<Map.Entry<K,V>>> {
        private final AMap<K,V> map;

        public EntrySet (AMap<K, V> map) {
            this.map = map;
        }

        @Override public ATreeSet<Map.Entry<K, V>> toSortedSet () {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }

        @Override public AHashSet<Map.Entry<K,V>> added (Map.Entry<K,V> o) {
            return AHashSet.from(this, equality()).added(o);
        }

        @Override public AHashSet<Map.Entry<K,V>> removed (Map.Entry<K,V> o) {
            return AHashSet.from(this, equality()).removed(o);
        }

        @Override public AHashSet<Map.Entry<K,V>> union (Iterable<Map.Entry<K,V>> that) {
            return AHashSet.from(this, equality()).union(that);
        }

        @Override public AHashSet<Map.Entry<K,V>> intersect (Set<Map.Entry<K,V>> that) {
            return AHashSet.fromIterator(iterator().filter(that::contains), equality());
        }

        @Override public ASet<Map.Entry<K,V>> diff (Set<Map.Entry<K,V>> that) {
            return AHashSet.fromIterator(iterator().filterNot(that::contains), equality());
        }

        @Override public AEquality equality () {
            return AEquality.EQUALS;
        }

        @Override public AIterator<Map.Entry<K,V>> iterator () {
            return map.iterator();
        }

        @Override public <U> ACollectionBuilder<U, AHashSet<U>> newBuilder () {
            return AHashSet.builder(AEquality.EQUALS);
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

        @Override public Map.Entry<K, V> min () {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }

        @Override public Map.Entry<K, V> max () {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }

        @Override public int size () {
            return map.size();
        }

        @Override public boolean contains (Object o) {
            return map.contains(o);
        }

        @Override public boolean containsAll (Collection<?> c) {
            return ACollectionDefaults.super.containsAll(c);
        }

        @Override public boolean equals (Object o) {
            return ASetSupport.equals(this, o);
        }

        @Override public String toString () {
            return ACollectionSupport.toString(EntrySet.class, this);
        }
    }

    public static class SortedKeySet<T> extends AbstractImmutableCollection<T> implements ASortedSet<T>, ACollectionDefaults<T, ATreeSet<T>>, ASetDefaults<T, ATreeSet<T>> {
        private final ASortedMap<T,?> map;

        public SortedKeySet (ASortedMap<T, ?> map) {
            this.map = map;
        }

        @Override public Comparator<T> comparator () {
            return map.comparator();
        }

        @Override public AHashSet<T> toSet () {
            return AHashSet.from(this);
        }

        @Override public ATreeSet<T> added (T o) {
            return ATreeSet.from(this, map.comparator()).added(o);
        }

        @Override public ATreeSet<T> removed (T o) {
            return ATreeSet.from(this, map.comparator()).removed(o);
        }

        @Override public ATreeSet<T> union (Iterable<T> that) {
            return ATreeSet.builder(map.comparator())
                    .addAll(this)
                    .addAll(that)
                    .build();
        }

        @Override public ATreeSet<T> intersect (Set<T> that) {
            return ATreeSet.fromIterator(iterator().filter(that::contains), map.comparator());
        }

        @Override public ATreeSet<T> diff (Set<T> that) {
            return ATreeSet.fromIterator(iterator().filterNot(that::contains), map.comparator());
        }

        @Override public int countInRange (AOption<T> from, AOption<T> to) {
            return map.countInRange(from, to);
        }

        @Override public ASortedSet<T> range (AOption<T> from, AOption<T> until) {
            return map.range(from, until).keySet();
        }

        @Override public ASortedSet<T> drop (int n) {
            return map.drop(n).keySet();
        }

        @Override public ASortedSet<T> take (int n) {
            return map.take(n).keySet();
        }

        @Override public ASortedSet<T> slice (int from, int until) {
            return map.slice(from, until).keySet();
        }

        @Override public AOption<T> smallest () {
            return map.smallest().map(Map.Entry::getKey);
        }

        @Override public AOption<T> greatest () {
            return map.greatest().map(Map.Entry::getKey);
        }

        @Override public AIterator<T> iterator (AOption<T> start) {
            return map.keysIterator(start);
        }

        @Override public AEquality equality () {
            return map.keyEquality();
        }

        @Override public AIterator<T> iterator () {
            return map.keysIterator();
        }

        @Override public <U> ACollectionBuilder<U, ATreeSet<U>> newBuilder () {
            return ATreeSet.builder((Comparator) map.comparator()); //TODO this is somewhat happy - better alternatives?
        }

        @Override public boolean isEmpty () {
            return map.isEmpty();
        }

        @Override public <U> ATreeSet<U> map (Function<T, U> f) {
            return ACollectionSupport.map(newBuilder(), this, f);
        }

        @Override public <U> ATreeSet<U> flatMap (Function<T, Iterable<U>> f) {
            return ACollectionSupport.flatMap(newBuilder(), this, f);
        }

        @Override public <U> ACollection<U> collect (Predicate<T> filter, Function<T, U> f) {
            return ACollectionSupport.collect(newBuilder(), this, filter, f);
        }

        @Override public ATreeSet<T> filter (Predicate<T> f) {
            return ACollectionDefaults.super.filter(f);
        }
        @Override public ATreeSet<T> filterNot (Predicate<T> f) {
            return ACollectionDefaults.super.filterNot(f);
        }

        @Override public int size () {
            return map.size();
        }

        @Override public boolean contains (Object o) {
            return map.containsKey(o);
        }

        @Override public boolean containsAll (Collection<?> c) {
            return ACollectionDefaults.super.containsAll(c);
        }

        @Override public AIterator<ATreeSet<T>> subsets () {
            return ASetDefaults.super.subsets();
        }

        @Override public AIterator<ATreeSet<T>> subsets (int len) {
            return ASetDefaults.super.subsets(len);
        }

        @Override public boolean equals (Object o) {
            return ASetSupport.equals(this, o);
        }

        @Override public String toString () {
            return ACollectionSupport.toString(SortedKeySet.class, this);
        }
    }

    public static class SortedEntrySet<K,V> extends AbstractImmutableCollection<Map.Entry<K,V>> implements ASortedSet<Map.Entry<K,V>>, ACollectionDefaults<Map.Entry<K,V>, ATreeSet<Map.Entry<K,V>>>, ASetDefaults<Map.Entry<K,V>, ATreeSet<Map.Entry<K,V>>> {
        private final ASortedMap<K,V> map;

        public SortedEntrySet (ASortedMap<K, V> map) {
            this.map = map;
        }

        @Override public Comparator<Map.Entry<K,V>> comparator() {
            return (o1, o2) -> map.comparator().compare(o1.getKey(), o2.getKey());
        }

        @Override public AVector<Map.Entry<K, V>> toVector () {
            return AVector.from(this);
        }
        @Override public ALinkedList<Map.Entry<K, V>> toLinkedList () {
            return ALinkedList.from(this);
        }
        @Override public AHashSet<Map.Entry<K, V>> toSet () {
            return AHashSet.from(this);
        }
        @Override public ATreeSet<Map.Entry<K,V>> toSortedSet() {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }

        @Override public Map.Entry<K, V> min () {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }
        @Override public Map.Entry<K, V> max () {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }

        @Override public ATreeSet<Map.Entry<K,V>> added (Map.Entry<K,V> o) {
            return ATreeSet.from(this, comparator()).added(o);
        }

        @Override public ATreeSet<Map.Entry<K,V>> removed (Map.Entry<K,V> o) {
            return ATreeSet.from(this, comparator()).removed(o);
        }

        @Override public ATreeSet<Map.Entry<K,V>> union (Iterable<Map.Entry<K,V>> that) {
            return ATreeSet.builder(comparator())
                    .addAll(this)
                    .addAll(that)
                    .build();
        }

        @Override public ATreeSet<Map.Entry<K,V>> intersect (Set<Map.Entry<K,V>> that) {
            return ATreeSet.fromIterator(iterator().filter(that::contains), comparator());
        }

        @Override public ATreeSet<Map.Entry<K,V>> diff (Set<Map.Entry<K,V>> that) {
            return ATreeSet.fromIterator(iterator().filterNot(that::contains), comparator());
        }

        @Override public int countInRange (AOption<Map.Entry<K,V>> from, AOption<Map.Entry<K,V>> to) {
            return map.countInRange(from.map(Map.Entry::getKey), to.map(Map.Entry::getKey));
        }

        @Override public ASortedSet<Map.Entry<K,V>> range (AOption<Map.Entry<K,V>> from, AOption<Map.Entry<K,V>> until) {
            return map.range(from.map(Map.Entry::getKey), until.map(Map.Entry::getKey)).entrySet();
        }

        @Override public ASortedSet<Map.Entry<K,V>> drop (int n) {
            return map.drop(n).entrySet();
        }

        @Override public ASortedSet<Map.Entry<K,V>> take (int n) {
            return map.take(n).entrySet();
        }

        @Override public ASortedSet<Map.Entry<K,V>> slice (int from, int until) {
            return map.slice(from, until).entrySet();
        }

        @Override public AOption<Map.Entry<K,V>> smallest () {
            return map.smallest();
        }

        @Override public AOption<Map.Entry<K,V>> greatest () {
            return map.greatest();
        }

        @Override public AIterator<Map.Entry<K,V>> iterator (AOption<Map.Entry<K,V>> start) {
            return map.iterator(start.map(Map.Entry::getKey));
        }

        @Override public AEquality equality () {
            return AEquality.fromComparator(new EntryComparator<>(map.comparator()));
        }

        @Override public AIterator<Map.Entry<K,V>> iterator () {
            return map.iterator();
        }

        @Override public <U> ACollectionBuilder<U, ATreeSet<U>> newBuilder () {
            return ATreeSet.builder((Comparator) map.comparator()); //TODO this is somewhat hacky - better alternatives?
        }

        @Override public boolean isEmpty () {
            return map.isEmpty();
        }

        @Override public <U> ATreeSet<U> map (Function<Map.Entry<K,V>, U> f) {
            return ACollectionSupport.map(newBuilder(), this, f);
        }

        @Override public <U> ATreeSet<U> flatMap (Function<Map.Entry<K,V>, Iterable<U>> f) {
            return ACollectionSupport.flatMap(newBuilder(), this, f);
        }

        @Override public <U> ACollection<U> collect (Predicate<Map.Entry<K,V>> filter, Function<Map.Entry<K,V>, U> f) {
            return ACollectionSupport.collect(newBuilder(), this, filter, f);
        }

        @Override public ATreeSet<Map.Entry<K,V>> filter (Predicate<Map.Entry<K,V>> f) {
            final ACollectionBuilder<Map.Entry<K,V>, ATreeSet<Map.Entry<K,V>>> builder = ATreeSet.builder(new EntryComparator<>(map.comparator()));
            for (Map.Entry<K,V> o: this) if (f.test(o)) builder.add(o);
            //noinspection unchecked
            return builder.build();
        }
        @Override public ATreeSet<Map.Entry<K,V>> filterNot (Predicate<Map.Entry<K,V>> f) {
            return filter(f.negate());
        }

        @Override public int size () {
            return map.size();
        }

        @Override
        public boolean contains (Object o) {
            return map.containsKey(o);
        }

        @Override public boolean containsAll (Collection<?> c) {
            return ACollectionDefaults.super.containsAll(c);
        }

        @Override public AIterator<ATreeSet<Map.Entry<K,V>>> subsets () {
            return ASetDefaults.super.subsets();
        }

        @Override public AIterator<ATreeSet<Map.Entry<K,V>>> subsets (int len) {
            return ASetDefaults.super.subsets(len);
        }

        @Override public boolean equals (Object o) {
            return ASetSupport.equals(this, o);
        }

        @Override public String toString () {
            return ACollectionSupport.toString(SortedKeySet.class, this);
        }
    }

    public static class EntryComparator<K,V> implements Comparator<Map.Entry<K,V>> {
        private final Comparator<K> keyComparator;

        public EntryComparator (Comparator<K> keyComparator) {
            this.keyComparator = keyComparator;
        }

        @Override public int compare (Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
            return keyComparator.compare(o1.getKey(), o2.getKey());
        }

        @Override public String toString () {
            return "EntryComparator{" +
                    "keyComparator=" + keyComparator +
                    '}';
        }

        @Override public boolean equals (Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final EntryComparator<?, ?> that = (EntryComparator<?, ?>) o;
            return Objects.equals(keyComparator, that.keyComparator);
        }

        @Override public int hashCode () {
            return Objects.hash(keyComparator);
        }
    }
}
