package engine

import model.world.{Climate, Environment, Generation, Summer}
import model.world.Generation.Population
import model.world.Reproduction.{generateInitialCouple, nextGenerationBunnies}

import scala.language.implicitConversions

object SimulationHistory{

  type History = List[Generation]

  var history: History = List(Generation(Environment(Summer(), List.empty), generateInitialCouple))

  var mutations: Option[List[Mutation]] = None

  /** Initialize the [[History]] of this simulation
   * @param environment the initial environment of the first [[Generation]] */
  def initialize(environment: Environment): Unit =
    history = Generation(environment, generateInitialCouple) :: history

  /** Introduce a new mutation */
  def introduceMutation(mutation: Mutation): Unit = {
    mutations match {
      case None => mutations = Some(List(mutation))
      case _ => mutations = Some(mutation :: mutations.get)
    }
    if(mutation.isDominant) KindsUtils.setAlleleDominance(mutation.geneKind.mutated)
    else KindsUtils.setAlleleDominance(mutation.geneKind.base)

  }

  /** Reset all the mutations added */
  def resetMutations(): Unit = {
    mutations = None
  }

  /**@return the actual [[Generation]]*/
  def getActualGeneration: Generation = history.head

  /**Terminate the actual [[Generation]]*/
  def endActualGeneration(): Unit = getActualGeneration.isEnded = true

  /**@return how many generation have been lived*/
  def getGenerationNumber: Int = history.length

  /**@return how many bunnies are alive in the actual generation*/
  def getBunniesNumber: Int = getActualGeneration.getBunniesNumber

  /**@return the [[Population]] of the actual generation*/
  def getActualPopulation: Population = getActualGeneration.population

  /**@return the [[Population]]  for the next [[Generation]]*/
  def getPopulationForNextGeneration : Population = nextGenerationBunnies(getActualPopulation, mutations)

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


