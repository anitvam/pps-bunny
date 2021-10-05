package view.scalaFX.components

import scalafx.animation.KeyFrame
import scalafx.scene.image.ImageView
import view.scalaFX.utilities.Direction.{ scaleXValue, Direction, Left, Right }

abstract class AnimalView {

  /** Type annotation for a Seq of KeyFrames */
  type AnimationFrames = Seq[KeyFrame]

  /** The image of the animal displayed in the GUI */
  val imageView: ImageView

  /** The direction in which the animal moves */
  protected var direction: Direction

  /** The position (X,Y) in which the animal moves */
  protected var positionX: Double
  protected var positionY: Double

  /** Starts the animation */
  def play(): Unit

  /** Stops the animation */
  def stop(): Unit

  /**
   * Method that checks the actual direction of the animal and update the orientation of its image according to the
   * specific condition
   * @param conditionForChangingDirectionLeft:
   *   the condition when changing direction to Left
   * @param conditionForChangingDirectionRight:
   *   the condition when changing direction to Right
   */
  def checkDirection(conditionForChangingDirectionLeft: Boolean, conditionForChangingDirectionRight: Boolean): Unit = {
    if (conditionForChangingDirectionLeft) direction = Left
    else if (conditionForChangingDirectionRight) direction = Right

    imageView.setScaleX(scaleXValue(direction))
  }

  /**
   * Method that moves the animal position according to animal actual Direction
   * @param movingSpace:
   *   the value of the moving space for the animal
   */
  def moveHorizontally(movingSpace: Double): Unit = direction match {
    case Right => positionX += movingSpace
    case Left  => positionX -= movingSpace
  }

}
