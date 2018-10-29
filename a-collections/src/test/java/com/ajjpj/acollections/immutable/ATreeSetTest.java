package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ASetTests;
import com.ajjpj.acollections.TestHelpers;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class ATreeSetTest implements ASetTests  {
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
