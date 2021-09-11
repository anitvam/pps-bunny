package engine

import cats.effect.IO
import engine.SimulationHistory._
import view.scalaFX.ScalaFXView

import scala.language.implicitConversions

object Simulation {

  def wolvesEat: IO[Unit] = {
    println("WOLVES ARE EATING")
  }

  def bunniesEat: IO[Unit] = {
    println("BUNNIES ARE EATING")
  }

  def applyTemperatureDamage : IO[Unit] = {
    println("SOME BUNNIES DIED BECAUSE OF TEMPERATURE")
  }

  def updateView(generationPhase:Double) : IO[Unit] = {
    ScalaFXView.updateView(getGenerationNumber+generationPhase, getActualPopulation)
  }

  def showEnd():IO[Unit] = {
    println("END")
  }

  def startNewGeneration: IO[Unit] = {
    SimulationHistory.startNextGeneration()
  }

  implicit def unitToIO(exp: => Unit) : IO[Unit] = IO{exp}
}
