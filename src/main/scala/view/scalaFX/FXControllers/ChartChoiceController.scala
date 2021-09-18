package view.scalaFX.FXControllers

import scalafx.scene.control.RadioButton
import scalafxml.core.macros.sfxml

sealed trait ChartChoiceControllerInterface {
  def initialize(controller: BaseAppControllerInterface): Unit
  def handleBunnyClick():Unit
}

@sfxml
class ChartChoiceController( private val pedigreeRadioButton: RadioButton ) extends ChartChoiceControllerInterface {

  private var baseAppController: Option[BaseAppControllerInterface] = None

  override def initialize(controller: BaseAppControllerInterface): Unit = {
    baseAppController = Some(controller)
  }

  override def handleBunnyClick(): Unit = if (pedigreeRadioButton.selected.value) baseAppController.get.showPedigreeChart()

  def showPopulationChart(): Unit = baseAppController.get.showPopulationChart()

  def showMutationsChart(): Unit = baseAppController.get.showProportionsChart()

  def showPedigreeChart(): Unit = baseAppController.get.showPedigreeChart()


}
