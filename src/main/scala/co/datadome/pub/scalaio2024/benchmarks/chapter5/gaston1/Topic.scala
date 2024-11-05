package co.datadome.pub.scalaio2024.benchmarks.chapter5.gaston1

import co.datadome.pub.scalaio2024.utils.*

final case class Topic(
  id: Topic.Id,
  name: String,
  mandatory: Set[Person],
  min: Int,
  max: Int
) extends Identified

object Topic {
  type Id = Int
}
