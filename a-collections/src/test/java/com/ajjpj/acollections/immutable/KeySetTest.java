package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.ASet;
import com.ajjpj.acollections.ASetTests;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.fail;


public class KeySetTest implements ASetTests {
    @Override @Test public void testSerDeser () {
        fail("todo");
    }

    private static class KeySetBuilder implements ACollectionBuilder<Integer, ASet<Integer>> {
        private AHashMap<Integer, Integer> map;

        KeySetBuilder() {
            this.map = AHashMap.empty();
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
        return Collections.singletonList(
                new Variant(KeySetBuilder::new, null)
        );
    }
}
