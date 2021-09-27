package view.scalaFX

import controller.ScalaFXLauncher.stage
import javafx.stage.Screen
import javafx.{ scene => jfxs }
import model.world.Generation.Population
import model.world.GenerationsUtils.GenerationPhase
import scalafx.Includes._
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.application.Platform
import scalafx.scene.Scene
import scalafx.scene.image.{ Image, ImageView }
import scalafx.scene.layout.AnchorPane
import scalafx.stage.Stage
import util.PimpScala._
import view._
import view.scalaFX.FXControllers.BaseAppControllerInterface
import view.scalaFX.ScalaFxViewConstants.{ SCENE_HEIGHT, SCENE_WIDTH }
import view.scalaFX.components.BunnyView
import view.scalaFX.components.charts.PopulationChart
import view.scalaFX.utilities.FxmlUtils

object ScalaFXView extends View {
  var baseAppController: Option[BaseAppControllerInterface] = Option.empty

  def start(): Unit = {
    val loadedRootPanel = FxmlUtils.loadFXMLResource[jfxs.Parent]("/fxml/baseApp.fxml")
    baseAppController = Some(loadedRootPanel._2.getController[BaseAppControllerInterface])
    baseAppController --> { _.initialize() }

    stage = new PrimaryStage {
      title = "Bunnies"
      scene = new Scene(loadedRootPanel._1)
      width = SCENE_WIDTH
      height = SCENE_HEIGHT
      resizable = false
    }
  }

  def updateView(generationPhase: GenerationPhase, bunnies: Population): Unit = Platform.runLater {
    baseAppController --> { _.showBunnies(bunnies, generationPhase) }
    PopulationChart.updateChart(generationPhase, bunnies)
  }

  override def showEnd(): Unit = {

    val endStage = new Stage {
      title = "Fine simulazione"
      scene = new Scene(new AnchorPane {
        children = new ImageView {
          image = new Image("img/world.png")
          fitHeight = ScalaFxViewConstants.PREFERRED_CHART_HEIGHT
          preserveRatio = true
        }
      })
      resizable = false
    }
    endStage.show()
  }

  override def handleBunnyClick(bunny: BunnyView): Unit = baseAppController --> { _.handleBunnyClick(bunny) }
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
  val SCENE_WIDTH: Double =
    if (SCREEN_BOUNDS.getWidth > DEFAULT_SCENE_WIDTH) DEFAULT_SCENE_WIDTH
    else SCREEN_BOUNDS.getWidth - WIDTH_SCREEN_BOUND

  /** Application window height */
  val SCENE_HEIGHT: Double =
    if (SCREEN_BOUNDS.getHeight > DEFAULT_SCENE_HEIGHT) DEFAULT_SCENE_HEIGHT
    else SCREEN_BOUNDS.getHeight - HEIGHT_SCREEN_BOUND

  /** Bunny panel inside application window width */
  val PREFERRED_BUNNY_PANEL_WIDTH: Int = (SCENE_WIDTH * BUNNY_PANEL_PERCENTUAL_WIDTH).toInt

  /** Bunny panel inside application window height */
  val PREFERRED_BUNNY_PANEL_HEIGHT: Int = (SCENE_HEIGHT * BUNNY_PANEL_PERCENTUAL_HEIGTH).toInt

  /** Bunny panel bound for the sky zone */
  val PANEL_SKY_ZONE: Int = (SCENE_HEIGHT * BUNNY_PANEL_PERCENTUAL_SKY_ZONE).toInt

  val PREFERRED_CHART_WIDTH: Int = (SCENE_WIDTH * 0.55).toInt

  val PREFERRED_CHART_HEIGHT: Int = (SCENE_HEIGHT * 0.45).toInt

  /** Constants for the tree visualizaiton */
  object GenealogicalTree {

    /** Size of the bunny picture in the tree */
    val MAX_TREE_BUNNY_SIZE: Int = 80

    /** Proportion constants to resize the view of the info size in the tree depending on the bunny size */
    val TREE_INFO_PROPORTION: Int = 4

    /** Proportion constants to resize the plus size in the tree depending on the bunny size */
    val TREE_PLUS_PROPORTION: Int = 2

    /** Percentual constants to resize the font depending on the plus size */
    val FONT_INFO_PERCENT: Double = 0.8

    /** The padding between the bunny and its alleles */
    val BUNNY_ALLELE_PADDING = 3
  }
}
