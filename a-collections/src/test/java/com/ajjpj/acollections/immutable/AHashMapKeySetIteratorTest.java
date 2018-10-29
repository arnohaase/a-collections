package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AIteratorTests;


class AHashMapKeySetIteratorTest implements AIteratorTests {
    @Override public boolean isOrdered () {
        return false;
    }

    @Override public AIterator<Integer> mkIterator (Integer... values) {
        AHashMap.Builder<Integer,Integer> b = AHashMap.builder();
        for (int i: values) b.add(i, 2*i+1);
        return b.build().keySet().iterator();
    }
}
