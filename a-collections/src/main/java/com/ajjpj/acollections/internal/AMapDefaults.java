package com.ajjpj.acollections.internal;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AMap;
import com.ajjpj.acollections.immutable.AHashMap;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public interface AMapDefaults<K,V,C extends AMap<K,V>> extends AMap<K,V> {
    default <U> AMap<K,U> mapValues(Function<V,U> f) {
        return foldLeft(AHashMap.empty(), (acc, el) -> acc.updated(el.getKey(), f.apply(el.getValue())));
    }
}
