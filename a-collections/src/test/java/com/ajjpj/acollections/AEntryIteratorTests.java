package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.ALinkedList;
import com.ajjpj.acollections.immutable.AVector;
import com.ajjpj.acollections.mutable.AMutableListWrapper;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static com.ajjpj.acollections.util.AOption.none;
import static com.ajjpj.acollections.util.AOption.some;
import static org.junit.jupiter.api.Assertions.*;


public interface AEntryIteratorTests {
    default boolean isOrdered() {
        return true;
    }
    default boolean isAscending() {
        return true;
    }
    AIterator<Map.Entry<Integer,Integer>> mkIterator (Integer... values);

    //-------------------------------------------------------------------------------

    default boolean isOdd (Map.Entry<Integer,Integer> i) {
        return i.getKey()%2==1;
    }
    default boolean isEven (Map.Entry<Integer,Integer> i) {
        return i.getKey()%2==0;
    }
    default Map.Entry<Integer,Integer> doubled(Map.Entry<Integer,Integer> e) {
        return entryOf(2*e.getKey());
    }


    static Map.Entry<Integer,Integer> entryOf(int i) {
        return new AbstractMap.SimpleImmutableEntry<>(i, 2*i+1);
    }
    Comparator<Map.Entry<Integer,Integer>> keyComparator = Map.Entry.comparingByKey();

    default AVector<Map.Entry<Integer,Integer>> v123() {
        return isAscending() ? AVector.of(entryOf(1), entryOf(2), entryOf(3)) : AVector.of(entryOf(3), entryOf(2), entryOf(1));
    }

    //-------------------------------------------------------------------------------

    @Test default void testToVector () {
        assertTrue(mkIterator().toVector().isEmpty());
        assertEquals(AVector.of(entryOf(1)), mkIterator(1).toVector());
        assertEquals(v123(), mkIterator(1, 2, 3).toVector());
    }
    @Test default void testToLinkedList () {
        assertTrue(mkIterator().toLinkedList().isEmpty());
        assertEquals(ALinkedList.of(entryOf(1)), mkIterator(1).toLinkedList());
        assertEquals(v123(), mkIterator(1, 2, 3).toLinkedList());
    }
    @Test default void testToSet () {
        assertTrue(mkIterator().toSet().isEmpty());
        assertEquals(AHashSet.of(entryOf(1)), mkIterator(1).toSet());
        assertEquals(AHashSet.of(entryOf(1), entryOf(2), entryOf(3)), mkIterator(1, 2, 3).toSet());
    }
    @Test default void testToMutableList () {
        assertTrue(mkIterator().toMutableList().isEmpty());
        assertEquals(AMutableListWrapper.of(entryOf(1)), mkIterator(1).toMutableList());
        assertEquals(v123(), mkIterator(1, 2, 3).toMutableList());
    }
    @Test default void testToMutableSet () {
        assertTrue(mkIterator().toMutableSet().isEmpty());
        assertEquals(AHashSet.of(entryOf(1)), mkIterator(1).toMutableSet());
        assertEquals(AHashSet.of(entryOf(1), entryOf(2), entryOf(3)), mkIterator(1, 2, 3).toMutableSet());
    }

    @Test default void testCorresponds () {
        assertTrue(mkIterator().corresponds(mkIterator()));
        assertFalse(mkIterator(1).corresponds(mkIterator()));
        assertFalse(mkIterator().corresponds(mkIterator(1)));

        assertTrue(mkIterator(1).corresponds(mkIterator(1)));
        assertFalse(mkIterator(1).corresponds(mkIterator(2)));
        assertFalse(mkIterator(1).corresponds(mkIterator(1, 2)));
        assertFalse(mkIterator(1, 2).corresponds(mkIterator(1)));

        assertTrue(mkIterator(1, 2, 3).corresponds(mkIterator(1, 2, 3)));
        if (isOrdered()) {
            assertFalse(v123().reverseIterator().corresponds(mkIterator(3, 2, 1)));
        }
    }
    @Test default void testCorrespondsWithPredicate () {
        final BiPredicate<Map.Entry<Integer,Integer>, Map.Entry<Integer,Integer>> eqDouble = (a,b) -> a.getKey() == 2*b.getKey();

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
            assertFalse(v123().map(this::doubled).reverseIterator().corresponds(mkIterator(1, 2, 3), eqDouble));
        }
    }

    @Test default void testMap () {
        assertTrue(mkIterator().map(String::valueOf).toVector().isEmpty());
        assertEquals(AVector.of("1"), mkIterator(1).map(x -> String.valueOf(x.getKey())).toVector());
        assertEquals(isAscending() ? AVector.of("1", "2", "3") : AVector.of("3", "2", "1"),
                mkIterator(1, 2, 3).map(x -> String.valueOf(x.getKey())).toVector());
    }
    @Test default void testFilter () {
        assertTrue(mkIterator().filter(this::isOdd).toVector().isEmpty());
        assertEquals(AVector.of(entryOf(1)), mkIterator(1).filter(this::isOdd).toVector());
        assertEquals(AVector.empty(), mkIterator(1).filter(this::isEven).toVector());
        assertEquals(isAscending() ? AVector.of(entryOf(1), entryOf(3)) : AVector.of(entryOf(3), entryOf(1)), mkIterator(1, 2, 3).filter(this::isOdd).toVector());
        assertEquals(AVector.of(entryOf(2)), mkIterator(1, 2, 3).filter(this::isEven).toVector());
    }
    @Test default void testFilterNot () {
        assertTrue(mkIterator().filterNot(this::isOdd).toVector().isEmpty());
        assertEquals(AVector.of(entryOf(1)), mkIterator(1).filterNot(this::isEven).toVector());
        assertEquals(AVector.empty(), mkIterator(1).filterNot(this::isOdd).toVector());
        assertEquals(isAscending() ? AVector.of(entryOf(1), entryOf(3)) : AVector.of(entryOf(3), entryOf(1)),
                mkIterator(1, 2, 3).filterNot(this::isEven).toVector());
        assertEquals(AVector.of(entryOf(2)), mkIterator(1, 2, 3).filterNot(this::isOdd).toVector());
    }

    @Test default void testCollect () {
        assertTrue (mkIterator().collect(x -> true, Function.identity()).toVector().isEmpty());
        assertTrue (mkIterator().collect(x -> false, Function.identity()).toVector().isEmpty());

        assertTrue (mkIterator(1).collect(this::isEven, Function.identity()).toVector().isEmpty());
        assertEquals (AVector.of(entryOf(1)), mkIterator(1).collect(this::isOdd, Function.identity()).toVector());
        assertEquals (AVector.of("1"), mkIterator(1).collect(this::isOdd, x -> String.valueOf(x.getKey())).toVector());

        assertEquals (isAscending() ? AVector.of("1", "3") : AVector.of("3", "1"), mkIterator(1, 2, 3).collect(this::isOdd, x -> String.valueOf(x.getKey())).toVector());
        assertEquals (AVector.of("2"), mkIterator(1, 2, 3).collect(this::isEven, x -> String.valueOf(x.getKey())).toVector());
    }
    @Test default void testCollectFirst () {
        assertTrue (mkIterator().collectFirst(x -> true, Function.identity()).isEmpty());
        assertTrue (mkIterator().collectFirst(x -> false, Function.identity()).isEmpty());

        assertTrue (mkIterator(1).collectFirst(this::isEven, Function.identity()).isEmpty());
        assertEquals (some(entryOf(1)), mkIterator(1).collectFirst(this::isOdd, Function.identity()));
        assertEquals (some("1"), mkIterator(1).collectFirst(this::isOdd, x -> String.valueOf(x.getKey())));

        assertEquals (some(isAscending() ? "1" : "3"), mkIterator(1, 2, 3).collectFirst(this::isOdd, x -> String.valueOf(x.getKey())));
        assertEquals (some("2"), mkIterator(1, 2, 3).collectFirst(this::isEven, x -> String.valueOf(x.getKey())));
    }

    @Test default void testDrop () {
        assertFalse(mkIterator().drop(-1).hasNext());
        assertFalse(mkIterator().drop(0).hasNext());
        assertFalse(mkIterator().drop(1).hasNext());

        assertEquals(AVector.of(entryOf(1)), mkIterator(1).drop(-1).toVector());
        assertEquals(AVector.of(entryOf(1)), mkIterator(1).drop(0).toVector());
        assertEquals(AVector.empty(), mkIterator(1).drop(1).toVector());
        assertFalse(mkIterator(1).drop(2).hasNext());

        assertEquals(v123(), mkIterator(1, 2, 3).drop(-1).toVector());
        assertEquals(v123(), mkIterator(1, 2, 3).drop(0).toVector());
        assertEquals(isAscending() ? AVector.of(entryOf(2), entryOf(3)) : AVector.of(entryOf(2), entryOf(1)),
                mkIterator(1, 2, 3).drop(1).toVector());
        assertEquals(isAscending() ? AVector.of(entryOf(3)) : AVector.of(entryOf(1)),
                mkIterator(1, 2, 3).drop(2).toVector());
        assertEquals(AVector.empty(), mkIterator(1, 2, 3).drop(3).toVector());
        assertFalse(mkIterator(1, 2, 3).drop(4).hasNext());
    }

    @Test default void testFind () {
        assertTrue(mkIterator().find(x -> true).isEmpty());
        assertTrue(mkIterator().find(x -> false).isEmpty());

        assertEquals(some(entryOf(1)), mkIterator(1).find(this::isOdd));
        assertEquals(none(), mkIterator(1).find(this::isEven));

        assertEquals(some(entryOf(isAscending() ? 1 : 3)), mkIterator(1, 2, 3).find(this::isOdd));
        assertEquals(some(entryOf(2)), mkIterator(1, 2, 3).find(this::isEven));
    }
    @Test default void testForall () {
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
    @Test default void testExists () {
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
    @Test default void testCount () {
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

    @Test default void testReduce () {
        assertThrows(NoSuchElementException.class, () -> mkIterator().reduce((a,b) -> null));

        assertEquals(entryOf(1), mkIterator(1).reduce((a,b) -> entryOf(a.getKey()+b.getKey())));
        assertEquals(entryOf(2), mkIterator(2).reduce((a,b) -> entryOf(a.getKey()+b.getKey())));
        assertEquals(entryOf(6), mkIterator(1, 2, 3).reduce((a,b) -> entryOf(a.getKey()+b.getKey())));
    }
    @Test default void testReduceOption () {
        assertEquals(none(), mkIterator().reduceOption((a,b) -> null));

        assertEquals(some(entryOf(1)), mkIterator(1).reduceOption((a,b) -> entryOf(a.getKey()+b.getKey())));
        assertEquals(some(entryOf(2)), mkIterator(2).reduceOption((a,b) -> entryOf(a.getKey()+b.getKey())));
        assertEquals(some(entryOf(6)), mkIterator(1, 2, 3).reduceOption((a,b) -> entryOf(a.getKey()+b.getKey())));
    }
    @Test default void testFold () {
        assertEquals(0, mkIterator().fold(0, (a,b) -> null).intValue());
        assertEquals(99, mkIterator().fold(99, (a,b) -> null).intValue());

        assertEquals(1, mkIterator(1).fold(0, (acc,el) -> acc+el.getKey()).intValue());
        assertEquals(2, mkIterator(2).fold(0, (acc,el) -> acc+el.getKey()).intValue());
        assertEquals(9, mkIterator(2).fold(7, (acc,el) -> acc+el.getKey()).intValue());

        assertEquals(6, mkIterator(1, 2, 3).fold(0, (acc,el) -> acc+el.getKey()).intValue());
        assertEquals(31, mkIterator(1, 2, 3).fold(25, (acc,el) -> acc+el.getKey()).intValue());
    }

    @Test default void testMin () {
        assertThrows(NoSuchElementException.class, () -> mkIterator().min());
        assertThrows(NoSuchElementException.class, () -> mkIterator().min(keyComparator));
        assertThrows(NoSuchElementException.class, () -> mkIterator().min(keyComparator.reversed()));

        assertEquals(entryOf(1), mkIterator(1).min(keyComparator));
        assertEquals(entryOf(1), mkIterator(1).min(keyComparator.reversed()));

        assertThrows(ClassCastException.class, () -> mkIterator(1, 2, 3).min());
        assertEquals(entryOf(1), mkIterator(1, 2, 3).min(keyComparator));
        assertEquals(entryOf(3), mkIterator(1, 2, 3).min(keyComparator.reversed()));
    }
    @Test default void testMax () {
        assertThrows(NoSuchElementException.class, () -> mkIterator().max());
        assertThrows(NoSuchElementException.class, () -> mkIterator().max(keyComparator));
        assertThrows(NoSuchElementException.class, () -> mkIterator().max(keyComparator.reversed()));

        assertEquals(entryOf(1), mkIterator(1).max(keyComparator));
        assertEquals(entryOf(1), mkIterator(1).max(keyComparator.reversed()));

        assertThrows(ClassCastException.class, () -> mkIterator(1, 2, 3).max());
        assertEquals(entryOf(3), mkIterator(1, 2, 3).max(keyComparator));
        assertEquals(entryOf(1), mkIterator(1, 2, 3).max(keyComparator.reversed()));
    }

    @Test default void testMkString () {
        assertEquals("", mkIterator().mkString("|"));
        assertEquals("<>", mkIterator().mkString("<", "|", ">"));

        assertEquals("1=3", mkIterator(1).mkString("|"));
        assertEquals("<1=3>", mkIterator(1).mkString("<", "|", ">"));

        if (isAscending()) {
            assertEquals("1=3|2=5|3=7", mkIterator(1, 2, 3).mkString("|"));
            assertEquals("<1=3|2=5|3=7>", mkIterator(1, 2, 3).mkString("<", "|", ">"));
        }
        else {
            assertEquals("3=7|2=5|1=3", mkIterator(1, 2, 3).mkString("|"));
            assertEquals("<3=7|2=5|1=3>", mkIterator(1, 2, 3).mkString("<", "|", ">"));
        }
    }

    @Test default void testConcatToThis () {
        assertEquals(AVector.empty(), mkIterator().concat(mkIterator()).toVector());

        assertEquals(AVector.of(entryOf(1)), mkIterator(1).concat(mkIterator()).toVector());
        assertEquals(AVector.of(entryOf(1)), mkIterator().concat(mkIterator(1)).toVector());
        assertEquals(AVector.of(entryOf(1), entryOf(1)), mkIterator(1).concat(mkIterator(1)).toVector());

        assertEquals(v123(), mkIterator(1, 2, 3).concat(mkIterator()).toVector());
        assertEquals(v123(), mkIterator().concat(mkIterator(1, 2, 3)).toVector());
        assertEquals(v123().append(entryOf(1)), mkIterator(1, 2, 3).concat(mkIterator(1)).toVector());
        assertEquals(v123().prepend(entryOf(1)), mkIterator(1).concat(mkIterator(1, 2, 3)).toVector());
        //noinspection unchecked
        assertEquals(v123().concat(v123()), mkIterator(1, 2, 3).concat(mkIterator(1, 2, 3)).toVector());
    }

    @Test default void testStaticConcat () {
        assertEquals(AVector.empty(), AIterator.concat(mkIterator(), mkIterator()).toVector());

        assertEquals(AVector.of(entryOf(1)), AIterator.concat(mkIterator(1), mkIterator()).toVector());
        assertEquals(AVector.of(entryOf(1)), AIterator.concat(mkIterator(), mkIterator(1)).toVector());
        assertEquals(AVector.of(entryOf(1), entryOf(1)), AIterator.concat(mkIterator(1), mkIterator(1)).toVector());

        assertEquals(v123(), AIterator.concat(mkIterator(1, 2, 3), mkIterator()).toVector());
        assertEquals(v123(), AIterator.concat(mkIterator(), mkIterator(1, 2, 3)).toVector());
        assertEquals(v123().append(entryOf(1)), AIterator.concat(mkIterator(1, 2, 3), mkIterator(1)).toVector());
        assertEquals(v123().prepend(entryOf(1)), AIterator.concat(mkIterator(1), mkIterator(1, 2, 3)).toVector());
        //noinspection unchecked
        assertEquals(v123().concat(v123()), AIterator.concat(mkIterator(1, 2, 3), mkIterator(1, 2, 3)).toVector());

        //noinspection unchecked
        assertEquals(v123().concat(v123()).append(entryOf(1)), AIterator.concat(mkIterator(1, 2, 3), mkIterator(), mkIterator(1, 2, 3), mkIterator(1)).toVector());
    }
}
