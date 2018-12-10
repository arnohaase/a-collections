package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ASet;
import com.ajjpj.acollections.ASetTests;
import com.ajjpj.acollections.TestHelpers;
import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.ARange;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AMutableSortedSetWrapperTest implements ASetTests {
    @Override public boolean isImmutable () {
        return false;
    }

    @Override public Iterable<Variant> variants () {
        return Collections.singletonList(
                new Variant(() -> AMutableSortedSetWrapper.builder(Comparator.naturalOrder()), null)
        );
    }

    @Override @Test public void testStaticFactories() {
        assertTrue(AMutableSortedSetWrapper.empty().isEmpty());
        assertTrue(AMutableSortedSetWrapper.of().isEmpty());
        assertEquals(new HashSet<>(Collections.singletonList(1)), AMutableSortedSetWrapper.of(1));
        assertEquals(new HashSet<>(Arrays.asList(1, 2)), AMutableSortedSetWrapper.of(1,2));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3)), AMutableSortedSetWrapper.of(1,2,3));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4)), AMutableSortedSetWrapper.of(1,2,3,4));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4, 5)), AMutableSortedSetWrapper.of(1,2,3,4,5));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6)), AMutableSortedSetWrapper.of(1,2,3,4,5,6));

        assertEquals(AHashSet.of(1, 2, 3), AMutableSortedSetWrapper.from(Arrays.asList(1, 2, 3)));
        assertEquals(AHashSet.of(1, 2, 3), AMutableSortedSetWrapper.from(new Integer[] {1, 2, 3}));
        assertEquals(AHashSet.of(1, 2, 3), AMutableSortedSetWrapper.fromIterator(Arrays.asList(1, 2, 3).iterator()));
    }

    @Override @Test public void testSerDeser () {
        doTest(v -> {
            final AMutableSortedSetWrapper<Integer> orig = (AMutableSortedSetWrapper<Integer>) v.mkSet(1);
            final AMutableSortedSetWrapper<Integer> serDeser = TestHelpers.serDeser(orig);
            assertNotSame(serDeser, orig);
            assertNotSame(serDeser.getInner(), orig.getInner());
            assertEquals(serDeser, orig);
        });
    }

    @Test void testCollector() {
        assertEquals(ASet.of(1, 2, 3, 4), Stream.of(1, 2, 3, 4).collect(AMutableSortedSetWrapper.streamCollector()));
        assertEquals(ASet.empty(), Stream.of().collect(AMutableSortedSetWrapper.streamCollector()));
        assertEquals(ARange.create(0, 100000).toSet(), ARange.create(0, 100000).parallelStream().collect(AMutableSortedSetWrapper.streamCollector()));
    }
}
