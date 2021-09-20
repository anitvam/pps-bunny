package view.scalaFX.FXControllers

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
}

