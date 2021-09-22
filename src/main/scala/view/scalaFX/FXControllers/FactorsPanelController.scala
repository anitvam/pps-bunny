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
                             @FXML private val predatorImage: ImageView,
                             @FXML private val toughFoodImage: ImageView,
                             @FXML private val upperFoodImage: ImageView,
                             @FXML private val limitedFoodImage: ImageView,
                             @FXML private val hostileTemperatureImage: ImageView,
                             @FXML private val predatorImageTime: ImageView,
                             @FXML private val toughFoodImageTime: ImageView,
                             @FXML private val upperFoodImageTime: ImageView,
                             @FXML private val limitedFoodImageTime: ImageView,
                             @FXML private val hostileTemperatureImageTime: ImageView
                            ) extends FactorsPanelControllerInterface {
  private def insertFactorImage(image: ImageView, url: String = "/img/death.png"): Unit = image.image = new Image(url)

  override def initialize(): Unit = {
    val factorsImageViews: List[ImageView] = List( predatorImage, predatorImageTime, toughFoodImage, toughFoodImageTime,
      upperFoodImage, upperFoodImageTime, limitedFoodImage, limitedFoodImageTime, hostileTemperatureImage, hostileTemperatureImageTime )

    factorsImageViews foreach(insertFactorImage(_))
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

