package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AEntryIteratorTests;
import com.ajjpj.acollections.AIterator;

import java.util.Map;


public class SortedEntrySetIteratorTests implements AEntryIteratorTests {
    @Override public AIterator<Map.Entry<Integer, Integer>> mkIterator (Integer... values) {
        final ATreeMap.Builder<Integer,Integer> builder = ATreeMap.builder();
        for(int v: values) builder.add(AEntryIteratorTests.entryOf(v));
        return builder.build().entrySet().iterator();
    }
}
