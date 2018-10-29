package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AIteratorTests;

import java.util.Arrays;


class ARangeIteratorTest implements AIteratorTests {
    @Override public AIterator<Integer> mkIterator (Integer... values) {
        final AVector<Integer> v = AVector.from(Arrays.asList(values));

        if (v.isEmpty()) return ARange.empty().iterator();
        if (v.equals(AVector.of(1))) return ARange.create(1, 2).iterator();
        if (v.equals(AVector.of(2))) return ARange.create(2, 3).iterator();
        if (v.equals(AVector.of(3))) return ARange.create(3, 4).iterator();
        if (v.equals(AVector.of(1, 2))) return ARange.create(1, 3).iterator();
        if (v.equals(AVector.of(2, 4))) return ARange.create(2, 5, 2).iterator();
        if (v.equals(AVector.of(1, 2, 3))) return ARange.create(1, 4).iterator();
        if (v.equals(AVector.of(3, 2, 1))) return ARange.create(3, 0).iterator();
        if (v.equals(AVector.of(1, 3, 5))) return ARange.create(1, 6, 2).iterator();
        if (v.equals(AVector.of(2, 4, 6))) return ARange.create(2, 7, 2).iterator();

        throw new IllegalArgumentException(v.toString());
    }
}
