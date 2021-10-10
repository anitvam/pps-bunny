package it.unibo.pps.bunny.model.world.disturbingFactors

import it.unibo.pps.bunny.model.world.Environment.Factors
import it.unibo.pps.bunny.util.PimpScala.RichList
import scala.language.postfixOps
import scala.language.implicitConversions

object PimpFactors {

  /** A rich wrapper of [[Factors]] to add specific methods */
  implicit class RichFactors(factors: Factors) {

    /** @return if in the factors list there is a [[FoodFactor]] */
    private def foodFactorIsPresent: Boolean = factors.exists(_.factorKind == FoodFactorKind)

    /** Getter for the [[FoodFactor]] in the list */
    private def getFoodFactor: FoodFactor = getFactor(FoodFactorKind).map(_.asInstanceOf[FoodFactor]).get

    /**
     * Combines the [[FoodFactor]] already in the list with the new one
     * @param newFoodFactor
     *   the new foodFactor added in the environment
     */
    private def combineFoodFactor(newFoodFactor: FoodFactor): Factors =
      getFoodFactor + newFoodFactor :: factors -? (_.factorKind == FoodFactorKind)

    /**
     * Method that removes a [[FoodFactor]] from [[Factors]]
     * @param subFactor
     *   the foodFactor removed from the environment
     */
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
