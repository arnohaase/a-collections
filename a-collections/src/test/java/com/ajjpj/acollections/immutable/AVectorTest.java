package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AList;
import com.ajjpj.acollections.AListTests;
import com.ajjpj.acollections.TestHelpers;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class AVectorTest implements AListTests {

    @Override public Iterable<Variant> variants () {
        return Collections.singletonList(
                new Variant(AVector.class, AVector::builder, AVector.of(1, 2, 3))
        );
    }

    @Override @Test public void testStaticFactories() {
        assertTrue(AVector.empty().isEmpty());
        assertTrue(AVector.of().isEmpty());
        assertEquals("1", AVector.of(1).mkString(","));
        assertEquals("1,2", AVector.of(1,2).mkString(","));
        assertEquals("1,2,3", AVector.of(1,2,3).mkString(","));
        assertEquals("1,2,3,4", AVector.of(1,2,3,4).mkString(","));
        assertEquals("1,2,3,4,5", AVector.of(1,2,3,4,5).mkString(","));
        assertEquals("1,2,3,4,5,6", AVector.of(1,2,3,4,5,6).mkString(","));

        assertEquals(AVector.of(1, 2, 3), AVector.from(Arrays.asList(1, 2, 3)));
        assertEquals(AVector.of(1, 2, 3), AVector.from(new Integer[] {1, 2, 3}));
        assertEquals(AVector.of(1, 2, 3), AVector.fromIterator(Arrays.asList(1, 2, 3).iterator()));
    }

    @Test void testStaticFactoriesInAList() {
        assertTrue(AList.empty().isEmpty());
        assertTrue(AList.of().isEmpty());
        assertEquals("1", AList.of(1).mkString(","));
        assertEquals("1,2", AList.of(1,2).mkString(","));
        assertEquals("1,2,3", AList.of(1,2,3).mkString(","));
        assertEquals("1,2,3,4", AList.of(1,2,3,4).mkString(","));
        assertEquals("1,2,3,4,5", AList.of(1,2,3,4,5).mkString(","));
        assertEquals("1,2,3,4,5,6", AList.of(1,2,3,4,5,6).mkString(","));

        assertEquals(AVector.of(1, 2, 3), AList.from(Arrays.asList(1, 2, 3)));
        assertEquals(AVector.of(1, 2, 3), AList.from(new Integer[] {1, 2, 3}));
        assertEquals(AVector.of(1, 2, 3), AList.fromIterator(Arrays.asList(1, 2, 3).iterator()));
    }

    @Override @Test public void testSerDeser () {
        doTest(v -> {
            assertSame(AVector.empty(), TestHelpers.serDeser(v.mkList()));

            assertNotSame(AVector.of(1), TestHelpers.serDeser(v.mkList(1)));
            assertEquals(AVector.of(1), TestHelpers.serDeser(v.mkList(1)));

            assertNotSame(AVector.of(1, 2, 3), TestHelpers.serDeser(v.mkList(1, 2, 3)));
            assertEquals(AVector.of(1, 2, 3), TestHelpers.serDeser(v.mkList(1, 2, 3)));
        });
    }

    @Test void testAppendSimple() {
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

    @Test void testPrependSimple() {
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


    @Test void testBuilderAndRandomAccess() {
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

    @Override @Test public void testSubList() {
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

    @Test void testCollector() {
        assertEquals(AVector.of(1, 2, 3, 4), Stream.of(1, 2, 3, 4).collect(AVector.streamCollector()));
        assertEquals(AVector.empty(), Stream.of().collect(AVector.streamCollector()));
        assertEquals(ARange.create(0, 100000).toVector(), ARange.create(0, 100000).parallelStream().collect(AVector.streamCollector()));
    }
}
