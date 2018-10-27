package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AMapTests;

import java.util.Collections;


public class AMutableMapWrapperTest implements AMapTests {
    @Override public boolean isImmutable () {
        return false;
    }

    @Override public Iterable<Variant> variants () {
        return Collections.singletonList(
                new Variant(AMutableMapWrapper::builder, null)
        );
    }
}
