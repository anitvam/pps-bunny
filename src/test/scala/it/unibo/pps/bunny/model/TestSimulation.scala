package it.unibo.pps.bunny.model

import it.unibo.pps.bunny.engine.{ SimulationConstants, SimulationHistory }
import it.unibo.pps.bunny.engine.SimulationHistory._
import it.unibo.pps.bunny.model.TestUtils.goToGenerationNumber
import it.unibo.pps.bunny.model.world.{ Generation, Summer }
import org.scalatest.{ FlatSpec, Matchers }

class TestSimulation extends FlatSpec with Matchers {

  SimulationHistory.resetHistory()
  val firstGeneration: Generation = SimulationHistory.getActualGeneration

  "The first generation" should "be the generation zero" in {
    assert(SimulationHistory.getGenerationNumber == 0)
  }

  it should "have an initial population of 2 bunnies" in {
    assert(firstGeneration.population.size == 2)
  }

  it should "have also an initial living population of 2 bunnies" in {
    assert(getActualPopulation.size == 2)
  }

  it should "have the Summer Climate" in {
    assert(firstGeneration.environment.climate == Summer)
  }

  it should "not have any Factor activated" in {
    assert(firstGeneration.environment.factors.isEmpty)
  }

  it should "be not already is over" in {
    assert(!firstGeneration.isOver)
  }

  it should "be the only Generation in the history" in {
    assert(SimulationHistory.history.size == 1)
  }

  "The second generation" should "have the same climate of the previous one" in {
    goToGenerationNumber(1)
    assert(getActualGeneration.environment.climate == Summer)
  }

  it should "have already no factors" in {
    goToGenerationNumber(1)
    assert(getActualGeneration.environment.factors.isEmpty)
  }

  it should "have the right number of alive bunnies" in {
    goToGenerationNumber(1)
    assert(getActualGeneration.getAliveBunniesNumber == 6)
  }

  "At the fifth generation the initial couple of bunnies" should "be dead" in {
    goToGenerationNumber(4)
    assert(getGenerationNumber == 4)
    assert(SimulationHistory.history.last.population.forall(!_.alive))
  }

  "The actual generation" should "be the only Generation in history that isn't already is over" in {
    SimulationHistory.history match {
      case h :: t => assert(!h.isOver && t.forall(_.isOver))
      case _      => fail()
    }
  }

  "The seventh generation" should "be overpopulated " in {
    goToGenerationNumber(6)
    assert(getGenerationNumber == 6)
    assert(worldIsOverpopulated)
    assert(getActualBunniesNumber >= SimulationConstants.MAX_ALIVE_BUNNIES)
  }

  it should "not have a next generation" in {
    goToGenerationNumber(6)
    assert(!existNextGeneration)
  }

  it should "only have one alive bunny if the others are killed" in {
    goToGenerationNumber(6)
    getActualPopulation.tail.foreach(_.kill())
    assert(getActualBunniesNumber == 1)
  }

  it should "no longer be overpopulated" in {
    goToGenerationNumber(6)
    getActualPopulation.tail.foreach(_.kill())
    assert(!worldIsOverpopulated)
  }

  it should "have a next generation now" in {
    goToGenerationNumber(6)
    getActualPopulation.tail.foreach(_.kill())
    assert(existNextGeneration)
  }

  "The next generation" should " have just one alive bunny if in the previous one all the other one are dead" in {
    goToGenerationNumber(6)
    getActualPopulation.tail.foreach(_.kill())
    SimulationHistory.startNextGeneration()
    assert(SimulationHistory.history.head.getAliveBunniesNumber == 1)
  }

  it should "have a next generation" in {
    goToGenerationNumber(6)
    getActualPopulation.tail.foreach(_.kill())
    SimulationHistory.startNextGeneration()
    assert(existNextGeneration)
  }

  "A generation" should "not have a next one if the bunnies have become extinct" in {
    getActualPopulation.foreach(_.kill())
    assert(bunniesAreExtinct)
    assert(!existNextGeneration)
  }

}

object TestUtils {

  def goToGenerationNumber(n: Int): Unit = {
    SimulationHistory.resetHistory()
    for (_ <- 1 to n) SimulationHistory.startNextGeneration()
  }

}
