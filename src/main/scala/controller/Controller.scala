package controller

import engine.{SimulationEngine, SimulationHistory}
import engine.SimulationEngine.simulationLoop
import engine.SimulationHistory.initialize
import model.mutation.Mutation
import model.world.{Climate, Environment, Summer, Winter}
import model.world.Environment.Factors

object Controller {

  def startSimulation(climate: Climate, factors: Factors): Unit = {
    SimulationHistory changeEnvironmentClimate climate
    simulationLoop().unsafeRunAsyncAndForget()
  }

  def setSummerClimate(): Unit = SimulationHistory changeEnvironmentClimate Summer()

  def setWinterClimate(): Unit = SimulationHistory changeEnvironmentClimate Winter()

  def insertMutation(mutation: Mutation): Unit = SimulationHistory.introduceMutation(mutation)

}
