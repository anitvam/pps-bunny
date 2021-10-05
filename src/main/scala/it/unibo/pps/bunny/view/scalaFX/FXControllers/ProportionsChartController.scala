package it.unibo.pps.bunny.view.scalaFX.FXControllers

import it.unibo.pps.bunny.controller.Controller
import it.unibo.pps.bunny.engine.SimulationConstants.START_PHASE
import javafx.scene.{ control => jfxc }
import it.unibo.pps.bunny.model.Bunny
import it.unibo.pps.bunny.model.genome.Genes
import it.unibo.pps.bunny.model.genome.Genes.GeneKind
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.GenerationsUtils.GenerationPhase
import scalafx.scene.chart.PieChart
import scalafx.scene.control.ToggleGroup
import scalafx.scene.layout.AnchorPane
import scalafxml.core.macros.sfxml
import it.unibo.pps.bunny.view.scalaFX.FXControllers.PieChartConverters._
import it.unibo.pps.bunny.view.scalaFX.FXControllers.PieChartFactory.createEmptyPieChart
import it.unibo.pps.bunny.view.scalaFX.utilities.PimpScalaFXChartLibrary._

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
    val pieChart: ToggleGroup
) extends ChartController {

  private var startPie: PieChart = createEmptyPieChart("Inizio Generazione")
  private var currentPie: PieChart = createEmptyPieChart("Attualmente")

  override def initialize(): Unit = {

    AnchorPane.setAnchors(startPie, 0, 0, 0, 0)
    AnchorPane.setAnchors(currentPie, 0, 0, 0, 0)

    startPiePane.children += startPie
    currentPiePane.children += currentPie

    fillPieCharts(Controller.population, getSelectedGeneKind)

  }

  override def updateChart(generationPhase: GenerationPhase, population: Population): Unit = {
    if (generationPhase.phase == START_PHASE) fillPieCharts(population, getSelectedGeneKind)
    else currentPie += (getSelectedGeneKind, population.filter(_.alive))
  }

  private def getSelectedGeneKind: GeneKind = pieChart.selectedToggle.value.asInstanceOf[jfxc.RadioButton].getText

  private def fillPieCharts(population: Population, gkSelected: GeneKind): Unit = {
    startPie += (gkSelected, population)
    currentPie += (gkSelected, population.filter(_.alive))
  }

  def onRadioButtonClick(): Unit = fillPieCharts(Controller.population, getSelectedGeneKind)

  override def resetChart(): Unit = this.onRadioButtonClick()
}

object PieChartConverters {
  implicit def fromStringToGeneKind(geneName: String): GeneKind = Genes.values.filter(_.prettyName == geneName).head

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
