package view.scalaFX.components.charts.pedigree

import model.Bunny
import scalafx.geometry.Insets
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafx.scene.text.Text
import view.scalaFX.ScalaFXConstants.GenealogicalTree.{BUNNY_ALLELE_PADDING, FONT_INFO_PERCENT, BUNNY_INFO_PROPORTION}
import view.scalaFX.components.charts.pedigree.PedigreeChart.{bunnyIconSize, spacingRegion}
import view.scalaFX.utilities.Direction.Right
import view.scalaFX.utilities.{BunnyImageUtils, Direction, ImageType}

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

  private def bunnyView(bunny: Bunny): HBox = new HBox {

    children = Seq(
      spacingRegion,
      new ImageView {
        image = BunnyImageUtils.bunnyToImage(bunny, ImageType.Normal)
        fitWidth = bunnyIconSize
        fitHeight = bunnyIconSize
        preserveRatio = true
        scaleX = Direction.scaleXValue(Right)
      },
      spacingRegion
    )

    padding = Insets(top = 0, left = 0, bottom = BUNNY_ALLELE_PADDING, right = 0)
  }

  private def allelesView(bunny: Bunny): HBox = new HBox(
    spacingRegion,
    new Text {
      text = bunny.genotype.genes.values.map(g => g.momAllele.getLetter + g.dadAllele.getLetter + " ").reduce(_ + _)

      style = "-fx-font-family: \"Helvetica\"; " +
        "-fx-font-weight: bold; " +
        "-fx-font-size: " + bunnyIconSize / BUNNY_INFO_PROPORTION * FONT_INFO_PERCENT + "px"

    },
    spacingRegion
  )

  private def infoView(bunny: Bunny): HBox = new HBox(
    spacingRegion,
    if (bunny.alive) new Region() else deadImageView,
    if (bunny.genotype.isJustMutated) mutationImageView else new Region(),
    spacingRegion
  )

  private def deadImageView: ImageView = infoImageView("/img/death.png")

  private def mutationImageView: ImageView = infoImageView("/img/mutation.png")

  private def infoImageView(path: String): ImageView = new ImageView {
    image = new Image(path)
    fitWidth = bunnyIconSize / BUNNY_INFO_PROPORTION
    fitHeight = bunnyIconSize / BUNNY_INFO_PROPORTION
  }

  private case class BunnyPedigreeViewImpl(override val bunny: Bunny) extends BunnyPedigreeView {
    override val pane: Pane = new VBox(bunnyView(bunny), allelesView(bunny), infoView(bunny))
  }

}
