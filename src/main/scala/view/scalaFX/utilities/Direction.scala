package view.scalaFX.utilities

/** Enumeration that describes the direction of bunnies jumps */
object Direction extends Enumeration {
  type Direction = Value
  val Left, Right = Value

  /**
   * Method that returns the appropriate scaleX value to set into ImageView in order to adopt the correct image
   * direction
   *
   * @param direction
   * the actual direction
   * @return
   * the scaleX value
   */
  def scaleXValue(direction: Direction): Int = if (direction == Right) 1 else -1
}
