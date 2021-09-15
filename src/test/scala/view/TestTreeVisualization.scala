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
import scalafx.scene.image.{Image, ImageView}
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
  var bunnies: Seq[Bunny] = Seq.fill(5)(generateRandomFirstBunny.asInstanceOf[Bunny])
  for (_ <- 0 to MAX_GENEALOGICAL_TREE_GENERATIONS) {
    bunnies = nextGenerationBunnies(bunnies)
  }
  val bunny: Bunny = Random.shuffle(bunnies).head
  val BUNNY_SIZE = 125
  val INFO_SIZE = BUNNY_SIZE/5
  val REGION_MIN_WIDTH = 5

  private def bunnyImageView(bunny: Bunny): ImageView = new ImageView {
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

  private def bunnyAllelesView(bunny: Bunny): Text = {
    val txt = new Text(bunny.genotype.genes.values
      .map(g => g.momAllele.letter + g.dadAllele.letter + " ")
      .reduce(_+_))
    txt.setStyle("-fx-font-family: \"Helvetica\"; " +
      "-fx-font-weight: bold; " +
      "-fx-font-size: 10pt")
    txt
  }

  private def treeBunnyView(bunny: Bunny): Pane =
    new VBox( bunnyImageView(bunny),
              new HBox(getRegion, bunnyAllelesView(bunny), getRegion),
              new HBox(getRegion,
                bunny.alive match {
                case false => deadImageView()
                case _ => getRegion},
                bunny.genotype.isJustMutated match {
                  case false => mutationImageView()
                  case _ => getRegion},
                getRegion))

  def getRegion:Region = {
    val region = new Region()
    region.minWidth = REGION_MIN_WIDTH
    region.hgrow = Priority.Always
    region
  }

  def createRow(trees: Seq[Option[BinaryTree[Bunny]]]) : (HBox, Seq[Option[BinaryTree[Bunny]]], Seq[Line]) = {
    var nextTrees: Seq[Option[BinaryTree[Bunny]]] = Seq()
    val lines: Seq[Line] = Seq()
    var index = 0
    val row = new HBox()
    row.padding = Insets(10)
    row.setAlignment(Pos.Center)

    row.children.add(getRegion)

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
        row.children.add(emptyImageView())
      }

      index += 1
      if (index < trees.size) {
        row.children.add(getRegion)
      }
      row.children.add(getRegion)

      if (tree.isDefined && tree.get.isInstanceOf[Node[Bunny]]) {
        nextTrees ++= Seq(Option(tree.get.asInstanceOf[Node[Bunny]].momTree), Option(tree.get.asInstanceOf[Node[Bunny]].dadTree))
      } else {
        nextTrees ++= Seq(Option.empty, Option.empty)
      }
    })

    (row, nextTrees, lines)
  }

   override def start(): Unit = {
     val tree: Option[BinaryTree[Bunny]] = Option(generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny))
     val lineGroup: Group = new Group()
     var rows: Seq[HBox] = Seq()
     var row: (HBox, Seq[Option[BinaryTree[Bunny]]], Seq[Line]) = (new HBox(), Seq(tree), Seq())
     for (_ <- 1 to tree.get.generations){
       row = createRow(row._2)
       rows ++= Seq(row._1)
       println(row._1.children)
       row._3.foreach(lineGroup.children.add(_))
     }

     val root = new VBox()
     rows.reverse.foreach(root.children.add(_))
     root.setMaxWidth(100)

     stage = new PrimaryStage() {
      title = "Bunnies"
      scene = new Scene(new Group(root, lineGroup))
     }
     stage.setResizable(true)
   }
}
