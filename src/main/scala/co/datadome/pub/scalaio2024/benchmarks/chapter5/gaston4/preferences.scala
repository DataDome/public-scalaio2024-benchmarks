package co.datadome.pub.scalaio2024.benchmarks.chapter5.gaston4

trait Preference {
  def reward: Score

  val person: Person
}

final case class PersonPersonAntiPreference(
  person: Person,
  target: Person,
  reward: Score // should be negative
) extends Preference {
  override def toString: String = s"${person.name} <!> ${target.name} (${reward.value})"
}

final case class PersonTopicPreference(
  person: Person,
  topic: Topic,
  reward: Score
) extends Preference {
  override def toString: String = s"${person.name} <3 ${topic.name} (${reward.value})"
}
