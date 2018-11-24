package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ASetTests;
import com.ajjpj.acollections.ASortedSet;
import com.ajjpj.acollections.TestHelpers;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class ATreeSetTest implements ASetTests  {
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

    @Override public Iterable<Variant> variants () {
        return Arrays.asList(
                new Variant(() -> ATreeSet.builder(Comparator.<Integer>naturalOrder()), AVector.of(1, 2, 3)),
                new Variant(() -> ATreeSet.builder(Comparator.<Integer>naturalOrder().reversed()), AVector.of(3, 2, 1))
        );
    }


    @Test @Override public void testToSortedSet() {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().toSortedSet());
            assertEquals(v.mkColl(1), v.mkColl(1).toSortedSet());
        });

        // TODO ascending / descending assertEquals(ATreeSet.of(1, 2, 3, 4), v.mkColl(2, 1, 4, 3).toSortedSet());
    }

}
