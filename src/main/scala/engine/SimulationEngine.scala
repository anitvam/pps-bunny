package engine

import cats.effect.IO
import engine.GenerationTimer.{resetTimer, waitFor}
import engine.Simulation._
import engine.SimulationConstants._
import engine.SimulationHistory.{getBunniesNumber, getGenerationNumber}
import model.world.GenerationsUtils.{FoodPhase, HighTemperaturePhase, StartPhase, WolvesPhase}

object SimulationEngine {

  def simulationLoop(): IO[Unit] = {
    for {
      _ <- resetTimer
      _ <- updateView(StartPhase(getGenerationNumber))
      _ <- waitFor(WOLF_INSTANT)
      _ <- wolvesEat
      _ <- updateView(WolvesPhase(getGenerationNumber))
      _ <- waitFor(FOOD_INSTANT)
      _ <- bunniesEat
      _ <- updateView(FoodPhase(getGenerationNumber))
      _ <- waitFor(TEMP_INSTANT)
      _ <- applyTemperatureDamage
      _ <- updateView(HighTemperaturePhase(getGenerationNumber))
      _ <- waitFor(GENERATION_END)
      _ <- startNewGeneration
      _ <- if(getGenerationNumber < MAX_GENERATIONS_NUMBER &&
        getBunniesNumber < MAX_ALIVE_BUNNIES) simulationLoop() else showEnd()
    } yield()
  }
}
