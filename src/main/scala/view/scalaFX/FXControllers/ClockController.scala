package view.scalaFX.FXControllers

import engine.SimulationConstants.NUMBER_OF_PHASE
import scalafx.scene.Group
import scalafx.scene.control.Label
import scalafx.scene.shape.{ Circle, Line }
import scalafx.scene.transform.Rotate

trait ClockControllerInterface {

  /**
   * Initialization of the clock element in the GUI
   * @return
   *   the clock element as a scalafx Group of Node
   */
  def initialize: Group

  /** Rotate the clock hand to show the flow of time */
  def rotateClockHand(): Unit

  /**
   * Update the label of the clock with the name of the current phase
   * @param phase
   *   the label to be shown
   */
  def updateClockLabel(phase: String): Unit
}

class ClockController extends ClockControllerInterface {
  private val analogueClock: Group = new Group()
  private val angle: Double = 360 / NUMBER_OF_PHASE
  private val clockRadius: Double = 35
  private val clockHand: Line = Line(0, 0, 0, -clockRadius)
  private val labelClock: Label = Label("")

  override def initialize: Group = {
    val clock: Circle = Circle(clockRadius, clockRadius, clockRadius)
    val spindle = Circle(clockRadius, clockRadius, 5)
    val ticks = new Group()

    clock.id = "clock"
    labelClock.id = "labelClock"
    labelClock.layoutXProperty().bind(clock.centerXProperty().subtract(labelClock.widthProperty().divide(2)))
    labelClock.layoutYProperty().bind(clock.layoutYProperty().subtract(20))
    clockHand.translateX = clockRadius
    clockHand.translateY = clockRadius
    clockHand.getTransforms.add(new Rotate(-angle))
    clockHand.id = "clockHand"
    spindle.setId("spindle")

    for (i <- 0 to NUMBER_OF_PHASE) {
      val tick = Line(0, -23, 0, -33)
      tick.translateX = clockRadius
      tick.translateY = clockRadius
      tick.getStyleClass.add("tick")
      tick.getTransforms.add(new Rotate(i * angle))
      ticks.getChildren.add(tick)
    }

    analogueClock.children = List(clock, labelClock, ticks, spindle, clockHand)
    analogueClock.translateX = clockRadius
    analogueClock.translateY = clockRadius
    analogueClock.getStylesheets.add("/fxml/stylesheets/clock.css")

    analogueClock
  }

  override def rotateClockHand(): Unit = clockHand.getTransforms.add(new Rotate(angle))

  override def updateClockLabel(phase: String): Unit = labelClock.text = phase
}
