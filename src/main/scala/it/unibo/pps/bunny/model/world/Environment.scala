package it.unibo.pps.bunny.model.world

import it.unibo.pps.bunny.model.bunny.Mutation
import it.unibo.pps.bunny.model.genome.KindsUtils
import it.unibo.pps.bunny.model.world.Environment.{Factors, Mutations}
import it.unibo.pps.bunny.model.world.disturbingFactors.Factor
import it.unibo.pps.bunny.model.world.disturbingFactors.PimpFactors._

import scala.language.postfixOps
import scala.language.implicitConversions

/** Environment of a Generation */
trait Environment {

  /**
   * Get the actual climate of the Environment
   * @return
   *   the Climate
   */
  def climate: Climate

  /**
   * Set a new climate inside the Environment
   * @param climate
   *   the new climate value
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
  var factors: Factors

  /**
   * Introduce the specified Factor inside the environment
   * @param factor
   *   the factor to introduce
   */
  def introduceFactor(factor: Factor): Unit

  /**
   * Remove the specified factor from the Environment
   * @param factor
   *   the factor to remove
   */
  def removeFactor(factor: Factor): Unit

  /**
   * Introduce the specified mutations inside the Environment
   * @param mutation
   *   the mutation to insert
   */
  def introduceMutation(mutation: Mutation): Unit
}

object Environment {

  type Mutations = List[Mutation]
  type Factors = List[Factor]

  /**
   * Generate an Environment from the previous one
   * @param environment
   *   the previous Environment
   */
  def fromPreviousOne(environment: Environment): Environment = apply(environment.climate, environment.factors)

  def apply(climate: Climate, factors: Factors): Environment = EnvironmentImpl(climate, factors)

  private case class EnvironmentImpl(
      override var climate: Climate,
      override var factors: Factors,
      override var mutations: Mutations = List()
  ) extends Environment {

    override def introduceFactor(factor: Factor): Unit = factors = factors + factor

    override def removeFactor(factor: Factor): Unit = factors = factors - factor

    def introduceMutation(mutation: Mutation): Unit = {
      KindsUtils.setAlleleDominanceFromMutation(mutation)
      mutations = mutation :: mutations
    }

  }

}

/** Climate Trait */
trait Climate

/** Summer Climate */
case object Summer extends Climate

/** Winter Climate */
case object Winter extends Climate
