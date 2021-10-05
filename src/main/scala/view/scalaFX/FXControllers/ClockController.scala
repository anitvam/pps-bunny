package view.scalaFX.FXControllers

import engine.SimulationConstants.NUMBER_OF_PHASE
import javafx.scene.Group
import scalafx.scene.control.Label
import scalafx.scene.shape.{Circle, Line}
import scalafx.scene.transform.Rotate

trait ClockControllerInterface {

  /** Initialization of the clock element*/
  def initialize(): Group

  /** Start the clock */
  def start(): Unit

  /** Rotate the clock hand to corresponding period */
  def rotateClockHand(): Unit

  def updateLabel(label: String): Unit
}

class ClockController extends ClockControllerInterface {
  private var analogueClock: Group = new Group()
  private val angle = 360 / NUMBER_OF_PHASE
  private val clockRadius = 30
  private val hourHand = Line(0, 0, 0, -clockRadius)
  private val labelClock = Label("")


  override def initialize(): Group = {
    val face = Circle(clockRadius, clockRadius, clockRadius)
    face.id = "face"
    labelClock.id = "labelClock"
    labelClock.layoutXProperty().bind(face.centerXProperty().subtract(labelClock.widthProperty().divide(2)))
    labelClock.layoutYProperty().bind(face.layoutYProperty().subtract(20))
    hourHand.setTranslateX(clockRadius)
    hourHand.setTranslateY(clockRadius)
    hourHand.getTransforms.add(new Rotate(-angle))
    hourHand.id = "hourHand"
    val spindle = Circle(clockRadius, clockRadius, 5)
    spindle.setId("spindle")
    val ticks = new Group()
    for (i <- 0 to NUMBER_OF_PHASE) {
      val tick = Line(0, -23, 0, -33)
      tick.setTranslateX(clockRadius)
      tick.setTranslateY(clockRadius)
      tick.getStyleClass.add("tick")
      tick.getTransforms.add(new Rotate(i * angle ))
      ticks.getChildren.add(tick)
    }

    analogueClock = new Group(face, labelClock, ticks, spindle, hourHand)
    analogueClock.setTranslateX(40)
    analogueClock.setTranslateY(30)
    analogueClock.getStylesheets.add("/fxml/stylesheets/clock.css")

    analogueClock
  }

  override def start(): Unit = {
  }

  override def rotateClockHand(): Unit =
    hourHand.getTransforms.add(new Rotate(angle))

  override def updateLabel(label: String): Unit = labelClock.setText(label)
}