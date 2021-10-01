package view.scalaFX.FXControllers

import controller.Controller
import javafx.fxml.FXML
import model.world.disturbingFactors._
import scalafx.scene.control.CheckBox
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml
import util.PimpScala._
import view.scalaFX.ScalaFXConstants.WOLVES_NUMBER
import view.scalaFX.components.WolfView
import view.scalaFX.utilities._

sealed trait FactorsPanelControllerInterface {

  /** initialize all the component inside the panel */
  def initialize(baseAppController: BaseAppControllerInterface): Unit

  def reset(): Unit

  /** manage the environment background depending on the climate and the disturbing factors introduced */
  def manageEnvironmentBackgroundChange(): Unit

  /** method to show the wolves phase */
  def showWolvesEating(): Unit

  /** method to remove the wolves when its phase terminate*/
  def removeWolves(): Unit

  /** method that consent to be warned when wolves terminate their gui phase */
  def notifyEndWolvesAnimation(): Unit
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
  private var wolvesView: Seq[WolfView] = Seq()
  private var numberOfWolvesFinishAnimation: Int = 0

  override def initialize(baseAppController: BaseAppControllerInterface): Unit = {
    this.baseAppController = Some(baseAppController)
    val factorsImageViews: List[ImageView] = List(wolf, tough_food, high_food, limited_food, hostile_temperature)
    wolvesView = (1 to WOLVES_NUMBER) map (_ => WolfView(Some(this)))

    factorsImageViews foreach (i => insertFactorImage(i, i.getId))
  }

  override def reset(): Unit = {
    wolfCheckBox.selected = false
    toughFoodCheckBox.selected = false
    highFoodCheckBox.selected = false
    limitedFoodCheckBox.selected = false
    hostileTemperatureCheckBox.selected = false
  }

  private def insertFactorImage(image: ImageView, id: String): Unit =
    try image.image = new Image("/img/factors/" + id + ".png")
    catch {
      case _: IllegalArgumentException =>
    }

  private def onFactorClick(checkBox: CheckBox, factor: Factor): Unit =
    if (checkBox.selected.value) Controller.introduceFactor(factor) else Controller.removeFactor(factor)

  override def manageEnvironmentBackgroundChange(): Unit = {
    import view.scalaFX.utilities.EnvironmentImageUtils._
    baseAppController --> {
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
  }

  def onWolfClick(): Unit = onFactorClick(wolfCheckBox, Wolves())

  def onToughFoodClick(): Unit = {
    onFactorClick(
      toughFoodCheckBox,
      ToughFoodFactor()
    )
    manageEnvironmentBackgroundChange()
  }

  def onHighFoodClick(): Unit = {
    onFactorClick(
      highFoodCheckBox,
      HighFoodFactor()
    )
    manageEnvironmentBackgroundChange()
  }

  def onLimitedFoodClick(): Unit = {
    onFactorClick(
      limitedFoodCheckBox,
      LimitedFoodFactor()
    )
    manageEnvironmentBackgroundChange()
  }

  def onHostileTemperatureClick(): Unit = onFactorClick(hostileTemperatureCheckBox, UnfriendlyClimate())

  override def showWolvesEating(): Unit = {
    if (wolfCheckBox.isSelected) wolvesView foreach (w => {
      baseAppController --> { b => b.simulationPane.children.add(w.imageView) }
      w.play()
    })
  }

  override def notifyEndWolvesAnimation(): Unit = {
    numberOfWolvesFinishAnimation += 1
    if(numberOfWolvesFinishAnimation == WOLVES_NUMBER) removeWolves()
  }

  override def removeWolves(): Unit = {
    numberOfWolvesFinishAnimation = 0
    wolvesView foreach (w => {
      baseAppController --> { b => b.simulationPane.children.remove(w.imageView) }
    })
  }

}
