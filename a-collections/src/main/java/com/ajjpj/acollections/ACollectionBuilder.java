package com.ajjpj.acollections;

import java.util.Iterator;


/**
 * This is a generic interface for collection builders, i.e. for objects building collections from their elements. Especially for
 *  'persistent' collections, this can be way more efficient than using the collection's API.
 *
 * @param <T> the element type that can be added
 * @param <R> the built result type
 */
public interface ACollectionBuilder<T, R extends ACollectionOps<T>> {
    ACollectionBuilder<T,R> add(T el);
    R build();

    default ACollectionBuilder<T,R> addAll(Iterator<? extends T> it) {
        while(it.hasNext()) add(it.next());
        return this;
    }
    default ACollectionBuilder<T,R> addAll(Iterable<? extends T> coll) {
        return addAll(coll.iterator());
    }
}
