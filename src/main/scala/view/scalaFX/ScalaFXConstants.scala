package view.scalaFX

import javafx.stage.Screen

object ScalaFXConstants {
  private val SCREEN_BOUNDS = Screen.getPrimary.getVisualBounds
  private val DEFAULT_SCENE_WIDTH = 1500
  private val DEFAULT_SCENE_HEIGHT = 900
  private val WIDTH_SCREEN_BOUND = 300
  private val HEIGHT_SCREEN_BOUND = 60
  private val BUNNY_PANEL_PERCENTUAL_WIDTH = 0.6
  private val BUNNY_PANEL_PERCENTUAL_HEIGTH = 0.25
  private val BUNNY_PANEL_PERCENTUAL_SKY_ZONE = 0.1

  /** Normal Jump height */
  val NORMAL_JUMP_HEIGHT = 40

  /** High Jump height */
  val HIGH_JUMP_HEIGHT = 80

  /** Number of wolves to show */
  val WOLVES_NUMBER = 5

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
  val PREFERRED_SIMULATION_PANEL_WIDTH: Int = (SCENE_WIDTH * BUNNY_PANEL_PERCENTUAL_WIDTH).toInt

  /** Bunny panel inside application window height */
  val PREFERRED_BUNNY_PANEL_HEIGHT: Int = (SCENE_HEIGHT * BUNNY_PANEL_PERCENTUAL_HEIGTH).toInt

  /** Wolf panel zone inside application window height */
  val PREFERRED_WOLF_PANEL_HEIGHT: Int = PREFERRED_BUNNY_PANEL_HEIGHT - 100

  val PREFERRED_SIMULATION_PANEL_BORDER: Int = 70

  /** Bunny panel bound for the sky zone */
  val PANEL_SKY_ZONE: Int = (SCENE_HEIGHT * BUNNY_PANEL_PERCENTUAL_SKY_ZONE).toInt

  val PREFERRED_CHART_WIDTH: Int = (SCENE_WIDTH * 0.55).toInt

  val PREFERRED_CHART_HEIGHT: Int = (SCENE_HEIGHT * 0.45).toInt

  /** Constants for the tree visualizaiton */
  object GenealogicalTree {

    /** Max size of the bunny picture in the tree */
    val MAX_TREE_BUNNY_SIZE: Int = 80

    /** Min size of the bunny picture in the tree */
    val MIN_TREE_BUNNY_SIZE: Int = 46

    /** Proportion constants to resize the view of the info size in the tree depending on the bunny size */
    val BUNNY_INFO_PROPORTION: Double = 4

    /** Proportion constants to resize the plus size in the tree depending on the bunny size */
    val BUNNY_PLUS_PROPORTION: Int = 2

    /** Percentual constants to resize the font depending on the info proportion */
    val FONT_INFO_PERCENT: Double = 0.8

    /** The padding between the bunny and its alleles */
    val BUNNY_ALLELE_PADDING = 3
  }

  object PopulationChart {

    val AXIS_LOWER_BOUND: Int = 0

    val X_AXIS_UPPER_BOUND: Int = 6

    val Y_AXIS_UPPER_BOUND: Int = 30

    val X_AXIS_TICK: Int = 1

    val Y_AXIS_TICK: Int = 5

  }

  object Style {

    object PopulationLegend {
      val ITEM_STYLE: String = "population-chart-legend-item"
      val CLICKED_ITEM_STYLE: String = "population-chart-legend-item-clicked"
    }

    object MutationsChoice {
      val CHOSEN_BUTTON_STYLE: String = "chosen-button"
      val OTHER_BUTTON_STYLE: String = "dashed-button"
    }

  }

}
