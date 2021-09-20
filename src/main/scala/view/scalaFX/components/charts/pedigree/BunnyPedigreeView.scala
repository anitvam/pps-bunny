package view.scalaFX.components.charts.pedigree

import model.Bunny
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafx.scene.text.Text
import view.scalaFX.ScalaFxViewConstants.GenealogicalTree.{TREE_FONT_PROPORTION, TREE_INFO_PROPORTION}
import view.scalaFX.components.charts.pedigree.PedigreeChart.{bunnyIconSize, spacingRegion}
import view.scalaFX.utilities.{BunnyImageUtils, Direction, ImageType}
import view.scalaFX.utilities.Direction.Right

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

  private case class BunnyPedigreeViewImpl(override val bunny: Bunny) extends BunnyPedigreeView {
      override val pane: Pane = new VBox( bunnyView(bunny), allelesView(bunny), infoView(bunny))
  }

  private def bunnyView(bunny: Bunny): ImageView = new ImageView {
    image = BunnyImageUtils.bunnyToImage(bunny, ImageType.Normal)
    fitWidth = bunnyIconSize
    fitHeight = bunnyIconSize
    preserveRatio = true
    scaleX = Direction.scaleXValue(Right)
  }

  private def allelesView(bunny: Bunny): HBox = new HBox(
    spacingRegion,
    new Text {
      text = bunny.genotype.genes.values
        .map(g => g.momAllele.getLetter + g.dadAllele.getLetter + " ")
        .reduce(_+_)
      style = "-fx-font-family: \"Helvetica\"; " +
        "-fx-font-weight: bold; " +
        "-fx-font-size: "+ bunnyIconSize/TREE_FONT_PROPORTION+"pt"},
    spacingRegion)

  private def infoImageView(path: String): ImageView = new ImageView {
    image = new Image(path)
    fitWidth = bunnyIconSize/TREE_INFO_PROPORTION
    fitHeight = bunnyIconSize/TREE_INFO_PROPORTION
  }

  private def deadImageView: ImageView = infoImageView("/img/death.png")
  private def mutationImageView: ImageView = infoImageView("/img/mutation.png")

  private def infoView(bunny: Bunny): HBox =
    new HBox( spacingRegion,
              if (bunny.alive) new Region() else deadImageView,
              if (bunny.genotype.isJustMutated) mutationImageView else new Region(),
              spacingRegion)
}
