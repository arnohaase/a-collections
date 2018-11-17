package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.immutable.rbs.RedBlackTree;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import scala.math.LowPriorityOrderingImplicits;
import scala.math.Ordering;

import java.util.Comparator;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;


@Warmup(iterations = 2, time=10, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@Measurement(iterations = 5, time=8, timeUnit = TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Fork(2)
public class ProbierenBenchmark {
    private static final int size = 100_000;
    private static final int numIter = 10_000_000;

    @Benchmark
    public void testModifyAColl(Blackhole bh) {
        final Comparator<Integer> ordering = Comparator.naturalOrder();
        RedBlackTree.Tree<Integer,Integer> root = null;

        final int key = 10;

//        final TreeMap<Integer,Integer> map = new TreeMap<>();
//        map.put(key, key);
//        map.put(key, key);
//        bh.consume(map);

        root = RedBlackTree.update(root, key, key, true, ordering);
        bh.consume(root);
        root = RedBlackTree.update(root, key, key, true, ordering);
        bh.consume(root);
//
//
//        bh.consume(RedBlackTree.update(RedBlackTree.update(null, 5L, 5L, true, Comparator.naturalOrder()), 5L, 5L, true, Comparator.naturalOrder()));
    }

    @Benchmark
    public void testModifyScala(Blackhole bh) {
        final Random rand = new Random(12345);
        final Ordering<Integer> ordering = new LowPriorityOrderingImplicits(){}.comparatorToOrdering(Comparator.<Integer>naturalOrder());

        scala.collection.immutable.RedBlackTree.Tree<Integer,Integer> root = null;

        for(int i=0; i<numIter; i++) {
            final int key = rand.nextInt(size);
            root = scala.collection.immutable.RedBlackTree.update(root, key, key, true, ordering);
        }
        bh.consume(root);
    }
}
