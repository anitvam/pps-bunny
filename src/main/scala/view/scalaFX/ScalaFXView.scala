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

class ScalaFXView extends View{
  def start(bunnies: Set[Bunny]): Unit = {
    val baseAppView = getClass.getResource("/fxml/baseApp.fxml")
    if (baseAppView == null) {
      throw new IOException("Cannot load resource: baseApp.fxml")
    }

    val loader = new FXMLLoader(baseAppView, NoDependencyResolver)
    loader.load()
    val root = loader.getRoot[jfxs.Parent]
    val rootController = loader.getController[BaseAppControllerInterface]
    rootController.initialize(bunnies)

    stage = new PrimaryStage() {
      title = "Bunnies"
      scene = new Scene(root)
    }
    stage.setResizable(false)
  }
}
