package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.util.AOption;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.NoSuchElementException;

import static com.ajjpj.acollections.AEntryCollectionOpsTests.entryOf;
import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("SimplifiableJUnitAssertion")
public interface ASortedMapTests extends AMapTests {
    void testComparator();

    @Test default void testRangeMaintainsWithDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDefaultValue(99);

            m = m
                    .filter(this::isOdd)
                    .filterNot(this::isEven);
            assertEquals(2, m.size());

            assertEquals(3, m.get(1).intValue());
            assertEquals(7, m.get(3).intValue());
            assertEquals(99, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testRangeMaintainsWithDerivedDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDerivedDefaultValue(k -> 99);

            m = m
                    .filter(this::isOdd)
                    .filterNot(this::isEven);
            assertEquals(2, m.size());

            assertEquals(3, m.get(1).intValue());
            assertEquals(7, m.get(3).intValue());
            assertEquals(99, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testDropMaintainsWithDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDefaultValue(99);

            m = m.drop(1);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testDropMaintainsWithDerivedDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDerivedDefaultValue(k -> 99);

            m = m.drop(1);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testTakeMaintainsWithDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDefaultValue(99);

            m = m.take(2);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testTakeMaintainsWithDerivedDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDerivedDefaultValue(k -> 99);

            m = m.take(2);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testSliceMaintainsWithDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDefaultValue(99);

            m = m.slice(1, 3);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testSliceMaintainsWithDerivedDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDerivedDefaultValue(k -> 99);

            m = m.slice(1, 3);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testDescendingMapMaintainsWithDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDefaultValue(99);

            m = m.descendingMap();
            assertEquals(3, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testDescendingMapMaintainsWithDerivedDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDerivedDefaultValue(k -> 99);

            m = m.descendingMap();
            assertEquals(3, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testSubMapWithFlagsMapMaintainsWithDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDefaultValue(99);

            m = v.isAscending() ? m.subMap(2, true, 4, false) : m.subMap(2, true, 0, false);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testSubMapWithFlagsMapMaintainsWithDerivedDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDerivedDefaultValue(k -> 99);

            m = v.isAscending() ? m.subMap(2, true, 4, false) : m.subMap(2, true, 0, false);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testSubMapWithoutFlagsMapMaintainsWithDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDefaultValue(99);

            m = v.isAscending() ? m.subMap(2, 4) : m.subMap(2, 0);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testSubMapWithoutFlagsMapMaintainsWithDerivedDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDerivedDefaultValue(k -> 99);

            m = v.isAscending() ? m.subMap(2, 4) : m.subMap(2, 0);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testHeadMapWithFlagsMapMaintainsWithDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDefaultValue(99);

            m = m.headMap(v.isAscending() ? 3 : 1, false);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testHeadMapWithFlagsMapMaintainsWithDerivedDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDerivedDefaultValue(k -> 99);

            m = m.headMap(v.isAscending() ? 3 : 1, false);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testHeadMapWithoutFlagsMapMaintainsWithDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDefaultValue(99);

            m = m.headMap(v.isAscending() ? 3 : 1);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testHeadMapWithoutFlagsMapMaintainsWithDerivedDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDefaultValue(99);

            m = m.headMap(v.isAscending() ? 3 : 1);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testTailMapWithFlagsMapMaintainsWithDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDefaultValue(99);

            m = m.tailMap(2, true);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testTailMapWithFlagsMapMaintainsWithDerivedDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDerivedDefaultValue(k -> 99);

            m = m.tailMap(2, true);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testTailMapWithoutFlagsMapMaintainsWithDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDefaultValue(99);

            m = m.tailMap(2);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }
    @Test default void testTailMapWithoutFlagsMapMaintainsWithDerivedDefaultValue() {
        doTest(v -> {
            ASortedMap<Integer,Integer> m = v.mkSortedMap(1, 2, 3).withDerivedDefaultValue(k -> 99);

            m = m.tailMap(2);
            assertEquals(2, m.size());

            assertEquals(5, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }


    @Test default void testCountInRange1() {
        doTest(v -> {
            if (v.isAscending()) {
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), AOption.none()));

                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(1), AOption.none()));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(2), AOption.none()));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(3), AOption.none()));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(4), AOption.none()));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(99), AOption.none()));

                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(9)));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(8)));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(7)));
                assertEquals (2, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(6)));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(2)));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(0)));

                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(2), AOption.some(9)));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(2), AOption.some(8)));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(3), AOption.some(9)));
                assertEquals (2, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(4), AOption.some(7)));

                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(4), AOption.some(4)));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(3), AOption.some(3)));
            }
            else {
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), AOption.none()));

                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(9), AOption.none()));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(8), AOption.none()));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(7), AOption.none()));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(6), AOption.none()));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(0), AOption.none()));

                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(1)));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(2)));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(3)));
                assertEquals (2, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(4)));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(8)));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), AOption.some(9)));

                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(8), AOption.some(1)));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(8), AOption.some(2)));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(7), AOption.some(1)));
                assertEquals (2, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(6), AOption.some(3)));

                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(4), AOption.some(4)));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(3), AOption.some(3)));
            }
        });
    }
    @Test default void testCountInRange2() {
        doTest(v -> {
            if (v.isAscending()) {
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.none(), true));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), false, AOption.none(), false));

                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(1), true, AOption.none(), true));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(1), false, AOption.none(), true));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(2), true, AOption.none(), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(2), false, AOption.none(), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(3), true, AOption.none(), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(3), false, AOption.none(), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(4), true, AOption.none(), true));
                assertEquals (2, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(4), false, AOption.none(), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(99), true, AOption.none(), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(99), false, AOption.none(), true));

                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(9), true));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(9), false));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(8), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(8), false));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(7), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(7), false));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(6), true));
                assertEquals (2, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(6), false));
                assertEquals (1, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(2), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(2), false));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(0), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(0), false));

                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(2), true, AOption.some(9), true));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(2), true, AOption.some(9), false));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(2), false, AOption.some(9), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(2), false, AOption.some(9), false));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(2), true, AOption.some(8), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(2), true, AOption.some(8), false));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(2), false, AOption.some(8), true));
                assertEquals (2, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(2), false, AOption.some(8), false));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(3), true, AOption.some(9), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(3), true, AOption.some(9), false));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(3), false, AOption.some(9), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(3), false, AOption.some(9), false));
                assertEquals (2, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(4), true, AOption.some(7), true));
                assertEquals (2, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(4), true, AOption.some(7), false));
                assertEquals (1, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(4), false, AOption.some(7), true));
                assertEquals (1, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(4), false, AOption.some(7), false));

                assertEquals (1, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(4), true, AOption.some(4), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(4), true, AOption.some(4), false));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(4), false, AOption.some(4), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(4), false, AOption.some(4), false));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(3), true, AOption.some(3), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(3), true, AOption.some(3), false));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(3), false, AOption.some(3), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(3), false, AOption.some(3), false));
            }
            else {
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.none(), true));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), false, AOption.none(), false));

                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(9), true, AOption.none(), true));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(9), false, AOption.none(), true));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(8), true, AOption.none(), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(8), false, AOption.none(), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(7), true, AOption.none(), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(7), false, AOption.none(), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(6), true, AOption.none(), true));
                assertEquals (2, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(6), false, AOption.none(), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(0), true, AOption.none(), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(0), false, AOption.none(), true));

                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(1), true));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(1), false));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(2), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(2), false));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(3), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(3), false));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(4), true));
                assertEquals (2, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(4), false));
                assertEquals (1, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(8), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(8), false));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(9), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.none(), true, AOption.some(9), false));

                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(8), true, AOption.some(1), true));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(8), true, AOption.some(1), false));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(8), false, AOption.some(1), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(8), false, AOption.some(1), false));
                assertEquals (4, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(8), true, AOption.some(2), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(8), true, AOption.some(2), false));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(8), false, AOption.some(2), true));
                assertEquals (2, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(8), false, AOption.some(2), false));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(7), true, AOption.some(1), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(7), true, AOption.some(1), false));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(7), false, AOption.some(1), true));
                assertEquals (3, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(7), false, AOption.some(1), false));
                assertEquals (2, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(6), true, AOption.some(3), true));
                assertEquals (2, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(6), true, AOption.some(3), false));
                assertEquals (1, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(6), false, AOption.some(3), true));
                assertEquals (1, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(6), false, AOption.some(3), false));

                assertEquals (1, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(6), true, AOption.some(6), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(6), true, AOption.some(6), false));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(6), false, AOption.some(6), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(6), false, AOption.some(6), false));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(7), true, AOption.some(7), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(7), true, AOption.some(7), false));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(7), false, AOption.some(7), true));
                assertEquals (0, v.mkSortedMap(2, 4, 6, 8).countInRange(AOption.some(7), false, AOption.some(7), false));
            }
        });
    }

    @Test default void testRange() {
        doTest(v -> {
            assertTrue (v.mkSortedMap().range(AOption.none(), true, AOption.none(), false).toVector().isEmpty());

            assertEquals (AVector.of(entryOf(4)), v.mkSortedMap(2, 4, 6).range(AOption.some(4), true, AOption.some(4), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).range(AOption.some(4), true, AOption.some(4), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).range(AOption.some(4), false, AOption.some(4), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).range(AOption.some(4), false, AOption.some(4), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).range(AOption.some(3), true, AOption.some(3), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).range(AOption.some(3), true, AOption.some(3), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).range(AOption.some(3), false, AOption.some(3), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).range(AOption.some(3), false, AOption.some(3), false).toVector());

            if (v.isAscending()) {
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).range(AOption.none(), true, AOption.none(), true).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).range(AOption.none(), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).range(AOption.none(), false, AOption.none(), true).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).range(AOption.none(), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).range(AOption.some(1), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).range(AOption.some(1), false, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).range(AOption.some(2), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(4), entryOf(6)),               v.mkSortedMap(2, 4, 6).range(AOption.some(2), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).range(AOption.none(), false, AOption.some(7), true).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).range(AOption.none(), false, AOption.some(7), false).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).range(AOption.none(), false, AOption.some(6), true).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4)),               v.mkSortedMap(2, 4, 6).range(AOption.none(), false, AOption.some(6), false).toVector());

                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).range(AOption.some(2), true, AOption.some(6), true).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4)),               v.mkSortedMap(2, 4, 6).range(AOption.some(2), true, AOption.some(6), false).toVector());
                assertEquals (AVector.of(entryOf(4), entryOf(6)),               v.mkSortedMap(2, 4, 6).range(AOption.some(2), false, AOption.some(6), true).toVector());
                assertEquals (AVector.of(entryOf(4)),                             v.mkSortedMap(2, 4, 6).range(AOption.some(2), false, AOption.some(6), false).toVector());
            }
            else {
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).range(AOption.none(), true, AOption.none(), true).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).range(AOption.none(), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).range(AOption.none(), false, AOption.none(), true).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).range(AOption.none(), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).range(AOption.some(7), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).range(AOption.some(7), false, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).range(AOption.some(6), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(4), entryOf(2)),               v.mkSortedMap(2, 4, 6).range(AOption.some(6), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).range(AOption.none(), false, AOption.some(1), true).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).range(AOption.none(), false, AOption.some(1), false).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).range(AOption.none(), false, AOption.some(2), true).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4)),               v.mkSortedMap(2, 4, 6).range(AOption.none(), false, AOption.some(2), false).toVector());

                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).range(AOption.some(6), true, AOption.some(2), true).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4)),               v.mkSortedMap(2, 4, 6).range(AOption.some(6), true, AOption.some(2), false).toVector());
                assertEquals (AVector.of(entryOf(4), entryOf(2)),               v.mkSortedMap(2, 4, 6).range(AOption.some(6), false, AOption.some(2), true).toVector());
                assertEquals (AVector.of(entryOf(4)),                             v.mkSortedMap(2, 4, 6).range(AOption.some(6), false, AOption.some(2), false).toVector());
            }
        });
    }

    @Test default void testDrop() {
        doTest(v -> {
            assertTrue(v.mkSortedMap().drop(0).isEmpty());
            assertTrue(v.mkSortedMap().drop(1).isEmpty());
            assertTrue(v.mkSortedMap().drop(-1).isEmpty());

            if (v.isAscending()) {
                assertEquals (v.mkSortedMap(1, 2, 3), v.mkSortedMap(1, 2, 3).drop(0));
                assertEquals (v.mkSortedMap(2, 3), v.mkSortedMap(1, 2, 3).drop(1));
                assertEquals (v.mkSortedMap(3), v.mkSortedMap(1, 2, 3).drop(2));
                assertEquals (v.mkSortedMap(), v.mkSortedMap(1, 2, 3).drop(3));

                assertEquals (v.mkSortedMap(1, 2, 3), v.mkSortedMap(1, 2, 3).drop(-1));
                assertEquals (v.mkSortedMap(), v.mkSortedMap(1, 2, 3).drop(4));
            }
            else {
                assertEquals (v.mkSortedMap(3, 2, 1), v.mkSortedMap(1, 2, 3).drop(0));
                assertEquals (v.mkSortedMap(2, 1), v.mkSortedMap(1, 2, 3).drop(1));
                assertEquals (v.mkSortedMap(1), v.mkSortedMap(1, 2, 3).drop(2));
                assertEquals (v.mkSortedMap(), v.mkSortedMap(1, 2, 3).drop(3));

                assertEquals (v.mkSortedMap(1, 2, 3), v.mkSortedMap(1, 2, 3).drop(-1));
                assertEquals (v.mkSortedMap(), v.mkSortedMap(1, 2, 3).drop(4));
            }
        });
    }

    @Test default void testTake() {
        doTest(v -> {
            assertTrue(v.mkSortedMap().take(0).isEmpty());
            assertTrue(v.mkSortedMap().take(1).isEmpty());
            assertTrue(v.mkSortedMap().take(-1).isEmpty());

            if (v.isAscending()) {
                assertEquals (v.mkSortedMap(), v.mkSortedMap(1, 2, 3).take(0));
                assertEquals (v.mkSortedMap(1), v.mkSortedMap(1, 2, 3).take(1));
                assertEquals (v.mkSortedMap(1, 2), v.mkSortedMap(1, 2, 3).take(2));
                assertEquals (v.mkSortedMap(1, 2, 3), v.mkSortedMap(1, 2, 3).take(3));

                assertEquals (v.mkSortedMap(), v.mkSortedMap(1, 2, 3).take(-1));
                assertEquals (v.mkSortedMap(1, 2, 3), v.mkSortedMap(1, 2, 3).take(4));
            }
            else {
                assertEquals (v.mkSortedMap(), v.mkSortedMap(1, 2, 3).take(0));
                assertEquals (v.mkSortedMap(3), v.mkSortedMap(1, 2, 3).take(1));
                assertEquals (v.mkSortedMap(3, 2), v.mkSortedMap(1, 2, 3).take(2));
                assertEquals (v.mkSortedMap(3, 2, 1), v.mkSortedMap(1, 2, 3).take(3));

                assertEquals (v.mkSortedMap(), v.mkSortedMap(1, 2, 3).take(-1));
                assertEquals (v.mkSortedMap(3, 2, 1), v.mkSortedMap(1, 2, 3).take(4));
            }
        });
    }

    @Test default void testSlice() {
        doTest(v -> {
            assertTrue (v.mkSortedMap().slice(0, 0).isEmpty());
            assertTrue (v.mkSortedMap().slice(0, 10).isEmpty());
            assertTrue (v.mkSortedMap().slice(-10, 10).isEmpty());

            assertEquals (v.mkSortedMap(1), v.mkSortedMap(1).slice(0, 1));
            assertEquals (v.mkSortedMap(), v.mkSortedMap(1).slice(0, 0));
            assertEquals (v.mkSortedMap(), v.mkSortedMap(1).slice(1, 1));
            assertEquals (v.mkSortedMap(1), v.mkSortedMap(1).slice(0, 10));
            assertEquals (v.mkSortedMap(), v.mkSortedMap(1).slice(-10, 0));
            assertEquals (v.mkSortedMap(1), v.mkSortedMap(1).slice(-10, 10));

            assertEquals (v.iterationOrder123(), v.mkSortedMap(1, 2, 3).slice(0, 3).toVector());
            assertEquals (v.iterationOrder123().slice(0, 1), v.mkSortedMap(1, 2, 3).slice(0, 1).toVector());
            assertEquals (v.iterationOrder123().slice(0, 2), v.mkSortedMap(1, 2, 3).slice(0, 2).toVector());
            assertEquals (v.iterationOrder123().slice(1, 2), v.mkSortedMap(1, 2, 3).slice(1, 2).toVector());
            assertEquals (v.iterationOrder123().slice(1, 3), v.mkSortedMap(1, 2, 3).slice(1, 3).toVector());
        });
    }

    @Test default void testSmallest() {
        doTest(v -> {
            assertEquals(AOption.none(), v.mkSortedMap().smallest());
            assertEquals(AOption.some(entryOf(1)), v.mkSortedMap(1).smallest());

            if (v.isAscending()) {
                assertEquals(AOption.some(entryOf(1)), v.mkSortedMap(1, 2, 3).smallest());
            }
            else {
                assertEquals(AOption.some(entryOf(3)), v.mkSortedMap(1, 2, 3).smallest());
            }
        });
    }

    @Test default void testGreatest() {
        doTest(v -> {
            assertEquals(AOption.none(), v.mkSortedMap().greatest());
            assertEquals(AOption.some(entryOf(1)), v.mkSortedMap(1).greatest());

            if (v.isAscending()) {
                assertEquals(AOption.some(entryOf(3)), v.mkSortedMap(1, 2, 3).greatest());
            }
            else {
                assertEquals(AOption.some(entryOf(1)), v.mkSortedMap(1, 2, 3).greatest());
            }
        });
    }

    @Test default void testIteratorWithRange() {
        doTest(v -> {
            assertTrue (v.mkSortedMap().iterator(AOption.none(), true, AOption.none(), false).toVector().isEmpty());

            assertEquals (AVector.of(entryOf(4)), v.mkSortedMap(2, 4, 6).iterator(AOption.some(4), true, AOption.some(4), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).iterator(AOption.some(4), true, AOption.some(4), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).iterator(AOption.some(4), false, AOption.some(4), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).iterator(AOption.some(4), false, AOption.some(4), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).iterator(AOption.some(3), true, AOption.some(3), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).iterator(AOption.some(3), true, AOption.some(3), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).iterator(AOption.some(3), false, AOption.some(3), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).iterator(AOption.some(3), false, AOption.some(3), false).toVector());

            if (v.isAscending()) {
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).iterator(AOption.none(), true, AOption.none(), true).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).iterator(AOption.none(), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).iterator(AOption.none(), false, AOption.none(), true).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).iterator(AOption.none(), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).iterator(AOption.some(1), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).iterator(AOption.some(1), false, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).iterator(AOption.some(2), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(4), entryOf(6)),               v.mkSortedMap(2, 4, 6).iterator(AOption.some(2), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).iterator(AOption.none(), false, AOption.some(7), true).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).iterator(AOption.none(), false, AOption.some(7), false).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).iterator(AOption.none(), false, AOption.some(6), true).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4)),               v.mkSortedMap(2, 4, 6).iterator(AOption.none(), false, AOption.some(6), false).toVector());

                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).iterator(AOption.some(2), true, AOption.some(6), true).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4)),               v.mkSortedMap(2, 4, 6).iterator(AOption.some(2), true, AOption.some(6), false).toVector());
                assertEquals (AVector.of(entryOf(4), entryOf(6)),               v.mkSortedMap(2, 4, 6).iterator(AOption.some(2), false, AOption.some(6), true).toVector());
                assertEquals (AVector.of(entryOf(4)),                             v.mkSortedMap(2, 4, 6).iterator(AOption.some(2), false, AOption.some(6), false).toVector());
            }
            else {
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).iterator(AOption.none(), true, AOption.none(), true).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).iterator(AOption.none(), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).iterator(AOption.none(), false, AOption.none(), true).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).iterator(AOption.none(), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).iterator(AOption.some(7), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).iterator(AOption.some(7), false, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).iterator(AOption.some(6), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(entryOf(4), entryOf(2)),               v.mkSortedMap(2, 4, 6).iterator(AOption.some(6), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).iterator(AOption.none(), false, AOption.some(1), true).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).iterator(AOption.none(), false, AOption.some(1), false).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).iterator(AOption.none(), false, AOption.some(2), true).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4)),               v.mkSortedMap(2, 4, 6).iterator(AOption.none(), false, AOption.some(2), false).toVector());

                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).iterator(AOption.some(6), true, AOption.some(2), true).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4)),               v.mkSortedMap(2, 4, 6).iterator(AOption.some(6), true, AOption.some(2), false).toVector());
                assertEquals (AVector.of(entryOf(4), entryOf(2)),               v.mkSortedMap(2, 4, 6).iterator(AOption.some(6), false, AOption.some(2), true).toVector());
                assertEquals (AVector.of(entryOf(4)),                             v.mkSortedMap(2, 4, 6).iterator(AOption.some(6), false, AOption.some(2), false).toVector());
            }
        });
    }

    @Test default void testKeysIteratorWithRange() {
        doTest(v -> {
            assertTrue (v.mkSortedMap().keysIterator(AOption.none(), true, AOption.none(), false).toVector().isEmpty());

            assertEquals (AVector.of(4), v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(4), true, AOption.some(4), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(4), true, AOption.some(4), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(4), false, AOption.some(4), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(4), false, AOption.some(4), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(3), true, AOption.some(3), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(3), true, AOption.some(3), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(3), false, AOption.some(3), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(3), false, AOption.some(3), false).toVector());

            if (v.isAscending()) {
                assertEquals (AVector.of(2, 4, 6), v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), true, AOption.none(), true).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), false, AOption.none(), true).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(2, 4, 6), v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(1), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(1), false, AOption.none(), false).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(2), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(4, 6),    v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(2), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(2, 4, 6), v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), false, AOption.some(7), true).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), false, AOption.some(7), false).toVector());
                assertEquals (AVector.of(2, 4, 6), v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), false, AOption.some(6), true).toVector());
                assertEquals (AVector.of(2, 4),    v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), false, AOption.some(6), false).toVector());

                assertEquals (AVector.of(2, 4, 6), v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(2), true, AOption.some(6), true).toVector());
                assertEquals (AVector.of(2, 4),    v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(2), true, AOption.some(6), false).toVector());
                assertEquals (AVector.of(4, 6),    v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(2), false, AOption.some(6), true).toVector());
                assertEquals (AVector.of(4),       v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(2), false, AOption.some(6), false).toVector());
            }
            else {
                assertEquals (AVector.of(6, 4, 2), v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), true, AOption.none(), true).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), false, AOption.none(), true).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(6, 4, 2), v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(7), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(7), false, AOption.none(), false).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(6), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(4, 2),    v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(6), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(6, 4, 2), v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), false, AOption.some(1), true).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), false, AOption.some(1), false).toVector());
                assertEquals (AVector.of(6, 4, 2), v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), false, AOption.some(2), true).toVector());
                assertEquals (AVector.of(6, 4),    v.mkSortedMap(2, 4, 6).keysIterator(AOption.none(), false, AOption.some(2), false).toVector());

                assertEquals (AVector.of(6, 4, 2), v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(6), true, AOption.some(2), true).toVector());
                assertEquals (AVector.of(6, 4),    v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(6), true, AOption.some(2), false).toVector());
                assertEquals (AVector.of(4, 2),    v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(6), false, AOption.some(2), true).toVector());
                assertEquals (AVector.of(4),       v.mkSortedMap(2, 4, 6).keysIterator(AOption.some(6), false, AOption.some(2), false).toVector());
            }
        });
    }

    @Test default void testValuesIteratorWithRange() {
        doTest(v -> {
            assertTrue (v.mkSortedMap().valuesIterator(AOption.none(), true, AOption.none(), false).toVector().isEmpty());

            assertEquals (AVector.of(9), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(4), true, AOption.some(4), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(4), true, AOption.some(4), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(4), false, AOption.some(4), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(4), false, AOption.some(4), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(3), true, AOption.some(3), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(3), true, AOption.some(3), false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(3), false, AOption.some(3), true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(3), false, AOption.some(3), false).toVector());

            if (v.isAscending()) {
                assertEquals (AVector.of(5, 9, 13), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), true, AOption.none(), true).toVector());
                assertEquals (AVector.of(5, 9, 13), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(5, 9, 13), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), false, AOption.none(), true).toVector());
                assertEquals (AVector.of(5, 9, 13), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(5, 9, 13), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(1), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(5, 9, 13), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(1), false, AOption.none(), false).toVector());
                assertEquals (AVector.of(5, 9, 13), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(2), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(9, 13),    v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(2), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(5, 9, 13), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), false, AOption.some(7), true).toVector());
                assertEquals (AVector.of(5, 9, 13), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), false, AOption.some(7), false).toVector());
                assertEquals (AVector.of(5, 9, 13), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), false, AOption.some(6), true).toVector());
                assertEquals (AVector.of(5, 9),     v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), false, AOption.some(6), false).toVector());

                assertEquals (AVector.of(5, 9, 13), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(2), true, AOption.some(6), true).toVector());
                assertEquals (AVector.of(5, 9),     v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(2), true, AOption.some(6), false).toVector());
                assertEquals (AVector.of(9, 13),    v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(2), false, AOption.some(6), true).toVector());
                assertEquals (AVector.of(9),        v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(2), false, AOption.some(6), false).toVector());
            }
            else {
                assertEquals (AVector.of(13, 9, 5), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), true, AOption.none(), true).toVector());
                assertEquals (AVector.of(13, 9, 5), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(13, 9, 5), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), false, AOption.none(), true).toVector());
                assertEquals (AVector.of(13, 9, 5), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(13, 9, 5), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(7), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(13, 9, 5), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(7), false, AOption.none(), false).toVector());
                assertEquals (AVector.of(13, 9, 5), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(6), true, AOption.none(), false).toVector());
                assertEquals (AVector.of(9, 5),     v.mkSortedMap(2, 4, 13).valuesIterator(AOption.some(6), false, AOption.none(), false).toVector());

                assertEquals (AVector.of(13, 9, 5), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), false, AOption.some(1), true).toVector());
                assertEquals (AVector.of(13, 9, 5), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), false, AOption.some(1), false).toVector());
                assertEquals (AVector.of(13, 9, 5), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), false, AOption.some(2), true).toVector());
                assertEquals (AVector.of(13, 9),    v.mkSortedMap(2, 4, 6).valuesIterator(AOption.none(), false, AOption.some(2), false).toVector());

                assertEquals (AVector.of(13, 9, 5), v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(6), true, AOption.some(2), true).toVector());
                assertEquals (AVector.of(13, 9),    v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(6), true, AOption.some(2), false).toVector());
                assertEquals (AVector.of(9, 5),     v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(6), false, AOption.some(2), true).toVector());
                assertEquals (AVector.of(9),        v.mkSortedMap(2, 4, 6).valuesIterator(AOption.some(6), false, AOption.some(2), false).toVector());
            }
        });
    }

    @Test default void testLowerEntry() {
        doTest(v -> {
            assertNull (v.mkSortedMap().lowerEntry(1));

            if (v.isAscending()) {
                assertEquals (null, v.mkSortedMap(2, 4, 6).lowerEntry(1));
                assertEquals (null, v.mkSortedMap(2, 4, 6).lowerEntry(2));
                assertEquals (entryOf(2), v.mkSortedMap(2, 4, 6).lowerEntry(3));
                assertEquals (entryOf(2), v.mkSortedMap(2, 4, 6).lowerEntry(4));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).lowerEntry(5));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).lowerEntry(6));
                assertEquals (entryOf(6), v.mkSortedMap(2, 4, 6).lowerEntry(7));
            }
            else {
                assertEquals (entryOf(2), v.mkSortedMap(2, 4, 6).lowerEntry(1));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).lowerEntry(2));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).lowerEntry(3));
                assertEquals (entryOf(6), v.mkSortedMap(2, 4, 6).lowerEntry(4));
                assertEquals (entryOf(6), v.mkSortedMap(2, 4, 6).lowerEntry(5));
                assertEquals (null, v.mkSortedMap(2, 4, 6).lowerEntry(6));
                assertEquals (null, v.mkSortedMap(2, 4, 6).lowerEntry(7));
            }
        });
    }
    @Test default void testLowerKey() {
        doTest(v -> {
            assertNull (v.mkSortedMap().lowerKey(1));

            if (v.isAscending()) {
                assertEquals (null, v.mkSortedMap(2, 4, 6).lowerKey(1));
                assertEquals (null, v.mkSortedMap(2, 4, 6).lowerKey(2));
                assertEquals (2, v.mkSortedMap(2, 4, 6).lowerKey(3).intValue());
                assertEquals (2, v.mkSortedMap(2, 4, 6).lowerKey(4).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).lowerKey(5).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).lowerKey(6).intValue());
                assertEquals (6, v.mkSortedMap(2, 4, 6).lowerKey(7).intValue());
            }
            else {
                assertEquals (2, v.mkSortedMap(2, 4, 6).lowerKey(1).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).lowerKey(2).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).lowerKey(3).intValue());
                assertEquals (6, v.mkSortedMap(2, 4, 6).lowerKey(4).intValue());
                assertEquals (6, v.mkSortedMap(2, 4, 6).lowerKey(5).intValue());
                assertEquals (null, v.mkSortedMap(2, 4, 6).lowerKey(6));
                assertEquals (null, v.mkSortedMap(2, 4, 6).lowerKey(7));
            }
        });
    }
    @Test default void testFloorEntry() {
        doTest(v -> {
            assertNull (v.mkSortedMap().floorEntry(1));

            if (v.isAscending()) {
                assertEquals (null, v.mkSortedMap(2, 4, 6).floorEntry(1));
                assertEquals (entryOf(2), v.mkSortedMap(2, 4, 6).floorEntry(2));
                assertEquals (entryOf(2), v.mkSortedMap(2, 4, 6).floorEntry(3));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).floorEntry(4));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).floorEntry(5));
                assertEquals (entryOf(6), v.mkSortedMap(2, 4, 6).floorEntry(6));
                assertEquals (entryOf(6), v.mkSortedMap(2, 4, 6).floorEntry(7));
            }
            else {
                assertEquals (entryOf(2), v.mkSortedMap(2, 4, 6).floorEntry(1));
                assertEquals (entryOf(2), v.mkSortedMap(2, 4, 6).floorEntry(2));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).floorEntry(3));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).floorEntry(4));
                assertEquals (entryOf(6), v.mkSortedMap(2, 4, 6).floorEntry(5));
                assertEquals (entryOf(6), v.mkSortedMap(2, 4, 6).floorEntry(6));
                assertEquals (null, v.mkSortedMap(2, 4, 6).floorEntry(7));
            }
        });
    }
    @Test default void testFloorKey() {
        doTest(v -> {
            assertNull (v.mkSortedMap().floorKey(1));

            if (v.isAscending()) {
                assertEquals (null, v.mkSortedMap(2, 4, 6).floorKey(1));
                assertEquals (2, v.mkSortedMap(2, 4, 6).floorKey(2).intValue());
                assertEquals (2, v.mkSortedMap(2, 4, 6).floorKey(3).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).floorKey(4).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).floorKey(5).intValue());
                assertEquals (6, v.mkSortedMap(2, 4, 6).floorKey(6).intValue());
                assertEquals (6, v.mkSortedMap(2, 4, 6).floorKey(7).intValue());
            }
            else {
                assertEquals (2, v.mkSortedMap(2, 4, 6).floorKey(1).intValue());
                assertEquals (2, v.mkSortedMap(2, 4, 6).floorKey(2).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).floorKey(3).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).floorKey(4).intValue());
                assertEquals (6, v.mkSortedMap(2, 4, 6).floorKey(5).intValue());
                assertEquals (6, v.mkSortedMap(2, 4, 6).floorKey(6).intValue());
                assertEquals (null, v.mkSortedMap(2, 4, 6).floorKey(7));
            }
        });
    }
    @Test default void testHigherEntry() {
        doTest(v -> {
            assertNull (v.mkSortedMap().higherEntry(1));

            if (v.isAscending()) {
                assertEquals (entryOf(2), v.mkSortedMap(2, 4, 6).higherEntry(1));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).higherEntry(2));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).higherEntry(3));
                assertEquals (entryOf(6), v.mkSortedMap(2, 4, 6).higherEntry(4));
                assertEquals (entryOf(6), v.mkSortedMap(2, 4, 6).higherEntry(5));
                assertEquals (null, v.mkSortedMap(2, 4, 6).higherEntry(6));
                assertEquals (null, v.mkSortedMap(2, 4, 6).higherEntry(7));
            }
            else {
                assertEquals (null, v.mkSortedMap(2, 4, 6).higherEntry(1));
                assertEquals (null, v.mkSortedMap(2, 4, 6).higherEntry(2));
                assertEquals (entryOf(2), v.mkSortedMap(2, 4, 6).higherEntry(3));
                assertEquals (entryOf(2), v.mkSortedMap(2, 4, 6).higherEntry(4));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).higherEntry(5));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).higherEntry(6));
                assertEquals (entryOf(6), v.mkSortedMap(2, 4, 6).higherEntry(7));
            }
        });
    }
    @Test default void testHigherKey() {
        doTest(v -> {
            assertNull (v.mkSortedMap().higherKey(1));

            if (v.isAscending()) {
                assertEquals (2, v.mkSortedMap(2, 4, 6).higherKey(1).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).higherKey(2).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).higherKey(3).intValue());
                assertEquals (6, v.mkSortedMap(2, 4, 6).higherKey(4).intValue());
                assertEquals (6, v.mkSortedMap(2, 4, 6).higherKey(5).intValue());
                assertEquals (null, v.mkSortedMap(2, 4, 6).higherKey(6));
                assertEquals (null, v.mkSortedMap(2, 4, 6).higherKey(7));
            }
            else {
                assertEquals (null, v.mkSortedMap(2, 4, 6).higherKey(1));
                assertEquals (null, v.mkSortedMap(2, 4, 6).higherKey(2));
                assertEquals (2, v.mkSortedMap(2, 4, 6).higherKey(3).intValue());
                assertEquals (2, v.mkSortedMap(2, 4, 6).higherKey(4).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).higherKey(5).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).higherKey(6).intValue());
                assertEquals (6, v.mkSortedMap(2, 4, 6).higherKey(7).intValue());
            }
        });
    }
    @Test default void testCeilingEntry() {
        doTest(v -> {
            assertNull (v.mkSortedMap().ceilingEntry(1));

            if (v.isAscending()) {
                assertEquals (entryOf(2), v.mkSortedMap(2, 4, 6).ceilingEntry(1));
                assertEquals (entryOf(2), v.mkSortedMap(2, 4, 6).ceilingEntry(2));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).ceilingEntry(3));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).ceilingEntry(4));
                assertEquals (entryOf(6), v.mkSortedMap(2, 4, 6).ceilingEntry(5));
                assertEquals (entryOf(6), v.mkSortedMap(2, 4, 6).ceilingEntry(6));
                assertEquals (null, v.mkSortedMap(2, 4, 6).ceilingEntry(7));
            }
            else {
                assertEquals (null, v.mkSortedMap(2, 4, 6).ceilingEntry(1));
                assertEquals (entryOf(2), v.mkSortedMap(2, 4, 6).ceilingEntry(2));
                assertEquals (entryOf(2), v.mkSortedMap(2, 4, 6).ceilingEntry(3));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).ceilingEntry(4));
                assertEquals (entryOf(4), v.mkSortedMap(2, 4, 6).ceilingEntry(5));
                assertEquals (entryOf(6), v.mkSortedMap(2, 4, 6).ceilingEntry(6));
                assertEquals (entryOf(6), v.mkSortedMap(2, 4, 6).ceilingEntry(7));
            }
        });
    }
    @Test default void testCeilingKey() {
        doTest(v -> {
            assertNull (v.mkSortedMap().ceilingKey(1));

            if (v.isAscending()) {
                assertEquals (2, v.mkSortedMap(2, 4, 6).ceilingKey(1).intValue());
                assertEquals (2, v.mkSortedMap(2, 4, 6).ceilingKey(2).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).ceilingKey(3).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).ceilingKey(4).intValue());
                assertEquals (6, v.mkSortedMap(2, 4, 6).ceilingKey(5).intValue());
                assertEquals (6, v.mkSortedMap(2, 4, 6).ceilingKey(6).intValue());
                assertEquals (null, v.mkSortedMap(2, 4, 6).ceilingKey(7));
            }
            else {
                assertEquals (null, v.mkSortedMap(2, 4, 6).ceilingKey(1));
                assertEquals (2, v.mkSortedMap(2, 4, 6).ceilingKey(2).intValue());
                assertEquals (2, v.mkSortedMap(2, 4, 6).ceilingKey(3).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).ceilingKey(4).intValue());
                assertEquals (4, v.mkSortedMap(2, 4, 6).ceilingKey(5).intValue());
                assertEquals (6, v.mkSortedMap(2, 4, 6).ceilingKey(6).intValue());
                assertEquals (6, v.mkSortedMap(2, 4, 6).ceilingKey(7).intValue());
            }
        });
    }

    @Test default void testFirstEntry() {
        doTest(v -> {
            assertNull (v.mkSortedMap().firstEntry());
            assertEquals (entryOf(1), v.mkSortedMap(1).firstEntry());
            if (v.isAscending()) {
                assertEquals(entryOf(1), v.mkSortedMap(1, 2, 3).firstEntry());
            }
            else {
                assertEquals(entryOf(3), v.mkSortedMap(1, 2, 3).firstEntry());
            }
        });
    }
    @Test default void testLastEntry() {
        doTest(v -> {
            assertNull (v.mkSortedMap().lastEntry());
            assertEquals (entryOf(1), v.mkSortedMap(1).lastEntry());
            if (v.isAscending()) {
                assertEquals(entryOf(3), v.mkSortedMap(1, 2, 3).lastEntry());
            }
            else {
                assertEquals(entryOf(1), v.mkSortedMap(1, 2, 3).lastEntry());
            }
        });
    }

    @Test default void testPollFirstEntry() {
        doTest(v -> {
            if (isImmutable()) {
                assertThrows(UnsupportedOperationException.class, () -> v.mkSortedMap().pollFirstEntry());
                assertThrows(UnsupportedOperationException.class, () -> v.mkSortedMap(1).pollFirstEntry());
                assertThrows(UnsupportedOperationException.class, () -> v.mkSortedMap(1, 2, 3).pollFirstEntry());
            }
            else {
                //TODO mutable
            }
        });
    }
    @Test default void testPollLastEntry() {
        doTest(v -> {
            if (isImmutable()) {
                assertThrows(UnsupportedOperationException.class, () -> v.mkSortedMap().pollLastEntry());
                assertThrows(UnsupportedOperationException.class, () -> v.mkSortedMap(1).pollLastEntry());
                assertThrows(UnsupportedOperationException.class, () -> v.mkSortedMap(1, 2, 3).pollLastEntry());
            }
            else {
                //TODO mutable
            }
        });
    }

    @Test default void testFirstKey() {
        doTest(v -> {
            assertThrows (NoSuchElementException.class, () -> v.mkSortedMap().firstKey());
            assertEquals (1, v.mkSortedMap(1).firstKey().intValue());
            if (v.isAscending()) {
                assertEquals(1, v.mkSortedMap(1, 2, 3).firstKey().intValue());
            }
            else {
                assertEquals(3, v.mkSortedMap(1, 2, 3).firstKey().intValue());
            }
        });
    }

    @Test default void testLastKey() {
        doTest(v -> {
            assertThrows (NoSuchElementException.class, () -> v.mkSortedMap().lastKey());
            assertEquals (1, v.mkSortedMap(1).lastKey().intValue());
            if (v.isAscending()) {
                assertEquals(3, v.mkSortedMap(1, 2, 3).lastKey().intValue());
            }
            else {
                assertEquals(1, v.mkSortedMap(1, 2, 3).lastKey().intValue());
            }
        });
    }

    @Test default void testDescendingMap() {
        doTest(v -> {
            assertTrue(v.mkSortedMap().descendingMap().isEmpty());
            assertEquals(v.mkSortedMap().comparator().compare(1, 2), -v.mkSortedMap().descendingMap().comparator().compare(1, 2));
            assertEquals(v.mkSortedMap().comparator().compare(1, 2), v.mkSortedMap().descendingMap().descendingMap().comparator().compare(1, 2));

            assertEquals(v.mkSortedMap(1), v.mkSortedMap(1).descendingMap());
            assertEquals(v.mkSortedMap(1).comparator().compare(1, 2), -v.mkSortedMap(1).descendingMap().comparator().compare(1, 2));
            assertEquals(v.mkSortedMap(1).comparator().compare(1, 2), v.mkSortedMap(1).descendingMap().descendingMap().comparator().compare(1, 2));

            assertEquals(v.iterationOrder123().reverse(), v.mkSortedMap(1, 2, 3).descendingMap().toVector());
            assertEquals(v.mkSortedMap(1, 2, 3).comparator().compare(1, 2), -v.mkSortedMap(1, 2, 3).descendingMap().comparator().compare(1, 2));
            assertEquals(v.mkSortedMap(1, 2, 3).comparator().compare(1, 2), v.mkSortedMap(1, 2, 3).descendingMap().descendingMap().comparator().compare(1, 2));
        });
    }

    @Test default void testNavigableKeySet() {
        doTest(v -> {
            assertTrue(v.mkSortedMap().navigableKeySet().isEmpty());
            assertEquals(v.mkSortedMap().comparator(), v.mkSortedMap().navigableKeySet().comparator());

            assertEquals(ASortedSet.of(1), v.mkSortedMap(1).navigableKeySet());
            assertEquals(v.mkSortedMap(1).comparator(), v.mkSortedMap(1).navigableKeySet().comparator());

            assertEquals(v.iterationOrder123().map(Map.Entry::getKey), v.mkSortedMap(1, 2, 3).navigableKeySet().toVector());
            assertEquals(v.mkSortedMap(1, 2, 3).comparator(), v.mkSortedMap(1, 2, 3).navigableKeySet().comparator());
        });
    }

    @Test default void testDescendingKeySet() {
        doTest(v -> {
            assertTrue(v.mkSortedMap().descendingKeySet().isEmpty());
            assertEquals(v.mkSortedMap().comparator().compare(1, 2), -v.mkSortedMap().descendingKeySet().comparator().compare(1, 2));

            assertEquals(ASortedSet.of(1), v.mkSortedMap(1).descendingKeySet());
            assertEquals(v.mkSortedMap(1).comparator().compare(1, 2), -v.mkSortedMap(1).descendingKeySet().comparator().compare(1, 2));

            assertEquals(v.iterationOrder123().map(Map.Entry::getKey).reverse(), v.mkSortedMap(1, 2, 3).descendingKeySet().toVector());
            assertEquals(v.mkSortedMap(1, 2, 3).comparator().compare(1, 2), -v.mkSortedMap(1, 2, 3).descendingKeySet().comparator().compare(1, 2));
        });
    }

    @Test default void testSubMapWithFlags() {
        doTest(v -> {
            assertEquals (AVector.of(entryOf(4)), v.mkSortedMap(2, 4, 6).subMap(4, true, 4, true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).subMap(4, true, 4, false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).subMap(4, false, 4, true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).subMap(4, false, 4, false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).subMap(3, true, 3, true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).subMap(3, true, 3, false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).subMap(3, false, 3, true).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).subMap(3, false, 3, false).toVector());

            if (v.isAscending()) {
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).subMap(2, true, 6, true).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4)),               v.mkSortedMap(2, 4, 6).subMap(2, true, 6, false).toVector());
                assertEquals (AVector.of(entryOf(4), entryOf(6)),               v.mkSortedMap(2, 4, 6).subMap(2, false, 6, true).toVector());
                assertEquals (AVector.of(entryOf(4)),                             v.mkSortedMap(2, 4, 6).subMap(2, false, 6, false).toVector());

                assertThrows(IllegalArgumentException.class, () -> v.mkSortedMap(2, 4, 6).subMap(5, true, 3, false));
            }
            else {
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).subMap(6, true, 2, true).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4)),               v.mkSortedMap(2, 4, 6).subMap(6, true, 2, false).toVector());
                assertEquals (AVector.of(entryOf(4), entryOf(2)),               v.mkSortedMap(2, 4, 6).subMap(6, false, 2, true).toVector());
                assertEquals (AVector.of(entryOf(4)),                             v.mkSortedMap(2, 4, 6).subMap(6, false, 2, false).toVector());

                assertThrows(IllegalArgumentException.class, () -> v.mkSortedMap(2, 4, 6).subMap(3, true, 5, false));
            }
        });
    }

    @Test default void testHeadMapWithFlags() {
        doTest(v -> {
            assertTrue (v.mkSortedMap().headMap(1, true).toVector().isEmpty());
            assertTrue (v.mkSortedMap().headMap(1, false).toVector().isEmpty());

            if (v.isAscending()) {
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).headMap(7, true).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).headMap(7, false).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).headMap(6, true).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4)),               v.mkSortedMap(2, 4, 6).headMap(6, false).toVector());
                assertEquals (AVector.of(entryOf(2)),                             v.mkSortedMap(2, 4, 6).headMap(2, true).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).headMap(2, false).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).headMap(1, true).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).headMap(1, false).toVector());
            }
            else {
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).headMap(1, true).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).headMap(1, false).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).headMap(2, true).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4)),               v.mkSortedMap(2, 4, 6).headMap(2, false).toVector());
                assertEquals (AVector.of(entryOf(6)),                             v.mkSortedMap(2, 4, 6).headMap(6, true).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).headMap(6, false).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).headMap(7, true).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).headMap(7, false).toVector());
            }
        });
    }

    @Test default void testTailMapWithFlags() {
        doTest(v -> {
            assertTrue (v.mkSortedMap().tailMap(1, true).toVector().isEmpty());
            assertTrue (v.mkSortedMap().tailMap(1, false).toVector().isEmpty());

            if (v.isAscending()) {
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).tailMap(1, true).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).tailMap(1, false).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).tailMap(2, true).toVector());
                assertEquals (AVector.of(entryOf(4), entryOf(6)),               v.mkSortedMap(2, 4, 6).tailMap(2, false).toVector());
                assertEquals (AVector.of(entryOf(6)),                             v.mkSortedMap(2, 4, 6).tailMap(6, true).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).tailMap(6, false).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).tailMap(7, true).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).tailMap(7, false).toVector());
            }
            else {
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).tailMap(7, true).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).tailMap(7, false).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).tailMap(6, true).toVector());
                assertEquals (AVector.of(entryOf(4), entryOf(2)),               v.mkSortedMap(2, 4, 6).tailMap(6, false).toVector());
                assertEquals (AVector.of(entryOf(2)),                             v.mkSortedMap(2, 4, 6).tailMap(2, true).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).tailMap(2, false).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).tailMap(1, true).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).tailMap(1, false).toVector());
            }
        });
    }

    @Test default void testSubMapWithoutFlags() {
        doTest(v -> {
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).subMap(4, true, 4, false).toVector());
            assertEquals (AVector.of(),  v.mkSortedMap(2, 4, 6).subMap(3, true, 3, false).toVector());

            if (v.isAscending()) {
                assertEquals (AVector.of(entryOf(2), entryOf(4)),               v.mkSortedMap(2, 4, 6).subMap(2, true, 6, false).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).subMap(2, true, 7, false).toVector());

                assertThrows(IllegalArgumentException.class, () -> v.mkSortedMap(2, 4, 6).subMap(5, 3));
            }
            else {
                assertEquals (AVector.of(entryOf(6), entryOf(4)),               v.mkSortedMap(2, 4, 6).subMap(6, true, 2, false).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).subMap(6, true, 1, false).toVector());

                assertThrows(IllegalArgumentException.class, () -> v.mkSortedMap(2, 4, 6).subMap(3, 5));
            }
        });
    }

    @Test default void testHeadMapWithoutFlags() {
        doTest(v -> {
            assertTrue (v.mkSortedMap().headMap(1, true).toVector().isEmpty());
            assertTrue (v.mkSortedMap().headMap(1, false).toVector().isEmpty());

            if (v.isAscending()) {
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).headMap(7).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4)),               v.mkSortedMap(2, 4, 6).headMap(6).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).headMap(2).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).headMap(1).toVector());
            }
            else {
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).headMap(1).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4)),               v.mkSortedMap(2, 4, 6).headMap(2).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).headMap(6).toVector());
                assertEquals (AVector.of(),                                         v.mkSortedMap(2, 4, 6).headMap(7).toVector());
            }
        });
    }

    @Test default void testTailMapWithoutFlags() {
        doTest(v -> {
            assertTrue (v.mkSortedMap().tailMap(1).toVector().isEmpty());

            if (v.isAscending()) {
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).tailMap(1).toVector());
                assertEquals (AVector.of(entryOf(2), entryOf(4), entryOf(6)), v.mkSortedMap(2, 4, 6).tailMap(2).toVector());
                assertEquals (AVector.of(entryOf(6)), v.mkSortedMap(2, 4, 6).tailMap(6).toVector());
                assertEquals (AVector.of(), v.mkSortedMap(2, 4, 6).tailMap(7).toVector());
            }
            else {
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).tailMap(7).toVector());
                assertEquals (AVector.of(entryOf(6), entryOf(4), entryOf(2)), v.mkSortedMap(2, 4, 6).tailMap(6).toVector());
                assertEquals (AVector.of(entryOf(2)), v.mkSortedMap(2, 4, 6).tailMap(2).toVector());
                assertEquals (AVector.of(), v.mkSortedMap(2, 4, 6).tailMap(1).toVector());
            }
        });
    }
}
