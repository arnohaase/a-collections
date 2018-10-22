package com.ajjpj.acollections;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public interface AListTests extends ACollectionTests {
    @Test default void testReverse() {
        doTest(v -> {
            assertEquals(v.mkList(), v.mkList().reverse());
            assertEquals(v.mkList(1), v.mkList(1).reverse());
            assertEquals(v.mkList(2, 1), v.mkList(1, 2).reverse());
        });
    }
}
