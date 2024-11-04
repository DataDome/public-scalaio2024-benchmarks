package co.datadome.pub.scalaio2024.benchmarks.chapter3

import co.datadome.pub.scalaio2024.utils.*
import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit
import scala.annotation.tailrec
import scala.collection.immutable.{BitSet, SortedSet}
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
abstract class SetBenchmarkBase[A: ClassTag : Ordering : Identifiable] {

  val maxIntValue = 2000000 // must be divisible by the various set sizes
  val sampleSize = 1000

  given rand: Random = new Random(0)

  lazy val sample: Vector[A]
  lazy val testVector: Vector[A]

  lazy val testSet: Set[A] = testVector.toSet
  lazy val testSortedSet: SortedSet[A] = SortedSet.from(testVector)
  lazy val testArray: Array[A] = testVector.toArray
  lazy val testJavaHashSet: java.util.HashSet[A] = new java.util.HashSet(testVector.asJava)
  lazy val testBitSet: BitSet = BitSet.fromSpecific(testVector.map(_.id))
  lazy val testArraySet: ArraySet[A] = ArraySet.from(maxIntValue, testVector)

  @Benchmark def contains_Set(): Unit = {
    val _ = sample.foreach(testSet.contains)
  }

  @Benchmark def contains_SortedSet(): Unit = {
    val _ = sample.foreach(testSortedSet.contains)
  }

  @Benchmark def contains_Array(): Unit = {
    val _ = sample.foreach(testArray.contains)
  }

  @Benchmark def contains_JavaHashSet(): Unit = {
    val _ = sample.foreach(testJavaHashSet.contains)
  }

  @Benchmark def contains_ArraySet(): Unit = {
    val _ = sample.foreach(testArraySet.contains)
  }

  @Benchmark def contains_BitSet(): Unit = {
    val _ = sample.foreach(a => testBitSet.contains(a.id))
  }
}


abstract class SetBenchmarkBase_Int(val testSize: Int, val hitRatio: Double) extends SetBenchmarkBase[Int] {

  private def generateNewEntry(): Int = rand.nextInt(maxIntValue)

  private def generateKnownEntry(): Int = testVector(rand.nextInt(testSize))

  @tailrec
  private def generateUnknownEntry(): Int = {
    val a = rand.nextInt(maxIntValue)
    if (testVector.contains(a)) generateUnknownEntry()
    else a
  }

  lazy val testVector: Vector[Int] = (0 until testSize).map(_ => generateNewEntry()).toVector
  lazy val sample: Vector[Int] = (0 until sampleSize).map { i => if (rand.nextDouble() < hitRatio) generateKnownEntry() else generateUnknownEntry() }.toVector
}

class SetBenchmark_Int_AllHit_VerySmall extends SetBenchmarkBase_Int(5, 1.0)

class SetBenchmark_Int_AllHit_Small extends SetBenchmarkBase_Int(100, 1.0)

class SetBenchmark_Int_AllHit__Medium extends SetBenchmarkBase_Int(10000, 1.0)

class SetBenchmark_Int_AllHit_Large extends SetBenchmarkBase_Int(1000000, 1.0) {
  override def contains_Array(): Unit = () // very slow
}

class SetBenchmark_Int_AllMiss_VerySmall extends SetBenchmarkBase_Int(5, 0)

class SetBenchmark_Int_AllMiss_Small extends SetBenchmarkBase_Int(100, 0)

class SetBenchmark_Int_AllMiss_Medium extends SetBenchmarkBase_Int(10000, 0)

class SetBenchmark_Int_AllMiss_Large extends SetBenchmarkBase_Int(1000000, 0) {
  override def contains_Array(): Unit = () // very slow
}

class SetBenchmark_Int_HalfHit_VerySmall extends SetBenchmarkBase_Int(5, 0.5)

class SetBenchmark_Int_HalfHit_Small extends SetBenchmarkBase_Int(100, 0.5)

class SetBenchmark_Int_HalfHit_Medium extends SetBenchmarkBase_Int(10000, 0.5)

class SetBenchmark_Int_HalfHit_Large extends SetBenchmarkBase_Int(1000000, 0.5) {
  override def contains_Array(): Unit = () // very slow
}


abstract class SetBenchmarkBase_Invoice(val testSize: Int, val hitRatio: Double) extends SetBenchmarkBase[Invoice] {

  private def generateNewEntry(): Invoice = Invoice.random(rand.nextInt(maxIntValue))

  private def generateKnownEntry(): Invoice = testVector(rand.nextInt(testSize))

  @tailrec
  private def generateUnknownEntry(): Invoice = {
    val a = rand.nextInt(maxIntValue)
    if (testVector.exists(_.id == a)) generateUnknownEntry()
    else Invoice.random(a)
  }

  lazy val testVector: Vector[Invoice] = (0 until testSize).map(_ => generateNewEntry()).toVector
  lazy val sample: Vector[Invoice] = (0 until sampleSize).map { i => if (rand.nextDouble() < hitRatio) generateKnownEntry() else generateUnknownEntry() }.toVector
}

class SetBenchmark_Invoice_AllHit_VerySmall extends SetBenchmarkBase_Invoice(5, 1.0)

class SetBenchmark_Invoice_AllHit_Small extends SetBenchmarkBase_Invoice(100, 1.0)

class SetBenchmark_Invoice_AllHit_Medium extends SetBenchmarkBase_Invoice(10000, 1.0)

class SetBenchmark_Invoice_AllHit_Large extends SetBenchmarkBase_Invoice(1000000, 1.0) {
  override def contains_Array(): Unit = () // very slow
}

class SetBenchmark_Invoice_AllMiss_VerySmall extends SetBenchmarkBase_Invoice(5, 0)

class SetBenchmark_Invoice_AllMiss_Small extends SetBenchmarkBase_Invoice(100, 0)

class SetBenchmark_Invoice_AllMiss_Medium extends SetBenchmarkBase_Invoice(10000, 0)

class SetBenchmark_Invoice_AllMiss_Large extends SetBenchmarkBase_Invoice(1000000, 0) {
  override def contains_Array(): Unit = () // very slow
}

class SetBenchmark_Invoice_HalfHit_VerySmall extends SetBenchmarkBase_Invoice(5, 0.5)

class SetBenchmark_Invoice_HalfHit_Small extends SetBenchmarkBase_Invoice(100, 0.5)

class SetBenchmark_Invoice_HalfHit_Medium extends SetBenchmarkBase_Invoice(10000, 0.5)

class SetBenchmark_Invoice_HalfHit_Large extends SetBenchmarkBase_Invoice(1000000, 0.5) {
  override def contains_Array(): Unit = () // very slow
}


abstract class SetBenchmarkBase_BadInvoice(val testSize: Int, val hitRatio: Double) extends SetBenchmarkBase[BadInvoice] {

  private def generateNewEntry(): BadInvoice = BadInvoice.random(rand.nextInt(maxIntValue))

  private def generateKnownEntry(): BadInvoice = testVector(rand.nextInt(testSize))

  @tailrec
  private def generateUnknownEntry(): BadInvoice = {
    val a = rand.nextInt(maxIntValue)
    if (testVector.exists(_.id == a)) generateUnknownEntry()
    else BadInvoice.random(a)
  }

  lazy val testVector: Vector[BadInvoice] = (0 until testSize).map(_ => generateNewEntry()).toVector
  lazy val sample: Vector[BadInvoice] = (0 until sampleSize).map { i => if (rand.nextDouble() < hitRatio) generateKnownEntry() else generateUnknownEntry() }.toVector
}

class SetBenchmark_BadInvoice_AllHit_VerySmall extends SetBenchmarkBase_BadInvoice(5, 1.0)

class SetBenchmark_BadInvoice_AllHit_Small extends SetBenchmarkBase_BadInvoice(100, 1.0)

class SetBenchmark_BadInvoice_AllHit_Medium extends SetBenchmarkBase_BadInvoice(10000, 1.0)

class SetBenchmark_BadInvoice_AllHit_Large extends SetBenchmarkBase_BadInvoice(1000000, 1.0) {
  override def contains_Array(): Unit = () // very slow
}

class SetBenchmark_BadInvoice_AllMiss_VerySmall extends SetBenchmarkBase_BadInvoice(5, 0)

class SetBenchmark_BadInvoice_AllMiss_Small extends SetBenchmarkBase_BadInvoice(100, 0)

class SetBenchmark_BadInvoice_AllMiss_Medium extends SetBenchmarkBase_BadInvoice(10000, 0)

class SetBenchmark_BadInvoice_AllMiss_Large extends SetBenchmarkBase_BadInvoice(1000000, 0) {
  override def contains_Array(): Unit = () // very slow
}

class SetBenchmark_BadInvoice_HalfMiss_VerySmall extends SetBenchmarkBase_BadInvoice(5, 0.5)

class SetBenchmark_BadInvoice_HalfMiss_Small extends SetBenchmarkBase_BadInvoice(100, 0.5)

class SetBenchmark_BadInvoice_HalfMiss_Medium extends SetBenchmarkBase_BadInvoice(10000, 0.5)

class SetBenchmark_BadInvoice_HalfMiss_Large extends SetBenchmarkBase_BadInvoice(1000000, 0.5) {
  override def contains_Array(): Unit = () // very slow
}