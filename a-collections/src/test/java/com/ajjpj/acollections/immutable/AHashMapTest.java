package com.ajjpj.acollections.immutable;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class AHashMapTest {
    final int size = 100_000;
    final int numIter = 10_000_000;

    private Map<Integer, Integer> createJu() {
        final Random rand = new Random(12345);
        final Map<Integer, Integer> result = new HashMap<>();

        for(int i=0; i<numIter; i++) {
            final int key = rand.nextInt(size);
            final boolean add = rand.nextBoolean();

            if(add)
                result.put(key, key);
            else
                result.remove(key);
        }
        return result;
    }
    private AHashMap<Integer, Integer> createA() {
        final Random rand = new Random(12345);
        AHashMap<Integer, Integer> result = AHashMap.empty();

        for(int i=0; i<numIter; i++) {
            final int key = rand.nextInt(size);
            final boolean add = rand.nextBoolean();

            if(add)
                result = result.updated(key, key);
            else
                result = result.removed(key);
        }
        return result;
    }

    @Test public void testAddRemove() {
        final Map<Integer, Integer> juMap = createJu();
        final AHashMap<Integer, Integer> aMap = createA();

        assertEquals(juMap.size(), aMap.size());

        for (int i=0; i<size; i++) {
            assertEquals(juMap.get(i), aMap.get(i));
        }

        // test iteration
        final HashMap<Integer,Integer> juMap2 = new HashMap<>();
        for (Map.Entry<Integer, Integer> o: aMap) {
            juMap2.put(o.getKey(), o.getValue());
        }
        assertEquals(juMap, juMap2);
    }

    @Test public void testCollision() {
        AHashMap<IntWithCollision, Integer> aMap = AHashMap.empty();
        for (int i=0; i<10; i++) {
            aMap = aMap.updated(new IntWithCollision(i), 2*i);
        }
        for (int i=0; i<10; i++) {
            aMap = aMap.updated(new IntWithCollision(i), i);
        }
        assertEquals (10, aMap.size());
        for (int i=0; i<10; i++) {
            assertEquals(i, aMap.get(new IntWithCollision(i)).intValue());
        }

        // test iteration
        final HashMap<Integer,Integer> juMap = new HashMap<>();
        for (Map.Entry<IntWithCollision, Integer> o: aMap) {
            juMap.put(o.getKey().i, o.getValue());
        }

        assertEquals(10, juMap.size());
        for (int i=0; i<10; i++) {
            assertEquals(i, juMap.get(i).intValue());
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