package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.util.AEquality;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.*;


public interface AListTests extends ACollectionTests {
    @Test default void testPrepend() {
        doTest(v -> {
            assertEquals(AVector.of(1), v.mkList().prepend(1));
            v.checkEquality(v.mkList().prepend(1));

            assertEquals(AVector.of(2, 1, 3), v.mkList().prepend(3).prepend(1).prepend(2));

            AList<Integer> l = v.mkList();
            l.prepend(1);
            if (isImmutable())
                assertTrue(l.isEmpty());
            else
                assertEquals(AVector.of(1), l);
        });
    }
    @Test default void testAppend() {
        doTest(v -> {
            assertEquals(AVector.of(1), v.mkList().append(1));
            v.checkEquality(v.mkList().append(1));

            assertEquals(AVector.of(2, 1, 3), v.mkList().append(2).append(1).append(3));

            AList<Integer> l = v.mkList();
            l.append(1);
            if (isImmutable())
                assertTrue(l.isEmpty());
            else
                assertEquals(AVector.of(1), l);
        });
    }
    @Test default void testConcatCollection() {
        doTest(v -> {
            assertEquals(AVector.empty(), v.mkList().concat(v.mkList()));
            assertEquals(AVector.of(1), v.mkList().concat(v.mkList(1)));
            assertEquals(AVector.of(1), v.mkList(1).concat(v.mkList()));

            v.checkEquality(v.mkList(1).concat(AHashSet.empty()));
            v.checkEquality(v.mkList(1).concat(AHashSet.empty(AEquality.IDENTITY)));

            assertEquals(AVector.of(2, 4, 3, 1, 2), v.mkList(2, 4).concat(AVector.of(3, 1, 2)));

            AList<Integer> l = v.mkList(1);
            l.concat(AVector.of(2));
            if (isImmutable())
                assertEquals(AVector.of(1), l);
            else
                assertEquals(AVector.of(1, 2), l);
        });
    }
    @Test default void testConcatIterator() {
        doTest(v -> {
            assertEquals(AVector.empty(), v.mkList().concat(v.mkList().iterator()));
            assertEquals(AVector.of(1), v.mkList().concat(v.mkList(1).iterator()));
            assertEquals(AVector.of(1), v.mkList(1).concat(v.mkList()).iterator());

            v.checkEquality(v.mkList(1).concat(AHashSet.<Integer>empty().iterator()));
            v.checkEquality(v.mkList(1).concat(AHashSet.<Integer>empty(AEquality.IDENTITY).iterator()));

            assertEquals(AVector.of(2, 4, 3, 1, 2), v.mkList(2, 4).concat(AVector.of(3, 1, 2).iterator()));

            AList<Integer> l = v.mkList(1);
            l.concat(AVector.of(2).iterator());
            if (isImmutable())
                assertEquals(AVector.of(1), l);
            else
                assertEquals(AVector.of(1, 2), l);
        });
    }
    @Test default void testUpdated() {
        doTest(v -> {
            assertThrows(IndexOutOfBoundsException.class, () -> v.mkList().updated(0, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> v.mkList(1).updated(-1, 2));
            assertThrows(IndexOutOfBoundsException.class, () -> v.mkList(1).updated(1, 2));

            assertEquals(AVector.of(2), v.mkList(1).updated(0, 2));
            v.checkEquality(v.mkList(1).updated(0, 2));

            assertEquals(AVector.of(3, 2), v.mkList(1, 2).updated(0, 3));
            assertEquals(AVector.of(1, 3), v.mkList(1, 2).updated(1, 3));

            assertEquals(AVector.of(1, 4, 3), v.mkList(1, 2, 3).updated(1, 4));

            AList<Integer> l = v.mkList(1);
            l.updated(0, 2);
            if (isImmutable())
                assertTrue(l.isEmpty());
            else
                assertEquals(AVector.of(2), l);
        });
    }
    @Test default void testPatch() {
        doTest(v -> {
            assertEquals(AVector.of(1, 2), v.mkList().patch(0, AVector.of(1, 2), 0));
            v.checkEquality(v.mkList().patch(0, AVector.of(1, 2), 0));

            assertThrows(NoSuchElementException.class, () -> v.mkList().patch(1, AVector.of(1, 2), 0));
            assertThrows(NoSuchElementException.class, () -> v.mkList().patch(-1, AVector.of(1, 2), 0));
            assertThrows(NoSuchElementException.class, () -> v.mkList().patch(0, AVector.of(1, 2), 1));

            assertEquals(AVector.of(2, 3, 1), v.mkList(1).patch(0, AVector.of(2, 3), 0));
            assertEquals(AVector.of(2, 3), v.mkList(1).patch(0, AVector.of(2, 3), 1));
            assertEquals(AVector.of(1, 2, 3), v.mkList(1).patch(1, AVector.of(2, 3), 0));

            assertEquals(AVector.of(3, 4, 1, 2), v.mkList(1, 2).patch(0, AVector.of(3, 4), 0));
            assertEquals(AVector.of(3, 4, 2), v.mkList(1, 2).patch(0, AVector.of(3, 4), 1));
            assertEquals(AVector.of(3, 4), v.mkList(1, 2).patch(0, AVector.of(3, 4), 2));
            assertEquals(AVector.of(1, 3, 4, 2), v.mkList(1, 2).patch(1, AVector.of(3, 4), 0));
            assertEquals(AVector.of(1, 3, 4), v.mkList(1, 2).patch(1, AVector.of(3, 4), 1));
            assertEquals(AVector.of(1, 2, 3, 4), v.mkList(1, 2).patch(2, AVector.of(3, 4), 0));
            assertThrows(NoSuchElementException.class, () -> v.mkList(1, 2).patch(-1, AVector.of(3, 4), 0));
            assertThrows(NoSuchElementException.class, () -> v.mkList(1, 2).patch(3, AVector.of(3, 4), 0));
            assertThrows(NoSuchElementException.class, () -> v.mkList(1, 2).patch(0, AVector.of(3, 4), 3));

            assertEquals(AVector.of(1, 2), v.mkList(1, 2).patch(0, AVector.empty(), 0));
            assertEquals(AVector.of(1, 2), v.mkList(1, 2).patch(0, AVector.empty(), 1));
            assertEquals(AVector.of(1, 2), v.mkList(1, 2).patch(0, AVector.empty(), 2));
            assertEquals(AVector.of(1, 2), v.mkList(1, 2).patch(1, AVector.empty(), 0));
            assertEquals(AVector.of(1, 2), v.mkList(1, 2).patch(1, AVector.empty(), 1));
            assertEquals(AVector.of(1, 2), v.mkList(1, 2).patch(2, AVector.empty(), 0));

            AList<Integer> l = v.mkList(1);
            l.patch(0, AVector.of(2), 1);
            if (isImmutable())
                assertEquals(AVector.of(1), l);
            else
                assertEquals(AVector.of(2), l);
        });
    }

    @Test default void testLast() {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkList().last());
            assertEquals(1, v.mkList(1).last().intValue());
            assertEquals(2, v.mkList(1, 2).last().intValue());
        });
    }
    @Test default void testLastOption() {
        doTest(v -> {
            assertTrue(v.mkList().lastOption().isEmpty());
            assertTrue(v.mkList(1).lastOption().contains(1));
            assertTrue(v.mkList(1, 2).lastOption().contains(2));
        });
    }
    @Test default void testInit() {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkList().init());
            assertEquals(AVector.empty(), v.mkList(1).init());
            assertEquals(AVector.of(1), v.mkList(1, 2).init());
            assertEquals(AVector.of(1, 2), v.mkList(1, 2, 3).init());
        });
    }
    @Test default void testTail() {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkList().tail());
            assertEquals(AVector.empty(), v.mkList(1).tail());
            assertEquals(AVector.of(2), v.mkList(1, 2).tail());
            assertEquals(AVector.of(2, 3), v.mkList(1, 2, 3).tail());
        });
    }

    @Test default void testTake() {
        doTest(v -> {
            assertEquals(AVector.empty(), v.mkList().take(0));
            assertEquals(AVector.empty(), v.mkList().take(1));

            assertEquals(AVector.empty(), v.mkList(1).take(0));
            assertEquals(AVector.of(1), v.mkList(1).take(1));
            assertEquals(AVector.of(1), v.mkList(1).take(2));

            assertEquals(AVector.of(1, 2), v.mkList(1, 2, 3).take(2));
        });
    }
    @Test default void testTakeRight() {
        doTest(v -> {
            assertEquals(AVector.empty(), v.mkList().takeRight(0));
            assertEquals(AVector.empty(), v.mkList().takeRight(1));

            assertEquals(AVector.empty(), v.mkList(1).takeRight(0));
            assertEquals(AVector.of(1), v.mkList(1).takeRight(1));
            assertEquals(AVector.of(1), v.mkList(1).takeRight(2));

            assertEquals(AVector.of(2, 3), v.mkList(1, 2, 3).takeRight(2));
        });
    }
    @Test default void testTakeWhile() {
        fail("todo");
    }
    @Test default void testDrop() {
        doTest(v -> {
            assertEquals(AVector.empty(), v.mkList().drop(0));
            assertEquals(AVector.empty(), v.mkList().drop(1));

            assertEquals(AVector.of(1), v.mkList(1).drop(0));
            assertEquals(AVector.empty(), v.mkList(1).drop(1));
            assertEquals(AVector.empty(), v.mkList(1).drop(2));

            assertEquals(AVector.of(1), v.mkList(1, 2, 3).drop(2));
        });
    }
    @Test default void testDropRight() {
        doTest(v -> {
            assertEquals(AVector.empty(), v.mkList().dropRight(0));
            assertEquals(AVector.empty(), v.mkList().dropRight(1));

            assertEquals(AVector.of(1), v.mkList(1).dropRight(0));
            assertEquals(AVector.empty(), v.mkList(1).dropRight(1));
            assertEquals(AVector.empty(), v.mkList(1).dropRight(2));

            assertEquals(AVector.of(1), v.mkList(1, 2, 3).dropRight(2));
        });
    }
    @Test default void testDropWhile() {
        fail("todo");
    }

    @Test default void testReverse() {
        doTest(v -> {
            assertEquals(AVector.empty(), v.mkList().reverse());
            v.checkEquality(v.mkList().reverse());
            assertEquals(AVector.of(1), v.mkList(1).reverse());
            v.checkEquality(v.mkList(1).reverse());
            assertEquals(AVector.of(2, 1), v.mkList(1, 2).reverse());
            v.checkEquality(v.mkList(1, 2).reverse());
        });
    }
    @Test default void testReverseIterator() {
        doTest(v -> {
            assertEquals(AVector.empty(), v.mkList().reverseIterator().toVector());
            v.checkEquality(v.mkList().reverseIterator().toVector());
            assertEquals(AVector.of(1), v.mkList(1).reverseIterator().toVector());
            v.checkEquality(v.mkList().reverseIterator().toVector());
            assertEquals(AVector.of(2, 1), v.mkList(1, 2).reverseIterator().toVector());
            v.checkEquality(v.mkList().reverseIterator().toVector());
        });
    }

    @Test default void testStartsWith() {
        doTest(v -> {
            assertTrue(v.mkList().startsWith(AVector.empty()));
            assertFalse(v.mkList().startsWith(AVector.of(1)));

            assertTrue(v.mkList(1).startsWith(AVector.empty()));
            assertTrue(v.mkList(1).startsWith(AVector.of(1)));
            assertFalse(v.mkList(1).startsWith(AVector.of(2)));
            assertFalse(v.mkList(1).startsWith(AVector.of(1, 2)));
            assertFalse(v.mkList(1).startsWith(AVector.of(2, 1)));

            assertTrue(v.mkList(1, 2).startsWith(AVector.empty()));
            assertTrue(v.mkList(1, 2).startsWith(AVector.of(1)));
            assertFalse(v.mkList(1, 2).startsWith(AVector.of(2)));
            assertTrue(v.mkList(1, 2).startsWith(AVector.of(1, 2)));
            assertFalse(v.mkList(1, 2).startsWith(AVector.of(2, 1)));

            if (v.isIdentity()) {
                //noinspection UnnecessaryBoxing
                assertFalse(v.mkList(1).startsWith(AVector.of(new Integer(1))));
            }
        });
    }
    @Test default void testEndsWith() {
        doTest(v -> {
            assertTrue(v.mkList().endsWith(AVector.empty()));
            assertFalse(v.mkList().endsWith(AVector.of(1)));

            assertTrue(v.mkList(1).endsWith(AVector.empty()));
            assertTrue(v.mkList(1).endsWith(AVector.of(1)));
            assertFalse(v.mkList(1).endsWith(AVector.of(2)));
            assertFalse(v.mkList(1).endsWith(AVector.of(1, 2)));
            assertFalse(v.mkList(1).endsWith(AVector.of(2, 1)));

            assertTrue(v.mkList(1, 2).endsWith(AVector.empty()));
            assertTrue(v.mkList(1, 2).endsWith(AVector.of(2)));
            assertFalse(v.mkList(1, 2).endsWith(AVector.of(1)));
            assertTrue(v.mkList(1, 2).endsWith(AVector.of(1, 2)));
            assertFalse(v.mkList(1, 2).endsWith(AVector.of(2, 1)));

            if (v.isIdentity()) {
                //noinspection UnnecessaryBoxing
                assertFalse(v.mkList(1).endsWith(AVector.of(new Integer(1))));
            }
        });
    }

    @Test default void testFoldRight() {
        doTest(v -> {
            assertEquals(0, v.mkList().foldRight(0, this::sum).intValue());
            assertEquals(1, v.mkList(1).foldRight(0, this::sum).intValue());
            assertEquals(6, v.mkList(1, 2, 3).foldRight(0, this::sum).intValue());

            if (v.iterationOrder123() != null) {
                final List<Integer> trace = new ArrayList<>();
                v.mkList(3, 2, 1).foldRight(0, (a, b) -> {
                    trace.add(b);
                    return 0;
                });
                assertEquals(Arrays.asList(v.iterationOrder123().get(0), v.iterationOrder123().get(1), v.iterationOrder123().get(2)), trace);
            }
        });
    }
    @Test default void testReduceRight() {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkList().reduceRight(this::sum));
            assertEquals(1, v.mkList(1).reduceRight(this::sum).intValue());
            assertEquals(6, v.mkList(1, 2, 3).reduceRight(this::sum).intValue());

            if (v.iterationOrder123() != null) {
                final List<Integer> trace = new ArrayList<>();
                v.mkList(3, 2, 1).reduceRight((a, b) -> {
                    trace.add(a);
                    trace.add(b);
                    return 0;
                });
                assertEquals(Arrays.asList(v.iterationOrder123().get(0), v.iterationOrder123().get(1), 0, v.iterationOrder123().get(2)), trace);
            }
        });
    }

    @Test default void testListIterator() {
        fail("todo");
    }

    @Test default void testIndexOf() {
        doTest(v -> {
            assertEquals(-1, v.mkList().indexOf(1));
            assertEquals(0, v.mkList(1).indexOf(1));
            assertEquals(0, v.mkList(1, 1).indexOf(1));
            assertEquals(0, v.mkList(1, 2, 1).indexOf(1));
            assertEquals(1, v.mkList(1, 2, 1).indexOf(2));
            assertEquals(1, v.mkList(1, 2, 2, 1, 2).indexOf(2));
            assertEquals(-1, v.mkList(1, 2, 2, 1, 2).indexOf(3));

            if (v.isIdentity()) {
                //noinspection UnnecessaryBoxing
                assertEquals(-1, v.mkList(1).indexOf(new Integer(1)));
            }
        });
    }
    @Test default void testLastIndexOf() {
        doTest(v -> {
            assertEquals(-1, v.mkList().lastIndexOf(1));
            assertEquals(0, v.mkList(1).lastIndexOf(1));
            assertEquals(1, v.mkList(1, 1).lastIndexOf(1));
            assertEquals(2, v.mkList(1, 2, 1).lastIndexOf(1));
            assertEquals(1, v.mkList(1, 2, 1).lastIndexOf(2));
            assertEquals(4, v.mkList(1, 2, 2, 1, 2).lastIndexOf(2));
            assertEquals(-1, v.mkList(1, 2, 2, 1, 2).lastIndexOf(3));

            if (v.isIdentity()) {
                //noinspection UnnecessaryBoxing
                assertEquals(-1, v.mkList(1).lastIndexOf(new Integer(1)));
            }
        });
    }

    @Test default void testIndices() {
        doTest(v -> {
            assertEquals(AVector.empty(), v.mkList().indices());
            assertEquals(AVector.of(0), v.mkList(1).indices());
            assertEquals(AVector.of(0, 1), v.mkList(2, 1).indices());
            assertEquals(AVector.of(0, 1, 2), v.mkList(3, 2, 1).indices());
        });
    }

    @Test default void testListMutators() {
        if (isImmutable()) {
            doTest(v -> {
                assertThrows(UnsupportedOperationException.class, () -> v.mkList(1).addAll(0, AVector.of(1)));
                assertThrows(UnsupportedOperationException.class, () -> v.mkList(1).replaceAll(UnaryOperator.identity()));
                assertThrows(UnsupportedOperationException.class, () -> v.mkList(1).sort(Comparator.naturalOrder()));
                assertThrows(UnsupportedOperationException.class, () -> v.mkList(1).set(0, 2));
                assertThrows(UnsupportedOperationException.class, () -> v.mkList(1).add(0, 2));
                assertThrows(UnsupportedOperationException.class, () -> v.mkList(1).remove(0));
            });
        }
    }
}
