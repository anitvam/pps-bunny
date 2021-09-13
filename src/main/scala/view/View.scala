package view

import model.world.Generation.Population

/** Methods required to be implemented inside each implementation of the View */
trait View {
  /** Method that starts the GUI and shows it to the user */
  def start(): Unit

  /** Method that displays a list of bunnies
   * @param bunnies the list of bunnies */
  def showPopulation(bunnies: Population): Unit
}
