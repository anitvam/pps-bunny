package engine

import cats.effect.IO
import engine.GenerationTimer.{ resetTimer, waitFor }
import engine.Simulation._
import engine.SimulationConstants._
import engine.SimulationHistory.{ getBunniesNumber, getGenerationNumber }
import model.world.GenerationsUtils.{ FoodPhase, GenerationPhase, HighTemperaturePhase, StartPhase, WolvesPhase }

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

  def generationPhase(generationPhase: GenerationPhase, action: IO[Unit]): IO[Unit] = for {
    _ <- waitFor((generationPhase.instant, simulationSpeed))
    _ <- action
    _ <- updateView(generationPhase)
  } yield ()

  def generationLoop(): IO[Unit] = {
    println("generation LOOP")
    for {
      _ <- resetTimer
      _ <- generationPhase(WolvesPhase(getGenerationNumber), wolvesEat)
      _ <-
        if (getBunniesNumber >= MIN_ALIVE_BUNNIES) generationPhase(FoodPhase(getGenerationNumber), bunniesEat)
        else showEnd(WolvesPhase(getGenerationNumber))
      _ <-
        if (getBunniesNumber >= MIN_ALIVE_BUNNIES)
          generationPhase(HighTemperaturePhase(getGenerationNumber), applyTemperatureDamage)
        else showEnd(FoodPhase(getGenerationNumber))
      _ <-
        if (getBunniesNumber >= MIN_ALIVE_BUNNIES)
          generationPhase(StartPhase(getGenerationNumber + 1), startNewGeneration)
        else showEnd(HighTemperaturePhase(getGenerationNumber))
      _ <-
        if (getGenerationNumber < MAX_GENERATIONS_NUMBER && getBunniesNumber < MAX_ALIVE_BUNNIES) generationLoop()
        else showEnd(StartPhase(getGenerationNumber))
    } yield ()
  }

  def simulationLoop(): IO[Unit] = {
    println("SIMULATION LOOP")
    for {
      _ <- updateView(StartPhase(getGenerationNumber))
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
