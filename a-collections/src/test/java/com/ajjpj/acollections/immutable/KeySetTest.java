package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.ASet;
import com.ajjpj.acollections.ASetTests;
import com.ajjpj.acollections.TestHelpers;
import com.ajjpj.acollections.internal.AMapSupport;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class KeySetTest implements ASetTests {
    @Override @Test public void testStaticFactories() {
        // nothing to be done - no static factories
    }

    @Override public void testJacksonFromJson () {
        // nothing to be done - can not be deserialized from JSON
    }

    @Override public void testJacksonFromJsonSingleValue () {
        // nothing to be done - can not be deserialized from JSON
    }

    @Override @Test public void testSerDeser () {
        doTest(v -> {
            assertEquals(v.mkSet(), TestHelpers.serDeser(v.mkSet()));
            assertEquals(v.mkSet().getClass(), TestHelpers.serDeser(v.mkSet()).getClass());

            assertEquals(v.mkSet(1), TestHelpers.serDeser(v.mkSet(1)));
            assertEquals(v.mkSet(1).getClass(), TestHelpers.serDeser(v.mkSet(1)).getClass());

            assertEquals(v.mkSet(1, 2, 3), TestHelpers.serDeser(v.mkSet(1, 2, 3)));
            assertEquals(v.mkSet(1, 2, 3).getClass(), TestHelpers.serDeser(v.mkSet(1, 2, 3)).getClass());
        });
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
                new Variant(AMapSupport.KeySet.class, KeySetBuilder::new, null)
        );
    }
}
