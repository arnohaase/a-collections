package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AIteratorTests;

import java.util.Arrays;
import java.util.HashSet;
import java.util.TreeSet;


public class AMutableSortedSetWrapperIteratorTest implements AIteratorTests {
    @Override public boolean isOrdered () {
        return false;
    }

    @Override public AIterator<Integer> mkIterator (Integer... values) {
        return AMutableSortedSetWrapper.wrap(new TreeSet<>(Arrays.asList(values))).iterator();
    }
}
