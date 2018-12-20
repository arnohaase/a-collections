package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AEntryCollectionOpsTests;
import com.ajjpj.acollections.ASet;
import com.ajjpj.acollections.TestHelpers;
import com.ajjpj.acollections.internal.AMapSupport;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import static com.ajjpj.acollections.AEntryCollectionOpsTests.entryOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class SortedEntrySetTests implements AEntryCollectionOpsTests {
    @Override @Test public void testStaticFactories() {
        // nothing to be done - no static factories
    }

    @Override @Test public void testSerDeser () {
        doTest(v -> {
            assertTrue(TestHelpers.serDeser(v.mkColl()).isEmpty());
            assertEquals(v.mkColl().getClass(), TestHelpers.serDeser(v.mkColl()).getClass());

            assertEquals(AHashSet.of(entryOf(1)), TestHelpers.serDeser(v.mkColl(1)));
            assertEquals(AHashSet.of(entryOf(1), entryOf(2), entryOf(3)), TestHelpers.serDeser(v.mkColl(1, 2, 3)));
        });
    }

    private static class Builder implements ACollectionBuilder<Map.Entry<Integer, Integer>, ASet<Map.Entry<Integer,Integer>>> {
        private final ATreeMap.Builder<Integer,Integer> builder;
        private final Comparator<Map.Entry<Integer,Integer>> entryComparator;

        Builder(Comparator<Integer> comparator) {
            this.builder = ATreeMap.builder(comparator);
            this.entryComparator = new AMapSupport.EntryComparator<>(comparator);
        }

        @Override public ACollectionBuilder<Map.Entry<Integer, Integer>, ASet<Map.Entry<Integer, Integer>>> add (Map.Entry<Integer, Integer> el) {
            builder.add(el);
            return this;
        }

        @Override public ASet<Map.Entry<Integer, Integer>> build () {
            return builder.build().entrySet();
        }
    }

    @Override public boolean isSorted () {
        return true;
    }

    @Override public Iterable<Variant> variants () {
        return Arrays.asList(
                new Variant(null,true, () -> new Builder(Comparator.naturalOrder()), AVector.of(1, 2, 3)),
                new Variant(null,true, () -> new Builder(Comparator.<Integer>naturalOrder().reversed()), AVector.of(3, 2, 1))
        );
    }
}
