package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.ALinkedList;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.util.AEquality;
import com.ajjpj.acollections.util.AOption;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;


public interface ACollection<T> extends ACollectionOps<T>, Collection<T> {
    @Override ACollection<T> filter (Predicate<T> f);
    @Override ACollection<T> filterNot (Predicate<T> f);
}
