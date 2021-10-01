package model.world

import engine.SimulationHistory.getActualGeneration
import model.genome.KindsUtils
import model.mutation.Mutation
import model.world.Environment.{ Factors, Mutations }
import model.world.Factor.{ FoodFactorImpl, Wolves }

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

    override def introduceFactor(factor: Factor): Unit = factor match {
      case FoodFactorImpl(isHighFood, isLimitedFood, isToughFood) if factors.exists(_.isInstanceOf[FoodFactor]) =>
        val previous: FoodFactor = factors.filter(_.isInstanceOf[FoodFactor]).head.asInstanceOf[FoodFactor]
        factors = factors.filter(!_.isInstanceOf[FoodFactor])
        factors = FoodFactorImpl(
          isHighFood || previous.isHighFood,
          isLimitedFood || previous.isLimitedFood,
          isToughFood || previous.isToughFood
        ) :: factors
      case _ => factors = factor :: factors
    }

    override def removeFactor(factor: Factor): Unit = factor match {
      case FoodFactorImpl(false, false, false) => factors = factors.filter(!_.isInstanceOf[FoodFactor])
      case FoodFactorImpl(isHighFood, isLimitedFood, isToughFood) =>
        val previous: FoodFactor = factors.filter(_.isInstanceOf[FoodFactor]).head.asInstanceOf[FoodFactor]
        factors = factors.filter(!_.isInstanceOf[FoodFactor])
        if (isHighFood) factors = FoodFactorImpl(
          isHighFood = false,
          isLimitedFood = previous.isLimitedFood,
          isToughFood = previous.isToughFood
        ) :: factors
        if (isToughFood)
          factors = FoodFactorImpl(previous.isHighFood, previous.isLimitedFood, isToughFood = false) :: factors
        if (isLimitedFood)
          factors = FoodFactorImpl(previous.isHighFood, isLimitedFood = false, previous.isToughFood) :: factors
      case _ => factors = factors.filter(_ != factor)
    }

    /** Introduce a new mutation */
    def introduceMutation(mutation: Mutation): Unit = {
      mutations = mutation :: mutations

      if (mutation.isDominant) KindsUtils.setAlleleDominance(mutation.geneKind.mutated)
      else KindsUtils.setAlleleDominance(mutation.geneKind.base)
    }

  }

}

/** Climate Trait */
trait Climate

/** Summer Climate */
case class Summer() extends Climate

/** Winter Climate */
case class Winter() extends Climate
