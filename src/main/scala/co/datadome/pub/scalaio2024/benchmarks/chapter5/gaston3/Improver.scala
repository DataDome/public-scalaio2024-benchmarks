package co.datadome.pub.scalaio2024.benchmarks.chapter5.gaston3

import scala.annotation.tailrec
import scala.math.Ordering.Implicits.infixOrderingOps
import scala.util.Random

object Improver {

  private val defaultMaxRoundsCount = 1000

  @tailrec
  def improve(schedule: Schedule, maxRounds: Int = defaultMaxRoundsCount)(implicit rand: Random): Schedule =
    if (maxRounds == 0) schedule // Stopping assignment improvement because max number of rounds was reached.
    else goodMove(schedule) match {
      case None => schedule // Stopping assignment improvement because Schedule can't be improved anymore
      case Some(candidate) => improve(candidate, maxRounds - 1) // Schedule was improved, try again
    }

  private def goodMove(schedule: Schedule)(implicit rand: Random): Option[Schedule] = {
    lazy val records: Iterable[Record] = rand.shuffle(schedule.records)
    lazy val recordsWithRemovablePersons: Iterable[Record] = rand.shuffle(schedule.recordsThatCanRemovePersons)
    lazy val recordsWithAddablePersons: Iterable[Record] = rand.shuffle(schedule.recordsThatCanAddPersons)

    /* Schedules where we swapped two persons */
    lazy val swappedSchedules = for {
      r1 <- records.view
      r2 <- records.view
      t1 = r1.topic
      t2 = r2.topic
      if t1.id < t2.id // avoiding duplicates (cases where we just swap r1 and r2)
      p1 <- r1.optionalPersons.view
      p2 <- r2.optionalPersons.view
      improvedSchedule = schedule.swapPersons((t1, p1), (t2, p2))
      if improvedSchedule.totalScore > schedule.totalScore
    } yield improvedSchedule

    /* Schedules where we moved one person from one topic to another */
    lazy val movedSchedules = for {
      r1 <- recordsWithRemovablePersons.view
      r2 <- recordsWithAddablePersons.view if r1 != r2
      t1 = r1.topic
      t2 = r2.topic
      p <- r1.optionalPersons.view
      improvedSchedule = schedule.movePerson(t1, t2, p)
      if improvedSchedule.totalScore > schedule.totalScore
    } yield improvedSchedule

    swappedSchedules.headOption orElse movedSchedules.headOption
  }

}
