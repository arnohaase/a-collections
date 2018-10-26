package com.ajjpj.acollections.internal;

import com.ajjpj.acollections.AMap;
import com.ajjpj.acollections.immutable.AHashMap;

import java.util.function.Function;


public interface AMapDefaults<K,V,C extends AMap<K,V>> extends AMap<K,V> {
    default <U> AMap<K,U> mapValues(Function<V,U> f) { //TODO override to specialize return type
        return foldLeft(AHashMap.empty(), (acc, el) -> acc.plus(el.getKey(), f.apply(el.getValue())));
    }

    @Override default C withDefaultValue (V defaultValue) {
        //noinspection unchecked
        return (C) new AMapSupport.MapWithDefaultValue<>(this, defaultValue);
    }

    @Override default C withDerivedDefaultValue (Function<K, V> defaultProvider) {
        //noinspection unchecked
        return (C) new AMapSupport.MapWithDerivedDefaultValue<>(this, defaultProvider);
    }
}
