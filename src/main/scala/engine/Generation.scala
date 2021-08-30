package engine

import engine.Generation.{Environment, Population}

trait Generation{
  def environment: Environment
  def population: Population
  def population_=(bunnies:Population)
  def ended: Unit
}

object Generation{

  type Environment = String
  type Bunny = Int
  type Population = Set[Bunny]

  def apply(actualEnvironment:Environment, bunniesAlive:Population): Generation =
    new GenerationImpl(actualEnvironment, bunniesAlive)

  private class GenerationImpl( override val environment: Environment,
                                override var population: Population,
                                var isEnded:Boolean = false) extends Generation{
    override def ended: Unit = isEnded = true
  }
}



