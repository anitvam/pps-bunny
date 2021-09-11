package view.scalaFX.components

import model.genome.Alleles
import model.genome.Alleles.AlleleKind
import model.world.Generation.Population
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import view.scalaFX.components.PopulationChart.{ChartData, SeriesData}

import scala.language.implicitConversions

object PopulationChart {
  import LineChartComponentFactory._
  type Data = (Double, Int)
  type ChartData = (Data, Boolean)
  type SeriesData = Seq[ChartData]
  type ChartSeriesData = Map[AlleleKind, SeriesData]

  var mutationsData:ChartSeriesData = Map()
  Alleles.values.foreach(a => mutationsData += a -> Seq())
  var totalData:SeriesData = Seq()
  var totalSeries: XYChart.Series[Number, Number] = createMySeries("Total", totalData)
  val dataPairs = Seq((0, 50), (1, 80), (2, 90), (3, 30), (4, 122), (5, 10))

  val chart: LineChart[Number, Number] = new LineChart(createNumberAxis("Generation Axis", 0, 6, 1),
    createNumberAxis("Population Axis", 0, 100, 10)) {
    maxHeight = 325
    maxWidth = 500
    legendSide = Side.Right
    data = totalSeries
  }

  def updateChart(generationPhase:Double, population:Population): Unit = {
    val data:Data = (generationPhase, population.size)
    if(totalData.nonEmpty) totalData = totalData :+ ((generationPhase,totalData.last._1._2), false)
    totalData = totalData :+ data
    updateSeries()
  }

  implicit def fromDataToChartData(data:Data): ChartData = (data, true)

  def updateSeries(): Unit = Platform.runLater{
    totalData.takeRight(2).foreach( p => totalSeries.dataProperty().value.add(getDataXYChart(p)))
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

  def createMySeries(name:String, data: SeriesData): XYChart.Series[Number, Number] = {
    XYChart.Series[Number, Number](
      name = name,
      ObservableBuffer.from(data.map({
        case ((x, y), true) =>
          val truePoint = XYChart.Data[Number, Number](x, y)
          truePoint.nodeProperty().addListener(_ => {
            truePoint.getNode.setStyle("-fx-scale-x: 0.7; -fx-scale-y: 0.7; -fx-background-color: #e9967a;")
          })
          truePoint

        case ((x, y), false) =>
          val fakePoint = XYChart.Data[Number, Number](x, y)
          fakePoint.nodeProperty().addListener(_ => fakePoint.getNode.setVisible(false))
          fakePoint
      }))
    )
  }

  def getDataXYChart(dataChart: ChartData): XYChart.Data[Number, Number] = dataChart match {
    case ((x, y), true)  =>
      val truePoint = XYChart.Data[Number, Number](x, y)
      truePoint.nodeProperty().addListener(_ => {
        truePoint.getNode.setStyle("-fx-scale-x: 0.7; -fx-scale-y: 0.7; -fx-background-color: #e9967a;")
      })
      truePoint


    case ((x, y), false) =>
      val fakePoint = XYChart.Data[Number, Number](x, y)
      fakePoint.nodeProperty().addListener(_ => fakePoint.getNode.setVisible(false))
      fakePoint
  }

}
