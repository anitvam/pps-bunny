package view.scalaFX.FXControllers

import controller.Controller
import engine.SimulationHistory
import javafx.fxml.FXML
import javafx.scene.{layout => jfxs}
import model.world.Environment.Factors
import model.world.Generation.Population
import model.world.GenerationsUtils.GenerationPhase
import model.world.{Climate, Summer, Winter}
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.AnchorPane
import scalafxml.core.macros.sfxml
import util.PimpScala.RichOption
import view.scalaFX.ScalaFxViewConstants
import view.scalaFX.components.charts.PopulationChart
import view.scalaFX.components.charts.pedigree.PedigreeChart
import view.scalaFX.components.{BunnyView, WolfView}
import view.scalaFX.utilities.EnvironmentImageUtils._
import view.scalaFX.utilities.FxmlUtils.{loadFXMLResource, setFitParent}
import view.scalaFX.utilities._

import scala.language.postfixOps

sealed trait BaseAppControllerInterface {
  /** Method that initialize the application interface */
  def initialize(): Unit

  /** Method that shows population chart inside chartsPane */
  def showPopulationChart(): Unit

  /** Method that shows pedigree chart inside chartsPane */
  def showPedigreeChart(): Unit

  /** Method that shows proportions chart inside chartsPane */
  def showProportionsChart(): Unit

  /** Method that handle the click on a Bunny */
  def handleBunnyClick(bunny: BunnyView): Unit

  /** Method that shows new bunnies into the GUI and the actual generation number */
  def showBunnies(bunnies:Population, generationPhase: GenerationPhase): Unit

  /** Method that shows wolves eating into the GUI */
  def showWolvesEating(numberOfWolves: Int): Unit

  /** Method that change background corresponding to the factors selected
   * @param factors the list of factors
   * @param climate the current climate */
  def changeBackgroundEnvironment(factors: Factors, climate: Climate): Unit
}

@sfxml
class BaseAppController(@FXML private val simulationPane: AnchorPane,
                        @FXML private val chartsPane: AnchorPane,
                        @FXML private val mutationChoicePane: AnchorPane,
                        @FXML private val factorChoicePane: AnchorPane,
                        @FXML private val startButton: Button,
                        @FXML private val generationLabel: Label,
                        @FXML private val chartChoicePane: AnchorPane) extends BaseAppControllerInterface {


  private var bunnyViews: Seq[BunnyView] = Seq.empty
  private var chartSelectionPanelController: Option[ChartChoiceControllerInterface] = None
  private var selectedBunny: Option[BunnyView] = None
  private var mutationsPanelController: Option[MutationsPanelControllerInterface] = Option.empty
  private var factorsPanelController: Option[FactorsPanelControllerInterface] = Option.empty
  private var proportionsChartController: Option[ChartController] = Option.empty
  private var proportionsChartPane: Option[AnchorPane] = Option.empty


  override def initialize(): Unit = {
    // Load the default environment background
    simulationPane.background = SummerImage()

    BunnyImage
    val loadedMutationChoicePanel = loadFXMLResource[jfxs.AnchorPane]("/fxml/mutationsPanel.fxml")
    mutationChoicePane.children += loadedMutationChoicePanel._1
    mutationsPanelController = Some(loadedMutationChoicePanel._2.getController[MutationsPanelControllerInterface])

    val loadedFactorsChoicePanel = loadFXMLResource[jfxs.AnchorPane]("/fxml/factorsPanel.fxml")
    factorChoicePane.children += loadedFactorsChoicePanel._1
    factorsPanelController = Some(loadedFactorsChoicePanel._2.getController[FactorsPanelControllerInterface])
    factorsPanelController --> {_.initialize()}

    val loadedChartChoice = loadFXMLResource[jfxs.AnchorPane]("/fxml/chartChoiceSelection.fxml")
    chartChoicePane.children += loadedChartChoice._1
    chartSelectionPanelController = Some(loadedChartChoice._2.getController[ChartChoiceControllerInterface])
    chartSelectionPanelController --> { _.initialize(this) }

    val loadedProportionsChartView = loadFXMLResource[jfxs.AnchorPane]("/fxml/proportionsChartPane.fxml")
    proportionsChartPane = Some(loadedProportionsChartView._1)
    proportionsChartController = Some(loadedProportionsChartView._2.getController[ChartController])

    AnchorPane.setAnchors(proportionsChartPane.get, 0, 0, 0, 0)
    proportionsChartController --> {_.initialize()}

    showPopulationChart()

    //    chartsPane.children =  PopulationChart.chart(325, 500)
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
    changeBackgroundEnvironment(SimulationHistory.getActualGeneration.environment.factors, Summer())
  }

  /** Handler of Winter button click */
  def setEnvironmentWinter(): Unit = {
    Controller.setWinterClimate()
    simulationPane.background = WinterImage()
    changeBackgroundEnvironment(SimulationHistory.getActualGeneration.environment.factors, Winter())
  }

  def showBunnies(bunnies:Population, generationPhase: GenerationPhase): Unit = {
    proportionsChartController.get.updateChart(generationPhase, bunnies)
    // Bunny visualization inside simulationPane
    if (bunnyViews.size != bunnies.size) {
      val newBunnyViews = bunnies filter {
        _.age == 0
      } map {
        BunnyView(_)
      }
      bunnyViews = bunnyViews.filter(_.bunny.alive) ++ newBunnyViews
      simulationPane.children = bunnyViews map {
        _.imageView
      }


      generationLabel.text = "Generazione " + generationPhase.generationNumber
      if (generationPhase.generationNumber > 0) {
        mutationsPanelController --> { _.hideMutationIncoming() }
      }

      // Start movement of the new bunnies
      newBunnyViews foreach { _.play() }
    }
  }

  def showWolvesEating(numberOfWolves: Int): Unit = {
    val wolvesView = (1 to numberOfWolves) map(_ => WolfView())
    wolvesView foreach(w => {
      simulationPane.children.add(w.imageView)
      w.play()
    })
  }

  override def showPedigreeChart(): Unit = if (selectedBunny?) {
    val pedigreeChart = PedigreeChart(selectedBunny.get.bunny, ScalaFxViewConstants.PREFERRED_CHART_WIDTH, ScalaFxViewConstants.PREFERRED_CHART_HEIGHT).chartPane
    setFitParent(pedigreeChart)
    chartsPane.children = pedigreeChart
  } else chartsPane.children = ObservableBuffer.empty

  override def showPopulationChart(): Unit = chartsPane.children =
    PopulationChart.chart(ScalaFxViewConstants.PREFERRED_CHART_HEIGHT, ScalaFxViewConstants.PREFERRED_CHART_WIDTH)

  override def showProportionsChart(): Unit = {
    chartsPane.children = proportionsChartPane.get
  }

  override def handleBunnyClick(bunny: BunnyView): Unit = {
    if (selectedBunny?) selectedBunny.get.removeClickedEffect()
    selectedBunny = Some(bunny)
    selectedBunny.get.addClickedEffect()
    chartSelectionPanelController --> { _.handleBunnyClick() }
  }

  override def changeBackgroundEnvironment(factors: Factors, climate: Climate): Unit = simulationPane.background = getBackgroundEnvironment(factors, climate)
}
