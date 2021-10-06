package it.unibo.pps.bunny.view.scalaFX.components.charts.pedigree

import it.unibo.pps.bunny.engine.SimulationConstants.MAX_GENEALOGICAL_TREE_GENERATIONS
import it.unibo.pps.bunny.model.Tree.generateTree
import it.unibo.pps.bunny.model.{ BinaryTree, Bunny, Node }
import scalafx.geometry.Pos
import scalafx.scene.image.ImageView
import scalafx.scene.layout._
import scalafx.scene.text.Text
import it.unibo.pps.bunny.util.PimpScala._
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants.GenealogicalTree._
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants.{ PREFERRED_CHART_HEIGHT, PREFERRED_CHART_WIDTH }

import scala.language.postfixOps
import scala.math.{ log10, min, pow }

trait PedigreeChart {

  /** Reference to the model bunny entity */
  val bunny: Bunny

  /** Reference to the model tree entity created from the bunny */
  val tree: BinaryTree[Bunny]

  /** The pane with the view of the tree */
  val chartPane: Pane
}

object PedigreeChart {

  /** The size required for the bunny icons */
  var bunnyIconSize: Int = MAX_TREE_BUNNY_SIZE

  /**
   * To create a chart with the standard panel size
   *
   * @param bunny
   *   the bunny that is the subject of the tree
   * @return
   *   the pedigree chart
   */
  def apply(bunny: Bunny): PedigreeChart = {
    this(bunny, PREFERRED_CHART_HEIGHT, PREFERRED_CHART_WIDTH)
  }

  /**
   * To create a chart with
   *
   * @param bunny
   *   the bunny that is the subject of the tree
   * @param chartWidth
   *   the width of the panel and maximum width of the tree
   * @param chartHeight
   *   the height of the panel and maximum height of the tree
   * @return
   *   the pedigree chart
   */
  def apply(bunny: Bunny, chartWidth: Int, chartHeight: Int): PedigreeChart = {
    var tree: BinaryTree[Bunny] = generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny)
    bunnyIconSize = maxBunnyIconsSizeInPanel(chartWidth, chartHeight, tree)

    // Cuts on generations number if the screen is not big enough
    if (bunnyIconSize < MIN_TREE_BUNNY_SIZE) {
      bunnyIconSize = MIN_TREE_BUNNY_SIZE
      val maxGenerations = maxGenerationsInPanel(chartWidth, chartHeight)
      tree = generateTree(maxGenerations, bunny)
    }

    PedigreeChartImpl(bunny, tree)
  }

  private def maxBunnyIconsSizeInPanel(chartWidth: Int, chartHeight: Int, tree: BinaryTree[Bunny]): Int = {
    val maxBunnySizeForWidth: Int = ((chartWidth * BUNNY_PLUS_PROPORTION) /
      (pow(BUNNY_PLUS_PROPORTION + 1, tree.generations - 1) - 1)).toInt
    val maxBunnySizeForHeight: Int = maxHeight(chartHeight, tree.generations)

    Seq(maxBunnySizeForHeight, maxBunnySizeForWidth, MAX_TREE_BUNNY_SIZE).min
  }

  private def maxGenerationsInPanel(chartWidth: Int, chartHeight: Int): Int = {
    val maxGenerationsForWidth: Int = (1 + log10(1 + (chartWidth + BUNNY_PLUS_PROPORTION) / bunnyIconSize) / log10(
      BUNNY_PLUS_PROPORTION + 1
    )).ceil.toInt
    val maxGenerationsForHeight: Int = maxHeight(chartHeight, bunnyIconSize)

    min(maxGenerationsForWidth, maxGenerationsForHeight)
  }

  private def maxHeight(height: Int, parameter: Int): Int = ((height * BUNNY_INFO_PROPORTION * FONT_INFO_PERCENT) /
    ((BUNNY_INFO_PROPORTION * FONT_INFO_PERCENT + 1 + FONT_INFO_PERCENT) * parameter)).toInt

  /**
   * @param trees
   *   The tree with the elems that need to be in this row
   * @return
   *   The view of a row and the trees which needs to be inserted in the next one
   */
  private def createRow(trees: Seq[Option[BinaryTree[Bunny]]]): (HBox, Seq[Option[BinaryTree[Bunny]]]) = {
    var nextTrees: Seq[Option[BinaryTree[Bunny]]] = Seq()
    var index = 0
    val row = new HBox {
      alignment = Pos.Center
      maxHeight = bunnyIconSize
      children = spacingRegion
    }

    trees.foreach(tree => {
      if (tree ?) row.children += BunnyPedigreeView(tree.get.elem).pane
      else row.children += emptyImageView

      index += 1
      if (index < trees.size) {
        row.children += spacingRegion
        if (index % 2 == 1 && (tree ?)) row.children += plusView else row.children += emptyPlusView
      }
      row.children += spacingRegion

      if ((tree ?) && tree.get.isInstanceOf[Node[Bunny]]) {
        nextTrees ++= Seq(
          Option(tree.get.asInstanceOf[Node[Bunny]].momTree),
          Option(tree.get.asInstanceOf[Node[Bunny]].dadTree)
        )
      } else {
        nextTrees ++= Seq(Option.empty, Option.empty)
      }
    })
    (row, nextTrees)
  }

  /** Creates a spacing region to justify the rows of bunnies */
  def spacingRegion: Region = new Region {
    hgrow = Priority.Always
  }

  /**
   * Creates an empty ImageView with the same size of the bunny, for the bunnies with no ancient relatives
   */
  def emptyImageView: ImageView = new ImageView {
    fitWidth = bunnyIconSize
  }

  /** Creates an empty Text with the same size of the plus, for the bunnies with no ancient relatives */
  def emptyPlusView: Text = {
    val txt = plusView
    txt setVisible false
    txt
  }

  /** Creates a view of the plus between couples of bunnies */
  def plusView: Text = new Text {
    text = "+"
    style = "-fx-font-weight: bold; -fx-font-size: " + bunnyIconSize / BUNNY_PLUS_PROPORTION + ";"
    hgrow = Priority.Always
  }

  private case class PedigreeChartImpl(override val bunny: Bunny, override val tree: BinaryTree[Bunny])
      extends PedigreeChart {

    var rows: Seq[HBox] = Seq()
    var row: (HBox, Seq[Option[BinaryTree[Bunny]]]) = (new HBox(), Seq(Option(tree)))

    for (_ <- 1 to tree.generations) {
      row = createRow(row._2)
      rows = rows :+ row._1
    }

    override val chartPane: VBox = new VBox {
      children = spacingRegion +: rows.reverse :+ spacingRegion
      alignment = Pos.Center
    }

  }

}