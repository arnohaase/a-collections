package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollection;
import java.util.Collection;
import java.util.Iterator;


public abstract class AbstractImmutableCollection<T> implements ACollection<T> {

    @Override public boolean equals (Object o) {
        if (o == this) return true;
        if (! (o instanceof Iterable)) return false;
        if (o instanceof Collection && ((Collection) o).size() != size()) return false;

        final Iterator it1 = this.iterator();
        final Iterator it2 = ((Iterable) o).iterator();
        while (it1.hasNext() && it2.hasNext()) {
            if (!equality().equals(it1.next(), it2.next())) return false;
        }
        return !it1.hasNext() && !it2.hasNext();
    }

    @Override public int hashCode () {
        // we can not safely cache the hash code, even for immutable collections, because there is no way to
        //  be sure that the elements are immutable too :-(
        int result = 1;
        for (T o: this)
            result = 31*result + equality().hashCode(o);

        return result;
    }


    @Override public boolean add (T t) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public boolean remove (Object o) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public boolean addAll (Collection<? extends T> c) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public boolean removeAll (Collection<?> c) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public boolean retainAll (Collection<?> c) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public void clear () { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
}
