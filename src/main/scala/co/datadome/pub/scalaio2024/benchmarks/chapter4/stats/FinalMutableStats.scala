package co.datadome.pub.scalaio2024.benchmarks.chapter4.stats


final case class FinalMutableStats(
  var count: Int = 0,
  var countSmall: Int = 0,
  var countEmpty: Int = 0,
  var countA: Int = 0,
  var countB: Int = 0,
  var countC: Int = 0
) {
  def update(string: String): Unit = {
    count += 1
    if (string.isEmpty) {
      countEmpty += 1
      countSmall += 1
    } else {
      if (string.lengthCompare(MutableStats.smallStringSize) < 0) {
        countSmall += 1
      }
      if (string.head == 'a' || string.head == 'A') {
        countA += 1
      }
      else if (string.head == 'b' || string.head == 'B') {
        countB += 1
      }
      else if (string.head == 'c' || string.head == 'C') {
        countC += 1
      }
    }
  }
}

object FinalMutableStats {
  val smallStringSize = 3
}
