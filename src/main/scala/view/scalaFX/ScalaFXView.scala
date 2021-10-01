package view.scalaFX

import controller.ScalaFXLauncher.stage
import engine._
import javafx.{scene => jfxs}
import model.world.Generation.Population
import model.world.GenerationsUtils.GenerationPhase
import scalafx.Includes._
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.AnchorPane
import scalafx.stage.Stage
import util.PimpScala._
import view._
import view.scalaFX.FXControllers.BaseAppControllerInterface
import view.scalaFX.ScalaFXConstants.{PREFERRED_CHART_HEIGHT, SCENE_HEIGHT, SCENE_WIDTH}
import view.scalaFX.components.BunnyView
import view.scalaFX.utilities.FxmlUtils

object ScalaFXView extends View {
  var baseAppController: Option[BaseAppControllerInterface] = Option.empty
  var OVERPOPULATION_END_IMAGE = new Image("img/world.png")
  var EXTINCTION_END_IMAGE = new Image("img/extinction.png")
  var GENERATIONS_OVERLOAD_END_IMAGE = new Image("img/generations_overload.png")

  private def endStage(showingImage: Image): Stage = new Stage {
    title = "Fine simulazione"

    scene = new Scene(new AnchorPane {

      children = new ImageView {
        image = showingImage
        fitHeight = PREFERRED_CHART_HEIGHT
        preserveRatio = true
      }

    })

    resizable = false
  }

  def start(): Unit = {
    val loadedRootPanel = FxmlUtils.loadFXMLResource[jfxs.Parent]("/fxml/baseApp.fxml")
    baseAppController = Some(loadedRootPanel._2.getController[BaseAppControllerInterface])
    baseAppController --> {
      _.initialize()
    }

    stage = new PrimaryStage {
      title = "Bunnies"
      scene = new Scene(loadedRootPanel._1)
      width = SCENE_WIDTH
      height = SCENE_HEIGHT
      resizable = false
    }
  }

  def updateView(generationPhase: GenerationPhase, bunnies: Population): Unit = Platform.runLater {
    baseAppController --> {
      _.updateView(bunnies, generationPhase)
    }
  }

  override def showEnd(endType: SimulationEndType): Unit = {
    endStage(endType).show()
    Platform.runLater { baseAppController --> { _.reset() } }
  }

  override def handleBunnyClick(bunny: BunnyView): Unit = baseAppController --> { _.handleBunnyClick(bunny) }

  implicit private def simulationEndTypeImage(endType: SimulationEndType): Image = endType match {
    case Overpopulation      => OVERPOPULATION_END_IMAGE
    case Extinction          => EXTINCTION_END_IMAGE
    case GenerationsOverload => GENERATIONS_OVERLOAD_END_IMAGE
  }

}


