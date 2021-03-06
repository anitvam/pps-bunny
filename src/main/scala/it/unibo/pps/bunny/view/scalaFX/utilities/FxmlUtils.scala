package it.unibo.pps.bunny.view.scalaFX.utilities

import javafx.scene.{ layout => jfxs }
import scalafx.Includes._
import scalafx.scene.Node
import scalafx.scene.layout.AnchorPane
import scalafxml.core.{ FXMLLoader, NoDependencyResolver }

import java.io.IOException

object FxmlUtils {

  /**
   * Method that loads a fxml document of type T
   *
   * @param fxmlPath
   *   the path where the fxml is located
   * @return
   *   (T, FXMLLoader) a pair with the loaded panel and its loader
   * @tparam T
   *   the type of the panel loaded
   */
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

  /**
   * Method that loads a fxml from the specified path and applies the consumer
   * @param fxmlPath
   *   a [[String]] representing the fxml path
   * @param paneConsumer
   *   a [[(AnchorPane) => Unit]] function that is applied to the loaded pane
   * @tparam T
   *   the type of the panel loaded
   */
  def loadPanelAndGetController[T](fxmlPath: String, paneConsumer: AnchorPane => Unit): Option[T] = {
    val loadedMutationChoicePanel = loadFXMLResource[jfxs.AnchorPane](fxmlPath)
    paneConsumer(loadedMutationChoicePanel._1)
    Some(loadedMutationChoicePanel._2.getController[T])
  }

  /**
   * Method that sets the 4 anchors to zero of an AnchorPane's children
   *
   * @param node
   *   the node on which anchors are set
   */
  def setFitParent(node: Node): Unit = AnchorPane.setAnchors(node, 0, 0, 0, 0)

}
