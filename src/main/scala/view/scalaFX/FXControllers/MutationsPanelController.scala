package view.scalaFX.FXControllers

import controller.Controller
import javafx.fxml.FXML
import model.genome.Genes._
import model.mutation.Mutation
import scalafx.scene.control.{ Button, Label }
import scalafxml.core.macros.sfxml

sealed trait MutationsPanelControllerInterface {

  /** Method called when a new generation is loaded that hides the Mutation Incoming label */
  def hideMutationIncoming(): Unit
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

  private def manageChoiceClick(
      geneKind: GeneKind,
      isDominant: Boolean,
      newText: String,
      buttonClicked: Button,
      otherButton: Button
  ): Unit = {
    Controller.insertMutation(Mutation(geneKind, isDominant))
    otherButton.text = newText
    updateButtonStyle(buttonClicked, otherButton)
    showMutationIncoming()
    disableButtons(buttonClicked, otherButton)
  }

  private def disableButtons(firstButton: Button, secondButton: Button): Unit = {
    firstButton.disable = true
    secondButton.disable = true
  }

  private def updateButtonStyle(clicked: Button, other: Button): Unit = {
    clicked.styleClass += "chosen-button"
    other.styleClass += "dashed-button"
  }

  private def showMutationIncoming(): Unit = mutationIncomingLabel.visible = true

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

  def hideMutationIncoming(): Unit = mutationIncomingLabel.visible = false
}
