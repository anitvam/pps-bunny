package view.scalaFX.FXControllers

import controller.Controller
import engine.SimulationConstants.MAX_GENEALOGICAL_TREE_GENERATIONS
import model.{Bunny, ChildBunny}
import model.Bunny.generateRandomFirstBunny
import model.genome.{Gene, Genes, JustMutatedAllele}
import model.genome.GenesUtils.assignRandomDominance
import model.world.Generation.Population
import model.world.Reproduction.nextGenerationBunnies
import scalafx.animation.Timeline
import scalafx.application.Platform
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.Button
import scalafx.scene.image.Image
import scalafx.scene.layout._
import scalafx.util.Duration
import scalafxml.core.macros.sfxml
import view.scalaFX.components.BunnyView
import view.scalaFX.components.tree.GenealogicalTreeView
import view.utilities.BunnyImage

import scala.language.postfixOps
import scala.util.Random

trait BaseAppControllerInterface {
  def initialize(): Unit
  def showBunnies(bunnies:Population): Unit
}

@sfxml
class BaseAppController(private val simulationPane: AnchorPane,
                        private val chartPane: AnchorPane,
                        private val mutationChoicePane: AnchorPane,
                        private val factorChoicePane: AnchorPane,
                        private val graphChoicePane: AnchorPane,
                        private val startButton: Button) extends BaseAppControllerInterface {


  private var bunnyViews: Seq[BunnyView] = Seq.empty
  private var bunnyTimelines: Seq[Timeline] = Seq.empty

  def initialize(): Unit = {
    // Environment background configuration
    val hotBackground = new Image("/img/environment/climate_hot.png")
    if (hotBackground == null) {
      println("An error occurred while loading resource: climate_hot.png")
      Platform.exit()
    }
    simulationPane.background = new Background(Array(new BackgroundImage(
      image = hotBackground,
      repeatX = BackgroundRepeat.NoRepeat,
      repeatY = BackgroundRepeat.NoRepeat,
      position = BackgroundPosition.Default,
      size = new BackgroundSize(
        width = 1.0,
        height = 1.0,
        widthAsPercentage = true,
        heightAsPercentage = true,
        contain = false,
        cover = false)
    )))
    BunnyImage
  }

  def handleStartSimulation(): Unit = {
    startButton.setVisible(false)
    Controller.startSimulation()
  }

  def showGenealogicalTree(bunny: Bunny): Unit = {
    chartPane.children.add(GenealogicalTreeView(bunny).chartPane)
  }

  def showBunnies(bunnies:Population): Unit ={
    // Bunny visualization inside simulationPane
      val newBunnyViews = bunnies.filter(_.age == 0).map(BunnyView(_))
      bunnyViews = bunnyViews.filter(_.bunny.alive) ++ newBunnyViews
      simulationPane.children = ObservableBuffer.empty
      simulationPane.children = bunnyViews.map(_.imageView)

      // Timeline definition for each bunny of the Population
      newBunnyViews.zipWithIndex.foreach(bunny => {
        val bunnyTimeline = new Timeline {
          onFinished = _ => {
            keyFrames = bunny._1.jump()
            this.play()
          }
          delay = Duration(1500 + bunny._2)
          autoReverse = true
          cycleCount = 1
          keyFrames = bunny._1.jump()
        }
        bunnyTimelines = bunnyTimeline +: bunnyTimelines
        bunnyTimeline.play()
      })
  }

  //TEST
  assignRandomDominance()
  var bunnies: Seq[Bunny] = Seq.fill(5)(generateRandomFirstBunny.asInstanceOf[Bunny])
  for (_ <- 0 to MAX_GENEALOGICAL_TREE_GENERATIONS) {
    bunnies = nextGenerationBunnies(bunnies)
  }
  var bunny: Bunny = Random.shuffle(bunnies).head
  val mutatedGene = Gene(Genes.FUR_COLOR, JustMutatedAllele(Genes.FUR_COLOR.mutated), JustMutatedAllele(Genes.FUR_COLOR.mutated))
  bunny = new ChildBunny(bunny.genotype + mutatedGene, bunny.mom, bunny.dad)
  chartPane.children.add(GenealogicalTreeView(bunny).chartPane)
}
