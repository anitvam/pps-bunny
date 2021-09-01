package controller

import scalafx.application.JFXApp
import view._
import view.scalaFX.ScalaFXView

object ScalaFXLauncher extends JFXApp{
  private val view = new ScalaFXView()
  view.start()
}
