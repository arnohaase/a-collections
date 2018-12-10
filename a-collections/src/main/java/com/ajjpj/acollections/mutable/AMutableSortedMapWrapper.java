package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.ALinkedList;
import com.ajjpj.acollections.immutable.ATreeSet;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.util.AOption;

import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * TODO javadoc
 */
public class AMutableSortedMapWrapper<K,V> implements ASortedMap<K,V> {
    private final NavigableMap<K,V> inner;

    public AMutableSortedMapWrapper (NavigableMap<K, V> inner) {
        this.inner = Objects.requireNonNull(inner);
    }

    @Override public Comparator<? super K> comparator () {
        return inner.comparator();
    }

    @Override public int countInRange (AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive) {
        return iterator(from, fromInclusive, to, toInclusive).count(x -> true);
    }

    @Override public ASortedMap<K, V> range (AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive) {
        if (from.isDefined() && to.isDefined()) return subMap(from.get(), fromInclusive, to.get(), toInclusive);
        if (from.isDefined()) return tailMap(from.get(), fromInclusive);
        if (to.isDefined()) return headMap(to.get(), toInclusive);
        return this;
    }

    @Override public ASortedMap<K, V> drop (int n) {
        return null;
    }

    @Override
    public ASortedMap<K, V> take (int n) {
        return null;
    }

    @Override
    public ASortedMap<K, V> slice (int from, int to) {
        return null;
    }

    @Override
    public AOption<Entry<K, V>> smallest () {
        return null;
    }

    @Override
    public AOption<Entry<K, V>> greatest () {
        return null;
    }

    @Override
    public ASortedSet<K> keySet () {
        return null;
    }

    @Override
    public ASortedSet<Entry<K, V>> entrySet () {
        return null;
    }

    @Override
    public AIterator<Entry<K, V>> iterator (AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive) {
        return null;
    }

    @Override
    public AIterator<K> keysIterator (AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive) {
        return null;
    }

    @Override
    public AIterator<V> valuesIterator (AOption<K> from, boolean fromInclusive, AOption<K> to, boolean toInclusive) {
        return null;
    }

    @Override
    public ASortedMap<K, V> descendingMap () {
        return null;
    }

    @Override
    public ASortedSet<K> navigableKeySet () {
        return null;
    }

    @Override
    public ASortedSet<K> descendingKeySet () {
        return null;
    }

    @Override
    public ASortedMap<K, V> subMap (K fromKey, boolean fromInclusive, K toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public ASortedMap<K, V> headMap (K toKey, boolean inclusive) {
        return null;
    }

    @Override
    public ASortedMap<K, V> tailMap (K fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public ASortedMap<K, V> subMap (K fromKey, K toKey) {
        return null;
    }

    @Override
    public ASortedMap<K, V> headMap (K toKey) {
        return null;
    }

    @Override
    public ASortedMap<K, V> tailMap (K fromKey) {
        return null;
    }

    @Override
    public boolean containsKey (Object key) {
        return false;
    }

    @Override
    public V get (Object key) {
        return null;
    }

    @Override
    public AOption<V> getOptional (K key) {
        return null;
    }

    @Override
    public AMap<K, V> plus (K key, V value) {
        return null;
    }

    @Override
    public AMap<K, V> plus (Entry<K, V> entry) {
        return null;
    }

    @Override
    public AMap<K, V> minus (K key) {
        return null;
    }

    @Override
    public AMap<K, V> filter (Predicate<Entry<K, V>> f) {
        return null;
    }

    @Override
    public AMap<K, V> filterNot (Predicate<Entry<K, V>> f) {
        return null;
    }

    @Override
    public AMap<K, V> filterKeys (Predicate<K> f) {
        return null;
    }

    @Override
    public <U> AMap<K, U> mapValues (Function<V, U> f) {
        return null;
    }

    @Override
    public AMap<K, V> withDefaultValue (V defaultValue) {
        return null;
    }

    @Override
    public AMap<K, V> withDerivedDefaultValue (Function<K, V> defaultProvider) {
        return null;
    }

    @Override
    public ACollection<V> values () {
        return null;
    }

    @Override
    public AIterator<K> keysIterator () {
        return null;
    }

    @Override
    public AIterator<V> valuesIterator () {
        return null;
    }

    @Override
    public AIterator<Entry<K, V>> iterator () {
        return null;
    }

    @Override
    public <U> ACollectionBuilder<U, ? extends ACollectionOps<U>> newBuilder () {
        return null;
    }

    @Override
    public Entry<K, V> head () {
        return null;
    }

    @Override
    public AOption<Entry<K, V>> headOption () {
        return null;
    }

    @Override
    public ALinkedList<Entry<K, V>> toLinkedList () {
        return null;
    }

    @Override
    public AVector<Entry<K, V>> toVector () {
        return null;
    }

    @Override
    public AHashSet<Entry<K, V>> toSet () {
        return null;
    }

    @Override
    public ATreeSet<Entry<K, V>> toSortedSet (Comparator<Entry<K, V>> comparator) {
        return null;
    }

    @Override
    public <K, V> AMap<K, V> toMap () {
        return null;
    }

    @Override
    public AMutableListWrapper<Entry<K, V>> toMutableList () {
        return null;
    }

    @Override
    public AMutableSetWrapper<Entry<K, V>> toMutableSet () {
        return null;
    }

    @Override
    public <U> ACollection<U> map (Function<Entry<K, V>, U> f) {
        return null;
    }

    @Override
    public <U> ACollection<U> flatMap (Function<Entry<K, V>, Iterable<U>> f) {
        return null;
    }

    @Override
    public <U> ACollection<U> collect (Predicate<Entry<K, V>> filter, Function<Entry<K, V>, U> f) {
        return null;
    }

    @Override
    public <U> AOption<U> collectFirst (Predicate<Entry<K, V>> filter, Function<Entry<K, V>, U> f) {
        return null;
    }

    @Override
    public AOption<Entry<K, V>> find (Predicate<Entry<K, V>> f) {
        return null;
    }

    @Override
    public boolean forall (Predicate<Entry<K, V>> f) {
        return false;
    }

    @Override
    public boolean exists (Predicate<Entry<K, V>> f) {
        return false;
    }

    @Override
    public int count (Predicate<Entry<K, V>> f) {
        return 0;
    }

    @Override
    public boolean contains (Object o) {
        return false;
    }

    @Override
    public Entry<K, V> reduceLeft (BiFunction<Entry<K, V>, Entry<K, V>, Entry<K, V>> f) {
        return null;
    }

    @Override
    public AOption<Entry<K, V>> reduceLeftOption (BiFunction<Entry<K, V>, Entry<K, V>, Entry<K, V>> f) {
        return null;
    }

    @Override
    public <U> U foldLeft (U zero, BiFunction<U, Entry<K, V>, U> f) {
        return null;
    }

    @Override
    public <K1> AMap<K1, ? extends ACollectionOps<Entry<K, V>>> groupBy (Function<Entry<K, V>, K1> keyExtractor) {
        return null;
    }

    @Override
    public Entry<K, V> min () {
        return null;
    }

    @Override
    public Entry<K, V> min (Comparator<Entry<K, V>> comparator) {
        return null;
    }

    @Override
    public Entry<K, V> max () {
        return null;
    }

    @Override
    public Entry<K, V> max (Comparator<Entry<K, V>> comparator) {
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
    public Entry<K, V> lowerEntry (K key) {
        return null;
    }

    @Override
    public K lowerKey (K key) {
        return null;
    }

    @Override
    public Entry<K, V> floorEntry (K key) {
        return null;
    }

    @Override
    public K floorKey (K key) {
        return null;
    }

    @Override
    public Entry<K, V> ceilingEntry (K key) {
        return null;
    }

    @Override
    public K ceilingKey (K key) {
        return null;
    }

    @Override
    public Entry<K, V> higherEntry (K key) {
        return null;
    }

    @Override
    public K higherKey (K key) {
        return null;
    }

    @Override
    public Entry<K, V> firstEntry () {
        return null;
    }

    @Override
    public Entry<K, V> lastEntry () {
        return null;
    }

    @Override
    public Entry<K, V> pollFirstEntry () {
        return null;
    }

    @Override
    public Entry<K, V> pollLastEntry () {
        return null;
    }

    @Override
    public K firstKey () {
        return null;
    }

    @Override
    public K lastKey () {
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
    public boolean containsValue (Object value) {
        return false;
    }

    @Override
    public V put (K key, V value) {
        return null;
    }

    @Override
    public V remove (Object key) {
        return null;
    }

    @Override
    public void putAll (Map<? extends K, ? extends V> m) {

    }

    @Override
    public void clear () {

    }

    @Override
    public int countInRange (AOption<K> from, AOption<K> to) {
        return 0;
    }

    @Override
    public Spliterator<Entry<K, V>> spliterator () {
        return null;
    }

    @Override
    public V getOrDefault (Object key, V defaultValue) {
        return null;
    }

    @Override
    public void forEach (BiConsumer<? super K, ? super V> action) {

    }

    @Override
    public void replaceAll (BiFunction<? super K, ? super V, ? extends V> function) {

    }

    @Override
    public V putIfAbsent (K key, V value) {
        return null;
    }

    @Override
    public boolean remove (Object key, Object value) {
        return false;
    }

    @Override
    public boolean replace (K key, V oldValue, V newValue) {
        return false;
    }

    @Override
    public V replace (K key, V value) {
        return null;
    }

    @Override
    public V computeIfAbsent (K key, Function<? super K, ? extends V> mappingFunction) {
        return null;
    }

    @Override
    public V computeIfPresent (K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V compute (K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return null;
    }

    @Override
    public V merge (K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return null;
    }
}