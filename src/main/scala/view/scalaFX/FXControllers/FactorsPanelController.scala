package view.scalaFX.FXControllers

<<<<<<< HEAD
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

=======
import scalafx.scene.control.RadioButton
import scalafxml.core.macros.sfxml

sealed trait FactorsPanelControllerInterface {
  /** Method called when a new generation is loaded that hides the Factors Incoming label*/
  def hideFactorIncoming(): Unit
}

@sfxml
class FactorsPanelController( private val predatorRadioButton: RadioButton,
                              private val toughFoodRadioButton: RadioButton,
                              private val upperFoodRadioButton: RadioButton,
                              private val limitedFoodRadioButton: RadioButton,
                              private val hostileTemperatureRadioButton: RadioButton
                            ) extends FactorsPanelControllerInterface {

  private def manageChoiceClick(): Unit = {
    println("Factor clicked")
  }

  private def showFactorIncoming(): Unit = {  }

  def hideFactorIncoming(): Unit = {}

  def predatorOnClick(): Unit = if (predatorRadioButton.selected.value) println("Predatori selezionati") else println("Predatori deselezionati")
  def toughFoodOnClick(): Unit = if (toughFoodRadioButton.selected.value) println("Cibo difficilmente masticabile selezionato") else println("Cibo difficilmente masticabile deselezionato")
  def upperFoodOnClick(): Unit = if (upperFoodRadioButton.selected.value) println("Cibo in alto selezionato") else println("Cibo in alto deselezionato")
  def limitedFoodOnClick(): Unit = if (limitedFoodRadioButton.selected.value) println("Cibo scarso selezionato") else println("Cibo scarso deselezionato")
  def hostileTemperatureOnClick(): Unit = if (hostileTemperatureRadioButton.selected.value) println("Temperatura ostile selezionato") else println("Temperatura ostile deselezionato")
>>>>>>> 78e90d1 (add disturbing factors first gui implementation)
}

