package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AList;
import com.ajjpj.acollections.AbstractAIterator;
import com.ajjpj.acollections.util.AEquality;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


public abstract class ALinkedList<T> extends AbstractImmutableCollection<T> implements AList<T> {
    private final AEquality equality;

    private ALinkedList (AEquality equality) {
        this.equality = equality;
    }

    public static <T> ALinkedList<T> fromIterator(Iterator<T> it) {
        return fromIterator(it, AEquality.EQUALS);
    }
    public static <T> ALinkedList<T> fromIterator(Iterator<T> it, AEquality equality) {
        return builderFromIterator(it, equality).build();
    }
    private static <T> ListBuilder<T> builderFromIterator(Iterator<T> it, AEquality equality) {
        final ListBuilder<T> builder = new ListBuilder<>(equality);
        while (it.hasNext()) {
            builder.append(it.next());
        }
        return builder;
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

    @Override public ALinkedList<T> prepend(T o) {
        return new HeadTail<>(o, this, equality);
    }

    @Override public ALinkedList<T> append(T o) {
        final ListBuilder<T> builder = builderFromIterator(iterator(), equality);
        builder.append(o);
        return builder.build();
    }

    @Override public ALinkedList<T> prependAll (AList<T> other) {
        return other.reverse().fold(this, ALinkedList<T>::prepend);
    }

    @Override public ALinkedList<T> updated (int idx, T o) {
        final ListBuilder<T> builder = new ListBuilder<>(equality);

        ALinkedList<T> l = this;
        for(int i=0; i<idx; i++) {
            builder.append(l.head());
            l = l.tail();
        }

        builder.append(o);
        l = l.tail();

        for (T el: l)
            builder.append(el);
        return builder.build();
    }

    @Override public ALinkedList<T> patch (int idx, List<T> patch, int numReplaced) {
        final ListBuilder<T> builder = new ListBuilder<>(equality);

        ALinkedList<T> l = this;
        for(int i=0; i<idx; i++) {
            builder.append(l.head());
            l = l.tail();
        }

        for (T el: patch)
            builder.append(el);
        for (int i=0; i<numReplaced; i++)
            l = l.tail();

        for (T el: l)
            builder.append(el);
        return builder.build();
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
        return dropRight(1);
    }

    @Override public ListIterator<T> listIterator () {
        throw new UnsupportedOperationException("ListIterator does not make sense for a linked list");
    }

    @Override  public ListIterator<T> listIterator (int index) {
        throw new UnsupportedOperationException("ListIterator does not make sense for a linked list");
    }

    @Override public ALinkedList<T> take (int n) {
        if (n < 0) throw new IllegalArgumentException();
        final ListBuilder<T> builder = new ListBuilder<>(equality);
        ALinkedList<T> l = this;
        for(int i=0; i<n; i++) {
            builder.append(l.head());
            l = l.tail();
        }
        return builder.build();
    }

    @Override public AList<T> takeRight (int n) {
        if (n < 0) throw new IllegalArgumentException();
        return drop(size() - n);
    }

    @Override public ALinkedList<T> takeWhile (Predicate<T> f) {
        final ListBuilder<T> builder = new ListBuilder<>(equality);
        ALinkedList<T> l = this;
        while(l.nonEmpty() && f.test(l.head())) {
            builder.append(l.head());
            l = l.tail();
        }
        return builder.build();
    }

    @Override public ALinkedList<T> drop (int n) {
        if (n < 0) throw new IllegalArgumentException();
        ALinkedList<T> l = this;
        for (int i=0; i<n; i++)
            l = l.tail();
        return l;
    }

    @Override public ALinkedList<T> dropRight (int n) {
        if (n < 0) throw new IllegalArgumentException();
        return take(size() - n);
    }

    @Override public ALinkedList<T> dropWhile (Predicate<T> f) {
        ALinkedList<T> l = this;
        while (l.nonEmpty() && f.test(l.head()))
            l = l.tail();
        return l;
    }

    @Override public ACollection<T> filter (Predicate<T> f) {
        final ListBuilder<T> builder = new ListBuilder<>(equality);
        for (T o: this) {
            if (f.test(o))
                builder.append(o);
        }
        return builder.build();
    }

    @Override public boolean endsWith (AList<T> that) {
        if (that.size() > this.size()) return false;

        final Iterator<T> itThis = this.iterator().drop(size() - that.size());
        final Iterator<T> itThat = that.iterator();
        while (itThis.hasNext()) {
            if (!equality.equals(itThis.next(), itThat.next()))
                return false;
        }
        return true;
    }

    @Override public <U> ACollection<U> collect (Predicate<T> filter, Function<T, U> f) {
        return fromIterator(iterator().collect(filter, f));
    }

    @Override public ALinkedList<T> reverse () {
        ALinkedList<T> result = nil(equality);
        for (T o: this) {
            result = result.prepend(o);
        }
        return result;
    }

    @Override public AIterator<T> reverseIterator () {
        return reverse().iterator();
    }

    @Override public ALinkedList<T> subList (int fromIndex, int toIndex) {
        return drop(fromIndex).take(toIndex - fromIndex);
    }

    @Override public boolean addAll (int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T get (int index) {
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

    @Override public int indexOf (Object o) {
        int idx=0;
        ALinkedList<T> l = this;
        while(l.nonEmpty()) {
            if (equality.equals(head(), o)) return idx;
            l = l.tail();
        }

        return -1;
    }

    @Override public int lastIndexOf (Object o) {
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

    @Override public abstract <U> ALinkedList<U> map (Function<T, U> f);

    @Override public ALinkedList<T> toLinkedList() {
        return this;
    }


    private static class HeadTail<T> extends ALinkedList<T> {
        private final int size;
        private final T head;
        private ALinkedList<T> tail; // mutable only during construction - this allows for some optimizations

        HeadTail (T head, ALinkedList<T> tail, AEquality equality) {
            super(equality);
            size = tail.size() + 1;
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
                private ALinkedList<T> cur = null;
                private ALinkedList<T> next = HeadTail.this;

                @Override public boolean hasNext () {
                    return next != null;
                }

                @Override public T doNext () {
                    if (next == null) throw new NoSuchElementException();
                    cur = next;
                    next = next.isEmpty() ? null : next.tail();

                    return cur.head();
                }
            };
        }

        @Override public <U> ALinkedList<U> map (Function<T, U> f) {
            final ListBuilder<U> builder = new ListBuilder<>(equality());
            for (T o: this) {
                builder.append(f.apply(o));
            }
            return builder.build();
        }

        @Override public int size () {
            return this.size;
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

        @Override public T head () { throw new NoSuchElementException(); }
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

    private static class ListBuilder<T> {
        private ALinkedList<T> result;
        private HeadTail<T> last;
        private boolean wasBuilt=false;

        ListBuilder (AEquality equality) {
            result = nil(equality);
        }

        void append(T o) {
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
        }

        ALinkedList<T> build() {
            if (wasBuilt) throw new IllegalStateException();
            wasBuilt = true;
            return result;
        }
    }
}
