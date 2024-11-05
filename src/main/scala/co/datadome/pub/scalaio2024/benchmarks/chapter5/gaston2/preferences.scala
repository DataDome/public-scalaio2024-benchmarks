package co.datadome.pub.scalaio2024.benchmarks.chapter5.gaston2

trait Preference {

  def reward: Score

  def score(schedule: Schedule): Score =
    Score.sumScorable(schedule.records)(scoreRecord)

  def scoreRecord(record: Record): Score

  val person: Person
}

final case class PersonPersonAntiPreference(
  person: Person,
  target: Person,
  reward: Score // should be negative
) extends Preference {

  override def scoreRecord(record: Record): Score = {
    if (record.persons.contains(person) && record.persons.contains(target)) reward
    else Score.Zero
  }

  override def toString: String = s"${person.name} <!> ${target.name} (${reward.value})"
}

final case class PersonTopicPreference(
  person: Person,
  topic: Topic,
  reward: Score
) extends Preference {

  override def scoreRecord(record: Record): Score = {
    if (record.topic == topic && record.persons.contains(person)) reward
    else Score.Zero
  }

  override def toString: String = s"${person.name} <3 ${topic.name} (${reward.value})"
}
