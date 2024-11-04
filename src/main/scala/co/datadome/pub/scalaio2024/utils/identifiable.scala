package co.datadome.pub.scalaio2024.utils

trait Identified {
  def id: Int
}

trait Identifiable[A] {
  extension (a: A) {
    def id: Int
  }
}

object Identifiable {
  given Identifiable[Int] with {
    extension (i: Int) {
      def id: Int = i
    }
  }

  given [A <: Identified]: Identifiable[A] with {
    extension (a: A) {
      def id: Int = a.id
    }
  }

  given [A]: Identifiable[(Int, A)] with {
    extension (ia: (Int, A)) {
      def id: Int = ia._1
    }
  }
}