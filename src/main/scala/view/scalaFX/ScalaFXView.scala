package view.scalaFX

import controller.ScalaFXLauncher.stage
import engine.SimulationConstants.REPRODUCTION_PHASE
import engine.SimulationEndType
import engine.SimulationEndType.{ Extinction, GenerationsOverload, Overpopulation }
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

  private val endStage: Stage = new Stage {
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

  override def showEnd(endType: SimulationEndType): Unit = {
    endType match {
      case Overpopulation()      => endStage.show()
      case Extinction()          => println("FINE CAUSATA DA ESTINZIONE")
      case GenerationsOverload() => println("RAGGIUNTO NUMERO MASSIMO DI GENERAZIONI")
    }
    Platform.runLater { baseAppController --> { _.reset() } }
  }

  override def handleBunnyClick(bunny: BunnyView): Unit = baseAppController --> { _.handleBunnyClick(bunny) }
}
