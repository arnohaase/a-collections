package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AEntryIteratorTests;
import com.ajjpj.acollections.AIterator;

import java.util.Comparator;
import java.util.Map;


public class ATreeMapReverseIteratorTest implements AEntryIteratorTests {
    @Override public boolean isAscending () {
        return false;
    }

    @Override public AIterator<Map.Entry<Integer, Integer>> mkIterator (Integer... values) {
        final ATreeMap.Builder<Integer,Integer> builder = ATreeMap.builder(Comparator.<Integer>naturalOrder().reversed());
        for (int v: values)
            builder.add(AEntryIteratorTests.entryOf(v));
        return builder.build().iterator();
    }
}
