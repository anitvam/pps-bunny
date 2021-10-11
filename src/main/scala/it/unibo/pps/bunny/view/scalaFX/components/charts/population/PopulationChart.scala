package it.unibo.pps.bunny.view.scalaFX.components.charts.population

import it.unibo.pps.bunny.model.genome.Alleles
import it.unibo.pps.bunny.model.genome.Alleles.AlleleKind
import it.unibo.pps.bunny.model.genome.KindsUtils.getGeneKind
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.GenerationsUtils.GenerationPhase
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants.PopulationChart._
import it.unibo.pps.bunny.view.scalaFX.components.charts.population.LineChartFactory.{
  createEmptySeries, createNumberAxis, createPopulationChart, createXYChartData
}
import it.unibo.pps.bunny.view.scalaFX.components.charts.population.PopulationChartDataType._
import it.unibo.pps.bunny.view.scalaFX.utilities.PimpScalaFXChartLibrary._
import scalafx.scene.chart.{ LineChart, NumberAxis, XYChart }

import scala.collection.immutable.ListMap
import scala.language.implicitConversions

object PopulationChartDataType {
  type XYSeries = XYChart.Series[Number, Number]
  type XYData = XYChart.Data[Number, Number]

  /**
   * A simple Point with two coordinates
   * @param x
   *   the coordinate on the x axis
   * @param y
   *   the coordinate on the y axis
   */
  case class Point(x: Double, y: Int)

  /**
   * A [[Point]] of the chart with a flag that specifies if it must be shown
   * @param point
   *   the subject [[Point]]
   * @param isTruePoint
   *   true if the point must be shown, otherwise false
   */
  case class ChartPoint(point: Point, isTruePoint: Boolean)

  /**
   * A sequence of points of the chart that must be graphed
   * @param data
   *   The sequence of [[ChartPoint]] s
   */
  case class SeriesData(var data: Seq[ChartPoint] = Seq()) {
    import ChartConverters._

    def +(p: Point): SeriesData = data match {
      case Nil                    => SeriesData(data :+ (p, true))
      case lastPopulationValue(y) => SeriesData(data ++ Seq((p.x, y, false), (p, y != p.y)))
      case _                      => SeriesData(data)
    }

  }

  /**
   * A wrapper for a sequence of [[ChartPoint]] and the graphic element that displays them
   * @param seriesData
   *   the sequence of [[ChartPoint]]
   * @param xySeries
   *   the graphic element that displays the [[ChartPoint]]
   */
  case class ChartSeries(var seriesData: SeriesData, xySeries: XYSeries) {

    def +(p: Point): Unit = {
      seriesData = seriesData + p
      seriesData.data takeRight 2 foreach { xySeries += createXYChartData(_, xySeries) }
    }

    def reset(): Unit = {
      seriesData = SeriesData()
      xySeries.getData.clear()
    }

  }

  /**
   * A map to manage the [[ChartSeries]] for each [[AlleleKind]]
   * @param mutationMap
   *   A map to bind any [[AlleleKind]] to a [[ChartSeries]]
   */
  case class MutationsChartSeries(var mutationMap: Map[AlleleKind, ChartSeries] = Map()) {

    Alleles.values.foreach { ak =>
      mutationMap = mutationMap + (ak -> ChartSeries(SeriesData(), createEmptySeries(ak.prettyName)))
    }

    mutationMap = ListMap(mutationMap.toSeq.sortBy(entry => getGeneKind(entry._1)): _*)

    def seriesData: Seq[SeriesData] = mutationMap.values.map(_.seriesData).toSeq
    def xySeries: List[XYSeries] = mutationMap.values.map(_.xySeries).toList

    def +(ak: AlleleKind, p: Point): Unit = {
      mutationMap(ak) + p
    }

    def reset(): Unit = mutationMap.values foreach { _.reset() }

  }

  /** Ad hoc ChartPoint extractor */
  object ChartPoint {
    def unapply(arg: ChartPoint): Option[((Double, Int), Boolean)] = Some(((arg.point.x, arg.point.y), arg.isTruePoint))
  }

}

/**
 * Represent the chart for the Population.
 * @param height
 *   the height of the panel to fit in
 * @param width
 *   the width of the panel to fit in
 */
case class PopulationChart(height: Double, width: Double) {

  val xAxis: NumberAxis = createNumberAxis("Generazioni", AXIS_LOWER_BOUND, X_AXIS_UPPER_BOUND, X_AXIS_TICK)
  val yAxis: NumberAxis = createNumberAxis("Popolazione", AXIS_LOWER_BOUND, Y_AXIS_UPPER_BOUND, Y_AXIS_TICK)

  val mutations: MutationsChartSeries = MutationsChartSeries()
  val total: ChartSeries = ChartSeries(SeriesData(), createEmptySeries("Total"))

  val chart: LineChart[Number, Number] =
    createPopulationChart(xAxis, yAxis, height, width, total.xySeries :: mutations.xySeries)

  /**
   * Updates the chart with population alive in the specified generation phase.
   * @param generationPhase
   *   the [[GenerationPhase]] of the data
   * @param population
   *   the [[Population]] to represent with the next point in the chart
   */
  def updateChart(generationPhase: GenerationPhase, population: Population): Unit = {
    import ChartConverters._
    total + (generationPhase, population.size)
    Alleles.values.foreach(ak => mutations + (ak, (generationPhase, population.count(_.genotype.phenotype.has(ak)))))
    updateChartBound(generationPhase, population.size)
  }

  /**
   * Updates the boundaries of the chart.
   * @param x
   *   The maximum coordinate of the points on the x axis.
   * @param y
   *   The maximum coordinate of the points on the y axis.
   */
  def updateChartBound(x: Double, y: Int): Unit = {
    if (x >= xAxis.upperBound.toDouble) xAxis.upperBound = x + 2
    if (y >= yAxis.upperBound.toInt) yAxis.upperBound = y + 10
  }

}

/** Some implicit method for conversions */
object ChartConverters {
  implicit def fromGenerationPhaseToX(g: GenerationPhase): Double = g.generationNumber + g.phase
  implicit def fromTupleToPoint(t: (Double, Int)): Point = Point(t._1, t._2)
  implicit def fromTupleGenToPoint(t: (GenerationPhase, Int)): Point = Point(t._1, t._2)
  implicit def fromTupleToChartPoint(t: (Point, Boolean)): ChartPoint = ChartPoint(t._1, t._2)
  implicit def fromTupleToChartPoint(t: (Double, Int, Boolean)): ChartPoint = ChartPoint(Point(t._1, t._2), t._3)

  /** An extractor to get last population value from a sequence of [[ChartPoint]] */
  object lastPopulationValue {
    def unapply(s: Seq[ChartPoint]): Option[Int] = Some(s.last.point.y)
  }

}
