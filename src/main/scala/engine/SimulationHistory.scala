package engine

import model.genome.KindsUtils
import model.mutation.Mutation
import model.world.{Climate, Environment, Generation, Summer}
import model.world.Generation.Population
import model.world.Reproduction.{generateInitialCouple, nextGenerationBunnies}
import util.PimpScala.RichTuple2
import scala.language.implicitConversions

object SimulationHistory{

  type History = List[Generation]

  var history: History = List(Generation(Environment(Summer(), List.empty), generateInitialCouple.toSeq))

  /** Initialize the [[History]] of this simulation
   * @param environment the initial environment of the first [[Generation]] */
  def initialize(environment: Environment): Unit =
    history = Generation(environment, generateInitialCouple.toSeq) :: history

  /** Introduce a new mutation */
  def introduceMutation(mutation: Mutation): Unit = {
    getActualGeneration.environment.mutations = mutation :: getActualGeneration.environment.mutations

    if (mutation.isDominant) KindsUtils.setAlleleDominance(mutation.geneKind.mutated)
    else KindsUtils.setAlleleDominance(mutation.geneKind.base)
  }

  /** Reset all the mutations added */
  def resetMutations(): Unit = {
    getActualGeneration.environment.mutations = List()
  }

  /**@return the actual [[Generation]]*/
  def getActualGeneration: Generation = history.head

  /**Terminate the actual [[Generation]]*/
  def endActualGeneration(): Unit = getActualGeneration.isEnded = true

  /**@return how many generation have been lived*/
  def getGenerationNumber: Int = history.length-1

  /**@return how many bunnies are alive in the actual generation*/
  def getBunniesNumber: Int = getActualGeneration.getBunniesNumber

  /**@return the [[Population]] of the actual generation*/
  def getActualPopulation: Population = getActualGeneration.population

  /**@return the [[Population]]  for the next [[Generation]]*/
  def getPopulationForNextGeneration : Population = nextGenerationBunnies(getActualPopulation, getActualGeneration.environment.mutations)

  /**@return the [[Environment]]  for the next [[Generation]]*/
  def getEnvironmentForNextGeneration : Environment = Environment.fromPreviousOne(getActualGeneration.environment)

  /**Terminate the actual [[Generation]] and start the next one*/
  def startNextGeneration() : Unit = {
    endActualGeneration()
    history = Generation(getEnvironmentForNextGeneration, getPopulationForNextGeneration) :: history
  }

  /** Change the Environment of the actual generation
   * @param climate the climate to set into the Environment */
  def changeEnvironmentClimate(climate: Climate): Unit = getActualGeneration.environment.climate = climate
}


