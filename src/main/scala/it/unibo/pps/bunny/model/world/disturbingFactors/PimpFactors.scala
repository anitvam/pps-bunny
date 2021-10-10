package it.unibo.pps.bunny.model.world.disturbingFactors

import it.unibo.pps.bunny.model.world.Environment.Factors
import it.unibo.pps.bunny.util.PimpScala.RichList

object PimpFactors {

  implicit class RichFactors(factors: Factors) {

    private def foodFactorIsPresent: Boolean = factors.exists(_.factorType == FoodFactorKind)

    private def getFoodFactor: FoodFactor = getFactor(FoodFactorKind).map(_.asInstanceOf[FoodFactor]).get

    private def combineFoodFactor(newFoodFactor: FoodFactor): Factors =
      getFoodFactor + newFoodFactor :: factors -? (_.factorType == FoodFactorKind)

    private def updateFoodFactor(subFactor: FoodFactor): Factors =
      getFoodFactor - subFactor :: factors -? (_.factorType == FoodFactorKind)

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
