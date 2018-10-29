package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AEntryIteratorTests;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AIteratorTests;

import java.util.Map;


class AHashMapIteratorTest implements AEntryIteratorTests {
    @Override public boolean isOrdered () {
        return false;
    }

    @Override public AIterator<Map.Entry<Integer,Integer>> mkIterator (Integer... values) {
        AHashMap.Builder<Integer,Integer> b = AHashMap.builder();
        for (int i: values) b.add(i, 2*i+1);
        return b.build().iterator();
    }
}
