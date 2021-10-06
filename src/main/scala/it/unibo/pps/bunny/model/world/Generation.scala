package it.unibo.pps.bunny.model.world

import it.unibo.pps.bunny.engine.SimulationConstants._
import it.unibo.pps.bunny.model.{ Bunny, ChildBunny }
import it.unibo.pps.bunny.model.world.Generation.Population

/** The unit of time of the simulation and wraps its properties */
trait Generation {

  /** @return the current [[Environment]] */
  def environment: Environment

  /** @return the [[Population]] with both alive and dead bunny */
  def population: Population

  /**
   * Updates the population
   * @param bunnies
   *   the new set of bunnies
   */
  def population_=(bunnies: Population): Unit

  /** @return the alive [[Population]] */
  def livingPopulation: Population = population.filter(_.alive)

  def populationAtTheEnd: Population

  def populationAtTheEnd_=(population: Population): Unit

  /** @return true if the population isEnded, otherwise false */
  def isEnded: Boolean

  /** Sets this Generation as ended */
  def isEnded_=(isEnded: Boolean): Unit

  def terminate(): Unit = {
    this.isEnded = true
    populationAtTheEnd = this.population.map(b => new ChildBunny(b.genotype, b.mom, b.dad, b.age, b.alive))
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

    /** @return the generation number to which the phase refers */
    def generationNumber: Int

    /** @return the phase of the generation */
    def phase: Double

    /** @return after how many milliseconds from the start of generation there is the phase istant */
    def instant: Double
  }

  case class ReproductionPhase(
      override val generationNumber: Int,
      override val phase: Double = REPRODUCTION_PHASE,
      override val instant: Double = GENERATION_END
  ) extends GenerationPhase

  case class WolvesPhase(
      override val generationNumber: Int,
      override val phase: Double = WOLVES_PHASE,
      override val instant: Double = WOLVES_INSTANT
  ) extends GenerationPhase

  case class FoodPhase(
      override val generationNumber: Int,
      override val phase: Double = FOOD_PHASE,
      override val instant: Double = FOOD_INSTANT
  ) extends GenerationPhase

  case class HighTemperaturePhase(
      override val generationNumber: Int,
      override val phase: Double = TEMPERATURE_PHASE,
      override val instant: Double = TEMP_INSTANT
  ) extends GenerationPhase

}