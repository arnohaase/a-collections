package com.ajjpj.acollections.internal;


import com.ajjpj.acollections.ASet;

import java.util.Set;

public class ASetSupport {
    public static <T> boolean equals(ASet<T>s, Object o) {
        if (o == s) return true;
        if (! (o instanceof Set)) return false;

        //noinspection unchecked
        final Set<T> that = (Set<T>) o;
        return s.size() == that.size() && s.forall(that::contains);
    }
}
