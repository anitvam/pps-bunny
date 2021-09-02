package engine

import cats.effect.IO
import model.world.Generation
import model.world.Generation.{Environment, Population}
import model.world.Reproduction.{generateInitialCouple, nextGenerationBunnies}

object Simulation{
  type History = List[Generation]
  var history:History = List()

  def getActualGeneration:Option[Generation] = history match {
    case g :: _ => Some(g)
    case _ => Option.empty
  }

  def endedActualGeneration():Unit = if (getActualGeneration.isDefined) getActualGeneration.get.ended()

  def getGenerationNumber:IO[Int] = IO{history.length}

  def getBunniesNumber:IO[Int] = IO{getActualGeneration.get.getBunniesNumber}

  def getPopulationForNextGeneration : Population = history match {
    case g :: _ => nextGenerationBunnies(g.population)
    case _ => generateInitialCouple
  }

  def getEnvironmentForNextGeneration : Environment = history match {
    case g :: _ => g.environment
    case _ => "env" //will create initial environment characteristic
  }

  def wolfsEat: IO[Unit] = {
    IO {println("WOLFS ARE EATING")}
  }

  def bunniesEat: IO[Unit] = {
    IO {println("BUNNIES ARE EATING")}
  }

  def applyTemperatureDamage : IO[Unit] = {
    IO {println("SOME BUNNIES DIED BECAUSE OF TEMPERATURE")}
  }

  def showBunnies: IO[Unit] = {
    IO { println("GENERATION " + history.length + " NUM BUNNIES " +
      getActualGeneration.map(_.getBunniesNumber).getOrElse(0))}
//    IO{getActualGeneration.get.population.foreach(print(_))}
  }


  def startNewGeneration: IO[Unit] = {
    IO {history = Generation(getEnvironmentForNextGeneration, getPopulationForNextGeneration) :: history}
  }


}
