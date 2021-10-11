package it.unibo.pps.bunny.view.scalaFX.components.charts.pedigree

import alice.tuprolog.Term
import it.unibo.pps.bunny.model.bunny.Tree.{ actualGenerations, generateTree }
import it.unibo.pps.bunny.model.bunny.{ BinaryTree, Bunny, Node }
import scalafx.geometry.Pos
import scalafx.scene.image.ImageView
import scalafx.scene.layout._
import scalafx.scene.text.Text
import it.unibo.pps.bunny.util.PimpScala._
import it.unibo.pps.bunny.util.Scala2P._
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants.GenealogicalTree._
import it.unibo.pps.bunny.view.scalaFX.ScalaFXConstants._

import scala.language.postfixOps

trait PedigreeChart {

  /** Reference to the model bunny entity */
  val bunny: Bunny

  /** Reference to the model tree entity created from the bunny */
  val tree: BinaryTree[Bunny]

  /** The pane with the view of the tree */
  val chartPane: Pane
}

object PedigreeChart {
  type IconSize = Int
  type Generations = Int

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
    val dims = dimensions(chartWidth, chartHeight, actualGenerations(bunny))
    bunnyIconSize = dims._1
    val tree = generateTree(dims._2, bunny)
    PedigreeChartImpl(bunny, tree)
  }

  private val dimensions: (Int, Int, Int) => (IconSize, Generations) = (chartWidth, chartHeight, treeGenerations) => {
    val BunnySizeIndex = 8
    val GenerationsIndex = 9
    val engine: Term => Option[Term] = SingleSolutionPrologEngine("prolog/pedigree_dim.pl")
    val heightWithChosen = chartHeight - ADDITIONAL_CHOSEN_SPACE
    val widthWithChosen = chartWidth - ADDITIONAL_CHOSEN_SPACE
    val goal: String =
      s"pedigree_dimensions($heightWithChosen, $widthWithChosen, $BUNNY_PLUS_PROPORTION, $BUNNY_INFO_PROPORTION," +
        s"$BUNNY_FONT_PROPORTION, $MAX_TREE_BUNNY_SIZE, $MIN_TREE_BUNNY_SIZE, $treeGenerations, BSF, G)"
    val solution = engine(goal)
    if (solution ?) (extractTerm(solution.get, BunnySizeIndex), extractTerm(solution.get, GenerationsIndex))
    else throw new PrologCalculationException
  }

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
      children = spacingRegion()
    }

    trees.foreach(tree => {
      if (tree ?) row.children += BunnyPedigreeView(tree.get.elem).pane
      else row.children += emptyImageView()

      index += 1
      if (index < trees.size) {
        row.children += spacingRegion()
        if (index % 2 == 1 && (tree ?)) row.children += plusView() else row.children += emptyPlusView()
      }
      row.children += spacingRegion()

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
  val spacingRegion: () => Region = () =>
    new Region {
      hgrow = Priority.Always
    }

  /** Creates an empty ImageView with the same size of the bunny, for the bunnies with no ancient relatives */
  private val emptyImageView: () => ImageView = () =>
    new ImageView {
      fitWidth = bunnyIconSize
    }

  /** Creates an empty Text with the same size of the plus, for the bunnies with no ancient relatives */
  private val emptyPlusView: () => Text = () => {
    val txt = plusView()
    txt setVisible false
    txt
  }

  /** Creates a view of the plus between couples of bunnies */
  private val plusView: () => Text = () =>
    new Text {
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
    rows(0).children(1).getStyleClass.add("tree-chosen-bunny")

    override val chartPane: VBox = new VBox {
      children = spacingRegion() +: rows.reverse :+ spacingRegion()
      alignment = Pos.Center
    }

  }

}
