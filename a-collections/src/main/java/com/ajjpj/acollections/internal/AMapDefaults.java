package com.ajjpj.acollections.internal;

import com.ajjpj.acollections.AMap;
import com.ajjpj.acollections.immutable.AHashMap;

import java.util.function.Function;
import java.util.function.Predicate;


public interface AMapDefaults<K,V,C extends AMap<K,V>> extends AMap<K,V> {
    @Override default C plus (Entry<K, V> entry) {
        //noinspection unchecked
        return (C) plus(entry.getKey(), entry.getValue());
    }

    default <V1> AMap<K, V1> mapValues(Function<V, V1> f) {
        final AMap<K,V1> zero = (AMap<K, V1>) this.<K,V1>newEntryBuilder().build();
        return foldLeft(zero, (acc, el) -> acc.plus(el.getKey(), f.apply(el.getValue())));
    }
    default C filterKeys(Predicate<K> f) {
        final AMap<K,V> zero = (AMap<K, V>) this.<K,V>newEntryBuilder().build();
        //noinspection unchecked
        return foldLeft((C) zero, (acc, el) -> f.test(el.getKey()) ? (C) acc.plus(el.getKey(), el.getValue()) : acc);
    }

//    @Override default AMap<K,V> withDefaultValue (V defaultValue) {
//        return AMapSupport.wrapMapWithDefaultValue(this, k -> defaultValue);
//    }
//
//    @Override default AMap<K,V> withDerivedDefaultValue (Function<K, V> defaultProvider) {
//        //noinspection unchecked
//        return new AMapSupport.MapWithDerivedDefaultValue<>(this, defaultProvider);
//    }
}
