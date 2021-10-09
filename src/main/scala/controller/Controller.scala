package controller

import engine.SimulationEndType
import engine.SimulationEngine.{ resetEngine, simulationLoop }
import engine.SimulationHistory.resetHistory
import engine.{ SimulationEngine, SimulationHistory }
import model.genome.KindsUtils.resetDominance
import model.world.Generation.Population
import model.world.disturbingFactors.Factor
import model.world.{ Mutation, Summer, Winter }
import scalafx.application.Platform
import view.scalaFX.ScalaFXView

object Controller {

  /** Method that starts the simulation */
  def startSimulation(): Unit = simulationLoop().unsafeRunAsyncAndForget()

  def incrementSimulationSpeed(): Unit = SimulationEngine.incrementSpeed()

  /** Method that sets the Summer Climate inside Environment */
  def setSummerClimate(): Unit = SimulationHistory.getActualGeneration.environment.climate = Summer

  /** Method that sets the Winter Climate inside Environment */
  def setWinterClimate(): Unit = SimulationHistory.getActualGeneration.environment.climate = Winter

  /**
   * Method that insert a mutation inside the simulation
   * @param mutation
   *   the Mutation to insert
   */
  def insertMutation(mutation: Mutation): Unit =
    SimulationHistory.getActualGeneration.environment introduceMutation mutation

  /** Method that shows the end of the simulation on the Application GUI */
  def showEnd(endType: SimulationEndType): Unit = Platform runLater {
    ScalaFXView.showEnd(endType)
  }

  /** Resets the simulation model to its initial state */
  def reset(): Unit = {
    resetDominance()
    resetHistory()
    resetEngine()
  }

  def population: Population = SimulationHistory.getActualGeneration.population

  def introduceFactor(factor: Factor): Unit = SimulationHistory.getActualGeneration.environment introduceFactor factor

  def removeFactor(factor: Factor): Unit = SimulationHistory.getActualGeneration.environment removeFactor factor

  def getCurrentSimulationSpeed: Double = SimulationEngine.simulationSpeed
}
