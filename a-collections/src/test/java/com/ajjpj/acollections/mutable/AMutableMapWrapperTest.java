package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AMap;
import com.ajjpj.acollections.AMapTests;
import com.ajjpj.acollections.TestHelpers;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class AMutableMapWrapperTest implements AMapTests {
    @Override public boolean isImmutable () {
        return false;
    }

    @Override @Test public void testStaticFactories() {
        assertTrue(AMutableMapWrapper.empty().isEmpty());

        AMap<Integer,String> expected = AMap.empty();
        assertEquals(expected, AMutableMapWrapper.of());
        expected = expected.plus(1, "1");
        assertEquals(expected, AMutableMapWrapper.of(1,"1"));
        expected = expected.plus(2, "2");
        assertEquals(expected, AMutableMapWrapper.of(1,"1", 2, "2"));
        expected = expected.plus(3, "3");
        assertEquals(expected, AMutableMapWrapper.of(1,"1", 2, "2", 3, "3"));
        expected = expected.plus(4, "4");
        assertEquals(expected, AMutableMapWrapper.of(1,"1", 2, "2", 3, "3", 4, "4"));

        assertEquals (AMap.of(5, "5"), AMutableMapWrapper.from(Collections.singletonList(new AbstractMap.SimpleImmutableEntry<>(5, "5"))));
        assertEquals (AMap.of(5, "5"), AMutableMapWrapper.fromIterator(Collections.singletonList(new AbstractMap.SimpleImmutableEntry<>(5, "5")).iterator()));

        assertEquals (AMap.of(5, "5"), AMutableMapWrapper.ofEntries(Collections.singletonList(new AbstractMap.SimpleImmutableEntry<>(5, "5"))));

        assertEquals(expected, AMutableMapWrapper.fromMap(expected));
        assertNotSame(expected, AMutableMapWrapper.fromMap(expected).getInner());
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
