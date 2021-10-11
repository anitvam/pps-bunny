package it.unibo.pps.bunny.model.world.disturbingFactors

import it.unibo.pps.bunny.model.world.Environment.Factors
import it.unibo.pps.bunny.util.PimpScala.RichList
import scala.language.postfixOps
import scala.language.implicitConversions

object PimpFactors {

  /** Implicit class that enrich [[Factors]] */
  implicit class RichFactors(factors: Factors) {

    /** @return true if there is a [[FoodFactor]] inside the simulation [[Factors]] */
    private def foodFactorIsPresent: Boolean = factors.exists(_.factorKind == FoodFactorKind)

    /** @return the [[FoodFactor]] instance inside the simulation [[Factors]] */
    private def getFoodFactor: FoodFactor = getFactor(FoodFactorKind).map(_.asInstanceOf[FoodFactor]).get

    /**
     * Method that inserts a new [[FoodFactor]] inside [[Factors]]
     * @param newFoodFactor
     *   the [[FoodFactor]] that will be inserted
     * @return
     *   the updated [[Factors]]
     */
    private def combineFoodFactor(newFoodFactor: FoodFactor): Factors =
      getFoodFactor + newFoodFactor :: factors -? (_.factorKind == FoodFactorKind)

    /** Method that removes a [[FoodFactor]] from [[Factors]] */
    private def updateFoodFactor(subFactor: FoodFactor): Factors =
      getFoodFactor - subFactor :: factors -? (_.factorKind == FoodFactorKind)

    /**
     * Method that return the instance of the specified [[FactorKind]]
     * @param factorKind
     *   the specified [[FactorKind]]
     * @return
     *   an [[Option]] containing the instance
     */
    def getFactor(factorKind: FactorKind): Option[Factor] = factors.find(_.factorKind == factorKind)

    /**
     * Adds a new [[Factor]] inside the environment [[Factors]]
     * @param factor
     *   the specified [[Factor]]
     * @return
     *   the updated [[Factors]]
     */
    def +(factor: Factor): Factors = factor.factorKind match {
      case FoodFactorKind if foodFactorIsPresent => combineFoodFactor(factor.asInstanceOf[FoodFactor])
      case _                                     => factor :: factors
    }

    /**
     * Removes a [[Factor]] from the environment [[Factors]]
     * @param factor
     *   the specified [[Factor]]
     * @return
     *   the updated [[Factors]]
     */
    def -(factor: Factor): Factors = factor.factorKind match {
      case FoodFactorKind if foodFactorIsPresent && getFoodFactor.isCombined =>
        updateFoodFactor(factor.asInstanceOf[FoodFactor])
      case _ => factors -? (_.factorKind == factor.factorKind)
    }

  }

}
