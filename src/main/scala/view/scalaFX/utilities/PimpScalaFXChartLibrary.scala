package view.scalaFX.utilities

import scalafx.Includes._
import com.sun.javafx.charts.Legend
import javafx.scene.control.Label
import scalafx.scene.chart.{LineChart, XYChart}
import view.scalaFX.utilities.PimpScala._
import java.util.function.Consumer

object PimpScalaFXChartLibrary {
  implicit class RichXYChartSeries[A,B](series:XYChart.Series[A,B]) {
    def enabled(isVisible:Boolean): Unit = {
      series.getNode.visible = isVisible
      series.getData.filter(_.getExtraValue.asInstanceOf[Boolean]) foreach {_.getNode.visible = isVisible}
    }
    def enabled : Boolean = series.getNode.isVisible

    def addStyle(style:String): Unit = series.getNode.styleClass += style

    def += (data:XYChart.Data[A,B]): Boolean = series.dataProperty().value.add(data)
  }

  implicit class RichLineChart[A,B](chart:LineChart[A,B]){
    def legend :Legend = chart.getChildrenUnmodifiable
      .find(_.isInstanceOf[Legend])
      .map(l => l.asInstanceOf[Legend]).get

    def += (series:XYChart.Series[A,B]): Boolean = chart.getData.add(series)

    def ++= (series:Seq[XYChart.Series[A,B]]): Unit = series foreach {chart += _ }
  }

  implicit class RichSeqChartSeries[A,B](series:Seq[XYChart.Series[A,B]]) {
    def filterAndForeach(pred:XYChart.Series[A,B] => Boolean, consumer: XYChart.Series[A,B] => Unit): Unit ={
      series filter pred foreach consumer
    }

    def getSeries(name:String):Option[XYChart.Series[A,B]] = series find{_.getName == name}
  }

  implicit class RichChartLegend(legend:Legend){
    def label(value:String):Option[Label] = legend.getChildrenUnmodifiable
      .find(_.asInstanceOf[Label].text.value == value).map(_.asInstanceOf[Label])

    def getLabels:Seq[Label] = legend.getChildrenUnmodifiable.collect{
      case l if l.isInstanceOf[Label] => l.asInstanceOf[Label]
    }.toSeq

    def setLabelAsClicked(value:String): Unit = {
      getLabels.foreach(_.styleClass -= "chart-legend-item-clicked")
      label(value) --> {_.styleClass += "chart-legend-item-clicked"}
    }

  }

}