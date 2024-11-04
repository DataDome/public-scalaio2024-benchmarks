package co.datadome.pub.scalaio2024.benchmarks.chapter3

import co.datadome.pub.scalaio2024.utils.*
import org.openjdk.jmh.annotations.*

import java.util as jutil
import java.util.concurrent.TimeUnit
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.*
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
abstract class AppendBenchmarkBase[A: ClassTag] {

  val rand: Random = new Random(0)

  lazy val sample: Seq[A]

  @Benchmark def append_list(): Unit = {
    var result: List[A] = Nil
    sample.foreach(i => result = i :: result)
    val _ = result.reverse
  }

  @Benchmark def prepend_list(): Unit = {
    var result: List[A] = Nil
    sample.foreach(i => result = i :: result)
  }

  @Benchmark def append_listBuffer(): Unit = {
    val result: ListBuffer[A] = ListBuffer[A]()
    sample.foreach(result.append)
    val _ = result.toList
  }

  @Benchmark def prepend_listBuffer(): Unit = {
    val result: ListBuffer[A] = ListBuffer[A]()
    sample.foreach(result.prepend)
    val _ = result.toList
  }

  @Benchmark def append_javaArrayList(): Unit = {
    val result: jutil.ArrayList[A] = new jutil.ArrayList[A]()
    sample.foreach(result.add)
    val _ = result.asScala.toList
  }

  @Benchmark def prepend_javaArrayList(): Unit = {
    val result: jutil.ArrayList[A] = new jutil.ArrayList[A]()
    sample.foreach(result.add)
    val _ = result.asScala.reverse.toList
  }
}

abstract class AppendBenchmarkBase_Int(sampleSize: Int) extends AppendBenchmarkBase[Int] {
  override lazy val sample: Seq[Int] = (0 until sampleSize).map(_ => rand.nextInt()).toVector
}

class AppendBenchmark_Int_Small extends AppendBenchmarkBase_Int(100)

class AppendBenchmark_Int_Medium extends AppendBenchmarkBase_Int(10000)

class AppendBenchmark_Int_Large extends AppendBenchmarkBase_Int(1000000)

abstract class AppendBenchmarkBase_String(sampleSize: Int) extends AppendBenchmarkBase[String] {
  private val stringSize = 50
  override lazy val sample: Seq[String] = (0 until sampleSize).map(_ => rand.nextSimpleString(stringSize)).toVector
}

class AppendBenchmark_String_Small extends AppendBenchmarkBase_String(100)

class AppendBenchmark_String_Medium extends AppendBenchmarkBase_String(10000)

class AppendBenchmark_String_Large extends AppendBenchmarkBase_String(1000000)
