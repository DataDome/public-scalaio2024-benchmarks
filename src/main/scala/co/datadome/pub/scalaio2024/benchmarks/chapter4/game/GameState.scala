package co.datadome.pub.scalaio2024.benchmarks.chapter4.game

final case class GameState(
  positionX: Int,
  positionY: Int,
  positionZ: Int
) {
  def updated(move: GameMove) = move match {
    case GameMove.Up => copy(positionZ = positionZ + 1)
    case GameMove.Down => copy(positionZ = positionZ - 1)
    case GameMove.North => copy(positionY = positionY + 1)
    case GameMove.South => copy(positionY = positionY - 1)
    case GameMove.East => copy(positionX = positionX + 1)
    case GameMove.West => copy(positionX = positionX - 1)
  }
}
