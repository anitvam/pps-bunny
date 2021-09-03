package controller

import model.Bunny.generateRandomFirstBunny
import scalafx.application.JFXApp
import view._
import view.scalaFX.ScalaFXView

object ScalaFXLauncher extends JFXApp{
  private val view = new ScalaFXView()
  private val someBunnies = Seq.fill(2)(generateRandomFirstBunny)

  view.start()
  view.showPopulation(someBunnies)
}
