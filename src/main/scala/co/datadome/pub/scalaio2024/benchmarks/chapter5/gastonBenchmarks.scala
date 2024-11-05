package co.datadome.pub.scalaio2024.benchmarks.chapter5

import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit
import scala.reflect.ClassTag
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
abstract class GastonBenchmarkBase(val iterations: Int) {

  given rand: Random = new Random(0)

  @Benchmark def improve_gaston1(): Unit = (0 until iterations) foreach { _ =>
    val _ = gaston1.Improver.improve(gaston1.Example.startingSchedule)
  }

  /** Replaces foldLeft with fastFoldLeft */
  @Benchmark def improve_gaston2(): Unit = (0 until iterations) foreach { _ =>
    val _ = gaston2.Improver.improve(gaston2.Example.startingSchedule)
  }

  /** Use arrays as much as possible */
  @Benchmark def improve_gaston3(): Unit = (0 until iterations) foreach { _ =>
    val _ = gaston3.Improver.improve(gaston3.Example.startingSchedule)
  }

  /** Completely rework the structure */
  @Benchmark def improve_gaston4(): Unit = (0 until iterations) foreach { _ =>
    val _ = gaston4.Improver.improve(gaston4.Example.startingSchedule)
  }
}

class GastonBenchmarkXSmall extends GastonBenchmarkBase(5)

class GastonBenchmarkSmall extends GastonBenchmarkBase(100)

class GastonBenchmarkLarge extends GastonBenchmarkBase(10000)
