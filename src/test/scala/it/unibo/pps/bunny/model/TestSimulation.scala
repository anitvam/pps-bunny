package it.unibo.pps.bunny.model

import it.unibo.pps.bunny.engine.SimulationHistory
import it.unibo.pps.bunny.engine.SimulationHistory.{ getActualGeneration, getActualPopulation }
import it.unibo.pps.bunny.model.Bunny.generateRandomFirstBunny
import it.unibo.pps.bunny.model.world.Reproduction.{ generateInitialCouple, nextGenerationBunnies }
import it.unibo.pps.bunny.model.world.{ Environment, Generation, Summer }
import org.scalatest.{ FlatSpec, Matchers }

class TestSimulation extends FlatSpec with Matchers {

  "The first generation" should "have an initial population of 2 bunnies" in {
    assert(SimulationHistory.history.last.population.size == 2)
  }

  it should "have also an initial living population of 2 bunnies" in {
    assert(SimulationHistory.history.last.getAliveBunniesNumber == 2)
  }

  it should "have only 1 alive bunny if the other one is died" in {
    SimulationHistory.history.last.population.head.alive = false
    assert(SimulationHistory.history.last.getAliveBunniesNumber == 1)
  }

  "If there is only one alive bunny the population for the next generation" should "be only the alive it.unibo.pps.bunny" in {
    SimulationHistory.startNextGeneration()
    assert(SimulationHistory.history.head.getAliveBunniesNumber == 1)
  }

}
