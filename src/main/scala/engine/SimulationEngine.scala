package engine

import cats.effect.IO
import engine.GenerationTimer.{resetTimer, waitFor}
import engine.Simulation.{applyTemperatureDamage, bunniesEat, getBunniesNumber, getGenerationNumber, showBunnies, startNewGeneration, wolfsEat}
import engine.SimulationConstants.{FOOD_INSTANT, GEN_END, MAX_BUNNIES_NUMBER, MAX_GENERATIONS_NUMBER, TEMP_INSTANT, WOLF_INSTANT}

object SimulationEngine {
  def simulationLoop(): IO[Unit] = {
    for {
      _ <- resetTimer
      _ <- startNewGeneration
      _ <- showBunnies
      _ <- waitFor(WOLF_INSTANT)
      _ <- wolfsEat
      _ <- waitFor(FOOD_INSTANT)
      _ <- bunniesEat
      _ <- waitFor(TEMP_INSTANT)
      _ <- applyTemperatureDamage
      _ <- waitFor(GEN_END)
      i <- getGenerationNumber
      b <- getBunniesNumber
      _ <- if(i < MAX_GENERATIONS_NUMBER && b < MAX_BUNNIES_NUMBER) simulationLoop() else showBunnies
    }yield()
  }
}
