package com.ajjpj.acollections.mutable;

import com.ajjpj.acollections.AEntryIteratorTests;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AMap;

import java.util.Map;


public class AMutableMapWrapperIteratorTest implements AEntryIteratorTests {
    @Override public AIterator<Map.Entry<Integer, Integer>> mkIterator (Integer... values) {
        final AMap<Integer,Integer> result = AMutableMapWrapper.empty();
        for (int v: values) result.put(v, 2*v+1);
        return result.iterator();
    }
}
