package com.ajjpj.acollections.immutable

import java.util.Comparator

import s._



object ProbScala {

  def update[A <: Comparable[A], B, B1 >: B](tree: Tree[A, B], k: A, v: B1): Tree[A, B1] = upd(tree, k, v)

  private[this] def mkTree[A, B](k: A, v: B, l: Tree[A, B], r: Tree[A, B]) =
    new Tree(k, v, l, r)

  private[this] def upd[A <: Comparable[A], B, B1 >: B](tree: Tree[A, B], k: A, v: B1): Tree[A, B1] = if (tree eq null) {
    new Tree(k, v, null, null)
  } else {
    val cmp = Comparator.naturalOrder[A].compare(k, tree.key)
    if (k == tree.key) mkTree(k, v, tree.left, tree.right)
    else tree
  }
}
