package it.unibo.pps.bunny.engine

import scala.language.postfixOps

object SimulationConstants {

  /** Constants for the speed of the simulation */
  val DEFAULT_SPEED: Double = 1
  val TWO_PER_SPEED: Double = 0.5
  val FOUR_PER_SPEED: Double = 0.25

  /** Double value associated to the deviation period to show the wolves in the gui */
  val WOLVES_INSTANT_DEVIATION: Double = 0.15

  /** Maximum number of alive bunnies in the world */
  val MAX_ALIVE_BUNNIES: Int = 1000

  /** Minimum number of alive bunnies in the world */
  val MIN_ALIVE_BUNNIES: Int = 1

  /** Maximum number of generations in a simulation */
  val MAX_GENERATIONS_NUMBER: Int = 1000

  /** Number of children for each couple of bunnies */
  val CHILDREN_FOR_EACH_COUPLE: Int = 4

  /** Maximum number of generation which each bunny can live */
  val MAX_BUNNY_AGE: Int = 4

  /** Number of generations in a genealogical tree */
  val MAX_GENEALOGICAL_TREE_GENERATIONS: Int = 4

  /** Number of phases in the loop of simulation */
  val NUMBER_OF_PHASES: Int = 4

  object PhasesConstants {

    /** Double value associated to generation initial phase */
    val REPRODUCTION_PHASE: Double = 0.0

    /** Double value associated to generation wolf phase */
    val WOLVES_PHASE: Double = 0.25

    /** Double value associated to generation food phase */
    val FOOD_PHASE: Double = 0.50

    /** Double value associated to generation temp phase */
    val TEMPERATURE_PHASE = 0.75

    /** After how many milliseconds from the start of generation the wolves can eat */
    val WOLVES_INSTANT: Double = 3000

    /** After how many milliseconds from the start of generation the bunnies can eat */
    val FOOD_INSTANT: Double = 6000

    /** After how many milliseconds from the start of generation the bunnies are affected by high temperatures */
    val TEMP_INSTANT: Double = 9000

    /** Duration in milliseconds of a generation */
    val GENERATION_END: Double = 12000
  }

  object FactorsConstants {

    /** Food factors damage to bunnies */
    val FOOD_FACTOR_NORMAL_DAMAGE = 0.8
    val FOOD_FACTOR_LOW_DAMAGE = 0.6

    /** Unfriendly climate damage to bunnies */
    val UNFRIENDLY_CLIMATE_DAMAGE = 0.6

    /** Wolf damages to bunnies */
    val WOLF_LOW_DAMAGE = 0.6
    val WOLF_MEDIUM_DAMAGE = 0.7
    val WOLF_HIGH_DAMAGE = 0.8
  }

}
