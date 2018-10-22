package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.AListTests;
import com.ajjpj.acollections.util.AEquality;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;


public class ARangeTest implements AListTests {
    @Override public boolean isImmutable () {
        return true;
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


    @Override public Iterable<Variant> variants () {
        //noinspection ArraysAsListWithZeroOrOneArgument
        return Arrays.asList(
                new RangeVariant()
        );
    }

    static class RangeVariant extends Variant {
        public RangeVariant () {
            super(null, AVector.of(1, 2, 3), false);
        }

        @Override public void checkEquality (ACollection<Integer> coll) {
            assertEquals (coll.equality(), AEquality.EQUALS);
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
            if (v.equals(Arrays.asList(2, 6))) return ARange.create(2, 7, 4);
            if (v.equals(Arrays.asList(2, 3))) return ARange.create(2, 4);
            if (v.equals(Arrays.asList(2, 1))) return ARange.create(2, 0);
            if (v.equals(Arrays.asList(1, 2, 3))) return ARange.create(1, 4);
            if (v.equals(Arrays.asList(2, 4, 6))) return ARange.create(2, 8, 2);
            if (v.equals(Arrays.asList(1, 2, 3, 4))) return ARange.create(1, 5);
            if (v.equals(Arrays.asList(2, 3, 4, 5, 6, 7))) return ARange.create(2, 8);

            throw new IllegalArgumentException("not supported as a range: " + v);
        }
    }
}
