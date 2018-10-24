package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AListTests;
import com.ajjpj.acollections.immutable.AVector;

import java.util.Collections;


public class AMutableArrayWrapperTest implements AListTests {
    @Override public boolean isImmutable () {
        return false; //TODO add mutable method tests to test suites
    }

    @Override public Iterable<Variant> variants () {
        return Collections.singleton(
                new Variant(AMutableArrayWrapper::builder, AVector.of(1, 2, 3))
        );
    }
}
