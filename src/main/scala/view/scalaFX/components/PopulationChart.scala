package view.scalaFX.components

import com.sun.javafx.charts.Legend
import model.genome.Alleles
import model.genome.Alleles.AlleleKind
import model.world.Generation.Population
import scalafx.Includes._
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.geometry.Side
import scalafx.scene.chart.{LineChart, NumberAxis, XYChart}
import view.scalaFX.components.PopulationChart.{ChartData, SeriesData, totalSeries}

import scala.language.implicitConversions

object PopulationChart {
  import LineChartComponentFactory._
  type Data = (Double, Int)
  type ChartData = (Data, Boolean)
  type SeriesData = Seq[ChartData]
  type ChartSeriesData = Map[AlleleKind, SeriesData]

  var mutationsData:ChartSeriesData = Map()
  var mutationsSeries:Map[AlleleKind, XYChart.Series[Number, Number]] = Map()
  Alleles.values.foreach(a => {
    mutationsData += a -> Seq()
    mutationsSeries += a -> createSeries(a.toString, Seq())
  })
  var totalData:SeriesData = Seq()
  var totalSeries: XYChart.Series[Number, Number] = createSeries("Total", totalData)
  val chartData: Seq[XYChart.Series[Number, Number]] = totalSeries :: mutationsSeries.values.toList

  val xAxis: NumberAxis = createNumberAxis("Generation Axis", 0, 6, 1)
  val yAxis: NumberAxis =  createNumberAxis("Population Axis", 0, 100, 10)
  val chart: LineChart[Number, Number] = createLineChart(xAxis, yAxis, 325, 500, chartData)

  def updateChart(generationPhase:Double, population:Population): Unit = {
    totalData = updatedData(totalData, (generationPhase, population.size))
    updateSeries(totalData, totalSeries)
    Alleles.values.foreach(ak => {
      mutationsData += ak -> updatedData(mutationsData(ak), (generationPhase, population.count(_.genotype.phenotype.visibleTraits.values.toSet.contains(ak))))
      updateSeries(mutationsData(ak), mutationsSeries(ak))
    })
    updateChartBound(generationPhase, population.size)
  }

  def updatedData(seriesData:SeriesData, d: Data) : SeriesData =  seriesData match {
    case Seq() => seriesData :+ (d, true)
    case lastPopulationValue(p) => seriesData ++ Seq(((d._1,p), false),(d, p != d._2))
  }

  def updateSeries(seriesData: SeriesData, series:XYChart.Series[Number,Number]): Unit = Platform.runLater{
    seriesData.takeRight(2).foreach( p => {
      val sd = createDataXYChart(p, series.getNode.isVisible)
      series.dataProperty().value.add(sd)
    })
  }

  def updateChartBound(generationPhase: Double, size: Int): Unit = {
    if (generationPhase > xAxis.upperBound.toDouble) xAxis.upperBound = generationPhase + 2
    if (size > yAxis.upperBound.toInt) yAxis.upperBound = size + 10
  }
}

object lastPopulationValue{
  def unapply(s:SeriesData):Option[Int] = Some(s.last._1._2)
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

  def createSeries(name:String, data: SeriesData): XYChart.Series[Number, Number] = {
    XYChart.Series[Number, Number](
      name = name,
      ObservableBuffer.from(data.map({d => XYChart.Data[Number, Number](d._1._1,d._1._2)}))
    )
  }

  def createDataXYChart(dataChart: ChartData, seriesVisibility:Boolean): XYChart.Data[Number, Number] = dataChart match {
    case ((x, y), true)  =>
      val truePoint = XYChart.Data[Number, Number](x, y)
      truePoint.nodeProperty().addListener(_ => {
        truePoint.getNode.setStyle("-fx-scale-x: 0.7; -fx-scale-y: 0.7;")
        truePoint.setExtraValue(true)
        truePoint.getNode.visible = seriesVisibility
      })
      truePoint

    case ((x, y), false) =>
      val fakePoint = XYChart.Data[Number, Number](x, y)
      fakePoint.nodeProperty().addListener(_ => {
        fakePoint.getNode.setVisible(false)
        fakePoint.setExtraValue(false)
      })
      fakePoint
  }

  def createLineChart(xAxis:NumberAxis, yAxis:NumberAxis,
                      chartHeight:Int, chartWidth:Double,
                      seriesData:Seq[XYChart.Series[Number,Number]]): LineChart[Number,Number] = {

    val chart = new LineChart(xAxis, yAxis) {
      maxHeight = chartHeight
      maxWidth = chartWidth
      legendSide = Side.Right
    }
    seriesData.foreach(s => {
      chart.getData.add(s)
      //At the beginning only the series "Total" is shown
      if(s.getName != "Total") {
        s.getNode.visible = false
        s.getData.foreach(_.getNode.visible = false)
      }
    })
    chart.getChildrenUnmodifiable.find(_.isInstanceOf[Legend])
      .map(l => l.asInstanceOf[Legend]).get.getItems.foreach(li => {
      seriesData.find(_.getName == li.getText).foreach(s =>
        li.getSymbol.onMouseClicked = _ => {
          s.getNode.visible = !s.getNode.isVisible
          s.getData.filter(_.getExtraValue.asInstanceOf[Boolean]) foreach {_.getNode.visible = s.getNode.isVisible}
          if(s.getNode.isVisible)
            seriesData.filterNot(_.getName == s.getName) foreach { s=> {
              s.getNode.visible = false
              s.getData.foreach(_.getNode.visible = false)
            }}
          else {
            totalSeries.getNode.visible = true
            totalSeries.getData.filter(_.getExtraValue.asInstanceOf[Boolean]) foreach { _.getNode.visible = true }
          }
        }
      )
    })
    chart
  }


}
