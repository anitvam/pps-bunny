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
  val MAX_BUNNIES_NUMBER = 50

  /**Maximum number of generations*/
  val MAX_GENERATIONS_NUMBER = 1000

}
