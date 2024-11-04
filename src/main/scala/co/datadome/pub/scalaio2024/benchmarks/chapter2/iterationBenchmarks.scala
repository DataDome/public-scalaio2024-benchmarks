package co.datadome.pub.scalaio2024.benchmarks.chapter2

import co.datadome.pub.scalaio2024.utils.fastLoop
import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.NANOSECONDS)
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
abstract class IterationBenchmarkBase(val sampleSize: Int) {

  /* *********************** */
  /* Minimum loop: do noting */

  @Benchmark def direct0_for(): Int = {
    var res = 0
    for (_ <- 0 until sampleSize) {
      res = res
    }
    res
  }

  @Benchmark def direct0_while(): Int = {
    var i = 0
    var res = 0
    while (i < sampleSize) {
      res = res
      i += 1
    }
    res
  }

  @Benchmark def direct0_fastLoop(): Int = {
    var res = 0
    fastLoop(0, sampleSize) { _ =>
      res = res
    }
    res
  }

  def compute0(res: Int, i: Int) = res

  @Benchmark def compute0_for(): Int = {
    var res = 0
    for (i <- 0 until sampleSize) {
      res = compute0(res, i)
    }
    res
  }

  @Benchmark def compute0_while(): Int = {
    var i = 0
    var res = 0
    while (i < sampleSize) {
      res = compute0(res, i)
      i += 1
    }
    res
  }

  @Benchmark def compute0_fastLoop(): Int = {
    var res = 0
    fastLoop(0, sampleSize) { i =>
      res = compute0(res, i)
    }
    res
  }



  /* ************************* */
  /* Very quick loop: addition */

  @Benchmark def direct1_for(): Int = {
    var res = 0
    for (i <- 0 until sampleSize) {
      res = res + i
    }
    res
  }

  @Benchmark def direct1_while(): Int = {
    var i = 0
    var res = 0
    while (i < sampleSize) {
      res = res + i
      i += 1
    }
    res
  }

  @Benchmark def direct1_fastLoop(): Int = {
    var res = 0
    fastLoop(0, sampleSize) { i =>
      res = res + i
    }
    res
  }

  def compute1(res: Int, i: Int) = {
    res + i
  }

  @Benchmark def compute1_for(): Int = {
    var res = 0
    for (i <- 0 until sampleSize) {
      res = compute1(res, i)
    }
    res
  }

  @Benchmark def compute1_while(): Int = {
    var i = 0
    var res = 0
    while (i < sampleSize) {
      res = compute1(res, i)
      i += 1
    }
    res
  }

  @Benchmark def compute1_fastLoop(): Int = {
    var res = 0
    fastLoop(0, sampleSize) { i =>
      res = compute1(res, i)
    }
    res
  }



  /* *************************************** */
  /* Quick loop: addition and multiplication */

  @Benchmark def direct2_for(): Int = {
    var res = 0
    for (i <- 0 until sampleSize) {
      res = res + i + i * i
    }
    res
  }

  @Benchmark def direct2_while(): Int = {
    var i = 0
    var res = 0
    while (i < sampleSize) {
      res = res + i + i * i
      i += 1
    }
    res
  }

  @Benchmark def direct2_fastLoop(): Int = {
    var res = 0
    fastLoop(0, sampleSize) { i =>
      res = res + i + i * i
    }
    res
  }

  def compute2(res: Int, i: Int) = {
    res + i + i * i
  }

  @Benchmark def compute2_for(): Int = {
    var res = 0
    for (i <- 0 until sampleSize) {
      res = compute2(res, i)
    }
    res
  }

  @Benchmark def compute2_while(): Int = {
    var i = 0
    var res = 0
    while (i < sampleSize) {
      res = compute2(res, i)
      i += 1
    }
    res
  }

  @Benchmark def compute2_fastLoop(): Int = {
    var res = 0
    fastLoop(0, sampleSize) { i =>
      res = compute2(res, i)
    }
    res
  }



  /* *************************************** */
  /* Slow loop: cosinus */

  @Benchmark def direct3_for(): Double = {
    var res = 0.0
    for (i <- 0 until sampleSize) {
      res = res + Math.cos(i)
    }
    res
  }

  @Benchmark def direct3_while(): Double = {
    var i = 0
    var res = 0.0
    while (i < sampleSize) {
      res = res + Math.cos(i)
      i += 1
    }
    res
  }

  @Benchmark def direct3_fastLoop(): Double = {
    var res = 0.0
    fastLoop(0, sampleSize) { i =>
      res = res + Math.cos(i)
    }
    res
  }

  def compute3(res: Double, i: Int) = {
    res + Math.cos(i)
  }

  @Benchmark def compute3_for(): Double = {
    var res = 0.0
    for (i <- 0 until sampleSize) {
      res = compute3(res, i)
    }
    res
  }

  @Benchmark def compute3_while(): Double = {
    var i = 0
    var res = 0.0
    while (i < sampleSize) {
      res = compute3(res, i)
      i += 1
    }
    res
  }

  @Benchmark def compute3_fastLoop(): Double = {
    var res = 0.0
    fastLoop(0, sampleSize) { i =>
      res = compute3(res, i)
    }
    res
  }

}

class IterationBenchmark_Small extends IterationBenchmarkBase(100)

class IterationBenchmark_Medium extends IterationBenchmarkBase(10000)

class IterationBenchmark_Large extends IterationBenchmarkBase(1000000)

