package controller

import model.Bunny.generateRandomFirstBunny
import scalafx.application.{JFXApp, JFXApp3}
import view._
import view.scalaFX.ScalaFXView

object ScalaFXLauncher extends JFXApp3{


  override def start(): Unit = {
    val view = new ScalaFXView()
    val someBunnies = Seq.fill(50)(generateRandomFirstBunny)

    view.start()
    view.showPopulation(someBunnies)

  }
}
