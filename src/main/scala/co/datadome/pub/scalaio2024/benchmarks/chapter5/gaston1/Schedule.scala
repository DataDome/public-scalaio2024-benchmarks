package co.datadome.pub.scalaio2024.benchmarks.chapter5.gaston1

final case class Schedule(private val wrapped: Map[Topic, Record])(using problem: Problem) {

  private inline def updateWrapped(inline w: Map[Topic, Record]): Schedule =
    copy(wrapped = w)

  lazy val records: Iterable[Record] = wrapped.values
  lazy val recordsThatCanRemovePersons: Iterable[Record] = records.filter(_.canRemovePersons)
  lazy val recordsThatCanAddPersons: Iterable[Record] = records.filter(_.canAddPersons)

  def swapPersons(tp1: (Topic, Person), tp2: (Topic, Person)): Schedule = updateWrapped {
    val (t1, p1) = tp1
    val (t2, p2) = tp2
    val newR1 = wrapped(t1).replacePerson(p1, p2)
    val newR2 = wrapped(t2).replacePerson(p2, p1)
    wrapped.updated(t1, newR1).updated(t2, newR2)
  }

  def movePerson(source: Topic, destination: Topic, person: Person): Schedule = updateWrapped {
    val newSourceRecord = wrapped(source).removePerson(person)
    val newDestinationRecord = wrapped(destination).addPerson(person)
    wrapped.updated(source, newSourceRecord).updated(destination, newDestinationRecord)
  }

  private lazy val scoresByPerson: Map[Person, Score] = records.foldLeft(Map.empty)(_ ++ _.scoresByPerson)

  lazy val totalScore: Score =
    scoresByPerson.values.toSeq.sorted(Score.ReverseOrdering).foldLeft(Score.Zero) { case (acc, s) => (acc * Schedule.RankFactor) + s }

  override def toString: String = {
    val content = wrapped.values.map(_.toString).toSeq.sorted.mkString("\n")
    val scores = scoresByPerson.map { case (p, s) => s"${p.name}: $s" }.toSeq.sorted.mkString("\n")
    s"Schedule [$totalScore]\n$content\n$scores"
  }
}

object Schedule {

  /** Factor by which someone is worth less than the person immediately below them */
  private val RankFactor: Double = 0.5

  def from(entries: Record*)(using Problem): Schedule = {
    new Schedule(entries.map(e => e.topic -> e).toMap)
  }
}
