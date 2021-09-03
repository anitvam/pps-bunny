package controller

import engine.SimulationEngine

object Launcher extends App {
  SimulationEngine.simulationLoop() unsafeRunSync
}
