package it.unibo.pps.bunny.model.factors

import it.unibo.pps.bunny.model.world.disturbingFactors._
import org.scalatest.{ FlatSpec, Matchers }

class TestFactors extends FlatSpec with Matchers {

  "A LimitedFoodFactor" should "have FoodFactor type" in assert(
    LimitedFoodFactor().factorKind == FoodFactorKind
  )

  "A ToughFoodFactor" should "have FoodFactor type" in assert(
    ToughFoodFactor().factorKind == FoodFactorKind
  )

  "A HighFoodFactor" should "have FoodFactor type" in assert(HighFoodFactor().factorKind == FoodFactorKind)

  "A HighLimitedFoodFactor" should "have FoodFactor type" in assert(
    LimitedHighFoodFactor().factorKind == FoodFactorKind
  )

  "A HighToughFoodFactor" should "have FoodFactor type" in assert(
    HighToughFoodFactor().factorKind == FoodFactorKind
  )

  "A ToughLimitedFoodFactor" should "have FoodFactor type" in assert(
    LimitedToughFoodFactor().factorKind == FoodFactorKind
  )

  "A LimitedToughHighFoodFactor" should "have FoodFactor type" in assert(
    LimitedHighToughFoodFactor().factorKind == FoodFactorKind
  )

  "A WolvesFactor" should "have Wolves type" in assert(WolvesFactor().factorKind == WolvesFactorKind)

  "A UnfriendlyClimateFactor" should "have UnfriendlyClimate type" in assert(
    UnfriendlyClimateFactor().factorKind == UnfriendlyClimateFactorKind
  )

}
