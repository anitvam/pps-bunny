package it.unibo.pps.bunny.view.scalaFX.FXControllers

import it.unibo.pps.bunny.controller.Controller
import it.unibo.pps.bunny.engine.SimulationConstants._
import javafx.fxml.FXML
import javafx.scene.{ layout => jfxs }
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.GenerationsUtils.GenerationPhase
import scalafx.Includes._
import scalafx.scene.control.{ Button, Label }
import scalafx.scene.layout.{ AnchorPane, Background }
import scalafx.scene.text.Text
import scalafxml.core.macros.sfxml
import it.unibo.pps.bunny.util.PimpScala.RichOption
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants.{ PREFERRED_CHART_HEIGHT, PREFERRED_CHART_WIDTH }
import it.unibo.pps.bunny.view.scalaFX.components.BunnyView
import it.unibo.pps.bunny.view.scalaFX.components.charts.PopulationChart
import it.unibo.pps.bunny.view.scalaFX.components.charts.pedigree.PedigreeChart
import it.unibo.pps.bunny.view.scalaFX.utilities.FxmlUtils.{ loadFXMLResource, setFitParent }
import it.unibo.pps.bunny.view.scalaFX.utilities._

import scala.language.postfixOps

sealed trait BaseAppControllerInterface {

  /** Method that initialize the application interface */
  def initialize(): Unit

  /** Method to reset the application interface and start a new simulation */
  def reset(): Unit

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

  def addSpeedUp(): Unit
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
    @FXML private val speedButton: Button
) extends BaseAppControllerInterface {

  private var bunnyViews: Seq[BunnyView] = Seq.empty
  private var chartSelectionPanelController: Option[ChartChoiceControllerInterface] = None
  private var selectedBunny: Option[BunnyView] = None
  private var mutationsPanelController: Option[MutationsPanelControllerInterface] = Option.empty
  private var factorsPanelController: Option[FactorsPanelControllerInterface] = Option.empty
  private var proportionsChartController: Option[ChartController] = Option.empty
  private var proportionsChartPane: Option[AnchorPane] = Option.empty
  private var populationChart: Option[PopulationChart] = Option.empty

  override def initialize(): Unit = {

    val loadedMutationChoicePanel = loadFXMLResource[jfxs.AnchorPane]("/fxml/mutationsPanel.fxml")
    mutationChoicePane.children += loadedMutationChoicePanel._1
    mutationsPanelController = Some(loadedMutationChoicePanel._2.getController[MutationsPanelControllerInterface])

    val loadedFactorsChoicePanel = loadFXMLResource[jfxs.AnchorPane]("/fxml/factorsPanel.fxml")
    factorChoicePane.children += loadedFactorsChoicePanel._1
    factorsPanelController = Some(loadedFactorsChoicePanel._2.getController[FactorsPanelControllerInterface])
    factorsPanelController --> { _.initialize(this) }

    val loadedChartChoice = loadFXMLResource[jfxs.AnchorPane]("/fxml/chartChoiceSelection.fxml")
    chartChoicePane.children += loadedChartChoice._1
    chartSelectionPanelController = Some(loadedChartChoice._2.getController[ChartChoiceControllerInterface])
    chartSelectionPanelController --> { _.initialize(this) }

    val loadedProportionsChartView = loadFXMLResource[jfxs.AnchorPane]("/fxml/proportionsChartPane.fxml")
    proportionsChartPane = Some(loadedProportionsChartView._1)
    proportionsChartController = Some(loadedProportionsChartView._2.getController[ChartController])

    setFitParent(proportionsChartPane.get)
    proportionsChartController --> { _.initialize() }

    this.initializeView()
  }

  private def initializeView(): Unit = {
    // Load the default environment background
    factorsPanelController --> { _.manageEnvironmentBackgroundChange() }
    populationChart = Some(PopulationChart(PREFERRED_CHART_HEIGHT, PREFERRED_CHART_WIDTH))
    showPopulationChart()
  }

  def reset(): Unit = {
    speedButton.onAction = _ => {
      Controller.reset()
      this.resetSimulationPanel()
      selectedBunny = Option.empty
      proportionsChartController --> { _.resetChart() }
      mutationsPanelController --> { _.reset() }
      chartSelectionPanelController --> { _.reset() }
      factorsPanelController --> { _.reset() }
      this.initializeView()
      speedButton.onAction = _ => addSpeedUp()
      speedButton.text = "2x"
      speedButton.styleClass -= "restart-button"
      startSimulation()
    }
    speedButton.text = ""
    speedButton.styleClass += "restart-button"
  }

  private def resetSimulationPanel(): Unit = {
    bunnyViews = Seq.empty
    simulationPane.children = Seq.empty
    generationLabel.text = ""
    startButton.setVisible(true)
  }

  /** Handler of Start button click */
  def startSimulation(): Unit = {
    startButton.setVisible(false)
    Controller.startSimulation()
  }

  /** Handler of Summer button click */
  def setEnvironmentSummer(): Unit = {
    Controller.setSummerClimate()
    factorsPanelController --> { _.manageEnvironmentBackgroundChange() }
  }

  /** Handler of Winter button click */
  def setEnvironmentWinter(): Unit = {
    Controller.setWinterClimate()
    factorsPanelController --> { _.manageEnvironmentBackgroundChange() }
  }

  def updateView(bunnies: Population, generationPhase: GenerationPhase): Unit = {
    proportionsChartController.get.updateChart(generationPhase, bunnies)
    populationChart --> { _.updateChart(generationPhase, bunnies) }
    if (chartSelectionPanelController.get.activeChart == ChartType.Pedigree) showPedigreeChart()

    bunnyViews.filterNot(_.bunny.alive).foreach(bv => simulationPane.children.remove(bv.imageView))
    bunnyViews = bunnyViews.filter(_.bunny.alive)

    // Bunny visualization inside simulationPane
    if (generationPhase.phase == REPRODUCTION_PHASE) {
      val newBunnyViews = bunnies filter { _.age == 0 } map { BunnyView(_) }
      bunnyViews = bunnyViews ++ newBunnyViews

      generationLabel.text = "Generazione " + generationPhase.generationNumber
      if (generationPhase.generationNumber > 0) {
        mutationsPanelController --> { _.hideMutationIncoming() }
      }
      simulationPane.children ++= newBunnyViews.map(_.imageView)
      // Start movement of the new bunnies
      newBunnyViews foreach { _.play() }
    }

    if (generationPhase.phase == WOLVES_PHASE) {
      factorsPanelController --> { _.showWolvesEating() }
    }

    if (generationPhase.phase == FOOD_PHASE) {
      factorsPanelController --> { _.removeWolves() }
    }
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

  override def showProportionsChart(): Unit = chartsPane.children = proportionsChartPane.get

  override def handleBunnyClick(bunny: BunnyView): Unit = {
    if (selectedBunny ?) selectedBunny.get.removeClickedEffect()
    selectedBunny = Some(bunny)
    selectedBunny.get.addClickedEffect()
    chartSelectionPanelController --> { _.handleBunnyClick() }
  }

  override def changeBackgroundEnvironment(background: Background): Unit = simulationPane.background = background

  def addSpeedUp(): Unit = {
    Controller.incrementSimulationSpeed()
    speedButton.text = speedButton.getText match {
      case "1x" => "2x"
      case "2x" => "4x"
      case "4x" => "1x"
    }
  }

}