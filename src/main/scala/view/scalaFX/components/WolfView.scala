package view.scalaFX.components

import engine.SimulationConstants.PhasesConstants.WOLVES_PHASE
import controller.Controller
import engine.SimulationConstants.{ WOLVES_INSTANT_DEVIATION }
import scalafx.animation.AnimationTimer
import scalafx.scene.image.{ Image, ImageView }
import view.scalaFX.FXControllers.FactorsPanelControllerInterface
import view.scalaFX.ScalaFXConstants.Wolf.{ PREFERRED_WOLF_PANEL_HEIGHT, WOLVES_MOVING_SPACE }
import view.scalaFX.ScalaFXConstants.{
  PANEL_SKY_ZONE, PREFERRED_SIMULATION_PANEL_BORDER, PREFERRED_SIMULATION_PANEL_WIDTH
}
import view.scalaFX.utilities.Direction
import view.scalaFX.utilities.Direction._
import util.PimpScala.RichOption

import scala.language.postfixOps
import scala.util.Random

/** Wolf wrapper in order to manage its movement inside of the GUI */
trait WolfView extends AnimalView {

  /** Reference to the factor panel controller entity */
  val factorsPanelController: Option[FactorsPanelControllerInterface]

  def playInstantly(): Unit

}

object WolfView {

  def apply(factorsPanelController: Option[FactorsPanelControllerInterface]): WolfView = {

    val newX = Random.nextInt(PREFERRED_SIMULATION_PANEL_WIDTH - PREFERRED_SIMULATION_PANEL_BORDER)
    val newY = Random.nextInt(PREFERRED_WOLF_PANEL_HEIGHT) + PANEL_SKY_ZONE

    WolfViewImpl(
      factorsPanelController = factorsPanelController,
      new ImageView {
        image = new Image("/img/factors/wolf.png")
        x = newX
        y = newY
        preserveRatio = true
        scaleX = Direction.scaleXValue(Right)
      },
      Right,
      newX,
      newY
    )
  }

  private case class WolfViewImpl(
      factorsPanelController: Option[FactorsPanelControllerInterface],
      imageView: ImageView,
      var direction: Direction,
      var positionX: Double,
      var positionY: Double
  ) extends WolfView {

    private var lastTime = 0L
    private var isPlayDelayed = true

    private val isAnimationDelayed: Long => Boolean =
      _ < WOLVES_INSTANT_DEVIATION * 1000 * Controller.getCurrentSimulationSpeed()

    private val timer: AnimationTimer = AnimationTimer(_ => {
      if (!isAnimationDelayed(lastTime) || !isPlayDelayed)
        if (lastTime <= WOLVES_PHASE * 1000 * Controller.getCurrentSimulationSpeed()) {
          imageView.visible = true
          checkDirection(
            positionX + imageView.getFitWidth / 2 >= PREFERRED_SIMULATION_PANEL_WIDTH - PREFERRED_SIMULATION_PANEL_BORDER,
            positionX - imageView.getFitWidth / 2 < 0
          )
          moveHorizontally(WOLVES_MOVING_SPACE)
          imageView.x = positionX
        } else stop()

      lastTime += 1
    })

    override def play(): Unit = {
      isPlayDelayed = true
      imageView.visible = false
      timer.start()
    }

    override def playInstantly(): Unit = {
      isPlayDelayed = false
      imageView.visible = false
      timer.start()
    }

    override def stop(): Unit = {
      timer.stop()
      lastTime = 0L
      factorsPanelController --> { _.removeWolf(imageView) }
    }

  }

}
