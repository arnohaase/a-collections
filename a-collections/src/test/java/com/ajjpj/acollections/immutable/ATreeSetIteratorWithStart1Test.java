package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AIteratorTests;
import com.ajjpj.acollections.mutable.AMutableArrayWrapper;
import com.ajjpj.acollections.util.AOption;

import java.util.Arrays;


class ATreeSetIteratorWithStart1Test implements AIteratorTests {
    @Override public boolean isOrdered () {
        return false;
    }

    @Override public AIterator<Integer> mkIterator (Integer... values) {
        return AMutableArrayWrapper.wrap(values).prepend(0).toSortedSet().iterator(AOption.some(1));
    }
}
