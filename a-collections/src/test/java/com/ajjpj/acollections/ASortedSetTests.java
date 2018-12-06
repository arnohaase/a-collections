package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.util.AOption;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings({"SimplifiableJUnitAssertion", "ConstantConditions"})
public interface ASortedSetTests extends ASetTests {
    void testComparator();

    @Test default void testCountInRange1() {
        doTest(v -> {
            if (v.isAscending()) {
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), AOption.none()));

                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(1), AOption.none()));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(2), AOption.none()));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(3), AOption.none()));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(4), AOption.none()));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(99), AOption.none()));

                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(9)));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(8)));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(7)));
                assertEquals (2, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(6)));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(2)));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(0)));

                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(2), AOption.some(9)));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(2), AOption.some(8)));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(3), AOption.some(9)));
                assertEquals (2, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(4), AOption.some(7)));

                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(4), AOption.some(4)));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(3), AOption.some(3)));
            }
            else {
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), AOption.none()));

                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(9), AOption.none()));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(8), AOption.none()));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(7), AOption.none()));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(6), AOption.none()));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(0), AOption.none()));

                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(1)));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(2)));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(3)));
                assertEquals (2, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(4)));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(8)));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(9)));

                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(8), AOption.some(1)));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(8), AOption.some(2)));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(7), AOption.some(1)));
                assertEquals (2, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(6), AOption.some(3)));

                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(4), AOption.some(4)));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(3), AOption.some(3)));
            }
        });
    }
    @Test default void testCountInRange2() {
        doTest(v -> {
            if (v.isAscending()) {
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.none(), true));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), false, AOption.none(), false));

                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(1), true, AOption.none(), true));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(1), false, AOption.none(), true));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(2), true, AOption.none(), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(2), false, AOption.none(), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(3), true, AOption.none(), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(3), false, AOption.none(), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(4), true, AOption.none(), true));
                assertEquals (2, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(4), false, AOption.none(), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(99), true, AOption.none(), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(99), false, AOption.none(), true));

                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(9), true));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(9), false));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(8), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(8), false));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(7), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(7), false));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(6), true));
                assertEquals (2, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(6), false));
                assertEquals (1, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(2), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(2), false));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(0), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(0), false));

                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(2), true, AOption.some(9), true));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(2), true, AOption.some(9), false));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(2), false, AOption.some(9), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(2), false, AOption.some(9), false));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(2), true, AOption.some(8), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(2), true, AOption.some(8), false));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(2), false, AOption.some(8), true));
                assertEquals (2, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(2), false, AOption.some(8), false));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(3), true, AOption.some(9), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(3), true, AOption.some(9), false));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(3), false, AOption.some(9), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(3), false, AOption.some(9), false));
                assertEquals (2, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(4), true, AOption.some(7), true));
                assertEquals (2, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(4), true, AOption.some(7), false));
                assertEquals (1, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(4), false, AOption.some(7), true));
                assertEquals (1, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(4), false, AOption.some(7), false));

                assertEquals (1, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(4), true, AOption.some(4), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(4), true, AOption.some(4), false));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(4), false, AOption.some(4), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(4), false, AOption.some(4), false));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(3), true, AOption.some(3), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(3), true, AOption.some(3), false));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(3), false, AOption.some(3), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(3), false, AOption.some(3), false));
            }
            else {
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.none(), true));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), false, AOption.none(), false));

                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(9), true, AOption.none(), true));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(9), false, AOption.none(), true));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(8), true, AOption.none(), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(8), false, AOption.none(), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(7), true, AOption.none(), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(7), false, AOption.none(), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(6), true, AOption.none(), true));
                assertEquals (2, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(6), false, AOption.none(), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(0), true, AOption.none(), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(0), false, AOption.none(), true));

                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(1), true));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(1), false));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(2), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(2), false));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(3), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(3), false));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(4), true));
                assertEquals (2, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(4), false));
                assertEquals (1, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(8), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(8), false));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(9), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(9), false));

                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(8), true, AOption.some(1), true));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(8), true, AOption.some(1), false));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(8), false, AOption.some(1), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(8), false, AOption.some(1), false));
                assertEquals (4, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(8), true, AOption.some(2), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(8), true, AOption.some(2), false));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(8), false, AOption.some(2), true));
                assertEquals (2, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(8), false, AOption.some(2), false));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(7), true, AOption.some(1), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(7), true, AOption.some(1), false));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(7), false, AOption.some(1), true));
                assertEquals (3, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(7), false, AOption.some(1), false));
                assertEquals (2, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(6), true, AOption.some(3), true));
                assertEquals (2, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(6), true, AOption.some(3), false));
                assertEquals (1, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(6), false, AOption.some(3), true));
                assertEquals (1, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(6), false, AOption.some(3), false));

                assertEquals (1, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(6), true, AOption.some(6), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(6), true, AOption.some(6), false));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(6), false, AOption.some(6), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(6), false, AOption.some(6), false));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(7), true, AOption.some(7), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(7), true, AOption.some(7), false));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(7), false, AOption.some(7), true));
                assertEquals (0, v.mkSortedSet(2, 4, 6, 8).countInRange(AOption.some(7), false, AOption.some(7), false));
            }
        });
    }

    @Test default void testDrop() {
        doTest(v -> {
            assertTrue(v.mkSortedSet().drop(0).isEmpty());
            assertTrue(v.mkSortedSet().drop(1).isEmpty());
            assertTrue(v.mkSortedSet().drop(-1).isEmpty());

            if (v.isAscending()) {
                assertEquals (v.mkSortedSet(1, 2, 3), v.mkSortedSet(1, 2, 3).drop(0));
                assertEquals (v.mkSortedSet(2, 3), v.mkSortedSet(1, 2, 3).drop(1));
                assertEquals (v.mkSortedSet(3), v.mkSortedSet(1, 2, 3).drop(2));
                assertEquals (v.mkSortedSet(), v.mkSortedSet(1, 2, 3).drop(3));

                assertEquals (v.mkSortedSet(1, 2, 3), v.mkSortedSet(1, 2, 3).drop(-1));
                assertEquals (v.mkSortedSet(), v.mkSortedSet(1, 2, 3).drop(4));
            }
            else {
                assertEquals (v.mkSortedSet(3, 2, 1), v.mkSortedSet(1, 2, 3).drop(0));
                assertEquals (v.mkSortedSet(2, 1), v.mkSortedSet(1, 2, 3).drop(1));
                assertEquals (v.mkSortedSet(1), v.mkSortedSet(1, 2, 3).drop(2));
                assertEquals (v.mkSortedSet(), v.mkSortedSet(1, 2, 3).drop(3));

                assertEquals (v.mkSortedSet(1, 2, 3), v.mkSortedSet(1, 2, 3).drop(-1));
                assertEquals (v.mkSortedSet(), v.mkSortedSet(1, 2, 3).drop(4));
            }
        });
    }

    @Test default void testTake() {
        doTest(v -> {
            assertTrue(v.mkSortedSet().take(0).isEmpty());
            assertTrue(v.mkSortedSet().take(1).isEmpty());
            assertTrue(v.mkSortedSet().take(-1).isEmpty());

            if (v.isAscending()) {
                assertEquals (v.mkSortedSet(), v.mkSortedSet(1, 2, 3).take(0));
                assertEquals (v.mkSortedSet(1), v.mkSortedSet(1, 2, 3).take(1));
                assertEquals (v.mkSortedSet(1, 2), v.mkSortedSet(1, 2, 3).take(2));
                assertEquals (v.mkSortedSet(1, 2, 3), v.mkSortedSet(1, 2, 3).take(3));

                assertEquals (v.mkSortedSet(), v.mkSortedSet(1, 2, 3).take(-1));
                assertEquals (v.mkSortedSet(1, 2, 3), v.mkSortedSet(1, 2, 3).take(4));
            }
            else {
                assertEquals (v.mkSortedSet(), v.mkSortedSet(1, 2, 3).take(0));
                assertEquals (v.mkSortedSet(3), v.mkSortedSet(1, 2, 3).take(1));
                assertEquals (v.mkSortedSet(3, 2), v.mkSortedSet(1, 2, 3).take(2));
                assertEquals (v.mkSortedSet(3, 2, 1), v.mkSortedSet(1, 2, 3).take(3));

                assertEquals (v.mkSortedSet(), v.mkSortedSet(1, 2, 3).take(-1));
                assertEquals (v.mkSortedSet(3, 2, 1), v.mkSortedSet(1, 2, 3).take(4));
            }
        });
    }

    @Test default void testSmallest() {
        doTest(v -> {
            assertEquals(AOption.none(), v.mkSortedSet().smallest());
            assertEquals(AOption.some(1), v.mkSortedSet(1).smallest());

            if (v.isAscending()) {
                assertEquals(AOption.some(1), v.mkSortedSet(1, 2, 3).smallest());
            }
            else {
                assertEquals(AOption.some(3), v.mkSortedSet(1, 2, 3).smallest());
            }
        });
    }

    @Test default void testGreatest() {
        doTest(v -> {
            assertEquals(AOption.none(), v.mkSortedSet().greatest());
            assertEquals(AOption.some(1), v.mkSortedSet(1).greatest());

            if (v.isAscending()) {
                assertEquals(AOption.some(3), v.mkSortedSet(1, 2, 3).greatest());
            }
            else {
                assertEquals(AOption.some(1), v.mkSortedSet(1, 2, 3).greatest());
            }
        });
    }

    @Test default void testFirst() {
        doTest(v -> {
            assertThrows (NoSuchElementException.class, () -> v.mkSortedSet().first());
            assertEquals (1, v.mkSortedSet(1).first().intValue());
            if (v.isAscending()) {
                assertEquals(1, v.mkSortedSet(1, 2, 3).first().intValue());
            }
            else {
                assertEquals(3, v.mkSortedSet(1, 2, 3).first().intValue());
            }
        });
    }

    @Test default void testLast() {
        doTest(v -> {
            assertThrows (NoSuchElementException.class, () -> v.mkSortedSet().last());
            assertEquals (1, v.mkSortedSet(1).last().intValue());
            if (v.isAscending()) {
                assertEquals(3, v.mkSortedSet(1, 2, 3).last().intValue());
            }
            else {
                assertEquals(1, v.mkSortedSet(1, 2, 3).last().intValue());
            }
        });
    }

    @Test default void testIteratorWithRange() {
        doTest(v -> {
            assertTrue (v.mkSortedSet().iterator(AOption.none(), true, AOption.none(), false).toVector().isEmpty());

            assertEquals (AVector.of(4), v.mkSortedSet(2, 4, 6).iterator(AOption.some(4), true, AOption.some(4), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).iterator(AOption.some(4), true, AOption.some(4), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).iterator(AOption.some(4), false, AOption.some(4), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).iterator(AOption.some(4), false, AOption.some(4), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).iterator(AOption.some(3), true, AOption.some(3), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).iterator(AOption.some(3), true, AOption.some(3), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).iterator(AOption.some(3), false, AOption.some(3), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).iterator(AOption.some(3), false, AOption.some(3), false).toVector());

            if (v.isAscending()) {
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).iterator(AOption.none(), true, AOption.none(), true).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).iterator(AOption.none(), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).iterator(AOption.none(), false, AOption.none(), true).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).iterator(AOption.none(), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).iterator(AOption.some(1), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).iterator(AOption.some(1), false, AOption.none(), false).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).iterator(AOption.some(2), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(4, 6),    v.mkSortedSet(2, 4, 6).iterator(AOption.some(2), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).iterator(AOption.none(), false, AOption.some(7), true).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).iterator(AOption.none(), false, AOption.some(7), false).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).iterator(AOption.none(), false, AOption.some(6), true).toVector());
                assertEquals (AVector.of(2, 4),    v.mkSortedSet(2, 4, 6).iterator(AOption.none(), false, AOption.some(6), false).toVector());

                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).iterator(AOption.some(2), true, AOption.some(6), true).toVector());
                assertEquals (AVector.of(2, 4),    v.mkSortedSet(2, 4, 6).iterator(AOption.some(2), true, AOption.some(6), false).toVector());
                assertEquals (AVector.of(4, 6),    v.mkSortedSet(2, 4, 6).iterator(AOption.some(2), false, AOption.some(6), true).toVector());
                assertEquals (AVector.of(4),       v.mkSortedSet(2, 4, 6).iterator(AOption.some(2), false, AOption.some(6), false).toVector());
            }
            else {
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).iterator(AOption.none(), true, AOption.none(), true).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).iterator(AOption.none(), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).iterator(AOption.none(), false, AOption.none(), true).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).iterator(AOption.none(), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).iterator(AOption.some(7), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).iterator(AOption.some(7), false, AOption.none(), false).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).iterator(AOption.some(6), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(4, 2),    v.mkSortedSet(2, 4, 6).iterator(AOption.some(6), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).iterator(AOption.none(), false, AOption.some(1), true).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).iterator(AOption.none(), false, AOption.some(1), false).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).iterator(AOption.none(), false, AOption.some(2), true).toVector());
                assertEquals (AVector.of(6, 4),    v.mkSortedSet(2, 4, 6).iterator(AOption.none(), false, AOption.some(2), false).toVector());

                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).iterator(AOption.some(6), true, AOption.some(2), true).toVector());
                assertEquals (AVector.of(6, 4),    v.mkSortedSet(2, 4, 6).iterator(AOption.some(6), true, AOption.some(2), false).toVector());
                assertEquals (AVector.of(4, 2),    v.mkSortedSet(2, 4, 6).iterator(AOption.some(6), false, AOption.some(2), true).toVector());
                assertEquals (AVector.of(4),       v.mkSortedSet(2, 4, 6).iterator(AOption.some(6), false, AOption.some(2), false).toVector());
            }
        });
    }

    @Test default void testRange() {
        doTest(v -> {
            assertTrue (v.mkSortedSet().range(AOption.none(), true, AOption.none(), false).toVector().isEmpty());

            assertEquals (AVector.of(4), v.mkSortedSet(2, 4, 6).range(AOption.some(4), true, AOption.some(4), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).range(AOption.some(4), true, AOption.some(4), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).range(AOption.some(4), false, AOption.some(4), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).range(AOption.some(4), false, AOption.some(4), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).range(AOption.some(3), true, AOption.some(3), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).range(AOption.some(3), true, AOption.some(3), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).range(AOption.some(3), false, AOption.some(3), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).range(AOption.some(3), false, AOption.some(3), false).toVector());

            if (v.isAscending()) {
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).range(AOption.none(), true, AOption.none(), true).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).range(AOption.none(), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).range(AOption.none(), false, AOption.none(), true).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).range(AOption.none(), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).range(AOption.some(1), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).range(AOption.some(1), false, AOption.none(), false).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).range(AOption.some(2), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(4, 6),    v.mkSortedSet(2, 4, 6).range(AOption.some(2), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).range(AOption.none(), false, AOption.some(7), true).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).range(AOption.none(), false, AOption.some(7), false).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).range(AOption.none(), false, AOption.some(6), true).toVector());
                assertEquals (AVector.of(2, 4),    v.mkSortedSet(2, 4, 6).range(AOption.none(), false, AOption.some(6), false).toVector());

                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).range(AOption.some(2), true, AOption.some(6), true).toVector());
                assertEquals (AVector.of(2, 4),    v.mkSortedSet(2, 4, 6).range(AOption.some(2), true, AOption.some(6), false).toVector());
                assertEquals (AVector.of(4, 6),    v.mkSortedSet(2, 4, 6).range(AOption.some(2), false, AOption.some(6), true).toVector());
                assertEquals (AVector.of(4),       v.mkSortedSet(2, 4, 6).range(AOption.some(2), false, AOption.some(6), false).toVector());
            }
            else {
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).range(AOption.none(), true, AOption.none(), true).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).range(AOption.none(), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).range(AOption.none(), false, AOption.none(), true).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).range(AOption.none(), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).range(AOption.some(7), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).range(AOption.some(7), false, AOption.none(), false).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).range(AOption.some(6), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(4, 2),    v.mkSortedSet(2, 4, 6).range(AOption.some(6), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).range(AOption.none(), false, AOption.some(1), true).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).range(AOption.none(), false, AOption.some(1), false).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).range(AOption.none(), false, AOption.some(2), true).toVector());
                assertEquals (AVector.of(6, 4),    v.mkSortedSet(2, 4, 6).range(AOption.none(), false, AOption.some(2), false).toVector());

                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).range(AOption.some(6), true, AOption.some(2), true).toVector());
                assertEquals (AVector.of(6, 4),    v.mkSortedSet(2, 4, 6).range(AOption.some(6), true, AOption.some(2), false).toVector());
                assertEquals (AVector.of(4, 2),    v.mkSortedSet(2, 4, 6).range(AOption.some(6), false, AOption.some(2), true).toVector());
                assertEquals (AVector.of(4),       v.mkSortedSet(2, 4, 6).range(AOption.some(6), false, AOption.some(2), false).toVector());
            }
        });
    }

    @Test default void testSubSet() {
        doTest(v -> {
            assertEquals (AVector.of(), v.mkSortedSet().subSet(0, 9).toVector());
            assertEquals (AVector.of(), v.mkSortedSet(2, 4, 6).subSet(4, 4).toVector());
            assertEquals (AVector.of(), v.mkSortedSet(2, 4, 6).subSet(3, 3).toVector());

            if (v.isAscending()) {
                assertEquals (AVector.of(2, 4),    v.mkSortedSet(2, 4, 6).subSet(2, 6).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).subSet(2, 7).toVector());
                assertEquals (AVector.of(),        v.mkSortedSet(2, 4, 6).subSet(9, 0).toVector());
            }
            else {
                assertEquals (AVector.of(6, 4),    v.mkSortedSet(2, 4, 6).subSet(6, 2).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).subSet(6, 1).toVector());
                assertEquals (AVector.of(),        v.mkSortedSet(2, 4, 6).subSet(0, 9).toVector());
            }
        });
    }

    @Test default void testHeadSet() {
        doTest(v -> {
            assertEquals(AVector.empty(), v.mkSortedSet().headSet(9).toVector());

            if(v.isAscending()) {
                assertEquals(AVector.of(), v.mkSortedSet(2, 4, 6).headSet(1).toVector());
                assertEquals(AVector.of(), v.mkSortedSet(2, 4, 6).headSet(2).toVector());
                assertEquals(AVector.of(2), v.mkSortedSet(2, 4, 6).headSet(3).toVector());
                assertEquals(AVector.of(2), v.mkSortedSet(2, 4, 6).headSet(4).toVector());
                assertEquals(AVector.of(2, 4), v.mkSortedSet(2, 4, 6).headSet(5).toVector());
                assertEquals(AVector.of(2, 4), v.mkSortedSet(2, 4, 6).headSet(6).toVector());
                assertEquals(AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).headSet(7).toVector());
            }
            else {
                assertEquals(AVector.of(), v.mkSortedSet(2, 4, 6).headSet(7).toVector());
                assertEquals(AVector.of(), v.mkSortedSet(2, 4, 6).headSet(6).toVector());
                assertEquals(AVector.of(6), v.mkSortedSet(2, 4, 6).headSet(5).toVector());
                assertEquals(AVector.of(6), v.mkSortedSet(2, 4, 6).headSet(4).toVector());
                assertEquals(AVector.of(6, 4), v.mkSortedSet(2, 4, 6).headSet(3).toVector());
                assertEquals(AVector.of(6, 4), v.mkSortedSet(2, 4, 6).headSet(2).toVector());
                assertEquals(AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).headSet(1).toVector());
            }
        });
    }

    @Test default void testTailSet() {
        doTest(v -> {
            assertEquals(AVector.empty(), v.mkSortedSet().tailSet(9).toVector());

            if(v.isAscending()) {
                assertEquals(AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).tailSet(1).toVector());
                assertEquals(AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).tailSet(2).toVector());
                assertEquals(AVector.of(4, 6), v.mkSortedSet(2, 4, 6).tailSet(3).toVector());
                assertEquals(AVector.of(4, 6), v.mkSortedSet(2, 4, 6).tailSet(4).toVector());
                assertEquals(AVector.of(6), v.mkSortedSet(2, 4, 6).tailSet(5).toVector());
                assertEquals(AVector.of(6), v.mkSortedSet(2, 4, 6).tailSet(6).toVector());
                assertEquals(AVector.of(), v.mkSortedSet(2, 4, 6).tailSet(7).toVector());
            }
            else {
                assertEquals(AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).tailSet(7).toVector());
                assertEquals(AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).tailSet(6).toVector());
                assertEquals(AVector.of(4, 2), v.mkSortedSet(2, 4, 6).tailSet(5).toVector());
                assertEquals(AVector.of(4, 2), v.mkSortedSet(2, 4, 6).tailSet(4).toVector());
                assertEquals(AVector.of(2), v.mkSortedSet(2, 4, 6).tailSet(3).toVector());
                assertEquals(AVector.of(2), v.mkSortedSet(2, 4, 6).tailSet(2).toVector());
                assertEquals(AVector.of(), v.mkSortedSet(2, 4, 6).tailSet(1).toVector());
            }
        });
    }

    @Test default void testSubSetWithFlags() {
        doTest(v -> {
            assertEquals (AVector.of(4), v.mkSortedSet(2, 4, 6).subSet(4, true, 4, true).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).subSet(4, true, 4, false).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).subSet(4, false, 4, true).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).subSet(4, false, 4, false).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).subSet(3, true, 3, true).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).subSet(3, true, 3, false).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).subSet(3, false, 3, true).toVector());
            assertEquals (AVector.of(),  v.mkSortedSet(2, 4, 6).subSet(3, false, 3, false).toVector());

            if (v.isAscending()) {
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).subSet(2, true, 6, true).toVector());
                assertEquals (AVector.of(2, 4),    v.mkSortedSet(2, 4, 6).subSet(2, true, 6, false).toVector());
                assertEquals (AVector.of(4, 6),    v.mkSortedSet(2, 4, 6).subSet(2, false, 6, true).toVector());
                assertEquals (AVector.of(4),       v.mkSortedSet(2, 4, 6).subSet(2, false, 6, false).toVector());
            }
            else {
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).subSet(6, true, 2, true).toVector());
                assertEquals (AVector.of(6, 4),    v.mkSortedSet(2, 4, 6).subSet(6, true, 2, false).toVector());
                assertEquals (AVector.of(4, 2),    v.mkSortedSet(2, 4, 6).subSet(6, false, 2, true).toVector());
                assertEquals (AVector.of(4),       v.mkSortedSet(2, 4, 6).subSet(6, false, 2, false).toVector());
            }
        });
    }
    @Test default void testHeadSetWithFlags() {
        doTest(v -> {
            if (v.isAscending()) {
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).headSet(7, true).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).headSet(7, false).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).headSet(6, true).toVector());
                assertEquals (AVector.of(2, 4),    v.mkSortedSet(2, 4, 6).headSet(6, false).toVector());
                assertEquals (AVector.of(2),       v.mkSortedSet(2, 4, 6).headSet(2, true).toVector());
                assertEquals (AVector.of(),        v.mkSortedSet(2, 4, 6).headSet(2, false).toVector());
                assertEquals (AVector.of(),        v.mkSortedSet(2, 4, 6).headSet(1, true).toVector());
                assertEquals (AVector.of(),        v.mkSortedSet(2, 4, 6).headSet(1, false).toVector());
            }
            else {
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).headSet(1, true).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).headSet(1, false).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).headSet(2, true).toVector());
                assertEquals (AVector.of(6, 4),    v.mkSortedSet(2, 4, 6).headSet(2, false).toVector());
                assertEquals (AVector.of(6),       v.mkSortedSet(2, 4, 6).headSet(6, true).toVector());
                assertEquals (AVector.of(),        v.mkSortedSet(2, 4, 6).headSet(6, false).toVector());
                assertEquals (AVector.of(),        v.mkSortedSet(2, 4, 6).headSet(7, true).toVector());
                assertEquals (AVector.of(),        v.mkSortedSet(2, 4, 6).headSet(7, false).toVector());
            }
        });
    }
    @Test default void testTailSetWithFlags() {
        doTest(v -> {
            if (v.isAscending()) {
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).tailSet(1, true).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).tailSet(1, false).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedSet(2, 4, 6).tailSet(2, true).toVector());
                assertEquals (AVector.of(4, 6),    v.mkSortedSet(2, 4, 6).tailSet(2, false).toVector());
                assertEquals (AVector.of(6),       v.mkSortedSet(2, 4, 6).tailSet(6, true).toVector());
                assertEquals (AVector.of(),        v.mkSortedSet(2, 4, 6).tailSet(6, false).toVector());
                assertEquals (AVector.of(),        v.mkSortedSet(2, 4, 6).tailSet(7, true).toVector());
                assertEquals (AVector.of(),        v.mkSortedSet(2, 4, 6).tailSet(7, false).toVector());
            }
            else {
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).tailSet(7, true).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).tailSet(7, false).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedSet(2, 4, 6).tailSet(6, true).toVector());
                assertEquals (AVector.of(4, 2),    v.mkSortedSet(2, 4, 6).tailSet(6, false).toVector());
                assertEquals (AVector.of(2),       v.mkSortedSet(2, 4, 6).tailSet(2, true).toVector());
                assertEquals (AVector.of(),        v.mkSortedSet(2, 4, 6).tailSet(2, false).toVector());
                assertEquals (AVector.of(),        v.mkSortedSet(2, 4, 6).tailSet(1, true).toVector());
                assertEquals (AVector.of(),        v.mkSortedSet(2, 4, 6).tailSet(1, false).toVector());
            }
        });
    }
    @Test default void testLower() {
        doTest(v -> {
            assertNull (v.mkSortedSet().lower(1));

            if (v.isAscending()) {
                assertEquals (null, v.mkSortedSet(2, 4, 6).lower(1));
                assertEquals (null, v.mkSortedSet(2, 4, 6).lower(2));
                assertEquals (2, v.mkSortedSet(2, 4, 6).lower(3).intValue());
                assertEquals (2, v.mkSortedSet(2, 4, 6).lower(4).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).lower(5).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).lower(6).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).lower(7).intValue());
            }
            else {
                assertEquals (2, v.mkSortedSet(2, 4, 6).lower(1).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).lower(2).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).lower(3).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).lower(4).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).lower(5).intValue());
                assertEquals (null, v.mkSortedSet(2, 4, 6).lower(6));
                assertEquals (null, v.mkSortedSet(2, 4, 6).lower(7));
            }
        });
    }
    @Test default void testFloor() {
        doTest(v -> {
            assertNull (v.mkSortedSet().floor(1));

            if (v.isAscending()) {
                assertEquals (null, v.mkSortedSet(2, 4, 6).floor(1));
                assertEquals (2, v.mkSortedSet(2, 4, 6).floor(2).intValue());
                assertEquals (2, v.mkSortedSet(2, 4, 6).floor(3).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).floor(4).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).floor(5).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).floor(6).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).floor(7).intValue());
            }
            else {
                assertEquals (2, v.mkSortedSet(2, 4, 6).floor(1).intValue());
                assertEquals (2, v.mkSortedSet(2, 4, 6).floor(2).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).floor(3).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).floor(4).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).floor(5).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).floor(6).intValue());
                assertEquals (null, v.mkSortedSet(2, 4, 6).floor(7));
            }
        });
    }
    @Test default void testCeiling() {
        doTest(v -> {
            assertNull (v.mkSortedSet().ceiling(1));

            if (v.isAscending()) {
                assertEquals (2, v.mkSortedSet(2, 4, 6).ceiling(1).intValue());
                assertEquals (2, v.mkSortedSet(2, 4, 6).ceiling(2).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).ceiling(3).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).ceiling(4).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).ceiling(5).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).ceiling(6).intValue());
                assertEquals (null, v.mkSortedSet(2, 4, 6).ceiling(7));
            }
            else {
                assertEquals (null, v.mkSortedSet(2, 4, 6).ceiling(1));
                assertEquals (2, v.mkSortedSet(2, 4, 6).ceiling(2).intValue());
                assertEquals (2, v.mkSortedSet(2, 4, 6).ceiling(3).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).ceiling(4).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).ceiling(5).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).ceiling(6).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).ceiling(7).intValue());
            }
        });
    }
    @Test default void testHigher() {
        doTest(v -> {
            assertNull (v.mkSortedSet().ceiling(1));

            if (v.isAscending()) {
                assertEquals (2, v.mkSortedSet(2, 4, 6).ceiling(1).intValue());
                assertEquals (2, v.mkSortedSet(2, 4, 6).ceiling(2).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).ceiling(3).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).ceiling(4).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).ceiling(5).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).ceiling(6).intValue());
                assertEquals (null, v.mkSortedSet(2, 4, 6).ceiling(7));
            }
            else {
                assertEquals (null, v.mkSortedSet(2, 4, 6).ceiling(1));
                assertEquals (2, v.mkSortedSet(2, 4, 6).ceiling(2).intValue());
                assertEquals (2, v.mkSortedSet(2, 4, 6).ceiling(3).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).ceiling(4).intValue());
                assertEquals (4, v.mkSortedSet(2, 4, 6).ceiling(5).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).ceiling(6).intValue());
                assertEquals (6, v.mkSortedSet(2, 4, 6).ceiling(7).intValue());
            }
        });
    }
    @Test default void testPollFirst() {
        doTest(v -> {
            assertThrows(UnsupportedOperationException.class, () -> v.mkSortedSet().pollFirst());
            assertThrows(UnsupportedOperationException.class, () -> v.mkSortedSet(1).pollFirst());
            assertThrows(UnsupportedOperationException.class, () -> v.mkSortedSet(1, 2, 3).pollFirst());
        });
    }
    @Test default void testPollLast() {
        doTest(v -> {
            assertThrows(UnsupportedOperationException.class, () -> v.mkSortedSet().pollLast());
            assertThrows(UnsupportedOperationException.class, () -> v.mkSortedSet(1).pollLast());
            assertThrows(UnsupportedOperationException.class, () -> v.mkSortedSet(1, 2, 3).pollLast());
        });
    }

    @Test default void testReverseIterator() {
        doTest(v -> {
            assertTrue(v.mkSortedSet().reverseIterator().toVector().isEmpty());
            assertEquals(AVector.of(1), v.mkSortedSet(1).reverseIterator().toVector());
            assertEquals(v.iterationOrder123().reverse(), v.mkSortedSet(1, 2, 3).reverseIterator().toVector());
        });
    }
    @Test default void testDescendingIterator() {
        doTest(v -> {
            assertTrue(v.mkSortedSet().descendingIterator().toVector().isEmpty());
            assertEquals(AVector.of(1), v.mkSortedSet(1).descendingIterator().toVector());
            assertEquals(v.iterationOrder123().reverse(), v.mkSortedSet(1, 2, 3).descendingIterator().toVector());
        });
    }
    @Test default void testDescendingSet() {
        doTest(v -> {
            assertTrue(v.mkSortedSet().descendingSet().isEmpty());
            assertEquals(AVector.of(1), v.mkSortedSet(1).descendingSet().toVector());
            assertEquals(v.iterationOrder123().reverse(), v.mkSortedSet(1, 2, 3).descendingSet().toVector());
        });
    }


}
