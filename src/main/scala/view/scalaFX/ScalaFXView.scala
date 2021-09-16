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
import view.scalaFX.ScalaFxViewConstants.{SCENE_HEIGHT, SCENE_WIDTH}

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

object ScalaFxViewConstants {
  private val SCREEN_BOUNDS = Screen.getPrimary.getVisualBounds
  private val DEFAULT_SCENE_WIDTH = 1500
  private val DEFAULT_SCENE_HEIGHT = 900
  private val WIDTH_SCREEN_BOUND = 300
  private val HEIGHT_SCREEN_BOUND = 60
  private val BUNNY_PANEL_PERCENTUAL_WIDTH = 0.6
  private val BUNNY_PANEL_PERCENTUAL_HEIGTH = 0.25
  private val BUNNY_PANEL_PERCENTUAL_SKY_ZONE = 0.1

  /** Size of the bunny picture */
  val PREFERRED_BUNNY_SIZE = 80

  /** Normal Jump height */
  val NORMAL_JUMP_HEIGHT = 40

  /** High Jump height */
  val HIGH_JUMP_HEIGHT = 80

  /** Random bound on jump delay */
  val RANDOM_BUNNY_JUMP_DELAY = 5000

  /** Standard delay on each bunny jump */
  val STANDARD_BUNNY_JUMP_DURATION = 1000

  /** Application window width */
  val SCENE_WIDTH: Double = if (SCREEN_BOUNDS.getWidth > DEFAULT_SCENE_WIDTH) DEFAULT_SCENE_WIDTH else SCREEN_BOUNDS.getWidth - WIDTH_SCREEN_BOUND

  /** Application window height */
  val SCENE_HEIGHT: Double = if (SCREEN_BOUNDS.getHeight > DEFAULT_SCENE_HEIGHT) DEFAULT_SCENE_HEIGHT else SCREEN_BOUNDS.getHeight - HEIGHT_SCREEN_BOUND

  /** Bunny panel inside application window width */
  var PREFERRED_BUNNY_PANEL_WIDTH: Int = (SCENE_WIDTH * BUNNY_PANEL_PERCENTUAL_WIDTH).toInt

  /** Bunny panel inside application window height */
  var PREFERRED_BUNNY_PANEL_HEIGHT: Int = (SCENE_HEIGHT * BUNNY_PANEL_PERCENTUAL_HEIGTH).toInt

  /** Bunny panel bound for the sky zone */
  var PANEL_SKY_ZONE: Int = (SCENE_HEIGHT * BUNNY_PANEL_PERCENTUAL_SKY_ZONE).toInt

}
