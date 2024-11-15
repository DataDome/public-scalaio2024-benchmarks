package co.datadome.pub.scalaio2024.benchmarks.chapter4

import co.datadome.pub.scalaio2024.benchmarks.chapter4.stats.*
import co.datadome.pub.scalaio2024.utils.*
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
abstract class StatsBenchmarkBase(val testSize: Int) {

  val rand: Random = new Random(0)

  val maxStringSize = 50

  val testStrings: Seq[String] = (0 until testSize).map(_ => rand.nextSimpleString(0, maxStringSize)).toVector

  /* foldLeft */

  @Benchmark def foldLeft_immutable_nonfinal(): Unit = {
    val result = testStrings.foldLeft(NonFinalStats()) { (stats, string) =>
      stats.updated(string)
    }
    assert(result.count == testSize)
  }

  @Benchmark def foldLeft_immutable_final(): Unit = {
    val result = testStrings.foldLeft(FinalStats()) { (stats, string) =>
      stats.updated(string)
    }
    assert(result.count == testSize)
  }

  @Benchmark def foldLeft_mutable_nonfinal(): Unit = {
    val result = testStrings.foldLeft(NonFinalMutableStats()) { (stats, string) =>
      stats.update(string)
      stats
    }
    assert(result.count == testSize)
  }

  @Benchmark def foldLeft_mutable_final(): Unit = {
    val result = testStrings.foldLeft(FinalMutableStats()) { (stats, string) =>
      stats.update(string)
      stats
    }
    assert(result.count == testSize)
  }

  @Benchmark def foldLeft_array(): Unit = {
    val result = testStrings.foldLeft(ArrayStats()) { (stats, string) =>
      stats.update(string)
      stats
    }
    assert(result.count == testSize)
  }


  /* fastFoldLeft */

  @Benchmark def fastFoldLeft_immutable_nonfinal(): Unit = {
    val result = testStrings.fastFoldLeft(NonFinalStats()) { (stats, string) =>
      stats.updated(string)
    }
    assert(result.count == testSize)
  }

  @Benchmark def fastFoldLeft_immutable_final(): Unit = {
    val result = testStrings.fastFoldLeft(FinalStats()) { (stats, string) =>
      stats.updated(string)
    }
    assert(result.count == testSize)
  }

  @Benchmark def fastFoldLeft_mutable_nonfinal(): Unit = {
    val result = testStrings.fastFoldLeft(NonFinalMutableStats()) { (stats, string) =>
      stats.update(string)
      stats
    }
    assert(result.count == testSize)
  }

  @Benchmark def fastFoldLeft_mutable_final(): Unit = {
    val result = testStrings.fastFoldLeft(FinalMutableStats()) { (stats, string) =>
      stats.update(string)
      stats
    }
    assert(result.count == testSize)
  }

  @Benchmark def fastFoldLeft_array(): Unit = {
    val result = testStrings.fastFoldLeft(ArrayStats()) { (stats, string) =>
      stats.update(string)
      stats
    }
    assert(result.count == testSize)
  }


  /* foreach */

  @Benchmark def foreach_immutable_nonfinal(): Unit = {
    var stats = NonFinalStats()
    testStrings.foreach { string =>
      stats = stats.updated(string)
    }
    assert(stats.count == testSize)
  }

  @Benchmark def foreach_immutable_final(): Unit = {
    var stats = FinalStats()
    testStrings.foreach { string =>
      stats = stats.updated(string)
    }
    assert(stats.count == testSize)
  }

  @Benchmark def foreach_mutable_nonfinal(): Unit = {
    val stats = NonFinalMutableStats()
    testStrings.foreach { string =>
      stats.update(string)
    }
    assert(stats.count == testSize)
  }

  @Benchmark def foreach_mutable_final(): Unit = {
    val stats = FinalMutableStats()
    testStrings.foreach { string =>
      stats.update(string)
    }
    assert(stats.count == testSize)
  }

  @Benchmark def foreach_array(): Unit = {
    val stats = ArrayStats()
    testStrings.foreach { string =>
      stats.update(string)
    }
    assert(stats.count == testSize)
  }


  /* fastForeach */

  @Benchmark def fastForeach_immutable_nonfinal(): Unit = {
    var stats = NonFinalStats()
    testStrings.fastForeach { string =>
      stats = stats.updated(string)
    }
    assert(stats.count == testSize)
  }

  @Benchmark def fastForeach_immutable_final(): Unit = {
    var stats = FinalStats()
    testStrings.fastForeach { string =>
      stats = stats.updated(string)
    }
    assert(stats.count == testSize)
  }

  @Benchmark def fastForeach_mutable_nonfinal(): Unit = {
    val stats = NonFinalMutableStats()
    testStrings.fastForeach { string =>
      stats.update(string)
    }
    assert(stats.count == testSize)
  }

  @Benchmark def fastForeach_mutable_final(): Unit = {
    val stats = FinalMutableStats()
    testStrings.fastForeach { string =>
      stats.update(string)
    }
    assert(stats.count == testSize)
  }

  @Benchmark def fastForeach_array(): Unit = {
    val stats = ArrayStats()
    testStrings.fastForeach { string =>
      stats.update(string)
    }
    assert(stats.count == testSize)
  }


  /* fastForeach - using the stats during the run */

  @Benchmark def fastForeach2_immutable_nonfinal(): Unit = {
    var stats = NonFinalStats()
    var total = 0L
    var totalSmall = 0L
    testStrings.fastForeach { string =>
      stats = stats.updated(string)
      total += stats.count
      totalSmall += stats.countSmall
    }
    assert(stats.count == testSize)
  }

  @Benchmark def fastForeach2_immutable_final(): Unit = {
    var stats = FinalStats()
    var total = 0L
    var totalSmall = 0L
    testStrings.fastForeach { string =>
      stats = stats.updated(string)
      total += stats.count
      totalSmall += stats.countSmall
    }
    assert(stats.count == testSize)
  }

  @Benchmark def fastForeach2_mutable_nonfinal(): Unit = {
    val stats = NonFinalMutableStats()
    var total = 0L
    var totalSmall = 0L
    testStrings.fastForeach { string =>
      stats.update(string)
      total += stats.count
      totalSmall += stats.countSmall
    }
    assert(stats.count == testSize)
  }

  @Benchmark def fastForeach2_mutable_final(): Unit = {
    val stats = FinalMutableStats()
    var total = 0L
    var totalSmall = 0L
    testStrings.fastForeach { string =>
      stats.update(string)
      total += stats.count
      totalSmall += stats.countSmall
    }
    assert(stats.count == testSize)
  }

}


class StatsBenchmark_Small extends StatsBenchmarkBase(100)

class StatsBenchmark_Medium extends StatsBenchmarkBase(10000)

class StatsBenchmark_Large extends StatsBenchmarkBase(1000000)
