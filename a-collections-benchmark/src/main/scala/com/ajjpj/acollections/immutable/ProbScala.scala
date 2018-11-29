package com.ajjpj.acollections.immutable

import scala.annotation.meta.getter


object ProbScala {

  def update[A: Ordering, B, B1 >: B](tree: Tree[A, B], k: A, v: B1, overwrite: Boolean): Tree[A, B1] = upd(tree, k, v, overwrite)
  private[this] def isBlackTree(tree: Tree[_, _]) = tree.isInstanceOf[BlackTree[_, _]]

  private[this] def mkTree[A, B](isBlack: Boolean, k: A, v: B, l: Tree[A, B], r: Tree[A, B]) =
    if (isBlack) BlackTree(k, v, l, r) else RedTree(k, v, l, r)

  private[this] def upd[A, B, B1 >: B](tree: Tree[A, B], k: A, v: B1, overwrite: Boolean)(implicit ordering: Ordering[A]): Tree[A, B1] = if (tree eq null) {
    RedTree(k, v, null, null)
  } else {
    val cmp = ordering.compare(k, tree.key)
    if (overwrite || k != tree.key) mkTree(isBlackTree(tree), k, v, tree.left, tree.right)
    else tree
  }

  /*
   * Forcing direct fields access using the @inline annotation helps speed up
   * various operations (especially smallest/greatest and update/delete).
   *
   * Unfortunately the direct field access is not guaranteed to work (but
   * works on the current implementation of the Scala compiler).
   *
   * An alternative is to implement the these classes using plain old Java code...
   */
  sealed abstract class Tree[A, +B](
                                     @(inline @getter) final val key: A,
                                     @(inline @getter) final val value: B,
                                     @(inline @getter) final val left: Tree[A, B],
                                     @(inline @getter) final val right: Tree[A, B])
    extends Serializable {
    def black: Tree[A, B]
    def red: Tree[A, B]
  }
  final class RedTree[A, +B](key: A,
                             value: B,
                             left: Tree[A, B],
                             right: Tree[A, B]) extends Tree[A, B](key, value, left, right) {
    override def black: Tree[A, B] = BlackTree(key, value, left, right)
    override def red: Tree[A, B] = this
    override def toString: String = "RedTree(" + key + ", " + value + ", " + left + ", " + right + ")"
  }
  final class BlackTree[A, +B](key: A,
                               value: B,
                               left: Tree[A, B],
                               right: Tree[A, B]) extends Tree[A, B](key, value, left, right) {
    override def black: Tree[A, B] = this
    override def red: Tree[A, B] = RedTree(key, value, left, right)
    override def toString: String = "BlackTree(" + key + ", " + value + ", " + left + ", " + right + ")"
  }

  object RedTree {
    @inline def apply[A, B](key: A, value: B, left: Tree[A, B], right: Tree[A, B]) = new RedTree(key, value, left, right)
    def unapply[A, B](t: RedTree[A, B]) = Some((t.key, t.value, t.left, t.right))
  }
  object BlackTree {
    @inline def apply[A, B](key: A, value: B, left: Tree[A, B], right: Tree[A, B]) = new BlackTree(key, value, left, right)
    def unapply[A, B](t: BlackTree[A, B]) = Some((t.key, t.value, t.left, t.right))
  }
}
