package controller

import engine.{SimulationEngine, SimulationHistory}
import engine.SimulationEngine.simulationLoop
import engine.SimulationHistory.initialize
import model.mutation.Mutation
import model.world.{Climate, Environment, Summer, Winter}
import model.world.Environment.Factors

object Controller {

  /** Method that starts the simulation
   * @param climate the Environment Climate
   * @param factors the Environment Factors*/
  def startSimulation(climate: Climate, factors: Factors): Unit = {
    SimulationHistory changeEnvironmentClimate climate
    simulationLoop().unsafeRunAsyncAndForget()
  }

  /** Method that sets the Summer Climate inside Environment */
  def setSummerClimate(): Unit = SimulationHistory changeEnvironmentClimate Summer()

  /** Method that sets the Winter Climate inside Environment */
  def setWinterClimate(): Unit = SimulationHistory changeEnvironmentClimate Winter()

  /** Method that insert a mutation inside the simulation
   * @param mutation the Mutation */
  def insertMutation(mutation: Mutation): Unit = SimulationHistory introduceMutation mutation

}