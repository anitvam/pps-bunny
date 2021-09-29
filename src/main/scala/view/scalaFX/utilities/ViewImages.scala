package view.scalaFX.utilities

import engine.DisturbingFactors

import scala.language.implicitConversions
import model.Bunny
import model.genome.Alleles._
import model.genome.Genes._
import model.genome.{Genes, Phenotype}
import model.world.Environment.Factors
import model.world.{Climate, Summer, Winter}
import scalafx.beans.property.ObjectProperty
import scalafx.scene.image.Image
import scalafx.scene.layout.{Background, BackgroundImage, BackgroundPosition, BackgroundRepeat, BackgroundSize}
import view.scalaFX.utilities.ImageType.{ImageType, Jumping, Normal}

/** Enumeration for all the bunny images */
object BunnyImage extends Enumeration {

   case class BunnyImage(normalImage: Image,
                         jumpingImage: Image,
                         phenotype: Phenotype) extends super.Val
  import scala.language.implicitConversions
  implicit def valueToBunnyImage(x: Value): BunnyImage = x.asInstanceOf[BunnyImage]

  val BrownHighEarsLongTeethNormalFur: BunnyImage = BunnyImage(
                                          new Image("/img/bunnies/brown/high_ears/long_teeth/normal_fur/normal.png"),
                                          new Image("/img/bunnies/brown/high_ears/long_teeth/normal_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> BROWN_FUR, EARS -> HIGH_EARS, TEETH -> LONG_TEETH, FUR_LENGTH -> SHORT_FUR)))

  val BrownHighEarsLongTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/brown/high_ears/long_teeth/thick_fur/normal.png"),
                                          new Image("/img/bunnies/brown/high_ears/long_teeth/thick_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> BROWN_FUR, EARS -> HIGH_EARS, TEETH -> LONG_TEETH, FUR_LENGTH -> LONG_FUR)))

  val BrownHighEarsNormalTeethNormalFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/brown/high_ears/normal_teeth/normal_fur/normal.png"),
                                          new Image("/img/bunnies/brown/high_ears/normal_teeth/normal_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> BROWN_FUR, EARS -> HIGH_EARS, TEETH -> SHORT_TEETH, FUR_LENGTH -> SHORT_FUR)))

  val BrownHighEarsNormalTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/brown/high_ears/normal_teeth/thick_fur/normal.png"),
                                          new Image("/img/bunnies/brown/high_ears/normal_teeth/thick_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> BROWN_FUR, EARS -> HIGH_EARS, TEETH -> SHORT_TEETH, FUR_LENGTH -> LONG_FUR)))

  val BrownLongEarsLongTeethNormalFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/brown/long_ears/long_teeth/normal_fur/normal.png"),
                                          new Image("/img/bunnies/brown/long_ears/long_teeth/normal_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> BROWN_FUR, EARS -> LOW_EARS, TEETH -> LONG_TEETH, FUR_LENGTH -> SHORT_FUR)))

  val BrownLongEarsLongTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/brown/long_ears/long_teeth/thick_fur/normal.png"),
                                          new Image("/img/bunnies/brown/long_ears/long_teeth/thick_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> BROWN_FUR, EARS -> LOW_EARS, TEETH -> LONG_TEETH, FUR_LENGTH -> LONG_FUR)))

  val BrownLongEarsNormalTeethNormalFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/brown/long_ears/normal_teeth/normal_fur/normal.png"),
                                          new Image("/img/bunnies/brown/long_ears/normal_teeth/normal_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> BROWN_FUR, EARS -> LOW_EARS, TEETH -> SHORT_TEETH, FUR_LENGTH -> SHORT_FUR)))

  val BrownLongEarsNormalTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/brown/long_ears/normal_teeth/thick_fur/normal.png"),
                                          new Image("/img/bunnies/brown/long_ears/normal_teeth/thick_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> BROWN_FUR, EARS -> LOW_EARS, TEETH -> SHORT_TEETH, FUR_LENGTH -> LONG_FUR)))

  val WhiteHighEarsLongTeethNormalFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/white/high_ears/long_teeth/normal_fur/normal.png"),
                                          new Image("/img/bunnies/white/high_ears/long_teeth/normal_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> WHITE_FUR, EARS -> HIGH_EARS, TEETH -> LONG_TEETH, FUR_LENGTH -> SHORT_FUR)))

  val WhiteHighEarsLongTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/white/high_ears/long_teeth/thick_fur/normal.png"),
                                          new Image("/img/bunnies/white/high_ears/long_teeth/thick_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> WHITE_FUR, EARS -> HIGH_EARS, TEETH -> LONG_TEETH, FUR_LENGTH -> LONG_FUR)))

  val WhiteHighEarsNormalTeethNormalFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/white/high_ears/normal_teeth/normal_fur/normal.png"),
                                          new Image("/img/bunnies/white/high_ears/normal_teeth/normal_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> WHITE_FUR, EARS -> HIGH_EARS, TEETH -> SHORT_TEETH, FUR_LENGTH -> SHORT_FUR)))

  val WhiteHighEarsNormalTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/white/high_ears/normal_teeth/thick_fur/normal.png"),
                                          new Image("/img/bunnies/white/high_ears/normal_teeth/thick_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> WHITE_FUR, EARS -> HIGH_EARS, TEETH -> SHORT_TEETH, FUR_LENGTH -> LONG_FUR)))

  val WhiteLongEarsLongTeethNormalFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/white/long_ears/long_teeth/normal_fur/normal.png"),
                                          new Image("/img/bunnies/white/long_ears/long_teeth/normal_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> WHITE_FUR, EARS -> LOW_EARS, TEETH -> LONG_TEETH, FUR_LENGTH -> SHORT_FUR)))

  val WhiteLongEarsLongTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/white/long_ears/long_teeth/thick_fur/normal.png"),
                                          new Image("/img/bunnies/white/long_ears/long_teeth/thick_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> WHITE_FUR, EARS -> LOW_EARS, TEETH -> LONG_TEETH, FUR_LENGTH -> LONG_FUR)))

  val WhiteLongEarsNormalTeethNormalFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/white/long_ears/normal_teeth/normal_fur/normal.png"),
                                          new Image("/img/bunnies/white/long_ears/normal_teeth/normal_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> WHITE_FUR, EARS -> LOW_EARS, TEETH -> SHORT_TEETH, FUR_LENGTH -> SHORT_FUR)))

  val WhiteLongEarsNormalTeethThickFur: BunnyImage= BunnyImage(
                                          new Image("/img/bunnies/white/long_ears/normal_teeth/thick_fur/normal.png"),
                                          new Image("/img/bunnies/white/long_ears/normal_teeth/thick_fur/jumping.png"),
                                          Phenotype(Map(FUR_COLOR -> WHITE_FUR, EARS -> LOW_EARS, TEETH -> SHORT_TEETH, FUR_LENGTH -> LONG_FUR)))
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
    val bunnyImage = BunnyImage.values.find(_.phenotype.visibleTraits == filterMap)
    imageType match {
      case Jumping => bunnyImage.get.jumpingImage
      case Normal => bunnyImage.get.normalImage
    }
  }
}

/** Representation of the Environment Type */
trait ClimateImage {
  val image: Image
}
case class WinterImage(image: Image = EnvironmentImageUtils.WINTER_IMAGE) extends ClimateImage
case class SummerImage(image: Image = EnvironmentImageUtils.SUMMER_IMAGE) extends ClimateImage
case class WinterImageHighFood(image: Image = new Image("/img/environment/cold_environment/cold_high_food.png")) extends ClimateImage
case class SummerImageHighFood(image: Image = new Image("/img/environment/hot_environment/hot_high_food.png")) extends ClimateImage
case class WinterImageHighToughFood(image: Image = new Image("/img/environment/cold_environment/cold_high_tough_food.png")) extends ClimateImage
case class SummerImageHighToughFood(image: Image = new Image("/img/environment/hot_environment/hot_high_tough_food.png")) extends ClimateImage
case class WinterImageLimitedHighFood(image: Image = new Image("/img/environment/cold_environment/cold_rare_high_food.png")) extends ClimateImage
case class SummerImageLimitedHighFood(image: Image = new Image("/img/environment/hot_environment/hot_rare_high_food.png")) extends ClimateImage
case class WinterImageLimitedHighToughFood(image: Image = new Image("/img/environment/cold_environment/cold_rare_high_tough_food.png")) extends ClimateImage
case class SummerImageLimitedHighToughFood(image: Image = new Image("/img/environment/hot_environment/hot_rare_high_tough_food.png")) extends ClimateImage
case class WinterImageLimitedFood(image: Image = new Image("/img/environment/cold_environment/cold_rare_normal.png")) extends ClimateImage
case class SummerImageLimitedFood(image: Image = new Image("/img/environment/hot_environment/hot_rare_normal.png")) extends ClimateImage
case class WinterImageLimitedToughFood(image: Image = new Image("/img/environment/cold_environment/cold_rare_tough_food.png")) extends ClimateImage
case class SummerImageLimitedToughFood(image: Image = new Image("/img/environment/hot_environment/hot_rare_tough_food.png")) extends ClimateImage
case class WinterImageToughFood(image: Image = new Image("/img/environment/cold_environment/cold_tough_food.png")) extends ClimateImage
case class SummerImageToughFood(image: Image = new Image("/img/environment/hot_environment/hot_tough_food.png")) extends ClimateImage

object EnvironmentImageUtils {
  type JavaBackground = javafx.scene.layout.Background

  val WINTER_IMAGE = new Image("/img/environment/cold_environment/cold_normal.png")
  val SUMMER_IMAGE = new Image("/img/environment/hot_environment/hot_normal.png")

  implicit def actualClimate(background: ObjectProperty[JavaBackground]): Climate =
    if ( background.value.getImages.get(0).getImage.getUrl.contains("cold") ) Winter() else Summer()


  implicit def getBackgroundConfiguration(environment: ClimateImage): Background = new Background(Array(new BackgroundImage(
    image = environment.image,
    repeatX = BackgroundRepeat.NoRepeat,
    repeatY = BackgroundRepeat.NoRepeat,
    position = BackgroundPosition.Default,
    size = new BackgroundSize(
      width = 1.0,
      height = 1.0,
      widthAsPercentage = true,
      heightAsPercentage = true,
      contain = false,
      cover = false)
  )))

  private def getImageBackgroundCorrespondingToClimate(summerImage: ClimateImage, winterImage: ClimateImage, climate: Climate): Background = climate match {
    case Winter() => winterImage
    case Summer() => summerImage
  }

  def getBackgroundEnvironment(factors: Factors, climate: Climate): Background = factors.sorted match {
    case List(DisturbingFactors.HIGH_FOOD, DisturbingFactors.LIMITED_FOOD, DisturbingFactors.TOUGH_FOOD)  =>
      getImageBackgroundCorrespondingToClimate(SummerImageLimitedHighToughFood(), WinterImageLimitedHighToughFood(), climate)
    case List(DisturbingFactors.HIGH_FOOD, DisturbingFactors.LIMITED_FOOD) =>
      getImageBackgroundCorrespondingToClimate(SummerImageLimitedHighFood(), WinterImageLimitedHighFood(), climate)
    case List(DisturbingFactors.HIGH_FOOD, DisturbingFactors.TOUGH_FOOD) =>
      getImageBackgroundCorrespondingToClimate(SummerImageHighToughFood(), WinterImageHighToughFood(), climate)
    case List(DisturbingFactors.LIMITED_FOOD, DisturbingFactors.TOUGH_FOOD)  =>
      getImageBackgroundCorrespondingToClimate(SummerImageLimitedToughFood(), WinterImageLimitedToughFood(), climate)
    case DisturbingFactors.HIGH_FOOD :: _ =>
      getImageBackgroundCorrespondingToClimate(SummerImageHighFood(), WinterImageHighFood(), climate)
    case DisturbingFactors.LIMITED_FOOD :: _ =>
      getImageBackgroundCorrespondingToClimate(SummerImageLimitedFood(), WinterImageLimitedFood(), climate)
    case DisturbingFactors.TOUGH_FOOD :: _ =>
      getImageBackgroundCorrespondingToClimate(SummerImageToughFood(), WinterImageToughFood(), climate)
    case _ => getImageBackgroundCorrespondingToClimate(SummerImage(), WinterImage(), climate)
  }

}
