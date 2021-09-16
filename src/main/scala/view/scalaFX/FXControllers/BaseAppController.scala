package view.scalaFX.FXControllers

import controller.Controller
import model.world.Generation.Population
import scalafx.animation.Timeline
import javafx.scene.{layout => jfxs}
import scalafx.Includes._
import view.scalaFX.utilities.EnvironmentImageUtils._
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{AnchorPane, Background}
import scalafx.util.Duration
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafxml.core.macros.sfxml
import view.scalaFX.components.BunnyView
import view.scalaFX.utilities.{BunnyImage, SummerImage, WinterImage}

import java.io.IOException
import scala.language.postfixOps
import scala.util.Random

sealed trait BaseAppControllerInterface {
  /** Method that initialize the application interface */
  def initialize(): Unit
  /** Method that shows new bunnies into the GUI and the actual generation number */
  def showBunnies(bunnies:Population, generationNumber: Int): Unit
}

@sfxml
class BaseAppController(private val simulationPane: AnchorPane,
                        private val graphPane: AnchorPane,
                        private val mutationChoicePane: AnchorPane,
                        private val factorChoicePane: AnchorPane,
                        private val graphChoicePane: AnchorPane,
                        private val startButton: Button,
                        private val generationLabel: Label) extends BaseAppControllerInterface {


  private var bunnyViews: Seq[BunnyView] = Seq.empty
  private var bunnyTimelines: Seq[Timeline] = Seq.empty
  private var mutationsPanelController: Option[MutationsPanelControllerInterface] = Option.empty

  def initialize(): Unit = {
    // Load the default environment background
    simulationPane.background = SummerImage()

    BunnyImage

    // Load mutationPane fxml controller
    val mutationPaneView = getClass.getResource("/fxml/mutationsPanel.fxml")
    if (mutationPaneView == null) {
      throw new IOException("Cannot load resource: mutationsPanel.fxml")
    }

    val loader = new FXMLLoader(mutationPaneView, NoDependencyResolver)
    loader.load()
    val mutationsPane = loader.getRoot[jfxs.AnchorPane]
    mutationsPanelController = Some(loader.getController[MutationsPanelControllerInterface])

    AnchorPane.setTopAnchor(mutationsPane, 0.0)
    AnchorPane.setBottomAnchor(mutationsPane, 0.0)
    AnchorPane.setLeftAnchor(mutationsPane, 0.0)
    AnchorPane.setRightAnchor(mutationsPane, 0.0)

    mutationChoicePane.children = mutationsPane
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

  def showBunnies(bunnies:Population, generationNumber: Int): Unit ={
      // Bunny visualization inside simulationPane
      val newBunnyViews = bunnies.filter(_.age == 0).map(BunnyView(_))
      bunnyViews = bunnyViews.filter(_.bunny.alive) ++ newBunnyViews
      simulationPane.children = bunnyViews.map(_.imageView)

      generationLabel.text = "Generazione " + generationNumber
      if (generationNumber > 1) {
        mutationsPanelController.get.hideMutationIncoming()
      }

      // Timeline definition for each bunny of the Population
      newBunnyViews.foreach(bunny => {
        val bunnyTimeline = new Timeline {
          onFinished = _ => {
            keyFrames = bunny.jump()
            this.play()
          }
          delay = Duration(1000 + Random.nextInt(5000))
          autoReverse = true
          cycleCount = 1
          keyFrames = bunny.jump()
        }
        bunnyTimelines = bunnyTimeline +: bunnyTimelines
        bunnyTimeline.play()
      })
  }
}
