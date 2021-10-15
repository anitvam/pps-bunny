package it.unibo.pps.bunny.view.scalaFX.utilities

import it.unibo.pps.bunny.engine.SimulationHistory
import it.unibo.pps.bunny.model.world.{ Climate, Summer, Winter }
import scalafx.beans.property.ObjectProperty
import scalafx.scene.image.Image
import scalafx.scene.layout.{ Background, BackgroundImage, BackgroundPosition, BackgroundRepeat, BackgroundSize }

/** Representation of the Environment Climate */
trait ClimateImage {
  val image: Image
}

object ClimateImageFactory {
  def apply(url: String) = new Image(url)
}

case class WinterImage(image: Image = ClimateImageFactory("/img/environment/cold_environment/cold_normal.png"))
    extends ClimateImage

case class SummerImage(image: Image = ClimateImageFactory("/img/environment/hot_environment/hot_normal.png")) extends ClimateImage

case class WinterImageHighFood(image: Image = ClimateImageFactory("/img/environment/cold_environment/cold_high_food.png"))
    extends ClimateImage

case class SummerImageHighFood(image: Image = ClimateImageFactory("/img/environment/hot_environment/hot_high_food.png"))
    extends ClimateImage

case class WinterImageHighToughFood(
    image: Image = ClimateImageFactory("/img/environment/cold_environment/cold_high_tough_food.png")
) extends ClimateImage

case class SummerImageHighToughFood(
    image: Image = ClimateImageFactory("/img/environment/hot_environment/hot_high_tough_food.png")
) extends ClimateImage

case class WinterImageLimitedHighFood(
    image: Image = ClimateImageFactory("/img/environment/cold_environment/cold_rare_high_food.png")
) extends ClimateImage

case class SummerImageLimitedHighFood(
    image: Image = ClimateImageFactory("/img/environment/hot_environment/hot_rare_high_food.png")
) extends ClimateImage

case class WinterImageLimitedHighToughFood(
    image: Image = ClimateImageFactory("/img/environment/cold_environment/cold_rare_high_tough_food.png")
) extends ClimateImage

case class SummerImageLimitedHighToughFood(
    image: Image = ClimateImageFactory("/img/environment/hot_environment/hot_rare_high_tough_food.png")
) extends ClimateImage

case class WinterImageLimitedFood(image: Image = ClimateImageFactory("/img/environment/cold_environment/cold_rare_normal.png"))
    extends ClimateImage

case class SummerImageLimitedFood(image: Image = ClimateImageFactory("/img/environment/hot_environment/hot_rare_normal.png"))
    extends ClimateImage

case class WinterImageLimitedToughFood(
    image: Image = ClimateImageFactory("/img/environment/cold_environment/cold_rare_tough_food.png")
) extends ClimateImage

case class SummerImageLimitedToughFood(
    image: Image = ClimateImageFactory("/img/environment/hot_environment/hot_rare_tough_food.png")
) extends ClimateImage

case class WinterImageToughFood(image: Image = ClimateImageFactory("/img/environment/cold_environment/cold_tough_food.png"))
    extends ClimateImage

case class SummerImageToughFood(image: Image = ClimateImageFactory("/img/environment/hot_environment/hot_tough_food.png"))
    extends ClimateImage

object EnvironmentImageUtils {
  import scala.language.implicitConversions
  type JavaBackground = javafx.scene.layout.Background

  /**
   * Implicit method that converts a [[JavaBackground]] into a [[Climate]]
   * @param background
   *   the [[JavaBackground]]
   * @return
   *   a [[Climate]]
   */
  implicit def actualClimate(background: ObjectProperty[JavaBackground]): Climate =
    if (background.value.getImages.get(0).getImage.getUrl.contains("cold")) Winter else Summer

  /**
   * Implicit method that converts a [[ClimateImage]] into a [[Background]]
   * @param climateImage
   *   the [[ClimateImage]]
   * @return
   *   the [[Background]]
   */
  implicit def getBackgroundConfiguration(climateImage: ClimateImage): Background = new Background(
    Array(
      new BackgroundImage(
        image = climateImage.image,
        repeatX = BackgroundRepeat.NoRepeat,
        repeatY = BackgroundRepeat.NoRepeat,
        position = BackgroundPosition.Default,
        size = new BackgroundSize(
          width = 1.0,
          height = 1.0,
          widthAsPercentage = true,
          heightAsPercentage = true,
          contain = false,
          cover = false
        )
      )
    )
  )

  /**
   * Implicit method that returns the right [[Background]] depending on the actual Environment [[Climate]]
   * @param climateImages
   *   a tuple [[(ClimateImage, ClimateImage)]] where the first represent the Summer one and the second is the Winter
   *   one
   * @return
   *   the right [[Background]] depending on the actual [[Climate]]
   */
  implicit def getBackgroundCorrespondingToClimate(
      climateImages: (ClimateImage, ClimateImage)
  ): Background = SimulationHistory.getActualGeneration.environment.climate match {
    case Winter => climateImages._2
    case Summer => climateImages._1
  }

}
