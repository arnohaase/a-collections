package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AMap;
import com.ajjpj.acollections.internal.ACollectionDefaults;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.ASetDefaults;
import com.ajjpj.acollections.internal.ASetSupport;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * This class implements immutable sets using a hash trie. Elements must implement a valid {@link Object#hashCode()} method; in particular,
 *  the hash code must be <em>stable</em> and <em>consistent with equality</em>.
 *
 * <p> Since this is an immutable class, it does not support modifying methods from {@link java.util.Set}: Those methods return
 *  {@code boolean} or a previous element, but in order to "modify" an immutable collection, they would need to return the new collection
 *  instance.
 *
 * <p> So instances of this class rely on methods like {@link #plus(Object)} or {@link #minus(Object)} for adding / removing
 *  elements. These methods return new sets with the new elements, leaving the original unmodified:
 *
 * <p>{@code ASet<Integer> s0 = AHashSet.of(1, 2, 3);}
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
 * <p> Implementation note: This is a port of Scala's standard library {@code HashMap}. It uses some optimization ideas from
 *      the <a href="https://github.com/andrewoma/dexx">Dexx collections library</a>.
 *
 * @param <T> the set's element type
 */
public class AHashSet<T> extends AbstractImmutableCollection<T> implements ACollectionDefaults<T, AHashSet<T>>, ASetDefaults<T, AHashSet<T>>, Serializable {
    private final CompactHashMap<EqualsSetEntry<T>> compactHashMap;

    /**
     * Convenience method for creating an empty {@link AHashSet}. This can later be modified by calling {@link #plus(Object)} or
     * {@link #minus(Object)}. For creating a set with known elements, calling one of the {@code of} factory methods is usually more concise.
     *
     * @param <T> the new set's element type
     * @return an empty {@link AHashSet}
     */
    public static<T> AHashSet<T> empty() {
        return new AHashSet<>(CompactHashMap.empty());
    }

    /**
     * This is an alias for {@link #empty()} for consistency with Java 9 conventions - it creates an empty {@link AHashSet}.
     *
     * @param <T> the new set's element type
     * @return an empty {@link AHashSet}
     */
    public static <T> AHashSet<T> of() {
        return empty();
    }

    /**
     * Convenience factory method creating an {@link AHashSet} with exactly one element.
     *
     * @param o the single element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AHashSet}
     */
    public static <T> AHashSet<T> of(T o) {
        return AHashSet
                .<T>builder()
                .add(o)
                .build();
    }

    /**
     * Convenience factory method creating an {@link AHashSet} with two elements.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AHashSet}
     */
    public static <T> AHashSet<T> of(T o1, T o2) {
        return AHashSet
                .<T>builder()
                .add(o1)
                .add(o2)
                .build();
    }

    /**
     * Convenience factory method creating an {@link AHashSet} with three elements.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param o3 the third element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AHashSet}
     */
    public static <T> AHashSet<T> of(T o1, T o2, T o3) {
        return AHashSet
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .build();
    }

    /**
     * Convenience factory method creating an {@link AHashSet} with four elements.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param o3 the third element for the new set
     * @param o4 the fourth element for the new set
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AHashSet}
     */
    public static <T> AHashSet<T> of(T o1, T o2, T o3, T o4) {
        return AHashSet
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .add(o4)
                .build();
    }

    /**
     * Convenience factory method creating an {@link AHashSet} with more than four elements.
     *
     * @param o1 the first element for the new set
     * @param o2 the second element for the new set
     * @param o3 the third element for the new set
     * @param o4 the fourth element for the new set
     * @param o5 the fifth element for the new set
     * @param others the (variable number of) additional elements
     * @param <T> the new set's element type (can often be inferred from the parameter by the compiler)
     * @return the new {@link AHashSet}
     */
    @SafeVarargs public static <T> AHashSet<T> of(T o1, T o2, T o3, T o4, T o5, T... others) {
        return AHashSet
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .add(o4)
                .add(o5)
                .addAll(others)
                .build();
    }

    /**
     * Creates a new {@link AHashSet} based on an array's elements.
     *
     * @param that the array from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    public static <T> AHashSet<T> from(T[] that) {
        return fromIterator(Arrays.asList(that).iterator());
    }

    /**
     * Creates a new {@link AHashSet} based on an {@link Iterable}'s elements.
     *
     * @param that the {@link Iterable} from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    public static <T> AHashSet<T> from(Iterable<T> that) {
        return fromIterator(that.iterator());
    }

    /**
     * Creates a new {@link AHashSet} based on an {@link Iterator}'s elements.
     *
     * @param it the {@link Iterator} from which the new set is initialized
     * @param <T> the set's element type
     * @return the new set
     */
    public static <T> AHashSet<T> fromIterator(Iterator<T> it) {
        return AHashSet.<T>builder()
                .addAll(it)
                .build();
    }

    private AHashSet (CompactHashMap<EqualsSetEntry<T>> compactHashMap) {
        this.compactHashMap = compactHashMap;
    }

    protected Object writeReplace() {
        return new SerializationProxy(this);
    }

    @Override public <U> ACollectionBuilder<U, AHashSet<U>> newBuilder() {
        return new Builder<>();
    }

    @Override public boolean equals (Object o) {
        return ASetSupport.equals(this, o);
    }
    @Override public int hashCode() {
        return ASetSupport.hashCode(this);
    }

    @Override public String toString () {
        return ACollectionSupport.toString(AHashSet.class, this);
    }

    @Override public AHashSet<T> toSet () {
        return this;
    }

    @Override public AHashSet<T> plus (T o) {
        return new AHashSet<>(compactHashMap.updated0(new EqualsSetEntry<>(o), 0));
    }

    @Override public AHashSet<T> minus (T o) {
        return new AHashSet<>(compactHashMap.removed0(new EqualsSetEntry<>(o), 0));
    }

    @Override public boolean contains (Object o) {
        //noinspection unchecked
        return compactHashMap.get0(new EqualsSetEntry<>((T)o), 0) != null;
    }

    @Override public AHashSet<T> union (Iterable<? extends T> that) {
        AHashSet<T> result = this;
        for (T o: that)
            result = result.plus(o);
        return result;
    }

    @Override public AHashSet<T> intersect (Set<T> that) {
        return filter(that::contains);
    }

    @Override public AHashSet<T> diff (Set<T> that) {
        AHashSet<T> result = this;
        for (T o: that)
            result = result.minus(o);
        return result;
    }

    @Override public AIterator<T> iterator () {
        return compactHashMap.iterator().map(e -> e.el);
    }

    @Override public boolean isEmpty () {
        return compactHashMap.isEmpty();
    }

    @Override public <U> AHashSet<U> map (Function<T, U> f) {
        return ACollectionSupport.map(newBuilder(), this, f);
    }

    @Override public <U> AHashSet<U> flatMap (Function<T, Iterable<U>> f) {
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }

    @Override public <U> AHashSet<U> collect (Predicate<T> filter, Function<T, U> f) {
        return ACollectionSupport.collect(newBuilder(), this, filter, f);
    }

    @Override public AHashSet<T> filter (Predicate<T> f) {
        return ACollectionDefaults.super.filter(f);
    }

    @Override public AHashSet<T> filterNot (Predicate<T> f) {
        return filter(f.negate());
    }

    @Override public <K> AMap<K, AHashSet<T>> groupBy (Function<T, K> keyExtractor) {
        return ACollectionDefaults.super.groupBy(keyExtractor);
    }

    @Override public int size () {
        return compactHashMap.size();
    }

    @Override public boolean containsAll (Collection<?> c) {
        return ACollectionDefaults.super.containsAll(c);
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> implements ACollectionBuilder<T, AHashSet<T>> {
        @SuppressWarnings("unchecked")
        CompactHashMap<EqualsSetEntry<T>> result = CompactHashMap.EMPTY;

        @Override public ACollectionBuilder<T, AHashSet<T>> add (T el) {
            result = result.updated0(new EqualsSetEntry<>(el), 0);
            return this;
        }

        @Override public AHashSet<T> build () {
            return new AHashSet<>(result);
        }
    }

    private static class EqualsSetEntry<T> implements CompactHashMap.EntryWithEquality {
        final T el;

        EqualsSetEntry (T el) {
            this.el = el;
        }

        @Override public boolean hasEqualKey (CompactHashMap.EntryWithEquality other) {
            return Objects.equals(el, ((EqualsSetEntry) other).el);
        }

        @Override public int keyHash () {
            return Objects.hashCode(el);
        }
    }

    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 123L;

        private transient AHashSet<?> orig;

        SerializationProxy (AHashSet<?> orig) {
            this.orig = orig;
        }

        private void writeObject(ObjectOutputStream oos) throws IOException {
            oos.writeInt(orig.size());
            for (Object o: orig) {
                oos.writeObject(o);
            }
        }

        private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
            final Builder<Object> builder = builder();
            final int size = ois.readInt();
            for (int i=0; i<size; i++) {
                builder.add(ois.readObject());
            }
            orig = builder.build();
        }

        private Object readResolve() {
            return orig;
        }
    }
}
