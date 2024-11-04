package co.datadome.pub.scalaio2024.utils


/** Array extension */
extension [A](inline as: Array[A]) {
  inline def fastFoldLeft[B](inline init: B)(inline f: (B, A) => B): B = {
    var res = init
    val asl = as.length
    var ix = 0
    while (ix < asl) {
      res = f(res, as(ix))
      ix += 1
    }
    res
  }

  inline def fastCount(inline f: A => Boolean): Int = {
    var res = 0
    val asl = as.length
    var ix = 0
    while (ix < asl) {
      if (f(as(ix))) {
        res += 1
      }
      ix += 1
    }
    res
  }

  inline def fastForeach(inline f: A => Unit): Unit = {
    val asl = as.length
    var ix = 0
    while (ix < asl) {
      f(as(ix))
      ix += 1
    }
  }
}


/** Scala seq extension */
extension [A](inline as: Seq[A]) {
  inline def fastFoldLeftWithIndex[B](inline init: B)(inline f: (B, A) => B): B = {
    var res = init
    val asl = as.length
    var ix = 0
    while (ix < asl) {
      res = f(res, as(ix))
      ix += 1
    }
    res
  }

  inline def fastCountWithIndex(inline f: A => Boolean): Int = {
    var res = 0
    val asl = as.length
    var ix = 0
    while (ix < asl) {
      if (f(as(ix))) {
        res += 1
      }
      ix += 1
    }
    res
  }
}


/** Scala iterable extension */
extension [A](inline as: Iterable[A]) {

  inline def fastFoldLeft[B](inline init: B)(inline f: (B, A) => B): B = {
    var res = init
    val it = as.iterator
    while (it.hasNext) {
      res = f(res, it.next())
    }
    res
  }

  inline def fastCount(inline f: A => Boolean): Int = {
    var res = 0
    val it = as.iterator
    while (it.hasNext) {
      if (f(it.next())) {
        res += 1
      }
    }
    res
  }

  inline def fastForeach(inline f: A => Unit): Unit = {
    val it = as.iterator
    while (it.hasNext) {
      f(it.next())
    }
  }
}


