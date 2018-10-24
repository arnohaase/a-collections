package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AEntryCollectionOpsTests;
import com.ajjpj.acollections.ASet;
import com.ajjpj.acollections.internal.AMapSupport;
import com.ajjpj.acollections.util.AEquality;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;


public class SortedEntrySetTests implements AEntryCollectionOpsTests {
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

        @Override public AEquality equality () {
            return AEquality.fromComparator(entryComparator);
        }
    }

    @Override public boolean isSorted () {
        return true;
    }

    @Override public Iterable<Variant> variants () {
        return Arrays.asList(
                new Variant(() -> new Builder(Comparator.naturalOrder()), AVector.of(1, 2, 3), false),
                new Variant(() -> new Builder(Comparator.<Integer>naturalOrder().reversed()), AVector.of(3, 2, 1), false)
        );
    }
}
