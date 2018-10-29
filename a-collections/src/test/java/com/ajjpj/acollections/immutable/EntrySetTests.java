package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AEntryCollectionOpsTests;
import com.ajjpj.acollections.ASet;
import com.ajjpj.acollections.TestHelpers;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;


public class EntrySetTests implements AEntryCollectionOpsTests {
    @Override @Test public void testSerDeser () {
        doTest(v -> {
            assertEquals(v.mkColl(), TestHelpers.serDeser(v.mkColl()));
            assertEquals(v.mkColl().getClass(), TestHelpers.serDeser(v.mkColl()).getClass());

            assertEquals(v.mkColl(1), TestHelpers.serDeser(v.mkColl(1)));
            assertEquals(v.mkColl(1).getClass(), TestHelpers.serDeser(v.mkColl(1)).getClass());

            assertEquals(v.mkColl(1, 2, 3), TestHelpers.serDeser(v.mkColl(1, 2, 3)));
            assertEquals(v.mkColl(1, 2, 3).getClass(), TestHelpers.serDeser(v.mkColl(1, 2, 3)).getClass());
        });
    }

    private static class Builder implements ACollectionBuilder<Map.Entry<Integer, Integer>, ASet<Map.Entry<Integer,Integer>>> {
        private final AHashMap.Builder<Integer,Integer> builder;

        Builder () {
            this.builder = AHashMap.builder();
        }

        @Override public ACollectionBuilder<Map.Entry<Integer, Integer>, ASet<Map.Entry<Integer, Integer>>> add (Map.Entry<Integer, Integer> el) {
            builder.add(el);
            return this;
        }

        @Override public ASet<Map.Entry<Integer, Integer>> build () {
            return builder.build().entrySet();
        }
    }

    @Override public Iterable<Variant> variants () {
        return Collections.singletonList(
                new Variant(Builder::new, null)
        );
    }
}
