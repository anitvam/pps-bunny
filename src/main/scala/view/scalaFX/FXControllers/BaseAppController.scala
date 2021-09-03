package view.scalaFX.FXControllers

import engine.SimulationEngine
import engine.SimulationEngine.simulationLoop
import model.Bunny
import model.world.Generation.Population
import scalafx.animation.Timeline
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Button
import scalafx.scene.image.Image
import scalafx.scene.layout.{AnchorPane, Background, BackgroundImage, BackgroundPosition, BackgroundRepeat, BackgroundSize}
import scalafx.util.Duration
import scalafxml.core.macros.sfxml
import view.scalaFX.components.BunnyView

import scala.language.postfixOps

trait BaseAppControllerInterface {
  def initialize(): Unit
  def showBunnies(bunnies:Population): Unit
}

@sfxml
class BaseAppController(private val simulationPane: AnchorPane,
                        private val graphPane: AnchorPane,
                        private val mutationChoicePane: AnchorPane,
                        private val factorChoicePane: AnchorPane,
                        private val graphChoicePane: AnchorPane,
                        private val startButton: Button) extends BaseAppControllerInterface {


  private var bunnyViews: Seq[BunnyView] = Seq.empty
  private var bunnyTimelines: Seq[Timeline] = Seq.empty

  def initialize(): Unit = {
    // Environment background configuration
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
  }

  def startSimulation(): Unit = {
    startButton.setVisible(false)
    simulationLoop().unsafeRunAsyncAndForget()
  }

  def showBunnies(bunnies:Population): Unit ={
    // Bunny visualization inside simulationPane
    val newBunnyViews = bunnies.filter(_.age == 0).map(BunnyView(_))
    bunnyViews = bunnyViews ++ newBunnyViews
    simulationPane.children = ObservableBuffer.empty
    simulationPane.children = bunnyViews.map(_.imageView)

    // Timeline definition for each bunny of the Population
    newBunnyViews.zipWithIndex.foreach(bunny => {
      val bunnyTimeline = new Timeline {
        onFinished = _ => {
          keyFrames = bunny._1.jump()
          this.play()
        }
        delay = Duration(2500 + bunny._2)
        autoReverse = true
        cycleCount = 1
        keyFrames = bunny._1.jump()
      }
      bunnyTimelines = bunnyTimeline +: bunnyTimelines
      bunnyTimeline.play()
    })
  }
}
