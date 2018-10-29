package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class ValueCollectionTest implements ACollectionTests {
    @Override @Test public void testEquals () {
        // value collections really are not supposed to be compared
    }

    @Override @Test public void testHashCode () {
        // value collections really are not supposed to be compared
    }

    @Override @Test public void testSerDeser () {
        doTest(v -> {
            assertEquals(v.mkColl(), TestHelpers.serDeser(v.mkColl()));
            assertEquals(v.mkColl().getClass(), TestHelpers.serDeser(v.mkColl()).getClass());

            assertEquals(v.mkColl(1), TestHelpers.serDeser(v.mkColl(1)));
            assertEquals(v.mkColl(1, 2, 3), TestHelpers.serDeser(v.mkColl(1, 2, 3)));
        });
    }

    private static class ValueCollectionBuilder implements ACollectionBuilder<Integer, ACollection<Integer>> {
        private ATreeMap<Integer, Integer> map;

        ValueCollectionBuilder(Comparator<Integer> comparator) {
            this.map = ATreeMap.empty(comparator);
        }

        @Override public ACollectionBuilder<Integer, ACollection<Integer>> add (Integer el) {
            map = map.plus(el, el);
            return this;
        }

        @Override public ACollection<Integer> build () {
            return map.values();
        }
    }

    @Override public Iterable<Variant> variants () {
        return Arrays.asList(
                new Variant(() -> new ValueCollectionBuilder(Comparator.<Integer>naturalOrder()), AVector.of(1, 2, 3)),
                new Variant(() -> new ValueCollectionBuilder(Comparator.<Integer>naturalOrder().reversed()), AVector.of(3, 2, 1))
        );
    }
}
