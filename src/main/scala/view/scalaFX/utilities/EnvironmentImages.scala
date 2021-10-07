package view.scalaFX.utilities

import engine.SimulationHistory
import model.world.{ Climate, Summer, Winter }
import scalafx.beans.property.ObjectProperty
import scalafx.scene.image.Image
import scalafx.scene.layout.{ Background, BackgroundImage, BackgroundPosition, BackgroundRepeat, BackgroundSize }

/** Representation of the Environment Climate */
trait ClimateImage {
  val image: Image
}

case class WinterImage(image: Image = new Image("/img/environment/cold_environment/cold_normal.png"))
    extends ClimateImage

case class SummerImage(image: Image = new Image("/img/environment/hot_environment/hot_normal.png")) extends ClimateImage

case class WinterImageHighFood(image: Image = new Image("/img/environment/cold_environment/cold_high_food.png"))
    extends ClimateImage

case class SummerImageHighFood(image: Image = new Image("/img/environment/hot_environment/hot_high_food.png"))
    extends ClimateImage

case class WinterImageHighToughFood(
    image: Image = new Image("/img/environment/cold_environment/cold_high_tough_food.png")
) extends ClimateImage

case class SummerImageHighToughFood(
    image: Image = new Image("/img/environment/hot_environment/hot_high_tough_food.png")
) extends ClimateImage

case class WinterImageLimitedHighFood(
    image: Image = new Image("/img/environment/cold_environment/cold_rare_high_food.png")
) extends ClimateImage

case class SummerImageLimitedHighFood(
    image: Image = new Image("/img/environment/hot_environment/hot_rare_high_food.png")
) extends ClimateImage

case class WinterImageLimitedHighToughFood(
    image: Image = new Image("/img/environment/cold_environment/cold_rare_high_tough_food.png")
) extends ClimateImage

case class SummerImageLimitedHighToughFood(
    image: Image = new Image("/img/environment/hot_environment/hot_rare_high_tough_food.png")
) extends ClimateImage

case class WinterImageLimitedFood(image: Image = new Image("/img/environment/cold_environment/cold_rare_normal.png"))
    extends ClimateImage

case class SummerImageLimitedFood(image: Image = new Image("/img/environment/hot_environment/hot_rare_normal.png"))
    extends ClimateImage

case class WinterImageLimitedToughFood(
    image: Image = new Image("/img/environment/cold_environment/cold_rare_tough_food.png")
) extends ClimateImage

case class SummerImageLimitedToughFood(
    image: Image = new Image("/img/environment/hot_environment/hot_rare_tough_food.png")
) extends ClimateImage

case class WinterImageToughFood(image: Image = new Image("/img/environment/cold_environment/cold_tough_food.png"))
    extends ClimateImage

case class SummerImageToughFood(image: Image = new Image("/img/environment/hot_environment/hot_tough_food.png"))
    extends ClimateImage

object EnvironmentImageUtils {
  import scala.language.implicitConversions
  type JavaBackground = javafx.scene.layout.Background

  implicit def actualClimate(background: ObjectProperty[JavaBackground]): Climate =
    if (background.value.getImages.get(0).getImage.getUrl.contains("cold")) Winter else Summer

  implicit def getBackgroundConfiguration(environment: ClimateImage): Background = new Background(
    Array(
      new BackgroundImage(
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
          cover = false
        )
      )
    )
  )

  implicit def getBackgroundCorrespondingToClimate(
      climateImages: (ClimateImage, ClimateImage)
  ): Background = SimulationHistory.getActualGeneration.environment.climate match {
    case Winter => climateImages._2
    case Summer => climateImages._1
  }

}
