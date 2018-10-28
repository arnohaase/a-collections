package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AMap;
import com.ajjpj.acollections.AMapTests;
import com.ajjpj.acollections.TestHelpers;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AMutableMapWrapperTest implements AMapTests {
    @Override public boolean isImmutable () {
        return false;
    }

    @Override @Test public void testSerDeser () {
        doTest(v -> {
            final AMutableMapWrapper<Integer,Integer> orig = (AMutableMapWrapper<Integer, Integer>) v.mkMap(1);
            final AMutableMapWrapper<Integer,Integer> serDeser = TestHelpers.serDeser(orig);
            assertNotSame(serDeser, orig);
            assertNotSame(serDeser.getInner(), orig.getInner());
            assertEquals(serDeser, orig);
        });
    }

    @Override public Iterable<Variant> variants () {
        return Collections.singletonList(
                new Variant(AMutableMapWrapper::builder, null)
        );
    }
}
