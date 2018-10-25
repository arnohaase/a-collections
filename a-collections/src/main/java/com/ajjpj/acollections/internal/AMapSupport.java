package com.ajjpj.acollections.internal;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.immutable.*;
import com.ajjpj.acollections.util.AOption;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


public class AMapSupport {
    public static <K,V,K1> AMap<K1, ? extends ACollectionOps<Map.Entry<K,V>>> groupBy (ACollectionOps<Map.Entry<K,V>> coll, Function<Map.Entry<K,V>, K1> keyExtractor) {
        Map<K1,ACollectionBuilder<Map.Entry<K,V>,? extends ACollectionOps<Map.Entry<K,V>>>> builders = new HashMap<>();
        //noinspection unchecked
        for(Map.Entry<K,V> o: (Iterable<Map.Entry<K,V>>) coll) {
            //noinspection unchecked
            builders.computeIfAbsent(keyExtractor.apply(o), x -> (ACollectionBuilder) coll.newEntryBuilder())
                    .add(o);
        }

        AHashMap.Builder<K1,ACollectionOps<Map.Entry<K,V>>> result = AHashMap.builder();
        for (Map.Entry<K1, ACollectionBuilder<Map.Entry<K,V>, ? extends ACollectionOps<Map.Entry<K,V>>>> e: builders.entrySet())
            result.add(e.getKey(), e.getValue().build());
        return result.build();
    }


    public static String toString (Class<?> baseClass, AMap<?,?> m) {
        final StringBuilder sb = new StringBuilder(baseClass.getSimpleName()).append("{");
        boolean isFirst = true;
        for (Map.Entry<?,?> e: m) {
            if (isFirst) isFirst = false;
            else sb.append(",");

            sb.append(e.getKey()).append("=").append(e.getValue());
        }

        sb.append("}");
        return sb.toString();
    }

    public static boolean equals(AMap<?,?> m, Object o) {
        if (o instanceof Map) {
            final Map<?,?> that = (Map<?, ?>) o;
            return m.entrySet().equals(that.entrySet());
        }

        if (! (o instanceof Collection)) return false;
        final Collection<?> that = (Collection<?>) o;
        if (m.size() != that.size()) return false;
        for (Object el: that) if (! m.contains(el)) return false;
        return true;
    }


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
            return ACollectionSupport.toString(ValueCollection.class, this);
        }
    }

    public static class EntrySet<K,V,T extends Map.Entry<K,V>> extends AbstractImmutableCollection<T> implements ASet<T>, ACollectionDefaults<T, AHashSet<T>>, ASetDefaults<T, AHashSet<T>> {
        private final AMap<K,V> map;

        public EntrySet (AMap<K, V> map) {
            this.map = map;
        }

        @Override public ATreeSet<T> toSortedSet () {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
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
            return AHashSet.fromIterator(iterator().filter(that::contains));
        }

        @Override public ASet<T> diff (Set<T> that) {
            return AHashSet.fromIterator(iterator().filterNot(that::contains));
        }

        @Override public AIterator<AHashSet<T>> subsets () {
            return ASetSupport.subsets(this, this::newBuilder);
        }

        @Override public AIterator<AHashSet<T>> subsets (int len) {
            return ASetSupport.subsets(len, this, this::newBuilder);
        }

        @Override public <K> AMap<K, AHashSet<T>> groupBy (Function<T, K> keyExtractor) {
            return ACollectionDefaults.super.groupBy(keyExtractor);
        }

        @Override public AIterator<T> iterator () {
            //noinspection unchecked
            return (AIterator) map.iterator();
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

        @Override public T min () {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }

        @Override public T max () {
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

        @Override public <K> AMap<K, ATreeSet<T>> groupBy (Function<T, K> keyExtractor) {
            return ACollectionDefaults.super.groupBy(keyExtractor);
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

    public static class SortedEntrySet<K,V,T extends Map.Entry<K,V>> extends AbstractImmutableCollection<T> implements ASortedSet<T>, ACollectionDefaults<T, ATreeSet<T>>, ASetDefaults<T, ATreeSet<T>> {
        private final ASortedMap<K,V> map;

        public SortedEntrySet (ASortedMap<K, V> map) {
            this.map = map;
        }

        @Override public Comparator<T> comparator() {
            //noinspection unchecked
            return (Comparator<T>) Map.Entry.comparingByKey((Comparator<? super K>) map.comparator());
        }

        @Override public AVector<T> toVector () {
            return AVector.from(this);
        }
        @Override public ALinkedList<T> toLinkedList () {
            return ALinkedList.from(this);
        }
        @Override public AHashSet<T> toSet () {
            return AHashSet.from(this);
        }
        @Override public ATreeSet<T> toSortedSet() {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }

        @Override public <K1> AMap<K1, ATreeSet<T>> groupBy (Function<T, K1> keyExtractor) {
            return AMapSupport.groupBy((ACollectionOps) this, (Function) keyExtractor);
        }

        @Override public T min () {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }
        @Override public T max () {
            throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
        }

        @Override public ATreeSet<T> added (T o) {
            return ATreeSet.from(this, comparator()).added(o);
        }

        @Override public ATreeSet<T> removed (T o) {
            return ATreeSet.from(this, comparator()).removed(o);
        }

        @Override public ATreeSet<T> union (Iterable<T> that) {
            return ATreeSet.builder(comparator())
                    .addAll(this)
                    .addAll(that)
                    .build();
        }

        @Override public ATreeSet<T> intersect (Set<T> that) {
            return ATreeSet.fromIterator(iterator().filter(that::contains), comparator());
        }

        @Override public ATreeSet<T> diff (Set<T> that) {
            return ATreeSet.fromIterator(iterator().filterNot(that::contains), comparator());
        }

        @Override public int countInRange (AOption<T> from, AOption<T> to) {
            return map.countInRange(from.map(Map.Entry::getKey), to.map(Map.Entry::getKey));
        }

        @Override public ASortedSet<T> range (AOption<T> from, AOption<T> until) {
            //noinspection unchecked
            return (ASortedSet<T>) map.range(from.map(Map.Entry::getKey), until.map(Map.Entry::getKey)).entrySet();
        }

        @Override public ASortedSet<T> drop (int n) {
            //noinspection unchecked
            return (ASortedSet<T>) map.drop(n).entrySet();
        }

        @Override public ASortedSet<T> take (int n) {
            //noinspection unchecked
            return (ASortedSet<T>) map.take(n).entrySet();
        }

        @Override public ASortedSet<T> slice (int from, int until) {
            //noinspection unchecked
            return (ASortedSet<T>) map.slice(from, until).entrySet();
        }

        @Override public AOption<T> smallest () {
            //noinspection unchecked
            return (AOption<T>) map.smallest();
        }

        @Override public AOption<T> greatest () {
            //noinspection unchecked
            return (AOption<T>) map.greatest();
        }

        @Override public AIterator<T> iterator (AOption<T> start) {
            //noinspection unchecked
            return (AIterator<T>) map.iterator(start.map(Map.Entry::getKey));
        }

        @Override public AIterator<T> iterator () {
            //noinspection unchecked
            return (AIterator<T>) map.iterator();
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
            //noinspection unchecked
            final ACollectionBuilder<T, ATreeSet<T>> builder = (ACollectionBuilder) ATreeSet.builder(new EntryComparator<>(map.comparator()));
            for (T o: this) if (f.test(o)) builder.add(o);
            //noinspection unchecked
            return builder.build();
        }
        @Override public ATreeSet<T> filterNot (Predicate<T> f) {
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
}
