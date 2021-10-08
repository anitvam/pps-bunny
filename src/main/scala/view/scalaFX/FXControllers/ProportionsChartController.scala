package view.scalaFX.FXControllers

import controller.Controller
import javafx.scene.{control => jfxc}
import engine.SimulationConstants.PhasesConstants._
import javafx.scene.{ control => jfxc }
import model.genome.Genes
import model.genome.Genes.GeneKind
import model.world.Generation.Population
import scalafx.scene.chart.PieChart
import scalafx.scene.control.{Button, ToggleGroup}
import scalafx.scene.layout.AnchorPane
import scalafxml.core.macros.sfxml
import view.scalaFX.utilities.PimpScalaFXChartLibrary._
import engine.SimulationHistory
import model.world.GenerationsUtils.GenerationPhase
import scalafx.scene.text.Text
import PieChartConverters._
import model.bunny.Bunny
import view.scalaFX.FXControllers.PieChartFactory.createEmptyPieChart

import scala.language.implicitConversions

trait ChartController {
  def initialize(): Unit
  def updateChart(generationPhase: GenerationPhase, population: Population): Unit
  def resetChart(): Unit
}

@sfxml
class ProportionsChartController(
    val startPiePane: AnchorPane,
    val currentPiePane: AnchorPane,
    val pieChart: ToggleGroup,
    val nextBtn: Button,
    val backBtn: Button,
    val genText: Text
) extends ChartController {

  private var startPie: PieChart = createEmptyPieChart("Inizio Generazione")
  private var currentPie: PieChart = createEmptyPieChart("Attualmente")
  private var displayedGenerationNumber: Int = 0
  private var isInHistoryMode: Boolean = false

  override def initialize(): Unit = {

    AnchorPane.setAnchors(startPie, 0, 0, 0, 0)
    AnchorPane.setAnchors(currentPie, 0, 0, 0, 0)

    startPiePane.children += startPie
    currentPiePane.children += currentPie

    fillPieCharts(Controller.population, getSelectedGeneKind)

    nextBtn.disable = true
    nextBtn.onAction = _ => {
      backBtn.disable = false
      changeGeneration(displayedGenerationNumber + 1)
    }

    backBtn.onAction = _ => {
      isInHistoryMode = true
      changeGeneration(displayedGenerationNumber - 1)
      nextBtn.disable = false
    }
    backBtn.disable = true

  }

  override def updateChart(generationPhase: GenerationPhase, population: Population): Unit = {
    if (!isInHistoryMode) {
      if (generationPhase.phase == REPRODUCTION_PHASE) {
        fillPieCharts(population, getSelectedGeneKind)
        displayedGenerationNumber = generationPhase.generationNumber
        genText.text = s"Generazione $displayedGenerationNumber"
        if (generationPhase.generationNumber > 0) backBtn.disable = false
      } else fillCurrentPieChart(population, getSelectedGeneKind)
    }
  }

  private def getSelectedGeneKind: GeneKind = pieChart.selectedToggle.value.asInstanceOf[jfxc.RadioButton].getText

  private def fillPieCharts(population: Population, gkSelected: GeneKind): Unit = {
    val p = if (population.nonEmpty) population else SimulationHistory.getActualGeneration.population
    startPie += (gkSelected, p)
    fillCurrentPieChart(p, gkSelected)
  }

  private def fillCurrentPieChart(population: Population, gkSelected: GeneKind): Unit =
    currentPie += (gkSelected, population.filter(_.alive))

  def onRadioButtonClick(): Unit = changeGeneration(displayedGenerationNumber)

  override def resetChart(): Unit = {
    isInHistoryMode = false
    displayedGenerationNumber = 0
    changeGeneration(displayedGenerationNumber)
  }

  private def changeGeneration(generationNumber: Int): Unit = {
    val generation = SimulationHistory.history(SimulationHistory.getGenerationNumber - generationNumber)
    displayedGenerationNumber = generationNumber
    genText.text = s"Generazione $displayedGenerationNumber"
    currentPie.title = "Fine Generazione"
    fillPieCharts(if (generation.isEnded) generation.populationAtTheEnd else generation.population, getSelectedGeneKind)
    if (displayedGenerationNumber == 0) backBtn.disable = true
    if (displayedGenerationNumber == SimulationHistory.getGenerationNumber) {
      isInHistoryMode = false
      nextBtn.disable = true
      currentPie.title = "Attualmente"

    }
  }

}

object PieChartConverters {
  implicit def fromStringToGeneKind(geneName: String): GeneKind = Genes.values.find(_.prettyName == geneName).get

  implicit def percentage(d: (Int, Int)): Double = (d._1.toDouble * 100 / d._2.toDouble).round.toDouble

  private def getBaseAndMutatedBunnies(population: Population, geneKind: GeneKind): (Seq[Bunny], Seq[Bunny]) =
    population.partition(_.genotype.phenotype.visibleTraits(geneKind) == geneKind.base)

  implicit def getDataChart(d: (GeneKind, Population)): Seq[(String, Double)] = {
    val (listBaseBunny, listMutatedBunny) = getBaseAndMutatedBunnies(d._2, d._1)
    Seq(
      (d._1.base.prettyName, (listBaseBunny.size, d._2.size)),
      (d._1.mutated.prettyName, (listMutatedBunny.size, d._2.size))
    )
  }

}

object PieChartFactory {

  def createEmptyPieChart(chartTitle: String): PieChart = new PieChart {
    title = chartTitle
    clockwise = false
    startAngle = 90
    animated = false
    labelsVisible = false
    legendVisible = true
  }

}
