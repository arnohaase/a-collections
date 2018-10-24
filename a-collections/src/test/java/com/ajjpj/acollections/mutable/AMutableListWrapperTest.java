package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AListTests;
import com.ajjpj.acollections.immutable.AVector;

import java.util.Arrays;


public class AMutableListWrapperTest implements AListTests {
    @Override public boolean isImmutable () {
        return false;
    }

    @Override public Iterable<Variant> variants () {
        //noinspection ArraysAsListWithZeroOrOneArgument
        return Arrays.asList(
                new Variant(AMutableListWrapper::builder, AVector.of(1, 2, 3))
        );
    }
}
