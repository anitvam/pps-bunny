package engine

import cats.effect.IO
import engine.GenerationTimer.{ resetTimer, waitFor }
import engine.Simulation._
import engine.SimulationConstants._
import engine.SimulationHistory.{ getBunniesNumber, getGenerationNumber }
import model.world.GenerationsUtils.{ FoodPhase, HighTemperaturePhase, StartPhase, WolvesPhase }
import scala.language.{ implicitConversions, postfixOps }
import engineConversions._
import scala.concurrent.duration.{ DurationDouble, FiniteDuration }

object SimulationEngine {
  var simulationSpeed: Double = 1

  def incrementSpeed(): Unit = simulationSpeed match {
    case DEFAULT_SPEED  => simulationSpeed = TWO_PER_SPEED
    case TWO_PER_SPEED  => simulationSpeed = FOUR_PER_SPEED
    case FOUR_PER_SPEED => simulationSpeed = DEFAULT_SPEED
  }

  def simulationLoop(): IO[Unit] = {
    for {
      _ <- resetTimer
      _ <- updateView(StartPhase(getGenerationNumber))
      _ <- waitFor((WOLF_INSTANT, simulationSpeed))
      _ <- wolvesEat
      _ <- updateView(WolvesPhase(getGenerationNumber))
      _ <- waitFor((FOOD_INSTANT, simulationSpeed))
      _ <- bunniesEat
      _ <- updateView(FoodPhase(getGenerationNumber))
      _ <- waitFor((TEMP_INSTANT, simulationSpeed))
      _ <- applyTemperatureDamage
      _ <- updateView(HighTemperaturePhase(getGenerationNumber))
      _ <- waitFor((GENERATION_END, simulationSpeed))
      _ <- startNewGeneration
      _ <-
        if (getGenerationNumber < MAX_GENERATIONS_NUMBER && getBunniesNumber < MAX_ALIVE_BUNNIES) simulationLoop()
        else showEnd(StartPhase(getGenerationNumber))
    } yield ()
  }

}

object engineConversions {
  implicit def fromTupleToFiniteDuration(d: (Double, Double)): FiniteDuration = (d._1 * d._2) millis
}
