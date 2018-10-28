package com.ajjpj.acollections.internal;


import com.ajjpj.acollections.AList;

import java.util.Iterator;
import java.util.List;

public class AListSupport {
    public static boolean equals(AList<?> l, Object o) {
        if (l == o) return true;
        if (!(o instanceof List))
            return false;

        final Iterator<?> e1 = l.iterator();
        final Iterator<?> e2 = ((List<?>) o).iterator();
        while (e1.hasNext() && e2.hasNext()) {
            final Object o1 = e1.next();
            final Object o2 = e2.next();
            if (!(o1==null ? o2==null : o1.equals(o2)))
                return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    public static int hashCode (AList<?> l) {
        //TODO do we want to cache?
        // we can not safely cache the hash code, even for immutable collections, because there is no way to
        //  be sure that the elements are immutable too :-(
        int result = 1;
        for (Object o: l)
            result = 31*result + (o==null ? 0 : o.hashCode());

        return result;
    }

}
