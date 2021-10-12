package it.unibo.pps.bunny.view.scalaFX.components.charts.population

import it.unibo.pps.bunny.util.PimpScala.RichOption
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants.Style.PopulationLegend.ITEM_STYLE
import it.unibo.pps.bunny.view.scalaFX.components.charts.population.PopulationChartDataType.{ ChartPoint, SeriesData }
import it.unibo.pps.bunny.view.scalaFX.utilities.PimpScalaFXChartLibrary._
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.Includes._
import scalafx.scene.chart.{ LineChart, NumberAxis, XYChart }

/** A factory for all the components of a LineChart */
object LineChartFactory {
  type XYSeries = XYChart.Series[Number, Number]
  type XYData = XYChart.Data[Number, Number]
  val fromNameToStyle: String => String = _.replace(" ", "_")

  /**
   * Createss a [[NumberAxis]]
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
   * Creates Series with no data
   * @param name
   *   the name of the series that is shown in legend
   */
  def createEmptySeries(name: String): XYSeries = createSeries(name, SeriesData())

  /**
   * Creates Series with some data
   * @param name
   *   the name of the series that is shown in legend
   * @param s
   *   the [[SeriesData]] shown by the series
   */
  def createSeries(name: String, s: SeriesData): XYChart.Series[Number, Number] = {
    XYChart.Series(
      name = name,
      ObservableBuffer.from(s.data.map({ d => XYChart.Data[Number, Number](d.point.x, d.point.y) }))
    )
  }

  /**
   * Creates a single [[XYData]]
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
   * Creates an empty [[LineChart]]
   */
  def createEmptyLineChart(
      xAxis: NumberAxis,
      yAxis: NumberAxis,
      chartHeight: Double,
      chartWidth: Double
  ): LineChart[Number, Number] = {
    new LineChart(xAxis, yAxis) {
      maxHeight = chartHeight
      minHeight = chartHeight
      maxWidth = chartWidth
      minWidth = chartWidth
      legendSide = Side.Right
    }
  }

  /**
   * Creates a [[LineChart]]
   * @param seriesData
   *   all the series that the chart shows
   */
  def createPopulationChart(
      xAxis: NumberAxis,
      yAxis: NumberAxis,
      chartHeight: Double,
      chartWidth: Double,
      seriesData: Seq[XYSeries]
  ): LineChart[Number, Number] = {

    val chart = createEmptyLineChart(xAxis, yAxis, chartHeight, chartWidth)
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
