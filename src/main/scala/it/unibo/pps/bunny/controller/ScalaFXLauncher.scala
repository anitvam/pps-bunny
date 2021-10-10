package it.unibo.pps.bunny.controller

import scalafx.application.JFXApp3
import it.unibo.pps.bunny.view.scalaFX.ScalaFXView

object ScalaFXLauncher extends JFXApp3 {

  System.setProperty("prism.order", "sw")
  System.setProperty("prism.forceGPU", "true")

  override def start(): Unit = ScalaFXView.start()

}
