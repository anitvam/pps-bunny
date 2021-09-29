package engine

import cats.effect.IO
import engine.SimulationConstants.NUMBER_OF_WOLVES
import engine.SimulationHistory._
import model.world.GenerationsUtils.GenerationPhase
import view.scalaFX.ScalaFXView
import view.scalaFX.ScalaFXView.showWolves

import scala.language.implicitConversions

object Simulation {

  def wolvesEat: IO[Unit] =
    if(getActualGeneration.environment.factors.contains(DisturbingFactors.WOLF))
      showWolves(NUMBER_OF_WOLVES)


  def bunniesEat: IO[Unit] = {
    println("BUNNIES ARE EATING")
  }

  def applyTemperatureDamage : IO[Unit] = {
    if(getActualGeneration.environment.factors.contains(DisturbingFactors.HOSTILE_TEMPERATURE)) println("SOME BUNNIES DIED BECAUSE OF TEMPERATURE")
  }

  def updateView(generationPhase:GenerationPhase) : IO[Unit] = {
    ScalaFXView.updateView(generationPhase, getActualPopulation)
  }

  def showEnd(generationPhase:GenerationPhase):IO[Unit] = {
    ScalaFXView.updateView(generationPhase, getActualPopulation)
    controller.Controller.showEnd()
  }

  def startNewGeneration: IO[Unit] = {
    SimulationHistory.startNextGeneration()
  }

  implicit def unitToIO(exp: => Unit) : IO[Unit] = IO{exp}
}
