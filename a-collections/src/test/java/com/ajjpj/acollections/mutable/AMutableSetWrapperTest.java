package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ASetTests;
import com.ajjpj.acollections.TestHelpers;
import com.ajjpj.acollections.immutable.AHashSet;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


public class AMutableSetWrapperTest implements ASetTests {
    @Override public boolean isImmutable () {
        return false;
    }

    @Override public Iterable<Variant> variants () {
        return Collections.singletonList(
                new Variant(AMutableSetWrapper::builder, null)
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

}
