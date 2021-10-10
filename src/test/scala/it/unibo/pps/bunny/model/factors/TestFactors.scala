package it.unibo.pps.bunny.model.factors

import it.unibo.pps.bunny.model.world.disturbingFactors._
import org.scalatest.{ FlatSpec, Matchers }

class TestFactors extends FlatSpec with Matchers {

  "A LimitedFoodFactor" should "have FoodFactor type" in assert(
    LimitedFoodFactor().factorType == FoodFactorKind
  )

  "A ToughFoodFactor" should "have FoodFactor type" in assert(
    ToughFoodFactor().factorType == FoodFactorKind
  )

  "A HighFoodFactor" should "have FoodFactor type" in assert(HighFoodFactor().factorType == FoodFactorKind)

  "A HighLimitedFoodFactor" should "have FoodFactor type" in assert(
    LimitedHighFoodFactor().factorType == FoodFactorKind
  )

  "A HighToughFoodFactor" should "have FoodFactor type" in assert(
    HighToughFoodFactor().factorType == FoodFactorKind
  )

  "A ToughLimitedFoodFactor" should "have FoodFactor type" in assert(
    LimitedToughFoodFactor().factorType == FoodFactorKind
  )

  "A LimitedToughHighFoodFactor" should "have FoodFactor type" in assert(
    LimitedHighToughFoodFactor().factorType == FoodFactorKind
  )

  "A WolvesFactor" should "have Wolves type" in assert(WolvesFactor().factorType == WolvesFactorKind)

  "A UnfriendlyClimateFactor" should "have UnfriendlyClimate type" in assert(
    UnfriendlyClimateFactor().factorType == UnfriendlyClimateFactorKind
  )

}
