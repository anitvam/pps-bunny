package model.world

import model.Bunny
import model.world.Generation.{Environment, Population}

trait Generation{
  def environment: Environment
  def population: Population
  def population_=(bunnies:Population):Unit
  def ended(): Unit

  def getBunniesNumber:Int = population.count(_.alive)
}

object Generation{

  type Environment = String
  type Population = Seq[Bunny]

  def apply(actualEnvironment:Environment, bunniesAlive:Population): Generation =
    new GenerationImpl(actualEnvironment, bunniesAlive)

  private class GenerationImpl( override val environment: Environment,
                                override var population: Population,
                                var isEnded:Boolean = false) extends Generation{
    override def ended(): Unit = isEnded = true
  }
}



