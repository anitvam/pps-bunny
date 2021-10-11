package it.unibo.pps.bunny.view.scalaFX.FXControllers

import javafx.fxml.FXML
import scalafx.scene.control.RadioButton
import scalafx.scene.layout.VBox
import scalafxml.core.macros.sfxml
import it.unibo.pps.bunny.util.PimpScala.RichOption

sealed trait ChartKind
case object Population extends ChartKind
case object Pedigree extends ChartKind
case object Proportions extends ChartKind

sealed trait ChartChoiceControllerInterface {

  /** The selected Chart */
  var activeChart: ChartKind = Population

  /**
   * Method that initialize the ChartChoiceController
   * @param controller
   *   the [[BaseAppControllerInterface]] instance
   */
  def initialize(controller: BaseAppControllerInterface): Unit

  /**
   * Method that resets the ChartChoiceController
   */
  def reset(): Unit

  /** Method that handles the click on a Bunny */
  def handleBunnyClick(): Unit
}

@sfxml
class ChartChoiceController(
    @FXML private val pedigreeRadioButton: RadioButton,
    @FXML private val legendBox: VBox,
    @FXML private val populationRadioButton: RadioButton
) extends ChartChoiceControllerInterface {

  private var baseAppController: Option[BaseAppControllerInterface] = None

  override def initialize(controller: BaseAppControllerInterface): Unit = {
    legendBox.visible = false
    baseAppController = Some(controller)
  }

  override def reset(): Unit = {
    activeChart = Population
    populationRadioButton.selected = true
    legendBox.visible = false
  }

  override def handleBunnyClick(): Unit =
    if (pedigreeRadioButton.isSelected) baseAppController --> { _.showPedigreeChart() }

  def showPopulationChart(): Unit = showChart(legendVisibility = false, _.showPopulationChart(), Population)

  def showProportionsChart(): Unit = showChart(legendVisibility = false, _.showProportionsChart(), Proportions)

  def showPedigreeChart(): Unit = showChart(legendVisibility = true, _.showPedigreeChart(), Pedigree)

  private def showChart(
      legendVisibility: Boolean,
      chartToShow: BaseAppControllerInterface => Unit,
      chartType: ChartKind
  ): Unit = {
    legendBox.visible = legendVisibility
    this.activeChart = chartType
    baseAppController --> { chartToShow }
  }

}
