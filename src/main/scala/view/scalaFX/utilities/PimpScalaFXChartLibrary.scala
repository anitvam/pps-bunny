package view.scalaFX.utilities

import scalafx.Includes._
import com.sun.javafx.charts.Legend
import scalafx.scene.chart.{LineChart, XYChart}

import java.util.function.Consumer

object PimpScalaFXChartLibrary {
  implicit class RichXYChartSeries[A,B](series:XYChart.Series[A,B]) {
    def enabled(isVisible:Boolean): Unit = {
      series.getNode.visible = isVisible
      series.getData.filter(_.getExtraValue.asInstanceOf[Boolean]) foreach {_.getNode.visible = isVisible}
    }

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
      series filter pred foreach {consumer}
    }
  }
}