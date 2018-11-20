package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.ASetDefaults;
import com.ajjpj.acollections.internal.ASetSupport;
import com.ajjpj.acollections.util.AOption;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * This class implements immutable sets using a red-black tree, sorting elements based on a {@link Comparator} and providing a
 *  {@link SortedSet}.
 *
 * <p> Since this is an immutable class, it does not support modifying methods from {@link java.util.Set}: Those methods return
 *  {@code boolean} or a previous element, but in order to "modify" an immutable collection, they would need to return the new collection
 *  instance.
 *
 * <p> So instances of this class rely on methods like {@link #plus(Object)} or {@link #minus(Object)} for adding / removing
 *  elements. These methods return new sets with the new elements, leaving the original unmodified:
 *
 * <p>{@code ASet<Integer> s0 = ATreeSet.of(1, 2, 3);}
 * <p>{@code ASet<Integer> s1 = s0.plus(5);}
 * <p>{@code ASet<Integer> s2 = s1.minus(2);}
 * <p>{@code System.out.println(s0); // 1, 2, 3 }
 * <p>{@code System.out.println(s1); // 1, 2, 3, 5 }
 * <p>{@code System.out.println(s2); // 1, 3, 5 }
 *
 * <p> These calls can of course be chained:

 * <p>{@code ASet<Integer> s3 = s2.plus(8).plus(9).minus(3); }
 * <p>{@code System.out.println(s3); // 1, 5, 8, 9 }
 *
 * <p> This class has static factory methods (Java 9 style) for convenience creating instances.
 *
 * <p> Implementation note: This class is a port of Scala's standard library {@code TreeSet}.
 *
 * @param <T>
 */
public class ATreeSet<T> extends AbstractImmutableCollection<T> implements ASortedSet<T>, ACollectionDefaults<T,ATreeSet<T>>, ASetDefaults<T,ATreeSet<T>>, Serializable {
    private final RedBlackTree.Tree<T,Object> root;
    private final Comparator<T> comparator;

    public static <T extends Comparable<T>> ATreeSet<T> empty() {
        return empty(Comparator.<T>naturalOrder());
    }
    public static <T> ATreeSet<T> empty(Comparator<T> comparator) {
        return new ATreeSet<>(null, comparator);
    }

    public static <T extends Comparable<T>> ATreeSet<T> of() {
        return empty();
    }
    public static <T extends Comparable<T>> ATreeSet<T> of(T o) {
        return ATreeSet
                .<T>builder()
                .add(o)
                .build();
    }
    public static <T extends Comparable<T>> ATreeSet<T> of(T o1, T o2) {
        return ATreeSet
                .<T>builder()
                .add(o1)
                .add(o2)
                .build();
    }
    public static <T extends Comparable<T>> ATreeSet<T> of(T o1, T o2, T o3) {
        return ATreeSet
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .build();
    }
    public static <T extends Comparable<T>> ATreeSet<T> of(T o1, T o2, T o3, T o4) {
        return ATreeSet
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .add(o4)
                .build();
    }
    @SafeVarargs public static <T extends Comparable<T>> ATreeSet<T> of(T o1, T o2, T o3, T o4, T o5, T... others) {
        return ATreeSet
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .add(o4)
                .add(o5)
                .addAll(others)
                .build();
    }


    public static <T extends Comparable<T>> ATreeSet<T> from(T[] that) {
        return from(that, Comparator.naturalOrder());
    }
    public static <T> ATreeSet<T> from(T[] that, Comparator<T> comparator) {
        return fromIterator(Arrays.asList(that).iterator(), comparator);
    }
    public static <T extends Comparable<T>> ATreeSet<T> from(Iterable<T> iterable) {
        return from(iterable, Comparator.naturalOrder());
    }
    public static <T> ATreeSet<T> from(Iterable<T> iterable, Comparator<T> comparator) {
        return builder(comparator).addAll(iterable).build();
    }
    public static <T extends Comparable<T>> ATreeSet<T> fromIterator(Iterator<T> it) {
        return fromIterator(it, Comparator.naturalOrder());
    }
    public static <T> ATreeSet<T> fromIterator(Iterator<T> it, Comparator<T> comparator) {
        return builder(comparator).addAll(it).build();
    }

    public static <T extends Comparable<T>> Builder<T> builder() {
        return builder(Comparator.<T>naturalOrder());
    }
    public static <T> Builder<T> builder(Comparator<T> comparator) {
        return new Builder<>(comparator);
    }

    private ATreeSet (RedBlackTree.Tree<T, Object> root, Comparator<T> comparator) {
        this.root = root;
        this.comparator = comparator;
    }

    @Override public Comparator<T> comparator () {
        return comparator;
    }

    @Override public boolean equals (Object o) {
        return ASetSupport.equals(this, o);
    }
    @Override public int hashCode() {
        return ASetSupport.hashCode(this);
    }

    @Override public String toString () {
        return ACollectionSupport.toString(ATreeSet.class, this);
    }

    @Override public AHashSet<T> toSet () {
        return AHashSet.from(this);
    }
    @Override public ATreeSet<T> plus (T o) {
        return new ATreeSet<>(RedBlackTree.update(root, o, null, true, comparator), comparator);
    }
    @Override public ATreeSet<T> minus (T o) {
        return new ATreeSet<>(RedBlackTree.delete(root, o, comparator), comparator);
    }

    @Override public ATreeSet<T> union (Iterable<T> that) {
        ATreeSet<T> result = this;
        for (T o: that) result = result.plus(o);
        return result;
    }

    @Override public ATreeSet<T> intersect (Set<T> that) {
        return filter(that::contains);
    }

    @Override public ATreeSet<T> diff (Set<T> that) {
        ATreeSet<T> result = this;
        for (T o: that) result = result.minus(o);
        return result;
    }

    @Override public int countInRange (AOption<T> from, AOption<T> to) {
        return RedBlackTree.countInRange(root, from, to, comparator);
    }

    @Override public ATreeSet<T> range (AOption<T> from, AOption<T> until) {
        return new ATreeSet<>(RedBlackTree.rangeImpl(root, from, until, comparator), comparator);
    }

    @Override public ASortedSet<T> drop (int n) {
        return new ATreeSet<>(RedBlackTree.drop(root, n), comparator);
    }

    @Override public ASortedSet<T> take (int n) {
        return new ATreeSet<>(RedBlackTree.take(root, n), comparator);
    }

    @Override public ASortedSet<T> slice (int from, int until) {
        return new ATreeSet<>(RedBlackTree.slice(root, from, until), comparator);
    }

    /**
     * relative to *natural* order, which may or may not be the tree's order
     */
    @Override public T min () {
        if (comparator.equals(Comparator.naturalOrder()))
            return smallest().orElseThrow(NoSuchElementException::new);
        else
            return ACollectionDefaults.super.min();
    }
    /**
     * relative to *natural* order, which may or may not be the tree's order
     */
    @Override public T max () {
        if (comparator.equals(Comparator.naturalOrder()))
            return greatest().orElseThrow(NoSuchElementException::new);
        else
            return ACollectionDefaults.super.max();
    }

    @Override public AOption<T> smallest () {
        final RedBlackTree.Tree<T,?> raw = RedBlackTree.smallest(root);
        return raw == null ? AOption.none() : AOption.some(raw.key);
    }

    @Override public AOption<T> greatest () {
        final RedBlackTree.Tree<T,?> raw = RedBlackTree.greatest(root);
        return raw == null ? AOption.none() : AOption.some(raw.key);
    }

    @Override public AIterator<T> iterator (AOption<T> start) {
        return RedBlackTree.keysIterator(root, start, comparator);
    }

    @Override public AIterator<T> iterator () {
        return RedBlackTree.keysIterator(root, AOption.none(), comparator);
    }

    @Override public <U> ACollectionBuilder<U, ATreeSet<U>> newBuilder () {
        return new Builder<U>((Comparator) comparator); //TODO this is somewhat hacky - better way?
    }

    @Override public boolean isEmpty () {
        return root == null;
    }

    @Override public <U> ATreeSet<U> map (Function<T, U> f) {
        return ACollectionSupport.map(newBuilder(), this, f);
    }

    @Override public <U> ATreeSet<U> flatMap (Function<T, Iterable<U>> f) {
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }

    @Override public <U> ATreeSet<U> collect (Predicate<T> filter, Function<T, U> f) {
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
        return RedBlackTree.count(root);
    }

    @Override public boolean contains (Object o) {
        //noinspection unchecked
        return RedBlackTree.lookup(root, (T) o, comparator) != null;
    }

    @Override public AIterator<ATreeSet<T>> subsets () {
        return ASetDefaults.super.subsets();
    }

    @Override public AIterator<ATreeSet<T>> subsets (int len) {
        return ASetDefaults.super.subsets(len);
    }

    @Override public boolean containsAll (Collection<?> c) {
        return ACollectionDefaults.super.containsAll(c);
    }

    public static class Builder<T> implements ACollectionBuilder<T,ATreeSet<T>> {
        private RedBlackTree.Tree<T,Object> root = null;
        private final Comparator<T> comparator;

        public Builder (Comparator<T> comparator) {
            this.comparator = comparator;
        }

        @Override public ACollectionBuilder<T, ATreeSet<T>> add (T el) {
            root = RedBlackTree.update(root, el, null, true, comparator);
            return this;
        }

        @Override public ATreeSet<T> build () {
            return new ATreeSet<>(root, comparator);
        }
    }
}
