package com.ajjpj.acollections.immutable;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import scala.math.LowPriorityOrderingImplicits;
import scala.math.Ordering;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Warmup(iterations = 2, time=10, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 5, time=8, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(1)
public class ProbBenchmark {
    private static final int numIter = 2;

    @Benchmark
    public void testModifyAColl(Blackhole bh) {
        final Comparator<Integer> ordering = Comparator.naturalOrder();

        ProbJava.Tree<Integer,Integer> root = ProbJava.update(null, 10, 10, true, ordering);
        root = ProbJava.update(root, 10, 10, true, ordering);
        bh.consume(root);
    }

    @Benchmark
    public void testModifyScala(Blackhole bh) {
        final Comparator<Integer> ordering = Comparator.naturalOrder();

        ProbScala.Tree<Integer,Integer> root = ProbScala.update(null, 10, 10, true, ordering);
        root = ProbScala.update(root, 10, 10, true, ordering);
        bh.consume(root);
    }
}
