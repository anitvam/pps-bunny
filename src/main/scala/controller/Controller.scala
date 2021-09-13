package controller

import engine.SimulationEngine
import engine.SimulationEngine.simulationLoop
import engine.SimulationHistory.initialize
import model.world.Generation.{Environment, Population}

object Controller {

  def startSimulation(): Unit = {
    initialize("env")
    simulationLoop().unsafeRunAsyncAndForget()
  }

}
