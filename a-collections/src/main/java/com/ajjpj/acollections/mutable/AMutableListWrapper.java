package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AList;
import com.ajjpj.acollections.AbstractAIterator;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AListDefaults;
import com.ajjpj.acollections.internal.AListIteratorWrapper;
import com.ajjpj.acollections.util.AOption;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;


/**
 * Always AEquality.EQUALS --> least surprise
 *
 */
public class AMutableListWrapper<T> implements AListDefaults<T, AMutableListWrapper<T>>, Serializable {
    private List<T> inner;

    public static <T> AMutableListWrapper<T> wrap(List<T> inner) {
        return new AMutableListWrapper<>(inner);
    }

    private AMutableListWrapper (List<T> inner) {
        this.inner = inner;
    }

    public List<T> inner() {
        return inner;
    }

    public static <T> AMutableListWrapper<T> empty() {
        return new AMutableListWrapper<>(new ArrayList<>());
    }

    public static <T> AMutableListWrapper<T> fromIterator (Iterator<T> it) {
        return AMutableListWrapper.<T>builder().addAll(it).build();
    }
    public static <T> AMutableListWrapper<T> from (Iterable<T> iterable) {
        return AMutableListWrapper.<T>builder().addAll(iterable).build();
    }

    public static <T> AMutableListWrapper<T> of(T o1) {
        return AMutableListWrapper.<T>builder().add(o1).build();
    }
    public static <T> AMutableListWrapper<T> of(T o1, T o2) {
        return AMutableListWrapper.<T>builder().add(o1).add(o2).build();
    }
    public static <T> AMutableListWrapper<T> of(T o1, T o2, T o3) {
        return AMutableListWrapper.<T>builder().add(o1).add(o2).add(o3).build();
    }

    //TODO static factories

    @Override public <U> ACollectionBuilder<U, AMutableListWrapper<U>> newBuilder () {
        return builder();
    }

    @Override public AMutableListWrapper<T> toMutableList () {
        return this;
    }

    public List<T> getInner() {
        return inner;
    }

    @Override
    public AOption<T> lastOption () {
        if (isEmpty()){
            return AOption.none();
        }
        return AOption.of(inner.get(inner.size()-1));
    }

    @Override
    public boolean contains (Object o) {
        return inner.contains(o);
    }

    @Override public <U> AMutableListWrapper<U> flatMap (Function<T, Iterable<U>> f) {
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }

    @Override public Object[] toArray () {
        return ACollectionSupport.toArray(this);
    }
    @Override public <T1> T1[] toArray (T1[] a) {
        return ACollectionSupport.toArray(this, a);
    }

    @Override public AMutableListWrapper<T> prepend (T o) {
        inner.add(0, o);
        return this;
    }
    @Override public AMutableListWrapper<T> append (T o) {
        inner.add(o);
        return this;
    }

    @Override public AMutableListWrapper<T> updated (int idx, T o) {
        inner.set(idx, o);
        return this;
    }

    @Override public AMutableListWrapper<T> concat(Iterator<? extends T> that) {
        while (that.hasNext()) {
            inner.add(that.next());
        }
        return this;
    }
    @Override public AMutableListWrapper<T> concat(Iterable<? extends T> that) {
        return concat(that.iterator());
    }

    @Override public AMutableListWrapper<T> patch (int idx, List<T> patch, int numReplaced) {
        for (int i = 0; i<numReplaced; i++)
            inner.remove(idx);
        inner.addAll(idx, patch);
        return this;
    }

    @Override public AMutableListWrapper<T> takeRight (int n) {
        if (n <= 0)
            clear();
        else if (n < size()) {
            inner = AMutableListWrapper.<T>builder()
                    .addAll(inner.listIterator(size()-n))
                    .build()
                    .inner();
        }
        return this;
    }
    @Override public AMutableListWrapper<T> dropRight (int n) {
        for (int i=0; i<n; i++) {
            if (inner.size() > 0) inner.remove(size()-1);
        }
        return this;
    }

    @Override public AIterator<T> reverseIterator () {
        return new AbstractAIterator<T>() {
            final ListIterator<T> it = inner.listIterator(size());

            @Override public boolean hasNext () {
                return it.hasPrevious();
            }

            @Override public T next () {
                return it.previous();
            }
        };
    }

    @Override public AMutableListWrapper<T> sorted (Comparator<? super T> comparator) {
        inner.sort(comparator);
        return this;
    }

    @Override public AMutableListWrapper<T> shuffled (Random r) {
        Collections.shuffle(inner, r);
        return this;
    }

    @Override public <U> AList<U> collect (Predicate<T> filter, Function<T, U> f) {
        return ACollectionSupport.collect(newBuilder(), this, filter, f);
    }

    @Override public AMutableListWrapper<T> take (int n) {
        return dropRight(size() - n);
    }
    @Override public AMutableListWrapper<T> drop (int n) {
        for (int i=0; i<n; i++) {
            if (inner.size() > 0) inner.remove(0);
        }
        return this;
    }

    @Override public AIterator<T> iterator () {
        return new AListIteratorWrapper<>(inner.listIterator());
    }

    @Override public <U> AMutableListWrapper<U> map (Function<T, U> f) {
        return ACollectionSupport.map(newBuilder(), this, f);
    }


    @Override public boolean addAll (int index, Collection<? extends T> c) {
        return inner.addAll(index, c);
    }

    @Override public T get (int index) {
        return inner.get(index);
    }

    @Override public T set (int index, T element) {
        return inner.set(index, element);
    }

    @Override public void add (int index, T element) {
        inner.add(index, element);
    }

    @Override public T remove (int index) {
        return inner.remove(index);
    }

    @Override public int indexOf (Object o) {
        return inner.indexOf(o);
    }

    @Override public int lastIndexOf (Object o) {
        return inner.lastIndexOf(o);
    }

    @Override public AListIteratorWrapper<T> listIterator () {
        return new AListIteratorWrapper<>(inner.listIterator());
    }

    @Override public AListIteratorWrapper<T> listIterator (int index) {
        return new AListIteratorWrapper<>(inner.listIterator());
    }

    @Override public List<T> subList (int fromIndex, int toIndex) {
        return new AMutableListWrapper<>(inner.subList(fromIndex,toIndex ));
    }

    @Override public int size () {
        return inner.size();
    }

    @Override public boolean isEmpty () {
        return inner.isEmpty();
    }

    @Override public boolean add (T t) {
        return inner.add(t);
    }

    @Override public boolean remove (Object o) {
        return inner.remove(o);
    }

    @Override public boolean addAll (Collection<? extends T> c) {
        return inner.addAll(c);
    }

    @Override public boolean removeAll (Collection<?> c) {
        return inner.removeAll(c);
    }

    @Override public boolean retainAll (Collection<?> c) {
        return inner.retainAll(c);
    }

    @Override public void clear () {
        inner.clear();
    }

    @Override public void replaceAll (UnaryOperator<T> operator) {
        inner.replaceAll(operator);
    }
    @Override public void sort (Comparator<? super T> c) {
        inner.sort(c);
    }
    @Override public Spliterator<T> spliterator () {
        return inner.spliterator();
    }
    @Override public boolean removeIf (Predicate<? super T> filter) {
        return inner.removeIf(filter);
    }
    @Override public Stream<T> stream () {
        return inner.stream();
    }
    @Override public Stream<T> parallelStream () {
        return inner.parallelStream();
    }
    @Override public void forEach (Consumer<? super T> action) {
        inner.forEach(action);
    }

    @Override public String toString () {
        return getClass().getSimpleName() + ":" + inner;
    }

    @Override public boolean equals(Object obj) {
        return inner.equals(obj);
    }

    @Override public int hashCode() {
        return inner.hashCode();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> implements ACollectionBuilder<T, AMutableListWrapper<T>> {
        private final List<T> inner = new ArrayList<>();

        @Override public ACollectionBuilder<T, AMutableListWrapper<T>> add (T el) {
            inner.add(el);
            return this;
        }

        @Override public AMutableListWrapper<T> build () {
            return AMutableListWrapper.wrap(inner);
        }
    }
}
