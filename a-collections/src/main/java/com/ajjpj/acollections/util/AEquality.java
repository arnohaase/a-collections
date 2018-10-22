package com.ajjpj.acollections.util;

import java.util.Comparator;
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

    static AEquality fromComparator(Comparator comparator) {
        return new ComparatorBasedEquality(comparator);
    }

    class ComparatorBasedEquality implements AEquality {
        private final Comparator comparator;

        public ComparatorBasedEquality (Comparator comparator) {
            this.comparator = comparator;
        }

        @Override public boolean equals (Object o1, Object o2) {
            //noinspection unchecked
            return comparator.compare(o1, o2) == 0;
        }

        @Override public int hashCode (Object o) {
            return Objects.hashCode(o);
        }

        @Override
        public boolean equals (Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            ComparatorBasedEquality that = (ComparatorBasedEquality) o;
            return Objects.equals(comparator, that.comparator);
        }

        @Override public int hashCode () {
            return Objects.hash(comparator);
        }
    }
}
