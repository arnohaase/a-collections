package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.*;
import com.ajjpj.acollections.internal.ACollectionSupport;
import com.ajjpj.acollections.internal.AListDefaults;
import com.ajjpj.acollections.internal.AListSupport;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;


public class AVector<T> extends AbstractImmutableCollection<T> implements AListDefaults<T, AVector<T>>, RandomAccess, Serializable {
    private static final int Log2ConcatFaster = 5;
    private static final int TinyAppendFaster = 2;

    public static <T> AVector<T> from(Iterable<T> that) {
        return fromIterator(that.iterator());
    }

    public static <T> AVector<T> fromIterator(Iterator<T> it) {
        return AVector
                .<T>builder()
                .addAll(it)
                .build();
    }

    public static <T> AVector<T> of(T o) {
        return AVector
                .<T>builder()
                .add(o)
                .build();
    }
    public static <T> AVector<T> of(T o1, T o2) {
        return AVector
                .<T>builder()
                .add(o1)
                .add(o2)
                .build();
    }
    public static <T> AVector<T> of(T o1, T o2, T o3) {
        return AVector
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .build();
    }
    public static <T> AVector<T> of(T o1, T o2, T o3, T o4) {
        return AVector
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .add(o4)
                .build();
    }
    public static <T> AVector<T> of(T o1, T o2, T o3, T o4, T o5, T... others) {
        return AVector
                .<T>builder()
                .add(o1)
                .add(o2)
                .add(o3)
                .add(o4)
                .add(o5)
                .addAll(others)
                .build();
    }

    @SuppressWarnings("StaticInitializerReferencesSubClass")
    private static final AVector EMPTY = new AVector<>(0, 0, 0);

    public static <T> AVector<T> empty() {
        return EMPTY;
    }

    private final VectorPointer<T> pointer = new VectorPointer<T>();

    private final int startIndex;
    private final int endIndex;
    private final int focus;

    private boolean dirty = false;

    AVector (int startIndex, int endIndex, int focus) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.focus = focus;
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override public boolean equals (Object o) {
        return AListSupport.equals(this, o);
    }

    @Override public int hashCode () {
        return AListSupport.hashCode(this);
    }

    @Override public <U> ACollectionBuilder<U, AVector<U>> newBuilder() {
        return builder();
    }

    @Override public AVector<T> toVector () {
        return this;
    }

    @Override public String toString () {
        return ACollectionSupport.toString(AVector.class, this);
    }

    public int size() {
        return endIndex - startIndex;
    }

    private void initIterator(Itr<T> s) {
        s.initFrom(pointer);
        if (dirty) s.stabilize(focus);
        if (s.depth > 1) s.gotoPos(startIndex, startIndex ^ focus);
    }

    @Override public AIterator<T> iterator() {
        final Itr<T> s = new Itr<T>(startIndex, endIndex);
        initIterator(s);
        return s;
    }

    /**
     *
     * Without knowing that's size, we have now way to determine whether element-wise appending is faster than rebuilding the AVector or not.
     *  To be on the safe side, this implementation rebuilds the entire AVector; for optimized code appending small collections, use
     *  {@link #concat(Iterable)}.
     */
    public AVector<T> concat (Iterator<? extends T> that) {
        return AVector
                .<T>builder()
                .addAll(this)
                .addAll(that)
                .build();
    }
    public AVector<T> concat (Iterable<? extends T> that) {
        if (! that.iterator().hasNext()) return this;
        if (! (that instanceof Collection)) {
            // we do not know that's size, so we use the generic builder-based approach
            return concat(that.iterator());
        }

        //noinspection unchecked
        final int thatSize = ((Collection<? extends T>) that).size();
        if (thatSize <= TinyAppendFaster || thatSize < (this.size() >>> Log2ConcatFaster)) {
            // 'that' is very small or at least way smaller than this, so element-wise appending is worth it
            AVector<T> result = this;
            for (T o: that) result = result.append(o);
            return result;
        }
        if (that instanceof AVector && (this.size() <= TinyAppendFaster || this.size() < (thatSize >>> Log2ConcatFaster))) {
            // if 'that' is an AVector too, the same performance optimizations work in reverse
            //noinspection unchecked
            AVector<T> result = (AVector<T>) that;
            final Iterator<T> it = reverseIterator();
            while (it.hasNext()) result = result.prepend(it.next());
            return result;
        }
        // both collections are non-trivial in size, and neither dominates the other --> rebuilding the entire AVector
        return concat(that.iterator());
    }

    @Override public AIterator<T> reverseIterator () {
        //TODO more efficient implementation along the lines of Itr
        return new AbstractAIterator<T>() {
            int nextIdx = size()-1;

            @Override public boolean hasNext () {
                return nextIdx >= 0;
            }

            @Override public T next () {
                if (!hasNext()) throw new NoSuchElementException();
                return get(nextIdx--);
            }
        };
    }

    @Override public <U> AVector<U> map (Function<T, U> f) {
        return ACollectionSupport.map(newBuilder(), this, f);
    }
    @Override public <U> AVector<U> flatMap(Function<T, Iterable<U>> f) {
        return ACollectionSupport.flatMap(newBuilder(), this, f);
    }

    @Override public <U> AVector<U> collect (Predicate<T> filter, Function<T, U> f) {
        return ACollectionSupport.collect(newBuilder(), this, filter, f);
    }

    @Override public boolean addAll (int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override public T set (int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override public void add (int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override public T remove (int index) {
        throw new UnsupportedOperationException();
    }

    @Override public List<T> subList (int fromIndex, int toIndex) {
        if (fromIndex>toIndex || toIndex>size() || fromIndex<0)
            throw new IndexOutOfBoundsException();
        return dropRight(size()-toIndex).takeRight(toIndex-fromIndex); //TODO return a lazily materialized view
    }

    // Ideally, clients will inline calls to map all the way down, including the iterator/builder methods.
    // In principle, escape analysis could even remove the iterator/builder allocations and do it
    // with local variables exclusively. But we're not quite there yet ...

    @Override public T get(int index) {
        final int idx = checkRangeConvert(index);
        return pointer.getElem(idx, idx ^ focus);
    }

    private int checkRangeConvert(int index) {
        final int idx = index + startIndex;
        if (index < 0 || idx >= endIndex) throw new IndexOutOfBoundsException(String.valueOf(index));
        return idx;
    }


    @Override public AVector<T> take(int n) {
        if (n <= 0) return empty();
        if (startIndex + n < endIndex) return dropBack0(startIndex + n);
        return this;
    }
    @Override public AVector<T> drop(int n) {
        if (n <= 0) return this;
        if (startIndex + n < endIndex) return dropFront0(startIndex + n);
        return empty();
    }
    @Override public AVector<T> takeRight(int n) {
        if (n <= 0) return empty();
        if (endIndex - n > startIndex) return dropFront0(endIndex - n);
        return this;
    }

    @Override public AVector<T> dropRight(int n) {
        if (n <= 0) return this;
        if (endIndex - n > startIndex) return dropBack0(endIndex - n);
        return empty();
    }

    @Override public boolean isEmpty() {
        return size() == 0;
    }

    @Override public T head() {
        if (isEmpty()) throw new NoSuchElementException();
        return get(0);
    }

    private AVector<T> slice(int from, int until) {
        return take(until).drop(from);
    }

    @Override public AVector<T> updated(int index, T elem) {
        final int idx = checkRangeConvert(index);
        final AVector<T> s = new AVector<>(startIndex, endIndex, idx);
        s.pointer.initFrom(pointer);
        s.dirty = dirty;
        s.gotoPosWritable(focus, idx, focus ^ idx);  // if dirty commit changes; go to new pos and prepare for writing
        s.pointer.display0[idx & 0x1f] = elem;
        return s;
    }

    private void gotoPosWritable(int oldIndex, int newIndex, int xor) {
        if (dirty) {
            pointer.gotoPosWritable1(oldIndex, newIndex, xor);
        } else {
            pointer.gotoPosWritable0(newIndex);
            dirty = true;
        }
    }

    private void gotoFreshPosWritable(int oldIndex, int newIndex, int xor) {
        if (dirty) {
            pointer.gotoFreshPosWritable1(oldIndex, newIndex, xor);
        } else {
            pointer.gotoFreshPosWritable0(oldIndex, newIndex, xor);
            dirty = true;
        }
    }


    @Override public AVector<T> prepend(T value) {
        if (endIndex != startIndex) {
            final int blockIndex = (startIndex - 1) & ~31;
            final int lo = (startIndex - 1) & 31;

            if (startIndex != blockIndex + 32) {
                final AVector<T> s = new AVector<>(startIndex - 1, endIndex, blockIndex);
                s.pointer.initFrom(pointer);
                s.dirty = dirty;
                s.gotoPosWritable(focus, blockIndex, focus ^ blockIndex);
                s.pointer.display0[lo] = value;
                return s;
            } else {

                final int freeSpace = ((1 << 5 * (pointer.depth)) - endIndex); // free space at the right given the current tree-structure depth
                final int shift = freeSpace & ~((1 << 5 * (pointer.depth - 1)) - 1); // number of elements by which we'll shift right (only move at top level)
                final int shiftBlocks = freeSpace >>> 5 * (pointer.depth - 1); // number of top-level blocks

                if (shift != 0) {
                    // case A: we can shift right on the top level
                    if (pointer.depth > 1) {
                        final int newBlockIndex = blockIndex + shift;
                        final int newFocus = focus + shift;
                        final AVector<T> s = new AVector<>(startIndex - 1 + shift, endIndex + shift, newBlockIndex);
                        s.pointer.initFrom(pointer);
                        s.dirty = dirty;
                        s.shiftTopLevel(0, shiftBlocks); // shift right by n blocks
                        s.gotoFreshPosWritable(newFocus, newBlockIndex, newFocus ^ newBlockIndex); // maybe create pos; prepare for writing
                        s.pointer.display0[lo] = value;
                        return s;
                    } else {
                        final int newBlockIndex = blockIndex + 32;
                        final int newFocus = focus;

                        final AVector<T> s = new AVector<>(startIndex - 1 + shift, endIndex + shift, newBlockIndex);
                        s.pointer.initFrom(pointer);
                        s.dirty = dirty;
                        s.shiftTopLevel(0, shiftBlocks); // shift right by n elements
                        s.gotoPosWritable(newFocus, newBlockIndex, newFocus ^ newBlockIndex); // prepare for writing
                        s.pointer.display0[shift - 1] = value;
                        return s;
                    }
                } else if (blockIndex < 0) {
                    // case B: we need to move the whole structure
                    final int move = (1 << 5 * (pointer.depth + 1)) - (1 << 5 * (pointer.depth));

                    final int newBlockIndex = blockIndex + move;
                    final int newFocus = focus + move;

                    final AVector<T> s = new AVector<>(startIndex - 1 + move, endIndex + move, newBlockIndex);
                    s.pointer.initFrom(pointer);
                    s.dirty = dirty;
                    s.gotoFreshPosWritable(newFocus, newBlockIndex, newFocus ^ newBlockIndex); // could optimize: we know it will create a whole branch
                    s.pointer.display0[lo] = value;
                    return s;
                } else {
                    final int newFocus = focus;

                    final AVector<T> s = new AVector<>(startIndex - 1, endIndex, blockIndex);
                    s.pointer.initFrom(pointer);
                    s.dirty = dirty;
                    s.gotoFreshPosWritable(newFocus, blockIndex, newFocus ^ blockIndex);
                    s.pointer.display0[lo] = value;
                    return s;
                }

            }
        } else {
            // empty vector, just insert single element at the back
            final Object[] elems = new Object[32];
            elems[31] = value;
            final AVector<T> s = new AVector<>(31, 32, 0);
            s.pointer.depth = 1;
            s.pointer.display0 = elems;
            return s;
        }
    }

    @Override public AVector<T> append(T value) {
        if (endIndex != startIndex) {
            int blockIndex = endIndex & ~31;
            int lo = endIndex & 31;

            if (endIndex != blockIndex) {
                final AVector<T> s = new AVector<>(startIndex, endIndex + 1, blockIndex);
                s.pointer.initFrom(pointer);
                s.dirty = dirty;
                s.gotoPosWritable(focus, blockIndex, focus ^ blockIndex);
                s.pointer.display0[lo] = value;
                return s;
            } else {
                final int shift = startIndex & ~((1 << 5 * (pointer.depth - 1)) - 1);
                final int shiftBlocks = startIndex >>> 5 * (pointer.depth - 1);

                if (shift != 0) {
                    if (pointer.depth > 1) {
                        final int newBlockIndex = blockIndex - shift;
                        final int newFocus = focus - shift;
                        final AVector<T> s = new AVector<>(startIndex - shift, endIndex + 1 - shift, newBlockIndex);
                        s.pointer.initFrom(pointer);
                        s.dirty = dirty;
                        s.shiftTopLevel(shiftBlocks, 0); // shift left by n blocks
                        s.gotoFreshPosWritable(newFocus, newBlockIndex, newFocus ^ newBlockIndex);
                        s.pointer.display0[lo] = value;
                        return s;
                    } else {
                        final int newBlockIndex = blockIndex - 32;
                        final int newFocus = focus;

                        final AVector<T> s = new AVector<>(startIndex - shift, endIndex + 1 - shift, newBlockIndex);
                        s.pointer.initFrom(pointer);
                        s.dirty = dirty;
                        s.shiftTopLevel(shiftBlocks, 0); // shift right by n elements
                        s.gotoPosWritable(newFocus, newBlockIndex, newFocus ^ newBlockIndex);
                        s.pointer.display0[32 - shift] = value;
                        return s;
                    }
                } else {
                    final int newFocus = focus;

                    final AVector<T> s = new AVector<>(startIndex, endIndex + 1, blockIndex);
                    s.pointer.initFrom(pointer);
                    s.dirty = dirty;
                    s.gotoFreshPosWritable(newFocus, blockIndex, newFocus ^ blockIndex);
                    s.pointer.display0[lo] = value;
                    //assert(s.depth == depth+1) might or might not create new level!
                    return s;
                }
            }
        } else {
            final Object[] elems = new Object[32];
            elems[0] = value;
            final AVector<T> s = new AVector<>(0, 1, 0);
            s.pointer.depth = 1;
            s.pointer.display0 = elems;
            return s;
        }
    }

    private void shiftTopLevel(int oldLeft, int newLeft) {
        switch (pointer.depth - 1) {
            case 0: pointer.display0 = pointer.copyRange(pointer.display0, oldLeft, newLeft); break;
            case 1: pointer.display1 = pointer.copyRange(pointer.display1, oldLeft, newLeft); break;
            case 2: pointer.display2 = pointer.copyRange(pointer.display2, oldLeft, newLeft); break;
            case 3: pointer.display3 = pointer.copyRange(pointer.display3, oldLeft, newLeft); break;
            case 4: pointer.display4 = pointer.copyRange(pointer.display4, oldLeft, newLeft); break;
            case 5: pointer.display5 = pointer.copyRange(pointer.display5, oldLeft, newLeft); break;
        }
    }

    private void zeroLeft(Object[] array, int index) {
        for (int i=0; i<index; i++) array[i] = 0;
    }

    private void zeroRight(Object[] array, int index) {
        for (int i=index; i<32; i++) array[i] = 0;
    }

    private Object[] copyLeft(Object[] array, int right) {
        final Object[] result = new Object[32];
        System.arraycopy(array, 0, result, 0, right);
        return result;
    }

    private Object[] copyRight(Object[] array, int left) {
        final Object[] result = new Object[32];
        System.arraycopy(array, left, result, left, 32 - left);
        return result;
    }

    private void preClean(int depth) {
        pointer.depth = depth;

        switch (depth - 1) {
            case 0:
                pointer.display1 = null;
                pointer.display2 = null;
                pointer.display3 = null;
                pointer.display4 = null;
                pointer.display5 = null;
                break;
            case 1:
                pointer.display2 = null;
                pointer.display3 = null;
                pointer.display4 = null;
                pointer.display5 = null;
                break;
            case 2:
                pointer.display3 = null;
                pointer.display4 = null;
                pointer.display5 = null;
                break;
            case 3:
                pointer.display4 = null;
                pointer.display5 = null;
                break;
            case 4:
                pointer.display5 = null;
                break;
            default:
        }
    }

    // requires structure is at index cutIndex and writable at level 0
    private void cleanLeftEdge(int cutIndex) {
        if (cutIndex < (1 << 5)) {
            zeroLeft(pointer.display0, cutIndex);
        } else if (cutIndex < (1 << 10)) {
            zeroLeft(pointer.display0, cutIndex & 0x1f);
            pointer.display1 = copyRight(pointer.display1, (cutIndex >>> 5));
        } else if (cutIndex < (1 << 15)) {
            zeroLeft(pointer.display0, cutIndex & 0x1f);
            pointer.display1 = copyRight(pointer.display1, (cutIndex >>> 5) & 0x1f);
            pointer.display2 = copyRight(pointer.display2, (cutIndex >>> 10));
        } else if (cutIndex < (1 << 20)) {
            zeroLeft(pointer.display0, cutIndex & 0x1f);
            pointer.display1 = copyRight(pointer.display1, (cutIndex >>> 5) & 0x1f);
            pointer.display2 = copyRight(pointer.display2, (cutIndex >>> 10) & 0x1f);
            pointer.display3 = copyRight(pointer.display3, (cutIndex >>> 15));
        } else if (cutIndex < (1 << 25)) {
            zeroLeft(pointer.display0, cutIndex & 0x1f);
            pointer.display1 = copyRight(pointer.display1, (cutIndex >>> 5) & 0x1f);
            pointer.display2 = copyRight(pointer.display2, (cutIndex >>> 10) & 0x1f);
            pointer.display3 = copyRight(pointer.display3, (cutIndex >>> 15) & 0x1f);
            pointer.display4 = copyRight(pointer.display4, (cutIndex >>> 20));
        } else if (cutIndex < (1 << 30)) {
            zeroLeft(pointer.display0, cutIndex & 0x1f);
            pointer.display1 = copyRight(pointer.display1, (cutIndex >>> 5) & 0x1f);
            pointer.display2 = copyRight(pointer.display2, (cutIndex >>> 10) & 0x1f);
            pointer.display3 = copyRight(pointer.display3, (cutIndex >>> 15) & 0x1f);
            pointer.display4 = copyRight(pointer.display4, (cutIndex >>> 20) & 0x1f);
            pointer.display5 = copyRight(pointer.display5, (cutIndex >>> 25));
        } else {
            throw new IllegalArgumentException();
        }
    }

    // requires structure is writable and at index cutIndex
    private void cleanRightEdge(int cutIndex) {
        // we're actually sitting one block left if cutIndex lies on a block boundary
        // this means that we'll end up erasing the whole block!!

        if (cutIndex <= (1 << 5)) {
            zeroRight(pointer.display0, cutIndex);
        } else if (cutIndex <= (1 << 10)) {
            zeroRight(pointer.display0, ((cutIndex - 1) & 0x1f) + 1);
            pointer.display1 = copyLeft(pointer.display1, (cutIndex >>> 5));
        } else if (cutIndex <= (1 << 15)) {
            zeroRight(pointer.display0, ((cutIndex - 1) & 0x1f) + 1);
            pointer.display1 = copyLeft(pointer.display1, (((cutIndex - 1) >>> 5) & 0x1f) + 1);
            pointer.display2 = copyLeft(pointer.display2, (cutIndex >>> 10));
        } else if (cutIndex <= (1 << 20)) {
            zeroRight(pointer.display0, ((cutIndex - 1) & 0x1f) + 1);
            pointer.display1 = copyLeft(pointer.display1, (((cutIndex - 1) >>> 5) & 0x1f) + 1);
            pointer.display2 = copyLeft(pointer.display2, (((cutIndex - 1) >>> 10) & 0x1f) + 1);
            pointer.display3 = copyLeft(pointer.display3, (cutIndex >>> 15));
        } else if (cutIndex <= (1 << 25)) {
            zeroRight(pointer.display0, ((cutIndex - 1) & 0x1f) + 1);
            pointer.display1 = copyLeft(pointer.display1, (((cutIndex - 1) >>> 5) & 0x1f) + 1);
            pointer.display2 = copyLeft(pointer.display2, (((cutIndex - 1) >>> 10) & 0x1f) + 1);
            pointer.display3 = copyLeft(pointer.display3, (((cutIndex - 1) >>> 15) & 0x1f) + 1);
            pointer.display4 = copyLeft(pointer.display4, (cutIndex >>> 20));
        } else if (cutIndex <= (1 << 30)) {
            zeroRight(pointer.display0, ((cutIndex - 1) & 0x1f) + 1);
            pointer.display1 = copyLeft(pointer.display1, (((cutIndex - 1) >>> 5) & 0x1f) + 1);
            pointer.display2 = copyLeft(pointer.display2, (((cutIndex - 1) >>> 10) & 0x1f) + 1);
            pointer.display3 = copyLeft(pointer.display3, (((cutIndex - 1) >>> 15) & 0x1f) + 1);
            pointer.display4 = copyLeft(pointer.display4, (((cutIndex - 1) >>> 20) & 0x1f) + 1);
            pointer.display5 = copyLeft(pointer.display5, (cutIndex >>> 25));
        } else {
            throw new IllegalArgumentException();
        }
    }

    private int requiredDepth(int xor) {
        if (xor < (1 << 5)) return 1;
        else if (xor < (1 << 10)) return 2;
        else if (xor < (1 << 15)) return 3;
        else if (xor < (1 << 20)) return 4;
        else if (xor < (1 << 25)) return 5;
        else if (xor < (1 << 30)) return 6;
        else throw new IllegalArgumentException();
    }

    private AVector<T> dropFront0(int cutIndex) {
        final int blockIndex = cutIndex & ~31;

        final int xor = cutIndex ^ (endIndex - 1);
        final int d = requiredDepth(xor);
        final int shift = (cutIndex & ~((1 << (5 * d)) - 1));

        // need to init with full display iff going to cutIndex requires swapping block at level >= d
        final AVector<T> s = new AVector<>(cutIndex - shift, endIndex - shift, blockIndex - shift);
        s.pointer.initFrom(pointer);
        s.dirty = dirty;
        s.gotoPosWritable(focus, blockIndex, focus ^ blockIndex);
        s.preClean(d);
        s.cleanLeftEdge(cutIndex - shift);
        return s;
    }

    private AVector<T> dropBack0(int cutIndex) {
        final int blockIndex = (cutIndex - 1) & ~31;

        final int xor = startIndex ^ (cutIndex - 1);
        final int d = requiredDepth(xor);
        final int shift = (startIndex & ~((1 << (5 * d)) - 1));

        final AVector<T> s = new AVector<>(startIndex - shift, cutIndex - shift, blockIndex - shift);
        s.pointer.initFrom(pointer);
        s.dirty = dirty;
        s.gotoPosWritable(focus, blockIndex, focus ^ blockIndex);
        s.preClean(d);
        s.cleanRightEdge(cutIndex - shift);
        return s;
    }

    static class Itr<T> extends VectorPointer<T> implements AIterator<T> {
        private int blockIndex;
        private int lo;
        private final int endIndex;
        private int endLo;
        private boolean hasNext;

        Itr (int startIndex, int endIndex) {
            blockIndex = startIndex & ~31;
            lo = startIndex & 31;
            this.endIndex = endIndex;
            endLo = Math.min(this.endIndex - blockIndex, 32);
            hasNext = blockIndex + lo < this.endIndex;
        }

        @Override public boolean hasNext() {
            return hasNext;
        }

        @Override public T next() {
            if (!hasNext) throw new NoSuchElementException("reached iterator end");

            @SuppressWarnings("unchecked")
            final T res = (T) display0[lo];
            lo += 1;

            if (lo == endLo) {
                if (blockIndex + lo < endIndex) {
                    int newBlockIndex = blockIndex + 32;
                    gotoNextBlockStart(newBlockIndex, blockIndex ^ newBlockIndex);

                    blockIndex = newBlockIndex;
                    endLo = Math.min(endIndex - blockIndex, 32);
                    lo = 0;
                } else {
                    hasNext = false;
                }
            }

            return res;
        }

        @Override public AIterator<T> filter (Predicate<T> f) {
            return AbstractAIterator.filter(this, f);
        }
    }


    static class VectorPointer<T> implements Serializable {
        int depth = 0;
        Object[] display0 = null;
        Object[] display1 = null;
        Object[] display2 = null;
        Object[] display3 = null;
        Object[] display4 = null;
        Object[] display5 = null;

        // used
        public void initFrom(VectorPointer<T> that) {
            initFrom(that, that.depth);
        }

        public void initFrom(VectorPointer<T> that, int depth) {
            this.depth = depth;

            switch (depth - 1) {
                case -1:
                    break;
                case 0:
                    display0 = that.display0;
                    break;
                case 1:
                    display1 = that.display1;
                    display0 = that.display0;
                    break;
                case 2:
                    display2 = that.display2;
                    display1 = that.display1;
                    display0 = that.display0;
                    break;
                case 3:
                    display3 = that.display3;
                    display2 = that.display2;
                    display1 = that.display1;
                    display0 = that.display0;
                    break;
                case 4:
                    display4 = that.display4;
                    display3 = that.display3;
                    display2 = that.display2;
                    display1 = that.display1;
                    display0 = that.display0;
                    break;
                case 5:
                    display5 = that.display5;
                    display4 = that.display4;
                    display3 = that.display3;
                    display2 = that.display2;
                    display1 = that.display1;
                    display0 = that.display0;
                    break;
                default:
            }
        }


        // requires structure is at pos oldIndex = xor ^ index
        @SuppressWarnings("unchecked")
        public T getElem(int index, int xor) {
            if (xor < (1 <<  5)) return (T)                                                             display0                                                                                                        [index & 31];
            if (xor < (1 << 10)) return (T)                                                 ((Object[]) display1                                                                                    [(index >> 5) & 31])[index & 31];
            if (xor < (1 << 15)) return (T)                                     ((Object[]) ((Object[]) display2                                                               [(index >> 10) & 31])[(index >> 5) & 31])[index & 31];
            if (xor < (1 << 20)) return (T)                         ((Object[]) ((Object[]) ((Object[]) display3                                          [(index >> 15) & 31])[(index >> 10) & 31])[(index >> 5) & 31])[index & 31];
            if (xor < (1 << 25)) return (T)             ((Object[]) ((Object[]) ((Object[]) ((Object[]) display4                     [(index >> 20) & 31])[(index >> 15) & 31])[(index >> 10) & 31])[(index >> 5) & 31])[index & 31];
            if (xor < (1 << 30)) return (T) ((Object[]) ((Object[]) ((Object[]) ((Object[]) ((Object[]) display5[(index >> 25) & 31])[(index >> 20) & 31])[(index >> 15) & 31])[(index >> 10) & 31])[(index >> 5) & 31])[index & 31];

            throw new IllegalArgumentException(); // level = 6
        }

        // go to specific position
        // requires structure is at pos oldIndex = xor ^ index,
        // ensures structure is at pos index
        public void gotoPos(int index, int xor) {
            //noinspection StatementWithEmptyBody
            if (xor < (1 << 5)) { // level = 0 (could maybe removed)
            } else if (xor < (1 << 10)) { // level = 1
                display0 = (Object[]) display1[(index >> 5) & 31];
            } else if (xor < (1 << 15)) { // level = 2
                display1 = (Object[]) display2[(index >> 10) & 31];
                display0 = (Object[]) display1[(index >> 5) & 31];
            } else if (xor < (1 << 20)) { // level = 3
                display2 = (Object[]) display3[(index >> 15) & 31];
                display1 = (Object[]) display2[(index >> 10) & 31];
                display0 = (Object[]) display1[(index >> 5) & 31];
            } else if (xor < (1 << 25)) { // level = 4
                display3 = (Object[]) display4[(index >> 20) & 31];
                display2 = (Object[]) display3[(index >> 15) & 31];
                display1 = (Object[]) display2[(index >> 10) & 31];
                display0 = (Object[]) display1[(index >> 5) & 31];
            } else if (xor < (1 << 30)) { // level = 5
                display4 = (Object[]) display5[(index >> 25) & 31];
                display3 = (Object[]) display4[(index >> 20) & 31];
                display2 = (Object[]) display3[(index >> 15) & 31];
                display1 = (Object[]) display2[(index >> 10) & 31];
                display0 = (Object[]) display1[(index >> 5) & 31];
            } else { // level = 6
                throw new IllegalArgumentException();
            }
        }


        // USED BY ITERATOR
        // xor: oldIndex ^ index
        public void gotoNextBlockStart(int index, int xor) { // goto block start pos
            if (xor < (1 << 10)) { // level = 1
                display0 = (Object[]) display1[(index >> 5) & 31];
            } else if (xor < (1 << 15)) { // level = 2
                display1 = (Object[]) display2[(index >> 10) & 31];
                display0 = (Object[]) display1[0];
            } else if (xor < (1 << 20)) { // level = 3
                display2 = (Object[]) display3[(index >> 15) & 31];
                display1 = (Object[]) display2[0];
                display0 = (Object[]) display1[0];
            } else if (xor < (1 << 25)) { // level = 4
                display3 = (Object[]) display4[(index >> 20) & 31];
                display2 = (Object[]) display3[0];
                display1 = (Object[]) display2[0];
                display0 = (Object[]) display1[0];
            } else if (xor < (1 << 30)) { // level = 5
                display4 = (Object[]) display5[(index >> 25) & 31];
                display3 = (Object[]) display4[0];
                display2 = (Object[]) display3[0];
                display1 = (Object[]) display2[0];
                display0 = (Object[]) display1[0];
            } else { // level = 6
                throw new IllegalArgumentException();
            }
        }

        // USED BY BUILDER
        // xor: oldIndex ^ index
        public void gotoNextBlockStartWritable(int index, int xor) { // goto block start pos
            if (xor < (1 << 10)) { // level = 1
                if (depth == 1) {
                    display1 = new Object[32];
                    display1[0] = display0;
                    depth += 1;
                }
                display0 = new Object[32];
                display1[(index >> 5) & 31] = display0;
            } else if (xor < (1 << 15)) { // level = 2
                if (depth == 2) {
                    display2 = new Object[32];
                    display2[0] = display1;
                    depth += 1;
                }
                display0 = new Object[32];
                display1 = new Object[32];
                display1[(index >> 5) & 31] = display0;
                display2[(index >> 10) & 31] = display1;
            } else if (xor < (1 << 20)) { // level = 3
                if (depth == 3) {
                    display3 = new Object[32];
                    display3[0] = display2;
                    depth += 1;
                }
                display0 = new Object[32];
                display1 = new Object[32];
                display2 = new Object[32];
                display1[(index >> 5) & 31] = display0;
                display2[(index >> 10) & 31] = display1;
                display3[(index >> 15) & 31] = display2;
            } else if (xor < (1 << 25)) { // level = 4
                if (depth == 4) {
                    display4 = new Object[32];
                    display4[0] = display3;
                    depth += 1;
                }
                display0 = new Object[32];
                display1 = new Object[32];
                display2 = new Object[32];
                display3 = new Object[32];
                display1[(index >> 5) & 31] = display0;
                display2[(index >> 10) & 31] = display1;
                display3[(index >> 15) & 31] = display2;
                display4[(index >> 20) & 31] = display3;
            } else if (xor < (1 << 30)) { // level = 5
                if (depth == 5) {
                    display5 = new Object[32];
                    display5[0] = display4;
                    depth += 1;
                }
                display0 = new Object[32];
                display1 = new Object[32];
                display2 = new Object[32];
                display3 = new Object[32];
                display4 = new Object[32];
                display1[(index >> 5) & 31] = display0;
                display2[(index >> 10) & 31] = display1;
                display3[(index >> 15) & 31] = display2;
                display4[(index >> 20) & 31] = display3;
                display5[(index >> 25) & 31] = display4;
            } else { // level = 6
                throw new IllegalArgumentException();
            }
        }

        // STUFF BELOW USED BY APPEND / UPDATE
        public Object[] copyOf(Object[] a) {
            final Object[] result = new Object[32];
            System.arraycopy(a, 0, result, 0, 32);
            return result;
        }

        public Object[] nullSlotAndCopy(Object[] array, int index) {
            final Object result = array[index];
            array[index] = null;
            return copyOf((Object[]) result);
        }

        // make sure there is no aliasing
        // requires structure is at pos index
        // ensures structure is clean and at pos index and writable at all levels except 0
        public void stabilize(int index) {
            switch (depth - 1) {
                case 5:
                    display5 = copyOf(display5);
                    display4 = copyOf(display4);
                    display3 = copyOf(display3);
                    display2 = copyOf(display2);
                    display1 = copyOf(display1);
                    display5[(index >> 25) & 31] = display4;
                    display4[(index >> 20) & 31] = display3;
                    display3[(index >> 15) & 31] = display2;
                    display2[(index >> 10) & 31] = display1;
                    display1[(index >> 5) & 31] = display0;
                    break;
                case 4:
                    display4 = copyOf(display4);
                    display3 = copyOf(display3);
                    display2 = copyOf(display2);
                    display1 = copyOf(display1);
                    display4[(index >> 20) & 31] = display3;
                    display3[(index >> 15) & 31] = display2;
                    display2[(index >> 10) & 31] = display1;
                    display1[(index >> 5) & 31] = display0;
                    break;
                case 3:
                    display3 = copyOf(display3);
                    display2 = copyOf(display2);
                    display1 = copyOf(display1);
                    display3[(index >> 15) & 31] = display2;
                    display2[(index >> 10) & 31] = display1;
                    display1[(index >> 5) & 31] = display0;
                    break;
                case 2:
                    display2 = copyOf(display2);
                    display1 = copyOf(display1);
                    display2[(index >> 10) & 31] = display1;
                    display1[(index >> 5) & 31] = display0;
                    break;
                case 1:
                    display1 = copyOf(display1);
                    display1[(index >> 5) & 31] = display0;
                    break;
                case 0:
                    break;
            }
        }


        /// USED IN UPDATE AND APPEND BACK
        // prepare for writing at an existing position

        // requires structure is clean and at pos oldIndex = xor ^ newIndex,
        // ensures structure is dirty and at pos newIndex and writable at level 0
        public void gotoPosWritable0(int newIndex) {
            switch (depth - 1) {
                case 5:
                    display5 = copyOf(display5);
                    display4 = nullSlotAndCopy(display5, (newIndex >> 25) & 31);
                    display3 = nullSlotAndCopy(display4, (newIndex >> 20) & 31);
                    display2 = nullSlotAndCopy(display3, (newIndex >> 15) & 31);
                    display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
                    display0 = nullSlotAndCopy(display1, (newIndex >>  5) & 31);
                    break;
                case 4:
                    display4 = copyOf(display4);
                    display3 = nullSlotAndCopy(display4, (newIndex >> 20) & 31);
                    display2 = nullSlotAndCopy(display3, (newIndex >> 15) & 31);
                    display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
                    display0 = nullSlotAndCopy(display1, (newIndex >>  5) & 31);
                    break;
                case 3:
                    display3 = copyOf(display3);
                    display2 = nullSlotAndCopy(display3, (newIndex >> 15) & 31);
                    display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
                    display0 = nullSlotAndCopy(display1, (newIndex >>  5) & 31);
                    break;
                case 2:
                    display2 = copyOf(display2);
                    display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
                    display0 = nullSlotAndCopy(display1, (newIndex >>  5) & 31);
                    break;
                case 1:
                    display1 = copyOf(display1);
                    display0 = nullSlotAndCopy(display1, (newIndex >> 5) & 31);
                    break;
                case 0:
                    display0 = copyOf(display0);
                    break;
                default:
            }
        }

        // requires structure is dirty and at pos oldIndex,
        // ensures structure is dirty and at pos newIndex and writable at level 0
        public void gotoPosWritable1(int oldIndex, int newIndex, int xor) {
            if (xor < (1 << 5)) { // level = 0
                display0 = copyOf(display0);
            }
            else if (xor < (1 << 10)) { // level = 1
                display1 = copyOf(display1);
                display1[(oldIndex >> 5) & 31] = display0;
                display0 = nullSlotAndCopy(display1, (newIndex >> 5) & 31);
            }
            else if (xor < (1 << 15)) { // level = 2
                display1 = copyOf(display1);
                display2 = copyOf(display2);
                display1[(oldIndex >>  5) & 31] = display0;
                display2[(oldIndex >> 10) & 31] = display1;
                display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
                display0 = nullSlotAndCopy(display1, (newIndex >>  5) & 31);
            }
            else if (xor < (1 << 20)) { // level = 3
                display1 = copyOf(display1);
                display2 = copyOf(display2);
                display3 = copyOf(display3);
                display1[(oldIndex >>  5) & 31] = display0;
                display2[(oldIndex >> 10) & 31] = display1;
                display3[(oldIndex >> 15) & 31] = display2;
                display2 = nullSlotAndCopy(display3, (newIndex >> 15) & 31);
                display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
                display0 = nullSlotAndCopy(display1, (newIndex >>  5) & 31);
            }
            else if (xor < (1 << 25)) { // level = 4
                display1 = copyOf(display1);
                display2 = copyOf(display2);
                display3 = copyOf(display3);
                display4 = copyOf(display4);
                display1[(oldIndex >>  5) & 31] = display0;
                display2[(oldIndex >> 10) & 31] = display1;
                display3[(oldIndex >> 15) & 31] = display2;
                display4[(oldIndex >> 20) & 31] = display3;
                display3 = nullSlotAndCopy(display4, (newIndex >> 20) & 31);
                display2 = nullSlotAndCopy(display3, (newIndex >> 15) & 31);
                display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
                display0 = nullSlotAndCopy(display1, (newIndex >>  5) & 31);
            }
            else if (xor < (1 << 30)) { // level = 5
                display1 = copyOf(display1);
                display2 = copyOf(display2);
                display3 = copyOf(display3);
                display4 = copyOf(display4);
                display5 = copyOf(display5);
                display1[(oldIndex >>  5) & 31] = display0;
                display2[(oldIndex >> 10) & 31] = display1;
                display3[(oldIndex >> 15) & 31] = display2;
                display4[(oldIndex >> 20) & 31] = display3;
                display5[(oldIndex >> 25) & 31] = display4;
                display4 = nullSlotAndCopy(display5, (newIndex >> 25) & 31);
                display3 = nullSlotAndCopy(display4, (newIndex >> 20) & 31);
                display2 = nullSlotAndCopy(display3, (newIndex >> 15) & 31);
                display1 = nullSlotAndCopy(display2, (newIndex >> 10) & 31);
                display0 = nullSlotAndCopy(display1, (newIndex >>  5) & 31);
            }
            else { // level = 6
                throw new IllegalArgumentException();
            }
        }

        // USED IN DROP
        public Object[] copyRange(Object[] array, int oldLeft, int newLeft) {
            final Object[] result = new Object[32];
            System.arraycopy(array, oldLeft, result, newLeft, 32 - Math.max(newLeft, oldLeft));
            return result;
        }

        // USED IN APPEND
        // create a new block at the bottom level (and possibly nodes on its path) and prepares for writing
        // requires structure is clean and at pos oldIndex,
        // ensures structure is dirty and at pos newIndex and writable at level 0
        public void gotoFreshPosWritable0(int oldIndex, int newIndex, int xor) { // goto block start pos
            //noinspection StatementWithEmptyBody
            if (xor < (1 << 5)) { // level = 0
            }
            else if (xor < (1 << 10)) { // level = 1
                if (depth == 1) {
                    display1 = new Object[32];
                    display1[(oldIndex >> 5) & 31] = display0;
                    depth += 1;
                }
                display0 = new Object[32];
            }
            else if (xor < (1 << 15)) { // level = 2
                if (depth == 2) {
                    display2 = new Object[32];
                    display2[(oldIndex >> 10) & 31] = display1;
                    depth += 1;
                }
                display1 = (Object[]) display2[(newIndex >> 10) & 31];
                if (display1 == null) display1 = new Object[32];
                display0 = new Object[32];
            }
            else if (xor < (1 << 20)) { // level = 3
                if (depth == 3) {
                    display3 = new Object[32];
                    display3[(oldIndex >> 15) & 31] = display2;
                    display2 = new Object[32];
                    display1 = new Object[32];
                    depth += 1;
                }
                display2 = (Object[]) display3[(newIndex >> 15) & 31];
                if (display2 == null) display2 = new Object[32];
                display1 = (Object[]) display2[(newIndex >> 10) & 31];
                if (display1 == null) display1 = new Object[32];
                display0 = new Object[32];
            }
            else if (xor < (1 << 25)) { // level = 4
                if (depth == 4) {
                    display4 = new Object[32];
                    display4[(oldIndex >> 20) & 31] = display3;
                    display3 = new Object[32];
                    display2 = new Object[32];
                    display1 = new Object[32];
                    depth += 1;
                }
                display3 = (Object[]) display4[(newIndex >> 20) & 31];
                if (display3 == null) display3 = new Object[32];
                display2 = (Object[]) display3[(newIndex >> 15) & 31];
                if (display2 == null) display2 = new Object[32];
                display1 = (Object[]) display2[(newIndex >> 10) & 31];
                if (display1 == null) display1 = new Object[32];
                display0 = new Object[32];
            }
            else if (xor < (1 << 30)) { // level = 5
                if (depth == 5) {
                    display5 = new Object[32];
                    display5[(oldIndex >> 25) & 31] = display4;
                    display4 = new Object[32];
                    display3 = new Object[32];
                    display2 = new Object[32];
                    display1 = new Object[32];
                    depth += 1;
                }
                display4 = (Object[]) display5[(newIndex >> 25) & 31];
                if (display4 == null) display4 = new Object[32];
                display3 = (Object[]) display4[(newIndex >> 20) & 31];
                if (display3 == null) display3 = new Object[32];
                display2 = (Object[]) display3[(newIndex >> 15) & 31];
                if (display2 == null) display2 = new Object[32];
                display1 = (Object[]) display2[(newIndex >> 10) & 31];
                if (display1 == null) display1 = new Object[32];
                display0 = new Object[32];
            }
            else { // level = 6
                throw new IllegalArgumentException();
            }
        }

        // requires structure is dirty and at pos oldIndex,
        // ensures structure is dirty and at pos newIndex and writable at level 0
        public void gotoFreshPosWritable1(int oldIndex, int newIndex, int xor) {
            stabilize(oldIndex);
            gotoFreshPosWritable0(oldIndex, newIndex, xor);
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> implements ACollectionBuilder<T,AVector<T>> {
        private final VectorPointer<T> pointer = new VectorPointer<T>();

        int blockIndex = 0;
        int lo = 0;

        private boolean wasBuilt = false;

        // possible alternative: start with display0 = null, blockIndex = -32, lo = 32
        // to avoid allocating initial array if the result will be empty anyways

        Builder () {
            pointer.display0 = new Object[32];
            pointer.depth = 1;
        }

        public Builder<T> add(T elem) {
            if (wasBuilt) throw new IllegalStateException();

            if (lo >= pointer.display0.length) {
                int newBlockIndex = blockIndex + 32;
                pointer.gotoNextBlockStartWritable(newBlockIndex, blockIndex ^ newBlockIndex);
                blockIndex = newBlockIndex;
                lo = 0;
            }
            pointer.display0[lo] = elem;
            lo += 1;
            return this;
        }

        public AVector<T> build() {
            if (wasBuilt) throw new IllegalStateException();
            wasBuilt = true;

            final int size = blockIndex + lo;
            if (size == 0) return empty();

            // should focus front or back?
            final AVector<T> s = new AVector<>(0, size, 0);

            s.pointer.initFrom(pointer);
            if (pointer.depth > 1) s.pointer.gotoPos(0, size - 1); // we're currently focused to size - 1, not size!
            return s;
        }
    }
}
