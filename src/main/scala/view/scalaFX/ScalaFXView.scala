package view.scalaFX

import controller.ScalaFXLauncher.stage
import javafx.geometry.Rectangle2D
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import view.scalaFX.FXControllers.BaseAppControllerInterface
import scalafx.Includes._
import javafx.stage.Screen
import javafx.{scene => jfxs}
import model.world.Generation.Population
import scalafx.application.Platform

import java.io.IOException
import view._

object ScalaFXView extends View {
  var baseAppController: Option[BaseAppControllerInterface] = Option.empty
  private val SCREEN_BOUNDS = Screen.getPrimary.getVisualBounds
  val SCENE_WIDTH: Double = if (SCREEN_BOUNDS.getWidth > 1500) 1500 else SCREEN_BOUNDS.getWidth - 300
  val SCENE_HEIGHT: Double = if (SCREEN_BOUNDS.getHeight > 900) 900 else SCREEN_BOUNDS.getHeight - 60
  var PREFERRED_BUNNY_PANEL_WIDTH: Int = (SCENE_WIDTH * 0.6).toInt
  var PREFERRED_BUNNY_PANEL_HEIGHT: Int = (SCENE_HEIGHT * 0.25).toInt
  var PANEL_SKY_ZONE: Int = (SCENE_HEIGHT * 0.1).toInt

  def start(): Unit = {
    val baseAppView = getClass.getResource("/fxml/baseApp.fxml")
    if (baseAppView == null) {
      throw new IOException("Cannot load resource: baseApp.fxml")
    }

    val loader = new FXMLLoader(baseAppView, NoDependencyResolver)
    loader.load()
    val root = loader.getRoot[jfxs.Parent]
    baseAppController = Some(loader.getController[BaseAppControllerInterface])

    stage = new PrimaryStage() {
      title = "Bunnies"
      scene = new Scene(root)
      width = SCENE_WIDTH
      height = SCENE_HEIGHT
    }
    stage.setResizable(false)
    baseAppController.get.initialize()
  }

  def showPopulation(bunnies: Population, generationNumber: Int): Unit = {
    Platform.runLater { baseAppController.get.showBunnies(bunnies, generationNumber) }
  }
}
