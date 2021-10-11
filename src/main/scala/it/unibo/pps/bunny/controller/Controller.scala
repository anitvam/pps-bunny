package it.unibo.pps.bunny.controller

import it.unibo.pps.bunny.engine.{ SimulationEndType, SimulationEngine, SimulationHistory }
import it.unibo.pps.bunny.engine.SimulationEngine.{ resetEngine, simulationLoop }
import it.unibo.pps.bunny.engine.SimulationHistory.resetHistory
import it.unibo.pps.bunny.model.bunny.Mutation
import it.unibo.pps.bunny.model.bunny.Mutation.{ dominantMutation, recessiveMutation }
import it.unibo.pps.bunny.model.genome.Genes.GeneKind
import it.unibo.pps.bunny.model.genome.KindsUtils.resetDominance
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.disturbingFactors.Factor
import it.unibo.pps.bunny.model.world.{ Summer, Winter }
import scalafx.application.Platform
import it.unibo.pps.bunny.view.scalaFX.ScalaFXView

object Controller {

  /** Method that starts the simulation */
  def startSimulation(): Unit = simulationLoop().unsafeRunAsyncAndForget()

  /** Method that changes the Simulation Speed */
  def changeSimulationSpeed(): Unit = SimulationEngine.changeSpeed()

  /** Method that sets the Summer Climate inside Environment */
  def setSummerClimate(): Unit = SimulationHistory.getActualGeneration.environment.climate = Summer

  /** Method that sets the Winter Climate inside Environment */
  def setWinterClimate(): Unit = SimulationHistory.getActualGeneration.environment.climate = Winter

  /**
   * Method that inserts a [[Mutation]] inside the simulation
   * @param mutation
   *   the Mutation to insert
   */
  def insertMutation(mutation: Mutation): Unit =
    SimulationHistory.getActualGeneration.environment introduceMutation mutation

  /**
   * Method that insert a recessive mutation inside the simulation for the given [[GeneKind]]
   * @param geneKind
   *   the recessive gene of the mutation
   */
  def insertRecessiveMutationFor(geneKind: GeneKind): Unit = insertMutation(recessiveMutation(geneKind))

  /**
   * Method that insert a dominant mutation inside the simulation for the given [[GeneKind]]
   * @param geneKind
   *   the dominant gene of the mutation
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

  /** @return the [[Population]] of the actual generation, it contains also dead bunnies */
  def population: Population = SimulationHistory.getActualGeneration.population

  /**
   * Method to introduce a new [[Factor]] inside the actual generation
   * @param factor
   *   the new factor added by the user
   */
  def introduceFactor(factor: Factor): Unit = SimulationHistory.getActualGeneration.environment introduceFactor factor

  /**
   * Method to remove a [[Factor]] from the actual generation
   * @param factor
   *   the factor to be removed
   */
  def removeFactor(factor: Factor): Unit = SimulationHistory.getActualGeneration.environment removeFactor factor

  /** @return the current simulation speed */
  def getCurrentSimulationSpeed: Double = SimulationEngine.simulationSpeed
}
