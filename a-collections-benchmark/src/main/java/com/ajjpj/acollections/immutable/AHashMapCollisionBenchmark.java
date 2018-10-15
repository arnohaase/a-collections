package com.ajjpj.acollections.immutable;

import com.github.andrewoma.dexx.collection.Pair;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import scala.Tuple2;
import scala.collection.Iterator;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 3, time=10, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 10, time=10, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Fork(3)
public class AHashMapCollisionBenchmark {
    private static final int size = 100_000;
    private static final int numIter = 10_000_000;

    @Benchmark
    public void testScala(Blackhole bh) {
        scala.collection.immutable.HashMap<IntWithCollision,Integer> m = new scala.collection.immutable.HashMap<>();

        for (int i=0; i<10; i++) {
            m = m.$plus(new Tuple2<>(new IntWithCollision(i), 2*i));
        }
        for (int i=0; i<10; i++) {
            m = m.$plus(new Tuple2<>(new IntWithCollision(i), i));
        }
        for (int i=0; i<10; i++) {
            m.get(new IntWithCollision(i));
        }

        int sum=0;
        final Iterator<Tuple2<IntWithCollision, Integer>> it = m.iterator();
        while (it.hasNext()) {
            sum += it.next()._2;
        }
        bh.consume(sum);
    }

    @Benchmark
    public void testDexx(Blackhole bh) {
        com.github.andrewoma.dexx.collection.HashMap<IntWithCollision,Integer> m = new com.github.andrewoma.dexx.collection.HashMap<>();

        for (int i=0; i<10; i++) {
            m = m.put(new IntWithCollision(i), 2*i);
        }
        for (int i=0; i<10; i++) {
            m = m.put(new IntWithCollision(i), i);
        }
        for (int i=0; i<10; i++) {
            m.get(new IntWithCollision(i));
        }

        int sum=0;
        for (Pair<IntWithCollision, Integer> el: m) {
            sum += el.component2();
        }
        bh.consume(sum);
    }
    @Benchmark
    public void testAHashMap(Blackhole bh) {
        AHashMap<IntWithCollision,Integer> m = AHashMap.empty();

        for (int i=0; i<10; i++) {
            m = m.updated(new IntWithCollision(i), 2*i);
        }
        for (int i=0; i<10; i++) {
            m = m.updated(new IntWithCollision(i), i);
        }
        for (int i=0; i<10; i++) {
            m.get(new IntWithCollision(i));
        }

        int sum=0; //TODO split into separate benchmarks for update, random access, iteration
        for (Map.Entry<IntWithCollision, Integer> el: m) {
            sum += el.getValue();
        }
        bh.consume(sum);
    }

    static class IntWithCollision {
        final int i;

        IntWithCollision (int i) {
            this.i = i;
        }

        @Override public boolean equals (Object obj) {
            return obj instanceof IntWithCollision && ((IntWithCollision) obj).i == i;
        }

        @Override public int hashCode () {
            return 1;
        }
    }
}
