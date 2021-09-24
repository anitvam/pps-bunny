package controller

import engine.SimulationEngine.simulationLoop
import engine.SimulationHistory
import engine.SimulationHistory.resetHistory
import model.genome.KindsUtils.resetDominance
import model.mutation.Mutation
import model.world.Environment.Factors
import model.world.Generation.Population
import model.world.{Climate, Summer, Winter}
import scalafx.application.Platform
import view.scalaFX.ScalaFXView

object Controller {

  def resetSimulation(): Unit = {
    resetDominance()
    resetHistory()
  }

  /**
   * Method that starts the simulation
   * @param climate
   *   the Environment Climate
   * @param factors
   *   the Environment Factors
   */
  def startSimulation(climate: Climate, factors: Factors): Unit = {
    SimulationHistory changeEnvironmentClimate climate
    simulationLoop().unsafeRunAsyncAndForget()
  }

  /** Method that sets the Summer Climate inside Environment */
  def setSummerClimate(): Unit = SimulationHistory changeEnvironmentClimate Summer()

  /** Method that sets the Winter Climate inside Environment */
  def setWinterClimate(): Unit = SimulationHistory changeEnvironmentClimate Winter()

  /**
   * Method that insert a mutation inside the simulation
   * @param mutation
   *   the Mutation
   */
  def insertMutation(mutation: Mutation): Unit = SimulationHistory introduceMutation mutation

  /** Method that shows the end of the simulation on the Application GUI */
  def showEnd(): Unit = Platform runLater { ScalaFXView.showEnd() }

  def population: Population = SimulationHistory.getActualPopulation

}
