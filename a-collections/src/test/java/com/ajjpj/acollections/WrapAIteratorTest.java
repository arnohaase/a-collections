package com.ajjpj.acollections;

import java.util.Arrays;


public class WrapAIteratorTest implements AIteratorTests {
    @Override public AIterator<Integer> mkIterator (Integer... values) {
        return AIterator.wrap(Arrays.asList(values).iterator());
    }
}
