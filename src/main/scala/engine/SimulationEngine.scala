package engine

import cats.effect.IO
import engine.GenerationTimer.{resetTimer, waitFor}
import engine.Simulation._
import engine.SimulationConstants._
import engine.SimulationHistory.{getBunniesNumber, getGenerationNumber}

object SimulationEngine {

  def simulationLoop(): IO[Unit] = {
    for {
      _ <- resetTimer
      _ <- updateView(0)
      _ <- waitFor(WOLF_INSTANT)
      _ <- wolvesEat
      _ <- updateView(0.25)
      _ <- waitFor(FOOD_INSTANT)
      _ <- bunniesEat
      _ <- updateView(0.5)
      _ <- waitFor(TEMP_INSTANT)
      _ <- applyTemperatureDamage
      _ <- updateView(0.75)
      _ <- waitFor(GENERATION_END)
      _ <- startNewGeneration
      _ <- if(getGenerationNumber < MAX_GENERATIONS_NUMBER &&
        getBunniesNumber < MAX_BUNNIES_NUMBER) simulationLoop() else showEnd()
    } yield()
  }
}
