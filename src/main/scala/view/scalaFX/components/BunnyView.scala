package view.scalaFX.components

import model.Bunny
import scalafx.Includes.{at, double2DurationHelper}
import scalafx.animation.KeyFrame
import scalafx.scene.image.{Image, ImageView}
import view.scalaFX.utilities.Direction._
import view.utilities.ImageType.{Jumping, Normal}
import view.utilities.{BunnyImageUtils, ImageType}

import scala.language.postfixOps
import scala.util.Random

/** Bunny wrapper in order to manage its movement */
trait BunnyView {
  /** Reference to the model bunny entity */
  val bunny: Bunny

  /** The image of the bunny displayed on the GUI */
  val imageView: ImageView

  /** The Direction of the bunny jumps */
  var direction: Direction

  /** The X-Axis position of the bunny */
  var positionX: Double

  /** The Y-Axis position of the bunny */
  var positionY: Double

  /** Method that returns the steps to perform a bunny jump
   * @return      a Set[KeyFrame] containing the representation of a bunny jump
   * */
  def jump(): Seq[KeyFrame]
}

object BunnyView {
  def apply(bunny: Bunny): BunnyView = {
    val newX = Random.nextInt(800)
    val newY = Random.nextInt(300) + 50

    BunnyViewImpl(new ImageView {
      image = BunnyImageUtils.bunnyToImage((bunny, ImageType.Normal))
      x = newX
      y = newY
      fitWidth = 100
      fitHeight = 100
      preserveRatio = true
      scaleX = -1
    }, bunny, Right, newX, newY)
  }

  private case class BunnyViewImpl(imageView: ImageView,
                                   val bunny: Bunny,
                                   var direction: Direction,
                                   var positionX: Double,
                                   var positionY: Double) extends BunnyView {

    override def jump(): Seq[KeyFrame] = {
      checkDirection()
      Seq(
        at(0 s){
          Set(imageView.image -> BunnyImageUtils.bunnyToImage(bunny, ImageType.Jumping))
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
          Set(imageView.image -> BunnyImageUtils.bunnyToImage((bunny, ImageType.Normal)))
        }
      )
    }

    /** Method that checks the actual direction of the bunny and update the orientation of its image */
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
