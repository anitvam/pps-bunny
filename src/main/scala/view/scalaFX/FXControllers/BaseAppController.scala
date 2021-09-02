package view.scalaFX.FXControllers

import model.Bunny
import scalafx.animation.Timeline
import scalafx.application.Platform
import scalafx.scene.image.Image
import scalafx.scene.layout.{AnchorPane, Background, BackgroundImage, BackgroundPosition, BackgroundRepeat, BackgroundSize}
import scalafx.util.Duration
import scalafxml.core.macros.sfxml
import view.scalaFX.components.BunnyView

import java.util.Timer
import java.util.concurrent.{Executor, ExecutorService, Executors, ScheduledExecutorService}

trait BaseAppControllerInterface {
  def initialize(bunnies: Set[Bunny]): Unit
}

@sfxml
class BaseAppController(private val simulationPane: AnchorPane,
                        private val graphPane: AnchorPane,
                        private val mutationChoicePane: AnchorPane,
                        private val factorChoicePane: AnchorPane,
                        private val graphChoicePane: AnchorPane,
                        private var bunnyTimeline: Timeline,
                        private var bunnyViews: Set[BunnyView] = Set.empty) extends BaseAppControllerInterface {



  def initialize(bunnies: Set[Bunny]): Unit = {
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

    val taken = bunnies.head
    println(taken)
    bunnyViews = bunnies.map(bunny => BunnyView(bunny))
    println(bunnyViews)
    simulationPane.children = bunnyViews.map(bunny => bunny.imageView)

    val executor = Executors.newFixedThreadPool(6)

    bunnyViews.foreach(bunny => {
    val bunnyTimeline = new Timeline {
      onFinished = _ => {
        keyFrames = bunny.jump()
        this.play()
      }
      delay = Duration(3500)
      autoReverse = true
      cycleCount = 1
      keyFrames = bunny.jump()
    }

    bunnyTimeline.play()
    })
  }
}
