package co.datadome.pub.scalaio2024.benchmarks.chapter5

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should

import scala.util.Random

class GastonBenchmarkTest extends AnyFunSuite with should.Matchers {

  test("all problems are the same") {
    val ref = gaston1.Example.problem.preferences.map(_.toString).toSeq.sorted
    gaston2.Example.problem.preferences.map(_.toString).toSeq.sorted should be(ref)
    gaston3.Example.problem.preferences.map(_.toString).toSeq.sorted should be(ref)
    gaston4.Example.problem.preferences.map(_.toString).toSeq.sorted should be(ref)
  }

  test("gaston1 > initial score") {
    import gaston1.*
    Example.startingSchedule.totalScore.value should be(-149.98958110809326)
  }

  test("gaston2 > initial score") {
    import gaston2.*
    Example.startingSchedule.totalScore.value should be(-149.98958110809326)
  }

  test("gaston3 > initial score") {
    import gaston3.*
    Example.startingSchedule.totalScore.value should be(-149.98958110809326)
  }

  test("gaston4 > initial score") {
    import gaston4.*
    Example.startingSchedule.totalScore.value should be(-149.98958110809326)
  }

  test("gaston1 > final score") {
    import gaston1.*
    given Random = new Random(0)

    val solution = Improver.improve(Example.startingSchedule)
    solution.totalScore should be(1.8749666213989258)
  }

  test("gaston2 > final score") {
    import gaston2.*
    given Random = new Random(0)

    val solution = Improver.improve(Example.startingSchedule)
    solution.totalScore should be(1.8749666213989258)
  }

  test("gaston3 > final score") {
    import gaston3.*
    given Random = new Random(0)

    val solution = Improver.improve(Example.startingSchedule)
    solution.totalScore should be(1.8749666213989258)
  }

  test("gaston4 > final score") {
    import gaston4.*
    given Random = new Random(0)

    val solution = Improver.improve(Example.startingSchedule)
    solution.totalScore should be(1.8749666213989258)
  }
}
