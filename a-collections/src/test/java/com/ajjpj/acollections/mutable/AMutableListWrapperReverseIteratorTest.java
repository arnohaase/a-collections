package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AIteratorTests;

import java.util.Arrays;


public class AMutableListWrapperReverseIteratorTest implements AIteratorTests {
    @Override public AIterator<Integer> mkIterator (Integer... values) {
        return AMutableListWrapper.wrap(Arrays.asList(values)).reverse().reverseIterator();
    }
}
