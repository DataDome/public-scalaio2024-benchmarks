package co.datadome.pub.scalaio2024.benchmarks.chapter5.gaston4

final case class Problem(
  topicsSet: Set[Topic],
  personsSet: Set[Person],
  preferences: Set[Preference]
) {
  val personsCount: Int = personsSet.size
  val topicsCount: Int = topicsSet.size

  val personsArray: Array[Person] = personsSet.toArray.sortBy(_.id)
  val topicsArray: Array[Topic] = topicsSet.toArray.sortBy(_.id)
  val topicIds = (0 until topicsCount).toArray

  val topicMandatoryArray: Array[Array[Person.Id]] = topicsArray.map(_.mandatory.map(_.id).toArray)
  val topicMinArray: Array[Int] = topicsArray.map(_.min)
  val topicMaxArray: Array[Int] = topicsArray.map(_.max)

  val personTopicPreferences = preferences.collect { case ptp: PersonTopicPreference => ptp }
  val personTopicPreferencesMatrix: Array[Array[Score]] =
    (0 until personsCount).map { personId =>
      (0 until topicsCount).map { topicId =>
        personTopicPreferences.find(p => p.person.id == personId && p.topic.id == topicId).fold(Score.Zero)(_.reward)
      }.toArray
    }.toArray

  val personPersonPreferences = preferences.collect { case ppp: PersonPersonAntiPreference => ppp }
  val personPersonPreferencesMatrix: Array[Array[Score]] =
    (0 until personsCount).map { personId =>
      (0 until personsCount).map { targetId =>
        personPersonPreferences.find(p => p.person.id == personId && p.target.id == targetId).fold(Score.Zero)(_.reward)
      }.toArray
    }.toArray

}