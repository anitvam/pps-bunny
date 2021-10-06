package it.unibo.pps.bunny.model.factors

import it.unibo.pps.bunny.model.InvalidFoodFactor
import scala.reflect.{ classTag, ClassTag }
import it.unibo.pps.bunny.model.world.disturbingFactors.{
  FoodFactor, HighFoodFactor, LimitedFoodFactor, LimitedHighFoodFactor, ToughFoodFactor, _
}
import org.scalatest.{ FlatSpec, Matchers }

class TestFactorsComposition extends FlatSpec with Matchers {

  "A HighFoodFactor" should "be concatenated to a LimitedFoodFactor" in
    checkCombinationOf[LimitedHighFoodFactor](HighFoodFactor(), LimitedFoodFactor())

  it should "be concatenated to a LimitedToughFoodFactor" in
    checkCombinationOf[LimitedHighToughFoodFactor](HighFoodFactor(), LimitedToughFoodFactor())

  "A ToughFoodFactor" should "be concatenated to a LimitedFoodFactor" in
    checkCombinationOf[LimitedToughFoodFactor](ToughFoodFactor(), LimitedFoodFactor())

  it should "be concatenated to a HighFoodFactor" in
    checkCombinationOf[HighToughFoodFactor](ToughFoodFactor(), HighFoodFactor())

  it should "be concatenated to a LimitedHighFoodFactor" in
    checkCombinationOf[LimitedHighToughFoodFactor](ToughFoodFactor(), LimitedHighFoodFactor())

  "A LimitedFoodFactor" should "be concatenated to a HighToughFoodFactor" in
    checkCombinationOf[LimitedHighToughFoodFactor](LimitedFoodFactor(), HighToughFoodFactor())

  "A LimitedFoodFactor" should "not be concatenated to a LimitedFoodFactor" in
    assertThrows[InvalidFoodFactor] {
      LimitedFoodFactor() + LimitedFoodFactor()
    }

  "A ToughFoodFactor" should "not be concatenated to a ToughFoodFactor" in
    assertThrows[InvalidFoodFactor] {
      ToughFoodFactor() + ToughFoodFactor()
    }

  "A HighFoodFactor" should "not be concatenated to a HighFoodFactor" in
    assertThrows[InvalidFoodFactor] {
      HighFoodFactor() + HighFoodFactor()
    }

  private def checkCombinationOf[T](firstFactor: FoodFactor, secondFactor: FoodFactor): Unit = {
    val combined = firstFactor + secondFactor
    val combinedReverse = secondFactor + firstFactor

    assert(combined.isInstanceOf[T])
    assert(combinedReverse.isInstanceOf[T])
    assertThrows[InvalidFoodFactor] {
      firstFactor + LimitedHighToughFoodFactor()
    }
    assertThrows[InvalidFoodFactor] {
      secondFactor + LimitedHighToughFoodFactor()
    }
    assertThrows[UnsupportedOperationException] {
      LimitedHighToughFoodFactor() + firstFactor
    }
    assertThrows[UnsupportedOperationException] {
      LimitedHighToughFoodFactor() + secondFactor
    }
  }

}
