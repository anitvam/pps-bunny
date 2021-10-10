package it.unibo.pps.bunny.model

import it.unibo.pps.bunny.model.world.Environment.fromPreviousOne
import it.unibo.pps.bunny.model.world._
import org.scalatest.{ FlatSpec, Matchers }

class TestEnvironment extends FlatSpec with Matchers {
  val env: Environment = Environment(Summer, List())
  val prevEnv: Environment = Environment(Winter, List(Wolves()))
  val newEnv: Environment = fromPreviousOne(prevEnv)

  "An environment" should "be created by a climate and a empty list of factors" in {
    assertResult(Summer)(env.climate)
    assertResult(List())(env.factors)
  }

  "An environment" should "be created by a previous one and have the same climate and factors" in {
    assertResult(prevEnv.climate)(newEnv.climate)
    assertResult(prevEnv.factors)(newEnv.factors)
  }

  it should "be able to change the climate" in {
    prevEnv.climate = Summer
    assert(prevEnv.climate != Winter)
    assertResult(Summer)(prevEnv.climate)
  }

  it should "be able to introduce a new factor" in {
    prevEnv introduceFactor UnfriendlyClimate()
    assert(prevEnv.factors.contains(UnfriendlyClimate()))
    assert(prevEnv.factors.contains(Wolves()))
  }

  it should "be able to introduce more than one FoodFactor" in {
    prevEnv introduceFactor LimitedFoodFactor()
    assert(prevEnv.factors.getFactor(FoodFactorKind).isDefined)
    assert(prevEnv.factors.contains(Wolves()))
    assert(prevEnv.factors.contains(LimitedFoodFactor()))
    prevEnv introduceFactor HighFoodFactor()
    assertResult(Some(LimitedHighFoodFactor()))(prevEnv.factors.getFactor(FoodFactorKind))
  }

  it should "be able to remove a specific Factor" in {
    prevEnv removeFactor Wolves()
    assert(!prevEnv.factors.contains(Wolves()))
  }

  it should "be able to remove a SingleFoodFactor" in {
    prevEnv removeFactor LimitedFoodFactor()
    assert(prevEnv.factors.contains(HighFoodFactor()))
  }

  it should "be able to remove all the Factors" in {
    prevEnv removeFactor UnfriendlyClimate()
    prevEnv removeFactor HighFoodFactor()
    prevEnv removeFactor Wolves()
    assert(prevEnv.factors.isEmpty)
  }

//  "A new Environment" should "have an empty list of Mutation" in {
//    assert(env.mutations.isEmpty)
//  }
//
//  it should "be able to introduce a new Mutation" in {
//    env introduceMutation Mutation(Genes.FUR_COLOR, true)
//  }

}
