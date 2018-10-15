package com.ajjpj.acollections.internal;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.ACollectionBuilder;

import java.util.Collection;
import java.util.function.Function;
import java.util.function.Predicate;


public class ACollectionSupport {
    public static Object[] toArray (Collection<?> coll) {
        return toArray(coll, new Object[coll.size()]);
    }
    public static <T1> T1[] toArray (Collection<?> coll, T1[] a) {
        final int s = coll.size();

        @SuppressWarnings("unchecked")
        final T1[] result = a.length >= s ?
                a :
                (T1[])java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), s);
        if (result.length > s) {
            // if the array is longer than required, then the spec demands the data to be followed by <tt>null</tt>
            result[s] = null;
        }

        int idx=0;
        for (Object o: coll) {
            //noinspection unchecked
            result[idx] = (T1) o;
            idx += 1;
        }
        return result;
    }

    public static String toString(Class<?> baseClass, ACollection<?> coll) {
        return coll.mkString(baseClass.getSimpleName() + "[", ",", "]");
    }

    public static <T, U, C extends ACollection<U>> C map(ACollectionBuilder<U, C> builder, Iterable<T> coll, Function<T,U> f) {
        for (T o: coll) builder.add(f.apply(o));
        return builder.build();
    }
    public static <T, U, C extends ACollection<U>> C flatMap(ACollectionBuilder<U, C> builder, Iterable<T> coll, Function<T, Iterable<U>> f) {
        for (T o: coll) builder.addAll(f.apply(o));
        return builder.build();
    }
    public static <T, U, C extends ACollection<U>> C collect(ACollectionBuilder<U,C> builder, Iterable<T> coll, Predicate<T> filter, Function<T,U> f) {
        for (T o: coll) if (filter.test(o)) builder.add(f.apply(o));
        return builder.build();
    }
}
