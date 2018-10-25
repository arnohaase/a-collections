package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AIteratorTests;

import java.util.Arrays;
import java.util.HashSet;


public class AMutableSetWrapperIteratorTest implements AIteratorTests {
    @Override public AIterator<Integer> mkIterator (Integer... values) {
        return AMutableSetWrapper.wrap(new HashSet<>(Arrays.asList(values))).iterator();
    }
}
