package co.datadome.pub.scalaio2024.utils

import scala.util.Random

extension (rand: Random) {

  def nextSimpleString(size: Int): String = nextSimpleString(size, size)

  def nextSimpleString(minSize: Int, maxSize: Int): String = {
    val size = minSize + rand.nextInt(maxSize - minSize)
    val stringBuilder = new StringBuilder()
    (0 until size).foreach { _ =>
      val char = rand.nextInt(93) + 33 // Generate an ASCII character between codes 33 to 126
      stringBuilder.append(char.toChar)
    }
    stringBuilder.toString
  }

}
