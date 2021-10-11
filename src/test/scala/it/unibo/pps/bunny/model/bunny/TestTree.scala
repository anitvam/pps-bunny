package it.unibo.pps.bunny.model.bunny

import it.unibo.pps.bunny.engine.SimulationConstants.MAX_GENEALOGICAL_TREE_GENERATIONS
import it.unibo.pps.bunny.model.bunny.Bunny._
import it.unibo.pps.bunny.model.bunny.Tree.{treeToNode}
import it.unibo.pps.bunny.model.bunny.Tree.{actualGenerations, generateTree}
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.Reproduction._
import it.unibo.pps.bunny.util.PimpScala.RichOption
import org.scalatest.{FlatSpec, Matchers}

import scala.language.{implicitConversions, postfixOps}

class TestTree extends FlatSpec with Matchers {

  "A genealogical tree " should "contain just the bunny as a Leaf, if he has no parents" in {
    val bunny = randomBunnyGenerator()
    val tree = generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny)
    assert(tree.isInstanceOf[Leaf[Bunny]])
    assert(tree.generations == 1)
    assert(tree.elem == bunny)
  }

  private val bunnyWithParents: Bunny =
    nextGenerationBunnies(List.fill(2)(initialCoupleGenerator()).flatMap(_.toSeq)).filter(_.mom ?).head
  private val tree: BinaryTree[Bunny] = generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunnyWithParents)

  it should "contain his parents, if the bunny has them" in {
    assert(tree.momTree.elem == bunnyWithParents.mom.get)
    assert(tree.dadTree.elem == bunnyWithParents.dad.get)
    assert(tree.generations == 2)
  }

  it should "return its elem toString in the toString method" in {
    assert(tree.toString == tree.elem.toString)
  }

  private var bunnies: Population = Seq(randomBunnyGenerator())
  for (_ <- 0 to MAX_GENEALOGICAL_TREE_GENERATIONS) {
    bunnies = nextGenerationBunnies(bunnies ++ initialCoupleGenerator().toSeq)
  }
  private val bunny: Bunny = bunnies.sortBy(actualGenerations).reverse.head
  private val fullTree: BinaryTree[Bunny] = generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny)

  "A full genealogical tree " should "contain all the required generations" in {
    assert(fullTree.generations == MAX_GENEALOGICAL_TREE_GENERATIONS)
  }

  it should "contain the right bunnies as parents of the first bunny " in {
    assert(fullTree.momTree.elem == bunny.mom.get)
    assert(fullTree.dadTree.elem == bunny.dad.get)
  }

  it should "contain the right bunnies in all the generations" in {
    var bunniesToCheck: Seq[(Bunny, BinaryTree[Bunny])] = Seq((bunny, fullTree))
    bunniesToCheck.foreach(bt => {
      val momTree = bt._2.momTree
      val dadTree = bt._2.dadTree
      val mom = bt._1.mom.get
      val dad = bt._1.dad.get
      assert(momTree.elem == mom)
      assert(dadTree.elem == dad)
      bunniesToCheck = bunniesToCheck ++ Seq((mom, momTree), (dad, dadTree))
    })
  }

}
