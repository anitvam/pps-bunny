package it.unibo.pps.bunny.view

import it.unibo.pps.bunny.engine.SimulationEndType
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.GenerationsUtils.GenerationPhase
import it.unibo.pps.bunny.view.scalaFX.components.BunnyView

trait View {

  /** Method that starts the GUI and shows it to the user */
  def start(): Unit

  /**
   * Method that displays a list of bunnies and the generation number
   * @param bunnies
   *   the list of bunnies
   * @param generationNumber
   *   the generation number
   */
  def updateView(generationNumber: GenerationPhase, bunnies: Population): Unit

  /** Method that shows the end of the simulation to the user */
  def showEnd(endType: SimulationEndType): Unit

  /**
   * Method that handle the click of a bunny
   * @param bunny
   *   the clicked bunny
   */
  def handleBunnyClick(bunny: BunnyView): Unit
}
