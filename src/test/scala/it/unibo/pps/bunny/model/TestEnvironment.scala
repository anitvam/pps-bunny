package it.unibo.pps.bunny.model

import it.unibo.pps.bunny.model.bunny.Mutation.dominantMutation
import it.unibo.pps.bunny.model.genome.Genes.FUR_COLOR
import it.unibo.pps.bunny.model.world.Environment.fromPreviousOne
import it.unibo.pps.bunny.model.world.disturbingFactors.PimpFactors._
import it.unibo.pps.bunny.model.world.disturbingFactors._
import it.unibo.pps.bunny.model.world.{ Environment, Summer, Winter }
import org.scalatest.{ FlatSpec, Matchers }

class TestEnvironment extends FlatSpec with Matchers {
  val env: Environment = Environment(Summer, List())
  val prevEnv: Environment = Environment(Winter, List(WolvesFactor()))
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
    prevEnv introduceFactor UnfriendlyClimateFactor()
    assert(prevEnv.factors.contains(UnfriendlyClimateFactor()))
    assert(prevEnv.factors.contains(WolvesFactor()))
  }

  it should "be able to introduce more than one FoodFactor" in {
    prevEnv introduceFactor LimitedFoodFactor()
    assert(prevEnv.factors.getFactor(FoodFactorKind).isDefined)
    assert(prevEnv.factors.contains(WolvesFactor()))
    assert(prevEnv.factors.contains(LimitedFoodFactor()))
    prevEnv introduceFactor HighFoodFactor()
    assertResult(Some(LimitedHighFoodFactor()))(prevEnv.factors.getFactor(FoodFactorKind))
  }

  it should "be able to remove a specific Factor" in {
    prevEnv removeFactor WolvesFactor()
    assert(!prevEnv.factors.contains(WolvesFactor()))
  }

  it should "be able to remove a SingleFoodFactor" in {
    prevEnv removeFactor LimitedFoodFactor()
    assert(prevEnv.factors.contains(HighFoodFactor()))
  }

  it should "be able to remove all the Factors" in {
    prevEnv removeFactor UnfriendlyClimateFactor()
    prevEnv removeFactor HighFoodFactor()
    prevEnv removeFactor WolvesFactor()
    assert(prevEnv.factors.isEmpty)
  }

  "A new Environment" should "have an empty list of Mutation" in {
    assert(env.mutations.isEmpty)
  }

  it should "be able to introduce a new Mutation" in {
    env introduceMutation dominantMutation(FUR_COLOR)
  }

}
