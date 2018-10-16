package com.ajjpj.acollections.immutable;

import com.ajjpj.acollections.util.AOption;

import java.util.*;


class CompactRedBlackTree<X extends CompactRedBlackTree.EntryWithComparator>  {
    interface EntryWithComparator {
        int compareTo (EntryWithComparator other);
    }

    private final Tree<X> root;

    @SuppressWarnings("unchecked")
    private static final CompactRedBlackTree EMPTY = new CompactRedBlackTree(null);

    static <X extends EntryWithComparator> CompactRedBlackTree<X> empty () {
        //noinspection unchecked
        return EMPTY;
    }

    private CompactRedBlackTree (Tree<X> root) {
        this.root = root;
    }

    int size () {
        return root == null ? 0 : root.count;
    }
    boolean isEmpty() {
        return root == null;
    }

    X get (X key) {
        return AOption.of(lookup (root, key)).map(e -> e.entry).orNull();
    }

    CompactRedBlackTree<X> updated (X entry) {
        return new CompactRedBlackTree<> (blacken (upd (root, entry)));
    }

    CompactRedBlackTree<X> removed (X key) {
        return new CompactRedBlackTree<> (blacken (del (root, key)));
    }

    Iterator<X> iterator () {
        return new TreeIterator<X> () {
            @Override X nextResult (Tree<X> tree) {
                return tree.entry;
            }
        };
    }

    X first () {
        if (root == null) return null;

        Tree<X> cur = root;
        while (cur.left != null) cur = cur.left;

        return cur.entry;
    }

    X last () {
        if (root == null) return null;

        Tree<X> cur = root;
        while (cur.right != null) cur = cur.right;

        return cur.entry;
    }

    X firstGreaterThan (X key) {
        if (root == null) return null;

        Tree<X> cur = root;
        Tree<X> candidate = null;
        while (true) {
            final int cmp = cur.entry.compareTo(key);
            if (cmp <= 0) {
                // this node is smaller than the key --> go right
                if (cur.right == null) return AOption.of(candidate).map(e -> e.entry).orNull();
                cur = cur.right;
            }
            else {
                // this node is greater than the key --> go left
                if (cur.left == null) return cur.entry;
                candidate = cur;
                cur = cur.left;
            }
        }
    }

    X firstGreaterOrEquals (X key) {
        if (root == null) return null;

        Tree<X> cur = root;
        Tree<X> candidate = null;
        while (true) {
            final int cmp = cur.entry.compareTo(key);
            if (cmp == 0) return cur.entry;
            if (cmp < 0) {
                // this node is smaller than the key --> go right
                if (cur.right == null) return AOption.of(candidate).map(e -> e.entry).orNull();
                cur = cur.right;
            }
            else {
                // this node is greater than the key --> go left
                if (cur.left == null) return cur.entry;
                candidate = cur;
                cur = cur.left;
            }
        }
    }

    public X lastSmallerThan (X key) {
        if (root == null) return null;

        Tree<X> cur = root;
        Tree<X> candidate = null;
        while (true) {
            final int cmp = cur.entry.compareTo(key);
            if (cmp >= 0) {
                // this node is greater than the key --> go left
                if (cur.left == null) return AOption.of(candidate).map(e -> e.entry).orNull();
                cur = cur.left;
            }
            else {
                // this node is smaller than the key --> go right
                if (cur.right == null) return cur.entry;
                candidate = cur;
                cur = cur.right;
            }
        }
    }

    X lastSmallerOrEquals (X key) {
        if (root == null) return null;

        Tree<X> cur = root;
        Tree<X> candidate = null;
        while (true) {
            final int cmp = cur.entry.compareTo(key);
            if (cmp == 0) return cur.entry;
            if (cmp > 0) {
                // this node is greater than the key --> go left
                if (cur.left == null) return AOption.of(candidate).map(e -> e.entry).orNull();
                cur = cur.left;
            }
            else {
                // this node is smaller than the key --> go right
                if (cur.right == null) return cur.entry;
                candidate = cur;
                cur = cur.right;
            }
        }
    }

    Iterable<X> rangeII (final X fromKey, final X toKey) {
        return () -> CompactRedBlackTree.this.iterator (fromKey, toKey, true, true);
    }

    Iterable<X> rangeIE (final X fromKey, final X toKey) {
        return () -> CompactRedBlackTree.this.iterator (fromKey, toKey, true, false);
    }

    Iterable<X> rangeEI (final X fromKey, final X toKey) {
        return () -> CompactRedBlackTree.this.iterator (fromKey, toKey, false, true);
    }

    Iterable<X> rangeEE (final X fromKey, final X toKey) {
        return () -> CompactRedBlackTree.this.iterator (fromKey, toKey, false, false);
    }

    Iterable<X> fromI (final X fromKey) {
        return () -> CompactRedBlackTree.this.iterator (fromKey, null, true, false);
    }

    Iterable<X> fromE (final X fromKey) {
        return () -> CompactRedBlackTree.this.iterator (fromKey, null, false, false);
    }

    Iterable<X> toI (final X toKey) {
        return () -> CompactRedBlackTree.this.iterator (null, toKey, false, true);
    }

    Iterable<X> toE (final X toKey) {
        return () -> CompactRedBlackTree.this.iterator (null, toKey, false, false);
    }


    private Iterator<X> iterator (final X from, final X to, boolean fromInclusive, boolean toInclusive) {
        if (root == null) {
            return new TreeIterator<X> (null, null) {
                @Override X nextResult (Tree<X> tree) {
                    return null;
                }
            };
        }

        // this stack contains all nodes for which the left side was visited, while they themselves were not, and neither was their right side
        final ArrayDeque<Tree<X>> pathStack = new ArrayDeque<> ();

        Tree<X> cur = root;
        while (true) {
            final int cmp = (from == null) ? 1 : cur.entry.compareTo(from);
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
            return new TreeIterator<X> (pathStack, pathStack.isEmpty () ? null : pathStack.pop ()) {
                @Override X nextResult (Tree<X> tree) {
                    return tree.entry;
                }
            };
        }

        Tree<X> first = null;
        if (! pathStack.isEmpty ()) {
            first = pathStack.pop ();

            final int cmp = first.entry.compareTo(to);
            if (cmp > 0 || (cmp == 0 && !toInclusive)) {
                first = null;
            }
        }

        if (toInclusive) {
            return new TreeIterator<X> (pathStack, first) {
                @Override X nextResult (Tree<X> tree) {
                    return tree.entry;
                }
                @Override protected boolean isAfterIntendedEnd (Tree<X> tree) {
                    return tree.entry.compareTo(to) > 0;
                }
            };
        }
        else {
            return new TreeIterator<X> (pathStack, first) {
                @Override X nextResult (Tree<X> tree) {
                    return tree.entry;
                }
                @Override protected boolean isAfterIntendedEnd (Tree<X> tree) {
                    return tree.entry.compareTo(to) >= 0;
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
                throw new IllegalStateException ("tree " + tree.entry + " is redden and has a left child that is redden");
            }
            if (isRedTree (tree.right)) {
                throw new IllegalStateException ("tree " + tree.entry + " is redden and has a right child that is redden");
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
            throw new IllegalStateException ("left and right side have paths to leaf nodes with different numbers of blacken nodes: " + tree.entry);
        }
        return own + left;
    }


    private abstract class TreeIterator<R> implements Iterator<R> {
        @SuppressWarnings ("unchecked")
        private final ArrayDeque<Tree<X>> pathStack;
        private Tree<X> next;

        abstract R nextResult (Tree<X> tree); //TODO rename this

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

        private TreeIterator (ArrayDeque<Tree<X>> pathStack, Tree<X> first) {
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

            final Tree<X> cur = next;
            next = filteredFindNext (next.right);
            return nextResult (cur);
        }

        @Override public void remove () {
            throw new UnsupportedOperationException ();
        }

        private Tree<X> filteredFindNext (Tree<X> tree) {
            final Tree<X> result = findNext (tree);
            if (result == null || isAfterIntendedEnd (result)) return null;
            return result;
        }

        private Tree<X> findNext (Tree<X> tree) {
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
        protected boolean isAfterIntendedEnd (Tree<X> tree) {
            return false;
        }

        private Tree<X> popPath() {
            if (pathStack.isEmpty ()) {
                // convenience for handling the end of iteration
                return null;
            }
            return pathStack.pop ();
        }
    }



    private static <X extends EntryWithComparator> Tree<X> lookup(Tree<X> tree, X key) {
        while (tree != null) {
            final int cmp = key.compareTo(tree.entry);
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

    private static <X extends EntryWithComparator> Tree<X> blacken (Tree<X> tree) {
        if (tree == null) {
            return null;
        }
        return tree.blacken ();
    }

    private static <X extends EntryWithComparator> Tree<X> balanceLeft (TreeFactory<X> treeFactory, X entry, Tree<X> l, Tree<X> d) {
        if (isRedTree (l) && isRedTree (l.left)) {
            return new RedTree<> (l.entry,
                    new BlackTree<> (l.left.entry, l.left.left, l.left.right),
                    new BlackTree<> (entry, l.right, d));
        }
        if (isRedTree (l) && isRedTree (l.right)) {
            return new RedTree<> (l.right.entry,
                    new BlackTree<> (l.entry, l.left, l.right.left),
                    new BlackTree<> (entry, l.right.right, d));
        }
        return treeFactory.create (l, d);
    }

    private static <X extends EntryWithComparator> Tree<X> balanceRight (TreeFactory<X> treeFactory, X entry, Tree<X> a, Tree<X> r) {
        if (isRedTree (r) && isRedTree (r.left)) {
            return new RedTree<> (r.left.entry,
                    new BlackTree<> (entry, a, r.left.left),
                    new BlackTree<> (r.entry, r.left.right, r.right));
        }
        if (isRedTree (r) && isRedTree (r.right)) {
            return new RedTree<> (r.entry,
                    new BlackTree<> (entry, a, r.left),
                    new BlackTree<> (r.right.entry, r.right.left, r.right.right));
        }
        return treeFactory.create (a, r);
    }

    private static <X extends EntryWithComparator> Tree<X> upd (Tree<X> tree, X entry) {
        if (tree == null) {
            return new RedTree<> (entry, null, null);
        }
        final int cmp = entry.compareTo(tree.entry);
        if (cmp < 0) {
            return balanceLeft (tree, tree.entry, upd (tree.left, entry), tree.right);
        }
        if (cmp > 0) {
            return balanceRight (tree, tree.entry, tree.left, upd (tree.right, entry));
        }
        return tree.withNewValue (entry);
    }

    private static <X extends EntryWithComparator> Tree<X> del (Tree<X> tree, X key) {
        if (tree == null) {
            return null;
        }

        final int cmp = key.compareTo(tree.entry);
        if (cmp < 0) {
            // the node that must be deleted is to the left
            return isBlackTree (tree.left) ?
                    balanceLeft (tree.entry, del (tree.left, key), tree.right) :

                    // tree.left is 'redden', so its children are guaranteed to be blacken.
                    new RedTree<> (tree.entry, del (tree.left, key), tree.right);
        }
        else if (cmp > 0) {
            // the node that must be deleted is to the right
            return isBlackTree (tree.right) ?
                    balanceRight (tree.entry, tree.left, del (tree.right, key)) :
                    new RedTree<> (tree.entry, tree.left, del (tree.right, key));
        }

        // delete this node and we are finished
        return append (tree.left, tree.right);
    }

    private static <X extends EntryWithComparator> Tree<X> balance (X entry, Tree<X> tl, Tree<X> tr) {
        if (isRedTree (tl) && isRedTree (tr)) return new RedTree<> (entry, tl.blacken (), tr.blacken ());

        if (isRedTree (tl)) {
            // left is redden, right is blacken
            if (isRedTree (tl.left)) return new RedTree<> (tl.entry, tl.left.blacken (), new BlackTree<> (entry, tl.right, tr));
            if (isRedTree (tl.right)) {
                return new RedTree<> (tl.right.entry,
                        new BlackTree<> (tl.entry, tl.left, tl.right.left),
                        new BlackTree<> (entry, tl.right.right, tr));
            }
            return new BlackTree<> (entry, tl, tr);
        }

        if (isRedTree (tr)) {
            // left is blacken, right is redden
            if (isRedTree (tr.right)) return new RedTree<> (tr.entry, new BlackTree<> (entry, tl, tr.left), tr.right.blacken ());
            if (isRedTree (tr.left))  return new RedTree<> (tr.left.entry, new BlackTree<> (entry, tl, tr.left.left), new BlackTree<> (tr.entry, tr.left.right, tr.right));
            return new BlackTree<> (entry, tl, tr);
        }

        // tl and tr are both blacken
        return new BlackTree<> (entry, tl, tr);
    }

    private static <X extends EntryWithComparator> Tree<X> balanceLeft (X entry, Tree<X> tl, Tree<X> tr) {
        if (isRedTree (tl)) {
            return new RedTree<> (entry, tl.blacken (), tr);
        }
        if (isBlackTree (tr)) {
            return balance (entry, tl, tr.redden ());
        }
        if (isRedTree (tr) && isBlackTree (tr.left)) {
            return new RedTree<> (tr.left.entry, new BlackTree<> (entry, tl, tr.left.left), balance (tr.entry, tr.left.right, tr.right.blackToRed ()));
        }
        throw new IllegalStateException ("invariant violation");
    }

    private static <X extends EntryWithComparator> Tree<X> balanceRight (X entry, Tree<X> tl, Tree<X> tr) {
        if (isRedTree (tr)) {
            return new RedTree<> (entry, tl, tr.blacken ());
        }
        if (isBlackTree (tl)) {
            return balance (entry, tl.redden (), tr);
        }
        if (isRedTree (tl) && isBlackTree (tl.right)) {
            return new RedTree<> (tl.right.entry, balance (tl.entry, tl.left.blackToRed (), tl.right.left), new BlackTree <> (entry, tl.right.right, tr));
        }
        throw new IllegalStateException ("invariant violation");
    }

    /**
     * This method combines two separate sub-trees into a single (balanced) tree. It assumes that both subtrees are
     *  balanced and that all elements in 'tl' are smaller than all elements in 'tr'. This situation occurs when a
     *  node is deleted and its child nodes must be combined into a resulting tree.
     */
    private static <X extends EntryWithComparator> Tree<X> append (Tree<X> tl, Tree<X> tr) {
        if (tl == null) return tr;
        if (tr == null) return tl;

        if (isRedTree (tl) && isRedTree (tr)) {
            final Tree<X> bc = append (tl.right, tr.left);
            return isRedTree (bc) ?
                    new RedTree<> (bc.entry, new RedTree<> (tl.entry, tl.left, bc.left), new RedTree<> (tr.entry, bc.right, tr.right)) :
                    new RedTree<> (tl.entry, tl.left, new RedTree<> (tr.entry, bc, tr.right));
        }
        if (isBlackTree (tl) && isBlackTree (tr)) {
            final Tree<X> bc = append (tl.right, tr.left);
            return isRedTree (bc) ?
                    new RedTree<> (bc.entry, new BlackTree<> (tl.entry, tl.left, bc.left), new BlackTree<> (tr.entry, bc.right, tr.right)) :
                    balanceLeft (tl.entry, tl.left, new BlackTree<> (tr.entry, bc, tr.right));
        }
        if (isRedTree (tr)) {
            return new RedTree<> (tr.entry, append (tl, tr.left), tr.right);
        }
        if (isRedTree (tl)) {
            return new RedTree<> (tl.entry, tl.left, append (tl.right, tr));
        }
        throw new IllegalStateException ("invariant violation: unmatched tree on append: " + tl + ", " + tr);
    }


    /**
     * encapsulates tree creation for a given colour
     */
    interface TreeFactory<X extends EntryWithComparator> {
        Tree<X> create (Tree<X> left, Tree<X> right);
    }

    static abstract class Tree<X extends EntryWithComparator> implements TreeFactory<X> {
        final X entry;
        final int count;

        final Tree<X> left;
        final Tree<X> right;

        Tree (X entry, Tree<X> left, Tree<X> right) {
            this.entry = entry;
            this.left = left;
            this.right = right;

            this.count = 1 +
                    (left == null ? 0 : left.count) +
                    (right == null ? 0 : right.count);
        }

        abstract Tree<X> withNewValue (X entry);

        abstract Tree<X> blackToRed();

        abstract boolean isRed();
        abstract boolean isBlack();

        abstract Tree<X> redden ();
        abstract Tree<X> blacken ();
    }

    static class BlackTree<X extends EntryWithComparator> extends Tree<X> {
        BlackTree (X entry, Tree<X> left, Tree<X> right) {
            super(entry, left, right);
        }

        @Override Tree<X> withNewValue (X entry) {
            return new BlackTree<> (entry, left, right);
        }
        @Override public Tree<X> create (Tree<X> left, Tree<X> right) {
            return new BlackTree<> (entry, left, right);
        }

        @Override Tree<X> blackToRed () {
            return redden ();
        }

        @Override boolean isRed () {
            return false;
        }
        @Override boolean isBlack () {
            return true;
        }

        @Override Tree<X> redden () {
            return new RedTree<> (entry, left, right);
        }
        @Override Tree<X> blacken () {
            return this;
        }
    }

    static class RedTree<X extends EntryWithComparator> extends Tree<X> {
        RedTree (X entry, Tree<X> left, Tree<X> right) {
            super (entry, left, right);
        }

        @Override Tree<X> withNewValue (X entry) {
            return new RedTree<> (entry, left, right);
        }
        @Override public Tree<X> create (Tree<X> left, Tree<X> right) {
            return new RedTree<> (entry, left, right);
        }

        @Override Tree<X> blackToRed () {
            throw new IllegalStateException ();
        }

        @Override boolean isRed () {
            return true;
        }
        @Override boolean isBlack () {
            return false;
        }

        @Override Tree<X> redden () {
            return this;
        }
        @Override Tree<X> blacken () {
            return new BlackTree<> (entry, left, right);
        }
    }
}
