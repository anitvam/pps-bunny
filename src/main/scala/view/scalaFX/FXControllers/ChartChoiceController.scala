package view.scalaFX.FXControllers

import scalafx.scene.control.RadioButton
import scalafx.scene.layout.VBox
import scalafxml.core.macros.sfxml
import util.PimpScala.RichOption

sealed trait ChartChoiceControllerInterface {
  /** Method that initialize the ChartChoiceController
   * @param controller the BaseAppControllerInterface instance */
  def initialize(controller: BaseAppControllerInterface): Unit

  /** Method that handle the click of a Bunny */
  def handleBunnyClick():Unit
}

@sfxml
class ChartChoiceController( private val pedigreeRadioButton: RadioButton,
                             private val legendBox: VBox) extends ChartChoiceControllerInterface {

  private var baseAppController: Option[BaseAppControllerInterface] = None

  override def initialize(controller: BaseAppControllerInterface): Unit = {
    legendBox.setVisible(false)
    baseAppController = Some(controller)
  }

  override def handleBunnyClick(): Unit = if (pedigreeRadioButton.selected.value) baseAppController --> { _.showPedigreeChart() }

  private def showChart(legendVisibility: Boolean, chartToShow : BaseAppControllerInterface => Unit) {
    legendBox.setVisible(legendVisibility)
    baseAppController --> { chartToShow }
  }

  def showPopulationChart(): Unit = showChart(legendVisibility = false, _.showPopulationChart())

  def showProportionsChart(): Unit = showChart(legendVisibility = false, _.showProportionsChart())

  def showPedigreeChart(): Unit = showChart(legendVisibility = true, _.showPedigreeChart())
}
