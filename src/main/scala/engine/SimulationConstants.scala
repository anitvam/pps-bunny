package engine

import scala.concurrent.duration.{DurationInt, FiniteDuration}

object SimulationConstants {
  /**After how many milliseconds from the start of generation the wolves can eat */
  val WOLF_INSTANT: FiniteDuration = 3000 millis

  /**After how many milliseconds from the start of generation the bunnies can eat */
  val FOOD_INSTANT: FiniteDuration = 6000 millis

  /**After how many milliseconds from the start of generation
   * the bunnies are affected by high temperatures */
  val TEMP_INSTANT: FiniteDuration = 9000 millis

  /**Duration in milliseconds of a generation*/
  val GEN_END: FiniteDuration = 12000 millis

  /**Maximum number of alive bunnies in the world*/
  val MAX_BUNNIES_NUMBER = 500

  /**Maximum number of generations in a simulation*/
  val MAX_GENERATIONS_NUMBER = 1000

  /**Number of children for each couple of bunnies*/
  val CHILDREN_EACH_COUPLE = 4

  /**Maximum number of generation which each bunny can live*/
  val MAX_BUNNY_AGE = 4
}