package it.unibo.pps.bunny.model.factors

import it.unibo.pps.bunny.model.InvalidFoodFactor
import it.unibo.pps.bunny.model.world.disturbingFactors.{
  FoodFactor, HighFoodFactor, HighToughFoodFactor, LimitedFoodFactor, LimitedHighFoodFactor, LimitedHighToughFoodFactor,
  LimitedToughFoodFactor, SingleFoodFactor, ToughFoodFactor
}
import org.scalatest.{ FlatSpec, Matchers }

class TestFactorsSplit extends FlatSpec with Matchers {

  "A HighFoodFactor" should "be split from a LimitedHighFoodFactor" in {
    val res = checkRemoveSubFactorOf(LimitedHighFoodFactor(), HighFoodFactor())
    assert(res.isInstanceOf[LimitedFoodFactor])

  }

  it should "be split from a HighToughFoodFactor" in {
    val res = checkRemoveSubFactorOf(HighToughFoodFactor(), HighFoodFactor())
    assert(res.isInstanceOf[ToughFoodFactor])

  }

  it should "be split from a LimitedHighToughFoodFactor" in {
    val res = checkRemoveSubFactorOf(LimitedHighToughFoodFactor(), HighFoodFactor())
    assert(res.isInstanceOf[LimitedToughFoodFactor])
  }

  "A ToughFoodFactor" should "be split from aLimitedToughFoodFactor" in {
    val res = checkRemoveSubFactorOf(LimitedToughFoodFactor(), ToughFoodFactor())
    assert(res.isInstanceOf[LimitedFoodFactor])
  }

  it should "be split from a HighToughFoodFactor" in {
    val res = checkRemoveSubFactorOf(HighToughFoodFactor(), ToughFoodFactor())
    assert(res.isInstanceOf[HighFoodFactor])
  }

  it should "be split from a LimitedHighToughFoodFactor" in {
    val res = checkRemoveSubFactorOf(LimitedHighToughFoodFactor(), ToughFoodFactor())
    assert(res.isInstanceOf[LimitedHighFoodFactor])
  }

  "A LimitedFoodFactor" should "be split from a LimitedToughFoodFactor" in {
    val res = checkRemoveSubFactorOf(LimitedToughFoodFactor(), LimitedFoodFactor())
    assert(res.isInstanceOf[ToughFoodFactor])
  }

  it should "be split from a LimitedHighFoodFactor" in {
    val res = checkRemoveSubFactorOf(LimitedHighFoodFactor(), LimitedFoodFactor())
    assert(res.isInstanceOf[HighFoodFactor])
  }

  it should "be split from a LimitedHighToughFoodFactor" in {
    val res = checkRemoveSubFactorOf(LimitedHighToughFoodFactor(), LimitedFoodFactor())
    assert(res.isInstanceOf[HighToughFoodFactor])
  }

  private def checkRemoveSubFactorOf(firstFactor: FoodFactor, secondFactor: FoodFactor): FoodFactor = {

    shouldRaiseInvalidFoodFactor(firstFactor, firstFactor)
    if (secondFactor.isInstanceOf[SingleFoodFactor]) {
      shouldRaiseUnsupportedOperationException(secondFactor, firstFactor)
      shouldRaiseUnsupportedOperationException(secondFactor, secondFactor)
    } else {
      shouldRaiseInvalidFoodFactor(secondFactor, secondFactor)
      shouldRaiseInvalidFoodFactor(secondFactor, firstFactor)
    }
    firstFactor - secondFactor
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
