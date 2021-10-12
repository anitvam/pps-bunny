package it.unibo.pps.bunny.view.scalaFX.utilities

/** Interface that describes the direction of bunnies jumps */
sealed trait Direction
case object Left extends Direction
case object Right extends Direction

object DirectionUtils {

  /**
   * Method that returns the appropriate scaleX value to set into ImageView in order to adopt the correct image
   * direction
   *
   * @param direction
   *   the actual direction
   * @return
   *   the scaleX value
   */
  def scaleXValue(direction: Direction): Int = if (direction == Right) 1 else -1
}
