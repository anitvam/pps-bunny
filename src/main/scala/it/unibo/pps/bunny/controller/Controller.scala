package it.unibo.pps.bunny.controller

import it.unibo.pps.bunny.engine.{ SimulationEndType, SimulationEngine, SimulationHistory }
import it.unibo.pps.bunny.engine.SimulationEngine.{ resetEngine, simulationLoop }
import it.unibo.pps.bunny.engine.SimulationHistory.resetHistory
import it.unibo.pps.bunny.model.genome.Genes.GeneKind
import it.unibo.pps.bunny.model.genome.KindsUtils.resetDominance
import it.unibo.pps.bunny.model.mutation.Mutation
import it.unibo.pps.bunny.model.mutation.Mutation._
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.disturbingFactors.Factor
import it.unibo.pps.bunny.model.world.{ Summer, Winter }
import scalafx.application.Platform
import it.unibo.pps.bunny.view.scalaFX.ScalaFXView

object Controller {

  /** Method that starts the simulation */
  def startSimulation(): Unit = simulationLoop().unsafeRunAsyncAndForget()

  /** Method that change the Simulation Speed */
  def changeSimulationSpeed(): Unit = SimulationEngine.changeSpeed()

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

  /**
   * Method that insert a recessive mutation inside the simulation
   * @param geneKind
   *   the gene mutated
   */
  def insertRecessiveMutationFor(geneKind: GeneKind): Unit = insertMutation(recessiveMutation(geneKind))

  /**
   * Method that insert a dominant mutation inside the simulation
   * @param geneKind
   *   the gene mutated
   */
  def insertDominantMutationFor(geneKind: GeneKind): Unit = insertMutation(dominantMutation(geneKind))

  /**
   * Method that shows the end of the simulation on the Application GUI
   * @param endType
   *   the reason why simulation ended
   */
  def showEnd(endType: SimulationEndType): Unit = Platform runLater {
    ScalaFXView.showEnd(endType)
  }

  /** Resets the simulation model to its initial state */
  def reset(): Unit = {
    resetDominance()
    resetHistory()
    resetEngine()
  }

  /** @return the population of the actual generation, it contains also dead bunnies */
  def population: Population = SimulationHistory.getActualGeneration.population

  /**
   * Method to introduce a new [[Factor]] in the environment of the actual generation
   * @param factor
   *   the new factor added by the user
   */
  def introduceFactor(factor: Factor): Unit = SimulationHistory.getActualGeneration.environment introduceFactor factor

  /**
   * Method to remove a [[Factor]] from the environment of the actual generation
   * @param factor
   *   the new factor added by the user
   */
  def removeFactor(factor: Factor): Unit = SimulationHistory.getActualGeneration.environment removeFactor factor

  /** @return the current simulation speed */
  def getCurrentSimulationSpeed: Double = SimulationEngine.simulationSpeed
}
