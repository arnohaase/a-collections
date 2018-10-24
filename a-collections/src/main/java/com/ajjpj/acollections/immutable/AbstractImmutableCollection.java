package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.ACollection;
import com.ajjpj.acollections.internal.ACollectionSupport;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;


public abstract class AbstractImmutableCollection<T> implements ACollection<T> {

    @Override public boolean equals (Object o) { //TODO make this abstract?
        if (o == this) return true;
        if (! (o instanceof Iterable)) return false;
        if (o instanceof Collection && ((Collection) o).size() != size()) return false;

        final Iterator it1 = this.iterator();
        final Iterator it2 = ((Iterable) o).iterator();
        while (it1.hasNext() && it2.hasNext()) {
            if (!Objects.equals(it1.next(), it2.next())) return false;
        }
        return !it1.hasNext() && !it2.hasNext();
    }

    @Override public int hashCode () {
        //TODO do we want to cache?
        // we can not safely cache the hash code, even for immutable collections, because there is no way to
        //  be sure that the elements are immutable too :-(
        int result = 1;
        for (T o: this)
            result = 31*result + Objects.hashCode(o);

        return result;
    }

    @Override public Object[] toArray () {
        return ACollectionSupport.toArray(this);
    }

    @Override public <T1> T1[] toArray (T1[] a) {
        return ACollectionSupport.toArray(this, a);
    }

    @Override public boolean add (T t) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public boolean remove (Object o) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public boolean addAll (Collection<? extends T> c) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public boolean removeAll (Collection<?> c) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public boolean retainAll (Collection<?> c) { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
    @Override public void clear () { throw new UnsupportedOperationException("no mutable operations for immutable collection"); }
}
