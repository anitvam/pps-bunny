package it.unibo.pps.bunny.engine

import cats.effect.IO
import it.unibo.pps.bunny.controller.Controller
import it.unibo.pps.bunny.engine.SimulationHistory.{ getActualGeneration, getActualPopulation }
import it.unibo.pps.bunny.model.world.GenerationsUtils.GenerationPhase
import it.unibo.pps.bunny.model.world.disturbingFactors._
import it.unibo.pps.bunny.util.PimpScala.RichOption
import it.unibo.pps.bunny.view.scalaFX.ScalaFXView

import scala.language.implicitConversions

object Simulation {

  def wolvesEat: IO[Unit] = applyFactorDamage(WolvesFactorKind)

  def bunniesEat: IO[Unit] = applyFactorDamage(FoodFactorKind)

  def applyTemperatureDamage: IO[Unit] = applyFactorDamage(UnfriendlyClimateFactorKind)

  def updateView(generationPhase: GenerationPhase): IO[Unit] =
    ScalaFXView.updateView(generationPhase, getActualPopulation)

  def overpopulation(): IO[Unit] = {
    Controller.showEnd(true)
  }

  def extinction(): IO[Unit] = {
    Controller.showEnd(false)
  }

  def startNewGeneration: IO[Unit] = {
    SimulationHistory.startNextGeneration()
  }

  implicit def unitToIO(exp: => Unit): IO[Unit] = IO { exp }

  private def applyFactorDamage(factorKind: FactorKind): Unit =
    getActualGeneration.environment.factors.getFactor(factorKind) --> { f =>
      getActualGeneration.population = f applyDamage (getActualPopulation, getActualGeneration.environment.climate)
    }

}
