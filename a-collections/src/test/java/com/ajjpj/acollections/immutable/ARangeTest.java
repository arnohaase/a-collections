package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.AListTests;
import com.ajjpj.acollections.TestHelpers;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;


public class ARangeTest implements AListTests {
    @Override public boolean isImmutable () {
        return true;
    }

    @Override @Test public void testStaticFactories() {
        // nothing to be done - no static factories
    }

    @Override @Test public void testSerDeser () {
        assertEquals(ARange.empty(), TestHelpers.serDeser(ARange.empty()));
        assertNotSame(ARange.empty(), TestHelpers.serDeser(ARange.empty()));

        assertEquals(ARange.create(1, 5), TestHelpers.serDeser(ARange.create(1, 5)));
        assertEquals(ARange.create(1, 99, 4), TestHelpers.serDeser(ARange.create(1, 99, 4)));
        assertEquals(ARange.create(200, 15, -7), TestHelpers.serDeser(ARange.create(200, 15, -7)));
    }

    @Override @Test public void testSize () {
        AListTests.super.testSize();

        assertEquals(1, ARange.create(1, 2).size());
        assertEquals(5, ARange.create(0, 5).size());
        assertEquals(2, ARange.create(0, 4, 2).size());

        assertEquals(2, ARange.create(1, 4, 2).size());
        assertEquals(3, ARange.create(0, 5, 2).size());
        assertEquals(3, ARange.create(0, 7, 3).size());

        assertEquals(4, ARange.create(4, 0, -1).size());
        assertEquals(2, ARange.create(4, 0, -2).size());
        assertEquals(2, ARange.create(4, 1, -2).size());
        assertEquals(2, ARange.create(4, 0, -3).size());
    }

    @Override @Test public void testIterator() {
        AListTests.super.testIterator();

        assertEquals(AVector.empty(), ARange.empty().iterator().toVector());
        assertEquals(AVector.of(2, 4), ARange.create(2, 5, 2).iterator().toVector());
        assertEquals(AVector.of(1, 4), ARange.create(1, 6, 3).iterator().toVector());
        assertEquals(AVector.of(4, 2), ARange.create(4, 1, -2).iterator().toVector());
        assertEquals(AVector.of(6, 3), ARange.create(6, 1, -3).iterator().toVector());
    }

    @Override @Test public void testMin() {
        assertThrows(NoSuchElementException.class, () -> ARange.empty().min());

        assertEquals(1, ARange.create(1, 2).min().intValue());
        assertEquals(1, ARange.create(1, 7).min().intValue());
        assertEquals(1, ARange.create(1, 6, 2).min().intValue());

        assertEquals(1, ARange.create(1, 0, -1).min().intValue());
        assertEquals(1, ARange.create(3, 0, -1).min().intValue());
        assertEquals(2, ARange.create(4, 0, -2).min().intValue());

        assertEquals(5, ARange.create(1, 6, 2).min(Comparator.<Integer>naturalOrder().reversed()).intValue());
        assertEquals(4, ARange.create(4, 0, -2).min(Comparator.<Integer>naturalOrder().reversed()).intValue());
    }
    @Override @Test public void testMax() {
        assertThrows(NoSuchElementException.class, () -> ARange.empty().max());

        assertEquals(1, ARange.create(1, 2).max().intValue());
        assertEquals(6, ARange.create(1, 7).max().intValue());
        assertEquals(3, ARange.create(1, 5, 2).max().intValue());

        assertEquals(1, ARange.create(1, 0, -1).max().intValue());
        assertEquals(3, ARange.create(3, 0, -1).max().intValue());
        assertEquals(4, ARange.create(4, 0, -2).max().intValue());

        assertEquals(1, ARange.create(1, 5, 2).max(Comparator.<Integer>naturalOrder().reversed()).intValue());
        assertEquals(2, ARange.create(4, 0, -2).max(Comparator.<Integer>naturalOrder().reversed()).intValue());
    }

    @Override @Test public void testReverse() {
        AListTests.super.testReverse();

        assertEquals(ARange.create(2, 7, 2), ARange.create(6, 1, -2).reverse());
        assertEquals(ARange.create(6, 1, -2), ARange.create(2, 7, 2).reverse());
    }

    @Override @Test public void testToSortedSet() {
        assertEquals(ATreeSet.empty(), ARange.empty().toSortedSet());
        assertEquals(ATreeSet.of(1), ARange.create(1, 2).toSortedSet());
        assertEquals(ATreeSet.of(1, 2, 3, 4), ARange.create(1, 5).toSortedSet());
        assertEquals(ATreeSet.of(1, 2, 3, 4), ARange.create(4, 0).toSortedSet());
    }

    @Override @Test public void testIndexOf() {
        assertEquals(-1, ARange.empty().indexOf(1));

        assertEquals(0, ARange.create(1, 2).indexOf(1));
        assertEquals(0, ARange.create(1, 3).indexOf(1));
        assertEquals(1, ARange.create(1, 3).indexOf(2));
        assertEquals(-1, ARange.create(1, 3).indexOf(3));

        assertEquals(0, ARange.create(1, 9, 3).indexOf(1));
        assertEquals(1, ARange.create(1, 9, 3).indexOf(4));
        assertEquals(-1, ARange.create(1, 7, 3).indexOf(7));
        assertEquals(2, ARange.create(1, 8, 3).indexOf(7));
        assertEquals(-1, ARange.create(1, 10, 3).indexOf(2));
        assertEquals(-1, ARange.create(1, 10, 3).indexOf(0));
        assertEquals(-1, ARange.create(1, 10, 3).indexOf(-2));
        assertEquals(-1, ARange.create(1, 10, 3).indexOf(10));

        assertEquals(0, ARange.create(2, 1).indexOf(2));
        assertEquals(0, ARange.create(3, 1).indexOf(3));
        assertEquals(1, ARange.create(3, 1).indexOf(2));
        assertEquals(-1, ARange.create(3, 1).indexOf(1));

        assertEquals(0, ARange.create(9, 3, -3).indexOf(9));
        assertEquals(1, ARange.create(9, 3, -3).indexOf(6));
        assertEquals(-1, ARange.create(9, 3, -3).indexOf(3));
        assertEquals(2, ARange.create(9, 2, -3).indexOf(3));
        assertEquals(-1, ARange.create(9, 2, -3).indexOf(12));
        assertEquals(-1, ARange.create(9, 2, -3).indexOf(0));
    }

    @Override @Test public void testLastIndexOf() {
        assertEquals(-1, ARange.empty().lastIndexOf(1));

        assertEquals(0, ARange.create(1, 2).lastIndexOf(1));
        assertEquals(0, ARange.create(1, 3).lastIndexOf(1));
        assertEquals(1, ARange.create(1, 3).lastIndexOf(2));
        assertEquals(-1, ARange.create(1, 3).lastIndexOf(3));

        assertEquals(0, ARange.create(1, 9, 3).lastIndexOf(1));
        assertEquals(1, ARange.create(1, 9, 3).lastIndexOf(4));
        assertEquals(-1, ARange.create(1, 7, 3).lastIndexOf(7));
        assertEquals(2, ARange.create(1, 8, 3).lastIndexOf(7));
        assertEquals(-1, ARange.create(1, 10, 3).lastIndexOf(2));
        assertEquals(-1, ARange.create(1, 10, 3).lastIndexOf(0));
        assertEquals(-1, ARange.create(1, 10, 3).lastIndexOf(-2));
        assertEquals(-1, ARange.create(1, 10, 3).lastIndexOf(10));

        assertEquals(0, ARange.create(2, 1).lastIndexOf(2));
        assertEquals(0, ARange.create(3, 1).lastIndexOf(3));
        assertEquals(1, ARange.create(3, 1).lastIndexOf(2));
        assertEquals(-1, ARange.create(3, 1).lastIndexOf(1));

        assertEquals(0, ARange.create(9, 3, -3).lastIndexOf(9));
        assertEquals(1, ARange.create(9, 3, -3).lastIndexOf(6));
        assertEquals(-1, ARange.create(9, 3, -3).lastIndexOf(3));
        assertEquals(2, ARange.create(9, 2, -3).lastIndexOf(3));
        assertEquals(-1, ARange.create(9, 2, -3).lastIndexOf(12));
        assertEquals(-1, ARange.create(9, 2, -3).lastIndexOf(0));
    }

    @Override @Test public void testDropWhile() {
        assertEquals(AVector.empty(), ARange.empty().dropWhile(x -> x<2));

        assertEquals(AVector.of(2), ARange.create(2, 3).dropWhile(x -> x<2));
        assertEquals(AVector.of(2), ARange.create(1, 3).dropWhile(x -> x<2));
        assertEquals(AVector.of(1, 2, 3), ARange.create(1, 4).dropWhile(x -> x>1));
        assertEquals(AVector.of(6, 7, 8, 9), ARange.create(1, 10).dropWhile(x -> x<6));
        assertEquals(AVector.empty(), ARange.create(1, 10).dropWhile(x -> x<20));
        assertEquals(AVector.of(7, 9), ARange.create(1, 10, 2).dropWhile(x -> x<6));

        assertEquals(AVector.of(2), ARange.create(2, 1).dropWhile(x -> x<2));
        assertEquals(AVector.of(3, 2, 1), ARange.create(3, 0).dropWhile(x -> x<3));
    }
    @Override @Test public void testTakeWhile() {
        assertEquals(AVector.empty(), ARange.empty().takeWhile(x -> x<2));

        assertEquals(AVector.of(2), ARange.create(2, 3).takeWhile(x -> x<3));
        assertEquals(AVector.empty(), ARange.create(2, 3).takeWhile(x -> x<2));
        assertEquals(AVector.of(1, 2, 3, 4), ARange.create(1, 10).takeWhile(x -> x<5));
        assertEquals(AVector.of(1, 3), ARange.create(1, 10, 2).takeWhile(x -> x<5));
        assertEquals(AVector.empty(), ARange.create(1, 10).takeWhile(x -> x>1));

        assertEquals(AVector.of(5, 4), ARange.create(5, 1).takeWhile(x -> x>3));
        assertEquals(AVector.of(5), ARange.create(5, 1, -2).takeWhile(x -> x>3));
        assertEquals(AVector.empty(), ARange.create(5, 1).takeWhile(x -> x<5));
    }

    @Override public Iterable<Variant> variants () {
        //noinspection ArraysAsListWithZeroOrOneArgument
        return Arrays.asList(
                new RangeVariant()
        );
    }

    static class RangeVariant extends Variant {
        public RangeVariant () {
            super(null, AVector.of(1, 2, 3));
        }

        @Override public ACollection<Integer> mkColl (Integer... values) {
            final List<Integer> v = Arrays.asList(values);

            if (v.isEmpty()) return ARange.empty();
            //noinspection ArraysAsListWithZeroOrOneArgument
            if (v.equals(Arrays.asList(0))) return ARange.create(0, 1);
            if (v.equals(Arrays.asList(1))) return ARange.create(1, 2);
            if (v.equals(Arrays.asList(2))) return ARange.create(2, 3);
            if (v.equals(Arrays.asList(1, 2))) return ARange.create(1, 3);
            if (v.equals(Arrays.asList(1, 3))) return ARange.create(1, 4, 2);
            if (v.equals(Arrays.asList(2, 4))) return ARange.create(2, 5, 2);
            if (v.equals(Arrays.asList(2, 6))) return ARange.create(2, 7, 4);
            if (v.equals(Arrays.asList(2, 3))) return ARange.create(2, 4);
            if (v.equals(Arrays.asList(2, 1))) return ARange.create(2, 0);
            if (v.equals(Arrays.asList(1, 2, 3))) return ARange.create(1, 4);
            if (v.equals(Arrays.asList(3, 2, 1))) return ARange.create(3, 0);
            if (v.equals(Arrays.asList(2, 4, 6))) return ARange.create(2, 8, 2);
            if (v.equals(Arrays.asList(1, 2, 3, 4))) return ARange.create(1, 5);
            if (v.equals(Arrays.asList(2, 3, 4, 5, 6, 7))) return ARange.create(2, 8);

            throw new IllegalArgumentException("not supported as a range: " + v);
        }
    }
}
