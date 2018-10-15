package com.ajjpj.acollections.immutable;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ALinkedListTest {
    @Test public void test()  {
        ALinkedList<Integer> l = ALinkedList.of(1, 2);
        assertEquals(1, l.head().intValue());
        assertEquals(2, l.tail().head().intValue());

        ALinkedList<Integer> filtered = ALinkedList.<Integer>builder().addAll(l.iterator().filterNot(el -> el == 2)).build();
        assertEquals(1, filtered.size());
        assertEquals(1, filtered.head().intValue());
    }
}
