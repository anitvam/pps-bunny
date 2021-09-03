package engine

import cats.effect.IO
import engine.SimulationHistory.{endedActualGeneration, getActualPopulation, getEnvironmentForNextGeneration, getPopulationForNextGeneration, history}
import model.world.Generation
import model.world.Generation.{Environment, Population}
import model.world.Reproduction.{generateInitialCouple, nextGenerationBunnies}
import view.scalaFX.ScalaFXView

import scala.language.implicitConversions

object SimulationHistory{

  type History = List[Generation]

  var history:History = List()

  /**Initiliaze the [[History]] of this simulation
   * @param environment the initial environment of the first [[Generation]]
   * @param initialPopulation the initial population*/
  def initialize(environment: Environment, initialPopulation: Population) =
    history = Generation(environment, initialPopulation) :: history

  /**@return the actual [[Generation]]*/
  def getActualGeneration: Generation = history.head

  /**Terminate the actual [[Generation]]*/
  def endedActualGeneration():Unit =getActualGeneration.ended()

  /**@return how many generation have been lived*/
  def getGenerationNumber: Int = history.length

  /**@return how many bunnies are alive in the actual generation*/
  def getBunniesNumber: Int = getActualGeneration.getBunniesNumber

  /**@return the [[Population]] of the actual generation*/
  def getActualPopulation: Population = getActualGeneration.population

  /**@return the [[Population]]  for the next [[Generation]]*/
  def getPopulationForNextGeneration : Population = nextGenerationBunnies(getActualPopulation)

  /**@return the [[Environment]]  for the next [[Generation]]*/
  def getEnvironmentForNextGeneration : Environment = getActualGeneration.environment

  /**Terminate the actual [[Generation]] and start the next one*/
  def startNextGeneration() : Unit = {
    endedActualGeneration()
    history = Generation(getEnvironmentForNextGeneration, getPopulationForNextGeneration) :: history
  }
}


