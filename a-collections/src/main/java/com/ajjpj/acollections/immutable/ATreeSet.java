package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.util.AEquality;
import com.ajjpj.acollections.util.AOption;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


public class ATreeSet<T> extends AbstractImmutableCollection<T> implements ASortedSet<T>, ACollectionDefaults<T,ATreeSet<T>> {
    private final RedBlackTree.Tree<T,Object> root;
    private final Comparator<T> comparator;

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

    @Override public String toString () {
        return ACollectionSupport.toString(ATreeSet.class, this);
    }

    @Override public AHashSet<T> toSet () {
        return AHashSet.from(this, AEquality.EQUALS);
    }
    @Override public ATreeSet<T> toSortedSet (Comparator<T> comparator) {
        return this;
    }

    @Override public ATreeSet<T> added (T o) {
        return new ATreeSet<>(RedBlackTree.update(root, o, null, true, comparator), comparator);
    }
    @Override public ATreeSet<T> removed (T o) {
        return new ATreeSet<>(RedBlackTree.delete(root, o, comparator), comparator);
    }

    @Override public ATreeSet<T> union (Iterable<T> that) {
        ATreeSet<T> result = this;
        for (T o: that) result = result.added(o);
        return result;
    }

    @Override public ATreeSet<T> intersect (Set<T> that) {
        return filter(that::contains);
    }

    @Override public ATreeSet<T> diff (Set<T> that) {
        ATreeSet<T> result = this;
        for (T o: that) result = result.removed(o);
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

    @Override public AIterator<ASortedSet<T>> subsets () {
        return null; //TODO
    }

    @Override public AEquality equality () {
        return AEquality.fromComparator(comparator);
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

    @Override public int size () {
        return RedBlackTree.count(root);
    }

    @Override public Object[] toArray () {
        return ACollectionSupport.toArray(this);
    }

    @Override public <T1> T1[] toArray (T1[] a) {
        return ACollectionSupport.toArray(this, a);
    }

    @Override public boolean contains (Object o) {
        //noinspection unchecked
        return RedBlackTree.lookup(root, (T) o, comparator) != null;
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

        @Override public AEquality equality () {
            return AEquality.fromComparator(comparator);
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
