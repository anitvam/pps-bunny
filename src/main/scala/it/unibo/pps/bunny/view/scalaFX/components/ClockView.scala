package it.unibo.pps.bunny.view.scalaFX.components

import it.unibo.pps.bunny.engine.SimulationConstants.NUMBER_OF_PHASES
import it.unibo.pps.bunny.engine.SimulationConstants.PhasesConstants._
import it.unibo.pps.bunny.model.world.GenerationsUtils.{ GenerationPhase, ReproductionPhase }
import scalafx.Includes.jfxDoubleProperty2sfx
import scalafx.scene.Group
import scalafx.scene.control.Label
import scalafx.scene.shape.{ Circle, Line }
import scalafx.scene.transform.Rotate

trait ClockView {

  /** Angle of the clock hand */
  protected val angle: Double = 360 / NUMBER_OF_PHASES

  /**
   * Initialization of the clock element inside the GUI
   * @return
   *   the clock element as a scalafx [[Group]] of Node
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
    private val spindle = Circle(clockRadius, clockRadius, radius = 5)
    private val analogueClock: Group = new Group()
    private val ticks = new Group()
    private val clockHand: Line = Line(0, 0, 0, -clockRadius)
    private val labelClock: Label = Label("")
    private var generationPhase: GenerationPhase = ReproductionPhase(1)
    private val tickStartX, tickEndX = 0
    private val tickStartY = -23
    private val tickEndY = -33

    override def initialize: Group = {
      clock.id = "clock"
      labelClock.id = "labelClock"
      clockHand.id = "clockHand"
      spindle.id = "spindle"
      labelClock.layoutXProperty() <== clock.centerXProperty().subtract(labelClock.widthProperty().divide(2))
      labelClock.layoutYProperty() <== clock.layoutYProperty().subtract(20)
      clockHand.translateX = clockRadius
      clockHand.translateY = clockRadius

      for (i <- 0 to NUMBER_OF_PHASES) {
        val tick = Line(tickStartX, tickStartY, tickEndX, tickEndY)
        tick.translateX = clockRadius
        tick.translateY = clockRadius
        tick.styleClass += "tick"
        tick.transforms += new Rotate(i * angle)
        ticks.children += tick
      }

      analogueClock.children = List(clock, labelClock, ticks, spindle, clockHand)
      analogueClock.translateX = clockRadius
      analogueClock.translateY = clockRadius
      analogueClock.stylesheets += "/stylesheets/clock.css"

      rotateClockHand(-angle)
      analogueClock
    }

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

    private def rotateClockHand(angle: Double = angle): Unit = clockHand.transforms += new Rotate(angle)

  }

}
