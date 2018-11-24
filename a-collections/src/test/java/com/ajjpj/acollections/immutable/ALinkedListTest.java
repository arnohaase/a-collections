package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AListTests;
import com.ajjpj.acollections.TestHelpers;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class ALinkedListTest implements AListTests {
    @Override @Test public void testSerDeser () {
        assertSame(ALinkedList.empty(), ALinkedList.empty());
        assertSame(ALinkedList.empty(), TestHelpers.serDeser(ALinkedList.empty()));

        assertEquals(ALinkedList.of(1), TestHelpers.serDeser(ALinkedList.of(1)));
        assertNotSame(ALinkedList.of(1), TestHelpers.serDeser(ALinkedList.of(1)));

        assertEquals(ALinkedList.of(1, 2, 3), TestHelpers.serDeser(ALinkedList.of(1, 2, 3)));
        assertNotSame(ALinkedList.of(1, 2, 3), TestHelpers.serDeser(ALinkedList.of(1, 2, 3)));
    }


    @Override public Iterable<Variant> variants () {
        return Collections.singletonList(
                new Variant(ALinkedList::builder, AVector.of(1, 2, 3))
        );
    }

    @Override @Test public void testStaticFactories() {
        assertTrue(ALinkedList.empty().isEmpty());
        assertTrue(ALinkedList.of().isEmpty());
        assertEquals("1", ALinkedList.of(1).mkString(","));
        assertEquals("1,2", ALinkedList.of(1,2).mkString(","));
        assertEquals("1,2,3", ALinkedList.of(1,2,3).mkString(","));
        assertEquals("1,2,3,4", ALinkedList.of(1,2,3,4).mkString(","));
        assertEquals("1,2,3,4,5", ALinkedList.of(1,2,3,4,5).mkString(","));
        assertEquals("1,2,3,4,5,6", ALinkedList.of(1,2,3,4,5,6).mkString(","));

        assertEquals(ALinkedList.of(1, 2, 3), ALinkedList.from(Arrays.asList(1, 2, 3)));
        assertEquals(ALinkedList.of(1, 2, 3), ALinkedList.from(new Integer[] {1, 2, 3}));
        assertEquals(ALinkedList.of(1, 2, 3), ALinkedList.fromIterator(Arrays.asList(1, 2, 3).iterator()));
    }

    @Test void testCollector() {
        assertEquals(AVector.of(1, 2, 3, 4), Stream.of(1, 2, 3, 4).collect(ALinkedList.streamCollector()));
        assertEquals(AVector.empty(), Stream.of().collect(ALinkedList.streamCollector()));
        assertEquals(ARange.create(0, 100000).toVector(), ARange.create(0, 100000).parallelStream().collect(ALinkedList.streamCollector()));
    }

}
