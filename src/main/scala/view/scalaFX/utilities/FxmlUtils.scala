package view.scalaFX.utilities

import scalafxml.core.{FXMLLoader, NoDependencyResolver}

import java.io.IOException
import javafx.scene.{layout => jfxs}
import scalafx.scene.layout.AnchorPane
import scalafx.Includes._
import scalafx.scene.Node


object FxmlUtils {

  /** Method that loads an fxml document of type T
   * @param fxmlPath the path where the fxml is located
   * @return (T, FXMLLoader) a pair with the loaded panel and its loader*/
  def loadFXMLResource[T](fxmlPath: String): (T, FXMLLoader) = {
    val fxmlFile = getClass.getResource(fxmlPath)
    if (fxmlFile == null) {
      throw new IOException("Cannot load resource: " + fxmlPath)
    }
    val loader = new FXMLLoader(fxmlFile, NoDependencyResolver)
    loader.load()
    val loadedPane = loader.getRoot[T]

    loadedPane match {
      case node: jfxs.AnchorPane => setFitParent(node)
    }

    (loadedPane, loader)
  }

  /** Methos that sets the 4 anchors to zero of an AnchorPane's children
   * @param node the node on which anchors are set */
  def setFitParent(node: Node): Unit = AnchorPane.setAnchors(node, 0, 0, 0, 0)

}
