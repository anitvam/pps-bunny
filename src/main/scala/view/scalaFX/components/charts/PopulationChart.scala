package view.scalaFX.components.charts

import model.genome.Alleles
import model.genome.Alleles.AlleleKind
import model.world.Generation.Population
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import javafx.scene.control.Label
import model.world.GenerationsUtils.GenerationPhase
import view.scalaFX.components.charts.LineChartComponentFactory.{createEmptySeries, createXYChartData}
import view.scalaFX.components.charts.PopulationChartDataType._
import view.scalaFX.utilities.PimpScala.RichOption
import view.scalaFX.utilities.PimpScalaFXChartLibrary._

import scala.language.implicitConversions

object PopulationChartDataType{
  type XYSeries = XYChart.Series[Number,Number]
  type XYData =  XYChart.Data[Number,Number]

  case class Point(x:Double, y:Int)
  case class ChartPoint (point:Point, isTruePoint:Boolean)
  object ChartPoint{
    def unapply(arg: ChartPoint): Option[((Double, Int), Boolean)] = Some(((arg.point.x,arg.point.y),arg.isTruePoint))
  }
  case class SeriesData (var data: Seq[ChartPoint] = Seq()){
    import ChartConverters._
    def ^ (p:Point) :Unit = data match {
      case Nil => data = data :+ (p, true)
      case lastPopulationValue(y) => data = data.appendedAll(Seq((p.x, y, false),(p, y != p.y)))
      case _ => println(data)
    }
  }
  case class ChartSeries (seriesData: SeriesData, xySeries:XYSeries){
    def + (p:Point): Unit ={
      seriesData ^ p
      seriesData.data takeRight 2 foreach {xySeries += createXYChartData(_, xySeries)}
    }
  }
  case class MutationsChartSeries(var mutationMap: Map[AlleleKind, ChartSeries] = Map()){
    Alleles.values.foreach{ak => mutationMap = mutationMap + (ak -> ChartSeries(SeriesData(), createEmptySeries(ak.prettyName)))}
    def seriesData : Seq[SeriesData] = mutationMap.values.map(_.seriesData).toSeq
    def xySeries:List[XYSeries] = mutationMap.values.map(_.xySeries).toList
    def + (ak:AlleleKind, p:Point): Unit = {
      mutationMap(ak) + p
    }
  }
}

object PopulationChart {
  import LineChartComponentFactory._

  var mutations: MutationsChartSeries = MutationsChartSeries()
  var total: ChartSeries = ChartSeries(SeriesData(), createEmptySeries("Total"))

  val xAxis: NumberAxis = createNumberAxis("Generation Axis", 0, 6, 1)
  val yAxis: NumberAxis =  createNumberAxis("Population Axis", 0, 30, 5)
  val chart: (Double, Double) => LineChart[Number, Number] = createLineChart(xAxis, yAxis, _, _, total.xySeries :: mutations.xySeries)

  def updateChart(generationPhase:GenerationPhase, population:Population): Unit = {
    import ChartConverters._
    total + (generationPhase, population.size)
    Alleles.values.foreach(ak => {
      mutations + (ak, (generationPhase, population.count(_.genotype.phenotype.visibleTraits.values.toSet.contains(ak))))
    })
    updateChartBound(generationPhase, population.size)
  }

  def updateChartBound(generationPhase: Double, size: Int): Unit = {
    if (generationPhase > xAxis.upperBound.toDouble) xAxis.upperBound = generationPhase + 2
    if (size > yAxis.upperBound.toInt) yAxis.upperBound = size + 10
  }
}

object ChartConverters {
  implicit def fromGenerationPhaseToX(g:GenerationPhase) : Double = g.generationNumber + g.phase
  implicit def fromTupleToPoint(t:(Double, Int)): Point = Point(t._1, t._2)
  implicit def fromTupleToChartPoint(t:(Point,Boolean)):ChartPoint = ChartPoint(t._1, t._2)
  implicit def fromTupleToChartPoint(t:(Double,Int,Boolean)):ChartPoint = ChartPoint(Point(t._1,t._2), t._3)
  object lastPopulationValue{
    def unapply(s:Seq[ChartPoint]):Option[Int] = Some(s.last.point.y)
  }
}

object LineChartComponentFactory{

  def createNumberAxis(name:String, lowerBound:Int, upperBound:Int, tickUnit:Int): NumberAxis = {
    val axis = NumberAxis(name)
    axis.lowerBound = lowerBound
    axis.upperBound = upperBound
    axis.tickUnit = tickUnit
    axis.minorTickVisible = false
    axis.autoRanging = false
    axis
  }

  def createEmptySeries(name:String): XYSeries = createSeries(name, SeriesData())

  def createSeries(name:String, s: SeriesData): XYSeries = {
    XYChart.Series(
      name = name,
      ObservableBuffer.from(s.data.map({d => XYChart.Data[Number, Number](d.point.x,d.point.y)}))
    )
  }

  def createXYChartData(dataChart: ChartPoint, series: XYSeries): XYData = dataChart match {
    case ChartPoint((x, y), v)  =>
      val point = XYChart.Data[Number, Number](x, y)
      point.nodeProperty().addListener(_ => {
        point.getNode.styleClass ++= Seq("chart-line-mySymbol", series.getName.replace(" ", "_"))
        point.setExtraValue(v)
        point.getNode.visible = if(v) series.enabled else false
      })
      point
  }

  def createLineChart(xAxis:NumberAxis, yAxis:NumberAxis,
                      chartHeight:Double, chartWidth:Double,
                      seriesData:Seq[XYSeries]): LineChart[Number,Number] = {

    val chart = new LineChart(xAxis, yAxis) {
      maxHeight = chartHeight
      maxWidth = chartWidth
      legendSide = Side.Right
    }
    chart ++= seriesData
    seriesData.foreach(s => s.addStyle(s.getName.replace(" ", "_")))
    chart.legend.getItems.foreach(i => i.getSymbol.styleClass += i.getText.replace(" ", "_"))
    //At the beginning only the series "Total" is shown
    chart.legend.setLabelAsClicked("Total")
    seriesData.filterAndForeach(_.getName != "Total", _.enabled(false))

    chart.legend.getLabels.foreach(li => {
      seriesData.getSeries(li.text.value) --> { s =>
        li.onMouseClicked = _ => s.getName match {
          case "Total" =>
            s.enabled(true)
            seriesData filterAndForeach(_.getName != "Total", _.enabled(false))
            chart.legend.setLabelAsClicked("Total")
          case _ =>
            //toggle series visibility
            s.enabled(!s.getNode.isVisible)
            if (s.getNode.isVisible) {
              chart.legend.setLabelAsClicked(s.getName)
              seriesData filterAndForeach(_.getName != s.getName, _.enabled(false))
            }
            else {
              seriesData.getSeries("Total") --> {
                _.enabled(true)
              }
              chart.legend.setLabelAsClicked("Total")
            }
        }
      }
    })
    chart
  }
}


