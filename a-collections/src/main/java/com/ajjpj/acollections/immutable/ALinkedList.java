package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AList;
import com.ajjpj.acollections.AbstractAIterator;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AListDefaults;
import com.ajjpj.acollections.util.AEquality;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.function.Predicate;


public abstract class ALinkedList<T> extends AbstractImmutableCollection<T> implements AListDefaults<T, ALinkedList<T>> {
    private final AEquality equality;

    private ALinkedList (AEquality equality) {
        this.equality = equality;
    }

    public static <T> ALinkedList<T> from(Iterable<T> that) {
        return fromIterator(that.iterator());
    }
    public static <T> ALinkedList<T> from(Iterable<T> that, AEquality equality) {
        return fromIterator(that.iterator(), equality);
    }

    public static <T> ALinkedList<T> fromIterator(Iterator<T> it) {
        return fromIterator(it, AEquality.EQUALS);
    }
    public static <T> ALinkedList<T> fromIterator(Iterator<T> it, AEquality equality) {
        return ALinkedList
                .<T>builder(equality)
                .addAll(it)
                .build();
    }

    public static <T> ALinkedList<T> of(T o) {
        return ALinkedList.<T>nil().prepend(o);
    }
    public static <T> ALinkedList<T> of(T o1, T o2) {
        return ALinkedList.<T>nil().prepend(o2).prepend(o1);
    }
    public static <T> ALinkedList<T> of(T o1, T o2, T o3) {
        return ALinkedList.<T>nil().prepend(o3).prepend(o2).prepend(o1);
    }
    public static <T> ALinkedList<T> of(T o1, T o2, T o3, T o4) {
        return ALinkedList.<T>nil().prepend(o4).prepend(o3).prepend(o2).prepend(o1);
    }


    public static <T> ALinkedList<T> empty(AEquality equality) {
        return nil(equality);
    }
    public static <T> ALinkedList<T> empty() {
        return nil();
    }
    public static <T> ALinkedList<T> nil() {
        return nil(AEquality.EQUALS);
    }

    public static <T> ALinkedList<T> nil(AEquality equality) {
        if (equality == AEquality.EQUALS)
            //noinspection unchecked
            return Nil.EQUALS;
        return new Nil<>(equality);
    }

    @Override public <U> Builder<U> newBuilder () {
        return builder(equality);
    }

    @Override public ALinkedList<T> prepend(T o) {
        return new HeadTail<>(o, this, equality);
    }

    @Override public ALinkedList<T> append(T o) {
        return ALinkedList
                .<T>builder(equality)
                .addAll(this)
                .add(o)
                .build();
    }

    @Override public ALinkedList<T> concat (Iterator<? extends T> that) {
        return ALinkedList
                .<T>builder(equality)
                .addAll(this)
                .addAll(that)
                .build();
    }
    @Override public AList<T> concat (Iterable<? extends T> that) {
        return concat(that.iterator());
    }

    @Override public ALinkedList<T> updated (int idx, T o) {
        if (idx < 0) throw new IndexOutOfBoundsException();
        try {
            final Builder<T> builder = new Builder<>(equality);

            ALinkedList<T> l = this;
            for(int i=0; i<idx; i++) {
                builder.add(l.head());
                l = l.tail();
            }

            builder.add(o);
            l = l.tail();

            for (T el: l)
                builder.add(el);
            return builder.build();
        }
        catch (NoSuchElementException e) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override public AEquality equality () {
        return equality;
    }

    @Override public T last () {
        if (isEmpty()) throw new NoSuchElementException();
        ALinkedList<T> l = this;
        while(l.tail().nonEmpty())
            l = l.tail();
        return l.head();
    }

    @Override public ALinkedList<T> init () {
        if(isEmpty()) throw new NoSuchElementException();
        return dropRight(1);
    }

    @Override public ALinkedList<T> take (int n) {
        if (n < 0) return nil();
        final Builder<T> builder = new Builder<>(equality);
        ALinkedList<T> l = this;
        for(int i=0; i<n; i++) {
            if (l.isEmpty()) break;
            builder.add(l.head());
            l = l.tail();
        }
        return builder.build();
    }

    @Override public ALinkedList<T> takeRight (int n) {
        if (n < 0) throw new IllegalArgumentException();
        return drop(size() - n);
    }

    @Override public ALinkedList<T> drop (int n) {
        if (n < 0) return this; //throw new IllegalArgumentException();
        ALinkedList<T> l = this;
        for (int i=0; i<n; i++) {
            if(l.isEmpty()) return nil();
            l = l.tail();
        }
        return l;
    }

    @Override public ALinkedList<T> dropRight (int n) {
        if (n < 0) return this;
        return take(size() - n);
    }

    @Override public ALinkedList<T> dropWhile (Predicate<T> f) {
        // this is a more efficient implementation than the generic, builder-based code from AListDefaults
        ALinkedList<T> l = this;
        while (l.nonEmpty() && f.test(l.head()))
            l = l.tail();
        return l;
    }

    @Override public <U> ALinkedList<U> collect (Predicate<T> filter, Function<T, U> f) {
        return ACollectionSupport.collect(newBuilder(), this, filter, f);
    }

    @Override public ALinkedList<T> reverse () {
        // This is a more efficient implementation than the generic builder-based code from AListDefaults
        ALinkedList<T> result = nil(equality);
        for (T o: this) {
            result = result.prepend(o);
        }
        return result;
    }

    @Override public AIterator<T> reverseIterator () {
        return toVector().reverseIterator();
    }

    @Override public ALinkedList<T> subList (int fromIndex, int toIndex) {
        if (fromIndex < 0) throw new IndexOutOfBoundsException();
        final ALinkedList<T> result = drop(fromIndex).take(toIndex - fromIndex);
        if (result.size() != toIndex - fromIndex) throw new IndexOutOfBoundsException();
        return result;
    }

    @Override public boolean addAll (int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override public T get (int index) {
        ALinkedList<T> l = this;
        for (int i=0; i<index; i++) {
            l = l.tail();
        }
        return l.head();
    }

    @Override public T set (int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override public void add (int index, Object element) {
        throw new UnsupportedOperationException();
    }

    @Override public T remove (int index) {
        throw new UnsupportedOperationException();
    }

    @Override public int lastIndexOf (Object o) {
        // this is inefficient but more efficient than traversing in reverse order...
        int idx=0;
        int result = -1;
        for (T candidate: this) {
            if (equality.equals(candidate, o))
                result = idx;
            idx += 1;
        }
        return result;
    }

    public abstract ALinkedList<T> tail();

    @Override public <U> ALinkedList<U> map (Function<T, U> f) {
        return ACollectionSupport.map(newBuilder(), this, f);
    }
    @Override public <U> ALinkedList<U> flatMap(Function<T, Iterable<U>> f) {
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }

    @Override public ALinkedList<T> toLinkedList() {
        return this;
    }

    @Override public String toString () {
        return ACollectionSupport.toString(ALinkedList.class, this);
    }

    private static class HeadTail<T> extends ALinkedList<T> {
        private final T head;
        private ALinkedList<T> tail; // mutable only during construction - this allows for some optimizations

        HeadTail (T head, ALinkedList<T> tail, AEquality equality) {
            super(equality);
            this.head = head;
            this.tail = tail;
        }

        @Override public boolean isEmpty () {
            return false;
        }

        @Override public T head () {
            return head;
        }

        @Override public ALinkedList<T> tail () {
            return tail;
        }

        @Override public AIterator<T> iterator () {
            return new AbstractAIterator<T>() {
                private ALinkedList<T> next = HeadTail.this;

                @Override public boolean hasNext () {
                    return next.nonEmpty();
                }

                @Override public T next () {
                    if (next.isEmpty()) throw new NoSuchElementException();
                    final T result = next.head();
                    next = next.tail();
                    return result;
                }
            };
        }

        @Override public int size () {
            int result = 1;
            ALinkedList<T> l = this;
            while (l.tail().nonEmpty()) {
                l = l.tail();
                result += 1;
            }
            return result;
        }
    }

    private static class Nil<T> extends ALinkedList<T> {
        static final Nil EQUALS = new Nil(AEquality.EQUALS);

        Nil(AEquality equality) {
            super(equality);
        }

        @Override public boolean isEmpty () {
            return true;
        }

        @Override public T head () {
            throw new NoSuchElementException();
        }
        @Override public ALinkedList<T> tail () {
            throw new NoSuchElementException();
        }
        @Override public AIterator<T> iterator () {
            return AIterator.empty();
        }

        @Override public <U> ALinkedList<U> map (Function<T, U> f) {
            //noinspection unchecked
            return (ALinkedList<U>) this;
        }

        @Override public int size () {
            return 0;
        }

        @Override public boolean contains (Object o) {
            return false;
        }
    }

    public static <T> Builder<T> builder() {
        return builder(AEquality.EQUALS);
    }
    public static <T> Builder<T> builder(AEquality equality) {
        return new Builder<>(equality);
    }

    public static class Builder<T> implements ACollectionBuilder<T, ALinkedList<T>> {
        private ALinkedList<T> result;
        private HeadTail<T> last;
        private boolean wasBuilt=false;

        Builder (AEquality equality) {
            result = nil(equality);
        }

        @Override public AEquality equality () {
            return result.equality();
        }

        public Builder<T> add(T o) {
            if (wasBuilt) throw new IllegalStateException();
            if(result.isEmpty()) {
                last = new HeadTail<>(o, result, result.equality);
                result = last;
            }
            else {
                final HeadTail<T> newLast = new HeadTail<>(o, last.tail(), last.equality());
                last.tail = newLast;
                last = newLast;
            }
            return this;
        }
        public Builder<T> addAll(Iterator<? extends T> it) {
            while(it.hasNext()) add(it.next());
            return this;
        }
        public Builder<T> addAll(Iterable<? extends T> coll) {
            return addAll(coll.iterator());
        }


        public ALinkedList<T> build() {
            if (wasBuilt) throw new IllegalStateException();
            wasBuilt = true;
            return result;
        }
    }
}
