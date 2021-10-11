package it.unibo.pps.bunny.view.scalaFX.utilities

import com.sun.javafx.charts.Legend
import javafx.scene.control.Label
import scalafx.Includes._
import scalafx.collections.ObservableBuffer
import scalafx.scene.chart.{ LineChart, PieChart, XYChart }
import it.unibo.pps.bunny.util.PimpScala._
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants.Style.PopulationLegend.{ CLICKED_ITEM_STYLE, ITEM_STYLE }

object PimpScalaFXChartLibrary {

  /** A richer version of [[XYChart.Series]] */
  implicit class RichXYChartSeries[A, B](series: XYChart.Series[A, B]) {

    /** @return true if the series are visible, otherwise false */
    def enabled: Boolean = series.getNode.isVisible

    /**
     * Sets the series and its point visibility
     *
     * @param isVisible
     *   specifies if they are visible
     */
    def enabled_=(isVisible: Boolean): Unit = {
      series.getNode.visible = isVisible
      series.getData.filter(_.getExtraValue.asInstanceOf[Boolean]) foreach { _.getNode.visible = isVisible }
    }

    /**
     * Adds a style to the Style list of the series
     *
     * @param style
     *   the name of style
     */
    def addStyle(style: String): Unit = {
      series.getNode.styleClass += style
      series.getData.foreach(_.getNode.styleClass ++= Seq("chart-line-mySymbol", style))
    }

    /**
     * Adds a new [[XYChart.Data]] to the series
     *
     * @param data
     *   the new point
     * @return
     *   true if the collection has changed
     */
    def +=(data: XYChart.Data[A, B]): Boolean = series.dataProperty().value.add(data)
  }

  /** A richer version of [[XYChart]] */
  implicit class RichLineChart[A, B](chart: LineChart[A, B]) {

    /** @return the chart legend */
    def legend: Legend = chart.getChildrenUnmodifiable.find(_.isInstanceOf[Legend]).map(l => l.asInstanceOf[Legend]).get

    /**
     * Add a sequence of [[XYChart.Series]] to the chart data
     * @param series
     *   a [[Seq]] of [[XYChart.Series]]
     */
    def ++=(series: Seq[XYChart.Series[A, B]]): Unit = series foreach { chart += _ }

    /**
     * Add a [[XYChart.Series]] to the chart data
     * @param series
     *   a [[Seq]] of [[XYChart.Series]]
     */
    def +=(series: XYChart.Series[A, B]): Boolean = chart.getData.add(series)
  }

  /** A richer version of a sequence of [[XYChart.Series]] */
  implicit class RichSeqChartSeries[A, B](series: Seq[XYChart.Series[A, B]]) {

    /**
     * Filters the sequence and consumes any [[XYChart.Series]] that satisfies the predicate
     * @param pres
     *   predicate on the [[XYChart.Series]]
     * @param consumer
     *   consumer applied to the [[XYChart.Series]]
     */
    def filterAndForeach(pred: XYChart.Series[A, B] => Boolean, consumer: XYChart.Series[A, B] => Unit): Unit = {
      series filter pred foreach consumer
    }

    /**
     * Returns the first series that have the specified name
     * @param name
     *   the specified name
     * @return
     *   the first occurrence of [[XYChart.Series]]
     */
    def getSeries(name: String): Option[XYChart.Series[A, B]] = series find { _.getName == name }
  }

  /** A richer version of [[Legend]] */
  implicit class RichChartLegend(legend: Legend) {

    /**
     * A getter for easier access to the label of the specified value
     *
     * @param value
     *   the name of the label you are looking for
     * @return
     *   the label if present
     */
    def label(value: String): Option[Label] =
      legend.getChildrenUnmodifiable.find(_.asInstanceOf[Label].text.value == value).map(_.asInstanceOf[Label])

    /** @return a [[Seq]] containing all the chart labels */
    def getLabels: Seq[Label] = legend.getChildrenUnmodifiable.collect {
      case l if l.isInstanceOf[Label] => l.asInstanceOf[Label]
    }.toSeq

    /**
     * Set the specified label as clicked by adding a specific style
     * @param value
     *   the specified label
     */
    def setLabelAsClicked(value: String): Unit = label(value) --> { _.styleClass += CLICKED_ITEM_STYLE }

    /**
     * Set the specified label as unclicked by removing a specific style
     * @param value
     *   the specified label
     */
    def setLabelAsUnClicked(value: String): Unit = label(value) --> { _.styleClass -= CLICKED_ITEM_STYLE }

  }

  /** Class that enrich the [[PieChart]] implementation */
  implicit class RichPieChart(chart: PieChart) {

    def +=(data: Seq[(String, Double)]): Unit = {
      val cssClass: PieChart.Data => String = _.getName.replace(" ", "_") + "_Pie"
      chart.getData.clear()
      chart.getData ++= ObservableBuffer.from(data.map({ case (x, y) => PieChart.Data(x, y) }))
      chart.getData.foreach { d => d.getNode.styleClass += cssClass(d) }
      chart.legend.getItems foreach { i =>
        chart.getData.find(d => i.getText.contains(d.getName)) --> { d =>
          {
            i.getSymbol.styleClass += cssClass(d)
            i.setText(i.getText + " " + d.getPieValue + "%")
          }
        }
      }
    }

    /** @return the chart legend */
    def legend: Legend = chart.getChildrenUnmodifiable.find(_.isInstanceOf[Legend]).map(l => l.asInstanceOf[Legend]).get

  }

}
