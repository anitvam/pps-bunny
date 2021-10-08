package view.scalaFX.components

import engine.SimulationConstants.NUMBER_OF_PHASE
import engine.SimulationConstants.PhasesConstants._
import model.world.GenerationsUtils.{ GenerationPhase, ReproductionPhase }
import scalafx.Includes.jfxDoubleProperty2sfx
import scalafx.scene.Group
import scalafx.scene.control.Label
import scalafx.scene.shape.{ Circle, Line }
import scalafx.scene.transform.Rotate

trait ClockView {

  /** angle of the clock hand */
  protected val angle: Double = 360 / NUMBER_OF_PHASE

  /**
   * Initialization of the clock element in the GUI
   * @return
   *   the clock element as a scalafx Group of Node
   */
  def initialize: Group

  /**
   * Update the label of the clock with the name of the current phase (chosen in correlation to the current generation
   * phase) and rotate the clock hand
   * @param generationPhase
   *   the label to be shown
   * @param angle
   *   the angle the clock hand rotate to
   */
  def updateClock(generationPhase: GenerationPhase, angle: Double = angle): Unit

  /** Reset the clock to its initial status */
  def reset(): Unit
}

object ClockView {
  def apply(): ClockView = ClockImpl()

  private case class ClockImpl() extends ClockView {
    private val clockRadius: Double = 35
    private val clock: Circle = Circle(clockRadius, clockRadius, clockRadius)
    private val spindle = Circle(clockRadius, clockRadius, 5)
    private val analogueClock: Group = new Group()
    private val ticks = new Group()
    private val clockHand: Line = Line(0, 0, 0, -clockRadius)
    private val labelClock: Label = Label("")
    private var generationPhase: GenerationPhase = ReproductionPhase(1)

    override def initialize: Group = {
      clock.id = "clock"
      labelClock.id = "labelClock"
      clockHand.id = "clockHand"
      spindle.id = "spindle"
      labelClock.layoutXProperty() <== clock.centerXProperty().subtract(labelClock.widthProperty().divide(2))
      labelClock.layoutYProperty() <== clock.layoutYProperty().subtract(20)
      clockHand.translateX = clockRadius
      clockHand.translateY = clockRadius

      for (i <- 0 to NUMBER_OF_PHASE) {

        val tick = Line(0, -23, 0, -33)
        tick.translateX = clockRadius
        tick.translateY = clockRadius
        tick.styleClass += "tick"
        tick.transforms += new Rotate(i * angle)
        ticks.children += tick
      }

      analogueClock.children = List(clock, labelClock, ticks, spindle, clockHand)
      analogueClock.translateX = clockRadius
      analogueClock.translateY = clockRadius
      analogueClock.stylesheets += "/fxml/stylesheets/clock.css"

      rotateClockHand(-angle)
      analogueClock
    }

    private def rotateClockHand(angle: Double = angle): Unit = clockHand.transforms += new Rotate(angle)

    override def updateClock(phase: GenerationPhase, angle: Double = angle): Unit = {
      this.generationPhase = phase
      labelClock.text = phase.name
      rotateClockHand(angle)
    }

    override def reset(): Unit = generationPhase.phase match {
      case WOLVES_PHASE       => rotateClockHand(2 * angle)
      case FOOD_PHASE         => rotateClockHand(angle)
      case TEMPERATURE_PHASE  =>
      case REPRODUCTION_PHASE => rotateClockHand(3 * angle)
    }

  }

}
