package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.ALinkedList;
import com.ajjpj.acollections.immutable.ATreeSet;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.util.AOption;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;


public interface ACollectionTests {
    // These methods must be implemented by concrete test classes, customizing the tests per tested collection class

    default boolean isSorted() { return false; }
    default boolean isImmutable() {return true; }
    Iterable<Variant> variants();

    //---------------------------- the tests ------------------------------

    default boolean isEven(int n) { return n%2 == 0; }
    default boolean isOdd(int n) { return n%2 == 1; }
    default int doubled(int n) { return 2*n; }
    default int sum(int a, int b) { return a+b; }

    //TODO equals, hashCode

    @Test default void testMutators() {
        if (isImmutable()) {
            doTest(v -> {
                assertThrows(UnsupportedOperationException.class, () -> v.mkColl().add(1));
                assertThrows(UnsupportedOperationException.class, () -> v.mkColl().remove(1));
                assertThrows(UnsupportedOperationException.class, () -> v.mkColl().addAll(Arrays.asList(1, 2, 3)));
                assertThrows(UnsupportedOperationException.class, () -> v.mkColl().removeAll(Arrays.asList(1, 2, 3)));
                assertThrows(UnsupportedOperationException.class, () -> v.mkColl().retainAll(Arrays.asList(1, 2, 3)));
                assertThrows(UnsupportedOperationException.class, () -> v.mkColl().clear());
            });
        }
    }

    @Test default void testAEquality() {
        doTest(v -> {
            v.checkEquality(v.mkColl());
            v.checkEquality(v.mkColl(1, 2, 3));
        });
    }

    @Test default void testIterator() {
        doTest(v -> {
            assertTrue(! v.mkColl().iterator().hasNext());
            assertEquals(AVector.of(1), v.mkColl(1).iterator().toVector());

            if (v.iterationOrder123 != null)
                assertEquals(v.iterationOrder123, v.mkColl(1, 2, 3).iterator().toVector());
            else
                assertEquals(v.mkColl(1, 2, 3).toSet(), v.mkColl(1, 2, 3).iterator().toSet());
        });
    }

    //TODO iterator --> separate tests

    @Test default void testToArray() {
        doTest(v -> {
            assertEquals(0, v.mkColl().toArray().length);
            assertEquals(Object.class, v.mkColl().toArray().getClass().getComponentType());
            assertEquals(0, v.mkColl().toArray(new Integer[0]).length);
            assertEquals(Integer.class, v.mkColl().toArray(new Integer[0]).getClass().getComponentType());

            assertArrayEquals(new Object[]{1}, v.mkColl(1).toArray());
            assertEquals(Object.class, v.mkColl(1).toArray().getClass().getComponentType());
            assertArrayEquals(new Object[]{1}, v.mkColl(1).toArray(new Integer[0]));
            assertEquals(Integer.class, v.mkColl(1).toArray(new Integer[0]).getClass().getComponentType());

            {
                final Integer[] arr = new Integer[1];
                assertSame(arr, v.mkColl(1).toArray(arr));
                assertEquals(1, arr[0].intValue());
            }
            {
                final Integer[] arr = new Integer[2];
                arr[1] = 99;

                assertSame(arr, v.mkColl(1).toArray(arr));
                assertEquals(1, arr[0].intValue());
                assertNull(arr[1]);
            }
        });
    }

    @Test default void testSize() {
        doTest(v -> {
            assertEquals(0, v.mkColl().size());
            assertEquals(1, v.mkColl(1).size());
            assertEquals(3, v.mkColl(1, 2, 3).size());
        });
    }

    @Test default void testIsEmpty() {
        doTest(v -> {
            assertTrue(v.mkColl().isEmpty());
            assertFalse(v.mkColl(1).isEmpty());
            assertFalse(v.mkColl(0).isEmpty());
            assertFalse(v.mkColl(1, 2, 3).isEmpty());
        });
    }
    @Test default void testNonEmpty() {
        doTest(v -> {
            assertFalse(v.mkColl().nonEmpty());
            assertTrue(v.mkColl(1).nonEmpty());
            assertTrue(v.mkColl(0).nonEmpty());
            assertTrue(v.mkColl(1, 2, 3).nonEmpty());
        });
    }

    @Test default void testHead() {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkColl().head());
            assertEquals(1, v.mkColl(1).head().intValue());
            if (v.iterationOrder123() != null)
                assertEquals(v.mkColl(1, 2, 3).head(), v.iterationOrder123().head());
            else {
                assertTrue(AHashSet.of(1, 2, 3).contains(v.mkColl(1, 2, 3).head()));
            }
        });
    }
    @Test default void testHeadOption() {
        doTest(v -> {
            assertTrue(v.mkColl().headOption().isEmpty());
            assertTrue(v.mkColl(1).headOption().contains(1));
            if (v.iterationOrder123() != null)
                assertTrue(v.mkColl(1, 2, 3).headOption().contains(v.iterationOrder123().head()));
            else {
                assertTrue(v.mkColl(1, 2, 3).toSet().contains(v.mkColl(1, 2, 3).headOption().get()));
            }
        });
    }

    @Test default void testToLinkedList() {
        doTest(v -> {
            assertEquals(ALinkedList.empty(), v.mkColl().toLinkedList());
            v.checkEquality(v.mkColl().toLinkedList());

            assertEquals(ALinkedList.of(1), v.mkColl(1).toLinkedList());
            assertEquals(v.mkColl(1, 2, 3, 4).toLinkedList(), v.mkColl(1, 2, 3, 4));
        });
    }
    @Test default void testToVector() {
        doTest(v -> {
            assertEquals(AVector.empty(), v.mkColl());
            v.checkEquality(v.mkColl().toVector());

            assertEquals(AVector.of(1), v.mkColl(1).toVector());
            assertEquals(v.mkColl(1, 2, 3, 4).toVector(), v.mkColl(1, 2, 3, 4));
        });
    }

    @Test default void testToSet() {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().toSet());
            if (! isSorted()) v.checkEquality(v.mkColl().toSet());

            assertEquals(v.mkColl(1), v.mkColl(1).toSet());
            assertEquals(AHashSet.of(1, 2, 3, 4), v.mkColl(1, 2, 3, 4).toSet());
        });
    }
    @Test default void testToSortedSet() {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().toSortedSet());
            assertEquals(v.mkColl(1), v.mkColl(1).toSortedSet());
            assertEquals(ATreeSet.of(1, 2, 3, 4), v.mkColl(2, 1, 4, 3).toSortedSet());
        });
    }

    @Test default void testMap() {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().map(this::doubled));
            v.checkEquality(v.mkColl().map(this::doubled));
            assertEquals(v.mkColl(2), v.mkColl(1).map(this::doubled));
            v.checkEquality(v.mkColl(1).map(this::doubled));
            assertEquals(v.mkColl(2, 4, 6), v.mkColl(1, 2, 3).map(this::doubled));
        });
    }
    @Test default void testFlatMap() {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().flatMap(x -> AVector.of(2*x, 2*x+1)));
            v.checkEquality(v.mkColl().flatMap(x -> AVector.of(2*x, 2*x+1)));
            assertEquals(v.mkColl(2, 3), v.mkColl(1).flatMap(x -> AVector.of(2*x, 2*x+1)));
            v.checkEquality(v.mkColl(1).flatMap(x -> AVector.of(2*x, 2*x+1)));
            assertEquals(v.mkColl(2, 3, 4, 5, 6, 7), v.mkColl(1, 2, 3).flatMap(x -> AVector.of(2*x, 2*x+1)));
        });
    }
    @Test default void testCollect() {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().collect(this::isOdd, this::doubled));
            v.checkEquality(v.mkColl().collect(this::isOdd, this::doubled));
            assertEquals(v.mkColl(2), v.mkColl(1).collect(this::isOdd, this::doubled));
            v.checkEquality(v.mkColl(1).collect(this::isOdd, this::doubled));
            assertEquals(v.mkColl(2, 6), v.mkColl(1, 2, 3).collect(this::isOdd, this::doubled));
        });
    }
    @Test default void testCollectFirst() {
        doTest(v -> {
            assertEquals(AOption.none(), v.mkColl().collectFirst(this::isOdd, this::doubled));
            v.checkEquality(v.mkColl().collect(this::isOdd, this::doubled));
            assertEquals(AOption.none(), v.mkColl(2).collectFirst(this::isOdd, this::doubled));
            assertEquals(AOption.some(2), v.mkColl(1).collectFirst(this::isOdd, this::doubled));
            v.checkEquality(v.mkColl(1).collect(this::isOdd, this::doubled));

            final int firstOdd;
            if (v.iterationOrder123() != null)
                firstOdd = v.iterationOrder123.head();
            else {
                final Iterator<Integer> it = v.mkColl(1, 2, 3).iterator();
                if (it.next() == 2) firstOdd = it.next();
                else firstOdd = v.mkColl(1, 2, 3).iterator().next();
            }
            assertEquals(AOption.some(2*firstOdd), v.mkColl(1, 2, 3).collectFirst(this::isOdd, this::doubled));
        });
    }

    @Test default void testFilter() {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().filter(this::isOdd));
            v.checkEquality(v.mkColl().filter(this::isOdd));
            assertEquals(v.mkColl(1), v.mkColl(1).filter(this::isOdd));
            v.checkEquality(v.mkColl(1).filter(this::isOdd));
            assertEquals(v.mkColl(1, 3), v.mkColl(1, 2, 3).filter(this::isOdd));
        });
    }
    @Test default void testFilterNot() {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().filterNot(this::isEven));
            v.checkEquality(v.mkColl().filterNot(this::isEven));
            assertEquals(v.mkColl(1), v.mkColl(1).filterNot(this::isEven));
            v.checkEquality(v.mkColl(1).filterNot(this::isEven));
            assertEquals(v.mkColl(1, 3), v.mkColl(1, 2, 3).filterNot(this::isEven));
        });
    }

    @Test default void testFind() {
        doTest(v -> {
            assertEquals(AOption.none(), v.mkColl().find(this::isEven));
            assertEquals(AOption.none(), v.mkColl(1).find(this::isEven));
            assertEquals(AOption.some(1), v.mkColl(1).find(this::isOdd));
            assertEquals(AOption.some(2), v.mkColl(1, 2, 3).find(this::isEven));
        });
    }

    @Test default void testForall() {
        doTest(v -> {
            assertTrue(v.mkColl().forall(this::isOdd));
            assertTrue(v.mkColl(1).forall(this::isOdd));
            assertFalse(v.mkColl(1).forall(this::isEven));
            assertFalse(v.mkColl(1, 2, 3).forall(this::isOdd));
            assertFalse(v.mkColl(1, 2, 3).forall(this::isEven));
            assertTrue(v.mkColl(1, 2, 3).map(this::doubled).forall(this::isEven));
        });
    }
    @Test default void testExists() {
        doTest(v -> {
            assertFalse(v.mkColl().exists(this::isOdd));
            assertTrue(v.mkColl(1).exists(this::isOdd));
            assertFalse(v.mkColl(1).exists(this::isEven));
            assertTrue(v.mkColl(1, 2, 3).exists(this::isOdd));
            assertTrue(v.mkColl(1, 2, 3).exists(this::isEven));
            assertFalse(v.mkColl(1, 2, 3).map(this::doubled).exists(this::isOdd));
            assertTrue(v.mkColl(1, 2, 3).map(this::doubled).exists(this::isEven));
        });
    }
    @Test default void testCount() {
        doTest(v -> {
            assertEquals(0, v.mkColl().count(this::isOdd));
            assertEquals(1, v.mkColl(1).count(this::isOdd));
            assertEquals(0, v.mkColl(1).count(this::isEven));
            assertEquals(2, v.mkColl(1, 2, 3).count(this::isOdd));
            assertEquals(1, v.mkColl(1, 2, 3).count(this::isEven));
            assertEquals(0, v.mkColl(1, 2, 3).map(this::doubled).count(this::isOdd));
            assertEquals(3, v.mkColl(1, 2, 3).map(this::doubled).count(this::isEven));
        });
    }
    @Test default void testContains() {
        doTest(v -> {
            assertFalse(v.mkColl().contains(1));
            assertTrue(v.mkColl(1).contains(1));
            assertFalse(v.mkColl(1).contains(2));
            assertTrue(v.mkColl(1, 2, 3).contains(1));
            assertTrue(v.mkColl(1, 2, 3).contains(2));
            assertTrue(v.mkColl(1, 2, 3).contains(3));
            assertFalse(v.mkColl(1, 2, 3).contains(4));

            //noinspection UnnecessaryBoxing
            assertEquals(!v.isIdentity(), v.mkColl(1).contains(new Integer(1)));
        });
    }
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @Test default void testContainsAll() {
        doTest(v -> {
            assertTrue(v.mkColl().containsAll(Collections.emptyList()));
            assertFalse(v.mkColl().containsAll(Arrays.asList(1)));
            assertFalse(v.mkColl().containsAll(Arrays.asList(1, 2, 3)));

            assertTrue(v.mkColl(1).containsAll(Arrays.asList(1)));
            assertFalse(v.mkColl(1).containsAll(Arrays.asList(1, 2)));
            assertFalse(v.mkColl(1).containsAll(Arrays.asList(2)));

            assertTrue(v.mkColl(1, 2, 3).containsAll(Arrays.asList(1)));
            assertTrue(v.mkColl(1, 2, 3).containsAll(Arrays.asList(2)));
            assertTrue(v.mkColl(1, 2, 3).containsAll(Arrays.asList(3)));
            assertFalse(v.mkColl(1, 2, 3).containsAll(Arrays.asList(4)));
            assertTrue(v.mkColl(1, 2, 3).containsAll(Arrays.asList(1, 2)));
            assertTrue(v.mkColl(1, 2, 3).containsAll(Arrays.asList(3, 2, 1)));
            assertFalse(v.mkColl(1, 2, 3).containsAll(Arrays.asList(3, 2, 4)));

            //noinspection UnnecessaryBoxing
            assertEquals(!v.isIdentity(), v.mkColl(1).containsAll(Arrays.asList(new Integer(1))));
        });
    }

    @Test default void testReduce() {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkColl().reduce(this::sum));
            assertEquals(1, v.mkColl(1).reduce(this::sum).intValue());
            assertEquals(6, v.mkColl(1, 2, 3).reduce(this::sum).intValue());

            if (v.iterationOrder123() != null) {
                final List<Integer> trace = new ArrayList<>();
                v.mkColl(1, 2, 3).reduce((a, b) -> {
                    trace.add(a);
                    trace.add(b);
                    return 0;
                });
                assertEquals(Arrays.asList(v.iterationOrder123().get(0), v.iterationOrder123().get(1), 0, v.iterationOrder123().get(2)), trace);
            }
        });
    }
    @Test default void testReduceLeft() {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkColl().reduceLeft(this::sum));
            assertEquals(1, v.mkColl(1).reduceLeft(this::sum).intValue());
            assertEquals(6, v.mkColl(1, 2, 3).reduceLeft(this::sum).intValue());

            if (v.iterationOrder123() != null) {
                final List<Integer> trace = new ArrayList<>();
                v.mkColl(1, 2, 3).reduceLeft((a, b) -> {
                    trace.add(a);
                    trace.add(b);
                    return 0;
                });
                assertEquals(Arrays.asList(v.iterationOrder123().get(0), v.iterationOrder123().get(1), 0, v.iterationOrder123().get(2)), trace);
            }
        });
    }
    @Test default void testReduceLeftOption() {
        doTest(v -> {
            assertEquals(AOption.none(), v.mkColl().reduceLeftOption(this::sum));
            assertEquals(AOption.some(1), v.mkColl(1).reduceLeftOption(this::sum));
            assertEquals(AOption.some(6), v.mkColl(1, 2, 3).reduceLeftOption(this::sum));

            if (v.iterationOrder123() != null) {
                final List<Integer> trace = new ArrayList<>();
                v.mkColl(1, 2, 3).reduceLeftOption((a, b) -> {
                    trace.add(a);
                    trace.add(b);
                    return 0;
                });
                assertEquals(Arrays.asList(v.iterationOrder123().get(0), v.iterationOrder123().get(1), 0, v.iterationOrder123().get(2)), trace);
            }
        });
    }

    @Test default void testFold() {
        doTest(v -> {
            assertEquals(0, v.mkColl().fold(0, this::sum).intValue());
            assertEquals(1, v.mkColl(1).fold(0, this::sum).intValue());
            assertEquals(6, v.mkColl(1, 2, 3).fold(0, this::sum).intValue());

            if (v.iterationOrder123() != null) {
                final List<Integer> trace = new ArrayList<>();
                v.mkColl(1, 2, 3).fold(0, (a, b) -> {
                    trace.add(b);
                    return 0;
                });
                assertEquals(Arrays.asList(v.iterationOrder123().get(0), v.iterationOrder123().get(1), v.iterationOrder123().get(2)), trace);
            }
        });
    }
    @Test default void testFoldLeft() {
        doTest(v -> {
            assertEquals(0, v.mkColl().foldLeft(0, this::sum).intValue());
            assertEquals(1, v.mkColl(1).foldLeft(0, this::sum).intValue());
            assertEquals(6, v.mkColl(1, 2, 3).foldLeft(0, this::sum).intValue());

            if (v.iterationOrder123() != null) {
                final List<Integer> trace = new ArrayList<>();
                v.mkColl(1, 2, 3).foldLeft(0, (a, b) -> {
                    trace.add(b);
                    return 0;
                });
                assertEquals(Arrays.asList(v.iterationOrder123().get(0), v.iterationOrder123().get(1), v.iterationOrder123().get(2)), trace);
            }
        });
    }

    @Test default void testMin() {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkColl().min());
            assertEquals(1, v.mkColl(1).min().intValue());
            assertEquals(1, v.mkColl(2, 1, 3).min().intValue());
            assertEquals(3, v.mkColl(2, 1, 3).min(Comparator.<Integer>naturalOrder().reversed()).intValue());
        });
    }
    @Test default void testMax() {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkColl().max());
            assertEquals(1, v.mkColl(1).max().intValue());
            assertEquals(3, v.mkColl(2, 3, 1).max().intValue());
            assertEquals(1, v.mkColl(2, 1, 3).max(Comparator.<Integer>naturalOrder().reversed()).intValue());
        });
    }

    @Test default void testMkString() {
        doTest(v -> {
            assertEquals("", v.mkColl().mkString("|"));
            assertEquals("$%", v.mkColl().mkString("$", "|", "%"));
            assertEquals("1", v.mkColl(1).mkString("|"));
            assertEquals("$1%", v.mkColl(1).mkString("$", "|", "%"));

            if (v.iterationOrder123() != null) {
                assertEquals(v.iterationOrder123().mkString("|"), v.mkColl(1, 2, 3).mkString("|"));
                assertEquals(v.iterationOrder123().mkString("$", "|", "%"), v.mkColl(1, 2, 3).mkString("$", "|", "%"));
            }
        });
    }

    //TODO keySet, entrySet, Range; AMap


    //---------------------------- internal -------------------------------

    default void doTest(Consumer<Variant> test) {
        variants().forEach(test);
    }

    class Variant {
        private final Supplier<ACollectionBuilder<Integer, ? extends ACollection<Integer>>> builderFactory;
        private final AVector<Integer> iterationOrder123;
        private final boolean isIdentity;

        public Variant (Supplier<ACollectionBuilder<Integer, ? extends ACollection<Integer>>> builderFactory, AVector<Integer> iterationOrder123, boolean isIdentity) {
            this.builderFactory = builderFactory;
            this.iterationOrder123 = iterationOrder123;
            this.isIdentity = isIdentity;
        }

        public ACollectionBuilder<Integer, ? extends ACollection<Integer>> newBuilder() {
            return builderFactory.get();
        }

        public ACollection<Integer> mkColl(Integer... values) {
            return newBuilder()
                    .addAll(values)
                    .build();
        }

        public AVector<Integer> iterationOrder123() {
            return this.iterationOrder123;
        }

        public boolean isIdentity() {
            return isIdentity;
        }

        public void checkEquality(ACollection<Integer> coll) {
            assertEquals(builderFactory.get().equality(), coll.equality());
        }
    }
}