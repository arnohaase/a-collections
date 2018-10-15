package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.AIterator;
import com.ajjpj.acollections.AbstractAIterator;

import java.util.*;


/**
 * inspired by dexx
 */
class CompactHashMap<K,V,X extends CompactHashMap.MapEntryWithEquality<K,V>> {
    interface MapEntryWithEquality<K,V> extends Map.Entry<K,V> {
        boolean hasEqualKey(MapEntryWithEquality<K,V> other);
        int keyHash();

        @Override default V setValue (V value) {
            throw new UnsupportedOperationException();
        }
    }

    protected static final CompactHashMap EMPTY = new CompactHashMap();

    public AIterator<X> iterator() {
        return AIterator.empty();
    }

    public int size() {
        return 0;
    }
    public boolean isEmpty() {
        return true;
    }

    @SuppressWarnings("unchecked")
    public static <K, V, X extends MapEntryWithEquality<K,V>> CompactHashMap<K,V,X> empty() {
        return EMPTY;
    }

    protected X get0(X kv, int level) {
        return null;
    }

    protected CompactHashMap<K,V,X> updated0(X kv, int level) {
        return new HashMap1<>(kv);
    }

    protected CompactHashMap<K,V,X> removed0(X kv, int level) { // entry instead of key as an optimization
        return this;
    }

    // utility method to create a HashTrieMap from two leaf CompactHashMaps (HashMap1 or CompactHashMapCollision1) with non-colliding hash code)
    static <K,V,X extends MapEntryWithEquality<K, V>> HashTrieMap<K,V,X> makeHashTrieMap(int hash0, CompactHashMap<K,V,X> elem0, int hash1, CompactHashMap<K,V,X> elem1, int level, int size) {
        int index0 = (hash0 >>> level) & 0x1f;
        int index1 = (hash1 >>> level) & 0x1f;
        if (index0 != index1) {
            int bitmap = (1 << index0) | (1 << index1);
            @SuppressWarnings("unchecked")
            Object[] elems = new Object[2];
            if (index0 < index1) {
                elems[0] = elem0;
                elems[1] = elem1;
            } else {
                elems[0] = elem1;
                elems[1] = elem0;
            }
            return new HashTrieMap<>(bitmap, elems, size);
        } else {
            @SuppressWarnings("unchecked")
            Object[] elems = new Object[1];
            int bitmap = (1 << index0);
            elems[0] = makeHashTrieMap(hash0, elem0, hash1, elem1, level + 5, size);
            return new HashTrieMap<>(bitmap, elems, size);
        }
    }

    static class HashMap1<K,V,X extends CompactHashMap.MapEntryWithEquality<K,V>> extends CompactHashMap<K,V,X> {
        private final X kv;

        HashMap1(X kv) {
            this.kv = kv;
        }

        @Override public int size() {
            return 1;
        }
        @Override public boolean isEmpty() {
            return false;
        }

        @Override protected X get0(X kv, int level) {
            if (this.kv.hasEqualKey(kv))
                return this.kv;
            else
                return null;
        }

        @Override protected CompactHashMap<K,V,X> updated0(X kv, int level) {
            if (this.kv.hasEqualKey(kv)) {
                return new HashMap1<>(kv);
            } else {
                if (kv.keyHash() != this.kv.keyHash()) {
                    // they have different hashes, but may collide at this level - find a level at which they don't
                    return makeHashTrieMap(this.kv.keyHash(), this, kv.keyHash(), new HashMap1<>(kv), level, 2);
                } else {
                    // 32-bit hash collision (rare, but not impossible)
                    return new HashMapCollision1<>(kv.keyHash(), CompactListMap.<K,V,X>empty().updated(this.kv).updated(kv));
                }
            }
        }

        @Override protected CompactHashMap<K,V,X> removed0(X entry, int level) {
            if (this.kv.hasEqualKey(entry))
                return CompactHashMap.empty();
            else
                return this;
        }

        @Override public AIterator<X> iterator() {
            return AIterator.single(kv);
        }
    }

    static class HashMapCollision1<K,V,X extends CompactHashMap.MapEntryWithEquality<K,V>> extends CompactHashMap<K,V,X> {
        private final int hash;
        private final CompactListMap<K,V,X> kvs;

        HashMapCollision1(int hash, CompactListMap<K,V,X> kvs) {
            this.hash = hash; //TODO look up hash?
            this.kvs = kvs;
        }

        @Override public int size() {
            return kvs.size();
        }
        @Override public boolean isEmpty () {
            return false;
        }

        @Override protected X get0(X kv, int level) {
            if (hash != kv.keyHash()) return null;
            return kvs.get(kv);
        }

        @Override protected CompactHashMap<K,V,X> updated0(X kv, int level) {
            if (kv.keyHash() == this.hash) {
                return new HashMapCollision1<>(hash, kvs.updated(kv));
            } else {
                return makeHashTrieMap(this.hash, this, hash, new HashMap1<>(kv), level, size() + 1);
            }
        }

        @Override protected CompactHashMap<K,V,X> removed0(X entry, int level) {
            if (entry.keyHash() == this.hash) {
                final CompactListMap<K,V,X> m = kvs.removed(entry);
                if (m.isEmpty()) return CompactHashMap.empty();
                if (m.tail().isEmpty()) return new HashMap1<>(m.head());
                return new HashMapCollision1<>(hash, m);
            }
            else {
                return this;
            }
        }

        @Override public AIterator<X> iterator() {
            return new AbstractAIterator<X>() {
                CompactListMap<K,V,X> next = kvs;

                @Override public boolean hasNext () {
                    return next.nonEmpty();
                }

                @Override public X next () {
                    final X result = next.head();
                    next = next.tail();
                    return result;
                }
            };
        }
    }

    static abstract class CompactListMap<K,V,X extends CompactHashMap.MapEntryWithEquality<K,V>> {
        static <K,V,X extends MapEntryWithEquality<K,V>> CompactListMap<K,V,X> empty() {
            //noinspection unchecked
            return (CompactListMap<K,V,X>) EMPTY;
        }

        abstract X head();
        abstract CompactListMap<K,V,X> tail();
        abstract X get (X key);
        abstract int size();
        boolean isEmpty() {
            return ! nonEmpty();
        }
        abstract boolean nonEmpty(); // is way more efficient than size()
        abstract CompactListMap<K,V,X> updated(X entry);
        abstract CompactListMap<K,V,X> removed(X entry); // only key is used - 'entry' is used as an optimization

        private static final CompactListMap EMPTY = new CompactListMap() {
            @Override MapEntryWithEquality head () {
                throw new UnsupportedOperationException();
            }

            @Override
            CompactListMap<Object, Object, ?> tail () {
                throw new UnsupportedOperationException();
            }
            @Override MapEntryWithEquality get (MapEntryWithEquality key) {
                return null;
            }
            @Override int size () {
                return 0;
            }
            @Override boolean nonEmpty () {
                return false;
            }

            @Override CompactListMap updated (MapEntryWithEquality entry) {
                //noinspection unchecked
                return new CompactListMap.Node(entry, this);
            }
            @Override CompactListMap removed (MapEntryWithEquality entry) {
                return this;
            }
        };

        private static class Node<K,V,X extends CompactHashMap.MapEntryWithEquality<K,V>> extends CompactListMap<K,V,X> {
            private final X entry;
            private final CompactListMap<K,V,X> tail;

            Node (X entry, CompactListMap<K,V,X> tail) {
                this.entry = entry;
                this.tail = tail;
            }

            @Override X head () {
                return entry;
            }

            @Override CompactListMap<K,V,X> tail () {
                return tail;
            }
            @Override X get (X kv) {
                CompactListMap<K,V,X> m = this;
                while(m.nonEmpty()) {
                    if(m.head().hasEqualKey(kv)) {
                        return m.head();
                    }
                    m = m.tail();
                }
                return null;
            }
            @Override int size() {
                int result = 1;
                CompactListMap<K,V,X> m = this;
                while(m.tail().nonEmpty()) {
                    m = m.tail();
                    result += 1;
                }
                return result;
            }

            @Override boolean nonEmpty () {
                return true;
            }

            @Override CompactListMap<K,V,X> updated (X entry) {
                return new CompactListMap.Node<>(entry, removed(entry));
            }
            @Override CompactListMap<K,V,X> removed (X entry) {
                int idx = 0;
                boolean hasMatch = false;

                CompactListMap<K,V,X> remaining = this;
                while(remaining.nonEmpty()) {
                    if (entry.hasEqualKey(remaining.head())) {
                        remaining = remaining.tail();
                        hasMatch = true;
                        break;
                    }
                    idx += 1;
                    remaining = remaining.tail();
                }
                if (! hasMatch) return this;

                CompactListMap<K,V,X> result = remaining;
                CompactListMap<K,V,X> iter = this;
                for (int i=0; i<idx; i++) {
                    result = new CompactListMap.Node<>(iter.head(), result);
                    iter = iter.tail ();
                }

                return result;
            }
        }
    }


    static class HashTrieMap<K,V,X extends MapEntryWithEquality<K,V>> extends CompactHashMap<K,V,X> {
        private final int bitmap;
        private final Object[] elems;
        private final int size;

        HashTrieMap(int bitmap, Object[] elems, int size) {
            this.bitmap = bitmap;
            this.elems = elems;
            this.size = size;
        }

        @Override public int size() {
            return size;
        }
        @Override public boolean isEmpty () {
            return false;
        }

        Object[] getElems() {
            return elems;
        }

        private CompactHashMap<K,V,X> getElem(int index) {
            //noinspection unchecked
            return (CompactHashMap<K,V,X>) elems[index];
        }

        @Override protected X get0(X kv, int level) {
            final int index = (kv.keyHash() >>> level) & 0x1f;
            final int mask = (1 << index);
            if (bitmap == -1) {
                return getElem(index & 0x1f).get0(kv, level + 5);
            }
            else if ((bitmap & mask) != 0) {
                final int offset = Integer.bitCount(bitmap & (mask - 1));
                return getElem(offset).get0(kv, level + 5);
            }
            else {
                return null;
            }
        }

        @Override protected CompactHashMap<K,V,X> updated0(X kv, int level) {
            final int index = (kv.keyHash() >>> level) & 0x1f;
            final int mask = (1 << index);
            final int offset = Integer.bitCount(bitmap & (mask - 1));
            if ((bitmap & mask) != 0) {
                final CompactHashMap<K,V,X> sub = getElem(offset);
                final CompactHashMap<K,V,X> subNew = sub.updated0(kv, level + 5);
                if (subNew.equals(sub)) {
                    return this;
                }
                else {
                    @SuppressWarnings("unchecked")
                    final Object[] elemsNew = new Object[elems.length];
                    System.arraycopy(elems, 0, elemsNew, 0, elems.length);
                    elemsNew[offset] = subNew;
                    return new HashTrieMap<>(bitmap, elemsNew, size + (subNew.size() - sub.size()));
                }
            }
            else {
                @SuppressWarnings("unchecked")
                final Object[] elemsNew = new Object[elems.length + 1];
                System.arraycopy(elems, 0, elemsNew, 0, offset);
                elemsNew[offset] = new HashMap1<>(kv);
                System.arraycopy(elems, offset, elemsNew, offset + 1, elems.length - offset);
                return new HashTrieMap<>(bitmap | mask, elemsNew, size + 1);
            }
        }

        @Override protected CompactHashMap<K,V,X> removed0(X kv, int level) {
            final int index = (kv.keyHash() >>> level) & 0x1f;
            final int mask = (1 << index);
            final int offset = Integer.bitCount(bitmap & (mask - 1));
            if ((bitmap & mask) != 0) {
                final CompactHashMap<K,V,X> sub = getElem(offset);
                final CompactHashMap<K,V,X> subNew = sub.removed0(kv, level + 5);
                if (subNew.equals(sub)) {
                    return this;
                }
                else if (subNew.size() == 0) {
                    int bitmapNew = bitmap ^ mask;
                    if (bitmapNew != 0) {
                        @SuppressWarnings("unchecked")
                        final Object[] elemsNew = new Object[elems.length - 1];
                        System.arraycopy(elems, 0, elemsNew, 0, offset);
                        System.arraycopy(elems, offset + 1, elemsNew, offset, elems.length - offset - 1);
                        final int sizeNew = size - sub.size();
                        if (elemsNew.length == 1 && !(elemsNew[0] instanceof HashTrieMap)) {
                            //noinspection unchecked
                            return (CompactHashMap<K,V,X>) elemsNew[0];
                        }
                        else {
                            return new HashTrieMap<>(bitmapNew, elemsNew, sizeNew);
                        }
                    } else {
                        return CompactHashMap.empty();
                    }
                }
                else if (elems.length == 1 && !(subNew instanceof HashTrieMap)) {
                    return subNew;
                }
                else {
                    @SuppressWarnings("unchecked")
                    final Object[] elemsNew = new Object[elems.length];
                    System.arraycopy(elems, 0, elemsNew, 0, elems.length);
                    elemsNew[offset] = subNew;
                    final int sizeNew = size + (subNew.size() - sub.size());
                    return new HashTrieMap<>(bitmap, elemsNew, sizeNew);
                }
            } else {
                return this;
            }
        }

        @Override public AIterator<X> iterator() {
            return new Itr<>(elems);
        }
    }

    static class Itr<K,V,X extends MapEntryWithEquality<K,V>> extends AbstractAIterator<X> {
        private final ArrayDeque<Snapshot> stack = new ArrayDeque<>();

        private Iterator<X> subIterator;
        private Snapshot current;
        private X next;

        Itr (Object[] elems) {
            current = new Snapshot(elems, 0);
            gotoNext();
        }

        private void gotoNext() {
            next = null;

            if (subIterator != null) {
                if (subIterator.hasNext()) {
                    next = subIterator.next();
                    return;
                } else {
                    subIterator = null;
                }
            }

            while (next == null) {
                if (current.pos == current.objects.length) {
                    // Exhausted current array, try the stack
                    if (stack.isEmpty()) {
                        return;
                    }
                    else {
                        current = stack.pop();
                    }
                }
                else {
                    Object object = current.objects[current.pos++];
                    if (object instanceof HashTrieMap) {
                        stack.push(current);
                        current = new Snapshot(((HashTrieMap) object).getElems(), 0);
                    }
                    else if (object instanceof HashMapCollision1) {
                        //noinspection unchecked
                        subIterator = ((HashMapCollision1) object).iterator();
                        next = subIterator.next();
                    }
                    else {
                        //noinspection unchecked
                        next = (X) ((HashMap1)object).kv;
                    }
                }
            }
        }

        @Override public boolean hasNext() {
            return next != null;
        }

        @Override public X next() {
            if (next == null) {
                throw new NoSuchElementException();
            }

            X result = next;
            gotoNext();
            return result;
        }

        @Override public void remove() {
            throw new UnsupportedOperationException();
        }

        static class Snapshot {
            final Object[] objects;
            int pos;

            Snapshot(Object[] objects, int pos) {
                this.objects = objects;
                this.pos = pos;
            }
        }
    }
}
