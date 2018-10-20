package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.util.AEquality;

import java.util.Arrays;
import java.util.Comparator;


public class ValueCollectionTest implements ACollectionTests {
    private static class ValueCollectionBuilder implements ACollectionBuilder<Integer, ACollection<Integer>> {
        private ATreeMap<Integer, Integer> map;

        ValueCollectionBuilder(Comparator<Integer> comparator) {
            this.map = ATreeMap.empty(comparator);
        }

        @Override public ACollectionBuilder<Integer, ACollection<Integer>> add (Integer el) {
            map = map.updated(el, el);
            return this;
        }

        @Override public ACollection<Integer> build () {
            return map.values();
        }

        @Override public AEquality equality () {
            return AEquality.EQUALS;
        }
    }

    @Override public Iterable<Variant> variants () {
        return Arrays.asList(
                new Variant(() -> new ValueCollectionBuilder(Comparator.<Integer>naturalOrder()), AVector.of(1, 2, 3), false),
                new Variant(() -> new ValueCollectionBuilder(Comparator.<Integer>naturalOrder().reversed()), AVector.of(3, 2, 1), false)
        );
    }
}
