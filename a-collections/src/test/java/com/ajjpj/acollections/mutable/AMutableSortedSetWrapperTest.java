package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AMap;
import com.ajjpj.acollections.ASet;
import com.ajjpj.acollections.ASortedSetTests;
import com.ajjpj.acollections.TestHelpers;
import com.ajjpj.acollections.immutable.AHashMap;
import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.ARange;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.internal.AMapSupport;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class AMutableSortedSetWrapperTest implements ASortedSetTests {
    @Override public boolean isImmutable () {
        return false;
    }

    @Override public Iterable<Variant> variants () {
        return Arrays.asList(
                new Variant(AMutableSortedSetWrapper.class, () -> AMutableSortedSetWrapper.builder(Comparator.naturalOrder()), AVector.of(1, 2, 3)),
                new Variant(AMutableSortedSetWrapper.class, () -> AMutableSortedSetWrapper.builder(Comparator.<Integer>naturalOrder().reversed()), AVector.of(3, 2, 1))
        );
    }

    @Test @Override  public void testComparator() {
        assertTrue (AMutableSortedSetWrapper.of(1, 2, 3).comparator().compare(1, 2) < 0);
        assertTrue(AMutableSortedSetWrapper.<Integer> empty().comparator().compare(1, 2) < 0);

        assertTrue(AMutableSortedSetWrapper.empty(Comparator.<Integer>naturalOrder()).comparator().compare(1, 2) < 0);
        assertTrue(AMutableSortedSetWrapper.empty(Comparator.<Integer>naturalOrder().reversed()).comparator().compare(1, 2) > 0);
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

        assertEquals(AVector.of(2, 1), AMutableSortedSetWrapper.empty(Comparator.<Integer>naturalOrder().reversed()).plus(1).plus(2).toVector());

        assertEquals(AVector.of(3, 2, 1), AMutableSortedSetWrapper.from(Arrays.asList(1, 2, 3), Comparator.<Integer>naturalOrder().reversed()).toVector());
        assertEquals(AVector.of(3, 2, 1), AMutableSortedSetWrapper.from(new Integer[] {1, 2, 3}, Comparator.<Integer>naturalOrder().reversed()).toVector());
        assertEquals(AVector.of(3, 2, 1), AMutableSortedSetWrapper.fromIterator(Arrays.asList(1, 2, 3).iterator(), Comparator.<Integer>naturalOrder().reversed()).toVector());
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
        assertEquals(ASet.of(1, 2, 3, 4), Stream.of(1, 2, 3, 4).collect(AMutableSortedSetWrapper.streamCollector(Comparator.naturalOrder())));
        assertEquals(ASet.empty(), Stream.<Integer>of().collect(AMutableSortedSetWrapper.streamCollector(Comparator.naturalOrder())));
        assertEquals(ARange.create(0, 100000).toSet(), ARange.create(0, 100000).parallelStream().collect(AMutableSortedSetWrapper.streamCollector(Comparator.naturalOrder())));
    }

    @Test @Override public void testToMap () {
        assertThrows(ClassCastException.class, () -> AMutableSortedSetWrapper.of(1, 2, 3).toMap());

        final ASet s = AMutableSortedSetWrapper
                .<Map.Entry<Integer,String>>empty(new AMapSupport.EntryComparator<>(Comparator.naturalOrder()))
                .plus(new AbstractMap.SimpleImmutableEntry<>(1, "one"));

        assertEquals(AMap.of(1, "one"), s.toMap());
    }
    @Test @Override public void testToMutableMap () {
        assertThrows(ClassCastException.class, () -> AMutableSortedSetWrapper.of(1, 2, 3).toMutableMap());

        final ASet s = AMutableSortedSetWrapper
                .<Map.Entry<Integer,String>>empty(new AMapSupport.EntryComparator<>(Comparator.naturalOrder()))
                .plus(new AbstractMap.SimpleImmutableEntry<>(1, "one"));

        assertEquals(AMap.of(1, "one"), s.toMutableMap());
    }
}
