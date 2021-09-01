package view.scalaFX.components

import scalafx.Includes.{at, double2DurationHelper}
import scalafx.animation.KeyFrame
import scalafx.scene.image.{Image, ImageView}
import view.scalaFX.utilities.Direction._

import scala.util.Random

object BunnyTypes {
  val NORMAL_BUNNY = new Image("/bunny/normal.png")
  val JUMPING_BUNNY = new Image("/bunny/jumping.png")
}

/** Bunny wrapper in order to manage its movement */
trait BunnyView {
  /** The image of the this displayed on the GUI */
  val imageView: ImageView

  /** The Direction of the this jumps */
  var direction: Direction

  /** The X-Axis position of the this */
  var positionX: Double

  /** The Y-Axis position of the this */
  var positionY: Double

  /** Method that returns the Set[KeyFrame] representing a bunny jump */
  def jump(): Seq[KeyFrame]
}

object BunnyView {
  def apply(): BunnyView = {
    val newX = Random.nextInt(800)
    val newY = Random.nextInt(300) + 50

    BunnyViewImpl(new ImageView {
      image = BunnyTypes.NORMAL_BUNNY
      x = newX
      y = newY
      fitWidth = 100
      fitHeight = 100
      preserveRatio = true
      scaleX = -1
    }, Right, newX, newY)
  }

  private case class BunnyViewImpl(imageView: ImageView,
                                   var direction: Direction,
                                   var positionX: Double,
                                   var positionY: Double) extends BunnyView {

    override def jump(): Seq[KeyFrame] = {
      checkDirection()
      Seq(
        at(0 s){
          Set(imageView.image -> BunnyTypes.JUMPING_BUNNY)
        },
        at(0.5 s) {
          if (direction == Right) positionX += 50 else positionX -= 50
          positionY -= 50
  
          Set(imageView.x -> positionX, imageView.y -> positionY)
        },
        at(1 s) {
          if (direction == Right) positionX += 50 else positionX -= 50
          positionY += 50

          Set(imageView.x -> positionX, imageView.y -> positionY)
        },
        at(1.1 s) {
          Set(imageView.image -> BunnyTypes.NORMAL_BUNNY)
        }
      )
    }

    /** Method that checks the actual direction of the this and update the orientation of its image */
    private def checkDirection(): Unit = {
      if ((positionX + 100) >= 1000-100) {
        direction = Left
      } else if (positionX - 100 < 0) {
        direction = Right
      }
      imageView.setScaleX(scaleXValue(direction))
    }
  }
}
