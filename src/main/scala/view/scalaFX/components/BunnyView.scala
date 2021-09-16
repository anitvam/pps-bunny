package view.scalaFX.components

import model.Bunny
import model.genome.Alleles
import scalafx.Includes.{at, double2DurationHelper}
import scalafx.animation.KeyFrame
import scalafx.scene.image.{Image, ImageView}
import view.scalaFX.ScalaFxViewConstants._
import view.scalaFX.utilities.{BunnyImageUtils, Direction, ImageType}
import view.scalaFX.utilities.Direction._
import view.scalaFX.utilities.BunnyImageUtils

import scala.language.postfixOps
import scala.util.Random

/** Bunny wrapper in order to manage its movement inside of the GUI */
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

  /** Type annotation for a Seq of KeyFrames */
  type AnimationFrames = Seq[KeyFrame]

  /** Method that returns the steps to perform a bunny jump
   * @return      a Seq[KeyFrame] containing the representation of a bunny jump
   * */
  def jump(): AnimationFrames
}

object BunnyView {

  def apply(bunny: Bunny): BunnyView = {
    val newX = Random.nextInt(PREFERRED_BUNNY_PANEL_WIDTH)
    val newY = Random.nextInt(PREFERRED_BUNNY_PANEL_HEIGHT) + PANEL_SKY_ZONE

    BunnyViewImpl(new ImageView {
      image = BunnyImageUtils.bunnyToImage(bunny, ImageType.Normal)
      x = newX
      y = newY
      fitWidth = PREFERRED_BUNNY_SIZE
      fitHeight = PREFERRED_BUNNY_SIZE
      preserveRatio = true
      scaleX = Direction.scaleXValue(Right)
    }, bunny, Right, newX, newY)
  }

  private case class BunnyViewImpl(imageView: ImageView,
                                   bunny: Bunny,
                                   var direction: Direction,
                                   var positionX: Double,
                                   var positionY: Double) extends BunnyView {

    private val normalImage: Image = BunnyImageUtils.bunnyToImage(bunny, ImageType.Normal)
    private val jumpingImage: Image = BunnyImageUtils.bunnyToImage(bunny, ImageType.Jumping)
    private val jumpingValue = if(bunny.genotype.phenotype.visibleTraits.values.exists(_ == Alleles.HIGH_JUMP)) HIGH_JUMP_HEIGHT else NORMAL_JUMP_HEIGHT

    override def jump(): AnimationFrames = {
      checkDirection()
      Seq(
        at(0 s){
          Set(imageView.image -> jumpingImage)
        },
        at(0.5 s) {
          moveHorizontally()
          positionY -= jumpingValue
  
          Set(imageView.x -> positionX, imageView.y -> positionY)
        },
        at(1 s) {
          moveHorizontally()
          positionY += jumpingValue

          Set(imageView.x -> positionX, imageView.y -> positionY)
        },
        at(1.1 s) {
          Set(imageView.image -> normalImage)
        }
      )
    }

    /** Method that checks the actual direction of the bunny and update the orientation of its image */
    private def checkDirection(): Unit = {
      if ((positionX + (2*jumpingValue)) >= PREFERRED_BUNNY_PANEL_WIDTH) {
        direction = Left
      } else if (positionX - (2*jumpingValue) < 0) {
        direction = Right
      }
      imageView.setScaleX(scaleXValue(direction))
    }

    /** Method that moves that update the bunny position according to bunny actual Direction */
    private def moveHorizontally(): Unit = direction match {
      case Right => positionX += jumpingValue
      case Left => positionX -= jumpingValue
    }

  }
}
