package co.datadome.pub.scalaio2024.benchmarks.chapter5.gaston3

import co.datadome.pub.scalaio2024.utils.*

opaque type Score = Double

object Score {
  def apply(d: Double): Score = d

  extension (score: Score) {
    def value: Double = score
    def +(t: Score): Score = score + t
    def -(t: Score): Score = score - t
    def *(d: Double): Score = score * d
    def /(d: Double): Score = score / d
  }

  given StraightOrdering: Ordering[Score] with {
    override def compare(x: Score, y: Score): Int = if (x > y) 1 else if (x < y) -1 else 0
  }

  val ReverseOrdering: Ordering[Score] = StraightOrdering.reverse

  val Zero: Score = Score(0)

  val MinValue: Score = Score(Double.MinValue)

  inline def sumScorable[A](inline it: Iterable[A])(inline f: A => Score): Score =
    if (it.isEmpty) Score.Zero
    else it.fastFoldLeft(Score.Zero)(_ + f(_))

  inline def sumScorable[A](inline it: Array[A])(inline f: A => Score): Score =
    if (it.isEmpty) Score.Zero
    else it.fastFoldLeft(Score.Zero)(_ + f(_))
}
