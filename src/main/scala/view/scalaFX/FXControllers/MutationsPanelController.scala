package view.scalaFX.FXControllers

import controller.Controller
import javafx.fxml.FXML
import model.genome.Genes._
import model.mutation.Mutation
import scalafx.scene.control.{ Button, Label }
import scalafxml.core.macros.sfxml
import view.scalaFX.ScalaFXConstants.Style.MutationsChoice.{ CHOSEN_BUTTON_STYLE, OTHER_BUTTON_STYLE }

sealed trait MutationsPanelControllerInterface {

  /** Method called when a new generation is loaded that hides the Mutation Incoming label */
  def hideMutationIncoming(): Unit

  /** Reset all the buttons to their original state */
  def reset(): Unit
}

@sfxml
class MutationsPanelController(
    @FXML private val jumpRecessiveChoiceButton: Button,
    @FXML private val jumpDominantChoiceButton: Button,
    @FXML private val teethRecessiveChoiceButton: Button,
    @FXML private val teethDominantChoiceButton: Button,
    @FXML private val earsRecessiveChoiceButton: Button,
    @FXML private val earsDominantChoiceButton: Button,
    @FXML private val furLengthRecessiveChoiceButton: Button,
    @FXML private val furLengthDominantChoiceButton: Button,
    @FXML private val furColorRecessiveChoiceButton: Button,
    @FXML private val furColorDominantChoiceButton: Button,
    @FXML private val mutationIncomingLabel: Label
) extends MutationsPanelControllerInterface {

  case class MutationButton(button: Button, originalText: String) {

    def reset(): Unit = {
      button.styleClass -= CHOSEN_BUTTON_STYLE
      button.styleClass -= OTHER_BUTTON_STYLE
      button.disable = false
      button.text = originalText
    }

  }

  val buttons: Seq[MutationButton] = Seq(
    MutationButton(jumpRecessiveChoiceButton, "Alto"),
    MutationButton(jumpDominantChoiceButton, "Alto"),
    MutationButton(teethRecessiveChoiceButton, "Lunghi"),
    MutationButton(teethDominantChoiceButton, "Lunghi"),
    MutationButton(earsRecessiveChoiceButton, "Basse"),
    MutationButton(earsDominantChoiceButton, "Basse"),
    MutationButton(furLengthRecessiveChoiceButton, "Lungo"),
    MutationButton(furLengthDominantChoiceButton, "Lungo"),
    MutationButton(furColorRecessiveChoiceButton, "Marrone"),
    MutationButton(furColorDominantChoiceButton, "Marrone")
  )

  def reset(): Unit = {
    hideMutationIncoming()
    buttons.foreach(_.reset())
  }

  private def manageChoiceClick(
      geneKind: GeneKind,
      isDominant: Boolean,
      newText: String,
      buttonClicked: Button,
      otherButton: Button
  ): Unit = {
    if (isDominant) Controller.insertDominantMutationFor(geneKind)
    else Controller.insertRecessiveMutationFor(geneKind)
    otherButton.text = newText
    updateButtonStyle(buttonClicked, otherButton)
    showMutationIncoming()
    disableButtons(buttonClicked, otherButton)
  }

  def hideMutationIncoming(): Unit = mutationIncomingLabel.visible = false

  private def disableButtons(firstButton: Button, secondButton: Button): Unit = {
    firstButton.disable = true
    secondButton.disable = true
  }

  private def updateButtonStyle(clicked: Button, other: Button): Unit = {
    clicked.styleClass += CHOSEN_BUTTON_STYLE
    other.styleClass += OTHER_BUTTON_STYLE
  }

  private def showMutationIncoming(): Unit = mutationIncomingLabel.visible = true

  def furColorDominantChoiceClick(): Unit = manageChoiceClick(
    FUR_COLOR,
    isDominant = true,
    "Bianco",
    buttonClicked = furColorDominantChoiceButton,
    otherButton = furColorRecessiveChoiceButton
  )

  def furColorRecessiveChoiceClick(): Unit = manageChoiceClick(
    FUR_COLOR,
    isDominant = false,
    "Bianco",
    buttonClicked = furColorRecessiveChoiceButton,
    otherButton = furColorDominantChoiceButton
  )

  def furLengthDominantChoiceClick(): Unit = manageChoiceClick(
    FUR_LENGTH,
    isDominant = true,
    "Corto",
    buttonClicked = furLengthDominantChoiceButton,
    otherButton = furLengthRecessiveChoiceButton
  )

  def furLengthRecessiveChoiceClick(): Unit = manageChoiceClick(
    FUR_LENGTH,
    isDominant = false,
    "Corto",
    buttonClicked = furLengthRecessiveChoiceButton,
    otherButton = furLengthDominantChoiceButton
  )

  def earsDominantChoiceClick(): Unit = manageChoiceClick(
    EARS,
    isDominant = true,
    "Alte",
    buttonClicked = earsDominantChoiceButton,
    otherButton = earsRecessiveChoiceButton
  )

  def earsRecessiveChoiceClick(): Unit = manageChoiceClick(
    EARS,
    isDominant = false,
    "Alte",
    buttonClicked = earsRecessiveChoiceButton,
    otherButton = earsDominantChoiceButton
  )

  def teethDominantChoiceClick(): Unit = manageChoiceClick(
    TEETH,
    isDominant = true,
    "Corti",
    buttonClicked = teethDominantChoiceButton,
    otherButton = teethRecessiveChoiceButton
  )

  def teethRecessiveChoiceClick(): Unit = manageChoiceClick(
    TEETH,
    isDominant = false,
    "Corti",
    buttonClicked = teethRecessiveChoiceButton,
    otherButton = teethDominantChoiceButton
  )

  def jumpDominantChoiceClick(): Unit = manageChoiceClick(
    JUMP,
    isDominant = true,
    "Basso",
    buttonClicked = jumpDominantChoiceButton,
    otherButton = jumpRecessiveChoiceButton
  )

  def jumpRecessiveChoiceClick(): Unit = manageChoiceClick(
    JUMP,
    isDominant = false,
    "Basso",
    buttonClicked = jumpRecessiveChoiceButton,
    otherButton = jumpDominantChoiceButton
  )

}
