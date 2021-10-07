package engine

import cats.effect.IO
import controller.Controller
import engine.SimulationConstants.REPRODUCTION_PHASE
import engine.SimulationEndType.{ Extinction, GenerationsOverload, Overpopulation }
import engine.SimulationHistory._
import model.world.disturbingFactors.FactorTypes._
import model.world.GenerationsUtils.GenerationPhase
import util.PimpScala.RichOption
import view.scalaFX.ScalaFXView

import scala.language.implicitConversions

object Simulation {

  def wolvesEat: IO[Unit] = applyFactorDamage(WolvesFactorKind)

  def bunniesEat: IO[Unit] = applyFactorDamage(FoodFactorKind)

  def applyTemperatureDamage: IO[Unit] = applyFactorDamage(UnfriendlyClimateFactorKind)

  def updateView(generationPhase: GenerationPhase): IO[Unit] =
    ScalaFXView.updateView(generationPhase, getActualPopulation)

  def overpopulation(): IO[Unit] = {
    Controller.showEnd(Overpopulation())
  }

  def extinction(): IO[Unit] = {
    Controller.showEnd(Extinction())
  }

  def end(): IO[Unit] = {
    Controller.showEnd(GenerationsOverload())
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

trait SimulationEndType

object SimulationEndType {
  case class Overpopulation() extends SimulationEndType
  case class Extinction() extends SimulationEndType
  case class GenerationsOverload() extends SimulationEndType
}
