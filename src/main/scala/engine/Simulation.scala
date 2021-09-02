package engine

import cats.effect.IO
import model.world.Generation
import model.world.Generation.Population

object Simulation{
  type History = List[Generation]
  var history:History = List()

  def getActualGeneration:Option[Generation] = history match {
    case g :: _ => Some(g)
    case _ => Option.empty
  }

  def endedActualGeneration():Unit = if (getActualGeneration.isDefined) getActualGeneration.get.ended

  def getGenerationNumber:IO[Int] = IO{history.length}

  def getBunniesNumber:IO[Int] = IO{getActualGeneration.map(g => g.population.size).getOrElse(0)}

//  def getPopulationForNextGeneration : Population = history match {
//    case g :: _ => g.population.filter(b => b > 1 && b < 20) //will take only alive bunny
//    case _ => Seq(1, 2) //will create initial population (the first couple of bunnies)
//  }
//
//  def getEnvironmentForNextGeneration : Environment = history match {
//    case g :: _ => g.environment
//    case _ => "env" //will create initial environment characteristic
//  }

  def reproduction: IO[Unit] = {
    IO{println("REPRODUCTION")}
//    val initialBunniesNumber:Int = getActualGeneration.map(_.population.size).get
//    val nextGenBunniesNumber:Int = (initialBunniesNumber / 2)*4 + initialBunniesNumber
//    IO {getActualGeneration.get.population = (1 to nextGenBunniesNumber) toSet }
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
    IO{getActualGeneration.get.population.foreach(println(_))}
  }

  //Controller will notify Simulation when Environment Change

  def startNewGeneration: IO[Unit] = {
    IO {println("NEW GEN")}
//    IO {history = Generation(getEnvironmentForNextGeneration, getPopulationForNextGeneration) :: history}
  }


}
