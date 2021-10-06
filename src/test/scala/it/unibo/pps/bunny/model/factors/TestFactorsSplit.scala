package it.unibo.pps.bunny.model.factors

import it.unibo.pps.bunny.model.InvalidFoodFactor
import it.unibo.pps.bunny.model.world.disturbingFactors.{
  FoodFactor, HighFoodFactor, HighToughFoodFactor, LimitedFoodFactor, LimitedHighFoodFactor, LimitedHighToughFoodFactor,
  LimitedToughFoodFactor, SingleFoodFactor, ToughFoodFactor
}
import org.scalatest.{ FlatSpec, Matchers }

class TestFactorsSplit extends FlatSpec with Matchers {

  "A HighFoodFactor" should "be split from a LimitedHighFoodFactor" in
    checkRemoveSubFactorOf[LimitedFoodFactor](LimitedHighFoodFactor(), HighFoodFactor())

  it should "be split from a HighToughFoodFactor" in
    checkRemoveSubFactorOf[LimitedFoodFactor](HighToughFoodFactor(), HighFoodFactor())

  it should "be split from a LimitedHighToughFoodFactor" in
    checkRemoveSubFactorOf[LimitedToughFoodFactor](LimitedHighToughFoodFactor(), HighFoodFactor())

  "A ToughFoodFactor" should "be split from aLimitedToughFoodFactor" in
    checkRemoveSubFactorOf[LimitedFoodFactor](LimitedToughFoodFactor(), ToughFoodFactor())

  it should "be split from a HighToughFoodFactor" in
    checkRemoveSubFactorOf[HighFoodFactor](HighToughFoodFactor(), ToughFoodFactor())

  it should "be split from a LimitedHighToughFoodFactor" in
    checkRemoveSubFactorOf[LimitedToughFoodFactor](LimitedHighToughFoodFactor(), ToughFoodFactor())

  "A LimitedFoodFactor" should "be split from a LimitedToughFoodFactor" in
    checkRemoveSubFactorOf[ToughFoodFactor](LimitedToughFoodFactor(), LimitedFoodFactor())

  it should "be split from a LimitedHighFoodFactor" in
    checkRemoveSubFactorOf[HighFoodFactor](LimitedHighFoodFactor(), LimitedFoodFactor())

  it should "be split from a LimitedHighToughFoodFactor" in
    checkRemoveSubFactorOf[LimitedHighToughFoodFactor](LimitedHighToughFoodFactor(), LimitedFoodFactor())

  private def checkRemoveSubFactorOf[T](firstFactor: FoodFactor, secondFactor: FoodFactor): Unit = {
    val resFactor = firstFactor - secondFactor
    assert(resFactor.isInstanceOf[T])
    shouldRaiseInvalidFoodFactor(firstFactor, firstFactor)
    if (secondFactor.isInstanceOf[SingleFoodFactor]) {
      shouldRaiseUnsupportedOperationException(secondFactor, firstFactor)
      shouldRaiseUnsupportedOperationException(secondFactor, secondFactor)
    } else {
      shouldRaiseInvalidFoodFactor(secondFactor, secondFactor)
      shouldRaiseInvalidFoodFactor(secondFactor, firstFactor)
    }

  }

  private def shouldRaiseUnsupportedOperationException(firstFactor: FoodFactor, secondFactor: FoodFactor): Unit =
    assertThrows[UnsupportedOperationException] {
      firstFactor - secondFactor
    }

  private def shouldRaiseInvalidFoodFactor(firstFactor: FoodFactor, secondFactor: FoodFactor): Unit =
    assertThrows[InvalidFoodFactor] {
      firstFactor - secondFactor
    }

}
