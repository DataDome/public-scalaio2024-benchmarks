package co.datadome.pub.scalaio2024.benchmarks.chapter4.game

final class Goal {
  def evaluate(gs: GameState): Int =
    gs.positionX * gs.positionY - 2 * gs.positionZ

  def evaluate(gs: MutableGameState): Int =
    gs.positionX * gs.positionY - 2 * gs.positionZ
}
