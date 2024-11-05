package co.datadome.pub.scalaio2024.benchmarks.chapter5.gaston3

import co.datadome.pub.scalaio2024.utils.*

final case class Problem(
  topicsSet: Set[Topic],
  personsSet: Set[Person],
  preferences: Set[Preference]
) {
  val personsCount = personsSet.size

  val preferencesByPerson: ArrayMap[Person, Array[Preference]] =
    preferences.toArray.groupBy(_.person).toArrayMap
}