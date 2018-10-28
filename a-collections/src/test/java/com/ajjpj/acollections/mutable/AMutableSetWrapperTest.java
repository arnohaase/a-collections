package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ASetTests;
import com.ajjpj.acollections.TestHelpers;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;


public class AMutableSetWrapperTest implements ASetTests {
    @Override public boolean isImmutable () {
        return false;
    }

    @Override public Iterable<Variant> variants () {
        return Collections.singletonList(
                new Variant(AMutableSetWrapper::builder, null)
        );
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
