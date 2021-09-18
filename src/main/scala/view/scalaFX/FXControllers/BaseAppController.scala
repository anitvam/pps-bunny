package view.scalaFX.FXControllers

import controller.Controller
import javafx.scene.{layout => jfxs}
import model.Bunny
import model.world.Generation.Population
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{AnchorPane, StackPane}
import scalafxml.core.macros.sfxml
import view.scalaFX.ScalaFxViewConstants
import view.scalaFX.utilities.FxmlUtils.loadFXMLResource
import view.scalaFX.components.BunnyView
import view.scalaFX.components.charts.PopulationChart
import view.scalaFX.components.charts.tree.GenealogicalTreeView
import view.scalaFX.utilities.EnvironmentImageUtils._
import view.scalaFX.utilities.{BunnyImage, SummerImage, WinterImage}
import scala.language.postfixOps

sealed trait BaseAppControllerInterface {
  /** Method that initialize the application interface */
  def initialize(): Unit
  /** Method that shows new bunnies into the GUI and the actual generation number */
  def showBunnies(bunnies:Population, generationNumber: Int): Unit
}

@sfxml
class BaseAppController(private val simulationPane: AnchorPane,
                        private val chartsPane: StackPane,
                        private val mutationChoicePane: AnchorPane,
                        private val factorChoicePane: AnchorPane,
                        private val startButton: Button,
                        private val generationLabel: Label,
                        private val chartChoicePane: AnchorPane) extends BaseAppControllerInterface {


  private var bunnyViews: Seq[BunnyView] = Seq.empty
  private var mutationsPanelController: Option[MutationsPanelControllerInterface] = None

  def initialize(): Unit = {
    // Load the default environment background
    simulationPane.background = SummerImage()

    BunnyImage
    val loadedMutationChoicePanel = loadFXMLResource[jfxs.AnchorPane]("/fxml/mutationsPanel.fxml")
    mutationChoicePane.children = loadedMutationChoicePanel._1
    mutationsPanelController = Some(loadedMutationChoicePanel._2.getController[MutationsPanelControllerInterface])

    chartsPane.children =  PopulationChart.chart(ScalaFxViewConstants.PREFERRED_CHART_HEIGHT, ScalaFxViewConstants.PREFERRED_CHART_WIDTH)

    val loadedChartChoice = loadFXMLResource[jfxs.AnchorPane]("/fxml/chartChoiceSelection.fxml")
    chartChoicePane.children = loadedChartChoice._1
  }

  def startSimulationClick(): Unit = {
    startButton.setVisible(false)
    Controller.startSimulation(simulationPane.background, List.empty)
  }

  def setEnvironmentSummer(): Unit = {
    Controller.setSummerClimate()
    simulationPane.background = SummerImage()
  }

  def setEnvironmentWinter(): Unit = {
    Controller.setWinterClimate()
    simulationPane.background = WinterImage()
  }

  def showGenealogicalTree(bunny: Bunny): Unit = {
    chartsPane.children = GenealogicalTreeView(bunny).treePane
  }

  def showBunnies(bunnies:Population, generationNumber: Int): Unit ={
      // Bunny visualization inside simulationPane
    if (bunnyViews.size != bunnies.size) {
      val newBunnyViews = bunnies.filter(_.age == 0).map(BunnyView(_))
      bunnyViews = bunnyViews.filter(_.bunny.alive) ++ newBunnyViews
      simulationPane.children = bunnyViews.map(_.imageView)

      generationLabel.text = "Generazione " + generationNumber
      if (generationNumber > 0) {
        mutationsPanelController.get.hideMutationIncoming()
      }
      // Start movement of the new bunnies
      newBunnyViews.foreach { _.play() }
    }
  }
}
