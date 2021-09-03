package controller

import engine.SimulationEngine.simulationLoop
import model.Bunny.generateRandomFirstBunny
import scalafx.application.{JFXApp, JFXApp3}
import view._
import view.scalaFX.ScalaFXView

object ScalaFXLauncher extends JFXApp3{


  override def start(): Unit = {
//    val someBunnies = Seq.fill(50)(generateRandomFirstBunny)

    ScalaFXView.start()
    simulationLoop().unsafeRunAsyncAndForget()
//    view.showPopulation(someBunnies)

  }
}
