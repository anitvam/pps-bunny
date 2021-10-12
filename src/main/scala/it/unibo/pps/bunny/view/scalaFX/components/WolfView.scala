package it.unibo.pps.bunny.view.scalaFX.components

import it.unibo.pps.bunny.controller.Controller
import it.unibo.pps.bunny.engine.SimulationConstants.PhasesConstants.WOLVES_PHASE
import it.unibo.pps.bunny.engine.SimulationConstants.WOLVES_INSTANT_DEVIATION
import it.unibo.pps.bunny.view.scalaFX.FXControllers.FactorsPanelControllerInterface
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants.Wolf._
import scalafx.animation.AnimationTimer
import scalafx.scene.image.{ Image, ImageView }
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants._
import it.unibo.pps.bunny.view.scalaFX.utilities._
import it.unibo.pps.bunny.view.scalaFX.utilities.DirectionUtils._
import it.unibo.pps.bunny.util.PimpScala._
import scala.language.postfixOps
import scala.language.implicitConversions
import scala.util.Random

/** Wolf wrapper in order to manage its movement inside of the GUI */
trait WolfView extends AnimalView {

  /** Reference [[FactorsPanelControllerInterface]] to the controller of the factors panel */
  val factorsPanelController: Option[FactorsPanelControllerInterface]
  var isShown = false

  /** Method that allows to start [[WolfView]] movement immediately without delay */
  def playInstantly(): Unit

}

object WolfView {

  def apply(factorsPanelController: Option[FactorsPanelControllerInterface]): WolfView = {

    val newX = Random.nextInt(PREFERRED_SIMULATION_PANEL_WIDTH - PREFERRED_SIMULATION_PANEL_BORDER)
    val newY = Random.nextInt(PREFERRED_WOLF_PANEL_HEIGHT) + PANEL_SKY_ZONE

    WolfViewImpl(
      factorsPanelController = factorsPanelController,
      new ImageView {
        image = new Image("/img/factors/wolf.png")
        x = newX
        y = newY
        preserveRatio = true
        scaleX = scaleXValue(Right)
      },
      Right,
      newX,
      newY
    )
  }

  private case class WolfViewImpl(
      factorsPanelController: Option[FactorsPanelControllerInterface],
      imageView: ImageView,
      var direction: Direction,
      var positionX: Double,
      var positionY: Double
  ) extends WolfView {

    private val isAnimationDelayed: Long => Boolean = _ < timePhaseInSeconds(WOLVES_INSTANT_DEVIATION)

    private def timePhaseInSeconds(phase: Double): Double = phase * 1000 * Controller.getCurrentSimulationSpeed

    private val areWolvesVisible: Long => Boolean = _ <= timePhaseInSeconds(WOLVES_PHASE)

    private val timer: AnimationTimer = AnimationTimer(_ => {
      if (!isAnimationDelayed(lastTime) || !isPlayDelayed) {
        if (areWolvesVisible(lastTime)) {
          imageView.visible = true
          isShown = true
          factorsPanelController --> { _.disableWolfFactor() }
          checkDirection(
            positionX + imageView.getFitWidth / 2 >= PREFERRED_SIMULATION_PANEL_WIDTH - PREFERRED_SIMULATION_PANEL_BORDER,
            positionX - imageView.getFitWidth / 2 < 0
          )
          moveHorizontally(WOLVES_MOVING_SPACE)
          imageView.x = positionX
        } else stop()
      }
      lastTime += 1
    })

    private var lastTime = 0L
    private var isPlayDelayed = true

    override def play(): Unit = {
      isPlayDelayed = true
      imageView.visible = false
      timer.start()
    }

    override def playInstantly(): Unit = {
      isPlayDelayed = false
      imageView.visible = false
      timer.start()
    }

    override def stop(): Unit = {
      timer.stop()
      lastTime = 0L
      isShown = false
      factorsPanelController --> { _.removeWolf(imageView) }
      factorsPanelController --> { _.enableWolfFactor() }
    }

  }

}
