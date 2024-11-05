package co.datadome.pub.scalaio2024.benchmarks.chapter4.game

final case class MutableGameState(
  var positionX: Int,
  var positionY: Int,
  var positionZ: Int
) {
  def update(move: GameMove) = {
    move match {
      case GameMove.Up => positionZ = positionZ + 1
      case GameMove.Down => positionZ = positionZ - 1
      case GameMove.North => positionY = positionY + 1
      case GameMove.South => positionY = positionY - 1
      case GameMove.East => positionX = positionX + 1
      case GameMove.West => positionX = positionX - 1
    }
    this
  }

  def reverse(move: GameMove) = {
    move match {
      case GameMove.Up => positionZ = positionZ - 1
      case GameMove.Down => positionZ = positionZ + 1
      case GameMove.North => positionY = positionY - 1
      case GameMove.South => positionY = positionY + 1
      case GameMove.East => positionX = positionX - 1
      case GameMove.West => positionX = positionX + 1
    }
    this
  }
}
