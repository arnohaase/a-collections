package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.*;
import com.ajjpj.acollections.mutable.AMutableListWrapper;
import com.ajjpj.acollections.util.AOption;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public interface ACollectionTests extends ACollectionOpsTests {
    // These methods must be implemented by concrete test classes, customizing the tests per tested collection class

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
        //TODO else tests for mutators
    }

    @Test default void testStream() {
        doTest(v -> {
            assertTrue (v.mkColl().stream().map(this::doubled).collect(Collectors.toList()).isEmpty());
            assertEquals (Collections.singletonList(2), v.mkColl(1).stream().map(this::doubled).collect(Collectors.toList()));
            if (v.iterationOrder123() != null)
                assertEquals (v.iterationOrder123().map(this::doubled), v.mkColl(1, 2, 3).stream().map(this::doubled).collect(Collectors.toList()));
            else
                assertEquals (AHashSet.of(2, 4, 6), v.mkColl(1, 2, 3).stream().map(this::doubled).collect(Collectors.toSet()));
        });
    }
    @Test default void testParallelStream() {
        doTest(v -> {
            assertTrue (v.mkColl().parallelStream().map(this::doubled).collect(Collectors.toList()).isEmpty());
            assertEquals (Collections.singletonList(2), v.mkColl(1).parallelStream().map(this::doubled).collect(Collectors.toList()));
            if (v.iterationOrder123() != null)
                assertEquals (v.iterationOrder123().map(this::doubled), v.mkColl(1, 2, 3).parallelStream().map(this::doubled).collect(Collectors.toList()));
            else
                assertEquals (AHashSet.of(2, 4, 6), v.mkColl(1, 2, 3).parallelStream().map(this::doubled).collect(Collectors.toSet()));
        });
    }

    @Test default void testForEach() {
        doTest(v -> {
            v.mkColl().forEach(i -> {
                throw new RuntimeException("never called");
            });

            AMutableListWrapper<Integer> trace = AMutableListWrapper.empty();
            //noinspection UseBulkOperation
            v.mkColl(1).forEach(trace::add);
            //noinspection AssertEqualsBetweenInconvertibleTypes
            assertEquals (AVector.of(1), trace);

            trace.clear();
            //noinspection UseBulkOperation
            v.mkColl(1, 2, 3).forEach(trace::add);
            if (v.iterationOrder123() != null)
                //noinspection AssertEqualsBetweenInconvertibleTypes
                assertEquals(v.iterationOrder123(), trace);
            else
                assertEquals(AHashSet.of(1, 2, 3), trace.toSet());
        });
    }

    @Test @Override default void testIterator() {
        doTest(v -> {
            assertTrue(! v.mkColl().iterator().hasNext());
            assertEquals(AVector.of(1), v.mkColl(1).iterator().toVector());

            if (v.iterationOrder123 != null)
                assertEquals(v.iterationOrder123, v.mkColl(1, 2, 3).iterator().toVector());
            else
                assertEquals(v.mkColl(1, 2, 3).toSet(), v.mkColl(1, 2, 3).iterator().toSet());
        });
    }

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

    @Test @Override default void testSize() {
        doTest(v -> {
            assertEquals(0, v.mkColl().size());
            assertEquals(1, v.mkColl(1).size());
            assertEquals(3, v.mkColl(1, 2, 3).size());
        });
    }

    @Test @Override default void testIsEmpty() {
        doTest(v -> {
            assertTrue(v.mkColl().isEmpty());
            assertFalse(v.mkColl(1).isEmpty());
            assertFalse(v.mkColl(0).isEmpty());
            assertFalse(v.mkColl(1, 2, 3).isEmpty());
        });
    }
    @Test @Override default void testNonEmpty() {
        doTest(v -> {
            assertFalse(v.mkColl().nonEmpty());
            assertTrue(v.mkColl(1).nonEmpty());
            assertTrue(v.mkColl(0).nonEmpty());
            assertTrue(v.mkColl(1, 2, 3).nonEmpty());
        });
    }

    @Test @Override default void testHead() {
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
    @Test @Override default void testHeadOption() {
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

    @Test @Override default void testToLinkedList() {
        doTest(v -> {
            assertEquals(ALinkedList.empty(), v.mkColl().toLinkedList());
            assertEquals(ALinkedList.of(1), v.mkColl(1).toLinkedList());
            assertEquals(v.mkColl(1, 2, 3, 4).toLinkedList(), v.mkColl(1, 2, 3, 4));
        });
    }
    @Test @Override default void testToVector() {
        doTest(v -> {
            assertEquals(AVector.empty(), v.mkColl());
            assertEquals(AVector.of(1), v.mkColl(1).toVector());
            assertEquals(v.mkColl(1, 2, 3, 4).toVector(), v.mkColl(1, 2, 3, 4));
        });
    }

    @Test @Override default void testToSet() {
        doTest(v -> {
            assertTrue(v.mkColl().toSet().isEmpty());
            assertEquals(AHashSet.of(1), v.mkColl(1).toSet());
            assertEquals(AHashSet.of(1, 2, 3, 4), v.mkColl(1, 2, 3, 4).toSet());
        });
    }
    @Test @Override default void testToSortedSet() {
        doTest(v -> {
            assertTrue(v.mkColl().toSortedSet().isEmpty());
            assertEquals(ATreeSet.of(1), v.mkColl(1).toSortedSet());
            assertEquals(ATreeSet.of(1, 2, 3, 4), v.mkColl(2, 1, 4, 3).toSortedSet());
        });
    }

    @Test @Override default void testToMutableList() {
        doTest(v -> {
            assertEquals(AMutableListWrapper.empty(), v.mkColl().toMutableList());
            assertEquals(AMutableListWrapper.of(1), v.mkColl(1).toMutableList());
            assertEquals(v.mkColl(1, 2, 3, 4).toMutableList().toVector(), v.mkColl(1, 2, 3, 4));
        });
    }
    @Test @Override default void testToMutableSet() {
        doTest(v -> {
            assertTrue(v.mkColl().toMutableSet().isEmpty());
            assertEquals(AHashSet.of(1), v.mkColl(1).toMutableSet());
            assertEquals(AHashSet.of(1, 2, 3, 4), v.mkColl(1, 2, 3, 4).toMutableSet());
        });
    }

    @Test @Override default void testMap() {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().map(this::doubled));
            assertEquals(v.mkColl(2), v.mkColl(1).map(this::doubled));
            assertEquals(v.mkColl(2, 4, 6), v.mkColl(1, 2, 3).map(this::doubled));
        });
    }
    @Test @Override default void testFlatMap() {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().flatMap(x -> AVector.of(2*x, 2*x+1)));
            assertEquals(v.mkColl(2, 3), v.mkColl(1).flatMap(x -> AVector.of(2*x, 2*x+1)));
            assertEquals(v.mkColl(2, 3, 4, 5, 6, 7), v.mkColl(1, 2, 3).flatMap(x -> AVector.of(2*x, 2*x+1)));
        });
    }
    @Test @Override default void testCollect() {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().collect(this::isOdd, this::doubled));
            assertEquals(v.mkColl(2), v.mkColl(1).collect(this::isOdd, this::doubled));
            assertEquals(v.mkColl(2, 6), v.mkColl(1, 2, 3).collect(this::isOdd, this::doubled));
        });
    }
    @Test @Override default void testCollectFirst() {
        doTest(v -> {
            assertEquals(AOption.none(), v.mkColl().collectFirst(this::isOdd, this::doubled));
            assertEquals(AOption.none(), v.mkColl(2).collectFirst(this::isOdd, this::doubled));
            assertEquals(AOption.some(2), v.mkColl(1).collectFirst(this::isOdd, this::doubled));

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

    @Test @Override default void testFilter() {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().filter(this::isOdd));
            assertEquals(v.mkColl(1), v.mkColl(1).filter(this::isOdd));
            assertEquals(v.mkColl(1, 3), v.mkColl(1, 2, 3).filter(this::isOdd));
        });
    }
    @Test @Override default void testFilterNot() {
        doTest(v -> {
            assertEquals(v.mkColl(), v.mkColl().filterNot(this::isEven));
            assertEquals(v.mkColl(1), v.mkColl(1).filterNot(this::isEven));
            assertEquals(v.mkColl(1, 3), v.mkColl(1, 2, 3).filterNot(this::isEven));
        });
    }

    @Test @Override default void testFind() {
        doTest(v -> {
            assertEquals(AOption.none(), v.mkColl().find(this::isEven));
            assertEquals(AOption.none(), v.mkColl(1).find(this::isEven));
            assertEquals(AOption.some(1), v.mkColl(1).find(this::isOdd));
            assertEquals(AOption.some(2), v.mkColl(1, 2, 3).find(this::isEven));
        });
    }

    @Test @Override default void testForall() {
        doTest(v -> {
            assertTrue(v.mkColl().forall(this::isOdd));
            assertTrue(v.mkColl(1).forall(this::isOdd));
            assertFalse(v.mkColl(1).forall(this::isEven));
            assertFalse(v.mkColl(1, 2, 3).forall(this::isOdd));
            assertFalse(v.mkColl(1, 2, 3).forall(this::isEven));
            assertTrue(v.mkColl(1, 2, 3).map(this::doubled).forall(this::isEven));
        });
    }
    @Test @Override default void testExists() {
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
    @Test @Override default void testCount() {
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
        });
    }

    @Test @Override default void testReduce() {
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
    @Test @Override default void testReduceLeft() {
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
    @Test @Override default void testReduceLeftOption() {
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

    @Test @Override default void testFold() {
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
    @Test @Override default void testFoldLeft() {
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

    @Test @Override default void testGroupBy() {
        doTest(v -> {
            assertTrue(v.mkColl().groupBy(this::isOdd).isEmpty());
            assertEquals(AHashMap.empty().updated(true, v.mkColl(1, 3)).updated(false, v.mkColl(2)), v.mkColl(1, 2, 3).groupBy(this::isOdd));
        });
    }

    @Test @Override default void testMin() {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkColl().min());
            assertEquals(1, v.mkColl(1).min().intValue());
            assertEquals(1, v.mkColl(2, 1, 3).min().intValue());
            assertEquals(3, v.mkColl(2, 1, 3).min(Comparator.<Integer>naturalOrder().reversed()).intValue());
        });
    }
    @Test @Override default void testMax() {
        doTest(v -> {
            assertThrows(NoSuchElementException.class, () -> v.mkColl().max());
            assertEquals(1, v.mkColl(1).max().intValue());
            assertEquals(3, v.mkColl(2, 3, 1).max().intValue());
            assertEquals(1, v.mkColl(2, 1, 3).max(Comparator.<Integer>naturalOrder().reversed()).intValue());
        });
    }

    @Test @Override default void testMkString() {
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

    //---------------------------- internal -------------------------------

    default void doTest(Consumer<Variant> test) {
        variants().forEach(test);
    }

    class Variant {
        private final Supplier<ACollectionBuilder<Integer, ? extends ACollection<Integer>>> builderFactory;
        private final AVector<Integer> iterationOrder123;

        public Variant (Supplier<ACollectionBuilder<Integer, ? extends ACollection<Integer>>> builderFactory, AVector<Integer> iterationOrder123) {
            this.builderFactory = builderFactory;
            this.iterationOrder123 = iterationOrder123;
        }

        public ACollectionBuilder<Integer, ? extends ACollection<Integer>> newBuilder() {
            return builderFactory.get();
        }

        public ACollection<Integer> mkColl(Integer... values) {
            return newBuilder()
                    .addAll(values)
                    .build();
        }

        public ASet<Integer> mkSet(Integer... values) {
            return (ASet<Integer>) mkColl(values);
        }

        public AList<Integer> mkList(Integer... values) {
            return (AList<Integer>) mkColl(values);
        }

        public AVector<Integer> iterationOrder123() {
            return this.iterationOrder123;
        }
    }
}
