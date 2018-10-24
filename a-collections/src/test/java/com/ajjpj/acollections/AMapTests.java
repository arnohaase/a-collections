package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AHashMap;
import com.ajjpj.acollections.immutable.AHashSet;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.ajjpj.acollections.AEntryCollectionOpsTests.entryOf;
import static org.junit.jupiter.api.Assertions.*;


public interface AMapTests extends AEntryCollectionOpsTests {

    @Test default void testContainsKey() {
        doTest(v -> {
            //noinspection SuspiciousMethodCalls
            assertFalse(v.mkMap().containsKey("abc"));
            assertFalse(v.mkMap().containsKey(1));

            assertTrue(v.mkMap(1).containsKey(1));
            //noinspection SuspiciousMethodCalls
            assertFalse(v.mkMap(1).containsKey("abc"));

            assertTrue(v.mkMap(1, 2, 3, 4).containsKey(1));
            assertTrue(v.mkMap(1, 2, 3, 4).containsKey(2));
            assertTrue(v.mkMap(1, 2, 3, 4).containsKey(3));
            assertTrue(v.mkMap(1, 2, 3, 4).containsKey(4));
            assertFalse(v.mkMap(1, 2, 3, 4).containsKey(0));
            assertFalse(v.mkMap(1, 2, 3, 4).containsKey(5));
        });
    }

    @Test default void testGet() {
        doTest(v -> {
            //noinspection SuspiciousMethodCalls
            assertNull(v.mkMap().get("a"));
            assertNull(v.mkMap().get(1));

            assertEquals(3, v.mkMap(1).get(1).intValue());
            assertNull(v.mkMap(1).get(2));
            //noinspection SuspiciousMethodCalls
            assertNull(v.mkMap(1).get("a"));

            assertEquals(3, v.mkMap(1, 2, 3, 4).get(1).intValue());
            assertEquals(5, v.mkMap(1, 2, 3, 4).get(2).intValue());
            assertEquals(7, v.mkMap(1, 2, 3, 4).get(3).intValue());
            assertEquals(9, v.mkMap(1, 2, 3, 4).get(4).intValue());
            assertNull(v.mkMap(1, 2, 3, 4).get(0));
            assertNull(v.mkMap(1, 2, 3, 4).get(5));
        });
    }
    @Test default void testGetOptional() {
        doTest(v -> {
            assertTrue(v.mkMap().getOptional(1).isEmpty());

            assertTrue(v.mkMap(1).getOptional(1).contains(3));
            assertTrue(v.mkMap(1).getOptional(2).isEmpty());

            assertTrue(v.mkMap(1, 2, 3, 4).getOptional(1).contains(3));
            assertTrue(v.mkMap(1, 2, 3, 4).getOptional(2).contains(5));
            assertTrue(v.mkMap(1, 2, 3, 4).getOptional(3).contains(7));
            assertTrue(v.mkMap(1, 2, 3, 4).getOptional(4).contains(9));
            assertTrue(v.mkMap(1, 2, 3, 4).getOptional(0).isEmpty());
            assertTrue(v.mkMap(1, 2, 3, 4).getOptional(5).isEmpty());
        });
    }

    @Test default void testUpdated() {
        doTest(v -> {
            AMap<Integer,Integer> m = v.mkMap();

            m = m.updated(1, 3);
            assertEquals(v.mkMap(1), m);
            m = m.updated(9, 19);
            assertEquals(v.mkMap(1,9), m);
            m = m.updated(2, 5);
            assertEquals(v.mkMap(2, 9, 1), m);

            m = m.updated(2, 99);
            assertEquals(99, m.get(2).intValue());
            assertEquals(3, m.size());
        });
    }
    @Test default void testRemoved() {
        doTest(v -> {
            assertTrue(v.mkMap().removed(1).isEmpty());

            assertTrue(v.mkMap(1).removed(1).isEmpty());
            assertEquals(v.mkMap(1), v.mkMap(1).removed(2));

            assertEquals(v.mkMap(1, 3), v.mkMap(1, 2, 3).removed(2));
        });
    }

    @Test default void testKeySet() {
        doTest(v -> {
            assertTrue(v.mkMap().keySet().isEmpty());

            assertEquals(AHashSet.of(1), v.mkMap(1).keySet());
            if(isSorted()) {
                assertEquals(v.iterationOrder123().map(Map.Entry::getKey), v.mkMap(1).keySet().toVector());
            }
            else {
                assertEquals(AHashSet.of(1, 2, 3), v.mkMap(1, 2, 3).keySet());
            }
        });
    }
    @Test default void testKeyIterator() {
        doTest(v -> {
            assertTrue(v.mkMap().keysIterator().toSet().isEmpty());

            assertEquals(AHashSet.of(1), v.mkMap(1).keysIterator().toSet());
            if(isSorted()) {
                assertEquals(v.iterationOrder123().map(Map.Entry::getKey), v.mkMap(1).keysIterator().toVector());
            }
            else {
                assertEquals(AHashSet.of(1, 2, 3), v.mkMap(1, 2, 3).keysIterator().toSet());
            }
        });
    }
    @Test default void testValues() {
        doTest(v -> {
            assertTrue(v.mkMap().values().isEmpty());

            assertEquals(AHashSet.of(3), v.mkMap(1).values().toSet());
            if (isSorted()) {
                assertEquals(v.iterationOrder123().map(Map.Entry::getValue), v.mkMap(1).values().toVector());
            }
            else {
                assertEquals(AHashSet.of(3, 5, 7), v.mkMap(1, 2, 3).values().toSet());
            }
        });
    }
    @Test default void testValuesIterator() {
        doTest(v -> {
            assertTrue(v.mkMap().valuesIterator().toSet().isEmpty());

            assertEquals(AHashSet.of(3), v.mkMap(1).valuesIterator().toSet());
            if (isSorted()) {
                assertEquals(v.iterationOrder123().map(Map.Entry::getValue), v.mkMap(1).valuesIterator().toVector());
            }
            else {
                assertEquals(AHashSet.of(3, 5, 7), v.mkMap(1, 2, 3).valuesIterator().toSet());
            }
        });
    }
    @Test default void testEntrySet() {
        doTest(v -> {
            assertTrue(v.mkMap().entrySet().isEmpty());

            assertEquals(AHashSet.of(entryOf(1)), v.mkMap(1).entrySet());
            if (isSorted()) {
                assertEquals(v.iterationOrder123(), v.mkMap(1).entrySet().toVector());
            }
            else {
                assertEquals(AHashSet.of(entryOf(1), entryOf(2), entryOf(3)), v.mkMap(1, 2, 3).entrySet());
            }
        });
    }
    @Test default void testIterator() {
        doTest(v -> {
            assertTrue(v.mkMap().iterator().toSet().isEmpty());

            assertEquals(AHashSet.of(entryOf(1)), v.mkMap(1).iterator().toSet());
            if (isSorted()) {
                assertEquals(v.iterationOrder123(), v.mkMap(1).iterator().toVector());
            }
            else {
                assertEquals(AHashSet.of(entryOf(1), entryOf(2), entryOf(3)), v.mkMap(1, 2, 3).iterator().toSet());
            }
        });
    }

    @Test default void testMutableOps() {
        doTest(v -> {
            if (isImmutable()) {
                assertThrows(UnsupportedOperationException.class, () -> v.mkMap().put(1, 2));
                assertThrows(UnsupportedOperationException.class, () -> v.mkMap(1).put(1, 2));
                assertThrows(UnsupportedOperationException.class, () -> v.mkMap(2).put(1, 2));

                assertThrows(UnsupportedOperationException.class, () -> v.mkMap().remove(1));
                assertThrows(UnsupportedOperationException.class, () -> v.mkMap(1).remove(1));
                assertThrows(UnsupportedOperationException.class, () -> v.mkMap(1).remove(2));

                assertThrows(UnsupportedOperationException.class, () -> v.mkMap().putAll(AHashMap.empty()));

                assertThrows(UnsupportedOperationException.class, () -> v.mkMap().clear());
                assertThrows(UnsupportedOperationException.class, () -> v.mkMap(1).clear());
            }
            //TODO else test mutable operations
        });
    }
}