package view.scalaFX.FXControllers

import model.Bunny
import scalafx.animation.Timeline
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.scene.image.Image
import scalafx.scene.layout.{AnchorPane, Background, BackgroundImage, BackgroundPosition, BackgroundRepeat, BackgroundSize}
import scalafx.util.Duration
import scalafxml.core.macros.sfxml
import view.scalaFX.components.BunnyView

trait BaseAppControllerInterface {
  def initialize(bunnies: Seq[Bunny]): Unit
}

@sfxml
class BaseAppController(private val simulationPane: AnchorPane,
                        private val graphPane: AnchorPane,
                        private val mutationChoicePane: AnchorPane,
                        private val factorChoicePane: AnchorPane,
                        private val graphChoicePane: AnchorPane,
                        private var bunnyTimeline: Timeline) extends BaseAppControllerInterface {


  private var bunnyViews: Seq[BunnyView] = Seq.empty
  private var bunnyTimelines: Seq[Timeline] = Seq.empty

  def initialize(bunnies: Seq[Bunny]): Unit = {
    if (bunnyTimelines.nonEmpty) bunnyTimelines.foreach(_.stop())

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


    // Bunny visualization inside simulationPane
    bunnyViews = bunnies.map(BunnyView(_))
    simulationPane.children = ObservableBuffer.empty
    simulationPane.children = bunnyViews.map(_.imageView)

    // Timeline definition for each bunny of the Population
    bunnyViews.zipWithIndex.foreach(bunny => {
      val bunnyTimeline = new Timeline {
        onFinished = _ => {
          keyFrames = bunny._1.jump()
          this.play()
        }
        delay = Duration(3500 + bunny._2)
        autoReverse = true
        cycleCount = 1
        keyFrames = bunny._1.jump()
      }
      bunnyTimelines = bunnyTimeline +: bunnyTimelines
      bunnyTimeline.play()
    })
  }
}
