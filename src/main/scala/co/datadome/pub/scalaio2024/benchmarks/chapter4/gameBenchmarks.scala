package co.datadome.pub.scalaio2024.benchmarks.chapter4

import co.datadome.pub.scalaio2024.benchmarks.chapter4.game.*
import co.datadome.pub.scalaio2024.utils.*
import org.openjdk.jmh.annotations.*

import java.util.concurrent.TimeUnit
import scala.reflect.ClassTag
import scala.util.Random

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 10, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, jvmArgs = Array(
  "-server",
  "-Xms1g",
  "-Xmx1g",
  "-XX:NewSize=512m",
  "-XX:MaxNewSize=512m",
  "-XX:InitialCodeCacheSize=256m",
  "-XX:ReservedCodeCacheSize=256m",
  "-XX:+UseParallelGC",
  "-XX:MaxInlineLevel=18",
  "-XX:+AlwaysPreTouch"
))
abstract class GameBenchmarkBase(val testSize: Int) {

  val rand = new Random(0)
  val positions = Seq.fill(testSize) {
    (rand.nextInt(100), rand.nextInt(100), rand.nextInt(100))
  }
  val startingPositions: Seq[GameState] = positions.map { case (x, y, z) => GameState(x, y, z) }
  val mutableStartingPositions: Seq[MutableGameState] = positions.map { case (x, y, z) => MutableGameState(x, y, z) }

  /* We want to see the best score among all possible series of three moves */

  val goal = new Goal

  @Benchmark def immutable(): Seq[GameState] = startingPositions.map { initialSituation =>
    var bestScore = Int.MinValue
    var bestMoves = (GameMove.Up, GameMove.Up, GameMove.Up, GameMove.Up)
    var bestPosition = initialSituation
    GameMove.values.fastForeach { one =>
      val updatedOne = initialSituation.updated(one)
      GameMove.values.fastForeach { two =>
        val updatedTwo = updatedOne.updated(two)
        GameMove.values.fastForeach { three =>
          val updatedThree = updatedTwo.updated(three)
          GameMove.values.fastForeach { four =>
            val updatedFour = updatedThree.updated(four)
            val newScore = goal.evaluate(updatedFour)
            if (newScore > bestScore) {
              bestScore = newScore
              bestMoves = (one, two, three, four)
              bestPosition = updatedThree
            }
          }
        }
      }
    }
    bestPosition
  }

  @Benchmark def mutable(): Seq[MutableGameState] = mutableStartingPositions.map { initialSituation =>
    val gameState = initialSituation.copy()
    var bestScore = Int.MinValue
    var bestMoves = (GameMove.Up, GameMove.Up, GameMove.Up, GameMove.Up)
    GameMove.values.fastForeach { one =>
      gameState.update(one)
      GameMove.values.fastForeach { two =>
        gameState.update(two)
        GameMove.values.fastForeach { three =>
          gameState.update(three)
          GameMove.values.fastForeach { four =>
            gameState.update(four)
            val newScore = goal.evaluate(gameState)
            if (newScore > bestScore) {
              bestScore = newScore
              bestMoves = (one, two, three, four)
            }
            gameState.reverse(four)
          }
          gameState.reverse(three)
        }
        gameState.reverse(two)
      }
      gameState.reverse(one)
    }
    gameState.update(bestMoves._1).update(bestMoves._2).update(bestMoves._3).update(bestMoves._4)
    gameState
  }
}

class GameBenchmarkXSmall extends GameBenchmarkBase(5)

class GameBenchmarkSmall extends GameBenchmarkBase(100)

class GameBenchmarkMedium extends GameBenchmarkBase(10000)

class GameBenchmarkLarge extends GameBenchmarkBase(1000000)
