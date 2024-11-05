package co.datadome.pub.scalaio2024.benchmarks.ffinal

final case class FinalStats(
  count: Int = 0,
  countSmall: Int = 0,
  countEmpty: Int = 0,
  countA: Int = 0,
  countB: Int = 0,
  countC: Int = 0
) {
  def updated(string: String) = {
    if (string.isEmpty) copy(count = count + 1, countSmall = count + 1, countEmpty = countEmpty + 1)
    else if (string.lengthCompare(FinalStats.smallStringSize) < 0) {
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

object FinalStats {
  val smallStringSize = 3
}

case class NonFinalStats(
  count: Int = 0,
  countSmall: Int = 0,
  countEmpty: Int = 0,
  countA: Int = 0,
  countB: Int = 0,
  countC: Int = 0
) {
  def updated(string: String) = {
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
      if (string.lengthCompare(FinalMutableStats.smallStringSize) < 0) {
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


case class NonFinalMutableStats(
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
      if (string.lengthCompare(NonFinalMutableStats.smallStringSize) < 0) {
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

object NonFinalMutableStats {
  val smallStringSize = 3
}
