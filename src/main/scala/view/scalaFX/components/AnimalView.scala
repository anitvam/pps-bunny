package view.scalaFX.components

import scalafx.animation.KeyFrame
import scalafx.scene.image.ImageView
import view.scalaFX.utilities.Direction.{Direction, Left, Right, scaleXValue}

abstract class AnimalView {

  /** Starts the animation */
  def play(): Unit

  /** Stops the animation */
  def stop(): Unit

  /** Type annotation for a Seq of KeyFrames */
  type AnimationFrames = Seq[KeyFrame]

  /** The image of the animal displayed on the GUI */
  val imageView: ImageView

  /** The direction the animal move on */
  protected var direction: Direction

  /** The position (X,Y) in which the animal move to */
  protected var positionX: Double
  protected var positionY: Double
  

  /**
   * Method that checks the actual direction of the animal and update the orientation of its image according to the
   * specific condition
   * @param conditionForChangingDirectionLeft: the condition when changing direction to Left
   * @param conditionForChangingDirectionRight: the condition when changing direction to Right
   */
  def checkDirection(conditionForChangingDirectionLeft: Boolean, conditionForChangingDirectionRight: Boolean): Unit = {
    if (conditionForChangingDirectionLeft) direction = Left
    else if (conditionForChangingDirectionRight) direction = Right

    imageView.setScaleX(scaleXValue(direction))
  }


  /** Method that moves the animal position according to animal actual Direction
   * @param movingSpace: represent the value of the moving space for the animal
   */
  def moveHorizontally(movingSpace: Double): Unit = direction match {
    case Right => positionX += movingSpace
    case Left  => positionX -= movingSpace
  }

}
