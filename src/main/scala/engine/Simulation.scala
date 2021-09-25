package engine

import cats.effect.IO
import engine.SimulationHistory._
<<<<<<< HEAD
=======
import model.world.Factor
import model.world.Factor._
>>>>>>> Test new Factor implementation inside the engine
import model.world.GenerationsUtils.GenerationPhase
import view.scalaFX.ScalaFXView

import scala.language.implicitConversions

object Simulation {

  def wolvesEat: IO[Unit] = {
<<<<<<< HEAD
    println("WOLVES ARE EATING")
  }

  def bunniesEat: IO[Unit] = {
    println("BUNNIES ARE EATING")
  }

  def applyTemperatureDamage: IO[Unit] = {
    println("SOME BUNNIES DIED BECAUSE OF TEMPERATURE")
  }

  def updateView(generationPhase: GenerationPhase): IO[Unit] = {
    ScalaFXView.updateView(generationPhase, getActualPopulation)
  }

  def showEnd(generationPhase: GenerationPhase): IO[Unit] = {
=======
    val wolves = Wolves()
    SimulationHistory.getActualGeneration.population = wolves.applyDamage(SimulationHistory.getActualPopulation, SimulationHistory.getActualGeneration.environment.climate)

  }

  def bunniesEat: IO[Unit] = {
    val limitedFood = UnfriendlyClimate()
    SimulationHistory.getActualGeneration.population = limitedFood.applyDamage(SimulationHistory.getActualPopulation, SimulationHistory.getActualGeneration.environment.climate)
  }

  def applyTemperatureDamage : IO[Unit] = {
    println("SOME BUNNIES DIED BECAUSE OF TEMPERATURE")
  }

  def updateView(generationPhase:GenerationPhase) : IO[Unit] = {
    ScalaFXView.updateView(generationPhase, getActualPopulation)
  }

  def showEnd(generationPhase:GenerationPhase):IO[Unit] = {
>>>>>>> Test new Factor implementation inside the engine
    ScalaFXView.updateView(generationPhase, getActualPopulation)
    controller.Controller.showEnd()
  }

  def startNewGeneration: IO[Unit] = {
    SimulationHistory.startNextGeneration()
  }

<<<<<<< HEAD
  implicit def unitToIO(exp: => Unit): IO[Unit] = IO { exp }
=======
  implicit def unitToIO(exp: => Unit) : IO[Unit] = IO{exp}
>>>>>>> Test new Factor implementation inside the engine
}
