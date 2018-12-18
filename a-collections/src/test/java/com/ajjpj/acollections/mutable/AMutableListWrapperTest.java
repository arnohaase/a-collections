package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AListTests;
import com.ajjpj.acollections.TestHelpers;
import com.ajjpj.acollections.immutable.ARange;
import com.ajjpj.acollections.immutable.AVector;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class AMutableListWrapperTest implements AListTests {
    @Override public boolean isImmutable () {
        return false;
    }

    @Override public Iterable<Variant> variants () {
        //noinspection ArraysAsListWithZeroOrOneArgument
        return Arrays.asList(
                new Variant(AMutableListWrapper.class, AMutableListWrapper::builder, AVector.of(1, 2, 3))
        );
    }

    @Override @Test public void testStaticFactories() {
        assertTrue(AMutableListWrapper.empty().isEmpty());
        assertTrue(AMutableListWrapper.of().isEmpty());
        assertEquals("1", AMutableListWrapper.of(1).mkString(","));
        assertEquals("1,2", AMutableListWrapper.of(1,2).mkString(","));
        assertEquals("1,2,3", AMutableListWrapper.of(1,2,3).mkString(","));
        assertEquals("1,2,3,4", AMutableListWrapper.of(1,2,3,4).mkString(","));
        assertEquals("1,2,3,4,5", AMutableListWrapper.of(1,2,3,4,5).mkString(","));
        assertEquals("1,2,3,4,5,6", AMutableListWrapper.of(1,2,3,4,5,6).mkString(","));

        assertEquals(AMutableListWrapper.of(1, 2, 3), AMutableListWrapper.from(Arrays.asList(1, 2, 3)));
        assertEquals(AMutableListWrapper.of(1, 2, 3), AMutableListWrapper.from(new Integer[] {1, 2, 3}));
        assertEquals(AMutableListWrapper.of(1, 2, 3), AMutableListWrapper.fromIterator(Arrays.asList(1, 2, 3).iterator()));
    }

    @Override @Test public void testSerDeser () {
        doTest(v -> {
            final AMutableListWrapper<Integer> orig = (AMutableListWrapper<Integer>) v.mkList(1);
            final AMutableListWrapper<Integer> serDeser = TestHelpers.serDeser(orig);
            assertNotSame(serDeser, orig);
            assertNotSame(serDeser.getInner(), orig.getInner());
            assertEquals(serDeser, orig);
        });
    }

    @Test void testCollector() {
        assertEquals(AVector.of(1, 2, 3, 4), Stream.of(1, 2, 3, 4).collect(AMutableListWrapper.streamCollector()));
        assertEquals(AVector.empty(), Stream.of().collect(AMutableListWrapper.streamCollector()));
        assertEquals(ARange.create(0, 100000).toVector(), ARange.create(0, 100000).parallelStream().collect(AMutableListWrapper.streamCollector()));
    }
}
