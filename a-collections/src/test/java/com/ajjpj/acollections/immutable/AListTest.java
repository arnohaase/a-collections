package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AList;
import org.junit.jupiter.api.Test;

import java.util.ListIterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AListTest { //TODO integrate with AListTests

    @Test public void listIteratorUsingVectorTest(){
        AVector.Builder<Integer> builder = AVector.builder();
        for (int i=0; i<10; i++) {
            builder.add(i);
        }
        AList<Integer> list = builder.build();

        ListIterator<Integer> listIterator = list.listIterator();
        assertEquals(0, listIterator.next().intValue());
        assertEquals(1, listIterator.next().intValue());
        assertEquals(2, listIterator.next().intValue());
        assertTrue(listIterator.hasNext());
    }

    @Test public void listIteratorUsingVectorWithIndexTest(){
        AVector.Builder<Integer> builder = AVector.builder();
        for (int i=0; i<10; i++) {
            builder.add(i);
        }
        AList<Integer> list = builder.build();

        ListIterator<Integer> listIterator = list.listIterator(5);
        assertEquals(5, listIterator.next().intValue());
        assertEquals(6, listIterator.next().intValue());
        assertEquals(7, listIterator.next().intValue());
        assertEquals(8, listIterator.next().intValue());
        assertEquals(9, listIterator.next().intValue());
        assertFalse(listIterator.hasNext());
    }

    @Test public void listIteratorUsingLinkedListTest(){
        ALinkedList.Builder<Integer> builder = ALinkedList.builder();
        for (int i=0; i<10; i++) {
            builder.add(i);
        }
        AList<Integer> list = builder.build();

        ListIterator<Integer> listIterator = list.listIterator();
        assertEquals(0, listIterator.next().intValue());
        assertEquals(1, listIterator.next().intValue());
        assertEquals(2, listIterator.next().intValue());
        assertEquals(3, listIterator.next().intValue());
        assertTrue(listIterator.hasNext());
    }

    @Test public void listIteratorUsingLinkedListWithIndexTest(){
        ALinkedList.Builder<Integer> builder = ALinkedList.builder();
        for (int i=0; i<10; i++) {
            builder.add(i);
        }
        AList<Integer> list = builder.build();

        ListIterator<Integer> listIterator = list.listIterator(5);
        assertEquals(5, listIterator.next().intValue());
        assertEquals(6, listIterator.next().intValue());
        assertEquals(7, listIterator.next().intValue());
        assertEquals(8, listIterator.next().intValue());
        assertEquals(9, listIterator.next().intValue());
        assertFalse(listIterator.hasNext());
    }
}
