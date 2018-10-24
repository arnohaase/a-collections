package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollectionBuilder;
import com.ajjpj.acollections.AEntryCollectionOpsTests;
import com.ajjpj.acollections.ASet;
import com.ajjpj.acollections.util.AEquality;

import java.util.Arrays;
import java.util.Map;


public class EntrySetTests implements AEntryCollectionOpsTests {
    private static class Builder implements ACollectionBuilder<Map.Entry<Integer, Integer>, ASet<Map.Entry<Integer,Integer>>> {
        private final AHashMap.Builder<Integer,Integer> builder;

        Builder (AEquality equality) {
            this.builder = AHashMap.builder(equality);
        }

        @Override public ACollectionBuilder<Map.Entry<Integer, Integer>, ASet<Map.Entry<Integer, Integer>>> add (Map.Entry<Integer, Integer> el) {
            builder.add(el);
            return this;
        }

        @Override public ASet<Map.Entry<Integer, Integer>> build () {
            return builder.build().entrySet();
        }

        @Override public AEquality equality () {
            return AEquality.EQUALS; // regardless of AMap equality, the entry set is based on EQUALS
        }
    }

    @Override public Iterable<Variant> variants () {
        return Arrays.asList(
                new Variant(() -> new Builder(AEquality.EQUALS), null, false),
                new Variant(() -> new Builder(AEquality.IDENTITY), null, false) // regardless of AMap equality, the entry set is based on EQUALS
        );
    }
}
