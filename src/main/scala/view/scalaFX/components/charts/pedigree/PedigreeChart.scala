package view.scalaFX.components.charts.pedigree

import engine.SimulationConstants.MAX_GENEALOGICAL_TREE_GENERATIONS
import model.Tree.generateTree
import model.{BinaryTree, Bunny, Node}
import scalafx.geometry.Pos
import scalafx.scene.image.ImageView
import scalafx.scene.layout._
import scalafx.scene.text.Text
import view.scalaFX.ScalaFxViewConstants.GenealogicalTree.{TREE_BUNNY_SIZE, TREE_PLUS_PROPORTION}

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
  val BUNNY_GENERATIONS_POW: Double = 0.5
  val PANEL_BUNNY_PROPORTION: Double = 11.5

  def apply (bunny:Bunny): PedigreeChart = {
    this (bunny, TREE_BUNNY_SIZE)
  }

  def apply(bunny: Bunny, panelWidth: Int): PedigreeChart = {
    val tree = generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny)
    val generationsCoefficient = Math.pow(MAX_GENEALOGICAL_TREE_GENERATIONS - tree.generations + 1, BUNNY_GENERATIONS_POW)
    bunnyIconSize = (panelWidth * generationsCoefficient / PANEL_BUNNY_PROPORTION).toInt
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

  def spacingRegion: Region = new Region {hgrow = Priority.Always}

  /** Creates an empty ImageView with the same size of the bunny, for the bunnies with no ancient relatives */
  def emptyImageView: ImageView = new ImageView {
    fitWidth = bunnyIconSize
  }

  def plusView: Text = new Text {
      text = "+"
      style = "-fx-font-weight: bold; -fx-font-size: "+ bunnyIconSize/TREE_PLUS_PROPORTION + "pt"
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
      if (tree.isDefined) row.children += BunnyPedigreeView(tree.get.elem).pane
      else row.children += emptyImageView

      index += 1
      if (index < trees.size) {
        row.children += spacingRegion
        if (index % 2 == 1 && tree.isDefined) row.children += plusView else row.children += emptyPlusView
      }
      row.children += spacingRegion

      if (tree.isDefined && tree.get.isInstanceOf[Node[Bunny]]) {
        nextTrees ++= Seq(Option(tree.get.asInstanceOf[Node[Bunny]].momTree), Option(tree.get.asInstanceOf[Node[Bunny]].dadTree))
      } else {
        nextTrees ++= Seq(Option.empty, Option.empty)
      }
    })
    (row, nextTrees)
  }
}
