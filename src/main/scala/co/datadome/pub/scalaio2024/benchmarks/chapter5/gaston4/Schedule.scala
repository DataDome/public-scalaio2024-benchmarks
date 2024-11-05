package co.datadome.pub.scalaio2024.benchmarks.chapter5.gaston4

import co.datadome.pub.scalaio2024.utils.*

/** For each person's id, the topic they are on */
final case class Schedule(
  private val personIdToTopicId: Array[Topic.Id],
  private val topicIdToPersonIds: Array[Set[Person.Id]],
)(using val problem: Problem) {
  lazy val records: Iterable[Record] = ???

  lazy val topicIdsThatCanRemovePersons: Array[Int] =
    topicIdToPersonIds.zipWithIndex.collect { case (persons, topicId) if persons.size > problem.topicMinArray(topicId) => topicId }
  lazy val topicIdsThatCanAddPersons =
    topicIdToPersonIds.zipWithIndex.collect { case (persons, topicId) if persons.size < problem.topicMaxArray(topicId) => topicId }

  def deepCopy(): Schedule = copy(
    personIdToTopicId = Array.copyOf(personIdToTopicId, personIdToTopicId.length),
    topicIdToPersonIds = Array.copyOf(topicIdToPersonIds, topicIdToPersonIds.length) // TODO also need to copy the inside
  )

  def optionalPersons(topicId: Topic.Id) = topicIdToPersonIds(topicId) -- problem.topicMandatoryArray(topicId)

  def swapPersons(t1: Topic.Id, p1: Person.Id, t2: Topic.Id, p2: Person.Id): Unit = {
    personIdToTopicId(p1) = t2
    personIdToTopicId(p2) = t1
    topicIdToPersonIds(t1) = topicIdToPersonIds(t1) - p1 + p2
    topicIdToPersonIds(t2) = topicIdToPersonIds(t2) - p2 + p1
  }

  inline def unswapPersons(t1: Topic.Id, p1: Person.Id, t2: Topic.Id, p2: Person.Id): Unit =
    swapPersons(t2, p1, t1, p2)

  def movePerson(source: Topic.Id, destination: Topic.Id, pid: Person.Id): Unit = {
    personIdToTopicId(pid) = destination
    topicIdToPersonIds(source) = topicIdToPersonIds(source) - pid
    topicIdToPersonIds(destination) = topicIdToPersonIds(destination) + pid
  }

  inline def unmovePerson(source: Topic.Id, destination: Topic.Id, person: Person.Id): Unit =
    movePerson(destination, source, person)

  private inline def currentScore(inline personId: Person.Id, inline topicId: Topic.Id): Score = {
    val topicScore = problem.personTopicPreferencesMatrix(personId)(topicId)
    val otherPersons = topicIdToPersonIds(topicId)
    val personsScore = otherPersons.fastFoldLeft(Score.Zero) { (score, targetPersonId) => score + problem.personPersonPreferencesMatrix(personId)(targetPersonId) }
    topicScore + personsScore
  }

  private def currentScoreByPersonId: Array[Score] = {
    val result = new Array[Score](problem.personsCount)
    fastLoop(0, problem.personsCount) { personId =>
      val topicId = personIdToTopicId(personId)
      result(personId) = currentScore(personId, topicId)
    }
    result
  }

  /** Calculates the total score after a possible modification */
  def currentTotalScore: Score = {
    var total = Score.Zero
    val scores = currentScoreByPersonId.sortInPlace()(Score.ReverseOrdering)
    fastLoop(0, problem.personsCount) { personId =>
      total = total * Schedule.RankFactor + scores(personId)
    }
    total
  }

  /** Cached value */
  lazy val totalScore: Score = currentTotalScore

  override def toString: String = {
    val content = problem.topicsArray.zipWithIndex.map { (topic, topicId) =>
      val persons = topicIdToPersonIds(topicId).map(problem.personsArray)
      s"${topic.name} => ${persons.map(_.name).mkString(", ")}"
    }.sorted.mkString("\n")
    val scoresBPI = currentScoreByPersonId
    val scores = problem.personsSet.map { person =>
      val score = scoresBPI(person.id)
      s"${person.name}: $score"
    }.toSeq.sorted.mkString("\n")
    s"Schedule [$totalScore]\n$content\n$scores"
  }
}

object Schedule {

  /** Factor by which someone is worth less than the person immediately below them */
  private val RankFactor: Double = 0.5

  def from(entries: (Topic, Seq[Person])*)(using Problem): Schedule = {

    val personIdToTopicId: Array[Topic.Id] =
      entries.flatMap { case (topic, persons) => persons.map(p => p.id -> topic.id) }.sortBy(_._1).map(_._2).toArray

    val topicIdToPersonIds: Array[Set[Person.Id]] =
      entries.sortBy(_._1.id).map { case (_, persons) => persons.map(_.id).toSet }.toArray

    Schedule(personIdToTopicId, topicIdToPersonIds)
  }
}
