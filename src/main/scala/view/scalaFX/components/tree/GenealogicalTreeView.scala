package view.scalaFX.components.tree

import engine.SimulationConstants.MAX_GENEALOGICAL_TREE_GENERATIONS
import model.Tree.generateTree
import model.{BinaryTree, Bunny, Node}
import scalafx.geometry.Pos
import scalafx.scene.image.ImageView
import scalafx.scene.layout._
import scalafx.scene.text.Text
import view.scalaFX.components.tree.GenealogicalTreeViewConstants.{BUNNY_PLUS_PROPORTION, BUNNY_REGION_PROPORTION, STANDARD_BUNNY_SIZE}

/** Proportion constants to resize the view depending on the bunny size*/
object GenealogicalTreeViewConstants {
  val STANDARD_BUNNY_SIZE = 60
  val BUNNY_INFO_PROPORTION: Int = 5
  val BUNNY_REGION_PROPORTION: Int = 20
  val BUNNY_FONT_PROPORTION: Int = 8
  val BUNNY_PLUS_PROPORTION: Int = 3
}

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
  var bunnySize: Int = STANDARD_BUNNY_SIZE

  def apply(bunny: Bunny): GenealogicalTreeView = {
    TreeViewImpl(bunny, generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny))
  }

  def apply(bunny: Bunny, bunnySize: Int): GenealogicalTreeView = {
    this.bunnySize = bunnySize
    this(bunny)
  }

  private case class TreeViewImpl(override val bunny: Bunny, override val tree: BinaryTree[Bunny]) extends GenealogicalTreeView{
    var rows: Seq[HBox] = Seq()
    var row: (HBox, Seq[Option[BinaryTree[Bunny]]]) = (new HBox(), Seq(Option(tree)))
    for (_ <- 1 to tree.generations){
      row = createRow(row._2)
      rows ++= Seq(row._1)
    }

    override val treePane = new VBox()
    rows.reverse.foreach(treePane.children.add(_))
    treePane.children.add(spacingRegion)
    treePane.setStyle("-fx-background-color: yellow;")
  }

  def spacingRegion: Region = {
    val region = new Region()
    region.minWidth = bunnySize/BUNNY_REGION_PROPORTION
    region.minHeight = bunnySize/BUNNY_REGION_PROPORTION
    region.hgrow = Priority.Always
    region
  }

  /** Creates an empty ImageView with the same size of the bunny, for the bunnies with no ancient relatives */
  def emptyImageView: ImageView = new ImageView {
    fitWidth = bunnySize
  }

  def plusView: Text = {
    val txt = new Text("+")
    txt.setStyle("-fx-font-weight: bold; " +
      "-fx-font-size: "+ bunnySize/BUNNY_PLUS_PROPORTION + "pt")
    txt.minWidth(bunnySize/BUNNY_REGION_PROPORTION)
    txt.hgrow = Priority.Always
    txt
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
    val row = new HBox()
    row.setAlignment(Pos.Center)
    row.maxHeight = bunnySize
    row.children.add(spacingRegion)

    trees.foreach(tree => {
      if (tree.isDefined) row.children.add(BunnyTreeView(tree.get.elem).pane)
      else row.children.add(emptyImageView)

      index += 1
      if (index < trees.size) {
        row.children.add(spacingRegion)
        if (index % 2 == 1) {
          if (tree.isDefined) row.children.add(plusView) else row.children.add(emptyPlusView)
        }
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
