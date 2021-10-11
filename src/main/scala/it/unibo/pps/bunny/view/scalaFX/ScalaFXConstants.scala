package it.unibo.pps.bunny.view.scalaFX

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

  /** Bunny panel border */
  val PREFERRED_SIMULATION_PANEL_BORDER: Int = 70

  /** Bunny panel bound for the sky zone */
  val PANEL_SKY_ZONE: Int = (SCENE_HEIGHT * BUNNY_PANEL_PERCENTUAL_SKY_ZONE).toInt

  val PREFERRED_CHART_WIDTH: Int = (SCENE_WIDTH * 0.55).toInt

  val PREFERRED_CHART_HEIGHT: Int = (SCENE_HEIGHT * 0.45).toInt

  /** Constants for the tree visualization */
  object GenealogicalTree {

    /** Max size in px of the bunny picture in the tree */
    val MAX_TREE_BUNNY_SIZE: Int = 80

    /** Min size in px of the bunny picture in the tree */
    val MIN_TREE_BUNNY_SIZE: Int = 46

    /** Proportion constants to resize the view of the info size in the tree depending on the bunny size */
    val BUNNY_INFO_PROPORTION: Double = 3.5

    /** Proportion constants to resize the plus size in the tree depending on the bunny size */
    val BUNNY_PLUS_PROPORTION: Int = 2

    /** Proportion constants to resize the font depending on the info proportion */
    val BUNNY_FONT_PROPORTION : Double = 5.5

    /** Padding size in px for the chosen bunny in the tree */
    val CHOSEN_BUNNY_PADDING : Int = 5

    /** Border size in px for the chosen bunny in the tree */
    val CHOSEN_BUNNY_BORDER : Int = 1

    /** Padding size in px for the tree inside its panel */
    val EXTERNAL_PADDING: Int = 5

    /** Additional space in each direction caused by the external padding and the chosen bunny selection*/
    val ADDITIONAL_SPACE: Int = (CHOSEN_BUNNY_PADDING + CHOSEN_BUNNY_BORDER + EXTERNAL_PADDING) * 2
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

  object Wolf {
    /** Wolf panel zone inside application window height */
    val PREFERRED_WOLF_PANEL_HEIGHT: Int = PREFERRED_BUNNY_PANEL_HEIGHT - 100

    /** Number of wolves to show */
    val WOLVES_NUMBER: Int = 5

    val WOLVES_SPEED: Int = 200
    val WOLVES_MOVING_SPACE: Int = PREFERRED_SIMULATION_PANEL_WIDTH / WOLVES_SPEED
  }
}
