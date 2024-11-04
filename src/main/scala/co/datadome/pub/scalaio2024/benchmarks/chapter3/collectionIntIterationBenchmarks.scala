package co.datadome.pub.scalaio2024.benchmarks.chapter3

import co.datadome.pub.scalaio2024.utils.*
import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit
import scala.annotation.tailrec
import scala.util.Random

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, jvmArgs = Array(
  "-server",
  "-Xms1g",
  "-Xmx1g",
  "-XX:NewSize=512m",
  "-XX:MaxNewSize=512m",
  "-XX:InitialCodeCacheSize=256m",
  "-XX:ReservedCodeCacheSize=256m",
  "-XX:+UseParallelGC",
  "-XX:MaxInlineLevel=18",
  "-XX:+AlwaysPreTouch"
))
abstract class CollectionIntIterationBenchmarkBase(val sampleSize: Int) {

  val rand: Random = new Random(0)

  val sampleVector: Vector[Int] = (0 until sampleSize).map(_ => rand.nextInt()).toVector
  val sampleList: List[Int] = sampleVector.toList
  val sampleArray: Array[Int] = sampleVector.toArray

  private def square(i: Int) = i * i

  @Benchmark def for_List(): Unit = {
    var res = 0
    for (i <- sampleList) {
      res = res + square(i)
    }
    val _ = res
  }

  @Benchmark def for_Vector(): Unit = {
    var res = 0
    for (i <- sampleVector) {
      res = res + square(i)
    }
    val _ = res
  }

  @Benchmark def for_Array(): Unit = {
    var res = 0
    for (i <- sampleArray) {
      res = res + square(i)
    }
    val _ = res
  }

  @Benchmark def while_List(): Unit = {
    var res = 0
    var curr = sampleList
    while (curr.nonEmpty) {
      res = res + square(curr.head)
      curr = curr.tail
    }
    val _ = res
  }

  @Benchmark def while_Vector(): Unit = {
    var res = 0
    var ix = 0
    while (ix < sampleVector.size) {
      res = res + square(sampleVector(ix))
      ix += 1
    }
    val _ = res
  }

  @Benchmark def while_Array(): Unit = {
    var res = 0
    var ix = 0
    while (ix < sampleArray.length) {
      res = res + square(sampleArray(ix))
      ix += 1
    }
    val _ = res
  }

  @Benchmark def reverseWhile_Vector(): Unit = {
    var res = 0
    var ix = sampleVector.size
    while (ix > 0) {
      ix -= 1
      res = res + square(sampleVector(ix))
    }
    val _ = res
  }

  @Benchmark def reverseWhile_Array(): Unit = {
    var res = 0
    var ix = sampleArray.length
    while (ix > 0) {
      ix -= 1
      res = res + square(sampleArray(ix))
    }
    val _ = res
  }

  @Benchmark def recursion_List(): Unit = {
    @tailrec
    def acc(is: List[Int], res: Int = 0): Int = is match {
      case Nil => res
      case h :: q => acc(q, res + square(h))
    }

    val _ = acc(sampleList)
  }

  @Benchmark def mapSum_List(): Unit = {
    val _ = sampleList.map(square).sum
  }

  @Benchmark def mapSum_Vector(): Unit = {
    val _ = sampleVector.map(square).sum
  }

  @Benchmark def mapSum_Array(): Unit = {
    val _ = sampleArray.map(square).sum
  }

  @Benchmark def viewMapSum_List(): Unit = {
    val _ = sampleList.view.map(square).sum
  }

  @Benchmark def viewMapSum_Vector(): Unit = {
    val _ = sampleVector.view.map(square).sum
  }

  @Benchmark def viewMapSum_Array(): Unit = {
    val _ = sampleArray.view.map(square).sum
  }

  @Benchmark def foldLeft_List(): Unit = {
    val _ = sampleList.foldLeft(0) { (acc, i) => acc + square(i) }
  }

  @Benchmark def foldLeft_Vector(): Unit = {
    val _ = sampleVector.foldLeft(0) { (acc, i) => acc + square(i) }
  }

  @Benchmark def foldLeft_Array(): Unit = {
    val _ = sampleArray.foldLeft(0) { (acc, i) => acc + square(i) }
  }

  @Benchmark def fastFoldLeft_List(): Unit = {
    val _ = sampleList.fastFoldLeft(0) { (acc, i) => acc + square(i) }
  }

  @Benchmark def fastFoldLeftWithIndex_Vector(): Unit = {
    val _ = sampleVector.fastFoldLeftWithIndex(0) { (acc, i) => acc + square(i) }
  }

  @Benchmark def fastFoldLeft_Vector(): Unit = {
    val _ = sampleVector.fastFoldLeft(0) { (acc, i) => acc + square(i) }
  }

  @Benchmark def fastFoldLeft_Array(): Unit = {
    val _ = sampleArray.fastFoldLeft(0) { (acc, i) => acc + square(i) }
  }

}

class CollectionIntIterationBenchmark_Small extends CollectionIntIterationBenchmarkBase(100)

class CollectionIntIterationBenchmark_Medium extends CollectionIntIterationBenchmarkBase(10000)

class CollectionIntIterationBenchmark_Large extends CollectionIntIterationBenchmarkBase(1000000)
