package view.scalaFX.FXControllers

import controller.Controller
import engine.SimulationConstants.PhasesConstants._
import javafx.fxml.FXML
import model.world.Generation.Population
import model.world.GenerationsUtils.GenerationPhase
import scalafx.scene.control.{ Button, Label }
import scalafx.scene.layout.{ AnchorPane, Background }
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml
import util.PimpScala._
import view.scalaFX.ScalaFXConstants.{ PREFERRED_CHART_HEIGHT, PREFERRED_CHART_WIDTH }
import view.scalaFX.components.charts.PopulationChart
import view.scalaFX.components.charts.pedigree.PedigreeChart
import view.scalaFX.components.{ BunnyView, ClockView }
import view.scalaFX.utilities.FxmlUtils.{ loadPanelAndGetController, setFitParent }
import view.scalaFX.utilities._

import scala.language.{ implicitConversions, postfixOps }

sealed trait BaseAppControllerInterface {

  /** Method that initialize the application interface */
  def initialize(): Unit

  /** Method to reset the application interface and start a new simulation */
  def reset(): Unit

  /** Method to get the simulation panel */
  def simulationPane: AnchorPane

  /** Method that shows population chart inside chartsPane */
  def showPopulationChart(): Unit

  /** Method that shows pedigree chart inside chartsPane */
  def showPedigreeChart(): Unit

  /** Method that shows proportions chart inside chartsPane */
  def showProportionsChart(): Unit

  /** Method that handle the click on a Bunny */
  def handleBunnyClick(bunny: BunnyView): Unit

  /** Method that shows new bunnies into the GUI and the actual generation number */
  def updateView(bunnies: Population, generationPhase: GenerationPhase): Unit

  /**
   * Method that change background on the simulationPane
   * @param background
   *   the current background
   */
  def changeBackgroundEnvironment(background: Background): Unit

  /** Method that requires the increment of simulation speed */
  def changeSimulationSpeed(): Unit
}

@sfxml
class BaseAppController(
    @FXML private val pedigreeText: Text,
    @FXML val simulationPane: AnchorPane,
    @FXML private val chartsPane: AnchorPane,
    @FXML private val mutationChoicePane: AnchorPane,
    @FXML private val factorChoicePane: AnchorPane,
    @FXML private val startButton: Button,
    @FXML private val generationLabel: Label,
    @FXML private val chartChoicePane: AnchorPane,
    @FXML private val speedButton: Button,
    @FXML private val summerButton: Button,
    @FXML private val winterButton: Button,
    @FXML private val informationPanel: AnchorPane,
    @FXML private val clock: AnchorPane
) extends BaseAppControllerInterface {

  private var bunnyViews: Seq[BunnyView] = Seq.empty
  private var chartSelectionPanelController: Option[ChartChoiceControllerInterface] = None
  private var selectedBunny: Option[BunnyView] = None
  private var mutationsPanelController: Option[MutationsPanelControllerInterface] = Option.empty
  private var factorsPanelController: Option[FactorsPanelControllerInterface] = Option.empty
  private var proportionsChartController: Option[ChartController] = Option.empty
  private var proportionsChartPane: Option[AnchorPane] = Option.empty
  private var populationChart: Option[PopulationChart] = Option.empty
  private val clockView: ClockView = ClockView()

  override def initialize(): Unit = {

    mutationsPanelController = loadPanelAndGetController[MutationsPanelControllerInterface](
      "/fxml/mutationsPanel.fxml",
      mutationChoicePane.children += _
    )

    factorsPanelController = loadPanelAndGetController[FactorsPanelControllerInterface](
      "/fxml/factorsPanel.fxml",
      factorChoicePane.children += _
    )
    factorsPanelController --> { _.initialize(this) }

    chartSelectionPanelController = loadPanelAndGetController[ChartChoiceControllerInterface](
      "/fxml/chartChoiceSelection.fxml",
      chartChoicePane.children += _
    )
    chartSelectionPanelController --> { _.initialize(this) }

    proportionsChartController = loadPanelAndGetController[ChartController](
      "/fxml/proportionsChartPane.fxml",
      chart => proportionsChartPane = Some(chart)
    )
    proportionsChartController --> { _.initialize() }

    setFitParent(proportionsChartPane.get)

    clock.children = clockView.initialize
    this.initializeView()
  }

  private def initializeView(): Unit = {
    // Load the default environment background
    factorsPanelController --> { _.manageEnvironmentBackgroundChange() }
    populationChart = Some(PopulationChart(PREFERRED_CHART_HEIGHT, PREFERRED_CHART_WIDTH))
    showPopulationChart()
  }

  private def resetSimulationPanel(): Unit = {
    bunnyViews = Seq.empty
    simulationPane.children = Seq.empty
    generationLabel.text = ""
  }

  private def resetSpeedButton(): Unit = {
    speedButton.onAction = _ => changeSimulationSpeed()
    speedButton.text = "2x"
    speedButton.styleClass -= "restart-button"
  }

  def reset(): Unit = {
    speedButton.onAction = _ => {
      Controller.reset()
      resetSimulationPanel()
      selectedBunny = Option.empty
      proportionsChartController --> { _.resetChart() }
      mutationsPanelController --> { _.reset() }
      chartSelectionPanelController --> { _.reset() }
      factorsPanelController --> { _.reset() }
      clockView.reset()
      initializeView()
      resetSpeedButton()
      startSimulation()
    }
    speedButton.text = ""
    speedButton.styleClass += "restart-button"
  }

  /** Handler of Start button click */
  def startSimulation(): Unit = {
    startButton.setVisible(false)
    informationPanel.visible = false
    Controller.startSimulation()
  }

  /** Handler of Summer button click */
  def setEnvironmentSummer(): Unit = {
    Controller.setSummerClimate()
    manageClimateClick(summerButton, winterButton)
  }

  /** Handler of Winter button click */
  def setEnvironmentWinter(): Unit = {
    Controller.setWinterClimate()
    manageClimateClick(winterButton, summerButton)
  }

  private def manageClimateClick(clickedButton: Button, otherButton: Button): Unit = {
    clickedButton.styleClass -= "button-clickable"
    otherButton.styleClass += "button-clickable"
    clickedButton.disable = true
    otherButton.disable = false
    factorsPanelController --> { _.manageEnvironmentBackgroundChange() }
  }

  private def updateCharts(bunnies: Population, generationPhase: GenerationPhase): Unit = {
    proportionsChartController --> { _.updateChart(generationPhase, bunnies) }
    populationChart --> { _.updateChart(generationPhase, bunnies) }
    chartSelectionPanelController --> { c => if (c.activeChart == ChartType.Pedigree) showPedigreeChart() }
  }

  private def stillAliveBunnyViews: Seq[BunnyView] = {
    val updatedBunnyViews = bunnyViews.partition(_.bunny.alive)
    updatedBunnyViews._2.foreach(bv => simulationPane.children.remove(bv.imageView))
    updatedBunnyViews._1
  }

  override def updateView(bunnies: Population, generationPhase: GenerationPhase): Unit = {
    updateCharts(bunnies, generationPhase)
    clockView.updateClock(generationPhase)

    bunnyViews = stillAliveBunnyViews
    // Bunny visualization inside simulationPane
    if (generationPhase.phase == REPRODUCTION_PHASE) {
      val newBunnyViews = bunnies filter { _.age == 0 } map { BunnyView(_) }
      bunnyViews = bunnyViews ++ newBunnyViews

      generationLabel.text = "Generazione " + generationPhase.generationNumber
      if (generationPhase.generationNumber > 0) mutationsPanelController --> { _.hideMutationIncoming() }
      simulationPane.children ++= newBunnyViews.map(_.imageView)
      // Start movement of the new bunnies
      newBunnyViews foreach { _.play() }
      factorsPanelController --> { _.showWolves() }
    } else factorsPanelController --> { _.areWolvesShown = false }
  }

  override def showPopulationChart(): Unit = populationChart --> { c => chartsPane.children = c.chart }

  override def showPedigreeChart(): Unit =
    if (selectedBunny ?) {
      val pedigreeChart = PedigreeChart(
        selectedBunny.get.bunny,
        PREFERRED_CHART_WIDTH,
        PREFERRED_CHART_HEIGHT
      ).chartPane
      setFitParent(pedigreeChart)
      chartsPane.children = pedigreeChart
    } else chartsPane.children = pedigreeText

  override def showProportionsChart(): Unit = proportionsChartPane --> { chartsPane.children = _ }

  override def handleBunnyClick(bunny: BunnyView): Unit = {
    selectedBunny --> { _.removeClickedEffect() }
    selectedBunny = Some(bunny)
    selectedBunny --> { _.addClickedEffect() }
    chartSelectionPanelController --> { _.handleBunnyClick() }
  }

  override def changeBackgroundEnvironment(background: Background): Unit = simulationPane.background = background

  override def changeSimulationSpeed(): Unit = {
    Controller.changeSimulationSpeed()
    speedButton.text = speedButton.getText match {
      case "1x" => "2x"
      case "2x" => "4x"
      case "4x" => "1x"
    }
  }

}
