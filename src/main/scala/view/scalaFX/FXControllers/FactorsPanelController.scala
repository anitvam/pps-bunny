package view.scalaFX.FXControllers

import engine.SimulationHistory
import javafx.fxml.FXML
import scalafx.scene.control.CheckBox
import scalafx.scene.image.{Image, ImageView}
import scalafxml.core.macros.sfxml
import util.PimpScala._
import view.scalaFX.ScalaFXConstants.WOLVES_NUMBER
import view.scalaFX.components.WolfView
import view.scalaFX.utilities.{SummerImage, SummerImageHighFood, SummerImageHighToughFood, SummerImageLimitedFood, SummerImageLimitedHighFood, SummerImageLimitedHighToughFood, SummerImageLimitedToughFood, SummerImageToughFood, WinterImage, WinterImageHighFood, WinterImageHighToughFood, WinterImageLimitedFood, WinterImageLimitedHighFood, WinterImageLimitedHighToughFood, WinterImageLimitedToughFood, WinterImageToughFood}

sealed trait FactorsPanelControllerInterface {

  /** initialize all the component inside the panel */
  def initialize(baseAppController: BaseAppControllerInterface): Unit
  def manageEnvironmentBackgroundChange(): Unit
  def showWolvesEating(): Unit
  def removeWolves(): Unit
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

  private def insertFactorImage(image: ImageView, id: String): Unit =
    try image.image = new Image("/img/factors/" + id + ".png")
    catch {
      case _: IllegalArgumentException =>
    }

  override def initialize(baseAppController: BaseAppControllerInterface): Unit = {
    this.baseAppController = Some(baseAppController)
    val factorsImageViews: List[ImageView] = List(wolf, tough_food, high_food, limited_food, hostile_temperature)
    wolvesView = (1 to WOLVES_NUMBER) map (_ => WolfView())
    factorsImageViews foreach (i => insertFactorImage(i, i.getId))
  }

  private def onFactorClick(factor: CheckBox, introduce: () => Unit, remove: () => Unit): Unit =
    if (factor.selected.value) introduce() else remove()

  override def manageEnvironmentBackgroundChange(): Unit = {
    import view.scalaFX.utilities.EnvironmentImageUtils._
    baseAppController --> { b =>
      b.changeBackgroundEnvironment(
        getImageBackgroundCorrespondingToClimate(
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
      )
    }
  }

  def onWolfClick(): Unit =
    onFactorClick(wolfCheckBox, () => SimulationHistory.introduceFactor(), () => SimulationHistory.removeFactor())

  def onToughFoodClick(): Unit = {
    onFactorClick(toughFoodCheckBox, () => SimulationHistory.introduceFactor(), () => SimulationHistory.removeFactor())
    manageEnvironmentBackgroundChange()
  }

  def onHighFoodClick(): Unit = {
    onFactorClick(toughFoodCheckBox, () => SimulationHistory.introduceFactor(), () => SimulationHistory.removeFactor())
    manageEnvironmentBackgroundChange()
  }

  def onLimitedFoodClick(): Unit = {
    onFactorClick(
      limitedFoodCheckBox,
      () => SimulationHistory.introduceFactor(),
      () => SimulationHistory.removeFactor()
    )
    manageEnvironmentBackgroundChange()
  }

  def onHostileTemperatureClick(): Unit = onFactorClick(
    hostileTemperatureCheckBox,
    () => SimulationHistory.introduceFactor(),
    () => SimulationHistory.removeFactor()
  )

  override def showWolvesEating(): Unit = {
    if (wolfCheckBox.isSelected) wolvesView foreach (w => {
      baseAppController --> { b => b.simulationPane.children.add(w.imageView) }
      w.play()
    })
  }

  override def removeWolves(): Unit = {
    wolvesView foreach (w => {
      baseAppController --> { b => b.simulationPane.children.remove(w.imageView) }
    })
  }

}
