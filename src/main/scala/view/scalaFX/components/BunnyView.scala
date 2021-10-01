package view.scalaFX.components

import model.Bunny
import model.genome.Alleles
import scalafx.Includes.{at, double2DurationHelper}

import scalafx.animation.Timeline
import scalafx.scene.effect.DropShadow
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.paint.Color
import scalafx.util.Duration
import view.scalaFX.ScalaFXConstants._
import view.scalaFX.ScalaFXView
import view.scalaFX.utilities.Direction._
import view.scalaFX.utilities.{BunnyImageUtils, Direction, ImageType}

import scala.language.postfixOps
import scala.util.Random

/** Bunny wrapper in order to manage its movement inside of the GUI */
trait BunnyView extends AnimalView {

  /** Reference to the model bunny entity */
  val bunny: Bunny

  /** Add clicked effect to this bunny */
  def addClickedEffect(): Unit

  /** Remove clicked effect from this bunny */
  def removeClickedEffect(): Unit
}

object BunnyView {

  def apply(bunny: Bunny): BunnyView = {
    val newX = Random.nextInt(PREFERRED_SIMULATION_PANEL_WIDTH)
    val newY = Random.nextInt(PREFERRED_BUNNY_PANEL_HEIGHT) + PANEL_SKY_ZONE

    val image = new ImageView {
      image = BunnyImageUtils.bunnyToImage(bunny, ImageType.Normal)
      x = newX
      y = newY
      preserveRatio = true
      scaleX = Direction.scaleXValue(Right)
    }
    BunnyViewImpl(image, bunny, Right, newX, newY)
  }

  private case class BunnyViewImpl(
      imageView: ImageView,
      bunny: Bunny,
      var direction: Direction,
      var positionX: Double,
      var positionY: Double
  ) extends BunnyView {

    private val normalImage: Image = BunnyImageUtils.bunnyToImage(bunny, ImageType.Normal)
    private val jumpingImage: Image = BunnyImageUtils.bunnyToImage(bunny, ImageType.Jumping)

    private val jumpingValue =
      if (bunny.genotype.phenotype.values.exists(_ == Alleles.HIGH_JUMP)) HIGH_JUMP_HEIGHT else NORMAL_JUMP_HEIGHT

    private val timeline: Timeline = new Timeline {

      onFinished = _ => {
        keyFrames = jump()
        this.play()
      }

      delay = Duration(STANDARD_BUNNY_JUMP_DURATION + Random.nextInt(RANDOM_BUNNY_JUMP_DELAY))
      autoReverse = true
      cycleCount = 1
      keyFrames = jump()
    }

    imageView.onMouseClicked = _ => {
      ScalaFXView.handleBunnyClick(this)
    }

    override def play(): Unit = timeline.play()

    override def stop(): Unit = timeline.stop()

    override def addClickedEffect(): Unit = imageView.effect = new DropShadow(10, Color.Black)

    override def removeClickedEffect(): Unit = imageView.effect = null

    private def jump(): AnimationFrames = {
      checkDirection(
        (positionX + (2 * jumpingValue)) >= PREFERRED_SIMULATION_PANEL_WIDTH,
        positionX - (2 * jumpingValue) < 0
      )
      Seq(
        at(0 s) {
          Set(imageView.image -> jumpingImage)
        },
        at(0.5 s) {
          moveHorizontally(jumpingValue)
          positionY -= jumpingValue

          Set(imageView.x -> positionX, imageView.y -> positionY)
        },
        at(1 s) {
          moveHorizontally(jumpingValue)
          positionY += jumpingValue

          Set(imageView.x -> positionX, imageView.y -> positionY)
        },
        at(1.1 s) {
          Set(imageView.image -> normalImage)
        }
      )
    }

  }

}
