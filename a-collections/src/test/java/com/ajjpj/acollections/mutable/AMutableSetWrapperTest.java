package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ASetTests;

import java.util.Arrays;


public class AMutableSetWrapperTest implements ASetTests {
    @Override public boolean isImmutable () {
        return false;
    }

    @Override public Iterable<Variant> variants () {
        return Arrays.asList(
                new Variant(AMutableSetWrapper::builder, null, false)
        );
    }
}
