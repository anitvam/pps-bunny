package engine

import scala.language.postfixOps

object Launcher extends App {
  SimulationEngine.simulationLoop() unsafeRunSync
}
