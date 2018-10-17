package com.ajjpj.acollections.immutable;

import com.github.andrewoma.dexx.collection.Pair;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import scala.Tuple2;
import scala.collection.Iterator;
import scala.math.LowPriorityOrderingImplicits;

import java.util.Comparator;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 1, time=10, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 3, time=5, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(1)
public class ATreeMapBenchmark {
    private static final int size = 100_000;
    private static final int numIter = 10_000_000;


    @Benchmark
    public void testModifyScala(Blackhole bh) {
        final Random rand = new Random(12345);
        scala.collection.immutable.TreeMap<Integer,Integer> m = new scala.collection.immutable.TreeMap<>(new LowPriorityOrderingImplicits(){}.comparatorToOrdering(Comparator.<Integer>naturalOrder()));

        for(int i=0; i<numIter; i++) {
            final int key = rand.nextInt(size);
            final boolean add = rand.nextBoolean();

            if(add)
                m=m.$plus(new Tuple2<>(key, key));
            else
                m=m.$minus(key);
        }
        bh.consume(m);
    }

//    @Benchmark
    public void testModifyDexx(Blackhole bh) {
        final Random rand = new Random(12345);
        com.github.andrewoma.dexx.collection.TreeMap<Integer,Integer> m = new com.github.andrewoma.dexx.collection.TreeMap<>();

        for(int i=0; i<numIter; i++) {
            final int key = rand.nextInt(size);
            final boolean add = rand.nextBoolean();

            if(add)
                m=m.put(key, key);
            else
                m=m.remove(key);
        }
        bh.consume(m);
    }
//    @Benchmark
    public void testModifyATreeMap(Blackhole bh) {
        final Random rand = new Random(12345);
        ATreeMap<Integer,Integer> m = ATreeMap.empty(Comparator.<Integer>naturalOrder());

        for(int i=0; i<numIter; i++) {
            final int key = rand.nextInt(size);
            final boolean add = rand.nextBoolean();

            if(add)
                m=m.updated(key, key);
            else
                m=m.removed(key);
        }
        bh.consume(m);
    }

    @Benchmark
    public void testModifyATreeMap2(Blackhole bh) {
        final Random rand = new Random(12345);
        ATreeMap2<Integer,Integer> m = ATreeMap2.empty(Comparator.<Integer>naturalOrder());

        for(int i=0; i<numIter; i++) {
            final int key = rand.nextInt(size);
            final boolean add = rand.nextBoolean();

            if(add)
                m=m.updated(key, key);
            else
                m=m.removed(key);
        }
        bh.consume(m);
    }
    @Benchmark
    public void testModifyATreeMap3(Blackhole bh) {
        final Random rand = new Random(12345);
        ATreeMap3<Integer,Integer> m = ATreeMap3.empty(Comparator.<Integer>naturalOrder());

        for(int i=0; i<numIter; i++) {
            final int key = rand.nextInt(size);
            final boolean add = rand.nextBoolean();

            if(add)
                m=m.updated(key, key);
            else
                m=m.removed(key);
        }
        bh.consume(m);
    }

//    @Benchmark
    public void testIterateScala(Blackhole bh) {
        scala.collection.immutable.TreeMap<Integer,Integer> m = new scala.collection.immutable.TreeMap<>(new LowPriorityOrderingImplicits(){}.comparatorToOrdering(Comparator.<Integer>naturalOrder()));

        for(int i=0; i<size; i++) { //TODO use builder (here and for the Java implementations)
            m=m.$plus(new Tuple2<>(i, i));
        }

        int sum=0;
        final Iterator<Tuple2<Integer, Integer>> it = m.iterator();
        while (it.hasNext()) {
            sum += it.next()._2;
        }
        bh.consume(sum);
    }

//    @Benchmark
    public void testIterateDexx(Blackhole bh) {
        com.github.andrewoma.dexx.collection.TreeMap<Integer,Integer> m = new com.github.andrewoma.dexx.collection.TreeMap<>();

        for(int i=0; i<size; i++) {
            m=m.put(i, i);
        }
        int sum=0;
        for (Pair<Integer, Integer> el: m) {
            sum += el.component2();
        }
        bh.consume(sum);
    }
//    @Benchmark
    public void testIterateATreeMap(Blackhole bh) {
        ATreeMap<Integer,Integer> m = ATreeMap.empty(Comparator.<Integer>naturalOrder());

        for(int i=0; i<size; i++) {
            m=m.updated(i, i);
        }
        int sum=0;
        for (Map.Entry<Integer, Integer> el: m) {
            sum += el.getValue();
        }
        bh.consume(sum);
    }
//    @Benchmark
    public void testIterateATreeMap2(Blackhole bh) {
        ATreeMap2<Integer,Integer> m = ATreeMap2.empty(Comparator.<Integer>naturalOrder());

        for(int i=0; i<size; i++) {
            m=m.updated(i, i);
        }
        int sum=0;
        for (Map.Entry<Integer, Integer> el: m) {
            sum += el.getValue();
        }
        bh.consume(sum);
    }
}
