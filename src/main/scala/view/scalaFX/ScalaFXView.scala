package view.scalaFX

import controller.ScalaFXLauncher.stage
import engine.SimulationConstants.START_PHASE
import javafx.{ scene => jfxs }
import model.world.Generation.Population
import model.world.GenerationsUtils.GenerationPhase
import scalafx.Includes._
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.image.{ Image, ImageView }
import scalafx.scene.layout.AnchorPane
import scalafx.stage.Stage
import util.PimpScala._
import view._
import view.scalaFX.FXControllers.BaseAppControllerInterface
import view.scalaFX.ScalaFXConstants.{ PREFERRED_CHART_HEIGHT, SCENE_HEIGHT, SCENE_WIDTH }
import view.scalaFX.components.BunnyView
import view.scalaFX.utilities.FxmlUtils

object ScalaFXView extends View {
  var baseAppController: Option[BaseAppControllerInterface] = Option.empty

  def start(): Unit = {
    val loadedRootPanel = FxmlUtils.loadFXMLResource[jfxs.Parent]("/fxml/baseApp.fxml")
    baseAppController = Some(loadedRootPanel._2.getController[BaseAppControllerInterface])
    baseAppController --> { _.initialize() }

    stage = new PrimaryStage {
      title = "Bunnies"
      scene = new Scene(loadedRootPanel._1)
      width = SCENE_WIDTH
      height = SCENE_HEIGHT
      resizable = false
    }
  }

  def updateView(generationPhase: GenerationPhase, bunnies: Population): Unit = Platform.runLater {
    baseAppController --> { _.updateView(bunnies, generationPhase) }
  }

  override def showEnd(generationPhase: GenerationPhase): Unit = {
    if (generationPhase.phase == START_PHASE) {
      val endStage = new Stage {
        title = "Fine simulazione"
        scene = new Scene(new AnchorPane {
          children = new ImageView {
            image = new Image("img/world.png")
            fitHeight = PREFERRED_CHART_HEIGHT
            preserveRatio = true
          }
        })
        resizable = false
      }
      endStage.show()
    }
    Platform.runLater { baseAppController --> { _.reset() } }
  }

  override def handleBunnyClick(bunny: BunnyView): Unit = baseAppController --> { _.handleBunnyClick(bunny) }
}
