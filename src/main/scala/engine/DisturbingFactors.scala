package engine

import model.world.Factor
import model.world.Factor.{FoodFactorImpl, UnfriendlyClimate, Wolves}

/** Enumeration for the disturbing factors */
object DisturbingFactors extends Enumeration {
  val WOLF: Factor =  Wolves()
  val HIGH_FOOD: Factor = FoodFactorImpl(true, false, false)
  val TOUGH_FOOD: Factor = FoodFactorImpl(false, false, true)
  val LIMITED_FOOD: Factor = FoodFactorImpl(false, true, false)
  val HOSTILE_TEMPERATURE: Factor =  UnfriendlyClimate()
}
