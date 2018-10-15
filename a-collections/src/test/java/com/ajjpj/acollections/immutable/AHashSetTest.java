package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ASet;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class AHashSetTest {
    final int size = 100_000;
    final int numIter = 10_000_000;

    private Set<Integer> createJu() {
        final Random rand = new Random(12345);
        final Set<Integer> result = new HashSet<>();

        for(int i=0; i<numIter; i++) {
            final int key = rand.nextInt(size);
            final boolean add = rand.nextBoolean();

            if(add)
                result.add(key);
            else
                result.remove(key);
        }
        return result;
    }
    private AHashSet<Integer> createA() {
        final Random rand = new Random(12345);
        AHashSet<Integer> result = AHashSet.empty();

        for(int i=0; i<numIter; i++) {
            final int key = rand.nextInt(size);
            final boolean add = rand.nextBoolean();

            if(add)
                result = result.added(key);
            else
                result = result.removed(key);
        }
        return result;
    }

    @Test public void testAddRemove() {
        final Set<Integer> juSet = createJu();
        final ASet<Integer> aSet = createA();

        assertEquals(juSet.size(), aSet.size());

        for (int i=0; i<size; i++) {
            assertEquals(juSet.contains(i), aSet.contains(i));
        }

        // test iteration
        final Set<Integer> juSet2 = new HashSet<>();
        for (Integer o: aSet) {
            //noinspection UseBulkOperation
            juSet2.add(o);
        }
        assertEquals(juSet, juSet2);
    }

    @Test public void testCollision() {
        AHashSet<IntWithCollision> aMap = AHashSet.empty();
        for (int i=0; i<10; i++) {
            aMap = aMap.added(new IntWithCollision(i));
        }
        for (int i=0; i<10; i++) {
            aMap = aMap.added(new IntWithCollision(i));
        }
        assertEquals (10, aMap.size());
        for (int i=0; i<10; i++) {
            assertTrue(aMap.contains(new IntWithCollision(i)));
        }
        for (int i=10; i<20; i++) {
            assertFalse(aMap.contains(new IntWithCollision(i)));
        }

        // test iteration
        final HashSet<Integer> juSet = new HashSet<>();
        for (IntWithCollision o: aMap) {
            //noinspection UseBulkOperation
            juSet.add(o.i);
        }

        assertEquals(10, juSet.size());
        for (int i=0; i<10; i++) {
            assertTrue(juSet.contains(i));
        }
    }

    static class IntWithCollision {
        final int i;

        public IntWithCollision (int i) {
            this.i = i;
        }

        @Override public boolean equals (Object obj) {
            return obj instanceof IntWithCollision && ((IntWithCollision) obj).i == i;
        }

        @Override public int hashCode () {
            return 1;
        }

        @Override public String toString () {
            return String.valueOf(i);
        }
    }
}
