package co.datadome.pub.scalaio2024.benchmarks.chapter5.gaston1

final case class Record(topic: Topic, persons: Set[Person])(implicit val problem: Problem) {

  private lazy val countPersons: Int = persons.size
  lazy val optionalPersons: Set[Person] = persons -- topic.mandatory
  lazy val canRemovePersons: Boolean = countPersons > topic.min && optionalPersons.nonEmpty
  lazy val canAddPersons: Boolean = topic.max > countPersons

  def addPerson(person: Person): Record = copy(persons = persons + person)

  def removePerson(person: Person): Record = copy(persons = persons - person)

  def replacePerson(oldP: Person, newP: Person): Record = copy(persons = persons - oldP + newP)

  lazy val scoresByPerson: Map[Person, Score] =
    persons.view.map { person =>
      val prefs = problem.preferencesByPerson(person)
      val score = Score.sumScorable(prefs)(_.scoreRecord(this))
      person -> score
    }.toMap

  override def toString: String = s"${topic.name} => ${persons.map(_.name).mkString(", ")}"
}

object Record {
  def apply(topic: Topic, persons: Person*)(implicit problem: Problem): Record = apply(topic, persons.toSet)
}
