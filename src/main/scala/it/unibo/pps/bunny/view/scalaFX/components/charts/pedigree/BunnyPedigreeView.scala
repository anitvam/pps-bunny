package it.unibo.pps.bunny.view.scalaFX.components.charts.pedigree

import it.unibo.pps.bunny.model.bunny.Bunny
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants.GenealogicalTree._
import it.unibo.pps.bunny.view.scalaFX.components.charts.pedigree.PedigreeChart.{bunnyIconSize, spacingGenerator}
import it.unibo.pps.bunny.view.scalaFX.utilities.Direction.Right
import it.unibo.pps.bunny.view.scalaFX.utilities.{BunnyImageUtils, Direction, ImageType}
import scalafx.geometry.Insets
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.text.Text

/**
 * Represents the view on the Bunny in a tree.
 */
trait BunnyPedigreeView {

  /** Reference to the model bunny entity */
  val bunny: Bunny

  /** Pane with actual view of the bunny */
  val pane: Pane
}

object BunnyPedigreeView {

  def apply(bunny: Bunny): BunnyPedigreeView = BunnyPedigreeViewImpl(bunny)

  private val bunnyViewer: Bunny => HBox = bunny =>
    new HBox {
      children = Seq(
        spacingGenerator(),
        new ImageView {
          image = BunnyImageUtils.bunnyToImage(bunny, ImageType.Normal)
          fitWidth = bunnyIconSize
          fitHeight = bunnyIconSize
          preserveRatio = true
          scaleX = Direction.scaleXValue(Right)
        },
        spacingGenerator()
      )
    }

  private val genderViewer: Bunny => HBox = bunny =>
    new HBox(
      spacingGenerator(),
      new Text {
        text = bunny.gender.toString
        styleClass = Iterable("tree-bunny-text")
        style = "-fx-font-size: " + bunnyIconSize / BUNNY_FONT_PROPORTION + "px;"
        fill = Color.DimGray
      },
      spacingGenerator()
    )

  private val allelesViewer: Bunny => HBox = bunny =>
    new HBox(
      spacingGenerator(),
      new Text {
        text = bunny.genotype.toString
        styleClass = Iterable("tree-bunny-text")
        style = "-fx-font-size: " + bunnyIconSize / BUNNY_FONT_PROPORTION + "px;"
      },
      spacingGenerator()
    )

  private def infoImage(path: String) : ImageView =
    new ImageView {
      image = new Image(path)
      fitWidth = bunnyIconSize / BUNNY_INFO_PROPORTION
      fitHeight = bunnyIconSize / BUNNY_INFO_PROPORTION
    }

  private val deadImageGenerator: () => ImageView = () => infoImage("/img/death.png")

  private val mutationImageGenerator: () => ImageView = () => infoImage("/img/mutation.png")

  private val infoViewer: Bunny => HBox = bunny =>
    new HBox(
      spacingGenerator(),
      if (bunny.alive) new Region() else deadImageGenerator(),
      if (bunny.genotype.isJustMutated) mutationImageGenerator() else new Region(),
      spacingGenerator()
    )

  private case class BunnyPedigreeViewImpl(override val bunny: Bunny) extends BunnyPedigreeView {
    override val pane: Pane = new VBox(bunnyViewer(bunny), genderViewer(bunny), allelesViewer(bunny), infoViewer(bunny))
  }

}
