package view

import engine.SimulationConstants.MAX_GENEALOGICAL_TREE_GENERATIONS
import model.Bunny.generateRandomFirstBunny
import model.Tree.generateTree
import model.genome.GenesUtils.assignRandomDominance
import model.world.Reproduction.nextGenerationBunnies
import model.{BinaryTree, Bunny, Node}
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.geometry.Pos
import scalafx.scene.Scene
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout._
import scalafx.scene.text.Text
import view.scalaFX.utilities.Direction
import view.scalaFX.utilities.Direction.Right
import view.utilities.{BunnyImageUtils, ImageType}

import scala.util.Random

object TestTreeVisualization extends JFXApp3 {
  assignRandomDominance()
  var bunnies: Seq[Bunny] = Seq.fill(5)(generateRandomFirstBunny.asInstanceOf[Bunny])
  for (_ <- 0 to MAX_GENEALOGICAL_TREE_GENERATIONS) {
    bunnies = nextGenerationBunnies(bunnies)
  }
  val bunny: Bunny = Random.shuffle(bunnies).head
  val BUNNY_SIZE = 125
  val INFO_SIZE = BUNNY_SIZE/5
  val REGION_MIN_WIDTH = 5

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

  private def bunnyView(bunny: Bunny): ImageView = new ImageView {
    image = BunnyImageUtils.bunnyToImage(bunny, ImageType.Normal)
    fitWidth = BUNNY_SIZE
    fitHeight = BUNNY_SIZE
    preserveRatio = true
    scaleX = Direction.scaleXValue(Right)
  }

  private def emptyImageView(): ImageView = new ImageView {
    fitWidth = BUNNY_SIZE
  }

  private def deadImageView(): ImageView = new ImageView {
    image = new Image("/img/death.png")
    fitWidth = INFO_SIZE
    fitHeight = INFO_SIZE
  }

  private def mutationImageView(): ImageView = new ImageView {
    image = new Image("/img/mutation.png")
    fitWidth = INFO_SIZE
    fitHeight = INFO_SIZE
  }

  private def infoView(bunny: Bunny): HBox =
    new HBox( spacingRegion,
              bunny.alive match {
                case false => deadImageView()
                case _ => emptyRegion},
              bunny.genotype.isJustMutated match {
                case true => mutationImageView()
                case _ => emptyRegion},
              spacingRegion)

  private def allelesView(bunny: Bunny): Text = {
    val txt = new Text(bunny.genotype.genes.values
      .map(g => g.momAllele.letter + g.dadAllele.letter + " ")
      .reduce(_+_))
    txt.setStyle("-fx-font-family: \"Helvetica\"; " +
      "-fx-font-weight: bold; " +
      "-fx-font-size: 10pt")
    txt
  }

  private def plusView(): Text = {
    val txt = new Text("+")
    txt.setStyle("-fx-font-weight: bold; " +
      "-fx-font-size: 25pt")
    txt
  }

  private def treeBunnyView(bunny: Bunny): Pane =
    new VBox( bunnyView(bunny),
              new HBox( spacingRegion, allelesView(bunny), spacingRegion),
              infoView(bunny))

  def createRow(trees: Seq[Option[BinaryTree[Bunny]]]) : (HBox, Seq[Option[BinaryTree[Bunny]]]) = {
    var nextTrees: Seq[Option[BinaryTree[Bunny]]] = Seq()
    var index = 0
    val row = new HBox()
    row.children.add(spacingRegion)
    row.setAlignment(Pos.Center)

    trees.foreach(tree => {
      if (tree.isDefined) row.children.add(treeBunnyView(tree.get.elem))
      else row.children.add(emptyImageView())

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

   override def start(): Unit = {
     val tree: Option[BinaryTree[Bunny]] = Option(generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny))
     var rows: Seq[HBox] = Seq()
     var row: (HBox, Seq[Option[BinaryTree[Bunny]]]) = (new HBox(), Seq(tree))
     for (_ <- 1 to tree.get.generations){
       row = createRow(row._2)
       row._1.maxHeight = BUNNY_SIZE
       rows ++= Seq(row._1)
       println(row._1.children)
     }

     val root = new VBox()
     rows.reverse.foreach(root.children.add(_))
     root.setMaxWidth(100)

     stage = new PrimaryStage() {
      title = "Bunnies"
      scene = new Scene(root)
     }
     stage.setResizable(true)
   }
}
