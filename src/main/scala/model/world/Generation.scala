package model.world

import engine.SimulationConstants.{FOOD_PHASE, START_PHASE, TEMPERATURE_PHASE, WOLVES_PHASE}
import model.Bunny
import model.world.Generation.Population

/**The unit of time of the simulation and wraps its properties*/
trait Generation{
  /**@return the current [[Environment]]*/
  def environment: Environment

  /**@return the [[Population]] with both alive and dead bunny*/
  def population: Population

  /**Updates the population
   * @param bunnies the new set of bunnies*/
  def population_=(bunnies:Population): Unit

  /**@return the alive [[Population]] */
  def livingPopulation : Population = population.filter(_.alive)

  def isEnded: Boolean

  /**Sets this Generation as ended*/
  def isEnded_=(isEnded:Boolean): Unit

  /**@return the current number of alive bunnies*/
  def getAliveBunniesNumber:Int = livingPopulation.size
}

object Generation{

  type Population = Seq[Bunny]

  def apply(actualEnvironment:Environment, bunniesAlive:Population): Generation =
    new GenerationImpl(actualEnvironment, bunniesAlive)

  private class GenerationImpl( override val environment: Environment,
                                override var population: Population,
                                override var isEnded:Boolean = false) extends Generation
}

object GenerationsUtils{
  /**The phase of a Generation that is identified by its number*/
  trait GenerationPhase{
    /**@return the generation number to which the phase refers */
    def generationNumber:Int
    /**@return the phase of the generation */
    def phase:Double
  }
  case class StartPhase(override val generationNumber: Int, override val phase: Double = START_PHASE) extends GenerationPhase
  case class WolvesPhase(override val generationNumber: Int, override val phase: Double = WOLVES_PHASE) extends GenerationPhase
  case class FoodPhase(override val generationNumber: Int, override val phase: Double = FOOD_PHASE) extends GenerationPhase
  case class HighTemperaturePhase(override val generationNumber: Int, override val phase: Double = TEMPERATURE_PHASE) extends GenerationPhase
}



