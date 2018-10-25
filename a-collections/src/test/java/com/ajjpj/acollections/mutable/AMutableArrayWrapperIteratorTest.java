package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AIteratorTests;


public class AMutableArrayWrapperIteratorTest implements AIteratorTests {
    @Override public AIterator<Integer> mkIterator (Integer... values) {
        return AMutableArrayWrapper.wrap(values).iterator();
    }
}
