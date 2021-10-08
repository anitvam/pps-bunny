package model.world.disturbingFactors

import model.world.Environment.Factors
import model.world.disturbingFactors.FactorTypes._
import util.PimpScala._

object PimpFactors {

  implicit class RichFactors(factors: Factors) {

    private def foodFactorIsPresent: Boolean = factors.exists(_.factorType == FoodFactorKind)

    private def getFoodFactor: FoodFactor = getFactor(FoodFactorKind).map(_.asInstanceOf[FoodFactor]).get

    private def combineFoodFactor(newFoodFactor: FoodFactor): Factors =
      getFoodFactor.combineWith(newFoodFactor) :: factors -? (_.factorType == FoodFactorKind)

    private def updateFoodFactor(subFactor: FoodFactor): Factors =
      (getFoodFactor removeSubFactor subFactor) :: factors -? (_.factorType == FoodFactorKind)

    def getFactor(factorKind: FactorKind): Option[Factor] = factors.find(_.factorType == factorKind)

    def +(factor: Factor): Factors = factor.factorType match {
      case FoodFactorKind if foodFactorIsPresent => combineFoodFactor(factor.asInstanceOf[FoodFactor])
      case _                                     => factor :: factors
    }

    def -(factor: Factor): Factors = factor.factorType match {
      case FoodFactorKind if foodFactorIsPresent && getFoodFactor.isCombined =>
        updateFoodFactor(factor.asInstanceOf[FoodFactor])
      case _ => factors -? (_.factorType == factor.factorType)
    }

  }

}
