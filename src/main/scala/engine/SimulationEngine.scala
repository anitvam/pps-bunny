package engine

import cats.effect.IO
import engine.GenerationTimer.{resetTimer, waitFor}
import engine.Simulation.{getGenerationNumber, _}
import engine.SimulationConstants._
import view.View

object SimulationEngine {
  def simulationLoop(): IO[Unit] = {
    for {
      _ <- resetTimer
      population <- startNewGeneration
      _ <- showNewPopulation(population)
      _ <- waitFor(WOLF_INSTANT)
      _ <- wolvesEat
      _ <- waitFor(FOOD_INSTANT)
      _ <- bunniesEat
      _ <- waitFor(TEMP_INSTANT)
      _ <- applyTemperatureDamage
      _ <- waitFor(GEN_END)
      _ <- if(getGenerationNumber < MAX_GENERATIONS_NUMBER &&
        getBunniesNumber < MAX_BUNNIES_NUMBER) simulationLoop() else showEnd()
    }yield()
  }
}
