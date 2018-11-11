package com.ajjpj.acollections;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;


/**
 * This is the primary abstraction of a-collections' collection classes: {@code ACollection} extends {@link java.util.Collection}, adding
 *  a rich set of API methods. For a more comprehensive introduction to the ideas behind a-collections, see the
 *  <a href="https://github.com/arnohaase/a-collections">project home page</a>.
 *
 * <p> The API structure of a-collections aims to mirror the interface hierarchy of Java's standard library, so there are interfaces
 *  {@link ASet} (extending {@link java.util.Set}), {@link ASortedSet} extends {@link java.util.SortedSet} and
 *  {@link AList} (extending {@link java.util.List}), as well as {@link AMap} (extending {@link java.util.Map}).
 *
 * <p> There are two "flavors" of collection classes implementing this interface, namely <em>mutable</em> and <em>immutable</em> classes.
 *
 * <p> There are mutable collection classes which behave pretty much like regular Java collection, except that they provide a lot of
 *   additional methods. Actually, they are wrappers around regular collection which makes them easy and cheap to create from arbitrary
 *   collections. There are
 *  {@link com.ajjpj.acollections.mutable.AMutableListWrapper AMutableListWrapper} to wrap a {@link java.util.List},
 *  {@link com.ajjpj.acollections.mutable.AMutableSetWrapper AMutableSetWrapper} to wrap a {@link java.util.Set}, and
 *  {@link com.ajjpj.acollections.mutable.AMutableArrayWrapper AMutableArrayWrapper} to wrap an array (plus
 *  {@link com.ajjpj.acollections.mutable.AMutableMapWrapper AMutableMapWrapper} which does not extend ACollection but is worth a mention
 *  nonetheless). Each of these classes has a range of factory methods to wrap existing collections or create instances based on
 *  values. Modifications write through to the underlying collection.
 *
 * <p> There are also immutable collection classes, i.e. collections that are guaranteed never to change once they are created. That is
 *  useful for (among other things) multithreaded code or robust API handing out internal data structures.
 * <p> Actually, a-collections' immutable collection classes are what is called "persistent" (see <a href="https://en.wikipedia.org/wiki/Persistent_data_structure"),
 *  i.e. they do have methods to add, remove or otherwise modify the collection, but these methods return a new instance, leaving the
 *  original alone. This requires some copying, but the underlying algorithms are pretty efficient and minimize the overhead.
 * <p> Immutable collection classes inherit all mutator methods from {@link java.util.Collection} etc., but there is no way for them to
 *  implement them in a meaningful way - they do not return the modified collection, which is necessary for modifications of "persistent"
 *  collection classes. So the immutable collection classes throw {@link UnsupportedOperationException} for inherited mutator methods.
 *
 * @param <T> This collection's element type
 */
public interface ACollection<T> extends ACollectionOps<T>, Collection<T> {
    @Override ACollection<T> filter (Predicate<T> f);
    @Override ACollection<T> filterNot (Predicate<T> f);

    @Override <U> ACollectionBuilder<U, ? extends ACollection<U>> newBuilder ();
    <K> AMap<K,? extends ACollection<T>> groupBy(Function<T,K> keyExtractor);
}
