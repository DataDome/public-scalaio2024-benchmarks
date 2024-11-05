package co.datadome.pub.scalaio2024.benchmarks.chapter4.stats


case class NonFinalStats(
  count: Int = 0,
  countSmall: Int = 0,
  countEmpty: Int = 0,
  countA: Int = 0,
  countB: Int = 0,
  countC: Int = 0
) {
  def updated(string: String): NonFinalStats = {
    if (string.isEmpty) copy(count = count + 1, countSmall = count + 1, countEmpty = countEmpty + 1)
    else if (string.lengthCompare(NonFinalStats.smallStringSize) < 0) {
      if (string.head == 'a' || string.head == 'A') copy(count = count + 1, countSmall = count + 1, countA = countA + 1)
      else if (string.head == 'b' || string.head == 'B') copy(count = count + 1, countSmall = count + 1, countB = countB + 1)
      else if (string.head == 'c' || string.head == 'C') copy(count = count + 1, countSmall = count + 1, countC = countC + 1)
      else copy(count = count + 1, countSmall = count + 1)
    }
    else {
      if (string.head == 'a' || string.head == 'A') copy(count = count + 1, countA = countA + 1)
      else if (string.head == 'b' || string.head == 'B') copy(count = count + 1, countB = countB + 1)
      else if (string.head == 'c' || string.head == 'C') copy(count = count + 1, countC = countC + 1)
      else copy(count = count + 1)
    }
  }
}

object NonFinalStats {
  val smallStringSize = 3
}
