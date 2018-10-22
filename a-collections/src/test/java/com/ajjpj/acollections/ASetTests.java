package com.ajjpj.acollections;

import com.ajjpj.acollections.immutable.AHashSet;
import com.ajjpj.acollections.immutable.ATreeSet;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public interface ASetTests extends ACollectionTests {
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
