package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AListTests;
import com.ajjpj.acollections.TestHelpers;
import com.ajjpj.acollections.immutable.AVector;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;


public class AMutableListWrapperTest implements AListTests {
    @Override public boolean isImmutable () {
        return false;
    }

    @Override public Iterable<Variant> variants () {
        //noinspection ArraysAsListWithZeroOrOneArgument
        return Arrays.asList(
                new Variant(AMutableListWrapper::builder, AVector.of(1, 2, 3))
        );
    }

    @Override @Test
    public void testSerDeser () {
        doTest(v -> {
            final AMutableListWrapper<Integer> orig = (AMutableListWrapper<Integer>) v.mkList(1);
            final AMutableListWrapper<Integer> serDeser = TestHelpers.serDeser(orig);
            assertNotSame(serDeser, orig);
            assertNotSame(serDeser.getInner(), orig.getInner());
            assertEquals(serDeser, orig);
        });
    }

}
