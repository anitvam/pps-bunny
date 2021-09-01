package model
import model.genome.Alleles.AlleleKind
import model.world.Reproduction.MAX_BUNNY_AGE
import model.genome.{CompletedGenotype, Genes, StandardAllele, StandardGene}

import scala.util.Random

/**
 * Represents a Bunny.
 */
sealed trait Bunny {
  val genotype: CompletedGenotype
  val mom: Option[Bunny]
  val dad: Option[Bunny]
  val age: Int
  val alive: Boolean

  override def toString: String = {
    super.toString+ "\n alive:" + alive + " \n age: " + age + "\n"+ genotype.genes
      .map(g => "\t" + g._1 + ": "+ g._2.getVisibleTrait.toString.toLowerCase + " (" + g._2.getLetters + ")")
      .reduce(_ + "\n" + _)
      .replace("_", " ") + "\n"
  }
}

/**
 * Represents a Bunny which is still alive.
 * @param genotype  contains the Bunny genes, it must be completed (contain all the available Genes of the world)
 * @param mom       the bunny's mom
 * @param dad       the bunny's dad
 * @param age       how many generations the Bunny has spent in the world.
 */
case class AliveBunny(genotype: CompletedGenotype, mom:Option[Bunny], dad:Option[Bunny], age:Int) extends Bunny{
  override val alive: Boolean = true
  def nextBunny: Bunny =
    if (age+1 < MAX_BUNNY_AGE) AliveBunny(genotype, mom, dad, age+1) else DeadBunny(genotype, mom, dad, age+1)
}

/**
 * Represents a Bunny which is dead.
 * @param genotype  contains the Bunny genes, it must be completed (contain all the available Genes of the world)
 * @param mom       the bunny's mom
 * @param dad       the bunny's dad
 * @param age       how many generations the Bunny has spent in the world.
 */
case class DeadBunny(genotype: CompletedGenotype, mom:Option[Bunny], dad:Option[Bunny], age:Int) extends Bunny {
  override val alive: Boolean = false
}

/**
 * Represents a Bunny that as just been created.
 */
class ChildBunny(genotype: CompletedGenotype, mom:Option[Bunny], dad:Option[Bunny]) extends AliveBunny(genotype, mom, dad, 0)

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
  def getBunniesWithAllele(alleleKind: AlleleKind): Set[AliveBunny] = ???
}