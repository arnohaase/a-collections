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
    /**
     * adds an element
     * @param el the added element
     * @return this builder with the added element
     */
    ACollectionBuilder<T,R> add(T el);

    /**
     * builds the resulting collection from this builder
     * @return the collection
     */
    R build();

    /**
     * adds all of an Iterator's elements
     * @param it the iterator being added
     * @return this builder with the added elements
     */
    default ACollectionBuilder<T,R> addAll(Iterator<? extends T> it) {
        while(it.hasNext()) add(it.next());
        return this;
    }
    /**
     * adds all of an Iterable's elements
     * @param coll the Iterable being added
     * @return this builder with the added elements
     */
    default ACollectionBuilder<T,R> addAll(Iterable<? extends T> coll) {
        return addAll(coll.iterator());
    }
    /**
     * adds all of an array's elements
     * @param coll the array being added
     * @return this builder with the added elements
     */
    default ACollectionBuilder<T,R> addAll(T[] coll) {
        for (T o: coll) add(o);
        return this;
    }
}
