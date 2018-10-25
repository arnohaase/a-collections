package com.ajjpj.acollections;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;


public interface ACollection<T> extends ACollectionOps<T>, Collection<T> {
    @Override ACollection<T> filter (Predicate<T> f);
    @Override ACollection<T> filterNot (Predicate<T> f);

    @Override <U> ACollectionBuilder<U, ? extends ACollection<U>> newBuilder ();
    <K> AMap<K,? extends ACollection<T>> groupBy(Function<T,K> keyExtractor);
}
