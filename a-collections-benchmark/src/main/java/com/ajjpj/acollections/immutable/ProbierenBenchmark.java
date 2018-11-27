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
@Fork(2)
public class ProbierenBenchmark {
    private static final int size = 100_000;
    private static final int numIter = 2;

//    @Benchmark
    public void testProbieren(Blackhole bh) {
        final Comparator<Integer> ordering = Comparator.naturalOrder();
        RedBlackTree.Tree<Integer,Integer> root = null;

        final int key = 10;

        root = RedBlackTree.update(root, key, key, true, ordering);
        bh.consume(root);
        root = RedBlackTree.update(root, key, key, true, ordering);
        bh.consume(root);
    }

    @Benchmark
    public void testModifyAColl(Blackhole bh) {
        //final Random rand = new Random(12345);
        final Comparator<Integer> ordering = Comparator.naturalOrder();

        RedBlackTree.Tree<Integer,Integer> root = null;

        for(int i=0; i<numIter; i++) {
            final int key = 10; //rand.nextInt(size);
            root = RedBlackTree.update(root, key, key, true, ordering);
        }
        bh.consume(root);
    }

    @Benchmark
    public void testModifyScala(Blackhole bh) {
        // final Random rand = new Random(12345);
        final Ordering<Integer> ordering = new LowPriorityOrderingImplicits(){}.comparatorToOrdering(Comparator.<Integer>naturalOrder());

        scala.collection.immutable.RedBlackTree.Tree<Integer,Integer> root = null;

        for(int i=0; i<numIter; i++) {
            final int key = 10; //rand.nextInt(size);
            root = scala.collection.immutable.RedBlackTree.update(root, key, key, true, ordering);
        }
        bh.consume(root);
    }
}
