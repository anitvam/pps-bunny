package view.scalaFX.components

import scalafx.animation.{AnimationTimer, KeyFrame}
import scalafx.scene.image.{Image, ImageView}
import view.scalaFX.ScalaFxViewConstants.{PANEL_SKY_ZONE, PREFERRED_BUNNY_PANEL_HEIGHT, PREFERRED_BUNNY_PANEL_WIDTH}
import view.scalaFX.utilities.Direction
import view.scalaFX.utilities.Direction._

import scala.language.postfixOps
import scala.util.Random

/** Bunny wrapper in order to manage its movement inside of the GUI */
trait WolfView {
  /** Reference to the model disturbing factor entity */
//  val wolf: DisturbingFactor

  /** The image of the disturbing factor displayed on the GUI */
  val imageView: ImageView

  /** Type annotation for a Seq of KeyFrames */
  type AnimationFrames = Seq[KeyFrame]

  /** Starts the disturbing factor animation */
  def play(): Unit
}

object WolfView {

  def apply(): WolfView = {
    val newX = Random.nextInt(PREFERRED_BUNNY_PANEL_WIDTH)
    val newY = Random.nextInt(PREFERRED_BUNNY_PANEL_HEIGHT) + PANEL_SKY_ZONE

    WolfViewImpl(new ImageView {
      image = new Image("/img/factors/wolf.png")
      x = newX
      y = newY
      preserveRatio = true
      scaleX = Direction.scaleXValue(Right)
    }, Right, newX, newY)
  }

  private case class WolfViewImpl( imageView: ImageView,
                                   var direction: Direction,
                                   var positionX: Double,
                                   var positionY: Double) extends WolfView {
    private val SPEED = 200
    private val movingSpace = PREFERRED_BUNNY_PANEL_WIDTH / SPEED
    private var lastTime = 0L

    private val timer: AnimationTimer = AnimationTimer(t => {
      if(lastTime > 0) {
        checkDirection()
        moveHorizontally()
        imageView.x = positionX
      }
      lastTime = t
    })

    override def play(): Unit = timer.start()

    /** Method that checks the actual direction of the bunny and update the orientation of its image */
    private def checkDirection(): Unit = {
      if (positionX + 40 >= PREFERRED_BUNNY_PANEL_WIDTH) {
        direction = Left
      } else if (positionX - 40 < 0) {
        direction = Right
      }
      imageView.setScaleX(scaleXValue(direction))
    }

    /** Method that moves that update the bunny position according to bunny actual Direction */
    private def moveHorizontally(): Unit = direction match {
      case Right => positionX = positionX + movingSpace
      case Left => positionX = positionX - movingSpace
    }
  }
}


