package com.ajjpj.acollections.immutable;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import scala.collection.Iterator;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 3, time=10, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 10, time=10, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(3)
public class AHashSetBenchmark {
    private static final int size = 100_000;
    private static final int numIter = 10_000_000;


    @Benchmark
    public void testModifyScala(Blackhole bh) {
        final Random rand = new Random(12345);
        scala.collection.immutable.HashSet<Integer> m = new scala.collection.immutable.HashSet<>();

        for(int i=0; i<numIter; i++) {
            final int key = rand.nextInt(size);
            final boolean add = rand.nextBoolean();

            if(add)
                m=m.$plus(key);
            else
                m=m.$minus(key);
        }
        bh.consume(m);
    }

    @Benchmark
    public void testModifyDexx(Blackhole bh) {
        final Random rand = new Random(12345);
        com.github.andrewoma.dexx.collection.HashSet<Integer> m = com.github.andrewoma.dexx.collection.HashSet.empty();

        for(int i=0; i<numIter; i++) {
            final int key = rand.nextInt(size);
            final boolean add = rand.nextBoolean();

            if(add)
                m=m.add(key);
            else
                m=m.remove(key);
        }
        bh.consume(m);
    }
    @Benchmark
    public void testModifyAHashSet(Blackhole bh) {
        final Random rand = new Random(12345);
        AHashSet<Integer> m = AHashSet.empty();

        for(int i=0; i<numIter; i++) {
            final int key = rand.nextInt(size);
            final boolean add = rand.nextBoolean();

            if(add)
                m=m.plus(key);
            else
                m=m.minus(key);
        }
        bh.consume(m);
    }

    @Benchmark
    public void testIterateScala(Blackhole bh) {
        scala.collection.immutable.HashSet<Integer> m = new scala.collection.immutable.HashSet<>();

        for(int i=0; i<size; i++) { //TODO use builder (here and for the Java implementations)
            m=m.$plus(i);
        }

        int sum=0;
        final Iterator<Integer> it = m.iterator();
        while (it.hasNext()) {
            sum += it.next();
        }
        bh.consume(sum);
    }

    @Benchmark
    public void testIterateDexx(Blackhole bh) {
        com.github.andrewoma.dexx.collection.HashSet<Integer> m = com.github.andrewoma.dexx.collection.HashSet.empty();

        for(int i=0; i<size; i++) {
            m=m.add(i);
        }
        int sum=0;
        for (Integer el: m) {
            sum += el;
        }
        bh.consume(sum);
    }
    @Benchmark
    public void testIterateAHashSet(Blackhole bh) {
        AHashSet<Integer> m = AHashSet.empty();

        for(int i=0; i<size; i++) {
            m=m.plus(i);
        }
        int sum=0;
        for (Integer el: m) {
            sum += el;
        }
        bh.consume(sum);
    }
}
