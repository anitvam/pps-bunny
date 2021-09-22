package view

import model.Bunny
import model.world.Generation.Population
import model.world.GenerationsUtils.GenerationPhase

/** Methods required to be implemented inside each implementation of the View */
trait View {

  /** Method that starts the GUI and shows it to the user */
  def start(): Unit

  /** Method that displays a list of bunnies and the generation number
   *  @param bunnies the list of bunnies
   *  @param generationNumber the generation number
   */
  def updateView(generationNumber: GenerationPhase, bunnies: Population): Unit

  /** Method that shows the end of the simulation to the user */
  def showEnd(): Unit

  /** Method that handle the click of a bunny
   *  @param bunny the bunny clicked
   */
  def handleBunnyClick(bunny: Bunny): Unit
}
