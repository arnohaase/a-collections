package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.ATreeSet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public interface ASetTests extends ACollectionTests {
    @Test default void testAddedRemoved() {
        doTest(v -> {
            ASet<Integer> s = v.mkSet();
            ASet<Integer> sOld;

            sOld = s;
            s = s.added(1);
            if (isImmutable()) assertTrue(sOld.isEmpty()); else assertSame(s, sOld);
            assertEquals(AHashSet.of(1), s);

            sOld = s;
            s = s.added(2);
            if (isImmutable()) assertEquals(AHashSet.of(1), sOld); else assertSame(s, sOld);
            assertEquals(AHashSet.of(1, 2), s);

            sOld = s;
            s = s.added(3);
            if (isImmutable()) assertEquals(AHashSet.of(1, 2), sOld); else assertSame(s, sOld);
            assertEquals(AHashSet.of(1, 2, 3), s);

            sOld = s;
            s = s.removed(2);
            if (isImmutable()) assertEquals(AHashSet.of(1, 2, 3), sOld); else assertSame(s, sOld);
            assertEquals(AHashSet.of(1, 3), s);

            sOld = s;
            s = s.removed(1);
            if (isImmutable()) assertEquals(AHashSet.of(1, 3), sOld); else assertSame(s, sOld);
            assertEquals(AHashSet.of(3), s);
            sOld = s;

            s = s.removed(3);
            if (isImmutable()) assertEquals(AHashSet.of(3), sOld); else assertSame(s, sOld);
            assertEquals(AHashSet.empty(), s);
        });
    }

    @Test default void testUnion() {
        doTest(v -> {
            assertTrue (v.mkSet().union(AHashSet.empty()).isEmpty());
            assertEquals (AHashSet.of(1), v.mkSet().union(AHashSet.of(1)));
            assertEquals (AHashSet.of(1), v.mkSet(1).union(AHashSet.empty()));
            assertEquals (AHashSet.of(1, 2), v.mkSet(1).union(AHashSet.of(2)));
            assertEquals (AHashSet.of(1, 2, 3), v.mkSet(1, 2).union(AHashSet.of(2, 3)));
        });
    }

    @Test default void testIntersect() {
        doTest(v -> {
            assertTrue(v.mkSet().intersect(AHashSet.empty()).isEmpty());
            assertTrue(v.mkSet().intersect(AHashSet.of(1)).isEmpty());
            assertTrue(v.mkSet(1).intersect(AHashSet.empty()).isEmpty());
            assertTrue(v.mkSet(1).intersect(AHashSet.of(2)).isEmpty());
            assertEquals(AHashSet.of(1), v.mkSet(1).intersect(AHashSet.of(1)));
            assertEquals(AHashSet.of(1), v.mkSet(1, 2).intersect(AHashSet.of(1)));
            assertEquals(AHashSet.of(1), v.mkSet(1).intersect(AHashSet.of(1, 2)));
            assertEquals(AHashSet.of(1, 2), v.mkSet(1, 2).intersect(AHashSet.of(1, 2)));
            assertEquals(AHashSet.of(2), v.mkSet(1, 2).intersect(AHashSet.of(2, 3)));
        });
    }

    @Test default void testDiff() {
        doTest(v -> {
            assertTrue(v.mkSet().diff(AHashSet.empty()).isEmpty());
            assertTrue(v.mkSet().diff(AHashSet.of(1)).isEmpty());
            assertTrue(v.mkSet(1).diff(AHashSet.of(1)).isEmpty());
            assertEquals(AHashSet.of(1), v.mkSet(1).diff(AHashSet.empty()));
            assertEquals(AHashSet.of(1), v.mkSet(1).diff(AHashSet.of(2)));
            assertEquals(AHashSet.of(1), v.mkSet(1, 2).diff(AHashSet.of(2, 3)));
        });
    }

    @SuppressWarnings("unchecked")
    @Test default void testSubsets() {
        doTest(v -> {
            assertEquals(AHashSet.of(ATreeSet.empty()),
                    v.mkSet().subsets().map(ASet::toSortedSet).toSet());
            assertEquals(AHashSet.of(ATreeSet.empty()),
                    v.mkSet().subsets(0).map(ASet::toSortedSet).toSet());
            assertEquals(AHashSet.empty(),
                    v.mkSet().subsets(1).map(ASet::toSortedSet).toSet());

            assertEquals(
                    AHashSet.of(
                            ATreeSet.empty(),
                            ATreeSet.of(1)
                    ), v.mkSet(1).subsets().map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.of(
                            ATreeSet.empty()
                    ), v.mkSet(1).subsets(0).map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.of(
                            ATreeSet.of(1)
                    ), v.mkSet(1).subsets(1).map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.empty(),
                    v.mkSet(1).subsets(2).map(ASet::toSortedSet).toSet());

            assertEquals(
                    AHashSet.of(
                            ATreeSet.empty(),
                            ATreeSet.of(1),
                            ATreeSet.of(2),
                            ATreeSet.of(1, 2)
                    ), v.mkSet(1, 2).subsets().map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.of(
                            ATreeSet.empty()
                    ), v.mkSet(1, 2).subsets(0).map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.of(
                            ATreeSet.of(1),
                            ATreeSet.of(2)
                    ), v.mkSet(1, 2).subsets(1).map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.of(
                            ATreeSet.of(1, 2)
                    ), v.mkSet(1, 2).subsets(2).map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.empty(),
                    v.mkSet(1, 2).subsets(3).map(ASet::toSortedSet).toSet());

            assertEquals(
                    AHashSet.of(
                            ATreeSet.empty(),
                            ATreeSet.of(1),
                            ATreeSet.of(2),
                            ATreeSet.of(3),
                            ATreeSet.of(1, 2),
                            ATreeSet.of(1, 3),
                            ATreeSet.of(2, 3),
                            ATreeSet.of(1, 2, 3)
                    ), v.mkSet(1, 2, 3).subsets().map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.of(
                            ATreeSet.empty()),
                    v.mkSet(1, 2, 3).subsets(0).map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.of(
                            ATreeSet.of(1),
                            ATreeSet.of(2),
                            ATreeSet.of(3)
                    ), v.mkSet(1, 2, 3).subsets(1).map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.of(
                            ATreeSet.of(1, 2),
                            ATreeSet.of(1, 3),
                            ATreeSet.of(2, 3)
                    ), v.mkSet(1, 2, 3).subsets(2).map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.of(
                            ATreeSet.of(1, 2, 3)
                    ), v.mkSet(1, 2, 3).subsets(3).map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.empty(),
                    v.mkSet(1, 2, 3).subsets(4).map(ASet::toSortedSet).toSet());


            assertEquals(
                    AHashSet.of(
                            ATreeSet.empty(),
                            ATreeSet.of(1),
                            ATreeSet.of(2),
                            ATreeSet.of(3),
                            ATreeSet.of(4),
                            ATreeSet.of(1, 2),
                            ATreeSet.of(1, 3),
                            ATreeSet.of(1, 4),
                            ATreeSet.of(2, 3),
                            ATreeSet.of(2, 4),
                            ATreeSet.of(3, 4),
                            ATreeSet.of(1, 2, 3),
                            ATreeSet.of(1, 2, 4),
                            ATreeSet.of(1, 3, 4),
                            ATreeSet.of(2, 3, 4),
                            ATreeSet.of(1, 2, 3, 4)
                    ), v.mkSet(1, 2, 3, 4).subsets().map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.of(
                            ATreeSet.empty()
                    ), v.mkSet(1, 2, 3, 4).subsets(0).map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.of(
                            ATreeSet.of(1),
                            ATreeSet.of(2),
                            ATreeSet.of(3),
                            ATreeSet.of(4)
                    ), v.mkSet(1, 2, 3, 4).subsets(1).map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.of(
                            ATreeSet.of(1, 2),
                            ATreeSet.of(1, 3),
                            ATreeSet.of(1, 4),
                            ATreeSet.of(2, 3),
                            ATreeSet.of(2, 4),
                            ATreeSet.of(3, 4)
                    ), v.mkSet(1, 2, 3, 4).subsets(2).map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.of(
                            ATreeSet.of(1, 2, 3),
                            ATreeSet.of(1, 2, 4),
                            ATreeSet.of(1, 3, 4),
                            ATreeSet.of(2, 3, 4)
                    ), v.mkSet(1, 2, 3, 4).subsets(3).map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.of(
                            ATreeSet.of(1, 2, 3, 4)
                    ), v.mkSet(1, 2, 3, 4).subsets(4).map(ASet::toSortedSet).toSet());
            assertEquals(
                    AHashSet.empty(),
                    v.mkSet(1, 2, 3, 4).subsets(5).map(ASet::toSortedSet).toSet());
        });
    }
}
