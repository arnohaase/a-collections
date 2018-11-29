package com.ajjpj.acollections.immutable.s

import scala.annotation.meta.getter


class Tree[A, +B](@(inline @getter) final val key: A,
                  @(inline @getter) final val value: B,
                  @(inline @getter) final val left: Tree[A, B],
                  @(inline @getter) final val right: Tree[A, B])
