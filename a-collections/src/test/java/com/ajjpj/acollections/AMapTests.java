package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AHashMap;
import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.ATreeMap;
import com.ajjpj.acollections.jackson.ACollectionsModule;
import com.ajjpj.acollections.jackson.JacksonModuleTest;
import com.ajjpj.acollections.util.AUnchecker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.*;

import static com.ajjpj.acollections.AEntryCollectionOpsTests.entryOf;
import static org.junit.jupiter.api.Assertions.*;


public interface AMapTests extends AEntryCollectionOpsTests {
    @SuppressWarnings({"SimplifiableJUnitAssertion", "EqualsBetweenInconvertibleTypes"})
    @Test default void testEquals() {
        final ATreeMap<Integer,Integer> emptyTree = ATreeMap.<Integer,Integer>empty(Comparator.naturalOrder());
        final ATreeMap<Integer,Integer> tree1 = emptyTree.plus(entryOf(1));
        final ATreeMap<Integer,Integer> tree123 = tree1.plus(entryOf(2)).plus(entryOf(3));

        final AHashMap<Integer,Integer> emptyHash = AHashMap.empty();
        final AHashMap<Integer,Integer> hash1 = emptyHash.plus(entryOf(1));
        final AHashMap<Integer,Integer> hash123 = hash1.plus(entryOf(2)).plus(entryOf(3));

        doTest(v -> {
            assertTrue(v.mkMap().equals(AHashMap.empty()));
            assertTrue(v.mkMap().equals(emptyTree));
            assertTrue(v.mkMap().equals(new HashMap<>()));
            assertTrue(v.mkMap().equals(new TreeMap<>()));
            assertFalse(v.mkMap().equals(AHashMap.empty().plus(0, 0)));
            assertFalse(v.mkMap().equals(emptyTree.plus(0, 0)));
            assertFalse(v.mkMap().equals(new HashMap<>(AHashMap.empty().plus(1, 2))));
            assertFalse(v.mkMap().equals(new TreeMap<>(AHashMap.empty().plus(2, 3))));

            assertTrue(AHashMap.empty().equals(v.mkMap()));
            assertTrue(emptyTree.equals(v.mkMap()));
            assertTrue(new HashMap<>().equals(v.mkMap()));
            assertTrue(new TreeMap<>().equals(v.mkMap()));
            assertFalse(AHashMap.empty().plus(0, 0).equals(v.mkMap()));
            assertFalse(emptyTree.plus(0, 0).equals(v.mkMap()));
            assertFalse(new HashMap<>(AHashMap.empty().plus(1, 2)).equals(v.mkMap()));
            assertFalse(new TreeMap<>(AHashMap.empty().plus(2, 3)).equals(v.mkMap()));

            assertTrue(v.mkMap(1).equals(hash1));
            assertTrue(v.mkMap(1).equals(tree1));
            assertTrue(v.mkMap(1).equals(new HashMap<>(hash1)));
            assertTrue(v.mkMap(1).equals(new TreeMap<>(hash1)));
            assertFalse(v.mkMap(1).equals(AHashMap.empty().plus(1, 0)));
            assertFalse(v.mkMap(1).equals(emptyTree.plus(1, 1)));
            assertFalse(v.mkMap(1).equals(new HashMap<>(AHashMap.empty().plus(1, 2))));
            assertFalse(v.mkMap(1).equals(new TreeMap<>(AHashMap.empty().plus(1, 4))));

            assertFalse(v.mkMap(1).equals(emptyHash.plus(entryOf(1)).plus(entryOf(2))));
            assertFalse(v.mkMap(1).equals(emptyTree.plus(entryOf(1)).plus(entryOf(2))));
            assertFalse(v.mkMap(1).equals(new HashMap<>(emptyHash.plus(entryOf(1)).plus(entryOf(2)))));
            assertFalse(v.mkMap(1).equals(new TreeMap<>(emptyHash.plus(entryOf(1)).plus(entryOf(2)))));

            assertTrue(hash1.equals(v.mkMap(1)));
            assertTrue(tree1.equals(v.mkMap(1)));
            assertTrue(new HashMap<>(hash1).equals(v.mkMap(1)));
            assertTrue(new TreeMap<>(hash1).equals(v.mkMap(1)));
            assertFalse(AHashMap.empty().plus(1, 0).equals(v.mkMap(1)));
            assertFalse(emptyTree.plus(1, 0).equals(v.mkMap(1)));
            assertFalse(new HashMap<>(AHashMap.empty().plus(1, 0)).equals(v.mkMap(1)));
            assertFalse(new TreeMap<>(AHashMap.empty().plus(1, 0)).equals(v.mkMap(1)));

            assertTrue(v.mkMap(1, 2, 3).equals(hash123));
            assertTrue(v.mkMap(1, 2, 3).equals(tree123));
            assertTrue(v.mkMap(1, 2, 3).equals(new HashMap<>(hash123)));
            assertTrue(v.mkMap(1, 2, 3).equals(new TreeMap<>(hash123)));
            assertTrue(hash123.equals (v.mkMap(1, 2, 3)));
            assertTrue(tree123.equals (v.mkMap(1, 2, 3)));
            assertTrue(new HashMap<>(hash123).equals (v.mkMap(1, 2, 3)));
            assertTrue(new TreeMap<>(hash123).equals (v.mkMap(1, 2, 3)));

            assertFalse(v.mkMap(1, 2, 3).equals(hash123.plus(4, 5)));
            assertFalse(v.mkMap(1, 2, 3).equals(hash123.minus(1)));
        });
    }
    @SuppressWarnings({"SimplifiableJUnitAssertion", "EqualsBetweenInconvertibleTypes"})
    @Test default void testEqualsOnlyMaps() {
        doTest(v -> {
            assertFalse(v.mkMap().equals(new HashSet<>()));
            assertFalse(v.mkMap().equals(AHashSet.empty()));

            assertFalse(v.mkMap(1).equals(AHashSet.of(entryOf(1))));
            assertFalse(AHashSet.of(entryOf(1)).equals(v.mkMap(1)));
        });
    }
    @Test default void testHashCode() {
        doTest(v -> {
            assertEquals(new HashMap<>().hashCode(), v.mkMap().hashCode());
            assertEquals(new HashMap<>(v.mkMap(1)).hashCode(), v.mkMap(1).hashCode());
            assertEquals(new HashMap<>(v.mkMap(1, 2, 3)).hashCode(), v.mkMap(1, 2, 3).hashCode());
        });
    }

    @Test default void testFilterKeys() {
        doTest(v -> {
            assertTrue(v.mkMap().filterKeys(x -> true).isEmpty());
            assertTrue(v.mkMap().filterKeys(x -> false).isEmpty());

            assertEquals(v.mkMap(1, 3), v.mkMap(1, 2, 3).filterKeys(x -> x%2==1));
            assertEquals(v.mkMap(2), v.mkMap(1, 2, 3).filterKeys(x -> x%2==0));
        });
    }

    @Test default void testMapValues() {
        doTest(v -> {
            assertTrue (v.mkMap().mapValues(x -> x+1).isEmpty());
            assertEquals (AHashMap.empty().plus(1, 2).plus(2, 4).plus(3, 6), v.mkMap(1, 2, 3).mapValues(x -> x-1));
        });
    }

    @Test default void testContainsKey() {
        doTest(v -> {
            assertFalse(v.mkMap().containsKey(1));

            assertTrue(v.mkMap(1).containsKey(1));

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
            assertNull(v.mkMap().get(1));

            assertEquals(3, v.mkMap(1).get(1).intValue());
            assertNull(v.mkMap(1).get(2));

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

    @Test default void testPlus() {
        doTest(v -> {
            AMap<Integer,Integer> m = v.mkMap();

            m = m.plus(1, 3);
            assertEquals(v.mkMap(1), m);
            m = m.plus(9, 19);
            assertEquals(v.mkMap(1,9), m);
            m = m.plus(2, 5);
            assertEquals(v.mkMap(2, 9, 1), m);

            m = m.plus(2, 99);
            assertEquals(99, m.get(2).intValue());
            assertEquals(3, m.size());
        });
    }
    @Test default void testPlusEntry() {
        doTest(v -> {
            AMap<Integer,Integer> m = v.mkMap();

            m = m.plus(new AbstractMap.SimpleEntry<>(1, 3));
            assertEquals(v.mkMap(1), m);
            m = m.plus(new AbstractMap.SimpleEntry<>(9, 19));
            assertEquals(v.mkMap(1,9), m);
            m = m.plus(new AbstractMap.SimpleEntry<>(2, 5));
            assertEquals(v.mkMap(2, 9, 1), m);

            m = m.plus(new AbstractMap.SimpleEntry<>(2, 99));
            assertEquals(99, m.get(2).intValue());
            assertEquals(3, m.size());
        });
    }
    @Test default void testMinus() {
        doTest(v -> {
            assertTrue(v.mkMap().minus(1).isEmpty());

            assertTrue(v.mkMap(1).minus(1).isEmpty());
            assertEquals(v.mkMap(1), v.mkMap(1).minus(2));

            assertEquals(v.mkMap(1, 3), v.mkMap(1, 2, 3).minus(2));
        });
    }

    @Test default void testPlusAll() {
        doTest(v -> {
            assertTrue(v.mkMap().plusAll(v.mkMap()).isEmpty());
            assertEquals(v.mkMap(1), v.mkMap().plusAll(v.mkMap(1)));
            assertEquals(v.mkMap(1), v.mkMap(1).plusAll(v.mkMap()));
            assertEquals(v.mkMap(1), v.mkMap(1).plusAll(v.mkMap(1)));
            assertEquals(v.mkMap(1, 2), v.mkMap(1).plusAll(v.mkMap(2)));

            // in case of conflict, the second map wins
            final AMap<Integer, Integer> m1 = v.mkMap().plus(1, 1);
            final AMap<Integer, Integer> m2 = v.mkMap().plus(1, 2);
            assertEquals(2, m1.plusAll(m2).get(1).intValue());
        });
    }

    @Test default void testWithDefaultValue() {
        doTest(v -> {
            final AMap<Integer,Integer> m = v.mkMap().withDefaultValue(1);
            assertEquals(1, m.get(9876).intValue());
            assertTrue(m.getOptional(9876).isEmpty());
            assertFalse(m.containsKey(912));
            assertTrue(m.isEmpty());
            assertEquals(0, m.size());
        });
    }
    @Test default void testPlusMinusMaintainWithDefaultValue() {
        doTest(v -> {
            AMap<Integer,Integer> m = v.mkMap(1, 2, 3).withDefaultValue(99);
            assertEquals(3, m.get(1).intValue());
            assertEquals(5, m.get(2).intValue());
            assertEquals(7, m.get(3).intValue());
            assertEquals(99, m.get(4).intValue());

            m = m
                    .minus(1)
                    .minus(2)
                    .plus(4, 19);

            assertEquals(99, m.get(1).intValue());
            assertEquals(99, m.get(2).intValue());
            assertEquals(19, m.get(4).intValue());

            assertEquals(2, m.size());
            assertTrue(m.getOptional(1).isEmpty());
            assertTrue(m.getOptional(4).contains(19));
            assertEquals(99, m.get(5).intValue());
        });
    }
    @Test default void testPlusAllMaintainsWithDefaultValue() {
        doTest(v -> {
            AMap<Integer,Integer> m = v.mkMap(1, 2, 3).withDefaultValue(99);

            m = m.plusAll(AHashMap.<Integer,Integer>empty().plus(1, 2).plus(4, 18));
            assertEquals(4, m.size());

            assertEquals(2, m.get(1).intValue());
            assertEquals(5, m.get(2).intValue());
            assertEquals(18, m.get(4).intValue());

            assertEquals(99, m.get(5).intValue());
        });
    }
    @Test default void testFilterMaintainsWithDefaultValue() {
        doTest(v -> {
            AMap<Integer,Integer> m = v.mkMap(1, 2, 3).withDefaultValue(99);

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
    @Test default void testFilterKeysMaintainsWithDefaultValue() {
        doTest(v -> {
            AMap<Integer,Integer> m = v.mkMap(1, 2, 3).withDefaultValue(99);
            m = m.filterKeys(x -> x%2 == 1);
            assertEquals(2, m.size());

            assertEquals(3, m.get(1).intValue());
            assertEquals(7, m.get(3).intValue());
            assertEquals(99, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }

    @Test default void testWithDerivedDefaultValue() {
        doTest(v -> {
            final AMap<Integer,Integer> m = v.mkMap().withDerivedDefaultValue(x -> 1);
            assertEquals(1, m.get(9876).intValue());
            assertTrue(m.getOptional(9876).isEmpty());
            assertFalse(m.containsKey(912));
            assertTrue(m.isEmpty());
            assertEquals(0, m.size());
        });
    }
    @Test default void testPlusMinusMaintainWithDerivedDefaultValue() {
        doTest(v -> {
            AMap<Integer,Integer> m = v.mkMap(1, 2, 3).withDerivedDefaultValue(x -> 99);
            assertEquals(3, m.get(1).intValue());
            assertEquals(5, m.get(2).intValue());
            assertEquals(7, m.get(3).intValue());
            assertEquals(99, m.get(4).intValue());

            m = m
                    .minus(1)
                    .minus(2)
                    .plus(4, 19);

            assertEquals(99, m.get(1).intValue());
            assertEquals(99, m.get(2).intValue());
            assertEquals(19, m.get(4).intValue());

            assertEquals(2, m.size());
            assertTrue(m.getOptional(1).isEmpty());
            assertTrue(m.getOptional(4).contains(19));
            assertEquals(99, m.get(5).intValue());
        });
    }
    @Test default void testPlusAllMaintainsWithDerivedDefaultValue() {
        doTest(v -> {
            AMap<Integer,Integer> m = v.mkMap(1, 2, 3).withDerivedDefaultValue(x -> 99);

            m = m.plusAll(AHashMap.<Integer,Integer>empty().plus(1, 2).plus(4, 18));
            assertEquals(4, m.size());

            assertEquals(2, m.get(1).intValue());
            assertEquals(5, m.get(2).intValue());
            assertEquals(18, m.get(4).intValue());

            assertEquals(99, m.get(5).intValue());
        });
    }
    @Test default void testFilterMaintainsWithDerivedDefaultValue() {
        doTest(v -> {
            AMap<Integer,Integer> m = v.mkMap(1, 2, 3).withDerivedDefaultValue(x -> 99);

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
    @Test default void testFilterKeysMaintainsWithDerivedDefaultValue() {
        doTest(v -> {
            AMap<Integer,Integer> m = v.mkMap(1, 2, 3).withDerivedDefaultValue(x -> 99);
            m = m.filterKeys(x -> x%2 == 1);
            assertEquals(2, m.size());

            assertEquals(3, m.get(1).intValue());
            assertEquals(7, m.get(3).intValue());
            assertEquals(99, m.get(2).intValue());
            assertEquals(99, m.get(40).intValue());
        });
    }

    @Test default void testWithDefaultValueSerializable() {
        doTest(v -> {
            assertEquals (3, TestHelpers.serDeser(v.mkMap(1, 2, 3).withDefaultValue(99)).get(1).intValue());
            assertEquals (99, TestHelpers.serDeser(v.mkMap(1, 2, 3).withDefaultValue(99)).get(4).intValue());
        });
    }
    @Test default void testWithDerivedDefaultValueSerializable() {
        doTest(v -> {
            assertEquals (3, TestHelpers.serDeser(v.mkMap(1, 2, 3).withDerivedDefaultValue(new TestHelpers.SerializableDoublingFunction())).get(1).intValue());
            assertEquals (8, TestHelpers.serDeser(v.mkMap(1, 2, 3).withDerivedDefaultValue(new TestHelpers.SerializableDoublingFunction())).get(4).intValue());
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

    @Test default void testJacksonToJson() {
        doTest(v -> AUnchecker.executeUncheckedVoid(() -> {
            final ObjectMapper om = new ObjectMapper();
            om.registerModule(new ACollectionsModule());

            assertEquals("{}", om.writeValueAsString(v.mkMap()));
            assertEquals("{\"1\":3}", om.writeValueAsString(v.mkMap(1).map(e -> new AbstractMap.SimpleImmutableEntry<>(e.getKey().toString(), e.getValue())).toMap()));
        }));
    }
    @Test default void testJacksonFromJson() {
        doTest(v -> AUnchecker.executeUncheckedVoid(() -> {
            JacksonModuleTest.testMapFromJson(v.baseClass(), v.baseClass(), v.mkMap(1), v.mkMap(1, 2, 3));
        }));
    }
}
