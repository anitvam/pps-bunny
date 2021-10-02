package view.scalaFX.components.charts

import model.genome.Alleles
import model.genome.Alleles.AlleleKind
import model.genome.KindsUtils.getGeneKind
import model.world.Generation.Population
import model.world.GenerationsUtils.GenerationPhase
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import util.PimpScala.RichOption
import view.scalaFX.components.charts.LineChartComponentFactory.{createEmptySeries, createXYChartData}
import view.scalaFX.components.charts.PopulationChartDataType._
import view.scalaFX.utilities.PimpScalaFXChartLibrary._

import scala.collection.immutable.ListMap
import scala.language.implicitConversions

object PopulationChartDataType {
  type XYSeries = XYChart.Series[Number, Number]
  type XYData = XYChart.Data[Number, Number]

  /** A simple Point with two coordinates */
  case class Point(x: Double, y: Int)

  /** A point with two coordinates and a flag to know if this point has to be shown */
  case class ChartPoint(point: Point, isTruePoint: Boolean)

  /** A sequence of points that must be graphed */
  case class SeriesData(var data: Seq[ChartPoint] = Seq()) {
    import ChartConverters._

    def ^(p: Point): Unit = data match {
      case Nil                    => data = data :+ (p, true)
      case lastPopulationValue(y) => data = data.appendedAll(Seq((p.x, y, false), (p, y != p.y)))
      case _                      => println(data)
    }

    def reset(): Unit = data = Seq()

  }

  /** A wrapper for a sequence of points and the graphic element that displays them */
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

  /** A map to manged the [[ChartSeries]] for each [[model.genome.Alleles.AlleleKind]] */
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

case class PopulationChart(height: Double, width: Double) {
  import LineChartComponentFactory._

  val xAxis: NumberAxis = createNumberAxis("Generation Axis", 0, 6, 1)
  val yAxis: NumberAxis = createNumberAxis("Population Axis", 0, 30, 5)
  var mutations: MutationsChartSeries = MutationsChartSeries()
  var total: ChartSeries = ChartSeries(SeriesData(), createEmptySeries("Total"))
  val chart: LineChart[Number, Number] =
    createLineChart(xAxis, yAxis, height, width, total.xySeries :: mutations.xySeries)

  def updateChart(generationPhase: GenerationPhase, population: Population): Unit = {
    import ChartConverters._
    total + (generationPhase, population.size)
    Alleles.values.foreach(ak =>
      mutations + (ak, (generationPhase, population.count(_.genotype.phenotype.values.toSet.contains(ak))))
    )
    updateChartBound(generationPhase, population.size)
  }

  def updateChartBound(x: Double, size: Int): Unit = {
    if (x >= xAxis.upperBound.toDouble) xAxis.upperBound = x + 2
    if (size >= yAxis.upperBound.toInt) yAxis.upperBound = size + 10
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

/** A factory for all the components of a LineChart */
object LineChartComponentFactory {

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
   * Create Series with no data
   * @param name
   *   the name of the series that is shown in legend
   */
  def createEmptySeries(name: String): XYSeries = createSeries(name, SeriesData())

  /**
   * Create Series with some data
   * @param name
   *   the name of the series that is shown in legend
   * @param s
   *   the [[SeriesData]] shown by the series
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
          point.getNode.styleClass ++= Seq("chart-line-mySymbol", series.getName.replace(" ", "_"))
          point.setExtraValue(v)
          point.getNode.visible = if (v) series.enabled else false
        })
      point
    case _ => XYChart.Data[Number, Number](0, 0)

  }

  /**
   * Create a [[LineChart]]
   * @param seriesData
   *   all the series that the chart shows
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
    seriesData.foreach(s => s.addStyle(s.getName.replace(" ", "_")))
    chart.legend.getItems.foreach(i => i.getSymbol.styleClass += i.getText.replace(" ", "_"))
    //At the beginning only the series "Total" is shown
    chart.legend.setLabelAsClicked("Total")
    seriesData.filterAndForeach(_.getName != "Total", _.enabled = false)

    chart.legend.getLabels.foreach(li => {
      seriesData.getSeries(li.text.value) --> { s =>
        s.addStyle("population-chart-legend-item")
        li.onMouseClicked = _ =>
          s.getName match {
            case "Total" =>
              s.enabled = true
              seriesData filterAndForeach (_.getName != "Total", _.enabled = false)
              chart.legend.setLabelAsClicked("Total")
            case _ =>
              //toggle series visibility
              s.enabled = !s.getNode.isVisible
              if (s.getNode.isVisible) {
                chart.legend.setLabelAsClicked(s.getName)
                seriesData filterAndForeach (_.getName != s.getName, _.enabled = false)
              } else {
                seriesData.getSeries("Total") --> {
                  _.enabled = true
                }
                chart.legend.setLabelAsClicked("Total")
              }
          }
      }
    })
    chart
  }

}
