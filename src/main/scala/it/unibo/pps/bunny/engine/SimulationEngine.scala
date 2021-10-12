package it.unibo.pps.bunny.engine

import cats.effect.IO
import scala.language.{ implicitConversions, postfixOps }
import engineConversions._
import it.unibo.pps.bunny.engine.GenerationTimer.{ resetTimer, waitFor }
import it.unibo.pps.bunny.engine.Simulation.{
  applyTemperatureDamage, bunniesEat, end, extinction, overpopulation, startNewGeneration, updateView, wolvesEat
}
import it.unibo.pps.bunny.engine.SimulationConstants._
import it.unibo.pps.bunny.engine.SimulationHistory._
import it.unibo.pps.bunny.model.world.GenerationsUtils.{
  FoodPhase, GenerationPhase, HighTemperaturePhase, ReproductionPhase, WolvesPhase
}

import scala.concurrent.duration.{ DurationDouble, FiniteDuration }

object SimulationEngine {

  private var _simulationSpeed: Double = DEFAULT_SPEED

  /** @return the actual simulation speed */
  def simulationSpeed: Double = _simulationSpeed

  /** Method that increments the Simulation Speed in a circular way */
  def changeSpeed(): Unit = simulationSpeed match {
    case DEFAULT_SPEED  => _simulationSpeed = TWO_PER_SPEED
    case TWO_PER_SPEED  => _simulationSpeed = FOUR_PER_SPEED
    case FOUR_PER_SPEED => _simulationSpeed = DEFAULT_SPEED
  }

  private def generationPhase(generationPhase: GenerationPhase, action: IO[Unit]): IO[Unit] =
    if (!bunniesAreExtinct) {
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
        if (bunniesAreExtinct) extinction()
        else if (worldIsOverpopulated) overpopulation()
        else if (tooManyGenerations) end()
        else generationLoop()
    } yield ()
  }

  /** Engine Loop for the simulation */
  def simulationLoop(): IO[Unit] = {
    for {
      _ <- updateView(ReproductionPhase(getGenerationNumber))
      _ <- generationLoop()
    } yield ()
  }

  /** Method that resets the Simulation Speed */
  def resetEngine(): Unit = {
    _simulationSpeed = DEFAULT_SPEED
  }

}

object engineConversions {

  /**
   * Implicit method that converts a tuple into a FiniteDuration
   * @param d
   *   a [[(Double, Double)]] tuple
   * @return
   *   a [[FiniteDuration]]
   */
  implicit def fromTupleToFiniteDuration(d: (Double, Double)): FiniteDuration = (d._1 * d._2) millis
}
