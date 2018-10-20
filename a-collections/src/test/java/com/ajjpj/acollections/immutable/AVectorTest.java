package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionTests;
import com.ajjpj.acollections.AList;
import com.ajjpj.acollections.util.AEquality;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


@SuppressWarnings("WeakerAccess")
public class AVectorTest implements ACollectionTests {
    @Override public Iterable<Variant> variants () {
        return Arrays.asList(
                new Variant (() -> AVector.builder(AEquality.EQUALS), AVector.of(1, 2, 3), false),
                new Variant (() -> AVector.builder(AEquality.IDENTITY), AVector.of(1, 2, 3), true)
        );
    }

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

    @Test public void testSubList(){
        AVector.Builder<Integer> builder = AVector.builder();
        for (int i=0; i<10; i++) {
            builder.add(i);
        }
        AList<Integer> list = builder.build();

        assertEquals(0,list.subList(0, 0).size());
        assertEquals(10, list.subList(0, 10).size());
        assertEquals(4, list.subList(5, 9).size());

        Iterator<Integer> subList = list.subList(3, 6).iterator();
        assertEquals(3,subList.next().intValue());
        assertEquals(4,subList.next().intValue());
        assertEquals(5,subList.next().intValue());
        assertFalse(subList.hasNext());
    }


}
