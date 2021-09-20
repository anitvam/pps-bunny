package view.scalaFX.components.charts.pedigree

import engine.SimulationConstants.MAX_GENEALOGICAL_TREE_GENERATIONS
import model.Tree.generateTree
import model.{BinaryTree, Bunny, Node}
import scalafx.geometry.Pos
import scalafx.scene.image.ImageView
import scalafx.scene.layout._
import scalafx.scene.text.Text
import util.PimpScala._
import view.scalaFX.ScalaFxViewConstants.GenealogicalTree.{FONT_INFO_PERCENT, TREE_BUNNY_SIZE, TREE_INFO_PROPORTION, TREE_PLUS_PROPORTION}
import view.scalaFX.ScalaFxViewConstants.{PREFERRED_CHART_HEIGHT, PREFERRED_CHART_WIDTH}

import scala.language.postfixOps

trait PedigreeChart{
  /** Reference to the model bunny entity */
  val bunny: Bunny

  /** Reference to the model tree entity created from the bunny */
  val tree: BinaryTree[Bunny]

  /** The pane with the view of the tree */
  val chartPane: Pane
}

object PedigreeChart {
  /** The size required for the bunny icons*/
  var bunnyIconSize: Int = TREE_BUNNY_SIZE

  def apply (bunny:Bunny): PedigreeChart = {
    this (bunny, PREFERRED_CHART_HEIGHT, PREFERRED_CHART_WIDTH)
  }

  def apply(bunny: Bunny, panelWidth: Int, panelHeight:Int): PedigreeChart = {
    val tree: BinaryTree[Bunny]= generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny)
    val maxBunnySizeForWidth: Int = ((panelWidth * TREE_PLUS_PROPORTION) /
      (Math.pow(TREE_PLUS_PROPORTION + 1, tree.generations - 1) - 1)).toInt
    val maxBunnySizeForHeight: Int = ((panelHeight * TREE_INFO_PROPORTION * FONT_INFO_PERCENT) /
      ((TREE_INFO_PROPORTION * FONT_INFO_PERCENT + 1 + FONT_INFO_PERCENT) * tree.generations)).toInt
    bunnyIconSize = Math.min(maxBunnySizeForHeight, maxBunnySizeForWidth)
    println(maxBunnySizeForWidth)
    println(maxBunnySizeForHeight)
    PedigreeChartImpl(bunny, tree)
  }

  private case class PedigreeChartImpl(override val bunny: Bunny,
                                  override val tree: BinaryTree[Bunny]) extends PedigreeChart{
    var rows: Seq[HBox] = Seq()
    var row: (HBox, Seq[Option[BinaryTree[Bunny]]]) = (new HBox(), Seq(Option(tree)))
    for (_ <- 1 to tree.generations){
      row = createRow(row._2)
      rows = rows :+ row._1
    }

    override val chartPane = new VBox {
        children = spacingRegion +: rows.reverse :+ spacingRegion
        alignment = Pos.Center
    }
  }

  def spacingRegion: Region = new Region {
    hgrow = Priority.Always
  }

  /** Creates an empty ImageView with the same size of the bunny, for the bunnies with no ancient relatives */
  def emptyImageView: ImageView = new ImageView {
    fitWidth = bunnyIconSize
  }

  def plusView: Text = new Text {
      text = "+"
      style = "-fx-font-weight: bold; " +
        "-fx-font-size: "+ bunnyIconSize/TREE_PLUS_PROPORTION +";"
      hgrow = Priority.Always
  }

  /** Creates an empty Text with the same size of the plus, for the bunnies with no ancient relatives */
  def emptyPlusView: Text = {
   val txt = plusView
    txt setVisible false
    txt
  }

  /**
   * @param trees The tree with the elems that need to be in this row
   * @return The view of a row and the trees which needs to be inserted in the next one
   */
  private def createRow(trees: Seq[Option[BinaryTree[Bunny]]]) : (HBox, Seq[Option[BinaryTree[Bunny]]]) = {
    var nextTrees: Seq[Option[BinaryTree[Bunny]]] = Seq()
    var index = 0
    val row = new HBox{
      alignment = Pos.Center
      maxHeight = bunnyIconSize
      children = spacingRegion
    }

    trees.foreach(tree => {
      if (tree?) row.children += BunnyPedigreeView(tree.get.elem).pane
      else row.children += emptyImageView

      index += 1
      if (index < trees.size) {
        row.children += spacingRegion
        if (index % 2 == 1 && (tree?)) row.children += plusView else row.children += emptyPlusView
      }
      row.children += spacingRegion

      if ((tree?) && tree.get.isInstanceOf[Node[Bunny]]) {
        nextTrees ++= Seq(Option(tree.get.asInstanceOf[Node[Bunny]].momTree), Option(tree.get.asInstanceOf[Node[Bunny]].dadTree))
      } else {
        nextTrees ++= Seq(Option.empty, Option.empty)
      }
    })
    (row, nextTrees)
  }
}
