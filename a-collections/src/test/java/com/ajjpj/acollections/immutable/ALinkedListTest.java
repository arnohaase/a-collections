package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AListTests;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ALinkedListTest implements AListTests {
    @Override public Iterable<Variant> variants () {
        return Collections.singletonList(
                new Variant(ALinkedList::builder, AVector.of(1, 2, 3))
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
