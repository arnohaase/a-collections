package com.ajjpj.acollections;

import java.util.Collection;
import java.util.function.Predicate;


public interface ACollection<T> extends ACollectionOps<T>, Collection<T> {
    @Override ACollection<T> filter (Predicate<T> f);
    @Override ACollection<T> filterNot (Predicate<T> f);
}
