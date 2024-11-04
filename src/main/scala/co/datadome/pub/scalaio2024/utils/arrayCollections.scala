package co.datadome.pub.scalaio2024.utils

import scala.annotation.targetName
import scala.reflect.ClassTag

opaque type ArrayMap[A, B] = Array[B]

object ArrayMap {
  extension [A: Identifiable, B](m: ArrayMap[A, B]) {
    inline def capacity: Int = m.length

    inline def apply(inline a: A): B = m(a.id)

    inline def update(inline a: A, inline b: B): Unit = {
      m(a.id) = b
    }

    inline def values: Array[B] = m

    inline def updated(inline a: A, inline b: B)(using inline ct: ClassTag[B]): ArrayMap[A, B] = {
      val m2 = Array.copyOf(m, m.length)
      m2(a.id) = b
      m2
    }

    inline def updated(inline abs: (A, B)*)(using inline ct: ClassTag[B]): ArrayMap[A, B] = {
      val m2 = Array.copyOf(m, m.length)
      abs.fastForeach { case (a, b) => m2(a.id) = b }
      m2
    }

    /** Note: this assume that both ArrayMap's capacities are the same. */
    @targetName("addAll")
    inline def ++(inline that: ArrayMap[A, B]): ArrayMap[A, B] = {
      val m2 = Array.copyOf(m, m.length)
      fastLoop(0, that.length) { i => m2(i) = that(i) }
      m2
    }
  }


  inline def fill[A: Identifiable, B: ClassTag](inline size: Int, inline default: => B): ArrayMap[A, B] = {
    Array.fill[B](size)(default)
  }

  inline def from[A: Identifiable, B: ClassTag](inline size: Int, inline it: Iterable[(A, B)], inline default: => B): ArrayMap[A, B] = {
    val m = fill(size, default)
    it.foreach { case (a, b) => m(a.id) = b }
    m
  }

  inline def from[A: Identifiable, B: ClassTag](inline it: Iterable[(A, B)], inline default: => B): ArrayMap[A, B] =
    if (it.isEmpty) Array.empty[B]
    else from(it.map(_._1.id).max + 1, it, default)

  inline def from[A: Identifiable, B: ClassTag](inline it: Iterable[(A, B)]): ArrayMap[A, B] = {
    if (it.isEmpty) throw new UnsupportedOperationException
    else from(it, it.head._2)
  }
}

opaque type ArraySet[A] = Array[Boolean]

object ArraySet {
  extension [A: Identifiable](s: ArraySet[A]) {
    inline def size: Int = s.count(identity)
    inline def contains(inline a: A): Boolean = s(a.id)
  }

  inline def empty[A: Identifiable](inline size: Int): ArraySet[A] = Array.fill(size)(false)

  inline def from[A: Identifiable](inline it: Iterable[A]): ArraySet[A] =
    if (it.isEmpty) empty(0)
    else from(it.map(_.id).max + 1, it)

  inline def from[A: Identifiable](inline size: Int, inline it: Iterable[A]): ArraySet[A] = {
    val s = empty(size)
    it.foreach { a => s(a.id) = true }
    s
  }
}

extension [A: Identifiable, B: ClassTag](m: Map[A, B]) {
  inline def toArrayMap(inline size: Int, inline default: => B): ArrayMap[A, B] = ArrayMap.from(size, m, default)
  inline def toArrayMap(inline default: => B): ArrayMap[A, B] = ArrayMap.from(m, default)
  inline def toArrayMap: ArrayMap[A, B] = ArrayMap.from(m)
}

extension [A: Identifiable](m: Iterable[A]) {
  inline def toArraySet: ArraySet[A] = ArraySet.from(m)
  inline def toArraySet(size: Int): ArraySet[A] = ArraySet.from(size, m)
}