package view.scalaFX

import controller.ScalaFXLauncher.stage
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import view.scalaFX.FXControllers.BaseAppControllerInterface
import scalafx.Includes._
import javafx.{scene => jfxs}
import model.Bunny

import java.io.IOException
import view._

class ScalaFXView() extends View {
  var baseAppController: Option[BaseAppControllerInterface] = None

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
  }

  def showPopulation(bunnies: Seq[Bunny]): Unit = {
    baseAppController.get.initialize(bunnies)
  }
}
