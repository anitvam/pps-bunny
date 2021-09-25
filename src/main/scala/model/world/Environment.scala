package model.world

import model.mutation.Mutation
import model.world.Environment.{ Factors, Mutations }

/** Environment of a Generation */
trait Environment {

  /**
   * Get the actual climate of the Environment
   * @return the Climate
   */
  def climate: Climate

  /**
   * Set a new climate inside the Environment
   * @param climate the new climate value
   */
  def climate_=(climate: Climate): Unit

  /** @return the Environment Mutations */
  def mutations: Mutations

  /**
   * Set a list of mutation inside the Environment
   * @param mutations
   *   a List[Mutation]
   */
  def mutations_=(mutations: Mutations): Unit

  /** @return the Environment Factors */
  def factors: Factors
}

object Environment {

  type Factor = String

  type Mutations = List[Mutation]
  type Factors = List[Factor]

  /**
   * Generate an Environment from the previous one
   * @param environment the previous Environment
   */
  def fromPreviousOne(environment: Environment): Environment = apply(environment.climate, environment.factors)

  def apply(climate: Climate, factors: Factors): Environment = EnvironmentImpl(climate, factors)

  private case class EnvironmentImpl(
      override var climate: Climate,
      override val factors: Factors,
      override var mutations: Mutations = List()
  ) extends Environment {}

}

/** Climate Trait */
trait Climate

/** Summer Climate */
case class Summer() extends Climate

/** Winter Climate */
case class Winter() extends Climate
