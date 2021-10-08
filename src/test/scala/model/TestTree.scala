package model

import engine.SimulationConstants.MAX_GENEALOGICAL_TREE_GENERATIONS
import model.bunny.Bunny.randomBunnyGenerator
import model.bunny.Tree.generateTree
import model.bunny._
import model.world.Reproduction.{generateInitialCouple, nextGenerationBunnies}
import org.scalatest.{FlatSpec, Matchers}
import util.PimpScala.RichOption

import scala.language.postfixOps

class TestTree extends FlatSpec with Matchers {

  "A genealogical tree " should "contain just the bunny as a Leaf, if he has no parents" in {
    val bunny = randomBunnyGenerator()
    val tree = generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny)
    assert(tree.isInstanceOf[Leaf[Bunny]])
    assert(tree.generations == 1)
    assert(tree.elem == bunny)
  }

  val bunnyWithParents: Bunny =
    nextGenerationBunnies(List.fill(2)(generateInitialCouple()).flatMap(_.toSeq)).filter(_.mom ?).head

  val tree: BinaryTree[Bunny] = generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunnyWithParents)

  it should "contain his parents, if he has them" in {
    assert(tree.asInstanceOf[Node[Bunny]].momTree.elem == bunnyWithParents.mom.get)
    assert(tree.asInstanceOf[Node[Bunny]].dadTree.elem == bunnyWithParents.dad.get)
    assert(tree.generations == 2)
  }

  var bunny: Bunny = randomBunnyGenerator()
  for (_ <- 0 to MAX_GENEALOGICAL_TREE_GENERATIONS) {
    bunny = nextGenerationBunnies(bunny :: List.fill(2)(generateInitialCouple()).flatMap(_.toSeq))
      .sortBy(generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, _).generations).reverse.head
  }
  val fullTree: BinaryTree[Bunny] = generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny)

  "A full genealogical tree " should "contain all the required generations" in {
    assert(fullTree.generations == MAX_GENEALOGICAL_TREE_GENERATIONS)
  }

  it should "contain the right bunnies as parents of the first bunny " in {
    assert(fullTree.asInstanceOf[Node[Bunny]].momTree.elem == bunny.mom.get)
    assert(fullTree.asInstanceOf[Node[Bunny]].dadTree.elem == bunny.dad.get)
  }

  it should "contain the right bunnies in all the generations" in {
    var bunniesToCheck: Seq[(Bunny, BinaryTree[Bunny])] = Seq((bunny, fullTree))
    bunniesToCheck.foreach(bt => {
      val momTree = bt._2.asInstanceOf[Node[Bunny]].momTree
      val dadTree = bt._2.asInstanceOf[Node[Bunny]].dadTree
      val mom = bt._1.mom.get
      val dad = bt._1.dad.get
      assert(momTree.elem == mom)
      assert(dadTree.elem == dad)
      bunniesToCheck = bunniesToCheck ++ Seq((mom, momTree), (dad, dadTree))
    })
  }

}
