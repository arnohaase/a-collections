package com.ajjpj.acollections.util;

import java.util.Objects;


public interface AEquality {
    boolean equals(Object o1, Object o2);
    default boolean notEquals(Object o1, Object o2){
        return !equals(o1, o2);
    }
    int hashCode(Object o);

    AEquality EQUALS = new AEquality() {
        @Override public boolean equals (Object o1, Object o2) {
            return Objects.equals(o1, o2);
        }

        @Override public int hashCode (Object o) {
            return o != null ? o.hashCode() : 0;
        }
    };

    AEquality IDENTITY = new AEquality() {
        @Override public boolean equals (Object o1, Object o2) {
            return o1 == o2;
        }

        @Override public int hashCode (Object o) {
            return o != null ? System.identityHashCode(o) : 0;
        }
    };
}
