package it.unibo.pps.bunny.model.world.disturbingFactors

import it.unibo.pps.bunny.model.world.disturbingFactors._
import it.unibo.pps.bunny.util.PimpScala._

case class Factors(private var factors: List[Factor] = List()) {

  private def foodFactorIsPresent: Boolean = factors.exists(_.factorType == FoodFactorKind)

  private def getFoodFactor: FoodFactor = getFactor(FoodFactorKind).map(_.asInstanceOf[FoodFactor]).get

  private def combineFoodFactor(newFoodFactor: FoodFactor): List[Factor] =
    getFoodFactor + newFoodFactor :: factors -? (_.factorType == FoodFactorKind)

  private def updateFoodFactor(subFactor: FoodFactor): List[Factor] =
    (getFoodFactor - subFactor) :: factors -? (_.factorType == FoodFactorKind)

  def getFactor(factorKind: FactorKind): Option[Factor] = factors.find(_.factorType == factorKind)

  def add(factor: Factor): Unit = factor.factorType match {
    case FoodFactorKind if foodFactorIsPresent => factors = combineFoodFactor(factor.asInstanceOf[FoodFactor])
    case _                                     => factors = factor :: factors
  }

  def remove(factor: Factor): Unit = factor.factorType match {
    case FoodFactorKind if foodFactorIsPresent && getFoodFactor.isCombined =>
      factors = updateFoodFactor(factor.asInstanceOf[FoodFactor])
    case _ => factors = factors -? (_.factorType == factor.factorType)
  }

}
