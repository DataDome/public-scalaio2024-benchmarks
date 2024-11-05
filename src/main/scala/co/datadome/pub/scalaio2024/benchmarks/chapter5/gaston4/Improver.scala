package co.datadome.pub.scalaio2024.benchmarks.chapter5.gaston4

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
    val _ = schedule.totalScore // make sure it's cached
    lazy val topicIds = rand.shuffle(schedule.problem.topicIds)
    lazy val topicIdsThatCanRemovePersons = rand.shuffle(schedule.topicIdsThatCanRemovePersons)
    lazy val topicIdsThatCanAddPersons = rand.shuffle(schedule.topicIdsThatCanAddPersons)

    /* Schedules where we swapped two persons */
    lazy val swappedSchedules = for {
      t1 <- topicIds.view
      t2 <- topicIds.view
      if t1 < t2 // avoiding duplicates (cases where we just swap r1 and r2)
      p1 <- schedule.optionalPersons(t1).view
      p2 <- schedule.optionalPersons(t2).view
      _ = schedule.swapPersons(t1, p1, t2, p2)
      improvedSchedule =
        if (schedule.currentTotalScore > schedule.totalScore) schedule.deepCopy() else {
          schedule.unswapPersons(t1, p1, t2, p2)
          schedule
        }
      if improvedSchedule.totalScore > schedule.totalScore
    } yield improvedSchedule

    /* Schedules where we moved one person from one topic to another */
    lazy val movedSchedules = for {
      t1 <- topicIdsThatCanRemovePersons.view
      t2 <- topicIdsThatCanAddPersons.view if t1 != t2
      p <- schedule.optionalPersons(t1).view
      _ = schedule.movePerson(t1, t2, p)
      improvedSchedule =
        if (schedule.currentTotalScore > schedule.totalScore) schedule.deepCopy() else {
          schedule.unmovePerson(t1, t2, p)
          schedule
        }
      if improvedSchedule.totalScore > schedule.totalScore
    } yield improvedSchedule

    swappedSchedules.headOption orElse movedSchedules.headOption
  }

}
