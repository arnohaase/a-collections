package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AIteratorTests;

import java.util.Arrays;


public class AHashSetIteratorTest implements AIteratorTests {
    @Override public boolean isOrdered () {
        return false;
    }

    @Override public AIterator<Integer> mkIterator (Integer... values) {
        return AHashSet.from(Arrays.asList(values)).iterator();
    }
}
