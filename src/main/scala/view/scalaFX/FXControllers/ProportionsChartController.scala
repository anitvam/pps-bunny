package view.scalaFX.FXControllers

import controller.Controller
import model.world.Generation.Population
import scalafx.scene.chart.PieChart
import scalafx.scene.control.{Label, RadioButton, ToggleGroup}
import javafx.scene.{control => jfxc}
import model.Bunny
import model.genome.{Gene, Genes}
import model.genome.Genes.GeneKind
import scalafx.scene.layout.{AnchorPane, VBox}
import scalafxml.core.macros.sfxml
import view.scalaFX.FXControllers.PieChartFactory.createEmptyPieChart
import view.scalaFX.utilities.PimpScalaFXChartLibrary._
import PieChartConverters._
import engine.SimulationConstants.START_PHASE
import model.world.GenerationsUtils.GenerationPhase
import scalafx.Includes.jfxNode2sfx
import scalafx.scene.paint.Color

import scala.language.implicitConversions

trait ChartController {
  def initialize(): Unit
  def updateChart(generationPhase: GenerationPhase, population: Population):Unit
}

@sfxml
class ProportionsChartController(val startPiePane:AnchorPane,
                                 val currentPiePane:AnchorPane,
                                 val pieChart: ToggleGroup) extends ChartController{

  var startPie: PieChart = createEmptyPieChart("Inizio Generazione")
  var currentPie: PieChart = createEmptyPieChart("Attualmente")

  override def initialize():Unit = {

    AnchorPane.setAnchors(startPie, 0, 0, 0,0)
    AnchorPane.setAnchors(currentPie, 0, 0, 0,0)

    startPiePane.children = startPie
    currentPiePane.children = currentPie
    fillPieCharts(Controller.population, getSelectedGeneKind)

  }

  override def updateChart(generationPhase: GenerationPhase, population: Population): Unit = {
    if(generationPhase.phase == START_PHASE) fillPieCharts(population, getSelectedGeneKind)
    else currentPie += (getSelectedGeneKind, population)
  }


  def onRadioButtonClick():Unit =
    resetAndFillPieCharts(Controller.population, getSelectedGeneKind)

  private def getSelectedGeneKind: GeneKind = pieChart.selectedToggle.value.asInstanceOf[jfxc.RadioButton].getText

  private def resetAndFillPieCharts(population: Population, gkSelected:GeneKind): Unit = {
    startPie /+= (gkSelected, population)
    currentPie /+= (gkSelected, population)
  }

  private def fillPieCharts(population: Population, gkSelected:GeneKind): Unit = {
    startPie += (gkSelected, population)
    currentPie += (gkSelected, population)
  }

}

object PieChartConverters {
  implicit def formStringToGeneKind(geneName:String):GeneKind = geneName match {
    case "Colore Pelo" => Genes.FUR_COLOR
    case "Lunghezza Pelo" => Genes.FUR_LENGTH
    case "Orecchie" => Genes.EARS
    case "Denti" => Genes.TEETH
    case "Salto" => Genes.JUMP
  }
  implicit def proportion(d:(Int, Int)):Double = d._1*100/d._2

  private def getBaseAndMutatedBunnies(population: Population, geneKind: GeneKind): (Seq[Bunny], Seq[Bunny]) =
    population.partition(_.genotype.phenotype.visibleTraits(geneKind) == geneKind.base)

  implicit def getDataChart(d:(GeneKind, Population)):Seq[(String, Double)] = {
    val (listBaseBunny, listMutatedBunny) = getBaseAndMutatedBunnies(d._2, d._1)
    Seq((d._1.base.prettyName,(listBaseBunny.size, d._2.size)),
      (d._1.mutated.prettyName, (listMutatedBunny.size, d._2.size)))
  }
}

object PieChartFactory {
  def createEmptyPieChart(chartTitle: String): PieChart =
    new PieChart {
      title = chartTitle
      clockwise = true
      startAngle = 90
      animated = false
      labelsVisible = true
      legendVisible = true
    }
}