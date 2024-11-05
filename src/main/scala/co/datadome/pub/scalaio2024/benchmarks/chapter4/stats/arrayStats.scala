package co.datadome.pub.scalaio2024.benchmarks.chapter4.stats


opaque type ArrayStats = Array[Int]

object ArrayStats {
  val smallStringSize = 3

  def apply(): ArrayStats = Array.fill(6)(0)

  inline val CountIndex = 0
  inline val CountSmallIndex = 1
  inline val CountEmptyIndex = 2
  inline val CountAIndex = 3
  inline val CountBIndex = 4
  inline val CountCIndex = 5
}

extension (s: ArrayStats) {
  inline def count: Int = s(ArrayStats.CountIndex)
  inline def countSmall: Int = s(ArrayStats.CountSmallIndex)
  inline def countEmpty: Int = s(ArrayStats.CountEmptyIndex)
  inline def countA: Int = s(ArrayStats.CountAIndex)
  inline def countB: Int = s(ArrayStats.CountBIndex)
  inline def countC: Int = s(ArrayStats.CountCIndex)


  inline def incrCount(): Unit = s(ArrayStats.CountIndex) += 1
  inline def incrCountSmall(): Unit = s(ArrayStats.CountSmallIndex) += 1
  inline def incrCountEmpty(): Unit = s(ArrayStats.CountEmptyIndex) += 1
  inline def incrCountA(): Unit = s(ArrayStats.CountAIndex) += 1
  inline def incrCountB(): Unit = s(ArrayStats.CountBIndex) += 1
  inline def incrCountC(): Unit = s(ArrayStats.CountCIndex) += 1

  inline def update(inline string: String): Unit = {
    s(ArrayStats.CountIndex) += 1
    if (string.isEmpty) {
      s(ArrayStats.CountEmptyIndex) += 1
      s(ArrayStats.CountSmallIndex) += 1
    } else {
      if (string.lengthCompare(ArrayStats.smallStringSize) < 0) {
        s(ArrayStats.CountSmallIndex) += 1
      }
      if (string.head == 'a' || string.head == 'A') {
        s(ArrayStats.CountAIndex) += 1
      }
      else if (string.head == 'b' || string.head == 'B') {
        s(ArrayStats.CountBIndex) += +1
      }
      else if (string.head == 'c' || string.head == 'C') {
        s(ArrayStats.CountCIndex) += +1
      }
    }
  }
}
