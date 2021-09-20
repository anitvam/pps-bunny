package view.scalaFX.FXControllers

import controller.Controller
import javafx.scene.{layout => jfxs}
import model.Bunny
import model.world.Generation.Population
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.AnchorPane
import scalafxml.core.macros.sfxml
import view.scalaFX.ScalaFxViewConstants
import view.scalaFX.components.BunnyView
import view.scalaFX.components.charts.PopulationChart
import view.scalaFX.components.charts.pedigree.PedigreeChart
import view.scalaFX.utilities.EnvironmentImageUtils._
import view.scalaFX.utilities.FxmlUtils.{loadFXMLResource, setFitParent}
import utilities.PimpScala.RichOption
import view.scalaFX.utilities.{BunnyImage, SummerImage, WinterImage}
import scala.language.postfixOps

sealed trait BaseAppControllerInterface {
  /** Method that initialize the application interface */
  def initialize(): Unit

  /** Method that shows new bunnies into the GUI and the actual generation number */
  def showBunnies(bunnies:Population, generationNumber: Int): Unit

  /** Method that shows population chart inside chartsPane */
  def showPopulationChart(): Unit

  /** Method that shows pedigree chart inside chartsPane */
  def showPedigreeChart(): Unit

  /** Method that shows proportions chart inside chartsPane */
  def showProportionsChart(): Unit

  /** Method that handle the click on a Bunny */
  def handleBunnyClick(bunny: Bunny): Unit
}

@sfxml
class BaseAppController(private val simulationPane: AnchorPane,
                        private val chartsPane: AnchorPane,
                        private val mutationChoicePane: AnchorPane,
                        private val factorChoicePane: AnchorPane,
                        private val startButton: Button,
                        private val generationLabel: Label,
                        private val chartChoicePane: AnchorPane) extends BaseAppControllerInterface {


  private var bunnyViews: Seq[BunnyView] = Seq.empty
  private var mutationsPanelController: Option[MutationsPanelControllerInterface] = None
  private var chartSelectionPanelController: Option[ChartChoiceControllerInterface] = None
  private var selectedBunny: Option[Bunny] = None

  override def initialize(): Unit = {
    // Load the default environment background
    simulationPane.background = SummerImage()

    BunnyImage
    val loadedMutationChoicePanel = loadFXMLResource[jfxs.AnchorPane]("/fxml/mutationsPanel.fxml")
    mutationChoicePane.children += loadedMutationChoicePanel._1
    mutationsPanelController = Some(loadedMutationChoicePanel._2.getController[MutationsPanelControllerInterface])

    showPopulationChart()

    val loadedChartChoice = loadFXMLResource[jfxs.AnchorPane]("/fxml/chartChoiceSelection.fxml")
    chartChoicePane.children += loadedChartChoice._1
    chartSelectionPanelController = Some(loadedChartChoice._2.getController[ChartChoiceControllerInterface])
    chartSelectionPanelController --> { _.initialize(this) }
  }

  /** Handler of Start button click */
  def startSimulationClick(): Unit = {
    startButton.setVisible(false)
    Controller.startSimulation(simulationPane.background, List.empty)
  }

  /** Handler of Summer button click */
  def setEnvironmentSummer(): Unit = {
    Controller.setSummerClimate()
    simulationPane.background = SummerImage()
  }

  /** Handler of Winter button click */
  def setEnvironmentWinter(): Unit = {
    Controller.setWinterClimate()
    simulationPane.background = WinterImage()
  }

  override def showBunnies(bunnies:Population, generationNumber: Int): Unit ={
      // Bunny visualization inside simulationPane
    if (bunnyViews.size != bunnies.size) {
      val newBunnyViews = bunnies filter { _.age == 0 } map { BunnyView(_) }
      bunnyViews = bunnyViews.filter(_.bunny.alive) ++ newBunnyViews
      simulationPane.children = bunnyViews map { _.imageView }

      generationLabel.text = "Generazione " + generationNumber
      if (generationNumber > 0) {
        mutationsPanelController --> { _.hideMutationIncoming() }
      }
      // Start movement of the new bunnies
      newBunnyViews foreach { _.play() }
    }
  }

  override def showPedigreeChart(): Unit = if (selectedBunny?) {
    val pedigreeChart = PedigreeChart(selectedBunny.get, ScalaFxViewConstants.PREFERRED_CHART_WIDTH).chartPane
    setFitParent(pedigreeChart)
    chartsPane.children = pedigreeChart
  } else chartsPane.children = ObservableBuffer.empty

  override def showPopulationChart(): Unit = chartsPane.children =
    PopulationChart.chart(ScalaFxViewConstants.PREFERRED_CHART_HEIGHT, ScalaFxViewConstants.PREFERRED_CHART_WIDTH)

  override def showProportionsChart(): Unit = chartsPane.children = ObservableBuffer.empty

  override def handleBunnyClick(bunny: Bunny): Unit = {
    selectedBunny = Some(bunny)
    chartSelectionPanelController --> { _.handleBunnyClick() }
  }
}
