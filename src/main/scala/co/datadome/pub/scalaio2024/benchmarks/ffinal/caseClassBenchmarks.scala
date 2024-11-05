package co.datadome.pub.scalaio2024.benchmarks.ffinal

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
abstract class CaseClassBenchmarkBase(val testSize: Int) {

  val rand: Random = new Random(0)

  val maxStringSize = 50
  val smallStringSize = 3

  val testStrings: Seq[String] = (0 until testSize).map(_ => rand.nextSimpleString(0, maxStringSize))

  @Benchmark def fastForeach_nothing(): Unit = {
    var stats = FinalStats()
    testStrings.fastForeach { _ =>
      stats = stats
    }
  }

  @Benchmark def fastForeach_immutable_final(): Unit = {
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

  @Benchmark def fastForeach_immutable_nonfinal(): Unit = {
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

  @Benchmark def fastForeach_mutable_final(): Unit = {
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

  @Benchmark def fastForeach_mutable_nonfinal(): Unit = {
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

}


class CaseClassBenchmarkSmall extends CaseClassBenchmarkBase(100)

class CaseClassBenchmarkMedium extends CaseClassBenchmarkBase(10000)

class CaseClassBenchmarkLarge extends CaseClassBenchmarkBase(1000000)
