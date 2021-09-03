package controller

import engine.SimulationEngine
import engine.SimulationEngine.simulationLoop
import engine.SimulationHistory.initialize
import model.world.Generation.{Environment, Population}

object Controller {

  def startSimulation(environment: Environment, population: Population): Unit = {
    initialize(environment, population)
    simulationLoop().unsafeRunAsyncAndForget()
  }

}
