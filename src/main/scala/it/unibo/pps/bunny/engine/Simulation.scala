package it.unibo.pps.bunny.engine

import cats.effect.IO
import it.unibo.pps.bunny.controller.Controller
import it.unibo.pps.bunny.engine.SimulationHistory._
import it.unibo.pps.bunny.model.world.GenerationsUtils.GenerationPhase
import it.unibo.pps.bunny.model.world.disturbingFactors._
import it.unibo.pps.bunny.model.world.disturbingFactors.PimpFactors._
import it.unibo.pps.bunny.util.PimpScala._

import scala.language.implicitConversions

object Simulation {

  /** An [[IO]] Monad that elaborates the simulation phase in which wolves eats bunnies */
  def wolvesEat: IO[Unit] = applyFactorDamage(WolvesFactorKind)

  /** An [[IO]] Monad that elaborates the simulation phase in which bunnies eats */
  def bunniesEat: IO[Unit] = applyFactorDamage(FoodFactorKind)

  /**
   * An [[IO]] Monad that elaborates the simulation phase in which unfriendly temperature are applied
   * @return
   *   an [[IO]]
   */
  def applyTemperatureDamage: IO[Unit] = applyFactorDamage(UnfriendlyClimateFactorKind)

  /**
   * An [[IO]] Monad that updates the view with the current population and phase
   * @param generationPhase
   *   the phase of the generation
   * @return
   *   an [[IO]]
   */
  def updateView(generationPhase: GenerationPhase): IO[Unit] =
    Controller.updateView(generationPhase, getActualPopulation)

  /**
   * An [[IO]] Monad that shows the end of the simulation by overpopulation
   * @return
   *   an [[IO]]
   */
  def overpopulation(): IO[Unit] = {
    Controller.showEnd(Overpopulation)
  }

  /**
   * An [[IO]] Monad that shows the end of the simulation by extinction
   * @return
   *   an [[IO]]
   */
  def extinction(): IO[Unit] = {
    Controller.showEnd(Extinction)
  }

  /**
   * An [[IO]] Monad that shows the end of the simulation by max number of generations
   * @return
   *   an [[IO]]
   */
  def end(): IO[Unit] = {
    Controller.showEnd(GenerationsOverload)
  }

  /**
   * An [[IO]] Monad that generates the next generation of bunnies
   * @return
   *   an [[IO]]
   */
  def startNewGeneration: IO[Unit] = SimulationHistory.startNextGeneration()

  /**
   * Implicit that converts a [[Unit]] function into a [[IO]] Monad
   * @param exp
   *   the function that returns [[Unit]]
   * @return
   *   an [[IO]]
   */
  implicit def unitToIO(exp: => Unit): IO[Unit] = IO { exp }

  /**
   * Method that applies a [[FactorKind]] damage to the actual population
   * @param factorKind
   *   the [[FactorKind]] on which the damage is computed
   */
  private def applyFactorDamage(factorKind: FactorKind): Unit =
    getActualGeneration.environment.factors.getFactor(factorKind) --> { f =>
      getActualGeneration.population = f applyDamage (getActualPopulation, getActualGeneration.environment.climate)
    }

}

/** End types of the simulation */
sealed trait SimulationEndType
case object Overpopulation extends SimulationEndType
case object Extinction extends SimulationEndType
case object GenerationsOverload extends SimulationEndType
