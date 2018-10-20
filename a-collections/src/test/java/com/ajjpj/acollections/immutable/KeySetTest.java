package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.ASet;
import com.ajjpj.acollections.ASetTests;
import com.ajjpj.acollections.util.AEquality;

import java.util.Arrays;


public class KeySetTest implements ASetTests {
    private static class KeySetBuilder implements ACollectionBuilder<Integer, ASet<Integer>> {
        private AHashMap<Integer, Integer> map;

        KeySetBuilder(AEquality equality) {
            this.map = AHashMap.empty(equality);
        }

        @Override public ACollectionBuilder<Integer, ASet<Integer>> add (Integer el) {
            map = map.updated(el, el);
            return this;
        }

        @Override public ASet<Integer> build () {
            return map.keySet();
        }

        @Override public AEquality equality () {
            return map.keyEquality();
        }
    }

    @Override public Iterable<Variant> variants () {
        return Arrays.asList(
                new Variant(() -> new KeySetBuilder(AEquality.EQUALS), null, false),
                new Variant(() -> new KeySetBuilder(AEquality.IDENTITY), null, true)
        );
    }
}
