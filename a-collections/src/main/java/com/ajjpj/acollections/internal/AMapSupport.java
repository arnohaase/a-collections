package com.ajjpj.acollections.internal;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.immutable.*;
import com.ajjpj.acollections.util.AOption;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


public class AMapSupport {
    public static <K,V,K1> AMap<K1, ? extends ACollectionOps<Map.Entry<K,V>>> groupBy (ACollectionOps<Map.Entry<K,V>> coll, Function<Map.Entry<K,V>, K1> keyExtractor) {
        Map<K1,ACollectionBuilder<Map.Entry<K,V>,? extends ACollectionOps<Map.Entry<K,V>>>> builders = new HashMap<>();
        //noinspection unchecked
        for(Map.Entry<K,V> o: (Iterable<Map.Entry<K,V>>) coll) {
            builders.computeIfAbsent(keyExtractor.apply(o), x -> coll.newEntryBuilder())
                    .add(o);
        }

        AHashMap.Builder<K1,ACollectionOps<Map.Entry<K,V>>> result = AHashMap.builder();
        for (Map.Entry<K1, ACollectionBuilder<Map.Entry<K,V>, ? extends ACollectionOps<Map.Entry<K,V>>>> e: builders.entrySet())
            result.add(e.getKey(), e.getValue().build());
        return result.build();
    }

    public static <K,V> boolean containsEntry(AMap<K,V> m, Object o) {
        if (! (o instanceof Map.Entry)) return false;
        //noinspection unchecked
        final Map.Entry<K,V> e = (Map.Entry<K,V>) o;
        return m.getOptional(e.getKey()).contains(e.getValue());
    }

    public static class KeySet<T> extends AbstractImmutableCollection<T> implements ASet<T>, ACollectionDefaults<T, AHashSet<T>>, ASetDefaults<T, AHashSet<T>>, Serializable {
        private final AMap<T,?> map;

        public KeySet (AMap<T, ?> map) {
            this.map = map;
        }



        @Override public AHashSet<T> plus (T o) {
            return AHashSet.from(this).plus(o);
        }

        @Override public AHashSet<T> minus (T o) {
            return AHashSet.from(this).minus(o);
        }

        @Override public AHashSet<T> union (Iterable<? extends T> that) {
            return AHashSet.from(this).union(that);
        }

        @Override public AHashSet<T> intersect (Set<T> that) {
            return AHashSet.fromIterator(iterator().filter(that::contains));
        }

        @Override public ASet<T> diff (Set<T> that) {
            return AHashSet.fromIterator(iterator().filterNot(that::contains));
        }

        @Override public AIterator<T> iterator () {
            return map.iterator().map(Map.Entry::getKey);
        }

        @Override public <U> ACollectionBuilder<U, AHashSet<U>> newBuilder () {
            return AHashSet.builder();
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

        @Override public <K> AMap<K, AHashSet<T>> groupBy (Function<T, K> keyExtractor) {
            return ACollectionDefaults.super.groupBy(keyExtractor);
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

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override public boolean equals (Object o) {
            return ASetSupport.equals(this, o);
        }
        @Override public int hashCode() {
            return ASetSupport.hashCode(this);
        }

        @Override public String toString () {
            return ACollectionSupport.toString(KeySet.class, this);
        }
    }

    public static class ValuesCollection<T> extends AbstractImmutableCollection<T> implements ACollection<T>, ACollectionDefaults<T, AVector<T>>, Serializable {
        private final AMap<?,T> map;

        public ValuesCollection (AMap<?, T> map) {
            this.map = map;
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

        @Override public <K> AMap<K, AVector<T>> groupBy (Function<T, K> keyExtractor) {
            return ACollectionDefaults.super.groupBy(keyExtractor);
        }

        @Override public int size () {
            return map.size();
        }

        @Override public boolean contains (Object o) {
            return exists(x -> Objects.equals(x, o));
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
            return ACollectionSupport.toString(ValuesCollection.class, this);
        }
    }

    public static class EntrySet<K,V> extends AbstractImmutableCollection<Map.Entry<K,V>> implements ASet<Map.Entry<K,V>>, ACollectionDefaults<Map.Entry<K,V>, AHashSet<Map.Entry<K,V>>>, ASetDefaults<Map.Entry<K,V>, AHashSet<Map.Entry<K,V>>>, Serializable {
        private final AMap<K,V> map;

        public EntrySet (AMap<K, V> map) {
            this.map = map;
        }

        @Override public ATreeSet<Map.Entry<K,V>> toSortedSet () {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }

        @Override public AHashSet<Map.Entry<K,V>> plus (Map.Entry<K,V> o) {
            return AHashSet.from(this).plus(o);
        }

        @Override public AHashSet<Map.Entry<K,V>> minus (Map.Entry<K,V> o) {
            return AHashSet.from(this).minus(o);
        }

        @Override public AHashSet<Map.Entry<K,V>> union (Iterable<? extends Map.Entry<K,V>> that) {
            return AHashSet.from(this).union(that);
        }

        @Override public AHashSet<Map.Entry<K,V>> intersect (Set<Map.Entry<K,V>> that) {
            return AHashSet.fromIterator(iterator().filter(that::contains));
        }

        @Override public ASet<Map.Entry<K,V>> diff (Set<Map.Entry<K,V>> that) {
            return AHashSet.fromIterator(iterator().filterNot(that::contains));
        }

        @Override public AIterator<AHashSet<Map.Entry<K,V>>> subsets () {
            return ASetSupport.subsets(this, this::newBuilder);
        }

        @Override public AIterator<AHashSet<Map.Entry<K,V>>> subsets (int len) {
            return ASetSupport.subsets(len, this, this::newBuilder);
        }

        @Override public <K1> AMap<K1, AHashSet<Map.Entry<K,V>>> groupBy (Function<Map.Entry<K,V>, K1> keyExtractor) {
            return ACollectionDefaults.super.groupBy(keyExtractor);
        }

        @Override public AIterator<Map.Entry<K,V>> iterator () {
            return map.iterator();
        }

        @Override public <U> ACollectionBuilder<U, AHashSet<U>> newBuilder () {
            return AHashSet.builder();
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

        @Override public Map.Entry<K,V> min () {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }

        @Override public Map.Entry<K,V> max () {
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

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override public boolean equals (Object o) {
            return ASetSupport.equals(this, o);
        }
        @Override public int hashCode() {
            return ASetSupport.hashCode(this);
        }

        @Override public String toString () {
            return ACollectionSupport.toString(EntrySet.class, this);
        }
    }

    public static class SortedKeySet<T> extends AbstractImmutableCollection<T> implements ASortedSet<T>, ACollectionDefaults<T, ATreeSet<T>>, ASetDefaults<T, ATreeSet<T>>, Serializable {
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

        @Override public ATreeSet<T> plus (T o) {
            return ATreeSet.from(this, map.comparator()).plus(o);
        }

        @Override public ATreeSet<T> minus (T o) {
            return ATreeSet.from(this, map.comparator()).minus(o);
        }

        @Override public ATreeSet<T> union (Iterable<? extends T> that) {
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

        @Override public AOption<T> smallest () {
            return map.smallest().map(Map.Entry::getKey);
        }

        @Override public AOption<T> greatest () {
            return map.greatest().map(Map.Entry::getKey);
        }

        @Override public AIterator<T> iterator (AOption<T> from, boolean fromInclusive, AOption<T> to, boolean toInclusive) {
            return map.keysIterator(from, fromInclusive, to, toInclusive);
        }

        @Override public AIterator<T> iterator () {
            return map.keysIterator();
        }

        @Override public <U> ACollectionBuilder<U, ATreeSet<U>> newBuilder () {
            //noinspection unchecked
            return ATreeSet.builder((Comparator) map.comparator()); //TODO this is somewhat hacky - better alternatives?
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

        @Override public <K> AMap<K, ATreeSet<T>> groupBy (Function<T, K> keyExtractor) {
            return ACollectionDefaults.super.groupBy(keyExtractor);
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

        @Override public AIterator<ATreeSet<T>> subsets () {
            return ASetDefaults.super.subsets();
        }

        @Override public AIterator<ATreeSet<T>> subsets (int len) {
            return ASetDefaults.super.subsets(len);
        }

        //TODO test these

        @Override public T lower (T t) {
            return map.lowerKey(t);
        }

        @Override public T floor (T t) {
            return map.floorKey(t);
        }

        @Override public T ceiling (T t) {
            return map.ceilingKey(t);
        }

        @Override public T higher (T t) {
            return map.higherKey(t);
        }

        @Override public T pollFirst () {
            throw new UnsupportedOperationException("mutable operation on an immutable collection");
        }

        @Override public T pollLast () {
            throw new UnsupportedOperationException("mutable operation on an immutable collection");
        }

        @Override public ASortedSet<T> descendingSet () {
            return map.descendingKeySet();
        }

        @Override public AIterator<T> descendingIterator () {
            return null; //TODO implement this
        }

        @Override public NavigableSet<T> subSet (T fromElement, boolean fromInclusive, T toElement, boolean toInclusive) {
            return map.subMap(fromElement, fromInclusive, toElement, toInclusive).keySet();
        }

        @Override public NavigableSet<T> headSet (T toElement, boolean inclusive) {
            return map.headMap(toElement, inclusive).keySet();
        }

        @Override public NavigableSet<T> tailSet (T fromElement, boolean inclusive) {
            return map.tailMap(fromElement, inclusive).keySet();
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override public boolean equals (Object o) {
            return ASetSupport.equals(this, o);
        }
        @Override public int hashCode() {
            return ASetSupport.hashCode(this);
        }

        @Override public String toString () {
            return ACollectionSupport.toString(SortedKeySet.class, this);
        }
    }

    public static class SortedEntrySet<K,V> extends AbstractImmutableCollection<Map.Entry<K,V>> implements ASortedSet<Map.Entry<K,V>>, ACollectionDefaults<Map.Entry<K,V>, ATreeSet<Map.Entry<K,V>>>, ASetDefaults<Map.Entry<K,V>, ATreeSet<Map.Entry<K,V>>>, Serializable {
        private final ASortedMap<K,V> map;

        public SortedEntrySet (ASortedMap<K, V> map) {
            this.map = map;
        }

        @Override public Comparator<Map.Entry<K,V>> comparator() {
            return Map.Entry.comparingByKey(map.comparator());
        }

        @Override public AVector<Map.Entry<K,V>> toVector () {
            return AVector.from(this);
        }
        @Override public ALinkedList<Map.Entry<K,V>> toLinkedList () {
            return ALinkedList.from(this);
        }
        @Override public AHashSet<Map.Entry<K,V>> toSet () {
            return AHashSet.from(this);
        }
        @Override public ATreeSet<Map.Entry<K,V>> toSortedSet() {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }

        @Override public <K1> AMap<K1, ATreeSet<Map.Entry<K,V>>> groupBy (Function<Map.Entry<K,V>, K1> keyExtractor) {
            //noinspection unchecked
            return (AMap<K1, ATreeSet<Map.Entry<K, V>>>) AMapSupport.groupBy(this, keyExtractor);
        }

        @Override public Map.Entry<K,V> min () {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }
        @Override public Map.Entry<K,V> max () {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }

        @Override public ATreeSet<Map.Entry<K,V>> plus (Map.Entry<K,V> o) {
            return ATreeSet.from(this, comparator()).plus(o);
        }

        @Override public ATreeSet<Map.Entry<K,V>> minus (Map.Entry<K,V> o) {
            return ATreeSet.from(this, comparator()).minus(o);
        }

        @Override public ATreeSet<Map.Entry<K,V>> union (Iterable<? extends Map.Entry<K,V>> that) {
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

        @Override public AOption<Map.Entry<K,V>> smallest () {
            return map.smallest();
        }

        @Override public AOption<Map.Entry<K,V>> greatest () {
            return map.greatest();
        }

        @Override public AIterator<Map.Entry<K,V>> iterator (AOption<Map.Entry<K,V>> from, boolean fromInclusive, AOption<Map.Entry<K,V>> to, boolean toInclusive) {
            return map.iterator(from.map(Map.Entry::getKey), fromInclusive, to.map(Map.Entry::getKey), toInclusive);
        }

        @Override public AIterator<Map.Entry<K,V>> iterator () {
            return map.iterator();
        }

        @Override public <U> ACollectionBuilder<U, ATreeSet<U>> newBuilder () {
            //noinspection unchecked
            return ATreeSet.builder((Comparator) map.comparator()); //TODO this is somewhat hacky - better alternatives?
        }

        @Override public <K1,V1> ACollectionBuilder<Map.Entry<K1,V1>, ? extends ACollectionOps<Map.Entry<K1,V1>>> newEntryBuilder () {
            //noinspection unchecked
            return ATreeSet.builder(Map.Entry.comparingByKey((Comparator) map.comparator()));
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
            return map.contains(o);
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

        //TODO test these

        @Override public Map.Entry<K, V> lower (Map.Entry<K, V> kvEntry) {
            return map.lowerEntry(kvEntry.getKey());
        }

        @Override public Map.Entry<K, V> floor (Map.Entry<K, V> kvEntry) {
            return map.floorEntry(kvEntry.getKey());
        }

        @Override public Map.Entry<K, V> ceiling (Map.Entry<K, V> kvEntry) {
            return map.ceilingEntry(kvEntry.getKey());
        }

        @Override public Map.Entry<K, V> higher (Map.Entry<K, V> kvEntry) {
            return map.higherEntry(kvEntry.getKey());
        }

        @Override public Map.Entry<K, V> pollFirst () {
            throw new UnsupportedOperationException("mutable operation on an immutable collection");
        }

        @Override public Map.Entry<K, V> pollLast () {
            throw new UnsupportedOperationException("mutable operation on an immutable collection");
        }

        @Override public ASortedSet<Map.Entry<K, V>> descendingSet () {
            return map.descendingMap().entrySet();
        }

        @Override public AIterator<Map.Entry<K, V>> descendingIterator () {
            return null; //TODO implement this
        }

        @Override public NavigableSet<Map.Entry<K, V>> subSet (Map.Entry<K, V> fromElement, boolean fromInclusive, Map.Entry<K, V> toElement, boolean toInclusive) {
            return map.subMap(fromElement.getKey(), fromInclusive, toElement.getKey(), toInclusive).entrySet();
        }

        @Override public NavigableSet<Map.Entry<K, V>> headSet (Map.Entry<K, V> toElement, boolean inclusive) {
            return map.headMap(toElement.getKey(), inclusive).entrySet();
        }

        @Override public NavigableSet<Map.Entry<K, V>> tailSet (Map.Entry<K, V> fromElement, boolean inclusive) {
            return map.tailMap(fromElement.getKey(), inclusive).entrySet();
        }

        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override public boolean equals (Object o) {
            return ASetSupport.equals(this, o);
        }
        @Override public int hashCode() {
            return ASetSupport.hashCode(this);
        }

        @Override public String toString () {
            return ACollectionSupport.toString(SortedKeySet.class, this);
        }
    }

    public static class EntryComparator<K,V> implements Comparator<Map.Entry<K,V>> { //TODO replace with Map.Entry.comparingByKey
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

    public static class MapWithDefaultValue<K,V> extends AbstractDelegatingMap<K,V> implements Serializable {
        private static final long serialVersionUID = 1L;
        private final V defaultValue;

        public MapWithDefaultValue (AMap<K, V> inner, V defaultValue) {
            super(inner);
            this.defaultValue = defaultValue;
        }

        @Override protected AMap<K,V> wrap (AMap<K,V> inner) {
            return new MapWithDefaultValue<>(inner, defaultValue);
        }

        //TODO javadoc: default applies to get() only, not contains(), getOptional(), ...
        @Override public V get (Object key) {
            //noinspection unchecked
            return inner.getOptional((K) key).orElse(defaultValue);
        }
    }

    public static class MapWithDerivedDefaultValue<K,V> extends AbstractDelegatingMap<K,V> implements Serializable {
        private static final long serialVersionUID = 1L;
        private final Function<K,V> defaultProvider;

        public MapWithDerivedDefaultValue (AMap<K, V> inner, Function<K,V> defaultProvider) {
            super(inner);
            this.defaultProvider = defaultProvider;
        }

        @Override protected AMap<K,V> wrap (AMap<K,V> inner) {
            return new MapWithDerivedDefaultValue<>(inner, defaultProvider);
        }

        //TODO javadoc: default applies to get() only, not contains(), getOptional(), ...
        @Override public V get (Object key) {
            //noinspection unchecked
            return inner.getOptional((K) key).orElseGet(() -> defaultProvider.apply((K) key));
        }
    }
}
