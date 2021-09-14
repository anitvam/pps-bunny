package view

import engine.SimulationConstants.MAX_GENEALOGICAL_TREE_GENERATIONS
import model.Bunny.generateRandomFirstBunny
import model.Tree.generateTree
import model.genome.GenesUtils.assignRandomDominance
import model.world.Reproduction.nextGenerationBunnies
import model.{BinaryTree, Bunny, Node}
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.image.ImageView
import scalafx.scene.layout._
import scalafx.scene.text.Text
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
      .map(g => g.dadAllele.letter + g.momAllele.letter + " ")
      .reduce(_+_))
    txt.setStyle("-fx-font-weight: bolder")
    txt
  }

  private def treeBunnyView(bunny: Bunny): Pane =
    new VBox(bunnyImageView(bunny),
      new HBox(getRegion(), bunnyAllelesView(bunny), getRegion()))

  def getRegion() : Region = {
    val region = new Region()
    region.minWidth = 8
    region.hgrow = Priority.Always
    region
  }

  def createLine(trees: Seq[BinaryTree[Bunny]]) : (HBox, Seq[BinaryTree[Bunny]]) = {
    var nextTrees: Seq[BinaryTree[Bunny]] = Seq()
    var index = 0
    val line = new HBox()
    line.padding = Insets(10)
    line.setAlignment(Pos.Center)

    line.children.add(getRegion())

    trees.foreach(tree => {
      line.children.add(treeBunnyView(tree.elem))
      index += 1
      if (index < trees.size){
        line.children.add(getRegion())
      }
      line.children.add(getRegion())
      tree match {
        case value: Node[Bunny] => nextTrees ++= Seq(value.momTree, value.dadTree)
        case _ =>
      }
    })

    (line, nextTrees)
  }

   override def start(): Unit = {
     val tree: BinaryTree[Bunny] = generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny)
     var lines: Seq[HBox] = Seq()
     var line: (HBox, Seq[BinaryTree[Bunny]]) = (new HBox(), Seq(tree))
     for (_ <- 0 to tree.generations){
       line = createLine(line._2)
       lines ++= Seq(line._1)
     }

     val root: VBox = new VBox()
     lines.reverse.foreach(root.children.add(_))

     stage = new PrimaryStage() {
      title = "Bunnies"
      scene = new Scene(root)
     }
     stage.setResizable(false)
   }
}
