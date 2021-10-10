package it.unibo.pps.bunny.model.world

import it.unibo.pps.bunny.model.bunny.Mutation
import it.unibo.pps.bunny.model.genome.KindsUtils
import it.unibo.pps.bunny.model.world.Environment.{ Factors, Mutations }
import it.unibo.pps.bunny.model.world.disturbingFactors._
import it.unibo.pps.bunny.model.world.disturbingFactors.PimpFactors._

import scala.language.postfixOps
import scala.language.implicitConversions

/** Environment of a Generation */
trait Environment {

  /** The actual Environment [[Climate]] */
  var climate: Climate

  /** @return The actual Environment [[Mutations]] */
  def mutations: Mutations

  /** @return The Environment [[Factors]] */
  def factors: Factors

  /**
   * Introduce the specified [[Factor]] inside the Environment
   * @param factor
   *   the factor to introduce
   */
  def introduceFactor(factor: Factor): Unit

  /**
   * Remove the specified [[Factor]] from the Environment
   * @param factor
   *   the factor to remove
   */
  def removeFactor(factor: Factor): Unit

  /**
   * Introduce the specified [[Mutation]] inside the Environment
   * @param mutation
   *   the mutation to insert
   */
  def introduceMutation(mutation: Mutation): Unit
}

object Environment {

  type Mutations = List[Mutation]
  type Factors = List[Factor]

  /**
   * Method that generates an Environment from the previous one
   * @param environment
   *   the previous Environment
   */
  def fromPreviousOne(environment: Environment): Environment = apply(environment.climate, environment.factors)

  def apply(climate: Climate, factors: Factors): Environment = EnvironmentImpl(climate, factors)

  private case class EnvironmentImpl(
      override var climate: Climate,
      private var _factors: Factors,
      private var _mutations: Mutations = List()
  ) extends Environment {

    override def factors: Factors = _factors

    override def mutations: Mutations = _mutations

    override def introduceFactor(factor: Factor): Unit = _factors = _factors + factor

    override def removeFactor(factor: Factor): Unit = _factors = _factors - factor

    override def introduceMutation(mutation: Mutation): Unit = {
      KindsUtils.setAlleleDominanceFromMutation(mutation)
      _mutations = mutation :: _mutations
    }

  }

}

/** Climate Trait */
trait Climate

/** Summer Climate */
case object Summer extends Climate

/** Winter Climate */
case object Winter extends Climate
