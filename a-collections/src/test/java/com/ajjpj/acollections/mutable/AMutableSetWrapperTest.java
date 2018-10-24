package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.ASetTests;

import java.util.Collections;


public class AMutableSetWrapperTest implements ASetTests {
    @Override public boolean isImmutable () {
        return false;
    }

    @Override public Iterable<Variant> variants () {
        return Collections.singletonList(
                new Variant(AMutableSetWrapper::builder, null)
        );
    }
}
