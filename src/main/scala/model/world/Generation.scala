package model.world

import model.Bunny
import model.world.Generation.{Environment, Population}

/**The unit of time of the simulation and wraps its properties*/
trait Generation{
  /**@return the current [[Environment]]*/
  def environment: Environment

  /**@return the current [[Population]]*/
  def population: Population

  /**Updates the current population
   * @param bunnies the new set of bunnies*/
  def population_=(bunnies:Population): Unit

  def isEnded: Boolean

  /**Sets this Generation as ended*/
  def isEnded_=(isEnded:Boolean): Unit

  /**@return the current number of alive bunnies*/
  def getBunniesNumber:Int = population.count(_.alive)
}

object Generation{

  type Environment = String
  type Population = Seq[Bunny]

  def apply(actualEnvironment:Environment, bunniesAlive:Population): Generation =
    new GenerationImpl(actualEnvironment, bunniesAlive)

  private class GenerationImpl( override val environment: Environment,
                                override var population: Population,
                                override var isEnded:Boolean = false) extends Generation
}



