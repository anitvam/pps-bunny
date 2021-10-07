package engine

import cats.effect.IO
import engine.GenerationTimer.{ resetTimer, waitFor }
import engine.Simulation.{ extinction, _ }
import engine.SimulationConstants._
import engine.SimulationHistory.{ existNextGeneration, getBunniesNumber, getGenerationNumber }
import model.world.GenerationsUtils.{ FoodPhase, GenerationPhase, HighTemperaturePhase, ReproductionPhase, WolvesPhase }

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

  private def generationPhase(generationPhase: GenerationPhase, action: IO[Unit]): IO[Unit] =
    if (getBunniesNumber >= MIN_ALIVE_BUNNIES) {
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
        if (getBunniesNumber < MIN_ALIVE_BUNNIES) extinction()
        else if (getBunniesNumber > MAX_ALIVE_BUNNIES) overpopulation()
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
