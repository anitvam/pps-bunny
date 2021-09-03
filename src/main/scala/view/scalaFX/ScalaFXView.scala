package view.scalaFX

import controller.ScalaFXLauncher.stage
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import view.scalaFX.FXControllers.BaseAppControllerInterface
import scalafx.Includes._
import javafx.{scene => jfxs}
import model.world.Generation.Population
import scalafx.application.Platform

import java.io.IOException
import view._

object ScalaFXView extends View {
  var baseAppController: Option[BaseAppControllerInterface] = Option.empty

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
    }
    stage.setResizable(false)
    baseAppController.get.initialize()
  }

  def showPopulation(bunnies: Population): Unit = {
    Platform.runLater{baseAppController.get.showBunnies(bunnies)}
  }
}
