package com.ajjpj.acollections.immutable;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;


@Warmup(iterations = 2, time=10, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 5, time=8, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
public class ProbBenchmark {

    @Benchmark
    public void testModifyAColl(Blackhole bh) {
        ProbJava.Tree<Integer,Integer> root = ProbJava.update(null, 10, 10);
        root = ProbJava.update(root, 10, 10);
        bh.consume(root);
    }

    @Benchmark
    public void testModifyScala(Blackhole bh) {
        ProbScala.Tree<Integer,Integer> root = ProbScala.update(null, 10, 10);
        root = ProbScala.update(root, 10, 10);
        bh.consume(root);
    }
}
