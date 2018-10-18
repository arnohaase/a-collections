package com.ajjpj.acollections;

import com.ajjpj.acollections.util.AOption;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public interface ASortedSet<T> extends ASet<T> {
    ASortedSet<T> added (T o);
    ASortedSet<T> removed (T o);

    ASortedSet<T> union (Iterable<T> that);
    ASortedSet<T> intersect (Set<T> that);
    ASortedSet<T> diff (Set<T> that);

    /**
     * Count all the nodes with keys greater than or equal to the lower bound and less than the upper bound.
     * The two bounds are optional.
     */
    int countInRange (AOption<T> from, AOption<T> to);

    ASortedSet<T> range (AOption<T> from, AOption<T> until);
    ASortedSet<T> drop (int n);
    ASortedSet<T> take (int n);
    ASortedSet<T> slice (int from, int until);

    AOption<T> smallest();
    AOption<T> greatest();

    AIterator<T> iterator(AOption<T> start);

    AIterator<ASortedSet<T>> subsets ();
}
