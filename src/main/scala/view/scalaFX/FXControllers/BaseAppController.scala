package view.scalaFX.FXControllers

import controller.Controller
import model.world.Generation.Population
import scalafx.animation.Timeline
import scalafx.application.Platform
import javafx.scene.{layout => jfxs}
import scalafx.Includes._
import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.Image
import scalafx.scene.layout.{AnchorPane, Background, BackgroundImage, BackgroundPosition, BackgroundRepeat, BackgroundSize}
import scalafx.util.Duration
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import scalafxml.core.macros.sfxml
import view.scalaFX.components.BunnyView
import view.utilities.BunnyImage

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
    // Environment background configuration for Simulation Pane
    val hotBackground = new Image( "/environment/climate_hot.png")
    if (hotBackground == null) {
      println("An error occurred while loading resource: climate_hot.png")
      Platform.exit()
    }
    simulationPane.background = new Background(Array(new BackgroundImage(
      image = hotBackground,
      repeatX = BackgroundRepeat.NoRepeat,
      repeatY = BackgroundRepeat.NoRepeat,
      position = BackgroundPosition.Default,
      size = new BackgroundSize(
        width = 1.0,
        height = 1.0,
        widthAsPercentage = true,
        heightAsPercentage = true,
        contain = false,
        cover = false)
    )))
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

  def handleStartSimulation(): Unit = {
    startButton.setVisible(false)
    Controller.startSimulation()
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
