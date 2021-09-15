package view.scalaFX

import controller.ScalaFXLauncher.stage
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
  var PREFERRED_BUNNY_PANEL_WIDTH = 0
  var PREFERRED_BUNNY_PANEL_HEIGHT = 0
  var PANEL_SKY_ZONE = 0

  def start(): Unit = {
    val baseAppView = getClass.getResource("/fxml/baseApp.fxml")
    if (baseAppView == null) {
      throw new IOException("Cannot load resource: baseApp.fxml")
    }

    val loader = new FXMLLoader(baseAppView, NoDependencyResolver)
    loader.load()
    val root = loader.getRoot[jfxs.Parent]
    baseAppController = Some(loader.getController[BaseAppControllerInterface])

    val bounds = Screen.getPrimary.getVisualBounds
    val sceneWidth = if (bounds.getWidth > 1500) 1500 else bounds.getWidth - 500
    val sceneHeigth = if (bounds.getHeight > 800) 800 else bounds.getHeight - 200
    PREFERRED_BUNNY_PANEL_WIDTH = (sceneWidth * 0.6).toInt
    PREFERRED_BUNNY_PANEL_HEIGHT = (sceneHeigth * 0.25).toInt
    PANEL_SKY_ZONE = (sceneHeigth * 0.1).toInt
    stage = new PrimaryStage() {
      title = "Bunnies"
      scene = new Scene(root)
      width = sceneWidth
      height = sceneHeigth
    }
    stage.setResizable(false)
    baseAppController.get.initialize()
  }

  def showPopulation(bunnies: Population, generationNumber: Int): Unit = {
    Platform.runLater{baseAppController.get.showBunnies(bunnies, generationNumber)}
  }
}
