package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.ASet;
import com.ajjpj.acollections.ASetTests;
import com.ajjpj.acollections.TestHelpers;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class SortedKeySetTest implements ASetTests {
    @Override @Test public void testStaticFactories() {
        // nothing to be done - no static factories
    }

    @Override @Test public void testSerDeser () {
        doTest(v -> {
            assertEquals(v.mkSet(), TestHelpers.serDeser(v.mkSet()));
            assertEquals(v.mkSet().getClass(), TestHelpers.serDeser(v.mkSet()).getClass());

            assertEquals(v.mkSet(1), TestHelpers.serDeser(v.mkSet(1)));
            assertEquals(v.mkSet(1, 2, 3), TestHelpers.serDeser(v.mkSet(1, 2, 3)));
        });
    }

    private static class KeySetBuilder implements ACollectionBuilder<Integer, ASet<Integer>> {
        private ATreeMap<Integer, Integer> map;

        KeySetBuilder(Comparator<Integer> comparator) {
            this.map = ATreeMap.empty(comparator);
        }

        @Override public ACollectionBuilder<Integer, ASet<Integer>> add (Integer el) {
            map = map.plus(el, el);
            return this;
        }

        @Override public ASet<Integer> build () {
            return map.keySet();
        }
    }

    @Override public Iterable<Variant> variants () {
        return Arrays.asList(
                new Variant(() -> new KeySetBuilder(Comparator.naturalOrder()), AVector.of(1, 2, 3)),
                new Variant(() -> new KeySetBuilder(Comparator.<Integer>naturalOrder().reversed()), AVector.of(3, 2, 1))
        );
    }
}
