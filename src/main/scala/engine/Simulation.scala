package engine

import cats.effect.IO
import controller.Controller
import engine.SimulationHistory._
import model.world.Factor._
import model.world.FoodFactor
import model.world.GenerationsUtils.GenerationPhase
import view.scalaFX.ScalaFXView

import scala.language.implicitConversions

object Simulation {

  def wolvesEat: IO[Unit] = {
    val wolvesFactor = getActualGeneration.environment.factors.filter(_.isInstanceOf[Wolves])
    if (wolvesFactor.nonEmpty) getActualGeneration.population =
      wolvesFactor.head.applyDamage(getActualPopulation, getActualGeneration.environment.climate)
  }

  def bunniesEat: IO[Unit] = {
    val foodFactor = getActualGeneration.environment.factors.filter(_.isInstanceOf[FoodFactor])
    if (foodFactor.nonEmpty) getActualGeneration.population =
      foodFactor.head.applyDamage(getActualPopulation, getActualGeneration.environment.climate)
  }

  def applyTemperatureDamage: IO[Unit] = {
    val temperatureFactor = getActualGeneration.environment.factors.filter(_.isInstanceOf[UnfriendlyClimate])
    if (temperatureFactor.nonEmpty) getActualGeneration.population =
      temperatureFactor.head.applyDamage(getActualPopulation, getActualGeneration.environment.climate)
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
