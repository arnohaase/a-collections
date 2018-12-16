package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.internal.AMapSupport;
import com.ajjpj.acollections.mutable.AMutableSortedSetWrapper;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


public class ATreeSetTest implements ASortedSetTests {
    @Override @Test public void testStaticFactories() {
        assertTrue(ATreeSet.empty().isEmpty());
        assertTrue(ATreeSet.of().isEmpty());
        assertEquals(new HashSet<>(Collections.singletonList(1)), ATreeSet.of(1));
        assertEquals(new HashSet<>(Arrays.asList(1, 2)), ATreeSet.of(1,2));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3)), ATreeSet.of(1,2,3));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4)), ATreeSet.of(1,2,3,4));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4, 5)), ATreeSet.of(1,2,3,4,5));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6)), ATreeSet.of(1,2,3,4,5,6));

        assertEquals(AVector.of(1, 2, 3), ATreeSet.from(Arrays.asList(1, 2, 3)).toVector());
        assertEquals(AVector.of(1, 2, 3), ATreeSet.from(new Integer[] {1, 2, 3}).toVector());
        assertEquals(AVector.of(1, 2, 3), ATreeSet.fromIterator(Arrays.asList(1, 2, 3).iterator()).toVector());

        assertTrue(ATreeSet.empty(Comparator.naturalOrder().reversed()).isEmpty());
        assertEquals(Comparator.naturalOrder().reversed(), ATreeSet.empty(Comparator.naturalOrder().reversed()).comparator());
        
        assertEquals(AVector.of(3, 2, 1), ATreeSet.from(Arrays.asList(1, 2, 3), Comparator.<Integer>naturalOrder().reversed()).toVector());
        assertEquals(AVector.of(3, 2, 1), ATreeSet.from(new Integer[] {1, 2, 3}, Comparator.<Integer>naturalOrder().reversed()).toVector());
        assertEquals(AVector.of(3, 2, 1), ATreeSet.fromIterator(Arrays.asList(1, 2, 3).iterator(), Comparator.<Integer>naturalOrder().reversed()).toVector());
    }
    
    @Test void testStaticFactoriesInASortedSet() {
        assertTrue(ASortedSet.empty().isEmpty());
        assertTrue(ASortedSet.of().isEmpty());
        assertEquals(new HashSet<>(Collections.singletonList(1)), ASortedSet.of(1));
        assertEquals(new HashSet<>(Arrays.asList(1, 2)), ASortedSet.of(1,2));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3)), ASortedSet.of(1,2,3));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4)), ASortedSet.of(1,2,3,4));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4, 5)), ASortedSet.of(1,2,3,4,5));
        assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6)), ASortedSet.of(1,2,3,4,5,6));

        assertEquals(AVector.of(1, 2, 3), ASortedSet.from(Arrays.asList(1, 2, 3)).toVector());
        assertEquals(AVector.of(1, 2, 3), ASortedSet.from(new Integer[] {1, 2, 3}).toVector());
        assertEquals(AVector.of(1, 2, 3), ASortedSet.fromIterator(Arrays.asList(1, 2, 3).iterator()).toVector());

        assertEquals(AVector.of(3, 2, 1), ASortedSet.from(Arrays.asList(1, 2, 3), Comparator.<Integer>naturalOrder().reversed()).toVector());
        assertEquals(AVector.of(3, 2, 1), ASortedSet.from(new Integer[] {1, 2, 3}, Comparator.<Integer>naturalOrder().reversed()).toVector());
        assertEquals(AVector.of(3, 2, 1), ASortedSet.fromIterator(Arrays.asList(1, 2, 3).iterator(), Comparator.<Integer>naturalOrder().reversed()).toVector());
    }

    @Override @Test public void testSerDeser () {
        doTest(v -> {
            assertEquals(AHashSet.empty(), TestHelpers.serDeser(v.mkSet()));
            assertEquals(v.mkSet().getClass(), TestHelpers.serDeser(v.mkSet()).getClass());
            assertEquals(((ATreeSet)v.mkSet()).comparator(), ((ATreeSet)TestHelpers.serDeser(v.mkSet())).comparator());

            assertEquals(AHashSet.of(1), TestHelpers.serDeser(v.mkSet(1)));
            assertEquals(AHashSet.of(1, 2, 3), TestHelpers.serDeser(v.mkSet(1, 2, 3)));
        });
    }

    @Test @Override public void testToSortedSet() {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().toSortedSet());
            assertEquals(v.mkColl(1), v.mkColl(1).toSortedSet());
        });
    }

    @Test void testCollector() {
        assertEquals(ATreeSet.of(1, 2, 3, 4), Stream.of(1, 2, 3, 4).collect(ATreeSet.streamCollector()));
        assertEquals(ATreeSet.empty(), Stream.<Integer>of().collect(ATreeSet.streamCollector()));
        assertEquals(ARange.create(0, 100000).toSortedSet(), ARange.create(0, 100000).parallelStream().collect(ATreeSet.streamCollector()));
    }

    @Test @Override  public void testComparator() {
        assertTrue (ATreeSet.of(1, 2, 3).comparator().compare(1, 2) < 0);
        assertTrue(ATreeSet.<Integer> empty().comparator().compare(1, 2) < 0);

        assertTrue(ATreeSet.empty(Comparator.<Integer>naturalOrder()).comparator().compare(1, 2) < 0);
        assertTrue(ATreeSet.empty(Comparator.<Integer>naturalOrder().reversed()).comparator().compare(1, 2) > 0);
    }

    @Override public Iterable<Variant> variants () {
        return Arrays.asList(
                new Variant(() -> ATreeSet.builder(Comparator.<Integer>naturalOrder()), AVector.of(1, 2, 3)),
                new Variant(() -> ATreeSet.builder(Comparator.<Integer>naturalOrder().reversed()), AVector.of(3, 2, 1))
        );
    }

    @Override public void testToMap () {
        assertThrows(ClassCastException.class, () -> ATreeSet.of(1, 2, 3).toMap());

        final ASet s = ATreeSet
                .<Map.Entry<Integer,String>>empty(new AMapSupport.EntryComparator<>(Comparator.naturalOrder()))
                .plus(new AbstractMap.SimpleImmutableEntry<>(1, "one"));

        assertEquals(AMap.of(1, 3), s.toMap());
    }
    @Override public void testToMutableMap () {
        assertThrows(ClassCastException.class, () -> ATreeSet.of(1, 2, 3).toMutableMap());

        final ASet s = ATreeSet
                .<Map.Entry<Integer,String>>empty(new AMapSupport.EntryComparator<>(Comparator.naturalOrder()))
                .plus(new AbstractMap.SimpleImmutableEntry<>(1, "one"));

        assertEquals(AMap.of(1, 3), s.toMutableMap());
    }
}
