package view.scalaFX.FXControllers

import scalafx.scene.control.RadioButton
import scalafxml.core.macros.sfxml
import view.scalaFX.utilities.PimpScala.RichOption

sealed trait ChartChoiceControllerInterface {
  /** Method that initialize the ChartChoiceController
   * @param controller the BaseAppControllerInterface instance */
  def initialize(controller: BaseAppControllerInterface): Unit

  /** Method that handle the click of a Bunny */
  def handleBunnyClick():Unit
}

@sfxml
class ChartChoiceController( private val pedigreeRadioButton: RadioButton ) extends ChartChoiceControllerInterface {

  private var baseAppController: Option[BaseAppControllerInterface] = None

  override def initialize(controller: BaseAppControllerInterface): Unit = baseAppController = Some(controller)

  override def handleBunnyClick(): Unit = if (pedigreeRadioButton.selected.value) baseAppController --> { _.showPedigreeChart() }

  def showPopulationChart(): Unit = baseAppController --> { _.showPopulationChart() }

  def showMutationsChart(): Unit = baseAppController --> { _.showProportionsChart() }

  def showPedigreeChart(): Unit = baseAppController --> { _.showPedigreeChart() }


}
