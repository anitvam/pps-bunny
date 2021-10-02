package view.scalaFX.FXControllers

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
  def rotateClockHand(rotation: Double): Unit

  def updateLabel(label: String): Unit
}

class ClockController extends ClockControllerInterface {
  private var analogueClock: Group = new Group()
  private val clockRadius = 30
  private val hourHand = Line(0, 0, 0, -clockRadius)
  private val brand = Label("")

  override def initialize(): Group = {
    val face = Circle(clockRadius, clockRadius, clockRadius)
    face.id = "face"
    brand.id = "brand"
    brand.layoutXProperty().bind(face.centerXProperty().subtract(brand.widthProperty().divide(2)))
    brand.layoutYProperty().bind(face.centerYProperty().add(face.radiusProperty().divide(2)))
    hourHand.setTranslateX(clockRadius)
    hourHand.setTranslateY(clockRadius)
    hourHand.id = "hourHand"
    val spindle = Circle(clockRadius, clockRadius, 5)
    spindle.setId("spindle")
    val ticks = new Group()
    for (i <- 0 to 3) {
      val tick = Line(0, -23, 0, -33)
      tick.setTranslateX(clockRadius)
      tick.setTranslateY(clockRadius)
      tick.getStyleClass.add("tick")
      tick.getTransforms.add(new Rotate(i * (360 / 4)))
      ticks.getChildren.add(tick)
    }

    analogueClock = new Group(face, brand, ticks, spindle, hourHand)
    analogueClock.setTranslateX(40)
    analogueClock.getStylesheets.add("/fxml/stylesheets/clock.css")

    analogueClock
  }

  override def start(): Unit = {
  }

  override def rotateClockHand(rotation: Double): Unit =
    hourHand.getTransforms.add(new Rotate(rotation * (360 / 4)))

  override def updateLabel(label: String): Unit = brand.text = label
}