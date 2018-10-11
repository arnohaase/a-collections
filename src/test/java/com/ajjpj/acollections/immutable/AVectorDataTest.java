package com.ajjpj.acollections.immutable;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SuppressWarnings("WeakerAccess")
public class AVectorDataTest {
    @Test public void testAppendSimple() {
        AVector<Integer> v = AVector.empty();

        final int numElements = 10_000_000;

        for (int i=0; i<numElements; i++) {
            v = v.append(i);
        }
        assertEquals(numElements, v.size());

        for (int i=0; i<numElements; i++) {
            assertEquals(i, v.get(i).intValue());
        }
    }

    @Test public void testPrependSimple() {
        AVector<Integer> v = AVector.empty();

        final int numElements = 10_000_000;

        for (int i=0; i<numElements; i++) {
            v = v.prepend(i);
        }
        assertEquals(numElements, v.size());

        for (int i=0; i<numElements; i++) {
            assertEquals(numElements-i-1, v.get(i).intValue());
        }

        v = v.prepend(9999);
        assertEquals(9999, v.get(0).intValue());
    }


    @Test public void testBuilderAndRandomAccess() {
        final int numElements = 10_000_000;

        AVector.Builder<Integer> builder = AVector.builder();
        for (int i=0; i<numElements; i++) {
            builder.add(i);
        }
        final AVector<Integer> v = builder.build();
        int result = 0;
        int expected = 0;
        for (int i=0; i<numElements; i++) {
            expected += i;
            result += v.get(i);
        }

        assertEquals(expected, result);
    }
}
