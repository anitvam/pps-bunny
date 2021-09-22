package view.scalaFX.FXControllers

import javafx.fxml.FXML
import scalafx.scene.control.CheckBox
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml

sealed trait FactorsPanelControllerInterface {
  /** initialize all the component inside the panel */
  def initialize(): Unit
}

@sfxml
class FactorsPanelController(@FXML private val predatorCheckBox: CheckBox,
                             @FXML private val toughFoodCheckBox: CheckBox,
                             @FXML private val upperFoodCheckBox: CheckBox,
                             @FXML private val limitedFoodCheckBox: CheckBox,
                             @FXML private val hostileTemperatureCheckBox: CheckBox,
                             @FXML private val wolf: ImageView,
                             @FXML private val tough_food: ImageView,
                             @FXML private val high_food: ImageView,
                             @FXML private val limited_food: ImageView,
                             @FXML private val hostile_temperature: ImageView,
                             @FXML private val wolf_time: ImageView,
                             @FXML private val tough_food_time: ImageView,
                             @FXML private val high_food_time: ImageView,
                             @FXML private val limited_food_time: ImageView,
                             @FXML private val hostile_temperature_time: ImageView
                            ) extends FactorsPanelControllerInterface {

  private def insertFactorImage(image: ImageView, id: String): Unit =
    try
      image.image = new Image("/img/factors/" + id + ".png")
    catch {
      case _: IllegalArgumentException => image.image = new Image("/img/death.png")
    }

  override def initialize(): Unit = {
    val factorsImageViews: List[ImageView] = List( wolf, tough_food,
      high_food, limited_food, hostile_temperature, wolf_time, tough_food_time,
      high_food_time, limited_food_time, hostile_temperature_time)

    factorsImageViews foreach(i => insertFactorImage(i, i.getId))
  }

  private def factorOnClick(factor: CheckBox): Unit = println(factor + " selezionato: " + factor.selected.value)
  def predatorOnClick(): Unit = factorOnClick(predatorCheckBox)
  def toughFoodOnClick(): Unit = factorOnClick(toughFoodCheckBox)
  def upperFoodOnClick(): Unit = factorOnClick(upperFoodCheckBox)
  def limitedFoodOnClick(): Unit = factorOnClick(limitedFoodCheckBox)
  def hostileTemperatureOnClick(): Unit = factorOnClick(hostileTemperatureCheckBox)

}

