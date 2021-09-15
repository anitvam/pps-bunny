package engine

import model.mutation.Mutation
import model.world.Generation
import model.world.Generation.{Environment, Population}
import model.world.Reproduction.{generateInitialCouple, nextGenerationBunnies}

import scala.language.implicitConversions

object SimulationHistory{

  type History = List[Generation]

  var history:History = List()

  var mutations: Option[List[Mutation]] = None

  /** Initialize the [[History]] of this simulation
   * @param environment the initial environment of the first [[Generation]] */
  def initialize(environment: Environment): Unit =
    history = Generation(environment, generateInitialCouple) :: history

  /** Introduce a new mutation */
  def introduceMutation(mutation: Mutation): Unit = mutations match {
    case None => mutations = Some(List(mutation))
    case _ => mutations = Some(mutation :: mutations.get)
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
  def getPopulationForNextGeneration : Population = nextGenerationBunnies(getActualPopulation)(mutations)

  /**@return the [[Environment]]  for the next [[Generation]]*/
  def getEnvironmentForNextGeneration : Environment = getActualGeneration.environment

  /**Terminate the actual [[Generation]] and start the next one*/
  def startNextGeneration() : Unit = {
    endActualGeneration()
    history = Generation(getEnvironmentForNextGeneration, getPopulationForNextGeneration) :: history
  }
}


