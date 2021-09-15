package view

import engine.SimulationConstants.MAX_GENEALOGICAL_TREE_GENERATIONS
import model.Bunny.generateRandomFirstBunny
import model.Tree.generateTree
import model.genome.GenesUtils.assignRandomDominance
import model.world.Reproduction.nextGenerationBunnies
import model.{BinaryTree, Bunny, TreeNode}
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.image.ImageView
import scalafx.scene.layout._
import scalafx.scene.shape.Line
import scalafx.scene.text.Text
import scalafx.scene.{Group, Scene}
import view.scalaFX.utilities.Direction
import view.scalaFX.utilities.Direction.Right
import view.utilities.{BunnyImageUtils, ImageType}

import scala.util.Random

object TestTreeVisualization extends JFXApp3 {
  assignRandomDominance()
  var bunnies = Seq.fill(5)(generateRandomFirstBunny.asInstanceOf[Bunny])
  for (_ <- 0 to MAX_GENEALOGICAL_TREE_GENERATIONS) {
    bunnies = nextGenerationBunnies(bunnies)
  }
  val bunny = Random.shuffle(bunnies).head
  val BUNNY_SIZE = 150

  private def bunnyImageView(bunny: Bunny): ImageView = new ImageView {
    image = BunnyImageUtils.bunnyToImage(bunny, ImageType.Normal)
    fitWidth = BUNNY_SIZE
    fitHeight = BUNNY_SIZE
    preserveRatio = true
    scaleX = Direction.scaleXValue(Right)
  }

  private def bunnyAllelesView(bunny: Bunny): Text = {
    val txt = new Text(bunny.genotype.genes.values
      .map(g => g.momAllele.letter + g.dadAllele.letter + " ")
      .reduce(_+_))
    txt.setStyle("-fx-font-family: \"Helvetica\"; " +
      "-fx-font-weight: bold; " +
      "-fx-font-size: 12pt")
    txt
  }

  private def treeBunnyView(bunny: Bunny): Pane = {
    val pane = new AnchorPane()
    pane.children.add(
      new VBox(bunnyImageView(bunny),
      new HBox(getRegion(), bunnyAllelesView(bunny), getRegion()))
    )
    pane
  }

  def getRegion():Region = {
    val region = new Region()
    region.minWidth = 10
    region.hgrow = Priority.Always
    region
  }

  object treeRegion extends Region {
    val region = new Region()
    def apply(binding: Pane) = {
      region.minWidth.bind(binding.widthProperty().divide(2))
      region.hgrow = Priority.Always
    }
    def getRegion():Region = {
      val regionInstance = new Region()
      regionInstance.minWidth.bind(region.widthProperty())
      regionInstance.hgrow = Priority.Always
    }
  }

  def createRow(trees: Seq[Option[BinaryTree[Bunny]]]) : (HBox, Seq[Option[BinaryTree[Bunny]]], Seq[Line]) = {
    var nextTrees: Seq[Option[BinaryTree[Bunny]]] = Seq()
    var lines: Seq[Line] = Seq()
    var index = 0
    var prevChild: VBox = null
    val row = new HBox()
    row.padding = Insets(10)
    row.setAlignment(Pos.Center)

    row.children.add(treeRegion.getRegion)

    trees.foreach(tree => {
      if (tree.isDefined) {
        val child: Pane = treeBunnyView(tree.get.elem)
        /*val innerChild: VBox = child.asInstanceOf[VBox]
        if (index % 2 == 0) {
          prevChild = innerChild
        } else {
          val line = new Line()
          line.startXProperty().bind(prevChild.layoutXProperty())
          line.startYProperty().bind(prevChild.layoutYProperty())
          line.endXProperty().bind(innerChild.layoutXProperty())
          line.endYProperty().bind(innerChild.layoutYProperty())
          lines ++= Seq(line)
        }*/
        row.children.add(child)
      } else {
        row.children.add(treeRegion.getRegion)
      }

      index += 1
      if (index < trees.size) {
        row.children.add(treeRegion.getRegion)
      }
      row.children.add(treeRegion.getRegion)

      if (tree.isDefined && tree.get.isInstanceOf[TreeNode[Bunny]]) {
        nextTrees ++= Seq(Option(tree.get.asInstanceOf[TreeNode[Bunny]].momTree), Option(tree.get.asInstanceOf[TreeNode[Bunny]].dadTree))
      } else {
        nextTrees ++= Seq(Option.empty, Option.empty)
      }
    })

    (row, nextTrees, lines)
  }

   override def start(): Unit = {
     val firstTreeBunny = treeBunnyView(bunny)
     treeRegion(firstTreeBunny)

     val tree: Option[BinaryTree[Bunny]] = Option(generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny))
     val lineGroup: Group = new Group()
     var rows: Seq[HBox] = Seq()
     var row: (HBox, Seq[Option[BinaryTree[Bunny]]], Seq[Line]) = (new HBox(), Seq(tree), Seq())
     for (_ <- 0 to tree.get.generations){
       row = createRow(row._2)
       rows ++= Seq(row._1)
       row._3.foreach(lineGroup.children.add(_))
     }

     val root: VBox = new VBox()
     rows.reverse.foreach(root.children.add(_))

     stage = new PrimaryStage() {
      title = "Bunnies"
      scene = new Scene(new Group(root, lineGroup))
     }
     stage.setResizable(false)
   }
}
