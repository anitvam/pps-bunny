package view.scalaFX.components

import scalafx.animation.AnimationTimer
import scalafx.scene.image.{Image, ImageView}
import view.scalaFX.FXControllers.FactorsPanelControllerInterface
import view.scalaFX.ScalaFXConstants.Wolf.PREFERRED_WOLF_PANEL_HEIGHT
import view.scalaFX.ScalaFXConstants.{PANEL_SKY_ZONE, PREFERRED_SIMULATION_PANEL_BORDER, PREFERRED_SIMULATION_PANEL_WIDTH}
import view.scalaFX.utilities.Direction
import view.scalaFX.utilities.Direction._

import scala.language.postfixOps
import scala.util.Random

/** Wolf wrapper in order to manage its movement inside of the GUI */
trait WolfView extends AnimalView {

  /** Reference to the factor panel controller entity */
  val factorsPanelController: Option[FactorsPanelControllerInterface]
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
    private val SPEED = 200
    private val movingSpace = PREFERRED_SIMULATION_PANEL_WIDTH / SPEED

    private var lastTime = 0L

    private val timer: AnimationTimer = AnimationTimer(t => {
      if (lastTime > 0) {
        checkDirection()
        moveHorizontally()
        imageView.x = positionX
        lastTime += 1
      } else stop()
    })

    override def play(): Unit = timer.start()

    override def stop(): Unit = {
      timer.stop()
      lastTime = 0L
      factorsPanelController.get.removeWolf(imageView)
    }

    /** Method that checks the actual direction of the bunny and update the orientation of its image */
     def checkDirection(): Unit = {
      if (positionX + imageView.getFitWidth/2 >= PREFERRED_SIMULATION_PANEL_WIDTH - PREFERRED_SIMULATION_PANEL_BORDER) {
        direction = Left
      } else if (positionX - imageView.getFitWidth/2 < 0) {
        direction = Right
      }
      imageView.setScaleX(scaleXValue(direction))
    }

    /** Method that moves that update the bunny position according to bunny actual Direction */
     def moveHorizontally(): Unit = direction match {
      case Right => positionX = positionX + movingSpace
      case Left  => positionX = positionX - movingSpace
    }

  }

}
