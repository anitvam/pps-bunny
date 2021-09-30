package view.scalaFX.FXControllers

import controller.Controller
import model.genome.Genes._
import model.mutation.Mutation
import scalafx.scene.control.{Button, Label}
import scalafxml.core.macros.sfxml

sealed trait MutationsPanelControllerInterface {

  /** Method called when a new generation is loaded that hides the Mutation Incoming label */
  def hideMutationIncoming(): Unit

  /** Reset all the buttons to their original state */
  def resetMutationsPanel(): Unit
}

@sfxml
class MutationsPanelController(
    private val jumpRecessiveChoiceButton: Button,
    private val jumpDominantChoiceButton: Button,
    private val teethRecessiveChoiceButton: Button,
    private val teethDominantChoiceButton: Button,
    private val earsRecessiveChoiceButton: Button,
    private val earsDominantChoiceButton: Button,
    private val furLengthRecessiveChoiceButton: Button,
    private val furLengthDominantChoiceButton: Button,
    private val furColorRecessiveChoiceButton: Button,
    private val furColorDominantChoiceButton: Button,
    private val mutationIncomingLabel: Label
) extends MutationsPanelControllerInterface {

  val CHOSEN_STYLE: String = "chosen-button"
  val OTHER_STYLE: String = "dashed-button"

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
    clicked.styleClass += CHOSEN_STYLE
    other.styleClass += OTHER_STYLE
  }

  private def showMutationIncoming(): Unit = mutationIncomingLabel.visible = true

  def hideMutationIncoming(): Unit = mutationIncomingLabel.visible = false

  def resetMutationsPanel(): Unit = {
    hideMutationIncoming()
    resetButtons(Seq( jumpRecessiveChoiceButton, jumpDominantChoiceButton,
                      teethRecessiveChoiceButton, teethDominantChoiceButton,
                      earsRecessiveChoiceButton, earsDominantChoiceButton,
                      furLengthRecessiveChoiceButton, furLengthDominantChoiceButton,
                      furColorRecessiveChoiceButton, furColorDominantChoiceButton))
  }

  private def resetButtons(btns:Seq[Button]): Unit = {
    btns.foreach(btn => {
      btn.styleClass -= CHOSEN_STYLE
      btn.styleClass -= OTHER_STYLE
      btn.disable = false
    })
  }
}
