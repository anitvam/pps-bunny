package it.unibo.pps.bunny.view.scalaFX.FXControllers

import scalafx.scene.control.RadioButton
import scalafx.scene.layout.VBox
import scalafxml.core.macros.sfxml
import it.unibo.pps.bunny.util.PimpScala.RichOption
import it.unibo.pps.bunny.view.scalaFX.utilities.ChartType
import it.unibo.pps.bunny.view.scalaFX.utilities.ChartType.ChartType

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
    private val pedigreeRadioButton: RadioButton,
    private val legendBox: VBox,
    private val populationRadioButton: RadioButton
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
