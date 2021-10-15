package it.unibo.pps.bunny.view

import it.unibo.pps.bunny.engine.SimulationEndType
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.GenerationsUtils.GenerationPhase
import it.unibo.pps.bunny.view.scalaFX.components.BunnyView

trait View {

  /** Method that starts the GUI and shows it to the user */
  def start(): Unit

  /**
   * Method that displays a the actual [[Population]] and the current [[GenerationPhase]]
   * @param bunnies
   *   the [[Population]] on bunnies
   * @param generationPhase
   *   the current [[GenerationPhase]]
   */
  def updateView(generationPhase: GenerationPhase, bunnies: Population): Unit

  /**
   * Method that shows the end of the simulation to the user
   * @param endType
   *   the [[SimulationEndType]]
   */
  def showEnd(endType: SimulationEndType): Unit

  /**
   * Method that handle the click of a bunny
   * @param bunny
   *   the clicked [[BunnyView]]
   */
  def handleBunnyClick(bunny: BunnyView): Unit
}
