package view

import model.Bunny

/** Methods required to be implemented inside each implementation of the View */
trait View {
  /** Method that starts the GUI and shows it to the user */
  def start(bunnies: Set[Bunny]): Unit
}
