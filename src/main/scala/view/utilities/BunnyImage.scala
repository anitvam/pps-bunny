package view.utilities

import model.Bunny
import model.genome.{Alleles, Genes}
import model.genome.Alleles._
import scalafx.scene.image.Image
import view.utilities.ImageType.{ImageType, Jumping, Normal}

object BunnyImage extends Enumeration {

   case class BunnyImage(normalImage: Image,
                         jumpingImage: Image,
                         alleles: List[Alleles.AlleleKind]) extends super.Val
  import scala.language.implicitConversions
  implicit def valueToBunnyImage(x: Value): BunnyImage = x.asInstanceOf[BunnyImage]

  val BrownHighEarsLongTeethNormalFur: BunnyImage = BunnyImage(
                                          new Image("/bunnies/brown/high_ears/long_teeth/normal_fur/normal.png"),
                                          new Image("/bunnies/brown/high_ears/long_teeth/normal_fur/jumping.png"),
                                          List(BROWN_FUR, HIGH_EARS, LONG_TEETH, SHORT_FUR))

  val BrownHighEarsLongTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/brown/high_ears/long_teeth/thick_fur/normal.png"),
                                          new Image("/bunnies/brown/high_ears/long_teeth/thick_fur/jumping.png"),
                                          List(BROWN_FUR, HIGH_EARS, LONG_TEETH, LONG_FUR))

  val BrownHighEarsNormalTeethNormalFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/brown/high_ears/normal_teeth/normal_fur/normal.png"),
                                          new Image("/bunnies/brown/high_ears/normal_teeth/normal_fur/jumping.png"),
                                          List(BROWN_FUR, HIGH_EARS, SHORT_TEETH, SHORT_FUR))

  val BrownHighEarsNormalTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/brown/high_ears/normal_teeth/thick_fur/normal.png"),
                                          new Image("/bunnies/brown/high_ears/normal_teeth/thick_fur/jumping.png"),
                                          List(BROWN_FUR, HIGH_EARS, SHORT_TEETH, LONG_FUR))

  val BrownLongEarsLongTeethNormalFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/brown/long_ears/long_teeth/normal_fur/normal.png"),
                                          new Image("/bunnies/brown/long_ears/long_teeth/normal_fur/jumping.png"),
                                          List(BROWN_FUR, LOW_EARS, LONG_TEETH, SHORT_FUR))

  val BrownLongEarsLongTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/brown/long_ears/long_teeth/thick_fur/normal.png"),
                                          new Image("/bunnies/brown/long_ears/long_teeth/thick_fur/jumping.png"),
                                          List(BROWN_FUR, LOW_EARS, LONG_TEETH, LONG_FUR))

  val BrownLongEarsNormalTeethNormalFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/brown/long_ears/normal_teeth/normal_fur/normal.png"),
                                          new Image("/bunnies/brown/long_ears/normal_teeth/normal_fur/jumping.png"),
                                          List(BROWN_FUR, LOW_EARS, SHORT_TEETH, SHORT_FUR))

  val BrownLongEarsNormalTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/brown/long_ears/normal_teeth/thick_fur/normal.png"),
                                          new Image("/bunnies/brown/long_ears/normal_teeth/thick_fur/jumping.png"),
                                          List(BROWN_FUR, LOW_EARS, SHORT_TEETH, LONG_FUR))

  val WhiteHighEarsLongTeethNormalFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/white/high_ears/long_teeth/normal_fur/normal.png"),
                                          new Image("/bunnies/white/high_ears/long_teeth/normal_fur/jumping.png"),
                                          List(WHITE_FUR, HIGH_EARS, LONG_TEETH, SHORT_FUR))

  val WhiteHighEarsLongTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/white/high_ears/long_teeth/thick_fur/normal.png"),
                                          new Image("/bunnies/white/high_ears/long_teeth/thick_fur/jumping.png"),
                                          List(WHITE_FUR, HIGH_EARS, LONG_TEETH, LONG_FUR))

  val WhiteHighEarsNormalTeethNormalFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/white/high_ears/normal_teeth/normal_fur/normal.png"),
                                          new Image("/bunnies/white/high_ears/normal_teeth/normal_fur/jumping.png"),
                                          List(WHITE_FUR, HIGH_EARS, SHORT_TEETH, SHORT_FUR))

  val WhiteHighEarsNormalTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/white/high_ears/normal_teeth/thick_fur/normal.png"),
                                          new Image("/bunnies/white/high_ears/normal_teeth/thick_fur/jumping.png"),
                                          List(WHITE_FUR, HIGH_EARS, SHORT_TEETH, LONG_FUR))

  val WhiteLongEarsLongTeethNormalFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/white/long_ears/long_teeth/normal_fur/normal.png"),
                                          new Image("/bunnies/white/long_ears/long_teeth/normal_fur/jumping.png"),
                                          List(WHITE_FUR, LOW_EARS, LONG_TEETH, SHORT_FUR))

  val WhiteLongEarsLongTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/white/long_ears/long_teeth/thick_fur/normal.png"),
                                          new Image("/bunnies/white/long_ears/long_teeth/thick_fur/jumping.png"),
                                          List(WHITE_FUR, LOW_EARS, LONG_TEETH, LONG_FUR))

  val WhiteLongEarsNormalTeethNormalFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/white/long_ears/normal_teeth/normal_fur/normal.png"),
                                          new Image("/bunnies/white/long_ears/normal_teeth/normal_fur/jumping.png"),
                                          List(WHITE_FUR, LOW_EARS, SHORT_TEETH, SHORT_FUR))

  val WhiteLongEarsNormalTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/bunnies/white/long_ears/normal_teeth/thick_fur/normal.png"),
                                          new Image("/bunnies/white/long_ears/normal_teeth/thick_fur/jumping.png"),
                                          List(WHITE_FUR, LOW_EARS, SHORT_TEETH, LONG_FUR))
}

/** Enumeration that describes the type of a Bunny image */
object ImageType extends Enumeration {
  type ImageType = Value
  val Jumping, Normal = Value
}

object BunnyImageUtils {

  /** Method that retrieves the image associated to a Bunny
   * @param bunny     the bunny on which the image will be computed
   * @param imageType the type of the image needed
   * @return          the scalafx.scene.image.Image of the bunny with the specified state
   * */
  def bunnyToImage(bunny: Bunny, imageType: ImageType): Image = {
    val filterMap = bunny.genotype.phenotype.visibleTraits - Genes.JUMP
    val bunnyImage = BunnyImage.values.find(image => image.alleles.toSet == filterMap.values.toSet)
    imageType match {
      case Jumping => bunnyImage.get.jumpingImage
      case Normal => bunnyImage.get.normalImage
    }
  }
}

