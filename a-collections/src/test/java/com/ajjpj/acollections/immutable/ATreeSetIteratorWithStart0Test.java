package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AIteratorTests;
import com.ajjpj.acollections.util.AOption;

import java.util.Arrays;


class ATreeSetIteratorWithStart0Test implements AIteratorTests {
    @Override public boolean isOrdered () {
        return false;
    }

    //TODO verify the validity / completeness in the presence of upper bounds

    @Override public AIterator<Integer> mkIterator (Integer... values) {
        return ATreeSet.from(Arrays.asList(values)).iterator(AOption.some(0), true, AOption.none(), false);
    }
}
