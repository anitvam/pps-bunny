package it.unibo.pps.bunny.view.scalaFX.FXControllers

import it.unibo.pps.bunny.controller.Controller
import javafx.fxml.FXML
import it.unibo.pps.bunny.model.world.disturbingFactors._
import scalafx.scene.control.CheckBox
import scalafx.scene.image.{ Image, ImageView }
import scalafxml.core.macros.sfxml
import it.unibo.pps.bunny.util.PimpScala._
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants.Wolf._
import it.unibo.pps.bunny.view.scalaFX.utilities.EnvironmentImageUtils._
import it.unibo.pps.bunny.view.scalaFX.components.WolfView
import it.unibo.pps.bunny.view.scalaFX.utilities._

sealed trait FactorsPanelControllerInterface {

  /** Method to initialize all the components of the panel related to disturbing factors */
  def initialize(baseAppController: BaseAppControllerInterface): Unit

  /** Method to reset all the component inside the disturbing factors panel */
  def reset(): Unit

  /** Method to manage the environment background depending on the climate and the disturbing factors introduced */
  def manageEnvironmentBackgroundChange(): Unit

  /** Method to show the wolves phase in the principal panel */
  def showWolves(): Unit

  /**
   * Method to remove the wolf when the corresponding phase terminate
   * @param wolfImage
   *   the image of the wolf to be removed from the panel
   */
  def removeWolf(wolfImage: ImageView): Unit

  /** @return true if the wolves have to be shown, otherwise false */
  var areWolvesShown: Boolean = false

  /** Method that disables the Wolf Factor selection */
  def disableWolfFactor(): Unit

  /** Method that enables the Wolf Factor selection */
  def enableWolfFactor(): Unit
}

@sfxml
class FactorsPanelController(
    @FXML private val wolfCheckBox: CheckBox,
    @FXML private val toughFoodCheckBox: CheckBox,
    @FXML private val highFoodCheckBox: CheckBox,
    @FXML private val limitedFoodCheckBox: CheckBox,
    @FXML private val hostileTemperatureCheckBox: CheckBox,
    @FXML private val wolf: ImageView,
    @FXML private val tough_food: ImageView,
    @FXML private val high_food: ImageView,
    @FXML private val limited_food: ImageView,
    @FXML private val hostile_temperature: ImageView
) extends FactorsPanelControllerInterface {

  private var baseAppController: Option[BaseAppControllerInterface] = None
  private val wolvesView: Seq[WolfView] = (1 to WOLVES_NUMBER) map (_ => WolfView(Some(this)))

  private val factorsCheckBox: List[CheckBox] =
    List(wolfCheckBox, toughFoodCheckBox, highFoodCheckBox, limitedFoodCheckBox, hostileTemperatureCheckBox)

  override def initialize(baseAppController: BaseAppControllerInterface): Unit = {
    this.baseAppController = Some(baseAppController)
    List(wolf, tough_food, high_food, limited_food, hostile_temperature) foreach (i => insertFactorImage(i, i.getId))
  }

  override def reset(): Unit = factorsCheckBox foreach (_.selected = false)

  override def manageEnvironmentBackgroundChange(): Unit = baseAppController --> {
    _.changeBackgroundEnvironment(
      (toughFoodCheckBox.isSelected, highFoodCheckBox.isSelected, limitedFoodCheckBox.isSelected) match {
        case (false, false, false) => (SummerImage(), WinterImage())
        case (true, false, false)  => (SummerImageToughFood(), WinterImageToughFood())
        case (false, true, false)  => (SummerImageHighFood(), WinterImageHighFood())
        case (false, false, true)  => (SummerImageLimitedFood(), WinterImageLimitedFood())
        case (true, false, true)   => (SummerImageLimitedToughFood(), WinterImageLimitedToughFood())
        case (true, true, false)   => (SummerImageHighToughFood(), WinterImageHighToughFood())
        case (false, true, true)   => (SummerImageLimitedHighFood(), WinterImageLimitedHighFood())
        case (true, true, true)    => (SummerImageLimitedHighToughFood(), WinterImageLimitedHighToughFood())
      }
    )
  }

  def startWolfAnimation(playWolf: WolfView => Unit = _.play()): Unit = {
    wolvesView.filter(!_.isShown) foreach (w => {
      baseAppController --> { _.simulationPane.children.add(w.imageView) }
      playWolf(w)
    })
    areWolvesShown = false
  }

  override def showWolves(): Unit = {
    areWolvesShown = true
    if (wolfCheckBox.isSelected) startWolfAnimation()
  }

  override def removeWolf(wolfImage: ImageView): Unit = {
    baseAppController --> { _.simulationPane.children.remove(wolfImage) }
  }

  def onWolfClick(): Unit = {
    onFactorClick(wolfCheckBox, WolvesFactor())
    if (areWolvesShown && wolfCheckBox.isSelected) startWolfAnimation(_.playInstantly())
    else wolvesView.foreach(w => removeWolf(w.imageView))
  }

  def onToughFoodClick(): Unit = {
    manageEnvironmentBackgroundChange()
    onFactorClick(toughFoodCheckBox, ToughFoodFactor())
  }

  def onHighFoodClick(): Unit = {
    manageEnvironmentBackgroundChange()
    onFactorClick(highFoodCheckBox, HighFoodFactor())
  }

  def onLimitedFoodClick(): Unit = {
    manageEnvironmentBackgroundChange()
    onFactorClick(limitedFoodCheckBox, LimitedFoodFactor())
  }

  def onHostileTemperatureClick(): Unit = onFactorClick(hostileTemperatureCheckBox, UnfriendlyClimateFactor())

  private def insertFactorImage(image: ImageView, id: String): Unit =
    try image.image = new Image("/img/factors/" + id + ".png")
    catch {
      case _: IllegalArgumentException =>
    }

  private def onFactorClick(checkBox: CheckBox, factor: Factor): Unit =
    if (checkBox.selected.value) Controller.introduceFactor(factor) else Controller.removeFactor(factor)

  def disableWolfFactor(): Unit = wolfCheckBox.disable = true
  def enableWolfFactor(): Unit = wolfCheckBox.disable = false
}
