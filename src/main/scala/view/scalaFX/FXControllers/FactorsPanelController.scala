package view.scalaFX.FXControllers

import javafx.fxml.FXML
import scalafx.scene.control.RadioButton
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml

sealed trait FactorsPanelControllerInterface {
  /** initialize all the component */
  def initialize(): Unit
  /** Method called when a new generation is loaded that hides the Factors Incoming label*/
  def hideFactorIncoming(): Unit
}

@sfxml
class FactorsPanelController(@FXML private val predatorRadioButton: RadioButton,
                             @FXML private val toughFoodRadioButton: RadioButton,
                             @FXML private val upperFoodRadioButton: RadioButton,
                             @FXML private val limitedFoodRadioButton: RadioButton,
                             @FXML private val hostileTemperatureRadioButton: RadioButton,
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

  private def factorOnClick(factor: RadioButton): Unit = println(factor + " selezionato: " + factor.selected.value)
  def predatorOnClick(): Unit = factorOnClick(predatorRadioButton)
  def toughFoodOnClick(): Unit = factorOnClick(toughFoodRadioButton)
  def upperFoodOnClick(): Unit = factorOnClick(upperFoodRadioButton)
  def limitedFoodOnClick(): Unit = factorOnClick(limitedFoodRadioButton)
  def hostileTemperatureOnClick(): Unit = factorOnClick(hostileTemperatureRadioButton)

  private def manageChoiceClick(): Unit = println("Factor clicked")

  private def showFactorIncoming(): Unit = {  }

  def hideFactorIncoming(): Unit = {}
}

