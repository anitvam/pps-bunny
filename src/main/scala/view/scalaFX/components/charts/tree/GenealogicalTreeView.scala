package view.scalaFX.components.charts.tree

import engine.SimulationConstants.MAX_GENEALOGICAL_TREE_GENERATIONS
import model.Tree.generateTree
import model.{BinaryTree, Bunny, Node}
import scalafx.geometry.Pos
import scalafx.scene.image.ImageView
import scalafx.scene.layout._
import scalafx.scene.text.Text
import view.scalaFX.ScalaFxViewConstants.GenealogicalTree.{TREE_PLUS_PROPORTION, TREE_REGION_PROPORTION, TREE_BUNNY_SIZE}

trait GenealogicalTreeView{
  /** Reference to the model bunny entity */
  val bunny: Bunny

  /** Reference to the model tree entity created from the bunny */
  val tree: BinaryTree[Bunny]

  /** The pane with the view of the tree */
  val treePane: Pane
}

object GenealogicalTreeView {
  /** The size required for the bunny icons*/
  var bunnyIconSize: Int = TREE_BUNNY_SIZE

  def apply(bunny: Bunny): GenealogicalTreeView = TreeViewImpl(bunny, generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny))

  def apply(bunny: Bunny, bunnySize: Int): GenealogicalTreeView = {
    this.bunnyIconSize = bunnySize
    this(bunny)
  }

  private case class TreeViewImpl(override val bunny: Bunny, override val tree: BinaryTree[Bunny]) extends GenealogicalTreeView{
    var rows: Seq[HBox] = Seq()
    var row: (HBox, Seq[Option[BinaryTree[Bunny]]]) = (new HBox(), Seq(Option(tree)))
    for (_ <- 1 to tree.generations){
      row = createRow(row._2)
      rows = rows :+ row._1
    }

    override val treePane = new VBox {
      children = rows.reverse :+ spacingRegion
      style = "-fx-background-color: yellow;"
    }
  }

  def spacingRegion: Region = new Region {
      minWidth = bunnyIconSize/TREE_REGION_PROPORTION
      minHeight = bunnyIconSize/TREE_REGION_PROPORTION
      hgrow = Priority.Always
  }

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
    txt.setVisible(false)
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
      if (tree.isDefined) row.children += BunnyTreeView(tree.get.elem).pane
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
