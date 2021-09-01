package model
import model.genome.Alleles.AlleleKind
import model.genome.{CompletedGenotype, Genes, StandardAllele, StandardGene}

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

object BunnyUtils {
  /**
   * @return a FirstBunny with the "base" allele for each gene
   */
  def generateBaseFirstBunny: FirstBunny =
    new FirstBunny(CompletedGenotype(Genes.values.unsorted.map(gk =>
      (gk, StandardGene(gk, StandardAllele(gk.base), StandardAllele(gk.base)))).toMap))

  /**
   * @return a FirstBunny with a random allele for each gene
   */
  def generateRandomFirstBunny: FirstBunny = {
    new FirstBunny(
      CompletedGenotype(
        Genes.values.unsorted.map(gk => {
          (gk, StandardGene(gk, StandardAllele(List(gk.base, gk.mutated)(Random.nextInt(2))),
            StandardAllele(List(gk.base, gk.mutated)(Random.nextInt(2)))))
        }).toMap
      )
    )
  }

  /** *
   *
   * @param generations the number of older generations to retrieve
   * @param bunny       the subject bunny
   * @return the genealogical tree of the bunny for the specified generations
   */
  def generateTree(generations: Int, bunny: Bunny) = ???

  /**
   * @param alleleKind the kind of Allele required
   * @return           all the bunnies with that kind of Allele
   */
  def getBunniesWithAllele(alleleKind: AlleleKind): Set[Bunny] = ???
}