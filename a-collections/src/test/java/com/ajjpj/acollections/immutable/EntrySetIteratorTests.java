package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.*;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class EntrySetIteratorTests implements AEntryIteratorTests {
    @Override public boolean isOrdered () {
        return false;
    }

    @Override public AIterator<Map.Entry<Integer, Integer>> mkIterator (Integer... values) {
        final AHashMap.Builder<Integer,Integer> builder = AHashMap.builder();
        for(int v: values) builder.add(AEntryIteratorTests.entryOf(v));
        return builder.build().entrySet().iterator();
    }
}
