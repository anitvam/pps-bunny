package it.unibo.pps.bunny.model.world

import it.unibo.pps.bunny.model.bunny.{ Bunny, HistoryBunny }
import it.unibo.pps.bunny.engine.SimulationConstants.PhasesConstants._
import it.unibo.pps.bunny.model.world.Generation.Population

/** The unit of time of the simulation and wraps its properties */
trait Generation {

  /** @return the current [[Environment]] */
  def environment: Environment

  /** The [[Population]] with both alive and dead bunnies */
  var population: Population

  /** @return the alive [[Population]] */
  def livingPopulation: Population = population filter { _.alive }

  /** The [[Population]] at the end of the Generation */
  var populationAtTheEnd: Population

  /** Generation end value: true if the population isEnded, otherwise false */
  var isEnded: Boolean

  /** Method that terminates the actual Generation */
  def terminate(): Unit = {
    this.isEnded = true
    populationAtTheEnd = this.population map { HistoryBunny }
  }

  /** @return the current number of alive bunnies */
  def getAliveBunniesNumber: Int = livingPopulation.size
}

object Generation {

  type Population = Seq[Bunny]

  def apply(actualEnvironment: Environment, bunniesAlive: Population): Generation =
    new GenerationImpl(actualEnvironment, bunniesAlive)

  private class GenerationImpl(
      override val environment: Environment,
      override var population: Population,
      override var populationAtTheEnd: Population = Seq(),
      override var isEnded: Boolean = false
  ) extends Generation

}

object GenerationsUtils {

  /** The phase of a Generation that is identified by its number */
  trait GenerationPhase {

    /** @return the generation number on which the phase refers */
    def generationNumber: Int

    /** @return the phase of the Generation */
    def phase: Double

    /** @return after how many milliseconds from the start of generation there is the phase instant */
    def instant: Double

    /** @return the name of the GenerationsPhase */
    def name: String

  }

  case class ReproductionPhase(
      override val generationNumber: Int,
      override val phase: Double = REPRODUCTION_PHASE,
      override val instant: Double = GENERATION_END,
      override val name: String = "Riproduzione"
  ) extends GenerationPhase

  case class WolvesPhase(
      override val generationNumber: Int,
      override val phase: Double = WOLVES_PHASE,
      override val instant: Double = WOLVES_INSTANT,
      override val name: String = "Lupi"
  ) extends GenerationPhase

  case class FoodPhase(
      override val generationNumber: Int,
      override val phase: Double = FOOD_PHASE,
      override val instant: Double = FOOD_INSTANT,
      override val name: String = "Cibo"
  ) extends GenerationPhase

  case class HighTemperaturePhase(
      override val generationNumber: Int,
      override val phase: Double = TEMPERATURE_PHASE,
      override val instant: Double = TEMP_INSTANT,
      override val name: String = "Temperature"
  ) extends GenerationPhase

}
