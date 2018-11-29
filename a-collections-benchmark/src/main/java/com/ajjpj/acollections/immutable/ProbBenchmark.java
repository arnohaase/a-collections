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
    public void testJava1(Blackhole bh) {
        com.ajjpj.acollections.immutable.j.Tree<Integer,Integer> root = ProbJava1.update(null, 10, 10);
        root = ProbJava1.update(root, 10, 10);
        bh.consume(root);
    }

    @Benchmark
    public void testJava2(Blackhole bh) {
        com.ajjpj.acollections.immutable.s.Tree<Integer,Integer> root = ProbJava2.update(null, 10, 10);
        root = ProbJava2.update(root, 10, 10);
        bh.consume(root);
    }

    @Benchmark
    public void testScala(Blackhole bh) {
        com.ajjpj.acollections.immutable.s.Tree<Integer,Integer> root = ProbScala.update(null, 10, 10);
        root = ProbScala.update(root, 10, 10);
        bh.consume(root);
    }
}
