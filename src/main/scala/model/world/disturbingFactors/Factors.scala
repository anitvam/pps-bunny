package model.world.disturbingFactors

import model.world.disturbingFactors.FactorTypes._
import util.PimpScala._

case class Factors(private var factors: List[Factor] = List()) {

  private def foodFactorIsPresent: Boolean = factors.exists(_.factorKind == FoodFactorKind)

  private def getFoodFactor: FoodFactor = getFactor(FoodFactorKind).map(_.asInstanceOf[FoodFactor]).get

  private def combineFoodFactor(newFoodFactor: FoodFactor): List[Factor] =
    getFoodFactor.combineWith(newFoodFactor) :: factors -? (_.factorKind == FoodFactorKind)

  private def updateFoodFactor(subFactor: FoodFactor): List[Factor] =
    (getFoodFactor removeSubFactor subFactor) :: factors -? (_.factorKind == FoodFactorKind)

  def getFactor(factorKind: FactorKind): Option[Factor] = factors.find(_.factorKind == factorKind)

  def add(factor: Factor): Unit = factor.factorKind match {
    case FoodFactorKind if foodFactorIsPresent => factors = combineFoodFactor(factor.asInstanceOf[FoodFactor])
    case _                                     => factors = factor :: factors
  }

  def remove(factor: Factor): Unit = factor.factorKind match {
    case FoodFactorKind if foodFactorIsPresent && getFoodFactor.isCombined =>
      factors = updateFoodFactor(factor.asInstanceOf[FoodFactor])
    case _ => factors = factors -? (_.factorKind == factor.factorKind)
  }

}
