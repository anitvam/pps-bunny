package view.scalaFX.components.charts

import model.genome.Alleles
import model.genome.Alleles.AlleleKind
import model.world.Generation.Population
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import view.scalaFX.components.charts.LineChartComponentFactory.{createDataXYChart, createEmptySeries}
import view.scalaFX.components.charts.PopulationChartDataType._
import view.scalaFX.utilities.PimpScalaFXChartLibrary._

import scala.language.implicitConversions

object PopulationChartDataType{
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
  case class ChartSeries (seriesData: SeriesData, xySeries:XYChart.Series[Number,Number]){
    def + (p:Point): Unit ={
      seriesData ^ p
      seriesData.data takeRight 2 foreach {xySeries += createDataXYChart(_, xySeries.getNode.isVisible)}
    }
  }

  case class MutationsChartSeries(var mutationMap: Map[AlleleKind, ChartSeries] = Map()){
    Alleles.values.foreach{ak => mutationMap = mutationMap + (ak -> ChartSeries(SeriesData(), createEmptySeries(ak.toString)))}
    def seriesData : Seq[SeriesData] = mutationMap.values.map(_.seriesData).toSeq
    def xySeries:List[XYChart.Series[Number, Number]] = mutationMap.values.map(_.xySeries).toList
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
  val chart: LineChart[Number, Number] = createLineChart(xAxis, yAxis, 325, 500, total.xySeries :: mutations.xySeries)

  def updateChart(generationPhase:Double, population:Population): Unit = {
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
  implicit def fromTupleToPoint(t:(Double, Int)): Point = Point(t._1, t._2)
  implicit def fromTupleToChartPoint(t:(Point,Boolean)):ChartPoint = ChartPoint(t._1, t._2)
  implicit def fromTupleToChartPoint(t:(Double,Int,Boolean)):ChartPoint = ChartPoint(Point(t._1,t._2), t._3)
  object lastPopulationValue{
    def unapply(s:Seq[ChartPoint]):Option[Int] = Some(s.last.point.y)
  }
}

object LineChartComponentFactory{

  def createNumberAxis(name:String, lowerBound:Int, upperBound:Int, tickUnit:Int): NumberAxis ={
    val axis = NumberAxis(name)
    axis.lowerBound = lowerBound
    axis.upperBound = upperBound
    axis.tickUnit = tickUnit
    axis.minorTickVisible = false
    axis.autoRanging = false
    axis
  }

  def createEmptySeries(name:String): XYChart.Series[Number, Number] = createSeries(name, SeriesData())

  def createSeries(name:String, s: SeriesData): XYChart.Series[Number, Number] = {
    XYChart.Series[Number, Number](
      name = name,
      ObservableBuffer.from(s.data.map({d => XYChart.Data[Number, Number](d.point.x,d.point.y)}))
    )
  }

  def createDataXYChart(dataChart: ChartPoint, seriesVisibility:Boolean): XYChart.Data[Number, Number] = dataChart match {
    case ChartPoint((x, y), v)  =>
      val point = XYChart.Data[Number, Number](x, y)
      point.nodeProperty().addListener(_ => {
        point.getNode.setStyle("-fx-scale-x: 0.7; -fx-scale-y: 0.7;")
        point.setExtraValue(v)
        point.getNode.visible = if(v) seriesVisibility else false
      })
      point
  }

  def createLineChart(xAxis:NumberAxis, yAxis:NumberAxis,
                      chartHeight:Int, chartWidth:Double,
                      seriesData:Seq[XYChart.Series[Number,Number]]): LineChart[Number,Number] = {

    val chart = new LineChart(xAxis, yAxis) {
      maxHeight = chartHeight
      maxWidth = chartWidth
      legendSide = Side.Right
    }
    chart ++= seriesData

    seriesData.filterAndForeach(_.getName == "Total", _.enabled(false))

    chart.legend.getItems.foreach(li => {
      seriesData.filterAndForeach(_.getName == li.getText, s =>
        li.getSymbol.onMouseClicked = _ => {
          if(s.getName != "Total"){
            s.enabled(!s.getNode.isVisible)
            if(s.getNode.isVisible)
              seriesData filterAndForeach(ms => ms.getName != "Total" &&  ms.getName != s.getName, _.enabled(false))
            else
              seriesData filterAndForeach (_.getName == "Total", _.enabled(true))
          } else {
            seriesData filterAndForeach(_.getName != "Total", _.enabled(false))
          }
        }
      )
    })
    chart
  }
}


