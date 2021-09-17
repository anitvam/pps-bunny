package view.scalaFX.FXControllers

import scalafxml.core.macros.sfxml

sealed trait ChartChoiceControllerInterface {

}

@sfxml
class ChartChoiceController extends ChartChoiceControllerInterface {

  def showPopulationChart(): Unit = {

  }

  def showMutationsChart(): Unit = {

  }

  def showPedigreeChart(): Unit = {

  }

}
