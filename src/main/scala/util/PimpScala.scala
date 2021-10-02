package util

import model.world.Environment.Factors
import model.world.FactorsUtils.FactorTypes.FoodFactorKind
import model.world.{ Factor, FoodFactor }

object PimpScala {

  implicit class RichOption[A](option: Option[A]) {
    def -->(consumer: A => Unit): Unit = option.foreach(consumer)
    def ? : Boolean = option.isDefined
  }

  implicit class RichTuple2[A](tuple: (A, A)) {
    def toSeq: Seq[A] = Seq(tuple._1, tuple._2)
  }

  implicit class RichList[A](list: List[A]) {
    def -?(pred: A => Boolean): List[A] = list.filterNot(pred)
  }

  implicit class RichFactorList(factors: Factors) {
    def foodFactorIsPresent: Boolean = factors.exists(_.factorType == FoodFactorKind)
    def getFoodFactor: FoodFactor = factors.filter(_.factorType == FoodFactorKind).head.asInstanceOf[FoodFactor]

    def combineFoodFactor(newFoodFactor: FoodFactor): Factors =
      factors.getFoodFactor.combineWith(newFoodFactor) :: factors -? (_.factorType == FoodFactorKind)

    def updateFoodFactor(subFactor: FoodFactor): Factors =
      (factors.getFoodFactor removeSubFactor (subFactor)) :: factors -? (_.factorType == FoodFactorKind)

  }

}
