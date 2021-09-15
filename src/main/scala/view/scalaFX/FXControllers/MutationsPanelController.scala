package view.scalaFX.FXControllers

import scalafx.scene.control.{Button, Label}
import scalafx.scene.image.ImageView
import scalafxml.core.macros.sfxml

sealed trait MutationsPanelControllerInterface {
  /** Method called when a new generation is loaded that hides the Mutation Incoming label*/
  def hideMutationIncoming(): Unit
}

@sfxml
class MutationsPanelController( val jumpRecessiveChoiceButton: Button,
                                val jumpDominantChoiceButton: Button,
                                val teethRecessiveChoiceButton: Button,
                                val teethDominantChoiceButton: Button,
                                val earsRecessiveChoiceButton: Button,
                                val earsDominantChoiceButton: Button,
                                val furLengthRecessiveChoiceButton: Button,
                                val furLengthDominantChoiceButton: Button,
                                val furColorRecessiveChoiceButton: Button,
                                val furColorDominantChoiceButton: Button,
                                val mutationIncomingLabel: Label) extends MutationsPanelControllerInterface {

  def furColorDominantChoiceClick(): Unit = {
    // Send information to controller: BROWN_FUR DOMINANT

    furColorRecessiveChoiceButton.text = "Bianco"
    updateButtonStyle(furColorDominantChoiceButton, furColorRecessiveChoiceButton)
    showMutationIncoming()
    disableFurColorChoice()
  }

  def furColorRecessiveChoiceClick(): Unit = {
    // Send information to controller: BROWN_FUR RECESSIVE

    furColorDominantChoiceButton.text = "Bianco"
    updateButtonStyle(furColorRecessiveChoiceButton, furColorDominantChoiceButton)
    showMutationIncoming()
    disableFurColorChoice()
  }

  def furLengthDominantChoiceClick(): Unit = {
    // Send information to controller: THICK_FUR DOMINANT

    furLengthRecessiveChoiceButton.text = "Corto"
    updateButtonStyle(furLengthDominantChoiceButton, furLengthRecessiveChoiceButton)
    showMutationIncoming()
    disableLengthChoice()
  }

  def furLengthRecessiveChoiceClick(): Unit = {
    // Send information to controller: THICK_FUR RECESSIVE

    furLengthDominantChoiceButton.text = "Corto"
    updateButtonStyle(furLengthRecessiveChoiceButton, furLengthDominantChoiceButton)
    showMutationIncoming()
    disableLengthChoice()
  }

  def earsDominantChoiceClick(): Unit = {
    // Send information to controller: LOW_EARS DOMINANT

    earsRecessiveChoiceButton.text = "Alte"
    updateButtonStyle(earsDominantChoiceButton, earsRecessiveChoiceButton)
    showMutationIncoming()
    disableEarsChoice()
  }

  def earsRecessiveChoiceClick(): Unit = {
    // Send information to controller: LOW_EARS RECESSIVE

    earsDominantChoiceButton.text = "Alte"
    updateButtonStyle(earsRecessiveChoiceButton, earsDominantChoiceButton)
    showMutationIncoming()
    disableEarsChoice()
  }

  def teethDominantChoiceClick(): Unit = {
    // Send information to controller: LONG_TEETH DOMINANT

    teethRecessiveChoiceButton.text = "Corti"
    updateButtonStyle(teethDominantChoiceButton, teethRecessiveChoiceButton)
    showMutationIncoming()
    disableTeethChoice()
  }

  def teethRecessiveChoiceClick(): Unit = {
    // Send information to controller: LONG_TEETH RECESSIVE

    teethDominantChoiceButton.text = "Corti"
    updateButtonStyle(teethRecessiveChoiceButton, teethDominantChoiceButton)
    showMutationIncoming()
    disableTeethChoice()
  }

  def jumpDominantChoiceClick(): Unit = {
    // Send information to controller: HIGH_JUMP DOMINANT

    jumpRecessiveChoiceButton.text = "Basso"
    updateButtonStyle(jumpDominantChoiceButton, jumpRecessiveChoiceButton)
    showMutationIncoming()
    disableJumpChoice()
  }

  def jumpRecessiveChoiceClick(): Unit = {
    // Send information to controller: HIGH_JUMP RECESSIVE

    jumpDominantChoiceButton.text = "Basso"
    updateButtonStyle(jumpRecessiveChoiceButton, jumpDominantChoiceButton)
    showMutationIncoming()
    disableJumpChoice()
  }

  private def disableFurColorChoice(): Unit = {
    furColorDominantChoiceButton.disable = true
    furColorRecessiveChoiceButton.disable = true
  }

  private def disableLengthChoice(): Unit = {
    furLengthDominantChoiceButton.disable = true
    furLengthRecessiveChoiceButton.disable = true
  }

  private def disableEarsChoice(): Unit = {
    earsRecessiveChoiceButton.disable = true
    earsDominantChoiceButton.disable = true
  }

  private def disableTeethChoice(): Unit = {
    teethDominantChoiceButton.disable = true
    teethRecessiveChoiceButton.disable = true
  }

  private def disableJumpChoice(): Unit = {
    jumpDominantChoiceButton.disable = true
    jumpRecessiveChoiceButton.disable = true
  }

  private def updateButtonStyle(chosen: Button, other: Button): Unit = {
    chosen.styleClass += "chosen-button"
    other.styleClass += "dashed-button"
  }

  private def showMutationIncoming(): Unit = {
    mutationIncomingLabel.visible = true
  }

  def hideMutationIncoming(): Unit = {
    mutationIncomingLabel.visible = false
  }
}
