package bunny.engine

import cats.effect.IO
import bunny.controller.Controller
import bunny.engine.SimulationHistory._
import bunny.model.world.disturbingFactors.FactorTypes._
import bunny.model.world.GenerationsUtils.GenerationPhase
import bunny.util.PimpScala.RichOption
import bunny.view.scalaFX.ScalaFXView

import scala.language.implicitConversions

object Simulation {

  def wolvesEat: IO[Unit] = applyFactorDamage(WolvesFactorKind)

  def bunniesEat: IO[Unit] = applyFactorDamage(FoodFactorKind)

  def applyTemperatureDamage: IO[Unit] = applyFactorDamage(UnfriendlyClimateFactorKind)

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

  private def applyFactorDamage(factorKind: FactorKind): Unit =
    getActualGeneration.environment.factors.getFactor(factorKind) --> { f =>
      getActualGeneration.population = f applyDamage (getActualPopulation, getActualGeneration.environment.climate)
    }

}
