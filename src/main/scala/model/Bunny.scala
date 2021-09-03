package model
import model.genome.Genes.GeneKind
import model.genome.{CompletedGenotype, Gene, Genes, StandardAllele}

import scala.annotation.tailrec
import scala.util.Random

/**
 * Represents a Bunny.
 */
sealed trait Bunny {
  val genotype: CompletedGenotype
  val mom: Option[Bunny]
  val dad: Option[Bunny]
  var age: Int
  var alive: Boolean

  override def toString: String = {
    super.toString+ "\n alive:" + alive + " \n age: " + age + "\n"+ genotype.genes
      .map(g => "\t" + g._1 + ": "+ g._2.getVisibleTrait.toString.toLowerCase + " (" + g._2.getLetters + ")")
      .reduce(_ + "\n" + _)
      .replace("_", " ") + "\n"
  }
}

/**
 * Represents a Bunny that as just been created.
 */
class ChildBunny(override val genotype: CompletedGenotype, override val mom:Option[Bunny], override val dad:Option[Bunny]) extends Bunny {
  override var age: Int = 0
  override var alive: Boolean = true
}

/**
 * Represents the first Bunny which appears in the world, so it does not have a mom and a dad.
 */
class FirstBunny(genotype: CompletedGenotype) extends ChildBunny(genotype,Option.empty, Option.empty)

object Bunny {
  /**
   * @return a FirstBunny with the "base" allele for each gene
   */
  def generateBaseFirstBunny: FirstBunny =
    new FirstBunny(CompletedGenotype(Genes.values.unsorted.map(gk =>
      (gk, Gene(gk, StandardAllele(gk.base), StandardAllele(gk.base)))).toMap))

  /**
   * @return a FirstBunny with a random allele for each gene
   */
  def generateRandomFirstBunny: FirstBunny = {
    new FirstBunny(
      CompletedGenotype(
        Genes.values.unsorted.map(gk => {
          (gk, Gene(gk, StandardAllele(List(gk.base, gk.mutated)(Random.nextInt(2))),
            StandardAllele(List(gk.base, gk.mutated)(Random.nextInt(2)))))
        }).toMap
      )
    )
  }

  /**
   *
   * @param geneKind the kind of Gene we want to split the bunnies by
   * @param bunnies  all the bunnies
   * @return a tuple with the sequence of bunnies with the base Allele
   *         and the sequence of bunnies with the mutated Allele
   */
  type baseBunnies = Seq[Bunny]
  type mutatedBunnies = Seq[Bunny]

  def splitBunniesByGene(geneKind: GeneKind, bunnies: Seq[Bunny]): (baseBunnies, mutatedBunnies) =
    bunnies.partition(_.genotype.phenotype(geneKind) == geneKind.base)

  /**
   *
   * @param generations the number of older generations to retrieve
   * @param bunny       the subject bunny
   * @return the genealogical tree of the bunny for the specified generations
   */

  def generateTree(totGenerations: Int, bunny: Bunny): BinaryTree[Bunny] = {
    @tailrec
    def generateTreeWithAccumulator(acc: BinaryTree[Bunny], generations: Int): BinaryTree[Bunny] =
      if (generations == 1 || acc.elem.mom.isEmpty) acc
      else generateTreeWithAccumulator(
        Node(acc.elem, Leaf(acc.elem.mom.get), Leaf(acc.elem.dad.get)),
        generations - 1)
    generateTreeWithAccumulator(Leaf(bunny), totGenerations)

    /*if (generations == 1 || bunny.mom.isEmpty) Leaf(bunny)
      else generateTree(Node(bunny, generateTree(generations - 1, bunny.mom.get), generateTree(generations - 1, bunny.dad.get)))*/
  }
}

/**
*  Represents a Tree.
*/
sealed trait BinaryTree[A]{
val elem: A
val generations: Int
}

/**
* Represents a Leaf of the Tree, with just an element.
* @param elem  the element in the leaf
* @tparam A    the type of the element
*/
case class Leaf[A](elem: A) extends BinaryTree[A] {
override val generations: Int = 1
}

/**
* Represents a Node of the Tree, with an element and two branches
* @param elem      the element in the node
* @param momTree   one of the branches
* @param dadTree   the other branch
* @tparam A        the type of the element
*/
case class Node[A](override val elem: A, momTree: BinaryTree[A], dadTree: BinaryTree[A]) extends BinaryTree[A] {
override val generations: Int = Math.max(momTree.generations, dadTree.generations) + 1
}