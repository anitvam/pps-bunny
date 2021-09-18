package view.scalaFX.utilities

import scalafxml.core.{FXMLLoader, NoDependencyResolver}

import java.io.IOException
import javafx.scene.{layout => jfxs}
import scalafx.scene.layout.AnchorPane
import scalafx.Includes._


object FxmlUtils {

  def loadFXMLResource[T](fxmlPath: String): (T, FXMLLoader) = {
    val fxmlFile = getClass.getResource(fxmlPath)
    if (fxmlFile == null) {
      throw new IOException("Cannot load resource: " + fxmlPath)
    }
    val loader = new FXMLLoader(fxmlFile, NoDependencyResolver)
    loader.load()
    val loadedPane = loader.getRoot[T]

    loadedPane match {
      case node: jfxs.AnchorPane => AnchorPane.setAnchors(node, 0, 0, 0, 0)
    }

    (loadedPane, loader)
  }

}
