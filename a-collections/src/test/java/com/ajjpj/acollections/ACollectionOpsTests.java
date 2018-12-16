package com.ajjpj.acollections;


public interface ACollectionOpsTests {
    void testStaticFactories();

    void testEquals();
    void testHashCode();
    void testSerDeser();

    void testIterator();

    void testToLinkedList();
    void testToVector();
    void testToSet();
    void testToSortedSet();
    void testToSortedSetWithComparator();
    void testToMap();

    void testToMutableList();
    void testToMutableSet();
    void testToMutableSortedSet();
    void testToMutableSortedSetWithComparator();
    void testToMutableMap();

    void testSize();
    void testIsEmpty();
    void testNonEmpty();

    void testHead();
    void testHeadOption();
    void testFirst();
    void testFirstOption();

    void testMap();
    void testFlatMap();
    void testCollect();
    void testCollectFirst();
    void testFilter();
    void testFilterNot();

    void testFind();
    void testForall();
    void testExists();
    void testCount();

    void testContains();

    void testReduce();
    void testReduceOption();
    void testReduceLeft();
    void testReduceLeftOption();

    void testGroupBy();

    void testMin();
    void testMax();

    void testFold();
    void testFoldLeft();

    void testMkString();
}
