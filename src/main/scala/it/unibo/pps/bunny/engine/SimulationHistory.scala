package it.unibo.pps.bunny.engine

import it.unibo.pps.bunny.engine.SimulationConstants._
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.Reproduction.{ initialCoupleGenerator, nextGenerationBunnies }
import it.unibo.pps.bunny.model.world._

import scala.language.implicitConversions

object SimulationHistory {

  type History = List[Generation]

  /** Function that resets the history */
  val historyInit: () => History = () => List(Generation(Environment(Summer, List()), initialCoupleGenerator().toSeq))

  /** History actual value */
  var history: History = historyInit()

  /** Resets history to the initial value */
  def resetHistory(): Unit = history = historyInit()

  /** @return the actual [[Generation]] */
  def getActualGeneration: Generation = history.head

  /** @return how many generation have been lived */
  def getGenerationNumber: Int = history.length - 1

  /** @return how many bunnies are alive in the actual generation */
  def getActualBunniesNumber: Int = getActualGeneration.getAliveBunniesNumber

  /** @return the [[Population]] of the actual generation */
  def getActualPopulation: Population = getActualGeneration.livingPopulation

  /** @return true if there is a next generation, otherwise false */
  def existNextGeneration: Boolean = !worldIsOverpopulated && !bunniesAreExtinct && !tooManyGenerations

  /** @return true if the world is overpopulated by the bunnies, otherwise false */
  def worldIsOverpopulated: Boolean = getActualBunniesNumber >= MAX_ALIVE_BUNNIES

  /** @return true if all bunnies are dead, otherwise false */
  def bunniesAreExtinct: Boolean = getActualBunniesNumber < MIN_ALIVE_BUNNIES

  /** @return true if too many generations have passed, otherwise false */
  def tooManyGenerations: Boolean = getGenerationNumber >= MAX_GENERATIONS_NUMBER

  /** Terminate the actual [[Generation]] and starts the next one */
  def startNextGeneration(): Unit = {
    getActualGeneration.terminate()
    history = Generation(getEnvironmentForNextGeneration, getPopulationForNextGeneration) :: history
  }

  /** @return the [[Population]] for the next [[Generation]] */
  private def getPopulationForNextGeneration: Population =
    nextGenerationBunnies(getActualPopulation, getActualGeneration.environment.mutations)

  /** @return the [[Environment]] for the next [[Generation]] */
  private def getEnvironmentForNextGeneration: Environment =
    Environment.fromPreviousOne(getActualGeneration.environment)

}
