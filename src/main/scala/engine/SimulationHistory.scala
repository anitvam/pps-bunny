package engine

import engine.SimulationConstants._
import model.world.Generation.Population
import model.world.Reproduction.{generateInitialCouple, nextGenerationBunnies}
import model.world._
import model.world.disturbingFactors.Factors

import scala.language.implicitConversions

object SimulationHistory {

  type History = List[Generation]

  val historyInit: () => History = () => List(Generation(Environment(Summer(), Factors()), generateInitialCouple().toSeq))

  var history: History = historyInit()

  /** Resets history to the initial value */
  def resetHistory(): Unit = history = historyInit()

  /** Reset all the mutations added */
  def resetMutations(): Unit = getActualGeneration.environment.mutations = List()

  /** @return the actual [[Generation]] */
  def getActualGeneration: Generation = history.head

  /** @return how many generation have been lived */
  def getGenerationNumber: Int = history.length - 1

  /** @return how many bunnies are alive in the actual generation */
  def getBunniesNumber: Int = getActualGeneration.getAliveBunniesNumber

  /** @return the [[Population]] of the actual generation */
  def getActualPopulation: Population = getActualGeneration.livingPopulation

  /**
   * Determines if there is a next generation
   */
  def existNextGeneration: Boolean =
    getGenerationNumber < MAX_GENERATIONS_NUMBER && getBunniesNumber < MAX_ALIVE_BUNNIES && getBunniesNumber >= MIN_ALIVE_BUNNIES

  /**
   * Determines if the wold is overpopulated by the bunnies
   */
  def isOverpopulated: Boolean = getBunniesNumber >= MAX_ALIVE_BUNNIES

  /** @return the [[Population]]  for the next [[Generation]] */
  def getPopulationForNextGeneration: Population =
    nextGenerationBunnies(getActualPopulation, getActualGeneration.environment.mutations)

  /** @return the [[Environment]]  for the next [[Generation]] */
  def getEnvironmentForNextGeneration: Environment = Environment.fromPreviousOne(getActualGeneration.environment)

  /** Terminate the actual [[Generation]] and start the next one */
  def startNextGeneration(): Unit = {
    getActualGeneration.terminate()
    history = Generation(getEnvironmentForNextGeneration, getPopulationForNextGeneration) :: history
  }

  /**
   * Change the Environment of the actual generation
   * @param climate
   *   the climate to set into the Environment
   */
  def changeEnvironmentClimate(climate: Climate): Unit = getActualGeneration.environment.climate = climate
}
