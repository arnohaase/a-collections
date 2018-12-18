package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.internal.AMapSupport;
import com.ajjpj.acollections.mutable.AMutableSortedSetWrapper;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class SortedKeySetTest implements ASetTests {
    @Override @Test public void testStaticFactories() {
        // nothing to be done - no static factories
    }

    @Override public void testToMap () {
        assertThrows(ClassCastException.class, () -> new KeySetBuilder(Comparator.naturalOrder()).add(1).add(2).add(3).build().toMap());

        //noinspection unchecked
        final AMap m = ATreeMap
                .empty(new AMapSupport.EntryComparator<>(Comparator.naturalOrder()))
                .plus(new AbstractMap.SimpleImmutableEntry(1, "one"), 99)
                ;
        final ASet s = m.keySet();

        assertEquals(AMap.of(1, 3), s.toMap());
    }
    @Override public void testToMutableMap () {
        assertThrows(ClassCastException.class, () -> new KeySetBuilder(Comparator.naturalOrder()).add(1).add(2).add(3).build().toMutableMap());

        //noinspection unchecked
        final AMap m = ATreeMap
                .empty(new AMapSupport.EntryComparator<>(Comparator.naturalOrder()))
                .plus(new AbstractMap.SimpleImmutableEntry(1, "one"), 99)
                ;
        final ASet s = m.keySet();

        assertEquals(AMap.of(1, 3), s.toMutableMap());
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
                new Variant(AMapSupport.SortedKeySet.class, () -> new KeySetBuilder(Comparator.naturalOrder()), AVector.of(1, 2, 3)),
                new Variant(AMapSupport.SortedKeySet.class, () -> new KeySetBuilder(Comparator.<Integer>naturalOrder().reversed()), AVector.of(3, 2, 1))
        );
    }
}
