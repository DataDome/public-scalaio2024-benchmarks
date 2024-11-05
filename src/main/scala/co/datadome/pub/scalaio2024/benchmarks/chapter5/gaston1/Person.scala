package co.datadome.pub.scalaio2024.benchmarks.chapter5.gaston1

import co.datadome.pub.scalaio2024.utils.Identified

final case class Person(
  id: Person.Id,
  name: String
) extends Identified

object Person {
  type Id = Int
}
