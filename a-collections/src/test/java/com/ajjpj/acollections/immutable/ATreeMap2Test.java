package com.ajjpj.acollections.immutable;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ATreeMap2Test {
    final int size = 100_000;
    final int numIter = 10_000_000;

    private Map<Integer, Integer> createJu() {
        final Random rand = new Random(12345);
        final Map<Integer, Integer> result = new TreeMap<>();

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
    private ATreeMap<Integer, Integer> createA() {
        final Random rand = new Random(12345);
        ATreeMap<Integer, Integer> result = ATreeMap.empty(Comparator.<Integer>naturalOrder());

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
        final ATreeMap<Integer, Integer> aMap = createA();

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
}
