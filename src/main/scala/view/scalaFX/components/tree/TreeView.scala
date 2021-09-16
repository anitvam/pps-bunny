package view.scalaFX.components.tree

import engine.SimulationConstants.MAX_GENEALOGICAL_TREE_GENERATIONS
import model.Tree.generateTree
import model.{BinaryTree, Bunny, Node}
import scalafx.geometry.Pos
import scalafx.scene.image.ImageView
import scalafx.scene.layout._
import scalafx.scene.text.Text
import view.scalaFX.components.tree.TreeViewConstants.{BUNNY_SIZE, REGION_MIN_WIDTH}

object TreeViewConstants {
  val REGION_MIN_WIDTH: Int = 5
  val BUNNY_SIZE: Int = 125
  val INFO_SIZE: Int = BUNNY_SIZE/5
}

trait TreeView {
  /** Reference to the model bunny entity */
  val bunny: Bunny

  /** Reference to the model tree entity created from the bunny */
  val tree: BinaryTree[Bunny]

  /** The pane with the view of the tree */
  val treePane: Pane
}

object TreeView {

  def apply(bunny: Bunny): TreeView = {
    TreeViewImpl(bunny, generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny))
  }

  private case class TreeViewImpl(bunny: Bunny, tree: BinaryTree[Bunny]) extends TreeView{
    var rows: Seq[HBox] = Seq()
    var row: (HBox, Seq[Option[BinaryTree[Bunny]]]) = (new HBox(), Seq(Option(tree)))
    for (_ <- 1 to tree.generations){
      row = createRow(row._2)
      rows ++= Seq(row._1)
    }

    override val treePane: VBox = new VBox()
    rows.reverse.foreach(treePane.children.add(_))
  }

  def spacingRegion: Region = {
    val region = new Region()
    region.minWidth = REGION_MIN_WIDTH
    region.hgrow = Priority.Always
    region
  }

  def emptyRegion: Region = {
    val region = new Region()
    region.maxWidth = 0
    region
  }

  def emptyImageView: ImageView = new ImageView {
    fitWidth = BUNNY_SIZE
  }

  def plusView(): Text = {
    val txt = new Text("+")
    txt.setStyle("-fx-font-weight: bold; -fx-font-size: 25pt")
    txt.minWidth(REGION_MIN_WIDTH)
    txt.hgrow = Priority.Always
    txt
  }

  private def createRow(trees: Seq[Option[BinaryTree[Bunny]]]) : (HBox, Seq[Option[BinaryTree[Bunny]]]) = {
    var nextTrees: Seq[Option[BinaryTree[Bunny]]] = Seq()
    var index = 0
    val row = new HBox()
    row.setAlignment(Pos.Center)
    row.maxHeight = BUNNY_SIZE
    row.children.add(spacingRegion)

    trees.foreach(tree => {
      if (tree.isDefined) row.children.add(BunnyTreeView(tree.get.elem).bunnyPane)
      else row.children.add(emptyImageView)

      index += 1
      if (index < trees.size) {
        row.children.add(spacingRegion)
        if (index % 2 == 1 && tree.isDefined) row.children.add(plusView())
      }
      row.children.add(spacingRegion)

      if (tree.isDefined && tree.get.isInstanceOf[Node[Bunny]]) {
        nextTrees ++= Seq(Option(tree.get.asInstanceOf[Node[Bunny]].momTree), Option(tree.get.asInstanceOf[Node[Bunny]].dadTree))
      } else {
        nextTrees ++= Seq(Option.empty, Option.empty)
      }
    })
    (row, nextTrees)
  }
}
