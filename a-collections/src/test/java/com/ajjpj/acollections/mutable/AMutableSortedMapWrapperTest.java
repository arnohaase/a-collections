package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ASortedMapTests;
import com.ajjpj.acollections.TestHelpers;
import com.ajjpj.acollections.immutable.AVector;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;


public class AMutableSortedMapWrapperTest implements ASortedMapTests {
    @Override public boolean isImmutable () {
        return false;
    }

    @Test @Override public void testComparator () {
//        assertTrue(AMutableSortedMapWrapper.of(9, "a").comparator().compare(1, 2) < 0);
//        assertTrue(AMutableSortedMapWrapper.<Integer, String> empty().comparator().compare(1, 2) < 0);
//
//        assertTrue(AMutableSortedMapWrapper.<Integer,String>empty(Comparator.naturalOrder()).comparator().compare(1, 2) < 0);
//        assertTrue(AMutableSortedMapWrapper.<Integer,String>empty(Comparator.<Integer>naturalOrder().reversed()).comparator().compare(1, 2) > 0);
        fail("todo");
    }

    @Override @Test public void testStaticFactories() {
//        assertTrue(AMutableSortedMapWrapper.empty().isEmpty());
//
//        AMap<Integer,String> expected = AMap.empty();
//        assertEquals(expected, AMutableSortedMapWrapper.of());
//        expected = expected.plus(1, "1");
//        assertEquals(expected, AMutableSortedMapWrapper.of(1,"1"));
//        expected = expected.plus(2, "2");
//        assertEquals(expected, AMutableSortedMapWrapper.of(1,"1", 2, "2"));
//        expected = expected.plus(3, "3");
//        assertEquals(expected, AMutableSortedMapWrapper.of(1,"1", 2, "2", 3, "3"));
//        expected = expected.plus(4, "4");
//        assertEquals(expected, AMutableSortedMapWrapper.of(1,"1", 2, "2", 3, "3", 4, "4"));
//
//        assertEquals (AMap.of(5, "5"), AMutableSortedMapWrapper.from(Collections.singletonList(new AbstractMap.SimpleImmutableEntry<>(5, "5"))));
//        assertEquals (AMap.of(5, "5"), AMutableSortedMapWrapper.fromIterator(Collections.singletonList(new AbstractMap.SimpleImmutableEntry<>(5, "5")).iterator()));
//
//        assertEquals (AMap.of(5, "5"), AMutableSortedMapWrapper.ofEntries(Collections.singletonList(new AbstractMap.SimpleImmutableEntry<>(5, "5"))));
//
//        assertEquals(expected, AMutableSortedMapWrapper.fromMap(expected));
//        assertNotSame(expected, AMutableSortedMapWrapper.fromMap(expected).getInner());
        fail("todo");
    }

    @Override @Test public void testSerDeser () {
        doTest(v -> {
            final AMutableSortedMapWrapper<Integer,Integer> orig = (AMutableSortedMapWrapper<Integer, Integer>) v.mkMap(1);
            final AMutableSortedMapWrapper<Integer,Integer> serDeser = TestHelpers.serDeser(orig);
            assertNotSame(serDeser, orig);
            assertNotSame(serDeser.getInner(), orig.getInner());
            assertEquals(serDeser, orig);
        });
    }

    @Override public Iterable<Variant> variants () {
        return Arrays.asList(
                new Variant(true, () -> AMutableSortedMapWrapper.builder(Comparator.naturalOrder()), AVector.of(1, 2, 3)),
                new Variant(true, () -> AMutableSortedMapWrapper.builder(Comparator.<Integer>naturalOrder().reversed()), AVector.of(3, 2, 1))
        );
    }
}
