package it.unibo.pps.bunny.view.scalaFX.components.charts

import it.unibo.pps.bunny.model.genome.Alleles
import it.unibo.pps.bunny.model.genome.Alleles.AlleleKind
import it.unibo.pps.bunny.model.genome.KindsUtils.getGeneKind
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.GenerationsUtils.GenerationPhase
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.chart.{ LineChart, NumberAxis, XYChart }
import it.unibo.pps.bunny.util.PimpScala.RichOption
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants.PopulationChart._
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants.Style.PopulationLegend.ITEM_STYLE
import it.unibo.pps.bunny.view.scalaFX.components.charts.LineChartComponentFactory._
import it.unibo.pps.bunny.view.scalaFX.components.charts.PopulationChartDataType._
import it.unibo.pps.bunny.view.scalaFX.utilities.PimpScalaFXChartLibrary._

import scala.collection.immutable.ListMap
import scala.language.implicitConversions

object PopulationChartDataType {
  type XYSeries = XYChart.Series[Number, Number]
  type XYData = XYChart.Data[Number, Number]

  /**
   * A simple Point with two coordinates.
   * @param x the coordinate on the x axis
   * @param y the coordinate on the y axis
   */
  case class Point(x: Double, y: Int)

  /**
   * A [[Point]] of the chart with a flag that specifies if it must be shown.
   * @param point the subject [[Point]]
   * @param isTruePoint true if the point must be shown
   */
  case class ChartPoint(point: Point, isTruePoint: Boolean)

  /**
   * A sequence of points of the chart that must be graphed.
   * @param data The sequence of [[ChartPoint]]s
   */
  case class SeriesData(var data: Seq[ChartPoint] = Seq()) {
    import ChartConverters._

    def ^(p: Point): Unit = data match {
      case Nil                    => data = data :+ (p, true)
      case lastPopulationValue(y) => data = data.appendedAll(Seq((p.x, y, false), (p, y != p.y)))
      case _                      => println(data)
    }

    def reset(): Unit = data = Seq()

  }

  /**
   * A wrapper for a sequence of [[ChartPoint]] and the graphic element that displays them
   * @param seriesData the sequence of [[ChartPoint]]
   * @param xySeries the graphic element that displays the [[ChartPoint]]
   */
  case class ChartSeries(seriesData: SeriesData, xySeries: XYSeries) {

    def +(p: Point): Unit = {
      seriesData ^ p
      seriesData.data takeRight 2 foreach { xySeries += createXYChartData(_, xySeries) }
    }

    def reset(): Unit = {
      seriesData.reset()
      xySeries.getData.clear()
    }

  }

  /**
   * A map to manage the [[ChartSeries]] for each [[AlleleKind]]
   * @param mutationMap A map any [[AlleleKind]] to a [[ChartSeries]]
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
 * @param height the height of the panel to fit in
 * @param width the width of the panel to fit in
 */
case class PopulationChart(height: Double, width: Double) {
  import LineChartComponentFactory._

  val xAxis: NumberAxis = createNumberAxis("Generazioni", AXIS_LOWER_BOUND, X_AXIS_UPPER_BOUND, X_AXIS_TICK)
  val yAxis: NumberAxis = createNumberAxis("Popolazione", AXIS_LOWER_BOUND, Y_AXIS_UPPER_BOUND, Y_AXIS_TICK)

  var mutations: MutationsChartSeries = MutationsChartSeries()
  var total: ChartSeries = ChartSeries(SeriesData(), createEmptySeries("Total"))

  val chart: LineChart[Number, Number] =
    createLineChart(xAxis, yAxis, height, width, total.xySeries :: mutations.xySeries)

  /**
   * Updates the chart with population alive in the specified generation phase.
   * @param generationPhase the [[GenerationPhase]] of the data
   * @param population the [[Population]] to represent with the next point in the chart
   */
  def updateChart(generationPhase: GenerationPhase, population: Population): Unit = {
    import ChartConverters._
    total + (generationPhase, population.size)
    Alleles.values.foreach(ak =>
      mutations + (ak, (generationPhase, population.count(_.genotype.phenotype.values.toSet.contains(ak))))
    )
    updateChartBound(generationPhase, population.size)
  }

  /**
   * Updates the boundaries of the chart.
   * @param x The maximum coordinate of the points on the x axis.
   * @param y The maximum coordinate of the points on the y axis.
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

/** A factory for all the components of a [[LineChart]] */
object LineChartComponentFactory {
  val fromNameToStyle: String => String = _.replace(" ", "_")

  /**
   * Creates a [[NumberAxis]]
   * @param name
   *   of the axis
   * @param lowerBound
   *   the minimum value for the axis
   * @param upperBound
   *   the maximum value for the axis
   * @param tickUnit
   *   the [[NumberAxis.tickUnit]]
   */
  def createNumberAxis(name: String, lowerBound: Int, upperBound: Int, tickUnit: Int): NumberAxis = {
    val axis = NumberAxis(name)
    axis.lowerBound = lowerBound
    axis.upperBound = upperBound
    axis.tickUnit = tickUnit
    axis.minorTickVisible = false
    axis.autoRanging = false
    axis
  }

  /**
   * Create a Series with no data
   * @param name
   *   the name of the series that is shown in the legend
   */
  def createEmptySeries(name: String): XYSeries = createSeries(name, SeriesData())

  /**
   * Create a Series with some data
   * @param name
   *   the name of the series that is shown in the legend
   * @param s
   *   the [[SeriesData]] to be shown
   */
  def createSeries(name: String, s: SeriesData): XYSeries = {
    XYChart.Series(
      name = name,
      ObservableBuffer.from(s.data.map({ d => XYChart.Data[Number, Number](d.point.x, d.point.y) }))
    )
  }

  /**
   * Create a single [[XYData]]
   * @param dataChart
   *   the point that must to be shown
   * @param series
   *   the [[XYSeries]] to which it belongs
   */
  def createXYChartData(dataChart: ChartPoint, series: XYSeries): XYData = dataChart match {
    case ChartPoint((x, y), v) =>
      val point = XYChart.Data[Number, Number](x, y)
      point
        .nodeProperty()
        .addListener(_ => {
          point.getNode.styleClass ++= Seq("chart-line-mySymbol", fromNameToStyle(series.getName))
          point.setExtraValue(v)
          point.getNode.visible = if (v) series.enabled else false
        })
      point
    case _ => XYChart.Data[Number, Number](0, 0)

  }

  /**
   * Create a [[LineChart]]
   * @param seriesData
   *   all the series the chart shows
   */
  def createLineChart(
      xAxis: NumberAxis,
      yAxis: NumberAxis,
      chartHeight: Double,
      chartWidth: Double,
      seriesData: Seq[XYSeries]
  ): LineChart[Number, Number] = {

    val chart = new LineChart(xAxis, yAxis) {
      maxHeight = chartHeight
      minHeight = chartHeight
      maxWidth = chartWidth
      minWidth = chartWidth
      legendSide = Side.Right
    }
    chart ++= seriesData
    seriesData.foreach(s => s.addStyle(fromNameToStyle(s.getName)))
    chart.legend.getItems.foreach(i => i.getSymbol.styleClass += fromNameToStyle(i.getText))
    //At the beginning only the series "Total" is shown
    chart.legend.setLabelAsClicked("Total")
    seriesData.filterAndForeach(_.getName != "Total", _.enabled = false)

    chart.legend.getLabels.foreach(li => {
      li.styleClass += ITEM_STYLE
      seriesData.getSeries(li.text.value) --> { s =>
        s.addStyle(ITEM_STYLE)
        li.onMouseClicked = _ => {
          s.enabled = !s.getNode.isVisible
          if (s.getNode.isVisible) chart.legend.setLabelAsClicked(s.getName)
          else chart.legend.setLabelAsUnClicked(s.getName)
        }
      }
    })
    chart
  }

}
