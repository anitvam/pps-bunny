package view.utilities

import model.Bunny
import model.genome.{Alleles, Genes}
import model.genome.Alleles._
import scalafx.scene.image.Image
import view.utilities.Conversions.{BunnyWrapper}
import view.utilities.ImageType.{ImageType, Jumping, Normal}

object BunnyImage extends Enumeration {

   case class BunnyImage(urlNormal: String,
                                urlJumping: String,
                                alleles: List[Alleles.AlleleKind]) extends super.Val
  import scala.language.implicitConversions
  implicit def valueToBunnyImage(x: Value): BunnyImage = x.asInstanceOf[BunnyImage]

  val BrownHighEarsLongTeethNormalFur: BunnyImage = BunnyImage(
                                                        "/bunnies/brown/high_ears/long_teeth/normal_fur/normal.png",
                                                        "/bunnies/brown/high_ears/long_teeth/normal_fur/jumping.png",
                                                        List(BROWN_FUR, HIGH_EARS, LONG_TEETH, SHORT_FUR))

  val BrownHighEarsLongTeethThickFur: BunnyImage= BunnyImage(
                                                        "/bunnies/brown/high_ears/long_teeth/thick_fur/normal.png",
                                                        "/bunnies/brown/high_ears/long_teeth/thick_fur/jumping.png",
                                                        List(BROWN_FUR, HIGH_EARS, LONG_TEETH, LONG_FUR))

  val BrownHighEarsNormalTeethNormalFur: BunnyImage= BunnyImage(
                                                        "/bunnies/brown/high_ears/normal_teeth/normal_fur/normal.png",
                                                        "/bunnies/brown/high_ears/normal_teeth/normal_fur/jumping.png",
                                                        List(BROWN_FUR, HIGH_EARS, SHORT_TEETH, SHORT_FUR))

  val BrownHighEarsNormalTeethThickFur: BunnyImage= BunnyImage(
                                                        "/bunnies/brown/high_ears/normal_teeth/thick_fur/normal.png",
                                                        "/bunnies/brown/high_ears/normal_teeth/thick_fur/jumping.png",
                                                        List(BROWN_FUR, HIGH_EARS, SHORT_TEETH, LONG_FUR))

  val BrownLongEarsLongTeethNormalFur: BunnyImage= BunnyImage(
                                                        "/bunnies/brown/long_ears/long_teeth/normal_fur/normal.png",
                                                        "/bunnies/brown/long_ears/long_teeth/normal_fur/jumping.png",
                                                        List(BROWN_FUR, LOW_EARS, LONG_TEETH, SHORT_FUR))

  val BrownLongEarsLongTeethThickFur: BunnyImage= BunnyImage(
                                                        "/bunnies/brown/long_ears/long_teeth/thick_fur/normal.png",
                                                        "/bunnies/brown/long_ears/long_teeth/thick_fur/jumping.png",
                                                        List(BROWN_FUR, LOW_EARS, LONG_TEETH, LONG_FUR))

  val BrownLongEarsNormalTeethNormalFur: BunnyImage= BunnyImage(
                                                        "/bunnies/brown/long_ears/normal_teeth/normal_fur/normal.png",
                                                        "/bunnies/brown/long_ears/normal_teeth/normal_fur/jumping.png",
                                                        List(BROWN_FUR, LOW_EARS, SHORT_TEETH, SHORT_FUR))

  val BrownLongEarsNormalTeethThickFur: BunnyImage= BunnyImage(
                                                        "/bunnies/brown/long_ears/normal_teeth/thick_fur/normal.png",
                                                        "/bunnies/brown/long_ears/normal_teeth/thick_fur/jumping.png",
                                                        List(BROWN_FUR, LOW_EARS, SHORT_TEETH, LONG_FUR))

  val WhiteHighEarsLongTeethNormalFur: BunnyImage= BunnyImage(
                                                        "/bunnies/white/high_ears/long_teeth/normal_fur/normal.png",
                                                        "/bunnies/white/high_ears/long_teeth/normal_fur/jumping.png",
                                                        List(WHITE_FUR, HIGH_EARS, LONG_TEETH, SHORT_FUR))

  val WhiteHighEarsLongTeethThickFur: BunnyImage= BunnyImage(
                                                        "/bunnies/white/high_ears/long_teeth/thick_fur/normal.png",
                                                        "/bunnies/white/high_ears/long_teeth/thick_fur/jumping.png",
                                                        List(WHITE_FUR, HIGH_EARS, LONG_TEETH, LONG_FUR))

  val WhiteHighEarsNormalTeethNormalFur: BunnyImage= BunnyImage(
                                                        "/bunnies/white/high_ears/normal_teeth/normal_fur/normal.png",
                                                        "/bunnies/white/high_ears/normal_teeth/normal_fur/jumping.png",
                                                        List(WHITE_FUR, HIGH_EARS, SHORT_TEETH, SHORT_FUR))

  val WhiteHighEarsNormalTeethThickFur: BunnyImage= BunnyImage(
                                                        "/bunnies/white/high_ears/normal_teeth/thick_fur/normal.png",
                                                        "/bunnies/white/high_ears/normal_teeth/thick_fur/jumping.png",
                                                        List(WHITE_FUR, HIGH_EARS, SHORT_TEETH, LONG_FUR))

  val WhiteLongEarsLongTeethNormalFur: BunnyImage= BunnyImage(
                                                        "/bunnies/white/long_ears/long_teeth/normal_fur/normal.png",
                                                        "/bunnies/white/long_ears/long_teeth/normal_fur/jumping.png",
                                                        List(WHITE_FUR, LOW_EARS, LONG_TEETH, SHORT_FUR))

  val WhiteLongEarsLongTeethThickFur: BunnyImage= BunnyImage(
                                                        "/bunnies/white/long_ears/long_teeth/thick_fur/normal.png",
                                                        "/bunnies/white/long_ears/long_teeth/thick_fur/jumping.png",
                                                        List(WHITE_FUR, LOW_EARS, LONG_TEETH, LONG_FUR))

  val WhiteLongEarsNormalTeethNormalFur: BunnyImage= BunnyImage(
                                                        "/bunnies/white/long_ears/normal_teeth/normal_fur/normal.png",
                                                        "/bunnies/white/long_ears/normal_teeth/normal_fur/jumping.png",
                                                        List(WHITE_FUR, LOW_EARS, SHORT_TEETH, SHORT_FUR))

  val WhiteLongEarsNormalTeethThickFur: BunnyImage= BunnyImage(
                                                        "/bunnies/white/long_ears/normal_teeth/thick_fur/normal.png",
                                                        "/bunnies/white/long_ears/normal_teeth/thick_fur/jumping.png",
                                                        List(WHITE_FUR, LOW_EARS, SHORT_TEETH, LONG_FUR))
}

object ImageType extends Enumeration {
  type ImageType = Value
  val Jumping, Normal = Value
}

object Conversions {

  case class BunnyWrapper(bunny: Bunny, imageType: ImageType)

  object BunnyWrapper {
    implicit def pairToBunnyWrapper(pair: (Bunny, ImageType)): BunnyWrapper = BunnyWrapper(pair._1, pair._2)
  }

}

object BunnyImageUtils {

  /** Retrieve the image associated to a Bunny
   * @param bunny the bunny on which the image will be computed
   * @return      the scalafx.scene.image.Image of the bunny on normal state
   * */
  def bunnyToImage(wrapper: BunnyWrapper): Image = {
    val filterMap = wrapper.bunny.genotype.phenotype.visibleTraits - Genes.JUMP
    val bunnyImage = BunnyImage.values.find(enum => enum.alleles.toSet == filterMap.values.toSet)
    wrapper.imageType match {
      case Jumping => new Image(bunnyImage.get.urlJumping)
      case Normal => new Image(bunnyImage.get.urlNormal)
    }
  }
}

