package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.util.AOption;

import java.util.*;


class CompactRedBlackTree<K,V>  {
    private final Tree<K,V> root;
    private final Comparator<K> comparator;

    static <K,V> CompactRedBlackTree<K,V> empty (Comparator<K> comparator) {
        return new CompactRedBlackTree<> (null, comparator);
    }

    private CompactRedBlackTree (Tree<K, V> root, Comparator<K> comparator) {
        this.root = root;
        this.comparator = comparator;
    }

    int size () {
        return root == null ? 0 : root.count;
    }
    boolean isEmpty() {
        return root == null;
    }

    Map.Entry<K,V> get (K key) {
        return lookup (root, key, comparator);
    }

    CompactRedBlackTree<K, V> updated (K key, V value) {
        return new CompactRedBlackTree<> (blacken (upd (root, key, value, comparator)), comparator);
    }

    CompactRedBlackTree<K, V> removed (K key) {
        return new CompactRedBlackTree<> (blacken (del (root, key, comparator)), comparator);
    }

    Iterator<Map.Entry<K, V>> iterator () {
        return new TreeIterator<Map.Entry<K, V>> () {
            @Override Map.Entry<K, V> nextResult (Tree<K, V> tree) {
                return tree;
            }
        };
    }

    AOption<Map.Entry<K, V>> first () {
        if (root == null) return AOption.none ();

        Tree<K,V> cur = root;
        while (cur.left != null) cur = cur.left;

        return AOption.some (cur);
    }

    AOption<Map.Entry<K, V>> last () {
        if (root == null) return AOption.none ();

        Tree<K,V> cur = root;
        while (cur.right != null) cur = cur.right;

        return AOption.some (cur);
    }

    AOption<Map.Entry<K, V>> firstGreaterThan (K key) {
        if (root == null) return AOption.none ();

        Tree<K,V> cur = root;
        Tree<K,V> candidate = null;
        while (true) {
            final int cmp = comparator.compare (cur.key, key);
            if (cmp <= 0) {
                // this node is smaller than the key --> go right
                if (cur.right == null) return AOption.of(candidate);
                cur = cur.right;
            }
            else {
                // this node is greater than the key --> go left
                if (cur.left == null) return AOption.some (cur);
                candidate = cur;
                cur = cur.left;
            }
        }
    }

    AOption<Map.Entry<K, V>> firstGreaterOrEquals (K key) {
        if (root == null) return AOption.none ();

        Tree<K,V> cur = root;
        Tree<K,V> candidate = null;
        while (true) {
            final int cmp = comparator.compare (cur.key, key);
            if (cmp == 0) return AOption.some (cur);
            if (cmp < 0) {
                // this node is smaller than the key --> go right
                if (cur.right == null) return AOption.of(candidate);
                cur = cur.right;
            }
            else {
                // this node is greater than the key --> go left
                if (cur.left == null) return AOption.some (cur);
                candidate = cur;
                cur = cur.left;
            }
        }
    }

    public AOption<Map.Entry<K, V>> lastSmallerThan (K key) {
        if (root == null) return AOption.none ();

        Tree<K,V> cur = root;
        Tree<K,V> candidate = null;
        while (true) {
            final int cmp = comparator.compare (cur.key, key);
            if (cmp >= 0) {
                // this node is greater than the key --> go left
                if (cur.left == null) return AOption.of(candidate);
                cur = cur.left;
            }
            else {
                // this node is smaller than the key --> go right
                if (cur.right == null) return AOption.some (cur);
                candidate = cur;
                cur = cur.right;
            }
        }
    }

    AOption<Map.Entry<K, V>> lastSmallerOrEquals (K key) {
        if (root == null) return AOption.none ();

        Tree<K,V> cur = root;
        Tree<K,V> candidate = null;
        while (true) {
            final int cmp = comparator.compare (cur.key, key);
            if (cmp == 0) return AOption.some (cur);
            if (cmp > 0) {
                // this node is greater than the key --> go left
                if (cur.left == null) return AOption.of(candidate);
                cur = cur.left;
            }
            else {
                // this node is smaller than the key --> go right
                if (cur.right == null) return AOption.some (cur);
                candidate = cur;
                cur = cur.right;
            }
        }
    }

    Iterable<Map.Entry<K, V>> rangeII (final K fromKey, final K toKey) {
        return () -> CompactRedBlackTree.this.iterator (fromKey, toKey, true, true);
    }

    Iterable<Map.Entry<K, V>> rangeIE (final K fromKey, final K toKey) {
        return () -> CompactRedBlackTree.this.iterator (fromKey, toKey, true, false);
    }

    Iterable<Map.Entry<K, V>> rangeEI (final K fromKey, final K toKey) {
        return () -> CompactRedBlackTree.this.iterator (fromKey, toKey, false, true);
    }

    Iterable<Map.Entry<K, V>> rangeEE (final K fromKey, final K toKey) {
        return () -> CompactRedBlackTree.this.iterator (fromKey, toKey, false, false);
    }

    Iterable<Map.Entry<K, V>> fromI (final K fromKey) {
        return () -> CompactRedBlackTree.this.iterator (fromKey, null, true, false);
    }

    Iterable<Map.Entry<K, V>> fromE (final K fromKey) {
        return () -> CompactRedBlackTree.this.iterator (fromKey, null, false, false);
    }

    Iterable<Map.Entry<K, V>> toI (final K toKey) {
        return () -> CompactRedBlackTree.this.iterator (null, toKey, false, true);
    }

    Iterable<Map.Entry<K, V>> toE (final K toKey) {
        return () -> CompactRedBlackTree.this.iterator (null, toKey, false, false);
    }


    private Iterator<Map.Entry<K,V>> iterator (final K from, final K to, boolean fromInclusive, boolean toInclusive) {
        if (root == null) {
            return new TreeIterator<Map.Entry<K, V>> (null, null) {
                @Override Map.Entry<K, V> nextResult (Tree<K, V> tree) {
                    return null;
                }
            };
        }

        // this stack contains all nodes for which the left side was visited, while they themselves were not, and neither was their right side
        final ArrayDeque<Tree<K,V>> pathStack = new ArrayDeque<> ();

        Tree<K, V> cur = root;
        while (true) {
            final int cmp = (from == null) ? 1 : comparator.compare (cur.key, from);
            if (cmp == 0 && fromInclusive) {
                pathStack.push (cur);
                break;
            }
            if (cmp <= 0) { // cmp == 0 can only happen if !fromInclusive
                // this node is smaller than the key --> go right
                if (cur.right == null) {
                    break;
                }
                // this node is not in range --> do not push
                cur = cur.right;
            }
            else {
                pathStack.push (cur);

                // this node is greater than the key --> go left
                if (cur.left == null) {
                    break;
                }

                cur = cur.left;
            }
        }

        if (to == null) {
            return new TreeIterator<Map.Entry<K, V>> (pathStack, pathStack.isEmpty () ? null : pathStack.pop ()) {
                @Override Map.Entry<K, V> nextResult (Tree<K, V> tree) {
                    return tree;
                }
            };
        }

        Tree<K,V> first = null;
        if (! pathStack.isEmpty ()) {
            first = pathStack.pop ();

            final int cmp = comparator.compare (first.key, to);
            if (cmp > 0 || (cmp == 0 && !toInclusive)) {
                first = null;
            }
        }

        if (toInclusive) {
            return new TreeIterator<Map.Entry<K, V>> (pathStack, first) {
                @Override Map.Entry<K, V> nextResult (Tree<K, V> tree) {
                    return tree;
                }
                @Override protected boolean isAfterIntendedEnd (Tree<K, V> tree) {
                    return comparator.compare (tree.key, to) > 0;
                }
            };
        }
        else {
            return new TreeIterator<Map.Entry<K, V>> (pathStack, first) {
                @Override Map.Entry<K, V> nextResult (Tree<K, V> tree) {
                    return tree;
                }
                @Override protected boolean isAfterIntendedEnd (Tree<K, V> tree) {
                    return comparator.compare (tree.key, to) >= 0;
                }
            };
        }
    }





    private static void validate(Tree tree) {
        if (tree == null) {
            return;
        }

        validate (tree.left);
        validate (tree.right);

        // rule 4: every redden node has two blacken children
        if (isRedTree (tree)) {
            if (isRedTree (tree.left)) {
                throw new IllegalStateException ("tree " + tree.key + " is redden and has a left child that is redden");
            }
            if (isRedTree (tree.right)) {
                throw new IllegalStateException ("tree " + tree.key + " is redden and has a right child that is redden");
            }
        }

        checkBlackDepth (tree);
    }

    private static int checkBlackDepth (Tree tree) {
        if (tree == null) {
            return 1;
        }

        final int own = isBlackTree (tree) ? 1 : 0;
        final int left  = checkBlackDepth (tree.left);
        final int right = checkBlackDepth (tree.right);

        // rule 5: every path to 'leaf' nodes must have the same number of blacken nodes
        if (left != right) {
            throw new IllegalStateException ("left and right side have paths to leaf nodes with different numbers of blacken nodes: " + tree.key);
        }
        return own + left;
    }


    private abstract class TreeIterator<R> implements Iterator<R> {
        @SuppressWarnings ("unchecked")
        private final ArrayDeque<Tree<K,V>> pathStack;
        private Tree<K,V> next;

        abstract R nextResult (Tree<K,V> tree); //TODO rename this

        @SuppressWarnings ("unchecked")
        private TreeIterator() {
            // initialize 'next' with the leftmost element
            if (root == null) {
                pathStack = null;
                next = null;
            }
            else {
                pathStack = new ArrayDeque<> ();
                next = root;
                while (next.left != null) {
                    pathStack.push (next);
                    next = next.left;
                }
            }
        }

        private TreeIterator (ArrayDeque<Tree<K,V>> pathStack, Tree<K,V> first) {
            this.pathStack = pathStack;
            this.next = first;
        }

        @Override public boolean hasNext() {
            return next != null;
        }

        @Override public R next() {
            if (next == null) {
                throw new NoSuchElementException();
            }

            final Tree<K,V> cur = next;
            next = filteredFindNext (next.right);
            return nextResult (cur);
        }

        @Override public void remove () {
            throw new UnsupportedOperationException ();
        }

        private Tree<K,V> filteredFindNext (Tree<K,V> tree) {
            final Tree<K,V> result = findNext (tree);
            if (result == null || isAfterIntendedEnd (result)) return null;
            return result;
        }

        private Tree<K,V> findNext (Tree<K,V> tree) {
            while (true) {
                if (tree == null) {
                    return popPath ();
                }
                if (tree.left == null) {
                    return tree;
                }
                pathStack.push (tree);
                tree = tree.left;
            }
        }

        /**
         * override if an iterator should finish before the entire map is traversed
         */
        protected boolean isAfterIntendedEnd (Tree<K,V> tree) {
            return false;
        }

        private Tree<K,V> popPath() {
            if (pathStack.isEmpty ()) {
                // convenience for handling the end of iteration
                return null;
            }
            return pathStack.pop ();
        }
    }



    private static <K,V> Tree<K,V> lookup(Tree<K,V> tree, K key, Comparator<K> comparator) {
        while (tree != null) {
            final int cmp = comparator.compare (key, tree.key);
            if (cmp == 0) return tree;

            tree = (cmp < 0) ? tree.left : tree.right;
        }
        return null;
    }


    private static boolean isRedTree (Tree tree) {
        return tree != null && tree.isRed ();
    }
    private static boolean isBlackTree (Tree tree) {
        return tree != null && tree.isBlack ();
    }

    private static <K,V> Tree<K,V> blacken (Tree<K, V> tree) {
        if (tree == null) {
            return null;
        }
        return tree.blacken ();
    }

    private static <K,V> Tree<K,V> balanceLeft (TreeFactory<K, V> treeFactory, K key, V value, Tree<K, V> l, Tree<K, V> d) {
        if (isRedTree (l) && isRedTree (l.left)) {
            return new RedTree<> (l.key, l.value,
                    new BlackTree<> (l.left.key, l.left.value, l.left.left, l.left.right),
                    new BlackTree<> (key, value, l.right, d));
        }
        if (isRedTree (l) && isRedTree (l.right)) {
            return new RedTree<> (l.right.key, l.right.value,
                    new BlackTree<> (l.key, l.value, l.left, l.right.left),
                    new BlackTree<> (key, value, l.right.right, d));
        }
        return treeFactory.create (l, d);
    }

    private static <K,V> Tree<K,V> balanceRight (TreeFactory<K, V> treeFactory, K key, V value, Tree<K, V> a, Tree<K, V> r) {
        if (isRedTree (r) && isRedTree (r.left)) {
            return new RedTree<> (r.left.key, r.left.value,
                    new BlackTree<> (key, value, a, r.left.left),
                    new BlackTree<> (r.key, r.value, r.left.right, r.right));
        }
        if (isRedTree (r) && isRedTree (r.right)) {
            return new RedTree<> (r.key, r.value,
                    new BlackTree<> (key, value, a, r.left),
                    new BlackTree<> (r.right.key, r.right.value, r.right.left, r.right.right));
        }
        return treeFactory.create (a, r);
    }

    private static <K,V> Tree<K,V> upd (Tree<K, V> tree, K key, V value, Comparator<K> comparator) {
        if (tree == null) {
            return new RedTree<> (key, value, null, null);
        }
        final int cmp = comparator.compare (key, tree.key);
        if (cmp < 0) {
            return balanceLeft (tree, tree.key, tree.value, upd (tree.left, key, value, comparator), tree.right);
        }
        if (cmp > 0) {
            return balanceRight (tree, tree.key, tree.value, tree.left, upd (tree.right, key, value, comparator));
        }
        return tree.withNewValue (key, value);
    }

    private static <K,V> Tree<K,V> del (Tree<K, V> tree, K key, Comparator<K> comparator) {
        if (tree == null) {
            return null;
        }

        final int cmp = comparator.compare(key, tree.key);
        if (cmp < 0) {
            // the node that must be deleted is to the left
            return isBlackTree (tree.left) ?
                    balanceLeft (tree.key, tree.value, del (tree.left, key, comparator), tree.right) :

                    // tree.left is 'redden', so its children are guaranteed to be blacken.
                    new RedTree<> (tree.key, tree.value, del (tree.left, key, comparator), tree.right);
        }
        else if (cmp > 0) {
            // the node that must be deleted is to the right
            return isBlackTree (tree.right) ?
                    balanceRight (tree.key, tree.value, tree.left, del (tree.right, key, comparator)) :
                    new RedTree<> (tree.key, tree.value, tree.left, del (tree.right, key, comparator));
        }

        // delete this node and we are finished
        return append (tree.left, tree.right);

    }

    private static <K,V> Tree<K,V> balance (K key, V value, Tree<K, V> tl, Tree<K, V> tr) {
        if (isRedTree (tl) && isRedTree (tr)) return new RedTree<> (key, value, tl.blacken (), tr.blacken ());

        if (isRedTree (tl)) {
            // left is redden, right is blacken
            if (isRedTree (tl.left)) return new RedTree<> (tl.key, tl.value, tl.left.blacken (), new BlackTree<> (key, value, tl.right, tr));
            if (isRedTree (tl.right)) {
                return new RedTree<> (tl.right.key, tl.right.value,
                        new BlackTree<> (tl.key, tl.value, tl.left, tl.right.left),
                        new BlackTree<> (key, value, tl.right.right, tr));
            }
            return new BlackTree<> (key, value, tl, tr);
        }

        if (isRedTree (tr)) {
            // left is blacken, right is redden
            if (isRedTree (tr.right)) return new RedTree<> (tr.key, tr.value, new BlackTree<> (key, value, tl, tr.left), tr.right.blacken ());
            if (isRedTree (tr.left))  return new RedTree<> (tr.left.key, tr.left.value, new BlackTree<> (key, value, tl, tr.left.left), new BlackTree<> (tr.key, tr.value, tr.left.right, tr.right));
            return new BlackTree<> (key, value, tl, tr);
        }

        // tl and tr are both blacken
        return new BlackTree<> (key, value, tl, tr);
    }

    private static <K,V> Tree<K,V> balanceLeft (K key, V value, Tree<K, V> tl, Tree<K, V> tr) { //TODO merge with other 'balanceLeft' method?
        if (isRedTree (tl)) {
            return new RedTree<> (key, value, tl.blacken (), tr);
        }
        if (isBlackTree (tr)) {
            return balance (key, value, tl, tr.redden ());
        }
        if (isRedTree (tr) && isBlackTree (tr.left)) {
            return new RedTree<> (tr.left.key, tr.left.value, new BlackTree<> (key, value, tl, tr.left.left), balance (tr.key, tr.value, tr.left.right, tr.right.blackToRed ()));
        }
        throw new IllegalStateException ("invariant violation");
    }

    private static <K,V> Tree<K,V> balanceRight (K key, V value, Tree<K, V> tl, Tree<K, V> tr) {
        if (isRedTree (tr)) {
            return new RedTree<> (key, value, tl, tr.blacken ());
        }
        if (isBlackTree (tl)) {
            return balance (key, value, tl.redden (), tr);
        }
        if (isRedTree (tl) && isBlackTree (tl.right)) {
            return new RedTree<> (tl.right.key, tl.right.value, balance (tl.key, tl.value, tl.left.blackToRed (), tl.right.left), new BlackTree <> (key, value, tl.right.right, tr));
        }
        throw new IllegalStateException ("invariant violation");
    }

    /**
     * This method combines two separate sub-trees into a single (balanced) tree. It assumes that both subtrees are
     *  balanced and that all elements in 'tl' are smaller than all elements in 'tr'. This situation occurs when a
     *  node is deleted and its child nodes must be combined into a resulting tree.
     */
    private static <K,V> Tree<K,V> append (Tree<K,V> tl, Tree<K,V> tr) {
        if (tl == null) return tr;
        if (tr == null) return tl;

        if (isRedTree (tl) && isRedTree (tr)) {
            final Tree<K,V> bc = append (tl.right, tr.left);
            return isRedTree (bc) ?
                    new RedTree<> (bc.key, bc.value, new RedTree<> (tl.key, tl.value, tl.left, bc.left), new RedTree<> (tr.key, tr.value, bc.right, tr.right)) :
                    new RedTree<> (tl.key, tl.value, tl.left, new RedTree<> (tr.key, tr.value, bc, tr.right));
        }
        if (isBlackTree (tl) && isBlackTree (tr)) {
            final Tree<K,V> bc = append (tl.right, tr.left);
            return isRedTree (bc) ?
                    new RedTree<> (bc.key, bc.value, new BlackTree<> (tl.key, tl.value, tl.left, bc.left), new BlackTree<> (tr.key, tr.value, bc.right, tr.right)) :
                    balanceLeft (tl.key, tl.value, tl.left, new BlackTree<> (tr.key, tr.value, bc, tr.right));
        }
        if (isRedTree (tr)) {
            return new RedTree<> (tr.key, tr.value, append (tl, tr.left), tr.right);
        }
        if (isRedTree (tl)) {
            return new RedTree<> (tl.key, tl.value, tl.left, append (tl.right, tr));
        }
        throw new IllegalStateException ("invariant violation: unmatched tree on append: " + tl + ", " + tr);
    }


    /**
     * encapsulates tree creation for a given colour
     */
    interface TreeFactory<K,V> {
        Tree<K,V> create (Tree<K,V> left, Tree<K,V> right);
    }

    static abstract class Tree<K,V> implements TreeFactory<K,V>, Map.Entry<K,V> {
        final K key;
        final V value;
        final int count;

        final Tree<K,V> left;
        final Tree<K,V> right;

        Tree (K key, V value, Tree<K, V> left, Tree<K, V> right) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;

            this.count = 1 +
                    (left == null ? 0 : left.count) +
                    (right == null ? 0 : right.count);
        }

        @Override public K getKey () {
            return key;
        }
        @Override public V getValue () {
            return value;
        }

        @Override public V setValue (V value) {
            throw new UnsupportedOperationException();
        }

        abstract Tree<K,V> withNewValue (K key, V value);

        abstract Tree<K,V> blackToRed();

        abstract boolean isRed();
        abstract boolean isBlack();

        abstract Tree<K,V> redden ();
        abstract Tree<K,V> blacken ();
    }

    static class BlackTree<K,V> extends Tree<K,V> {
        BlackTree (K key, V value, Tree<K, V> left, Tree<K, V> right) {
            super(key, value, left, right);
        }

        @Override Tree<K, V> withNewValue (K key, V value) {
            return new BlackTree<> (key, value, left, right);
        }
        @Override public Tree<K, V> create (Tree<K, V> left, Tree<K, V> right) {
            return new BlackTree<> (key, value, left, right);
        }

        @Override Tree<K, V> blackToRed () {
            return redden ();
        }

        @Override boolean isRed () {
            return false;
        }
        @Override boolean isBlack () {
            return true;
        }

        @Override Tree<K, V> redden () {
            return new RedTree<> (key, value, left, right);
        }
        @Override Tree<K, V> blacken () {
            return this;
        }
    }

    static class RedTree<K,V> extends Tree<K,V> {
        RedTree (K key, V value, Tree<K, V> left, Tree<K, V> right) {
            super (key, value, left, right);
        }

        @Override Tree<K, V> withNewValue (K key, V value) {
            return new RedTree<> (key, value, left, right);
        }
        @Override public Tree<K, V> create (Tree<K, V> left, Tree<K, V> right) {
            return new RedTree<> (key, value, left, right);
        }

        @Override Tree<K, V> blackToRed () {
            throw new IllegalStateException ();
        }

        @Override boolean isRed () {
            return true;
        }
        @Override boolean isBlack () {
            return false;
        }

        @Override Tree<K, V> redden () {
            return this;
        }
        @Override Tree<K, V> blacken () {
            return new BlackTree<> (key, value, left, right);
        }
    }
}
