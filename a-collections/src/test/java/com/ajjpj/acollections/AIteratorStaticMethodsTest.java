package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AVector;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


class AIteratorStaticMethodsTest {
    @Test void testEmpty() {
        assertFalse(AIterator.empty().hasNext());
        assertThrows(NoSuchElementException.class, () -> AIterator.empty().next());

    }
    @Test void testSingle() {
        assertEquals(AVector.of("a"), AIterator.single("a").toVector());
    }
}
