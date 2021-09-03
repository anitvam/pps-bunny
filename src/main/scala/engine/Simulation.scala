package engine

import cats.effect.IO
import model.world.Generation
import model.world.Generation.{Environment, Population}
import model.world.Reproduction.{generateInitialCouple, nextGenerationBunnies}
import view.scalaFX.ScalaFXView

import scala.language.implicitConversions

object Simulation{

  type History = List[Generation]
  var history:History = List()

  def getActualGeneration: Option[Generation] = history match {
    case g :: _ => Some(g)
    case _ => Option.empty
  }

  def endedActualGeneration():Unit = if (getActualGeneration.isDefined) getActualGeneration.get.ended()

  def getGenerationNumber: Int = history.length

  def getBunniesNumber: Int = getActualGeneration.get.getBunniesNumber

  def getPopulationForNextGeneration : Population = history match {
    case g :: _ => nextGenerationBunnies(g.population)
    case _ => generateInitialCouple
  }

  def getEnvironmentForNextGeneration : Environment = history match {
    case g :: _ => g.environment
    case _ => "env" //will create initial environment characteristic
  }

  def wolvesEat: IO[Unit] = {
   println("WOLVES ARE EATING")
  }

  def bunniesEat: IO[Unit] = {
   println("BUNNIES ARE EATING")
  }

  def applyTemperatureDamage : IO[Unit] = {
   println("SOME BUNNIES DIED BECAUSE OF TEMPERATURE")
  }

  def showNewPopulation(bunnies:Population) : IO[Unit] = {
    ScalaFXView.showPopulation(bunnies)
  }

  def showEnd():IO[Unit] = {
    println("END")
  }


  def startNewGeneration: IO[Population] = {
     history = Generation(getEnvironmentForNextGeneration, getPopulationForNextGeneration) :: history
    IO{getActualGeneration.map(_.population).getOrElse(Seq())}
  }

  implicit def unitToIO(exp: => Unit) : IO[Unit] = IO{exp}


}
