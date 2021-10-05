package bunny.controller

import scalafx.application.JFXApp3
import bunny.view.scalaFX.ScalaFXView

object ScalaFXLauncher extends JFXApp3 {

  override def start(): Unit = {
    ScalaFXView.start()
  }

}
