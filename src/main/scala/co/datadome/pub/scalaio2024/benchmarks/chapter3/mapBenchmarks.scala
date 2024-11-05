package co.datadome.pub.scalaio2024.benchmarks.chapter3

import co.datadome.pub.scalaio2024.utils.*
import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit
import scala.collection.immutable.{HashMap, IntMap}
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
abstract class MapBenchmarkBase[A: ClassTag : Ordering : Identifiable] {

  val maxIntValue = 2000000 // must be divisible by the various map sizes
  val sampleSize = 1000

  given rand: Random = new Random(0)

  lazy val sample: Vector[A]
  lazy val testSeq: Seq[(A, String)]

  lazy val testKeys: Seq[A] = testSeq.map(_._1) // might have duplicates, same size as testSeq

  lazy val testHashMap: HashMap[A, String] = testSeq.toMap.asInstanceOf[HashMap[A, String]]
  lazy val testJavaHashMap: java.util.HashMap[A, String] = new java.util.HashMap(testHashMap.asJava)
  lazy val testArrayMap: ArrayMap[A, Option[String]] = ArrayMap.from(maxIntValue, testHashMap.view.mapValues(Some(_)), None)
  lazy val testIntMap: IntMap[String] = IntMap.from(testHashMap.map((k, v) => k.id -> v))

  @Benchmark def get_HashMap(): Unit = {
    val _ = sample.map(testHashMap.get)
  }

  @Benchmark def get_JavaHashMap(): Unit = {
    val _ = sample.map(testJavaHashMap.get)
  }

  @Benchmark def apply_ArrayMap(): Unit = {
    val _ = sample.map(testArrayMap.apply)
  }

  @Benchmark def get_IntMap(): Unit = {
    val _ = sample.map(a => testIntMap.get(a.id))
  }
}


abstract class MapBenchmarkBase_Int(val testSize: Int, val hitRatio: Double) extends MapBenchmarkBase[Int] {

  private lazy val nonTestKeys = ((0 until maxIntValue).toSet -- testKeys).toArray

  private def generateNewEntry(): (Int, String) = rand.nextInt(maxIntValue) -> rand.nextSimpleString(20)

  private def generateKnownKey(): Int = testKeys(rand.nextInt(testSize))

  private def generateUnknownKey(): Int = nonTestKeys(rand.nextInt(nonTestKeys.length))

  lazy val testSeq: Seq[(Int, String)] = (0 until testSize).map(_ => generateNewEntry())
  lazy val sample: Vector[Int] = (0 until sampleSize).map { i => if (rand.nextDouble() < hitRatio) generateKnownKey() else generateUnknownKey() }.toVector
}

abstract class MapBenchmarkBase_Invoice(val testSize: Int, val hitRatio: Double) extends MapBenchmarkBase[Invoice] {

  private lazy val nonTestKeyIds = ((0 until maxIntValue).toSet -- testKeys.map(_.id)).toArray

  private def generateNewEntry(): (Invoice, String) = Invoice.random(rand.nextInt(maxIntValue)) -> rand.nextSimpleString(20)

  private def generateKnownKey(): Invoice = testKeys(rand.nextInt(testSize))

  private def generateUnknownKey(): Invoice = Invoice.random(nonTestKeyIds(rand.nextInt(nonTestKeyIds.length)))

  lazy val testSeq: Seq[(Invoice, String)] = (0 until testSize).map(_ => generateNewEntry())
  lazy val sample: Vector[Invoice] = (0 until sampleSize).map { i => if (rand.nextDouble() < hitRatio) generateKnownKey() else generateUnknownKey() }.toVector
}

abstract class MapBenchmarkBase_BadInvoice(val testSize: Int, val hitRatio: Double) extends MapBenchmarkBase[BadInvoice] {

  private lazy val nonTestKeyIds = ((0 until maxIntValue).toSet -- testKeys.map(_.id)).toArray

  private def generateNewEntry(): (BadInvoice, String) = BadInvoice.random(rand.nextInt(maxIntValue)) -> rand.nextSimpleString(20)

  private def generateKnownKey(): BadInvoice = testKeys(rand.nextInt(testSize))

  private def generateUnknownKey(): BadInvoice = BadInvoice.random(nonTestKeyIds(rand.nextInt(nonTestKeyIds.length)))

  lazy val testSeq: Seq[(BadInvoice, String)] = (0 until testSize).map(_ => generateNewEntry())
  lazy val sample: Vector[BadInvoice] = (0 until sampleSize).map { i => if (rand.nextDouble() < hitRatio) generateKnownKey() else generateUnknownKey() }.toVector
}

class MapBenchmark_Int_AllHit_VerySmall extends MapBenchmarkBase_Int(5, 1.0)

class MapBenchmark_Int_AllHit_Small extends MapBenchmarkBase_Int(100, 1.0)

class MapBenchmark_Int_AllHit_Medium extends MapBenchmarkBase_Int(10000, 1.0)

class MapBenchmark_Int_AllHit_Large extends MapBenchmarkBase_Int(1000000, 1.0)

class MapBenchmark_Int_AllMiss_VerySmall extends MapBenchmarkBase_Int(5, 0)

class MapBenchmark_Int_AllMiss_Small extends MapBenchmarkBase_Int(100, 0)

class MapBenchmark_Int_AllMiss_Medium extends MapBenchmarkBase_Int(10000, 0)

class MapBenchmark_Int_AllMiss_Large extends MapBenchmarkBase_Int(1000000, 0)

class MapBenchmark_Int_HalfHit_VerySmall extends MapBenchmarkBase_Int(5, 0.5)

class MapBenchmark_Int_HalfHit_Small extends MapBenchmarkBase_Int(100, 0.5)

class MapBenchmark_Int_HalfHit_Medium extends MapBenchmarkBase_Int(10000, 0.5)

class MapBenchmark_Int_HalfHit_Large extends MapBenchmarkBase_Int(1000000, 0.5)


class MapBenchmark_Invoice_AllHit_VerySmall extends MapBenchmarkBase_Invoice(5, 1.0)

class MapBenchmark_Invoice_AllHit_Small extends MapBenchmarkBase_Invoice(100, 1.0)

class MapBenchmark_Invoice_AllHit_Medium extends MapBenchmarkBase_Invoice(10000, 1.0)

class MapBenchmark_Invoice_AllHit_Large extends MapBenchmarkBase_Invoice(1000000, 1.0)

class MapBenchmark_Invoice_AllMiss_VerySmall extends MapBenchmarkBase_Invoice(5, 0)

class MapBenchmark_Invoice_AllMiss_Small extends MapBenchmarkBase_Invoice(100, 0)

class MapBenchmark_Invoice_AllMiss_Medium extends MapBenchmarkBase_Invoice(10000, 0)

class MapBenchmark_Invoice_AllMiss_Large extends MapBenchmarkBase_Invoice(1000000, 0)

class MapBenchmark_Invoice_HalfHit_VerySmall extends MapBenchmarkBase_Invoice(5, 0.5)

class MapBenchmark_Invoice_HalfHit_Small extends MapBenchmarkBase_Invoice(100, 0.5)

class MapBenchmark_Invoice_HalfHit_Medium extends MapBenchmarkBase_Invoice(10000, 0.5)

class MapBenchmark_Invoice_HalfHit_Large extends MapBenchmarkBase_Invoice(1000000, 0.5)


class MapBenchmark_BadInvoice_AllHit_VerySmall extends MapBenchmarkBase_BadInvoice(5, 1.0)

class MapBenchmark_BadInvoice_AllHit_Small extends MapBenchmarkBase_BadInvoice(100, 1.0)

class MapBenchmark_BadInvoice_AllHit_Medium extends MapBenchmarkBase_BadInvoice(10000, 1.0)

class MapBenchmark_BadInvoice_AllHit_Large extends MapBenchmarkBase_BadInvoice(1000000, 1.0)

class MapBenchmark_BadInvoice_AllMiss_VerySmall extends MapBenchmarkBase_BadInvoice(5, 0)

class MapBenchmark_BadInvoice_AllMiss_Small extends MapBenchmarkBase_BadInvoice(100, 0)

class MapBenchmark_BadInvoice_AllMiss_Medium extends MapBenchmarkBase_BadInvoice(10000, 0)

class MapBenchmark_BadInvoice_AllMiss_Large extends MapBenchmarkBase_BadInvoice(1000000, 0)

class MapBenchmark_BadInvoice_HalfHit_VerySmall extends MapBenchmarkBase_BadInvoice(5, 0.5)

class MapBenchmark_BadInvoice_HalfHit_Small extends MapBenchmarkBase_BadInvoice(100, 0.5)

class MapBenchmark_BadInvoice_HalfHit_Medium extends MapBenchmarkBase_BadInvoice(10000, 0.5)

class MapBenchmark_BadInvoice_HalfHit_Large extends MapBenchmarkBase_BadInvoice(1000000, 0.5)
