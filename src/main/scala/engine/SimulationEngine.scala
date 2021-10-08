package engine

import cats.effect.IO
import engine.GenerationTimer.{ resetTimer, waitFor }
import engine.Simulation._
import engine.SimulationConstants._
import engine.SimulationHistory.{ getActualBunniesNumber, getGenerationNumber }
import engine.engineConversions._
import model.world.GenerationsUtils._

import scala.concurrent.duration.{ DurationDouble, FiniteDuration }
import scala.language.{ implicitConversions, postfixOps }

object SimulationEngine {
  var simulationSpeed: Double = 1

  def incrementSpeed(): Unit = simulationSpeed match {
    case DEFAULT_SPEED  => simulationSpeed = TWO_PER_SPEED
    case TWO_PER_SPEED  => simulationSpeed = FOUR_PER_SPEED
    case FOUR_PER_SPEED => simulationSpeed = DEFAULT_SPEED
  }

  private def generationPhase(generationPhase: GenerationPhase, action: IO[Unit]): IO[Unit] =
    if (getActualBunniesNumber >= MIN_ALIVE_BUNNIES) {
      for {
        _ <- waitFor((generationPhase.instant, simulationSpeed))
        _ <- action
        _ <- updateView(generationPhase)
      } yield ()
    } else IO.unit

  private def generationLoop(): IO[Unit] = {
    for {
      _ <- resetTimer
      _ <- generationPhase(WolvesPhase(getGenerationNumber), wolvesEat)
      _ <- generationPhase(FoodPhase(getGenerationNumber), bunniesEat)
      _ <- generationPhase(HighTemperaturePhase(getGenerationNumber), applyTemperatureDamage)
      _ <- generationPhase(ReproductionPhase(getGenerationNumber + 1), startNewGeneration)
      _ <-
        if (getActualBunniesNumber < MIN_ALIVE_BUNNIES) extinction()
        else if (getActualBunniesNumber > MAX_ALIVE_BUNNIES) overpopulation()
        else if (getGenerationNumber >= MAX_GENERATIONS_NUMBER) end()
        else generationLoop()
    } yield ()
  }

  def simulationLoop(): IO[Unit] = {
    for {
      _ <- updateView(ReproductionPhase(getGenerationNumber))
      _ <- generationLoop()
    } yield ()
  }

  def resetEngine(): Unit = {
    simulationSpeed = 1
  }

}

object engineConversions {
  implicit def fromTupleToFiniteDuration(d: (Double, Double)): FiniteDuration = (d._1 * d._2) millis
}
