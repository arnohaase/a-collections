package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AListTests;
import com.ajjpj.acollections.TestHelpers;
import com.ajjpj.acollections.immutable.AVector;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


public class AMutableArrayWrapperTest implements AListTests {
    @Override public boolean isImmutable () {
        return false; //TODO add mutable method tests to test suites
    }

    @Override public Iterable<Variant> variants () {
        return Collections.singleton(
                new Variant(AMutableArrayWrapper::builder, AVector.of(1, 2, 3))
        );
    }

    @Override @Test public void testStaticFactories() {
        assertTrue(AMutableArrayWrapper.empty().isEmpty());
        assertTrue(AMutableArrayWrapper.of().isEmpty());
        assertEquals("1", AMutableArrayWrapper.of(1).mkString(","));
        assertEquals("1,2", AMutableArrayWrapper.of(1,2).mkString(","));
        assertEquals("1,2,3", AMutableArrayWrapper.of(1,2,3).mkString(","));
        assertEquals("1,2,3,4", AMutableArrayWrapper.of(1,2,3,4).mkString(","));
        assertEquals("1,2,3,4,5", AMutableArrayWrapper.of(1,2,3,4,5).mkString(","));
        assertEquals("1,2,3,4,5,6", AMutableArrayWrapper.of(1,2,3,4,5,6).mkString(","));

        assertEquals(AMutableArrayWrapper.of(1, 2, 3), AMutableArrayWrapper.from(Arrays.asList(1, 2, 3)));
        assertEquals(AMutableArrayWrapper.of(1, 2, 3), AMutableArrayWrapper.from(new Integer[] {1, 2, 3}));
        assertEquals(AMutableArrayWrapper.of(1, 2, 3), AMutableArrayWrapper.fromIterator(Arrays.asList(1, 2, 3).iterator()));
    }

    @Override @Test public void testSerDeser () {
        doTest(v -> {
            final AMutableArrayWrapper<Integer> orig = (AMutableArrayWrapper<Integer>) v.mkList(1);
            final AMutableArrayWrapper<Integer> serDeser = TestHelpers.serDeser(orig);
            assertNotSame(serDeser, orig);
            assertNotSame(serDeser.getInner(), orig.getInner());
            assertEquals(serDeser, orig);
        });
    }
}
