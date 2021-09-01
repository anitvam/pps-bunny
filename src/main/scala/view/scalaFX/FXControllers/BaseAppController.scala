package view.scalaFX.FXControllers

import scalafx.animation.Timeline
import scalafx.application.Platform
import scalafx.scene.image.Image
import scalafx.scene.layout.{AnchorPane, Background, BackgroundImage, BackgroundPosition, BackgroundRepeat, BackgroundSize}
import scalafx.util.Duration
import scalafxml.core.macros.sfxml
import view.scalaFX.components.BunnyView

trait BaseAppControllerInterface {
  def initialize(): Unit
}

@sfxml
class BaseAppController(private val simulationPane: AnchorPane,
                        private val graphPane: AnchorPane,
                        private val mutationChoicePane: AnchorPane,
                        private val factorChoicePane: AnchorPane,
                        private val graphChoicePane: AnchorPane) extends BaseAppControllerInterface {

  private val bunny = BunnyView()

  val bunnyTimeline: Timeline = new Timeline {
    onFinished = _ => {
      keyFrames = bunny.jump()
      this.play()
    }
    delay = Duration(2500)
    autoReverse = true
    cycleCount = 1
    keyFrames = bunny.jump()
  }

  def initialize(): Unit = {
    val hotBackground = new Image( "/environment/climate_hot.png")
    if (hotBackground == null) {
      println("An error occurred while loading resource: climate_hot.png")
      Platform.exit()
    }
    simulationPane.background = new Background(Array(new BackgroundImage(
      hotBackground,
      BackgroundRepeat.NoRepeat,
      BackgroundRepeat.NoRepeat,
      BackgroundPosition.Default,
      new BackgroundSize(1.0, 1.0, true, true, false, false)
    )))

    simulationPane.children = bunny.imageView
    bunnyTimeline.play()
  }
}
