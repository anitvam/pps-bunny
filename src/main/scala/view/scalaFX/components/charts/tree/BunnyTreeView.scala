package view.scalaFX.components.charts.tree

import model.Bunny
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafx.scene.text.Text
import view.scalaFX.ScalaFxViewConstants.GenealogicalTree.{TREE_FONT_PROPORTION, TREE_INFO_PROPORTION}
import view.scalaFX.components.charts.tree.GenealogicalTreeView.{bunnyIconSize, spacingRegion}
import view.scalaFX.utilities.{BunnyImageUtils, Direction, ImageType}
import view.scalaFX.utilities.Direction.Right

/**
 * Represents the view on the Bunny in a tree.
 */
trait BunnyTreeView {
  /** Reference to the model bunny entity */
  val bunny: Bunny

  /** Pane with actual view of the bunny */
  val pane: Pane
}

object BunnyTreeView {
  def apply(bunny: Bunny): BunnyTreeView = BunnyTreeViewImpl(bunny)

  private case class BunnyTreeViewImpl(override val bunny: Bunny) extends BunnyTreeView {
      override val pane: Pane = treeBunnyView(bunny)
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

  private def treeBunnyView(bunny: Bunny): Pane =
    new VBox( bunnyView(bunny),
              allelesView(bunny),
              infoView(bunny))
}
