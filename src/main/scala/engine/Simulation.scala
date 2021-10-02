package engine

import cats.effect.IO
import controller.Controller
import engine.SimulationHistory._
import model.world.disturbingFactors.FactorTypes._
import model.world.GenerationsUtils.GenerationPhase
import view.scalaFX.ScalaFXView

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

  private def applyFactorDamage(factorKind: FactorKind): Unit = getActualGeneration.environment.factors
    .filter(_.factorType == factorKind)
    .foreach(factor => {
      println("APPLICO: " + factor.getClass)
      getActualGeneration.population = factor.applyDamage(getActualPopulation, getActualGeneration.environment.climate)
    })

}
