package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AEntryIteratorTests;
import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AMapTests;
import com.ajjpj.acollections.TestHelpers;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ATreeMapIteratorTest implements AEntryIteratorTests {
    @Override public AIterator<Map.Entry<Integer, Integer>> mkIterator (Integer... values) {
        final ATreeMap.Builder<Integer,Integer> builder = ATreeMap.builder(Comparator.<Integer>naturalOrder());
        for (int v: values)
            builder.add(AEntryIteratorTests.entryOf(v));
        return builder.build().iterator();
    }
}
