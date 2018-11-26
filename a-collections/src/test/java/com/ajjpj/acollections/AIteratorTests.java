package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.ALinkedList;
import com.ajjpj.acollections.immutable.ATreeSet;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.mutable.AMutableListWrapper;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static com.ajjpj.acollections.util.AOption.none;
import static com.ajjpj.acollections.util.AOption.some;
import static org.junit.jupiter.api.Assertions.*;


public interface AIteratorTests {
    default boolean isOrdered() {
        return true;
    }
    AIterator<Integer> mkIterator (Integer... values);

    default boolean isOdd(int i) {
        return i%2==1;
    }
    default boolean isEven(int i) {
        return i%2==0;
    }

    //-------------------------------------------------------------------------------

    @Test default void testToVector () {
        assertTrue(mkIterator().toVector().isEmpty());
        assertEquals(AVector.of(1), mkIterator(1).toVector());
        assertEquals(AVector.of(1, 2, 3), mkIterator(1, 2, 3).toVector());
    }
    @Test default void testToLinkedList () {
        assertTrue(mkIterator().toLinkedList().isEmpty());
        assertEquals(ALinkedList.of(1), mkIterator(1).toLinkedList());
        assertEquals(ALinkedList.of(1, 2, 3), mkIterator(1, 2, 3).toLinkedList());
    }
    @Test default void testToSet () {
        assertTrue(mkIterator().toSet().isEmpty());
        assertEquals(AHashSet.of(1), mkIterator(1).toSet());
        assertEquals(AHashSet.of(1, 2, 3), mkIterator(1, 2, 3).toSet());
    }
    @Test default void testToSortedSet () {
        assertTrue(mkIterator().toSortedSet().isEmpty());
        assertEquals(ATreeSet.of(1), mkIterator(1).toSortedSet());
        assertEquals(ATreeSet.of(1, 2, 3), mkIterator(1, 2, 3).toSortedSet());
    }
    @Test default void testToMutableList() {
        assertTrue(mkIterator().toMutableList().isEmpty());
        assertEquals(AMutableListWrapper.of(1), mkIterator(1).toMutableList());
        assertEquals(AMutableListWrapper.of(1, 2, 3), mkIterator(1, 2, 3).toMutableList());
    }
    @Test default void testToMutableSet() {
        assertTrue(mkIterator().toMutableSet().isEmpty());
        assertEquals(AHashSet.of(1), mkIterator(1).toMutableSet());
        assertEquals(AHashSet.of(1, 2, 3), mkIterator(1, 2, 3).toMutableSet());
    }

    @Test default void testCorresponds() {
        assertTrue(mkIterator().corresponds(mkIterator()));
        assertFalse(mkIterator(1).corresponds(mkIterator()));
        assertFalse(mkIterator().corresponds(mkIterator(1)));

        assertTrue(mkIterator(1).corresponds(mkIterator(1)));
        assertFalse(mkIterator(1).corresponds(mkIterator(2)));
        assertFalse(mkIterator(1).corresponds(mkIterator(1, 2)));
        assertFalse(mkIterator(1, 2).corresponds(mkIterator(1)));

        assertTrue(mkIterator(1, 2, 3).corresponds(mkIterator(1, 2, 3)));
        if (isOrdered()) {
            assertFalse(mkIterator(1, 2, 3).corresponds(mkIterator(3, 2, 1)));
        }
    }
    @Test default void testCorrespondsWithPredicate() {
        final BiPredicate<Integer,Integer> eqDouble = (a,b) -> a == 2*b;

        assertTrue(mkIterator().corresponds(mkIterator(), eqDouble));
        assertFalse(mkIterator(1).corresponds(mkIterator(), eqDouble));
        assertFalse(mkIterator().corresponds(mkIterator(1), eqDouble));

        assertFalse(mkIterator(1).corresponds(mkIterator(1), eqDouble));
        assertTrue(mkIterator(2).corresponds(mkIterator(1), eqDouble));
        assertFalse(mkIterator(3).corresponds(mkIterator(1), eqDouble));
        assertFalse(mkIterator(2).corresponds(mkIterator(1, 2), eqDouble));
        assertFalse(mkIterator(2, 4).corresponds(mkIterator(1), eqDouble));

        if (isOrdered()) {
            assertTrue(mkIterator(2, 4, 6).corresponds(mkIterator(1, 2, 3), eqDouble));
            assertFalse(mkIterator(2, 4, 6).corresponds(mkIterator(3, 2, 1), eqDouble));
        }
    }

    @Test default void testFlatMap() {
        assertTrue(mkIterator().flatMap(n -> AVector.of(n, n-1, n+1).iterator()).toVector().isEmpty());
        assertTrue(mkIterator(1, 2, 3).flatMap(n -> AIterator.empty()).toVector().isEmpty());
        assertEquals(AVector.of(1, 2, 3), mkIterator(1, 2, 3).flatMap(AIterator::single).toVector());
        assertEquals(AVector.of(1, 0, 2, 2, 1, 3, 3, 2, 4), mkIterator(1, 2, 3).flatMap(n -> AVector.of(n, n-1, n+1).iterator()).toVector());
    }
    @Test default void testMap () {
        assertTrue(mkIterator().map(String::valueOf).toVector().isEmpty());
        assertEquals(AVector.of("1"), mkIterator(1).map(String::valueOf).toVector());
        assertEquals(AVector.of("1", "2", "3"), mkIterator(1, 2, 3).map(String::valueOf).toVector());
    }
    @Test default void testFilter() {
        assertTrue(mkIterator().filter(this::isOdd).toVector().isEmpty());
        assertEquals(AVector.of(1), mkIterator(1).filter(this::isOdd).toVector());
        assertEquals(AVector.empty(), mkIterator(1).filter(this::isEven).toVector());
        assertEquals(AVector.of(1, 3), mkIterator(1, 2, 3).filter(this::isOdd).toVector());
        assertEquals(AVector.of(2), mkIterator(1, 2, 3).filter(this::isEven).toVector());
    }
    @Test default void testFilterNot() {
        assertTrue(mkIterator().filterNot(this::isOdd).toVector().isEmpty());
        assertEquals(AVector.of(1), mkIterator(1).filterNot(this::isEven).toVector());
        assertEquals(AVector.empty(), mkIterator(1).filterNot(this::isOdd).toVector());
        assertEquals(AVector.of(1, 3), mkIterator(1, 2, 3).filterNot(this::isEven).toVector());
        assertEquals(AVector.of(2), mkIterator(1, 2, 3).filterNot(this::isOdd).toVector());
    }

    @Test default void testCollect() {
        assertTrue (mkIterator().collect(x -> true, Function.identity()).toVector().isEmpty());
        assertTrue (mkIterator().collect(x -> false, Function.identity()).toVector().isEmpty());

        assertTrue (mkIterator(1).collect(this::isEven, Function.identity()).toVector().isEmpty());
        assertEquals (AVector.of(1), mkIterator(1).collect(this::isOdd, Function.identity()).toVector());
        assertEquals (AVector.of("1"), mkIterator(1).collect(this::isOdd, String::valueOf).toVector());

        assertEquals (AVector.of("1", "3"), mkIterator(1, 2, 3).collect(this::isOdd, String::valueOf).toVector());
        assertEquals (AVector.of("2"), mkIterator(1, 2, 3).collect(this::isEven, String::valueOf).toVector());
    }
    @Test default void testCollectFirst() {
        assertTrue (mkIterator().collectFirst(x -> true, Function.identity()).isEmpty());
        assertTrue (mkIterator().collectFirst(x -> false, Function.identity()).isEmpty());

        assertTrue (mkIterator(1).collectFirst(this::isEven, Function.identity()).isEmpty());
        assertEquals (some(1), mkIterator(1).collectFirst(this::isOdd, Function.identity()));
        assertEquals (some("1"), mkIterator(1).collectFirst(this::isOdd, String::valueOf));

        assertEquals (some("1"), mkIterator(1, 2, 3).collectFirst(this::isOdd, String::valueOf));
        assertEquals (some("2"), mkIterator(1, 2, 3).collectFirst(this::isEven, String::valueOf));
    }

    @Test default void testDrop() {
        assertFalse(mkIterator().drop(-1).hasNext());
        assertFalse(mkIterator().drop(0).hasNext());
        assertThrows(NoSuchElementException.class, () -> mkIterator().drop(1));

        assertEquals(AVector.of(1), mkIterator(1).drop(-1).toVector());
        assertEquals(AVector.of(1), mkIterator(1).drop(0).toVector());
        assertEquals(AVector.empty(), mkIterator(1).drop(1).toVector());
        assertThrows(NoSuchElementException.class, () -> mkIterator(1).drop(2));

        assertEquals(AVector.of(1, 2, 3), mkIterator(1, 2, 3).drop(-1).toVector());
        assertEquals(AVector.of(1, 2, 3), mkIterator(1, 2, 3).drop(0).toVector());
        assertEquals(AVector.of(2, 3), mkIterator(1, 2, 3).drop(1).toVector());
        assertEquals(AVector.of(3), mkIterator(1, 2, 3).drop(2).toVector());
        assertEquals(AVector.empty(), mkIterator(1, 2, 3).drop(3).toVector());
        assertThrows(NoSuchElementException.class, () -> mkIterator(1, 2, 3).drop(4));
    }

    @Test default void testFind() {
        assertTrue(mkIterator().find(x -> true).isEmpty());
        assertTrue(mkIterator().find(x -> false).isEmpty());

        assertEquals(some(1), mkIterator(1).find(this::isOdd));
        assertEquals(none(), mkIterator(1).find(this::isEven));

        assertEquals(some(1), mkIterator(1, 2, 3).find(this::isOdd));
        assertEquals(some(2), mkIterator(1, 2, 3).find(this::isEven));
    }
    @Test default void testForall() {
        assertTrue(mkIterator().forall(this::isEven));
        assertTrue(mkIterator().forall(this::isOdd));

        assertTrue(mkIterator(1).forall(this::isOdd));
        assertFalse(mkIterator(1).forall(this::isEven));

        assertFalse(mkIterator(1, 2, 3).forall(this::isOdd));
        assertFalse(mkIterator(1, 2, 3).forall(this::isEven));

        assertTrue(mkIterator(1, 3, 5).forall(this::isOdd));
        assertFalse(mkIterator(1, 3, 5).forall(this::isEven));

        assertFalse(mkIterator(2, 4, 6).forall(this::isOdd));
        assertTrue(mkIterator(2, 4, 6).forall(this::isEven));
    }
    @Test default void testExists() {
        assertFalse(mkIterator().exists(this::isEven));
        assertFalse(mkIterator().exists(this::isOdd));

        assertTrue(mkIterator(1).exists(this::isOdd));
        assertFalse(mkIterator(1).exists(this::isEven));

        assertTrue(mkIterator(1, 2, 3).exists(this::isOdd));
        assertTrue(mkIterator(1, 2, 3).exists(this::isEven));

        assertTrue(mkIterator(1, 3, 5).exists(this::isOdd));
        assertFalse(mkIterator(1, 3, 5).exists(this::isEven));

        assertFalse(mkIterator(2, 4, 6).exists(this::isOdd));
        assertTrue(mkIterator(2, 4, 6).exists(this::isEven));
    }
    @Test default void testCount() {
        assertEquals(0, mkIterator().count(this::isEven));
        assertEquals(0, mkIterator().count(this::isOdd));

        assertEquals(1, mkIterator(1).count(this::isOdd));
        assertEquals(0, mkIterator(1).count(this::isEven));

        assertEquals(2, mkIterator(1, 2, 3).count(this::isOdd));
        assertEquals(1, mkIterator(1, 2, 3).count(this::isEven));

        assertEquals(3, mkIterator(1, 3, 5).count(this::isOdd));
        assertEquals(0, mkIterator(1, 3, 5).count(this::isEven));

        assertEquals(0, mkIterator(2, 4, 6).count(this::isOdd));
        assertEquals(3, mkIterator(2, 4, 6).count(this::isEven));
    }

    @Test default void testReduce() {
        assertThrows(NoSuchElementException.class, () -> mkIterator().reduce((a,b) -> null));

        assertEquals(1, mkIterator(1).reduce((a,b) -> a+b).intValue());
        assertEquals(2, mkIterator(2).reduce((a,b) -> a+b).intValue());
        assertEquals(6, mkIterator(1, 2, 3).reduce((a,b) -> a+b).intValue());
    }
    @Test default void testReduceOption() {
        assertEquals(none(), mkIterator().reduceOption((a,b) -> null));

        assertEquals(some(1), mkIterator(1).reduceOption((a,b) -> a+b));
        assertEquals(some(2), mkIterator(2).reduceOption((a,b) -> a+b));
        assertEquals(some(6), mkIterator(1, 2, 3).reduceOption((a,b) -> a+b));
    }
    @Test default void testFold() {
        assertEquals(0, mkIterator().fold(0, (a,b) -> null).intValue());
        assertEquals(99, mkIterator().fold(99, (a,b) -> null).intValue());

        assertEquals(1, mkIterator(1).fold(0, (acc,el) -> acc+el).intValue());
        assertEquals(2, mkIterator(2).fold(0, (acc,el) -> acc+el).intValue());
        assertEquals(9, mkIterator(2).fold(7, (acc,el) -> acc+el).intValue());

        assertEquals(6, mkIterator(1, 2, 3).fold(0, (acc,el) -> acc+el).intValue());
        assertEquals(31, mkIterator(1, 2, 3).fold(25, (acc,el) -> acc+el).intValue());
    }

    @Test default void testMin() {
        assertThrows(NoSuchElementException.class, () -> mkIterator().min());
        assertThrows(NoSuchElementException.class, () -> mkIterator().min(Comparator.naturalOrder()));
        assertThrows(NoSuchElementException.class, () -> mkIterator().min(Comparator.<Integer>naturalOrder().reversed()));

        assertEquals(1, mkIterator(1).min().intValue());
        assertEquals(1, mkIterator(1).min(Comparator.naturalOrder()).intValue());
        assertEquals(1, mkIterator(1).min(Comparator.<Integer>naturalOrder().reversed()).intValue());

        assertEquals(1, mkIterator(1, 2, 3).min().intValue());
        assertEquals(1, mkIterator(1, 2, 3).min(Comparator.naturalOrder()).intValue());
        assertEquals(3, mkIterator(1, 2, 3).min(Comparator.<Integer>naturalOrder().reversed()).intValue());
    }
    @Test default void testMax() {
        assertThrows(NoSuchElementException.class, () -> mkIterator().max());
        assertThrows(NoSuchElementException.class, () -> mkIterator().max(Comparator.naturalOrder()));
        assertThrows(NoSuchElementException.class, () -> mkIterator().max(Comparator.<Integer>naturalOrder().reversed()));

        assertEquals(1, mkIterator(1).max().intValue());
        assertEquals(1, mkIterator(1).max(Comparator.naturalOrder()).intValue());
        assertEquals(1, mkIterator(1).max(Comparator.<Integer>naturalOrder().reversed()).intValue());

        assertEquals(3, mkIterator(1, 2, 3).max().intValue());
        assertEquals(3, mkIterator(1, 2, 3).max(Comparator.naturalOrder()).intValue());
        assertEquals(1, mkIterator(1, 2, 3).max(Comparator.<Integer>naturalOrder().reversed()).intValue());
    }

    @Test default void testMkString() {
        assertEquals("", mkIterator().mkString("|"));
        assertEquals("<>", mkIterator().mkString("<", "|", ">"));

        assertEquals("1", mkIterator(1).mkString("|"));
        assertEquals("<1>", mkIterator(1).mkString("<", "|", ">"));

        assertEquals("1|2|3", mkIterator(1, 2, 3).mkString("|"));
        assertEquals("<1|2|3>", mkIterator(1, 2, 3).mkString("<", "|", ">"));
    }

    @Test default void testConcatToThis() {
        assertEquals(AVector.empty(), mkIterator().concat(mkIterator()).toVector());

        assertEquals(AVector.of(1), mkIterator(1).concat(mkIterator()).toVector());
        assertEquals(AVector.of(1), mkIterator().concat(mkIterator(1)).toVector());
        assertEquals(AVector.of(1, 1), mkIterator(1).concat(mkIterator(1)).toVector());

        assertEquals(AVector.of(1, 2, 3), mkIterator(1, 2, 3).concat(mkIterator()).toVector());
        assertEquals(AVector.of(1, 2, 3), mkIterator().concat(mkIterator(1, 2, 3)).toVector());
        assertEquals(AVector.of(1, 2, 3, 1), mkIterator(1, 2, 3).concat(mkIterator(1)).toVector());
        assertEquals(AVector.of(1, 1, 2, 3), mkIterator(1).concat(mkIterator(1, 2, 3)).toVector());
        assertEquals(AVector.of(1, 2, 3, 1, 2, 3), mkIterator(1, 2, 3).concat(mkIterator(1, 2, 3)).toVector());
    }

    @Test default void testStaticConcat() {
        assertEquals(AVector.empty(), AIterator.concat(mkIterator(), mkIterator()).toVector());

        assertEquals(AVector.of(1), AIterator.concat(mkIterator(1), mkIterator()).toVector());
        assertEquals(AVector.of(1), AIterator.concat(mkIterator(), mkIterator(1)).toVector());
        assertEquals(AVector.of(1, 1), AIterator.concat(mkIterator(1), mkIterator(1)).toVector());

        assertEquals(AVector.of(1, 2, 3), AIterator.concat(mkIterator(1, 2, 3), mkIterator()).toVector());
        assertEquals(AVector.of(1, 2, 3), AIterator.concat(mkIterator(), mkIterator(1, 2, 3)).toVector());
        assertEquals(AVector.of(1, 2, 3, 1), AIterator.concat(mkIterator(1, 2, 3), mkIterator(1)).toVector());
        assertEquals(AVector.of(1, 1, 2, 3), AIterator.concat(mkIterator(1), mkIterator(1, 2, 3)).toVector());
        assertEquals(AVector.of(1, 2, 3, 1, 2, 3), AIterator.concat(mkIterator(1, 2, 3), mkIterator(1, 2, 3)).toVector());

        assertEquals(AVector.of(1, 2, 3, 1, 2, 3, 1), AIterator.concat(mkIterator(1, 2, 3), mkIterator(), mkIterator(1, 2, 3), mkIterator(1)).toVector());
    }
}
