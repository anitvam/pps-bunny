package it.unibo.pps.bunny.view.scalaFX.FXControllers

import it.unibo.pps.bunny.controller.Controller
import it.unibo.pps.bunny.engine.SimulationConstants.PhasesConstants._
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.GenerationsUtils.GenerationPhase
import it.unibo.pps.bunny.util.PimpScala.RichOption
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants._
import it.unibo.pps.bunny.view.scalaFX.components.charts.pedigree.PedigreeChart
import it.unibo.pps.bunny.view.scalaFX.components.charts.population.PopulationChart
import it.unibo.pps.bunny.view.scalaFX.components.{BunnyView, ClockView}
import it.unibo.pps.bunny.view.scalaFX.utilities.FxmlUtils.{loadPanelAndGetController, setFitParent}
import javafx.fxml.FXML
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{AnchorPane, Background}
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml

import scala.language.{implicitConversions, postfixOps}

sealed trait BaseAppControllerInterface {

  /** Method that initialize the application interface */
  def initialize(): Unit

  /** Method to reset the application interface and start a new simulation */
  def reset(): Unit

  /**
   * Method to get the simulation panel
   * @return
   *   the [[AnchorPane]] that contains the simulation
   */
  def simulationPane: AnchorPane

  /** Method that shows population chart inside chartsPane */
  def showPopulationChart(): Unit

  /** Method that shows pedigree chart inside chartsPane */
  def showPedigreeChart(): Unit

  /** Method that shows proportions chart inside chartsPane */
  def showProportionsChart(): Unit

  /**
   * Method that handles the click on a [[BunnyView]]
   * @param bunny
   *   the [[BunnyView]] clicked
   */
  def handleBunnyClick(bunny: BunnyView): Unit

  /**
   * Method that shows new bunnies into the GUI and the actual generation number
   * @param bunnies
   *   the actual bunnies [[Population]]
   * @param generationPhase
   *   the actual [[GenerationPhase]]
   */
  def updateView(bunnies: Population, generationPhase: GenerationPhase): Unit

  /**
   * Method that change background on the simulationPane
   * @param background
   *   the current background
   */
  def changeBackgroundEnvironment(background: Background): Unit
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
    @FXML private val clock: AnchorPane,
    @FXML private val speedLabel: Label
) extends BaseAppControllerInterface {

  private val clockView: ClockView = ClockView()
  private var bunnyViews: Seq[BunnyView] = Seq.empty
  private var chartSelectionPanelController: Option[ChartChoiceControllerInterface] = None
  private var selectedBunny: Option[BunnyView] = None
  private var mutationsPanelController: Option[MutationsPanelControllerInterface] = Option.empty
  private var factorsPanelController: Option[FactorsPanelControllerInterface] = Option.empty
  private var proportionsChartController: Option[ChartController] = Option.empty
  private var proportionsChartPane: Option[AnchorPane] = Option.empty
  private var populationChart: Option[PopulationChart] = Option.empty

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


  override def reset(): Unit = {
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

  /** Handler of simulation speed button */
  def changeSimulationSpeed(): Unit = {
    Controller.changeSimulationSpeed()
    speedLabel.text = speedLabel.getText match {
      case "Speed: 1x" => "Speed: 2x"
      case "Speed: 2x" => "Speed: 4x"
      case "Speed: 4x" => "Speed: 1x"
    }
  }

  private def resetSimulationPanel(): Unit = {
    bunnyViews = Seq.empty
    simulationPane.children = Seq.empty
    generationLabel.text = ""
  }

  private def resetSpeedButton(): Unit = {
    speedButton.onAction = _ => changeSimulationSpeed()
    speedLabel.text = "Speed: 1x"
    speedButton.text = ">>"
    speedButton.styleClass -= "restart-button"
  }

  private def updateCharts(bunnies: Population, generationPhase: GenerationPhase): Unit = {
    proportionsChartController --> { _.updateChart(generationPhase, bunnies) }
    populationChart --> { _.updateChart(generationPhase, bunnies) }
    chartSelectionPanelController --> { c => if (c.activeChart == Pedigree) showPedigreeChart() }
  }

  private def manageClimateClick(clickedButton: Button, otherButton: Button): Unit = {
    clickedButton.styleClass -= "button-clickable"
    otherButton.styleClass += "button-clickable"
    clickedButton.disable = true
    otherButton.disable = false
    factorsPanelController --> { _.manageEnvironmentBackgroundChange() }
  }

  private def stillAliveBunnyViews: Seq[BunnyView] = {
    val updatedBunnyViews = bunnyViews.partition(_.bunny.alive)
    updatedBunnyViews._2.foreach(bv => simulationPane.children.remove(bv.imageView))
    updatedBunnyViews._1
  }

}
