package controller

import scalafx.application.JFXApp3
import view.scalaFX.ScalaFXView

object ScalaFXLauncher extends JFXApp3 {
  override def start(): Unit = ScalaFXView.start()
}
