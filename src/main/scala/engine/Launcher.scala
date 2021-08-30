package engine

object Launcher extends App {
  SimulationEngine.simulationLoop() unsafeRunSync
}
