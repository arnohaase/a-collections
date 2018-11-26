package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.*;
import com.ajjpj.acollections.mutable.AMutableListWrapper;
import com.ajjpj.acollections.util.AOption;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;


public interface AEntryCollectionOpsTests extends ACollectionOpsTests {
    // These methods must be implemented by concrete test classes, customizing the tests per tested collection class

    default boolean isSorted () { return false; }
    default boolean isImmutable () {return true; }
    Iterable<Variant> variants ();

    //---------------------------- the tests ------------------------------

    static Map.Entry<Integer,Integer> entryOf(int i) {
        return new AbstractMap.SimpleImmutableEntry<>(i, 2*i+1);
    }

    default boolean isEven (Map.Entry<Integer,Integer> e) { return e.getKey()%2 == 0; }
    default boolean isOdd (Map.Entry<Integer,Integer> e) { return e.getKey()%2 == 1; }
    default int doubled (Map.Entry<Integer,Integer> e) { return 2*e.getKey(); }
    default Map.Entry<Integer,Integer> doubledEntry (Map.Entry<Integer,Integer> e) { return entryOf(2*e.getKey()); }
    default Map.Entry<Integer,Integer> sum (Map.Entry<Integer,Integer> a, Map.Entry<Integer,Integer> b) { return entryOf(a.getKey()+b.getKey()); }

    @SuppressWarnings({"SimplifiableJUnitAssertion", "EqualsBetweenInconvertibleTypes", "ArraysAsListWithZeroOrOneArgument"})
    @Test default void testEquals() {
        // default implementation for sets
        doTest(v -> {
            assertTrue(new HashSet<>().equals(v.mkColl()));
            assertTrue(v.mkColl().equals(new HashSet<>()));
            assertFalse(v.mkColl().equals(new HashSet<>(Arrays.asList(entryOf(1)))));
            assertFalse(v.mkColl().equals(new ArrayList<>()));
            assertFalse(v.mkColl().equals(AVector.empty()));

            assertTrue(v.mkColl(1).equals(new HashSet<>(Arrays.asList(entryOf(1)))));
            assertFalse(v.mkColl(1).equals(new HashSet<>(Arrays.asList(entryOf(1), entryOf(2)))));
            assertFalse(v.mkColl(1).equals(new HashSet<>()));

            assertTrue(v.mkColl(1, 2, 3).equals(new HashSet<>(Arrays.asList(entryOf(1), entryOf(2), entryOf(3)))));
            assertFalse(v.mkColl(1, 2, 3).equals(new HashSet<>(Arrays.asList(entryOf(1), entryOf(2), entryOf(4)))));
            assertFalse(v.mkColl(1, 2, 3).equals(new HashSet<>(Arrays.asList(entryOf(1), entryOf(2), entryOf(3), entryOf(4)))));
            assertFalse(v.mkColl(1, 2, 3).equals(new HashSet<>(Arrays.asList(entryOf(1), entryOf(2)))));
        });
    }
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @Test default void testHashCode() {
        // default implementation for sets
        doTest(v -> {
            assertEquals(new HashSet<>().hashCode(), v.mkColl().hashCode());
            assertEquals(new HashSet<>(Arrays.asList(entryOf(1))).hashCode(), v.mkColl(1).hashCode());
            assertEquals(new HashSet<>(Arrays.asList(entryOf(1), entryOf(2), entryOf(3))).hashCode(), v.mkColl(1, 2, 3).hashCode());
        });
    }

    @Test @Override default void testIterator () {
        doTest(v -> {
            assertTrue(! v.mkColl().iterator().hasNext());
            assertEquals(AVector.of(entryOf(1)), v.mkColl(1).iterator().toVector());

            if (v.iterationOrder123() != null)
                assertEquals(v.iterationOrder123(), v.mkColl(1, 2, 3).iterator().toVector());
            else
                assertEquals(v.mkColl(1, 2, 3).toSet(), v.mkColl(1, 2, 3).iterator().toSet());
        });
    }

    void testSerDeser();

    //TODO iterator --> separate tests

    @Test @Override default void testSize () {
        doTest(v -> {
            assertEquals(0, v.mkColl().size());
            assertEquals(1, v.mkColl(1).size());
            assertEquals(3, v.mkColl(1, 2, 3).size());
        });
    }

    @Test @Override default void testIsEmpty () {
        doTest(v -> {
            assertTrue(v.mkColl().isEmpty());
            assertFalse(v.mkColl(1).isEmpty());
            assertFalse(v.mkColl(0).isEmpty());
            assertFalse(v.mkColl(1, 2, 3).isEmpty());
        });
    }
    @Test @Override default void testNonEmpty () {
        doTest(v -> {
            assertFalse(v.mkColl().nonEmpty());
            assertTrue(v.mkColl(1).nonEmpty());
            assertTrue(v.mkColl(0).nonEmpty());
            assertTrue(v.mkColl(1, 2, 3).nonEmpty());
        });
    }

    @Test @Override default void testHead () {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkColl().head());
            assertEquals(entryOf(1), v.mkColl(1).head());
            if (v.iterationOrder123() != null)
                assertEquals(v.mkColl(1, 2, 3).head(), v.iterationOrder123().head());
            else {
                assertTrue(AHashSet.of(entryOf(1), entryOf(2), entryOf(3)).contains(v.mkColl(1, 2, 3).head()));
            }
        });
    }
    @Test @Override default void testHeadOption () {
        doTest(v -> {
            assertTrue(v.mkColl().headOption().isEmpty());
            assertTrue(v.mkColl(1).headOption().contains(entryOf(1)));
            if (v.iterationOrder123() != null)
                assertTrue(v.mkColl(1, 2, 3).headOption().contains(v.iterationOrder123().head()));
            else {
                assertTrue(v.mkColl(1, 2, 3).toSet().contains(v.mkColl(1, 2, 3).headOption().get()));
            }
        });
    }
    @Test @Override default void testFirst () {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkColl().first());
            assertEquals(entryOf(1), v.mkColl(1).first());
            if (v.iterationOrder123() != null)
                assertEquals(v.mkColl(1, 2, 3).first(), v.iterationOrder123().first());
            else {
                assertTrue(AHashSet.of(entryOf(1), entryOf(2), entryOf(3)).contains(v.mkColl(1, 2, 3).first()));
            }
        });
    }
    @Test @Override default void testFirstOption () {
        doTest(v -> {
            assertTrue(v.mkColl().firstOption().isEmpty());
            assertTrue(v.mkColl(1).firstOption().contains(entryOf(1)));
            if (v.iterationOrder123() != null)
                assertTrue(v.mkColl(1, 2, 3).firstOption().contains(v.iterationOrder123().head()));
            else {
                assertTrue(v.mkColl(1, 2, 3).toSet().contains(v.mkColl(1, 2, 3).firstOption().get()));
            }
        });
    }

    @Test @Override default void testToLinkedList () {
        doTest(v -> {
            assertEquals(ALinkedList.empty(), v.mkColl().toLinkedList());
            assertEquals(ALinkedList.of(entryOf(1)), v.mkColl(1).toLinkedList());
            if (v.iterationOrder123() != null)
                assertEquals(v.iterationOrder123(), v.mkColl(1, 2, 3).toLinkedList());
            else
                assertEquals(AHashSet.of(entryOf(1), entryOf(2), entryOf(3)), v.mkColl(1, 2, 3).toLinkedList().toSet());
        });
    }
    @Test @Override default void testToVector () {
        doTest(v -> {
            assertEquals(AVector.empty(), v.mkColl().toVector());
            assertEquals(AVector.of(entryOf(1)), v.mkColl(1).toVector());
            if (v.iterationOrder123() != null)
                assertEquals(v.iterationOrder123(), v.mkColl(1, 2, 3).toVector());
            else
                assertEquals(AHashSet.of(entryOf(1), entryOf(2), entryOf(3)), v.mkColl(1, 2, 3).toVector().toSet());
        });
    }

    @Test @Override default void testToSet () {
        doTest(v -> {
            assertTrue(v.mkColl().toSet().isEmpty());
            assertEquals(AHashSet.of(entryOf(1)), v.mkColl(1).toSet());
            assertEquals(AHashSet.of(entryOf(1), entryOf(2), entryOf(3), entryOf(4)), v.mkColl(1, 2, 3, 4).toSet());
        });
    }
    @Test @Override default void testToSortedSet () {
        doTest(v -> {
            assertThrows(UnsupportedOperationException.class, () -> v.mkColl().toSortedSet());
            assertThrows(UnsupportedOperationException.class, () -> v.mkColl(1).toSortedSet());
            assertThrows(UnsupportedOperationException.class, () -> v.mkColl(1, 2).toSortedSet());
        });
    }

    @Test @Override default void testToMutableList() {
        doTest(v -> {
            assertEquals(AMutableListWrapper.empty(), v.mkColl().toMutableList());
            assertEquals(AMutableListWrapper.of(entryOf(1)), v.mkColl(1).toMutableList());
            if (v.iterationOrder123() != null)
                assertEquals(v.iterationOrder123(), v.mkColl(1, 2, 3).toMutableList());
            else
                assertEquals(AHashSet.of(entryOf(1), entryOf(2), entryOf(3)), v.mkColl(1, 2, 3).toMutableList().toSet());
        });
    }
    @Test @Override default void testToMutableSet() {
        doTest(v -> {
            assertTrue(v.mkColl().toMutableSet().isEmpty());
            assertEquals(AHashSet.of(entryOf(1)), v.mkColl(1).toMutableSet());
            assertEquals(AHashSet.of(entryOf(1), entryOf(2), entryOf(3), entryOf(4)), v.mkColl(1, 2, 3, 4).toMutableSet());
        });
    }

    @Test @Override default void testMap () {
        doTest(v -> {
            assertTrue(v.mkColl().map(this::doubled).isEmpty());
            assertEquals(AHashSet.of(entryOf(2)), v.mkColl(1).map(this::doubledEntry).toSet());
            assertEquals(AHashSet.of(2, 4, 6), v.mkColl(1, 2, 3).map(this::doubled).toSet());
        });
    }
    @Test @Override default void testFlatMap () {
        doTest(v -> {
            assertTrue(v.mkColl().flatMap(x -> AVector.of(doubled(x), doubled(x)+1)).isEmpty());
            assertEquals(AHashSet.of(2, 3), v.mkColl(1).flatMap(x -> AVector.of(doubled(x), doubled(x)+1)).toSet());
            assertEquals(AHashSet.of(2, 3, 4, 5, 6, 7), v.mkColl(1, 2, 3).flatMap(x -> AVector.of(doubled(x), doubled(x)+1)).toSet());
        });
    }
    @Test @Override default void testCollect () {
        doTest(v -> {
            assertEquals(AHashSet.empty(), v.mkColl().collect(this::isOdd, this::doubledEntry).toSet());
            assertEquals(AHashSet.of(entryOf(2)), v.mkColl(1).collect(this::isOdd, this::doubledEntry).toSet());
            assertEquals(AHashSet.of(2, 6), v.mkColl(1, 2, 3).collect(this::isOdd, this::doubled).toSet());
        });
    }
    @Test @Override default void testCollectFirst () {
        doTest(v -> {
            assertEquals(AOption.none(), v.mkColl().collectFirst(this::isOdd, this::doubled));
            assertEquals(AOption.none(), v.mkColl(2).collectFirst(this::isOdd, this::doubled));
            assertEquals(AOption.some(2), v.mkColl(1).collectFirst(this::isOdd, this::doubled));

            final Map.Entry<Integer,Integer> firstOdd;
            if (v.iterationOrder123() != null)
                firstOdd = v.iterationOrder123().head();
            else {
                final Iterator<Map.Entry<Integer,Integer>> it = v.mkColl(1, 2, 3).iterator();
                if (it.next().equals(entryOf(2))) firstOdd = it.next();
                else firstOdd = v.mkColl(1, 2, 3).iterator().next();
            }
            assertEquals(AOption.some(2*firstOdd.getKey()), v.mkColl(1, 2, 3).collectFirst(this::isOdd, this::doubled));
        });
    }

    @Test @Override default void testFilter () {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().filter(this::isOdd));
            assertEquals(v.mkColl(1), v.mkColl(1).filter(this::isOdd));
            assertEquals(v.mkColl(1, 3), v.mkColl(1, 2, 3).filter(this::isOdd));
        });
    }
    @Test @Override default void testFilterNot () {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().filterNot(this::isEven));
            assertEquals(v.mkColl(1), v.mkColl(1).filterNot(this::isEven));
            assertEquals(v.mkColl(1, 3), v.mkColl(1, 2, 3).filterNot(this::isEven));
        });
    }

    @Test @Override default void testFind () {
        doTest(v -> {
            assertEquals(AOption.none(), v.mkColl().find(this::isEven));
            assertEquals(AOption.none(), v.mkColl(1).find(this::isEven));
            assertEquals(AOption.some(entryOf(1)), v.mkColl(1).find(this::isOdd));
            assertEquals(AOption.some(entryOf(2)), v.mkColl(1, 2, 3).find(this::isEven));
        });
    }

    @Test @Override default void testForall () {
        doTest(v -> {
            assertTrue(v.mkColl().forall(this::isOdd));
            assertTrue(v.mkColl(1).forall(this::isOdd));
            assertFalse(v.mkColl(1).forall(this::isEven));
            assertFalse(v.mkColl(1, 2, 3).forall(this::isOdd));
            assertFalse(v.mkColl(1, 2, 3).forall(this::isEven));
            assertTrue(v.mkColl(2, 4, 6).forall(this::isEven));
        });
    }
    @Test @Override default void testExists () {
        doTest(v -> {
            assertFalse(v.mkColl().exists(this::isOdd));
            assertTrue(v.mkColl(1).exists(this::isOdd));
            assertFalse(v.mkColl(1).exists(this::isEven));
            assertTrue(v.mkColl(1, 2, 3).exists(this::isOdd));
            assertTrue(v.mkColl(1, 2, 3).exists(this::isEven));
            assertFalse(v.mkColl(2, 4, 6).exists(this::isOdd));
            assertTrue(v.mkColl(2, 4, 6).exists(this::isEven));
        });
    }
    @Test @Override default void testCount () {
        doTest(v -> {
            assertEquals(0, v.mkColl().count(this::isOdd));
            assertEquals(1, v.mkColl(1).count(this::isOdd));
            assertEquals(0, v.mkColl(1).count(this::isEven));
            assertEquals(2, v.mkColl(1, 2, 3).count(this::isOdd));
            assertEquals(1, v.mkColl(1, 2, 3).count(this::isEven));
            assertEquals(0, v.mkColl(2, 4, 6).count(this::isOdd));
            assertEquals(3, v.mkColl(2, 4, 6).count(this::isEven));
        });
    }
    @Test @Override default void testContains () {
        doTest(v -> {
            assertFalse(v.mkColl().contains(entryOf(1)));
            assertTrue(v.mkColl(1).contains(entryOf(1)));
            assertFalse(v.mkColl(1).contains(entryOf(2)));
            assertTrue(v.mkColl(1, 2, 3).contains(entryOf(1)));
            assertTrue(v.mkColl(1, 2, 3).contains(entryOf(2)));
            assertTrue(v.mkColl(1, 2, 3).contains(entryOf(3)));
            assertFalse(v.mkColl(1, 2, 3).contains(entryOf(4)));
        });
    }

    @Test @Override default void testReduce () {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkColl().reduce(this::sum));
            assertEquals(entryOf(1), v.mkColl(1).reduce(this::sum));
            assertEquals(entryOf(6), v.mkColl(1, 2, 3).reduce(this::sum));

            if (v.iterationOrder123() != null) {
                final List<Map.Entry<Integer,Integer>> trace = new ArrayList<>();
                v.mkColl(1, 2, 3).reduce((a, b) -> {
                    trace.add(a);
                    trace.add(b);
                    return entryOf(0);
                });
                assertEquals(Arrays.asList(v.iterationOrder123().get(0), v.iterationOrder123().get(1), entryOf(0), v.iterationOrder123().get(2)), trace);
            }
        });
    }
    @Test @Override default void testReduceOption () {
        doTest(v -> {
            assertEquals(AOption.none(), v.mkColl().reduceOption(this::sum));
            assertEquals(AOption.some(entryOf(1)), v.mkColl(1).reduceOption(this::sum));
            assertEquals(AOption.some(entryOf(6)), v.mkColl(1, 2, 3).reduceOption(this::sum));

            if (v.iterationOrder123() != null) {
                final List<Map.Entry<Integer,Integer>> trace = new ArrayList<>();
                v.mkColl(1, 2, 3).reduceOption((a, b) -> {
                    trace.add(a);
                    trace.add(b);
                    return entryOf(0);
                });
                assertEquals(Arrays.asList(v.iterationOrder123().get(0), v.iterationOrder123().get(1), entryOf(0), v.iterationOrder123().get(2)), trace);
            }
        });
    }
    @Test @Override default void testReduceLeft () {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkColl().reduceLeft(this::sum));
            assertEquals(entryOf(1), v.mkColl(1).reduceLeft(this::sum));
            assertEquals(entryOf(6), v.mkColl(1, 2, 3).reduceLeft(this::sum));

            if (v.iterationOrder123() != null) {
                final List<Map.Entry<Integer,Integer>> trace = new ArrayList<>();
                v.mkColl(1, 2, 3).reduceLeft((a, b) -> {
                    trace.add(a);
                    trace.add(b);
                    return entryOf(0);
                });
                assertEquals(Arrays.asList(v.iterationOrder123().get(0), v.iterationOrder123().get(1), entryOf(0), v.iterationOrder123().get(2)), trace);
            }
        });
    }
    @Test @Override default void testReduceLeftOption () {
        doTest(v -> {
            assertEquals(AOption.none(), v.mkColl().reduceLeftOption(this::sum));
            assertEquals(AOption.some(entryOf(1)), v.mkColl(1).reduceLeftOption(this::sum));
            assertEquals(AOption.some(entryOf(6)), v.mkColl(1, 2, 3).reduceLeftOption(this::sum));

            if (v.iterationOrder123() != null) {
                final List<Map.Entry<Integer,Integer>> trace = new ArrayList<>();
                v.mkColl(1, 2, 3).reduceLeftOption((a, b) -> {
                    trace.add(a);
                    trace.add(b);
                    return entryOf(0);
                });
                assertEquals(Arrays.asList(v.iterationOrder123().get(0), v.iterationOrder123().get(1), entryOf(0), v.iterationOrder123().get(2)), trace);
            }
        });
    }

    @Test @Override default void testFold () {
        doTest(v -> {
            assertEquals(entryOf(0), v.mkColl().fold(entryOf(0), this::sum));
            assertEquals(entryOf(1), v.mkColl(1).fold(entryOf(0), this::sum));
            assertEquals(entryOf(6), v.mkColl(1, 2, 3).fold(entryOf(0), this::sum));

            if (v.iterationOrder123() != null) {
                final List<Map.Entry<Integer,Integer>> trace = new ArrayList<>();
                v.mkColl(1, 2, 3).fold(0, (a, b) -> {
                    trace.add(b);
                    return 0;
                });
                assertEquals(Arrays.asList(v.iterationOrder123().get(0), v.iterationOrder123().get(1), v.iterationOrder123().get(2)), trace);
            }
        });
    }
    @Test @Override default void testFoldLeft () {
        doTest(v -> {
            assertEquals(entryOf(0), v.mkColl().foldLeft(entryOf(0), this::sum));
            assertEquals(entryOf(1), v.mkColl(1).foldLeft(entryOf(0), this::sum));
            assertEquals(entryOf(6), v.mkColl(1, 2, 3).foldLeft(entryOf(0), this::sum));

            if (v.iterationOrder123() != null) {
                final List<Map.Entry<Integer,Integer>> trace = new ArrayList<>();
                v.mkColl(1, 2, 3).foldLeft(0, (a, b) -> {
                    trace.add(b);
                    return 0;
                });
                assertEquals(Arrays.asList(v.iterationOrder123().get(0), v.iterationOrder123().get(1), v.iterationOrder123().get(2)), trace);
            }
        });
    }

    @Test @Override default void testGroupBy () {
        doTest(v -> {
            assertTrue(v.mkColl().groupBy(this::isOdd).isEmpty());
            assertEquals(AHashMap.empty().plus(true, v.mkColl(1, 3)).plus(false, v.mkColl(2)),
                    v.mkColl(1, 2, 3).groupBy(this::isOdd));
        });
    }

    @Test @Override default void testMin () {
        doTest(v -> {
            assertThrows(UnsupportedOperationException.class, () -> v.mkColl().min());
            assertThrows(UnsupportedOperationException.class, () -> v.mkColl(1).min());
            assertThrows(UnsupportedOperationException.class, () -> v.mkColl(1, 2).min());

            assertThrows(NoSuchElementException.class, () -> v.mkColl().min(keyComparator));
            assertEquals(entryOf(1), v.mkColl(1).min(keyComparator));
            assertEquals(entryOf(1), v.mkColl(2, 1, 3).min(keyComparator));
        });
    }
    @Test @Override default void testMax () {
        doTest(v -> {
            assertThrows(UnsupportedOperationException.class, () -> v.mkColl().max());
            assertThrows(UnsupportedOperationException.class, () -> v.mkColl(1).max());
            assertThrows(UnsupportedOperationException.class, () -> v.mkColl(1, 2).max());

            assertThrows(NoSuchElementException.class, () -> v.mkColl().max(keyComparator));
            assertEquals(entryOf(1), v.mkColl(1).max(keyComparator));
            assertEquals(entryOf(3), v.mkColl(2, 3, 1).max(keyComparator));
        });
    }

    @Test @Override default void testMkString () {
        doTest(v -> {
            assertEquals("", v.mkColl().mkString("|"));
            assertEquals("$%", v.mkColl().mkString("$", "|", "%"));
            assertEquals("1=3", v.mkColl(1).mkString("|"));
            assertEquals("$1=3%", v.mkColl(1).mkString("$", "|", "%"));

            if (v.iterationOrder123() != null) {
                assertEquals(v.iterationOrder123().mkString("|"), v.mkColl(1, 2, 3).mkString("|"));
                assertEquals(v.iterationOrder123().mkString("$", "|", "%"), v.mkColl(1, 2, 3).mkString("$", "|", "%"));
            }
        });
    }

    //---------------------------- internal -------------------------------

    Comparator<Map.Entry<Integer,Integer>> keyComparator = Map.Entry.comparingByKey();

    default void doTest (Consumer<Variant> test) {
        variants().forEach(test);
    }

    class Variant {
        private final Supplier<ACollectionBuilder<Map.Entry<Integer,Integer>, ? extends ACollectionOps<Map.Entry<Integer,Integer>>>> builderFactory;
        private final AVector<Integer> iterationOrder123;

        public Variant (Supplier<ACollectionBuilder<Map.Entry<Integer,Integer>, ? extends ACollectionOps<Map.Entry<Integer,Integer>>>> builderFactory, AVector<Integer> iterationOrder123) {
            this.builderFactory = builderFactory;
            this.iterationOrder123 = iterationOrder123;
        }

        ACollectionBuilder<Map.Entry<Integer,Integer>, ? extends ACollectionOps<Map.Entry<Integer,Integer>>> newBuilder() {
            return builderFactory.get();
        }

        public ACollectionOps<Map.Entry<Integer,Integer>> mkColl(Integer... values) {
            final ACollectionBuilder<Map.Entry<Integer,Integer>, ? extends ACollectionOps<Map.Entry<Integer,Integer>>> builder = newBuilder();
            for(Integer v: values) builder.add(entryOf(v));
            return builder.build();
        }

        public AMap<Integer,Integer> mkMap(Integer... values) {
            return (AMap<Integer,Integer>) mkColl(values);
        }

        AVector<Map.Entry<Integer,Integer>> iterationOrder123() {
            return this.iterationOrder123 != null ? iterationOrder123.map(AEntryCollectionOpsTests::entryOf) : null;
        }
    }
}
