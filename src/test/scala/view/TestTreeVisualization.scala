package view

import engine.SimulationConstants.MAX_GENEALOGICAL_TREE_GENERATIONS
import model.bunny.Bunny.generateRandomFirstBunny
import model.bunny.Gender.randomGender
import model.genome.KindsUtils.{assignRandomDominance, resetDominance}
import model.genome.{Gene, Genes, JustMutatedAllele}
import model.world.Reproduction.nextGenerationBunnies
import model.bunny.{Bunny, ChildBunny}
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene.Scene
import view.scalaFX.components.charts.pedigree.PedigreeChart

import scala.util.Random

object TestTreeVisualization extends JFXApp3 {
  resetDominance()
  assignRandomDominance()

  val mutatedGene =
    Gene(Genes.FUR_COLOR, JustMutatedAllele(Genes.FUR_COLOR.mutated), JustMutatedAllele(Genes.FUR_COLOR.mutated))

  for (_ <- 0 to MAX_GENEALOGICAL_TREE_GENERATIONS) {
    bunnies = nextGenerationBunnies(bunnies)
  }

  var bunnies: Seq[Bunny] = Seq.fill(5)(generateRandomFirstBunny.asInstanceOf[Bunny])
  var bunny: Bunny = Random.shuffle(bunnies).head

  bunny = new ChildBunny(bunny.genotype + mutatedGene, bunny.mom, bunny.dad, randomGender())
  bunny.alive = false

  override def start(): Unit = {
    val root = PedigreeChart(bunny).chartPane
    stage = new PrimaryStage() {
      title = "Bunnies"
      scene = new Scene(root)
    }
    stage.setResizable(true)
  }

}
