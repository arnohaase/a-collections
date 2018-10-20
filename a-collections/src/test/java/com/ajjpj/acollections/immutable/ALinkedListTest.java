package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AListTests;
import com.ajjpj.acollections.util.AEquality;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;


public class ALinkedListTest implements AListTests {
    @Override public Iterable<Variant> variants () {
        return Arrays.asList(
                new Variant(() -> ALinkedList.builder(AEquality.EQUALS), AVector.of(1, 2, 3), false),
                new Variant(() -> ALinkedList.builder(AEquality.IDENTITY), AVector.of(1, 2, 3), true)
        );
    }

    @Test public void test()  {
        ALinkedList<Integer> l = ALinkedList.of(1, 2);
        assertEquals(1, l.head().intValue());
        assertEquals(2, l.tail().head().intValue());

        ALinkedList<Integer> filtered = ALinkedList.<Integer>builder().addAll(l.iterator().filterNot(el -> el == 2)).build();
        assertEquals(1, filtered.size());
        assertEquals(1, filtered.head().intValue());
    }
}
