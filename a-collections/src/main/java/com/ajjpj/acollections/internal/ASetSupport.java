package com.ajjpj.acollections.internal;


import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.ASet;
import com.ajjpj.acollections.AbstractAIterator;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Supplier;

public class ASetSupport {
    public static boolean equals(ASet<?> s, Object o) {
        if (o == s) return true;
        if (! (o instanceof Set)) return false;

        //noinspection unchecked
        final Set<?> that = (Set<?>) o;
        return s.size() == that.size() && s.forall(that::contains);
    }

    public static int hashCode(ASet<?> s) {
        int h = 0;
        for (Object obj : s) {
            if (obj != null)
                h += obj.hashCode();
        }
        return h;
    }


    public static <T, C extends ASet<T>> AIterator<C> subsets(int len, Collection<T> coll, Supplier<ACollectionBuilder<T, C>> builderFactory) {
        //noinspection unchecked
        return new SubsetsItr<>((T[])coll.toArray(), len, builderFactory);
    }

    /** An iterator over all subsets of this set.
     */
    public static <T, C extends ASet<T>> AIterator<C> subsets(Collection<T> coll, Supplier<ACollectionBuilder<T, C>> builderFactory) {
        return new AbstractAIterator<C>() {
            @SuppressWarnings("unchecked")
            private final T[] elms = (T[]) coll.toArray();
            private int  len = 0;
            private AIterator<C> itr = AIterator.empty();

            @Override public boolean hasNext () {
                return len <= elms.length || itr.hasNext();
            }

            @Override public C next () {
                if (!itr.hasNext()) {
                    if (len > elms.length)
                        throw new NoSuchElementException();
                    else {
                        itr = new SubsetsItr<>(elms, len, builderFactory);
                        len += 1;
                    }
                }

                return itr.next();
            }
        };
    }

    /** An Iterator including all subsets containing exactly len elements.
     *  If the elements in 'This' type is ordered, then the subsets will also be in the same order.
     */
    private static class SubsetsItr<T, C extends ASet<T>> extends AbstractAIterator<C> {
        private final T[] elms;
        private final int len;
        private final Supplier<ACollectionBuilder<T, C>> builderFactory;

        private final int[] idxs;
        private boolean _hasNext;


        SubsetsItr (T[] elms, int len, Supplier<ACollectionBuilder<T, C>> builderFactory) {
            this.elms = elms;
            this.len = len;
            this.builderFactory = builderFactory;

            this.idxs = new int[len+1];
            for (int i=0; i<len; i++) idxs[i] = i;
            idxs[len] = elms.length;

            if (len < 0) throw new IllegalArgumentException();
            _hasNext = len <= elms.length;
        }

        @Override public boolean hasNext () {
            return _hasNext;
        }

        @Override public C next () {
            if (!hasNext()) throw new NoSuchElementException();

            final ACollectionBuilder<T, C> builder = builderFactory.get();
            for (int i=0; i<len; i++) builder.add(elms[idxs[i]]);
            final C result = builder.build();

            int i = len - 1;
            while (i >= 0 && idxs[i] == idxs[i+1]-1) i -= 1;

            if (i < 0)
                _hasNext = false;
            else {
                idxs[i] += 1;
                for (int j = i+1; j < len; j++)
                    idxs[j] = idxs[j-1] + 1;
            }

            return result;
        }
    }
}
