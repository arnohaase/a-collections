package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AList;
import com.ajjpj.acollections.AbstractAIterator;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AListDefaults;
import com.ajjpj.acollections.internal.AListSupport;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;
import java.util.function.Function;
import java.util.function.Predicate;


public class ARange extends AbstractImmutableCollection<Integer> implements AList<Integer>, AListDefaults<Integer, AList<Integer>>, RandomAccess, Serializable {
    private final int from, to, step;

    public static ARange empty() {
        return new ARange(1, 1, 1);
    }
    public static ARange create (int from, int to) { //TODO inclusive or exclusive?
        return create (from, to, (from < to) ? 1 : -1);
    }
    public static ARange create (int from, int to, int step) {
        return new ARange(from, to, step);
    }

    private ARange (int from, int to, int step) {
        this.from = from;
        this.to = to;
        this.step = step;

        if (step == 0) throw new IllegalArgumentException("step must not be 0");
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override public boolean equals (Object o) {
        return AListSupport.equals(this, o);
    }

    @Override public <U> ACollectionBuilder<U, AVector<U>> newBuilder () {
        return AVector.builder();
    }

    @Override public AList<Integer> prepend (Integer o) {
        return this.<Integer>newBuilder().add(o).addAll(this).build();
    }

    @Override public AList<Integer> append (Integer o) {
        return this.<Integer>newBuilder().addAll(this).add(o).build();
    }

    @Override public AList<Integer> concat (Iterator<? extends Integer> that) {
        return this.<Integer>newBuilder().addAll(this).addAll(that).build();
    }

    @Override public AList<Integer> concat (Iterable<? extends Integer> that) {
        return this.<Integer>newBuilder().addAll(this).addAll(that).build();
    }

    @Override public AList<Integer> updated (int idx, Integer o) {
        return toVector().updated(idx, o);
    }

    @Override public Integer last () {
        if(isEmpty()) throw new NoSuchElementException();
        return get(size() - 1);
    }

    @Override public AList<Integer> take (int n) {
        return n >= size() ? this : new ARange(from, from + n*step, step);
    }

    @Override public AList<Integer> takeRight (int n) {
        return n >= size() ? this : new ARange(last() - (n-1)*step, to, step);
    }

    @Override public AList<Integer> takeWhile (Predicate<Integer> f) {
        final AVector.Builder<Integer> builder = AVector.builder();
        final AIterator<Integer> it = iterator();
        Integer next;
        while (it.hasNext() && (f.test(next = it.next()))) builder.add(next);
        return builder.build();
    }

    @Override public AList<Integer> drop (int n) {
        return size() <= n ? AVector.empty() : new ARange(from + n*step, to, step);
    }

    @Override public AList<Integer> dropRight (int n) {
        return size() <= n ? AVector.empty() : new ARange(from, to - n*step, step);
    }

    @Override public AList<Integer> dropWhile (Predicate<Integer> f) {
        final AVector.Builder<Integer> builder = AVector.builder();
        final AIterator<Integer> it = iterator();
        while (it.hasNext()) {
            final Integer next = it.next();
            if (! f.test(next)) {
                builder.add(next);
                break;
            }
        }
        builder.addAll(it);
        return builder.build();
    }

    @Override public AList<Integer> reverse () {
        if (isEmpty()) return this;
        return new ARange(last(), from + (step > 0 ? -1 : 1), -step);
    }

    @Override public AIterator<Integer> reverseIterator () {
        return reverse().iterator();
    }

    //TODO potential for optimization: listIterator

    @Override public <U> AList<U> map (Function<Integer, U> f) {
        return ACollectionSupport.map(newBuilder(), this, f);
    }

    @Override public <U> AList<U> flatMap (Function<Integer, Iterable<U>> f) {
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }

    @Override public <U> AList<U> collect (Predicate<Integer> filter, Function<Integer, U> f) {
        return ACollectionSupport.collect(newBuilder(), this, filter, f);
    }

    @Override public AList<Integer> filter (Predicate<Integer> f) {
        return AListDefaults.super.filter(f);
    }

    @Override public AList<Integer> filterNot (Predicate<Integer> f) {
        return AListDefaults.super.filterNot(f);
    }

    @Override public Integer min () {
        if (isEmpty()) throw new NoSuchElementException();
        return step > 0 ? from : last();
    }

    @Override public Integer max () {
        if (isEmpty()) throw new NoSuchElementException();
        return step < 0 ? from : last();
    }

    @Override public AIterator<Integer> iterator () {
        if (step > 0)
            return new AbstractAIterator<Integer>() {
                Integer next = from;

                @Override public boolean hasNext () {
                    return next < to;
                }

                @Override public Integer next () {
                    if (!hasNext()) throw new NoSuchElementException();
                    final Integer result = next;
                    next += step;
                    return result;
                }
            };
        else
            return new AbstractAIterator<Integer>() {
                Integer next = from;

                @Override public boolean hasNext () {
                    return next > to;
                }

                @Override public Integer next () {
                    if (!hasNext()) throw new NoSuchElementException();
                    final Integer result = next;
                    next += step;
                    return result;
                }
            };
    }

    @Override public boolean isEmpty () {
        return size() == 0;
    }

    @Override public String toString () {
        return "ARange[" + from + " to " + to + " step " + step + "]";
    }

    @Override public boolean addAll (int index, Collection<? extends Integer> c) {
        throw new UnsupportedOperationException();
    }

    @Override public Integer get (int index) {
        if (index < 0 || index >= size()) throw new IndexOutOfBoundsException();
        return from + index*step;
    }

    @Override public Integer set (int index, Integer element) {
        throw new UnsupportedOperationException();
    }

    @Override public void add (int index, Integer element) {
        throw new UnsupportedOperationException();
    }

    @Override public Integer remove (int index) {
        throw new UnsupportedOperationException();
    }

    @Override public int indexOf (Object o) {
        if (! (o instanceof Integer)) return -1;

        final Integer i = (Integer) o;
        if (step > 0) {
            if (i < from || i >= to) return -1;

            final int fromStart = ((Integer)o) - from;
            if (fromStart % step != 0) return -1;

            return fromStart / step;
        }
        else {
            if (i > from || i <= to) return -1;

            final int fromStart = ((Integer)o) - from;
            if (fromStart % (-step) != 0) return -1;

            return fromStart / step;
        }
    }

    @Override public int lastIndexOf (Object o) {
        return indexOf(o);
    }

    @Override public AList<Integer> subList (int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size()) throw new IndexOutOfBoundsException();
        if (fromIndex > toIndex) throw new IllegalArgumentException();

        return new ARange(from + fromIndex*step, from + toIndex*step, step);
    }

    @Override public int size () {
        if (step > 0) return (to - from + step - 1) / step;
        else return (from - to - step - 1) / (-step); // rounding rules
    }
}
