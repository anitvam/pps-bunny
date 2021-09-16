package model.world

import model.world.Environment.{Factor, Factors, Mutation, Mutations}

/** Environment of a Generation */
trait Environment {
  /** Get the actual climate of the Environment
   * @return the Climate */
  def climate: Climate

  /** Set a new climate inside the Environment
   * @param climate the new climate value */
  def climate_=(climate: Climate): Unit

  /** @return the Environment Mutations */
  def mutations: Mutations

  /** @return the Environment Factors */
  def factors: Factors
}

object Environment {

  type Mutation = String
  type Factor = String

  type Mutations = List[Mutation]
  type Factors = List[Factor]

  def apply(climate: Climate, factors: Factors): Environment = EnvironmentImpl(climate, factors)
  def fromPreviousOne(environment: Environment): Environment = apply(environment.climate, environment.factors)

  private case class EnvironmentImpl(override var climate: Climate,
                                     override val factors: Factors,
                                     override val mutations: Mutations = List()) extends Environment {
  }
}

/** Climate Trait */
trait Climate

/** Summer Climate */
case class Summer() extends Climate

/** Winter Climate */
case class Winter() extends Climate
