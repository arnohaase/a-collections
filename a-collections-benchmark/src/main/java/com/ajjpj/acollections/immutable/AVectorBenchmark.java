package com.ajjpj.acollections.immutable;

import com.github.andrewoma.dexx.collection.Builder;
import com.github.andrewoma.dexx.collection.Vector;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Warmup(iterations = 3, time=10, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 3, time=8, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Fork(2)
public class AVectorBenchmark {
    private static final int numElements = 10_000_000;

//    @Benchmark
    public void testPrependScala(Blackhole bh) {
        scala.collection.immutable.Vector<Integer> v = (scala.collection.immutable.Vector)scala.collection.immutable.Vector.empty();
        for (int i=0; i<numElements; i++) {
            v = v.appendFront(i);
        }
        bh.consume(v);
    }

//    @Benchmark
    public void testPrependDexx(Blackhole bh) {
        com.github.andrewoma.dexx.collection.Vector<Integer> v = com.github.andrewoma.dexx.collection.Vector.empty();
        for (int i=0; i<numElements; i++) {
            v = v.prepend(i);
        }
        bh.consume(v);
    }
//    @Benchmark
    public void testPrependAVector(Blackhole bh) {
        AVector<Integer> v = AVector.empty();
        for (int i=0; i<numElements; i++) {
            v = v.prepend(i);
        }
        bh.consume(v);
    }

//    @Benchmark
    public void testAppendDexx(Blackhole bh) {
        com.github.andrewoma.dexx.collection.Vector<Integer> v = com.github.andrewoma.dexx.collection.Vector.empty();
        for (int i=0; i<numElements; i++) {
            v = v.append(i);
        }
        bh.consume(v);
    }
//    @Benchmark
    public void testAppendAVector(Blackhole bh) {
        AVector<Integer> v = AVector.empty();
        for (int i=0; i<numElements; i++) {
            v = v.append(i);
        }
        bh.consume(v);
    }

//    @Benchmark
    public void testBuildDexx(Blackhole bh) {
        Builder<Integer, Vector<Integer>> builder = Vector.<Integer>factory().newBuilder();
        for (int i=0; i<numElements; i++) {
            builder.add(i);
        }
        bh.consume(builder.build());
    }
//    @Benchmark
    public void testBuildAVector(Blackhole bh) {
        AVector.Builder<Integer> builder = AVector.builder();
        for (int i=0; i<numElements; i++) {
            builder.add(i);
        }
        bh.consume(builder.build());
    }

//    @Benchmark
    public void testRandomAccessDexx(Blackhole bh) {
        Builder<Integer, Vector<Integer>> builder = Vector.<Integer>factory().newBuilder();
        for (int i=0; i<numElements; i++) {
            builder.add(i);
        }
        final Vector<Integer> v = builder.build();
        int result = 0;
        for (int i=0; i<numElements; i++) result += v.get(i);
        bh.consume(result);
    }
//    @Benchmark
    public void testRandomAccessAVector(Blackhole bh) {
        AVector.Builder<Integer> builder = AVector.builder();
        for (int i=0; i<numElements; i++) {
            builder.add(i);
        }
        final AVector<Integer> v = builder.build();
        int result = 0;
        for (int i=0; i<numElements; i++) result += v.get(i);
        bh.consume(result);
    }

    @Benchmark
    public void testIteratorDexx(Blackhole bh) {
        Builder<Integer, Vector<Integer>> builder = Vector.<Integer>factory().newBuilder();
        for (int i=0; i<numElements; i++) {
            builder.add(i);
        }
        final Vector<Integer> v = builder.build();
        int result = 0;
        for (int i: v) {
            result += i;
        }
        bh.consume(result);
    }
    @Benchmark
    public void testIteratorAVector(Blackhole bh) {
        AVector.Builder<Integer> builder = AVector.builder();
        for (int i=0; i<numElements; i++) {
            builder.add(i);
        }
        final AVector<Integer> v = builder.build();
        int result = 0;
        for (int i: v) {
            result += i;
        }
        bh.consume(result);
    }
}
