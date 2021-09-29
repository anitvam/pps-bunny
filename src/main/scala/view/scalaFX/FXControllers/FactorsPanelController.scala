package view.scalaFX.FXControllers

import engine.{DisturbingFactors, SimulationHistory}
import engine.SimulationHistory.getActualGeneration
import javafx.fxml.FXML
import model.world.Factor
import scalafx.scene.control.CheckBox
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml
import util.PimpScala._
import view.scalaFX.ScalaFXView.baseAppController

sealed trait FactorsPanelControllerInterface {
  /** initialize all the component inside the panel */
  def initialize(): Unit
}

@sfxml
class FactorsPanelController(@FXML private val wolfCheckBox: CheckBox,
                             @FXML private val toughFoodCheckBox: CheckBox,
                             @FXML private val highFoodCheckBox: CheckBox,
                             @FXML private val limitedFoodCheckBox: CheckBox,
                             @FXML private val hostileTemperatureCheckBox: CheckBox,
                             @FXML private val wolf: ImageView,
                             @FXML private val tough_food: ImageView,
                             @FXML private val high_food: ImageView,
                             @FXML private val limited_food: ImageView,
                             @FXML private val hostile_temperature: ImageView
                            ) extends FactorsPanelControllerInterface {

  private def insertFactorImage(image: ImageView, id: String): Unit =
    try
      image.image = new Image("/img/factors/" + id + ".png")
    catch {
      case _: IllegalArgumentException =>
    }

  override def initialize(): Unit = {
    val factorsImageViews: List[ImageView] = List(wolf, tough_food,
      high_food, limited_food, hostile_temperature)

    factorsImageViews foreach (i => insertFactorImage(i, i.getId))
  }

  private def factorOnClick(factor: CheckBox, disturbingFactor: Factor): Unit = {
    if(factor.selected.value) SimulationHistory.introduceFactor(disturbingFactor) else SimulationHistory.removeFactor(disturbingFactor)
    baseAppController --> {_.changeBackgroundEnvironment(getActualGeneration.environment.factors.filter(f => f != DisturbingFactors.WOLF &&
      f != DisturbingFactors.HOSTILE_TEMPERATURE), getActualGeneration.environment.climate)
    }
  }

  def onWolfClick(): Unit = factorOnClick(wolfCheckBox, DisturbingFactors.WOLF)
  def onToughFoodClick(): Unit = factorOnClick(toughFoodCheckBox, DisturbingFactors.TOUGH_FOOD)
  def onHighFoodClick(): Unit = factorOnClick(highFoodCheckBox, DisturbingFactors.HIGH_FOOD)
  def onLimitedFoodClick(): Unit = factorOnClick(limitedFoodCheckBox, DisturbingFactors.LIMITED_FOOD)
  def onHostileTemperatureClick(): Unit = factorOnClick(hostileTemperatureCheckBox, DisturbingFactors.HOSTILE_TEMPERATURE)
}

