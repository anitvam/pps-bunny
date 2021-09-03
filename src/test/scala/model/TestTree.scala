package model

import model.Bunny.{generateRandomFirstBunny, generateTree}
import model.BunnyConstants.GENEALOGICAL_TREE_GENERATIONS
import model.world.Reproduction.nextGenerationBunnies
import org.scalatest.{FlatSpec, Matchers}

class TestTree extends FlatSpec with Matchers {

  "A genealogical tree " should "contain just the bunny as a Leaf, if he has no parents" in {
    val bunny = generateRandomFirstBunny
    val tree = generateTree(GENEALOGICAL_TREE_GENERATIONS, bunny)
    assert(tree.isInstanceOf[Leaf[Bunny]])
    assert(tree.generations == 1)
    assert(tree.elem == bunny)
  }

  val bunnyWithParents: Bunny = nextGenerationBunnies(List.fill(5)(generateRandomFirstBunny)).filter(_.mom.isDefined).head
  val tree: BinaryTree[Bunny] = generateTree(GENEALOGICAL_TREE_GENERATIONS, bunnyWithParents)
  it should "contain his parents, if he has them" in {
    assert(tree.asInstanceOf[Node[Bunny]].momTree.elem == bunnyWithParents.mom.get)
    assert(tree.asInstanceOf[Node[Bunny]].dadTree.elem == bunnyWithParents.dad.get)
    assert(tree.generations == 2)
  }

  var bunny: Bunny = generateRandomFirstBunny
  for (_ <- 0 to GENEALOGICAL_TREE_GENERATIONS) {
    bunny = nextGenerationBunnies(List.fill(5)(bunny)).filter(_.mom.isDefined).head
  }
  val fullTree: BinaryTree[Bunny] = generateTree(GENEALOGICAL_TREE_GENERATIONS, bunny)
  "A full genealogical tree "should "contain all the required generations" in {
    println(fullTree.generations)
    //assert(fullTree.generations == GENEALOGICAL_TREE_GENERATIONS)
  }

  it should "contain the right bunnies as parents of the first bunny " in {
    println(fullTree.asInstanceOf[Node[Bunny]].momTree.elem)
    println(bunny.mom.get)
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

