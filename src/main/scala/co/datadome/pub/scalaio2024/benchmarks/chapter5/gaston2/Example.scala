package co.datadome.pub.scalaio2024.benchmarks.chapter5.gaston2

import scala.util.Random

object Example {

  private object Persons {
    private val Names = List(
      "Arthur", "Bianca", "Charles", "Donna", "Eric",
      "Fiona", "Georges", "Haley", "Isidore", "Jane",
      "Kevin", "Lily", "Maurice", "Nicky", "Oliver",
      "Patricia", "Quentin", "Rita", "Steven", "Thomas",
      "Ursula", "Veronica"
    )
    val All: List[Person] = Names.zipWithIndex.map { case (n, i) => Person(i, n) }

    def person(n: String): Person = All.find(_.name == n).get
  }

  private object Topics {

    import Persons.person

    val DnD: Topic = Topic(0, "Dungeons & Dragon", mandatory = Set(person("Arthur")), min = 4, max = 7)
    val Cthulhu: Topic = Topic(1, "Call of Cthulhu", mandatory = Set(person("Bianca"), person("Lily")), min = 3, max = 5)
    val Vampire: Topic = Topic(2, "Vampire", mandatory = Set(person("Charles")), min = 3, max = 6)
    val L5R: Topic = Topic(3, "Legend of the Five Rings", mandatory = Set(person("Donna"), person("Eric")), min = 4, max = 6)
    val InsMv: Topic = Topic(4, "INS/MV", mandatory = Set(person("Fiona")), min = 3, max = 6)
    val All: List[Topic] = List(DnD, Cthulhu, Vampire, InsMv, L5R)
  }

  private object Preferences {
    private val random = new Random(0)

    import Persons.person

    private val negativePreferences = Set(
      PersonPersonAntiPreference(person("Patricia"), person("Quentin"), Score(-100)),
      PersonPersonAntiPreference(person("Steven"), person("Thomas"), Score(-100)),
    )

    private val wishes = Persons.All.flatMap { person =>
      val List(wish1, wish2, wish3) = random.shuffle(Topics.All).take(3)
      List(
        PersonTopicPreference(person, wish1, Score(70)),
        PersonTopicPreference(person, wish2, Score(30)),
        PersonTopicPreference(person, wish3, Score(10))
      )
    }

    val All: Set[Preference] = negativePreferences ++ wishes
  }

  given problem: Problem = Problem(Topics.All.toSet, Persons.All.toSet, Preferences.All.toSet)

  val startingSchedule: Schedule = Schedule.from(
    Record(Topics.DnD, Seq("Arthur", "Georges", "Haley", "Isidore", "Jane").map(Persons.person) *),
    Record(Topics.Cthulhu, Seq("Bianca", "Kevin", "Lily", "Maurice").map(Persons.person) *),
    Record(Topics.Vampire, Seq("Charles", "Nicky", "Oliver", "Patricia", "Quentin").map(Persons.person) *),
    Record(Topics.L5R, Seq("Donna", "Eric", "Rita", "Steven", "Thomas").map(Persons.person) *),
    Record(Topics.InsMv, Seq("Fiona", "Ursula", "Veronica").map(Persons.person) *)
  )
}
