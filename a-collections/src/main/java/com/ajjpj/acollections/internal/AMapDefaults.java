package com.ajjpj.acollections.internal;

import com.ajjpj.acollections.AMap;

import java.util.Map;


public interface AMapDefaults<K,V,C extends AMap<K,V>> extends ACollectionDefaults<Map.Entry<K,V>, C>, AMap<K,V> {
}
