package it.unibo.pps.bunny.model.factors

import it.unibo.pps.bunny.model.world.disturbingFactors.{
  FactorTypes, HighFoodFactor, HighToughFoodFactor, LimitedFoodFactor, LimitedHighFoodFactor,
  LimitedHighToughFoodFactor, LimitedToughFoodFactor, ToughFoodFactor, UnfriendlyClimateFactor, WolvesFactor
}
import org.scalatest.{ FlatSpec, Matchers }

class TestFactors extends FlatSpec with Matchers {

  "A LimitedFoodFactor" should "have FoodFactor type" in assert(
    LimitedFoodFactor().factorType == FactorTypes.FoodFactorKind
  )

  "A ToughFoodFactor" should "have FoodFactor type" in assert(
    ToughFoodFactor().factorType == FactorTypes.FoodFactorKind
  )

  "A HighFoodFactor" should "have FoodFactor type" in assert(HighFoodFactor().factorType == FactorTypes.FoodFactorKind)

  "A HighLimitedFoodFactor" should "have FoodFactor type" in assert(
    LimitedHighFoodFactor().factorType == FactorTypes.FoodFactorKind
  )

  "A HighToughFoodFactor" should "have FoodFactor type" in assert(
    HighToughFoodFactor().factorType == FactorTypes.FoodFactorKind
  )

  "A ToughLimitedFoodFactor" should "have FoodFactor type" in assert(
    LimitedToughFoodFactor().factorType == FactorTypes.FoodFactorKind
  )

  "A LimitedToughHighFoodFactor" should "have FoodFactor type" in assert(
    LimitedHighToughFoodFactor().factorType == FactorTypes.FoodFactorKind
  )

  "A WolvesFactor" should "have Wolves type" in assert(WolvesFactor().factorType == FactorTypes.WolvesFactorKind)

  "A UnfriendlyClimateFactor" should "have UnfriendlyClimate type" in assert(
    UnfriendlyClimateFactor().factorType == FactorTypes.UnfriendlyClimateFactorKind
  )

}
