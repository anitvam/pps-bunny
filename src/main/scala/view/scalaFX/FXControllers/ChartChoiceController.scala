package view.scalaFX.FXControllers

import javafx.fxml.FXML
import scalafx.scene.control.RadioButton
import scalafx.scene.layout.VBox
import scalafxml.core.macros.sfxml
import util.PimpScala.RichOption
import view.scalaFX.utilities.ChartType
import view.scalaFX.utilities.ChartType.ChartType

sealed trait ChartChoiceControllerInterface {

  /** States which is the active chart */
  var activeChart: ChartType = ChartType.Population

  /**
   * Method that initialize the ChartChoiceController
   * @param controller
   *   the BaseAppControllerInterface instance
   */
  def initialize(controller: BaseAppControllerInterface): Unit

  /**
   * Method that reset the ChartChoiceController
   */
  def reset(): Unit

  /** Method that handle the click of a Bunny */
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
    legendBox.setVisible(false)
    baseAppController = Some(controller)
  }

  override def reset(): Unit = {
    activeChart = ChartType.Population
    populationRadioButton.selected = true
  }

  override def handleBunnyClick(): Unit =
    if (pedigreeRadioButton.selected.value) baseAppController --> { _.showPedigreeChart() }

  private def showChart(
      legendVisibility: Boolean,
      chartToShow: BaseAppControllerInterface => Unit,
      chartType: ChartType
  ): Unit = {
    legendBox.setVisible(legendVisibility)
    this.activeChart = chartType
    baseAppController --> { chartToShow }
  }

  def showPopulationChart(): Unit = showChart(legendVisibility = false, _.showPopulationChart(), ChartType.Population)

  def showProportionsChart(): Unit =
    showChart(legendVisibility = false, _.showProportionsChart(), ChartType.Proportions)

  def showPedigreeChart(): Unit = showChart(legendVisibility = true, _.showPedigreeChart(), ChartType.Pedigree)
}
