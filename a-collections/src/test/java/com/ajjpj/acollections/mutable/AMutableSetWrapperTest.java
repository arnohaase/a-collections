package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ASet;
import com.ajjpj.acollections.ASetTests;
import com.ajjpj.acollections.TestHelpers;
import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.ARange;
import com.ajjpj.acollections.immutable.AVector;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class AMutableSetWrapperTest implements ASetTests {
    @Override public boolean isImmutable () {
        return false;
    }

    @Override public Iterable<Variant> variants () {
        return Collections.singletonList(
                new Variant(AMutableSetWrapper.class, AMutableSetWrapper::builder, null)
        );
    }

    @Override @Test public void testStaticFactories() {
        assertTrue(AMutableSetWrapper.empty().isEmpty());
        assertTrue(AMutableSetWrapper.of().isEmpty());
        assertEquals(new HashSet<>(Collections.singletonList(1)), AMutableSetWrapper.of(1));
        assertEquals(new HashSet<>(Arrays.asList(1, 2)), AMutableSetWrapper.of(1,2));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3)), AMutableSetWrapper.of(1,2,3));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4)), AMutableSetWrapper.of(1,2,3,4));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4, 5)), AMutableSetWrapper.of(1,2,3,4,5));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6)), AMutableSetWrapper.of(1,2,3,4,5,6));

        assertEquals(AHashSet.of(1, 2, 3), AMutableSetWrapper.from(Arrays.asList(1, 2, 3)));
        assertEquals(AHashSet.of(1, 2, 3), AMutableSetWrapper.from(new Integer[] {1, 2, 3}));
        assertEquals(AHashSet.of(1, 2, 3), AMutableSetWrapper.fromIterator(Arrays.asList(1, 2, 3).iterator()));
    }

    @Override @Test public void testSerDeser () {
        doTest(v -> {
            final AMutableSetWrapper<Integer> orig = (AMutableSetWrapper<Integer>) v.mkSet(1);
            final AMutableSetWrapper<Integer> serDeser = TestHelpers.serDeser(orig);
            assertNotSame(serDeser, orig);
            assertNotSame(serDeser.getInner(), orig.getInner());
            assertEquals(serDeser, orig);
        });
    }

    @Test void testCollector() {
        assertEquals(ASet.of(1, 2, 3, 4), Stream.of(1, 2, 3, 4).collect(AMutableSetWrapper.streamCollector()));
        assertEquals(ASet.empty(), Stream.of().collect(AMutableSetWrapper.streamCollector()));
        assertEquals(ARange.create(0, 100000).toSet(), ARange.create(0, 100000).parallelStream().collect(AMutableSetWrapper.streamCollector()));
    }
}
