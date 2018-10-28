package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AEntryCollectionOpsTests;
import com.ajjpj.acollections.ASet;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;


public class EntrySetTests implements AEntryCollectionOpsTests {
    @Override @Test public void testSerDeser () {
        fail("todo");
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
