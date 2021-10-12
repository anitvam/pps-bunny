package it.unibo.pps.bunny.view.scalaFX.FXControllers

import it.unibo.pps.bunny.controller.Controller
import it.unibo.pps.bunny.engine.SimulationConstants.PhasesConstants._
import it.unibo.pps.bunny.engine.SimulationHistory
import javafx.scene.{ control => jfxc }
import it.unibo.pps.bunny.model.bunny.Bunny
import it.unibo.pps.bunny.model.genome.Genes
import it.unibo.pps.bunny.model.genome.Genes.GeneKind
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.GenerationsUtils.GenerationPhase
import scalafx.scene.chart.PieChart
import scalafx.scene.control.{ Button, ToggleGroup }
import scalafx.scene.layout.AnchorPane
import scalafxml.core.macros.sfxml
import it.unibo.pps.bunny.view.scalaFX.FXControllers.PieChartConverters._
import it.unibo.pps.bunny.view.scalaFX.FXControllers.PieChartFactory.createEmptyPieChart
import it.unibo.pps.bunny.view.scalaFX.utilities.PimpScalaFXChartLibrary._
import scalafx.scene.text.Text

import scala.language.implicitConversions

trait ChartController {

  /** Method that initializes the ChartController */
  def initialize(): Unit

  /**
   * Method that updates the chart visualization
   * @param generationPhase
   *   the actual [[GenerationPhase]]
   * @param population
   *   the actual [[Population]]
   */
  def updateChart(generationPhase: GenerationPhase, population: Population): Unit

  /** Method that resets the chart visualization */
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

  private val startPie: PieChart = createEmptyPieChart("Inizio Generazione")
  private val currentPie: PieChart = createEmptyPieChart("Attualmente")
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

  /**
   * Implicit method that converts a Gene name to a [[GeneKind]]
   * @param geneName
   *   the name of the Gene
   * @return
   *   the [[GeneKind]]
   */
  implicit def fromStringToGeneKind(geneName: String): GeneKind = Genes.values.find(_.prettyName == geneName).get

  /**
   * Implicit method that converts a tuple into a percentage
   * @param d
   *   a tuple [[(Int, Int)]]
   * @return
   *   a [[Double]] percentage
   */
  implicit def percentage(d: (Int, Int)): Double = (d._1.toDouble * 100 / d._2.toDouble).round.toDouble

  private def getBaseAndMutatedBunnies(population: Population, geneKind: GeneKind): (Seq[Bunny], Seq[Bunny]) =
    population.partition(_.genotype.phenotype.visibleTraits(geneKind) == geneKind.base)

  /**
   * Implicit Method that generates the chart's data from a given [[Population]] and a [[GeneKind]]
   * @param d
   *   a tuple [[(GeneKind, Population)]]
   * @return
   *   a [[Seq]] containing the chart's data
   */
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
