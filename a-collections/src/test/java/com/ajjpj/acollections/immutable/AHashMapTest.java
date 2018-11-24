package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AMap;
import com.ajjpj.acollections.AMapTests;
import com.ajjpj.acollections.TestHelpers;
import com.ajjpj.acollections.mutable.AMutableMapWrapper;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


public class AHashMapTest implements AMapTests {
    @Override @Test public void testSerDeser () {
        assertEquals(AHashMap.empty(), TestHelpers.serDeser(AHashMap.empty()));
        assertEquals(AHashMap.empty().plus(1,2), TestHelpers.serDeser(AHashMap.empty().plus(1,2)));
        assertEquals(AHashMap.empty().plus(1,2).plus(3,4).plus(5,6), TestHelpers.serDeser(AHashMap.empty().plus(1,2).plus(3,4).plus(5,6)));
    }

    @Override public Iterable<Variant> variants () {
        return Collections.singletonList(
                new Variant(AHashMap::builder, null)
        );
    }

    @Override @Test public void testStaticFactories() {
        assertTrue(AHashMap.empty().isEmpty());

        AMap<Integer,String> expected = AMap.empty();
        assertEquals(expected, AHashMap.of());
        expected = expected.plus(1, "1");
        assertEquals(expected, AHashMap.of(1,"1"));
        expected = expected.plus(2, "2");
        assertEquals(expected, AHashMap.of(1,"1", 2, "2"));
        expected = expected.plus(3, "3");
        assertEquals(expected, AHashMap.of(1,"1", 2, "2", 3, "3"));
        expected = expected.plus(4, "4");
        assertEquals(expected, AHashMap.of(1,"1", 2, "2", 3, "3", 4, "4"));

        assertEquals (AMap.of(5, "5"), AHashMap.from(Collections.singletonList(new AbstractMap.SimpleImmutableEntry<>(5, "5"))));
        assertEquals (AMap.of(5, "5"), AHashMap.fromIterator(Collections.singletonList(new AbstractMap.SimpleImmutableEntry<>(5, "5")).iterator()));

        assertEquals (AMap.of(5, "5"), AHashMap.ofEntries(Collections.singletonList(new AbstractMap.SimpleImmutableEntry<>(5, "5"))));

        assertEquals(expected, AHashMap.fromMap(expected));
    }

    @Test void testStaticFactoriesInAMap() {
        assertTrue(AMap.empty().isEmpty());

        AMap<Integer,String> expected = AMap.empty();
        assertEquals(expected, AMap.of());
        expected = expected.plus(1, "1");
        assertEquals(expected, AMap.of(1,"1"));
        expected = expected.plus(2, "2");
        assertEquals(expected, AMap.of(1,"1", 2, "2"));
        expected = expected.plus(3, "3");
        assertEquals(expected, AMap.of(1,"1", 2, "2", 3, "3"));
        expected = expected.plus(4, "4");
        assertEquals(expected, AMap.of(1,"1", 2, "2", 3, "3", 4, "4"));

        assertEquals (AMap.of(5, "5"), AMap.from(Collections.singletonList(new AbstractMap.SimpleImmutableEntry<>(5, "5"))));
        assertEquals (AMap.of(5, "5"), AMap.fromIterator(Collections.singletonList(new AbstractMap.SimpleImmutableEntry<>(5, "5")).iterator()));

        assertEquals (AMap.of(5, "5"), AMap.ofEntries(Collections.singletonList(new AbstractMap.SimpleImmutableEntry<>(5, "5"))));

        assertEquals(expected, AMap.fromMap(expected));
    }

    @Test void testToMap() {
        // this test is here rather than in ACollectionOpsTests because those tests work on collections of Integer, and the functionality
        //  is generic enough to be sufficiently tested by just using AVector
        assertEquals(AHashMap.empty(), AVector.empty().toMap());
        assertEquals(AHashMap.empty().plus(1, 2), AVector.of(new AbstractMap.SimpleImmutableEntry<>(1, 2)).toMap());
        assertEquals(
                AHashMap.empty().plus(1, 2).plus(3, 4).plus(5, 6),
                AVector.of(new AbstractMap.SimpleImmutableEntry<>(1, 2), new AbstractMap.SimpleImmutableEntry<>(3, 4), new AbstractMap.SimpleImmutableEntry<>(5, 6)).toMap());
    }


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
                result = result.plus(key, key);
            else
                result = result.minus(key);
        }
        return result;
    }

    @Test void testAddRemove() {
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

    @Test void testCollision() {
        AHashMap<IntWithCollision, Integer> aMap = AHashMap.empty();
        for (int i=0; i<10; i++) {
            aMap = aMap.plus(new IntWithCollision(i), 2*i);
        }
        for (int i=0; i<10; i++) {
            aMap = aMap.plus(new IntWithCollision(i), i);
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
