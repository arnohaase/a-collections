package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AIteratorTests;

import java.util.Arrays;


public class AVectorIteratorTest implements AIteratorTests {
    @Override public AIterator<Integer> mkIterator (Integer... values) {
        return AVector.from(Arrays.asList(values)).iterator();
    }
}
