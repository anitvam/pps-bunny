package it.unibo.pps.bunny.view.scalaFX.components.charts.pedigree

import it.unibo.pps.bunny.model.bunny.Bunny
import scalafx.geometry.Insets
import scalafx.scene.image.{ Image, ImageView }
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.text.Text
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants.GenealogicalTree._
import it.unibo.pps.bunny.view.scalaFX.components.charts.pedigree.PedigreeChart.{ bunnyIconSize, spacingRegion }
import it.unibo.pps.bunny.view.scalaFX.utilities.Direction.Right
import it.unibo.pps.bunny.view.scalaFX.utilities.{ BunnyImageUtils, Direction, ImageType }

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

  private val bunnyView: Bunny => HBox = bunny =>
    new HBox {

      children = Seq(
        spacingRegion(),
        new ImageView {
          image = BunnyImageUtils.bunnyToImage(bunny, ImageType.Normal)
          fitWidth = bunnyIconSize
          fitHeight = bunnyIconSize
          preserveRatio = true
          scaleX = Direction.scaleXValue(Right)
        },
        spacingRegion()
      )

      padding = Insets(top = 0, left = 0, bottom = BUNNY_ALLELE_PADDING, right = 0)
    }

  private def genderView(bunny: Bunny): HBox = new HBox(
    spacingRegion(),
    new Text {
      text = bunny.gender.toString
      style = "-fx-font-size: " + bunnyIconSize / BUNNY_INFO_PROPORTION * FONT_INFO_PERCENT + "px;"
      fill = Color.DimGray
    },
    spacingRegion()
  )

  private val allelesView: Bunny => HBox = bunny =>
    new HBox(
      spacingRegion(),
      new Text {
        text = bunny.genotype.genes.values.map(g => g.momAllele.getLetter + g.dadAllele.getLetter).reduce(_ + " " + _)
        style = "-fx-font-size: " + bunnyIconSize / BUNNY_INFO_PROPORTION * FONT_INFO_PERCENT + "px;"
      },
      spacingRegion()
    )

  private val infoImageView: String => ImageView = path =>
    new ImageView {
      image = new Image(path)
      fitWidth = bunnyIconSize / BUNNY_INFO_PROPORTION
      fitHeight = bunnyIconSize / BUNNY_INFO_PROPORTION
    }

  private val deadImageView: ImageView = infoImageView("/img/death.png")

  private val mutationImageView: ImageView = infoImageView("/img/mutation.png")

  private val infoView: Bunny => HBox = bunny =>
    new HBox(
      spacingRegion(),
      if (bunny.alive) new Region() else deadImageView,
      if (bunny.genotype.isJustMutated) mutationImageView else new Region(),
      spacingRegion()
    )

  private case class BunnyPedigreeViewImpl(override val bunny: Bunny) extends BunnyPedigreeView {
    override val pane: Pane = new VBox(bunnyView(bunny), genderView(bunny), allelesView(bunny), infoView(bunny))
  }

}
