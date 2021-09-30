package engine

import cats.effect.IO
import controller.Controller
import engine.SimulationHistory._
import model.world.Factor._
import model.world.GenerationsUtils.GenerationPhase
import view.scalaFX.ScalaFXView

import scala.language.implicitConversions

object Simulation {

  def wolvesEat: IO[Unit] = {
//    if (getActualGeneration.factors.contains(DisturbingFactors.WOLF)) ScalaFXView.showWolves(NUMBER_OF_WOLVES)
    val wolves = Wolves()
    SimulationHistory.getActualGeneration.population = wolves
      .applyDamage(SimulationHistory.getActualPopulation, SimulationHistory.getActualGeneration.environment.climate)
  }

  def bunniesEat: IO[Unit] = {
    val limitedFood = UnfriendlyClimate()
    SimulationHistory.getActualGeneration.population = limitedFood
      .applyDamage(SimulationHistory.getActualPopulation, SimulationHistory.getActualGeneration.environment.climate)
  }

  def applyTemperatureDamage: IO[Unit] = {
    println("SOME BUNNIES DIED BECAUSE OF TEMPERATURE")
  }

  def updateView(generationPhase: GenerationPhase): IO[Unit] = {
    ScalaFXView.updateView(generationPhase, getActualPopulation)
  }

  def showEnd(generationPhase: GenerationPhase): IO[Unit] = {
    ScalaFXView.updateView(generationPhase, getActualPopulation)
    Controller.showEnd()
  }

  def startNewGeneration: IO[Unit] = {
    SimulationHistory.startNextGeneration()
  }

  implicit def unitToIO(exp: => Unit): IO[Unit] = IO { exp }
}
