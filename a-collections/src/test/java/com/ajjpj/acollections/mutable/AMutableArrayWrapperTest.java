package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AListTests;
import com.ajjpj.acollections.TestHelpers;
import com.ajjpj.acollections.immutable.AVector;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;


public class AMutableArrayWrapperTest implements AListTests {
    @Override public boolean isImmutable () {
        return false; //TODO add mutable method tests to test suites
    }

    @Override public Iterable<Variant> variants () {
        return Collections.singleton(
                new Variant(AMutableArrayWrapper::builder, AVector.of(1, 2, 3))
        );
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
