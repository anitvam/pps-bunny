package view.scalaFX.components.tree

import model.Bunny
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafx.scene.text.Text
import view.scalaFX.components.tree.TreeView.{emptyRegion, spacingRegion}
import view.scalaFX.components.tree.TreeViewConstants.{BUNNY_SIZE, INFO_SIZE}
import view.scalaFX.utilities.Direction
import view.scalaFX.utilities.Direction.Right
import view.utilities.{BunnyImageUtils, ImageType}

trait BunnyTreeView {
  val bunny: Bunny
  val bunnyPane: Pane
}

object BunnyTreeView {
  def apply(bunny: Bunny): BunnyTreeView = {
    BunnyTreeViewImpl(bunny)
  }

  private case class BunnyTreeViewImpl(bunny: Bunny) extends BunnyTreeView{
      override val bunnyPane = treeBunnyView(bunny)
  }

  private def bunnyView(bunny: Bunny): ImageView = new ImageView {
    image = BunnyImageUtils.bunnyToImage(bunny, ImageType.Normal)
    fitWidth = BUNNY_SIZE
    fitHeight = BUNNY_SIZE
    preserveRatio = true
    scaleX = Direction.scaleXValue(Right)
  }

  private def deadImageView: ImageView = new ImageView {
    image = new Image("/img/death.png")
    fitWidth = INFO_SIZE
    fitHeight = INFO_SIZE
  }

  private def mutationImageView: ImageView = new ImageView {
    image = new Image("/img/mutation.png")
    fitWidth = INFO_SIZE
    fitHeight = INFO_SIZE
  }

  private def infoView(bunny: Bunny): HBox =
    new HBox( spacingRegion,
              if (bunny.alive) emptyRegion else deadImageView,
              if (bunny.genotype.isJustMutated) mutationImageView else emptyRegion,
              spacingRegion)

  private def allelesView(bunny: Bunny): Text = {
    val txt = new Text(bunny.genotype.genes.values
      .map(g => g.momAllele.letter + g.dadAllele.letter + " ")
      .reduce(_+_))
    txt.setStyle( "-fx-font-family: \"Helvetica\"; " +
                  "-fx-font-weight: bold; " +
                  "-fx-font-size: 10pt")
    txt
  }

  private def treeBunnyView(bunny: Bunny): Pane =
    new VBox( bunnyView(bunny),
              new HBox( spacingRegion, allelesView(bunny), spacingRegion),
              infoView(bunny))
}
