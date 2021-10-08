package engine

import cats.effect.IO
import controller.Controller
import engine.SimulationHistory._
import model.world.GenerationsUtils.GenerationPhase
import model.world.disturbingFactors.FactorTypes._
import util.PimpScala.RichOption
import model.world.disturbingFactors.PimpFactors._
import view.scalaFX.ScalaFXView

import scala.language.implicitConversions

object Simulation {


  def wolvesEat: IO[Unit] = applyFactorDamage(WolvesFactorKind)


  def bunniesEat: IO[Unit] = applyFactorDamage(FoodFactorKind)


  def applyTemperatureDamage: IO[Unit] = applyFactorDamage(UnfriendlyClimateFactorKind)


  def updateView(generationPhase: GenerationPhase): IO[Unit] =
    ScalaFXView.updateView(generationPhase, getActualPopulation)

  def overpopulation(): IO[Unit] = {
    Controller.showEnd(Overpopulation)
  }

  def extinction(): IO[Unit] = {
    Controller.showEnd(Extinction)
  }

  def end(): IO[Unit] = {
    Controller.showEnd(GenerationsOverload)
  }

  def startNewGeneration: IO[Unit] = SimulationHistory.startNextGeneration()

  implicit def unitToIO(exp: => Unit): IO[Unit] = IO { exp }

  private def applyFactorDamage(factorKind: FactorKind): Unit =
    getActualGeneration.environment.factors.getFactor(factorKind) --> { f =>
      getActualGeneration.population = f applyDamage (getActualPopulation, getActualGeneration.environment.climate)
    }

}

sealed trait SimulationEndType
case object Overpopulation extends SimulationEndType
case object Extinction extends SimulationEndType
case object GenerationsOverload extends SimulationEndType
