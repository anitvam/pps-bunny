package view.scalaFX.utilities

object Direction extends Enumeration {
  type Direction = Value
  val Left, Right = Value
  def scaleXValue(direction: Direction): Int = if (direction == Right) 1 else -1
}
