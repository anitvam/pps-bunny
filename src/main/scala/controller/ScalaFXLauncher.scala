package controller

import scalafx.application.JFXApp3
import view.scalaFX.ScalaFXView

object ScalaFXLauncher extends JFXApp3 {

  System.setProperty("prism.order", "sw")
  System.setProperty("prism.forceGPU", "true")

  override def start(): Unit = ScalaFXView.start()

}
