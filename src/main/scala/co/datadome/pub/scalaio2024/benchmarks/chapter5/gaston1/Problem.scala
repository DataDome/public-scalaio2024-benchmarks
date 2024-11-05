package co.datadome.pub.scalaio2024.benchmarks.chapter5.gaston1

final case class Problem(
  topicsSet: Set[Topic],
  personsSet: Set[Person],
  preferences: Set[Preference]
) {
  val preferencesByPerson: Map[Person, Set[Preference]] =
    preferences.groupBy(_.person)
}