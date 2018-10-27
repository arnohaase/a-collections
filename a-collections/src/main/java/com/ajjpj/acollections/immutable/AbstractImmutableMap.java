package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.ACollectionOps;
import com.ajjpj.acollections.AMap;
import com.ajjpj.acollections.internal.AMapSupport;

import java.util.Collection;
import java.util.Map;


abstract class AbstractImmutableMap<K,V> implements AMap<K,V> {
    @Override public ATreeSet<Entry<K, V>> toSortedSet () {
        throw new UnsupportedOperationException("pass in a Comparator<Map.Entry> - Map.Entry has no natural order");
    }
    @Override public <U> ACollectionBuilder<U, ? extends ACollectionOps<U>> newBuilder () {
        throw new UnsupportedOperationException("Implementing this well goes beyond the boundaries of Java's type system. Use static ATreeMap.builder() instead.");
    }
    @Override public Entry<K, V> min () {
        throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
    }
    @Override public Entry<K, V> max () {
        throw new UnsupportedOperationException("pass in a Comparator explicitly - Map.Entry has no natural order");
    }

    @Override public V put (K key, V value) {
        throw new UnsupportedOperationException("use 'plus' for persistent collection");
    }

    @Override public V remove (Object key) {
        throw new UnsupportedOperationException("use 'minus' for persistent collection");
    }

    @Override public void putAll (Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException("use 'plusAll' for persistent collection");
    }

    @Override public void clear () {
        throw new UnsupportedOperationException("unsupported for persistent collection");
    }


    //TODO javadoc: equals / hashCode compatible to j.u.*
    @Override public boolean equals (Object o) { //TODO test this
        if (o instanceof Map) {
            final Map<?,?> that = (Map<?, ?>) o;
            return this.entrySet().equals(that.entrySet());
        }

        return false;
    }

    @Override public int hashCode () { //TODO test this
        int h = 0;
        for (Entry<K, V> kvEntry : entrySet()) h += kvEntry.hashCode();
        return h;
    }

    @Override public String toString () {
        final StringBuilder sb = new StringBuilder("{");
        boolean isFirst = true;
        for (Map.Entry<?,?> e: this) {
            if (isFirst) isFirst = false;
            else sb.append(",");

            sb.append(e.getKey()).append("=").append(e.getValue());
        }

        sb.append("}");
        return sb.toString();
    }

}
