package model

import engine.SimulationHistory
import engine.SimulationHistory._
import model.world.Generation.Population
import model.world.{ Generation, Summer }
import org.scalatest.{ FlatSpec, Matchers }

class TestSimulation extends FlatSpec with Matchers {

  SimulationHistory.resetHistory()
  val firstGeneration: Generation = SimulationHistory.getActualGeneration
  val initialBunnies: Population = SimulationHistory.getActualPopulation

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

  it should "be not already ended" in {
    assert(!firstGeneration.isEnded)
  }

  it should "be the only Generation in the history" in {
    assert(SimulationHistory.history.size == 1)
  }

  "The next generation" should "have the same climate of the previous one" in {
    SimulationHistory.startNextGeneration()
    assert(SimulationHistory.getActualGeneration.environment.climate == Summer)
  }

  it should "have already no factors" in {
    assert(SimulationHistory.getActualGeneration.environment.factors.isEmpty)
  }

  it should "have the right number of alive bunnies" in {
    val previousBunniesNumber: Int = firstGeneration.population.size
    assert(SimulationHistory.getActualBunniesNumber == (previousBunniesNumber / 2 * 4 + previousBunniesNumber))
  }

  "At the fifth generation the initial couple of bunnies" should "be dead" in {
    for (_ <- 1 to 3) SimulationHistory.startNextGeneration()
    assert(getGenerationNumber == 4)
    assert(initialBunnies.forall(!_.alive))
  }

  "The fifth generation" should "be the only Generation in history that isn't already ended" in {
    SimulationHistory.history match {
      case h :: t => assert(!h.isEnded && t.forall(_.isEnded))
    }
  }

  it should "have only 1 alive bunny if the other ones are died" in {
    getActualPopulation.tail.foreach(_.alive = false)
    assert(firstGeneration.getAliveBunniesNumber == 1)

  }

  "The next generation" should " have just one alive bunny if in the previous one all the other one are dead" in {
    SimulationHistory.startNextGeneration()
    assert(SimulationHistory.history.head.getAliveBunniesNumber == 1)
  }

}
