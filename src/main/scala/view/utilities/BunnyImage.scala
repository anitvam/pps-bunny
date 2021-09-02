package view.utilities

import scalafx.scene.image.Image

object BunnyImage {
  val NORMAL_BROWN_HIGH_EARS_LONG_TEETH_NORMAL_FUR = "/bunnies/brown/high_ears/long_teeth/normal_fur/normal.png"
  val JUMPING_BROWN_HIGH_EARS_LONG_TEETH_NORMAL_FUR = "/bunnies/brown/high_ears/long_teeth/normal_fur/jumping.png"

  val NORMAL_BROWN_HIGH_EARS_LONG_TEETH_THICK_FUR = "/bunnies/brown/high_ears/long_teeth/thick_fur/normal.png"
  val JUMPING_BROWN_HIGH_EARS_LONG_TEETH_THICK_FUR = "/bunnies/brown/high_ears/long_teeth/thick_fur/jumping.png"

  val NORMAL_BROWN_HIGH_EARS_NORMAL_TEETH_NORMAL_FUR = "/bunnies/brown/high_ears/normal_teeth/normal_fur/normal.png"
  val JUMPING_BROWN_HIGH_EARS_NORMAL_TEETH_NORMAL_FUR = "/bunnies/brown/high_ears/normal_teeth/normal_fur/jumping.png"

  val NORMAL_BROWN_HIGH_EARS_NORMAL_TEETH_THICK_FUR = "/bunnies/brown/high_ears/normal_teeth/thick_fur/normal.png"
  val JUMPING_BROWN_HIGH_EARS_NORMAL_TEETH_THICK_FUR = "/bunnies/brown/high_ears/normal_teeth/thick_fur/jumping.png"

  val NORMAL_BROWN_LONG_EARS_LONG_TEETH_NORMAL_FUR = "/bunnies/brown/long_ears/long_teeth/normal_fur/normal.png"
  val JUMPING_BROWN_LONG_EARS_LONG_TEETH_NORMAL_FUR = "/bunnies/brown/long_ears/long_teeth/normal_fur/jumping.png"

  val NORMAL_BROWN_LONG_EARS_LONG_TEETH_THICK_FUR = "/bunnies/brown/long_ears/long_teeth/thick_fur/normal.png"
  val JUMPING_BROWN_LONG_EARS_LONG_TEETH_THICK_FUR = "/bunnies/brown/long_ears/long_teeth/thick_fur/jumping.png"

  val NORMAL_BROWN_LONG_EARS_NORMAL_TEETH_NORMAL_FUR = "/bunnies/brown/long_ears/normal_teeth/normal_fur/normal.png"
  val JUMPING_BROWN_LONG_EARS_NORMAL_TEETH_NORMAL_FUR = "/bunnies/brown/long_ears/normal_teeth/normal_fur/jumping.png"

  val NORMAL_BROWN_LONG_EARS_NORMAL_TEETH_THICK_FUR = "/bunnies/brown/long_ears/normal_teeth/thick_fur/normal.png"
  val JUMPING_BROWN_LONG_EARS_NORMAL_TEETH_THICK_FUR = "/bunnies/brown/long_ears/normal_teeth/thick_fur/jumping.png"

  val NORMAL_WHITE_HIGH_EARS_LONG_TEETH_NORMAL_FUR = "/bunnies/white/high_ears/long_teeth/normal_fur/normal.png"
  val JUMPING_WHITE_HIGH_EARS_LONG_TEETH_NORMAL_FUR = "/bunnies/white/high_ears/long_teeth/normal_fur/jumping.png"

  val NORMAL_WHITE_HIGH_EARS_LONG_TEETH_THICK_FUR = "/bunnies/white/high_ears/long_teeth/thick_fur/normal.png"
  val JUMPING_WHITE_HIGH_EARS_LONG_TEETH_THICK_FUR = "/bunnies/white/high_ears/long_teeth/thick_fur/jumping.png"

  val NORMAL_WHITE_HIGH_EARS_NORMAL_TEETH_NORMAL_FUR = "/bunnies/white/high_ears/normal_teeth/normal_fur/normal.png"
  val JUMPING_WHITE_HIGH_EARS_NORMAL_TEETH_NORMAL_FUR = "/bunnies/white/high_ears/normal_teeth/normal_fur/jumping.png"

  val NORMAL_WHITE_HIGH_EARS_NORMAL_TEETH_THICK_FUR = "/bunnies/white/high_ears/normal_teeth/thick_fur/normal.png"
  val JUMPING_WHITE_HIGH_EARS_NORMAL_TEETH_THICK_FUR = "/bunnies/white/high_ears/normal_teeth/thick_fur/jumping.png"

  val NORMAL_WHITE_LONG_EARS_LONG_TEETH_NORMAL_FUR = "/bunnies/white/long_ears/long_teeth/normal_fur/normal.png"
  val JUMPING_WHITE_LONG_EARS_LONG_TEETH_NORMAL_FUR = "/bunnies/white/long_ears/long_teeth/normal_fur/jumping.png"

  val NORMAL_WHITE_LONG_EARS_LONG_TEETH_THICK_FUR = "/bunnies/white/long_ears/long_teeth/thick_fur/normal.png"
  val JUMPING_WHITE_LONG_EARS_LONG_TEETH_THICK_FUR = "/bunnies/white/long_ears/long_teeth/thick_fur/jumping.png"

  val NORMAL_WHITE_LONG_EARS_NORMAL_TEETH_NORMAL_FUR = "/bunnies/white/long_ears/normal_teeth/normal_fur/normal.png"
  val JUMPING_WHITE_LONG_EARS_NORMAL_TEETH_NORMAL_FUR = "/bunnies/white/long_ears/normal_teeth/normal_fur/jumping.png"

  val NORMAL_WHITE_LONG_EARS_NORMAL_TEETH_THICK_FUR = "/bunnies/white/long_ears/normal_teeth/thick_fur/normal.png"
  val JUMPING_WHITE_LONG_EARS_NORMAL_TEETH_THICK_FUR = "/bunnies/white/long_ears/normal_teeth/thick_fur/jumping.png"


  /** Retrieve the image associated to a Bunny
   * @param bunny the bunny on which the image will be computed
   * @return      the scalafx.scene.image.Image of the bunny on normal state
   * */
  implicit def getImageFromBunny(): ImageWrapper = ???

  case class ImageWrapper(normalImage: Image, jumpingImage: Image)
}

