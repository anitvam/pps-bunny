package view.scalaFX.utilities

import scalafx.Includes._
import com.sun.javafx.charts.Legend

import javafx.scene.control.Label
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{LineChart, PieChart, XYChart}
import view.scalaFX.utilities.PimpScala._

object PimpScalaFXChartLibrary {
  /**A richer version of [[XYChart.Series]]*/
  implicit class RichXYChartSeries[A,B](series:XYChart.Series[A,B]) {
    /**Sets the series and its point visibility
     * @param isVisible specifies if they are visible*/
    def enabled_=(isVisible:Boolean): Unit = {
      series.getNode.visible = isVisible
      series.getData.filter(_.getExtraValue.asInstanceOf[Boolean]) foreach {_.getNode.visible = isVisible}
    }
    /**A getter to know if the series is visible*/
    def enabled : Boolean = series.getNode.isVisible

    /**Adds a style to the Style list of the series
     * @param style the name of style*/
    def addStyle(style:String): Unit = series.getNode.styleClass += style

    /**Adds a new [[XYChart.Data]] to the series
     * @param data the new point*/
    def += (data:XYChart.Data[A,B]): Boolean = series.dataProperty().value.add(data)
  }

  /**A richer version of [[XYChart]]*/
  implicit class RichLineChart[A,B](chart:LineChart[A,B]){
    /**A getter for easier access to the chart legend*/
    def legend :Legend = chart.getChildrenUnmodifiable
      .find(_.isInstanceOf[Legend])
      .map(l => l.asInstanceOf[Legend]).get

    /**Add a [[XYChart.Series]] to the chart data*/
    def += (series:XYChart.Series[A,B]): Boolean = chart.getData.add(series)
    /**Add a sequence of [[XYChart.Series]] to the chart data*/
    def ++= (series:Seq[XYChart.Series[A,B]]): Unit = series foreach {chart += _ }
  }

  /**A richer version of a sequence of [[XYChart.Series]]*/
  implicit class RichSeqChartSeries[A,B](series:Seq[XYChart.Series[A,B]]) {
    /**Filters the sequence and consumes any series that satisfies the predicate*/
    def filterAndForeach(pred:XYChart.Series[A,B] => Boolean, consumer: XYChart.Series[A,B] => Unit): Unit ={
      series filter pred foreach consumer
    }
    /**Returns the first series that has the specified name*/
    def getSeries(name:String):Option[XYChart.Series[A,B]] = series find{_.getName == name}
  }

  /**A richer version of [[Legend]]*/
  implicit class RichChartLegend(legend:Legend){
    /**A getter for easier access to the label of the specified value
     * @param value the name of the label you are looking for
     * @return the label if present */
    def label(value:String):Option[Label] = legend.getChildrenUnmodifiable
      .find(_.asInstanceOf[Label].text.value == value).map(_.asInstanceOf[Label])

    /**A getter for easier access to all the chart labels*/
    def getLabels:Seq[Label] = legend.getChildrenUnmodifiable.collect{
      case l if l.isInstanceOf[Label] => l.asInstanceOf[Label]
    }.toSeq

    /**Set the specified label as clicked by adding a specific style*/
    def setLabelAsClicked(value:String): Unit = {
      getLabels.foreach(_.styleClass -= "chart-legend-item-clicked")
      label(value) --> {_.styleClass += "chart-legend-item-clicked"}
    }
  }

  implicit class RichPieChart(chart:PieChart){
    /**A getter for easier access to the chart legend*/
    def legend :Legend = chart.getChildrenUnmodifiable
      .find(_.isInstanceOf[Legend])
      .map(l => l.asInstanceOf[Legend]).get

    def += (data:Seq[(String, Double)]): Unit = {
      chart.getData.clear()
      chart.getData ++= ObservableBuffer.from(data.map({ case (x, y) =>
        PieChart.Data(x, y)
      }))
      chart.getData.foreach{ d => d.getNode.styleClass += d.getName.replace(" ", "_")+"pie" }
      chart.legend.getItems foreach { i =>
        chart.getData.find(d => i.getText.contains(d.getName)) -->
          { d =>{
            i.getSymbol.styleClass += d.getName.replace(" ", "_")+"pie"
            i.setText(i.getText + " " + d.getPieValue + "%")
          } }
      }
    }

  }

}