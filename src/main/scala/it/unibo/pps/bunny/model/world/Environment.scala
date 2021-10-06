package it.unibo.pps.bunny.model.world

import it.unibo.pps.bunny.model.genome.KindsUtils
import it.unibo.pps.bunny.model.mutation.Mutation
import it.unibo.pps.bunny.model.world.Environment.Mutations
import it.unibo.pps.bunny.model.world.disturbingFactors.FactorTypes._
import it.unibo.pps.bunny.model.world.disturbingFactors.{ Factor, Factors, FoodFactor }
import it.unibo.pps.bunny.util.PimpScala._

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

    override def introduceFactor(factor: Factor): Unit = factors add factor

    override def removeFactor(factor: Factor): Unit = factors remove factor

    def introduceMutation(mutation: Mutation): Unit = {
      if (mutation.isDominant) KindsUtils.setAlleleDominance(mutation.geneKind.mutated)
      else KindsUtils.setAlleleDominance(mutation.geneKind.base)
      mutations = mutation :: mutations
    }

  }

}

/** Climate Trait */
trait Climate

/** Summer Climate */
case class Summer() extends Climate

/** Winter Climate */
case class Winter() extends Climate
