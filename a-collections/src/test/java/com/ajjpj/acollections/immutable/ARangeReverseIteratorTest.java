package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AIteratorTests;

import java.util.Arrays;


class ARangeReverseIteratorTest implements AIteratorTests {
    @Override public AIterator<Integer> mkIterator (Integer... values) {
        final AVector<Integer> v = AVector.from(Arrays.asList(values));

        if (v.isEmpty()) return ARange.empty().reverseIterator();
        if (v.equals(AVector.of(1))) return ARange.create(1, 0).reverseIterator();
        if (v.equals(AVector.of(2))) return ARange.create(2, 1).reverseIterator();
        if (v.equals(AVector.of(3))) return ARange.create(3, 2).reverseIterator();
        if (v.equals(AVector.of(1, 2))) return ARange.create(2, 0).reverseIterator();
        if (v.equals(AVector.of(2, 4))) return ARange.create(4, 0, -2).reverseIterator();
        if (v.equals(AVector.of(1, 2, 3))) return ARange.create(3, 0).reverseIterator();
        if (v.equals(AVector.of(3, 2, 1))) return ARange.create(1, 4).reverseIterator();
        if (v.equals(AVector.of(1, 3, 5))) return ARange.create(5, 0, -2).reverseIterator();
        if (v.equals(AVector.of(2, 4, 6))) return ARange.create(6, 1, -2).reverseIterator();

        throw new IllegalArgumentException(v.toString());
    }
}
