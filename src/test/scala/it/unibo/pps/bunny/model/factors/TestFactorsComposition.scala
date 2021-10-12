package it.unibo.pps.bunny.model.factors

import it.unibo.pps.bunny.model.InvalidFoodFactor
import it.unibo.pps.bunny.model.world.disturbingFactors._
import org.scalatest.{FlatSpec, Matchers}

class TestFactorsComposition extends FlatSpec with Matchers {

  "A HighFoodFactor" should "be concatenated to a LimitedFoodFactor" in {
    val res = checkCombinationOf(HighFoodFactor(), LimitedFoodFactor())
    assert(res._1.isInstanceOf[LimitedHighFoodFactor])
    assert(res._2.isInstanceOf[LimitedHighFoodFactor])
  }

  it should "be concatenated to a LimitedToughFoodFactor" in {
    val res = checkCombinationOf(HighFoodFactor(), LimitedToughFoodFactor())
    assert(res._1.isInstanceOf[LimitedHighToughFoodFactor])
    assert(res._2.isInstanceOf[LimitedHighToughFoodFactor])
  }

  "A ToughFoodFactor" should "be concatenated to a LimitedFoodFactor" in {
    val res = checkCombinationOf(ToughFoodFactor(), LimitedFoodFactor())
    assert(res._1.isInstanceOf[LimitedToughFoodFactor])
    assert(res._2.isInstanceOf[LimitedToughFoodFactor])
  }

  it should "be concatenated to a HighFoodFactor" in {
    val res = checkCombinationOf(ToughFoodFactor(), HighFoodFactor())
    assert(res._1.isInstanceOf[HighToughFoodFactor])
    assert(res._2.isInstanceOf[HighToughFoodFactor])
  }

  it should "be concatenated to a LimitedHighFoodFactor" in {
    val res = checkCombinationOf(ToughFoodFactor(), LimitedHighFoodFactor())
    assert(res._1.isInstanceOf[LimitedHighToughFoodFactor])
    assert(res._2.isInstanceOf[LimitedHighToughFoodFactor])
  }

  "A LimitedFoodFactor" should "be concatenated to a HighToughFoodFactor" in {
    val res = checkCombinationOf(LimitedFoodFactor(), HighToughFoodFactor())
    assert(res._1.isInstanceOf[LimitedHighToughFoodFactor])
    assert(res._2.isInstanceOf[LimitedHighToughFoodFactor])
  }

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

  private def checkCombinationOf(firstFactor: FoodFactor, secondFactor: FoodFactor): (FoodFactor, FoodFactor) = {

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

    println(firstFactor)
    println(secondFactor)
    (firstFactor + secondFactor, secondFactor + firstFactor)
  }

}
