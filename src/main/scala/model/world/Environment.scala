package model.world

import model.genome.KindsUtils
import model.mutation.Mutation
import model.world.Environment.{ Factors, Mutations }
import model.world.disturbingFactors.FactorTypes._
import model.world.disturbingFactors.{ Factor, FoodFactor }
import util.PimpScala._

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

  def introduceFactor(factor: Factor): Unit
  def removeFactor(factor: Factor): Unit
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

    override def introduceFactor(factor: Factor): Unit = factors = managedFactor(factor)(
      factors.foodFactorIsPresent,
      factors.combineFoodFactor(_),
      _ :: factors
    )

    override def removeFactor(factor: Factor): Unit = factors = managedFactor(factor)(
      factors.foodFactorIsPresent && factors.getFoodFactor.isCombined,
      factors.updateFoodFactor(_),
      f => factors -? (_.factorType == f.factorType)
    )

    private def managedFactor(factor: Factor)(
        foodFactorCondition: Boolean,
        foodFactorOp: FoodFactor => Factors,
        otherFactorOp: Factor => Factors
    ): Factors = factor.factorType match {
      case FoodFactorKind if foodFactorCondition => foodFactorOp(factor.asInstanceOf[FoodFactor])
      case _                                     => otherFactorOp(factor)
    }

    /** Introduce a new mutation */
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
