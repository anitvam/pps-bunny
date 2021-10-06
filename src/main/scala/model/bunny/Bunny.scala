package model.bunny

import engine.SimulationConstants.MAX_BUNNY_AGE
import model.bunny.Gender.{Gender, randomGender}
import model.genome.Alleles.AlleleKind
import model.genome.Genes.GeneKind
import model.genome.KindsUtils.getRandomAlleleKind
import model.genome._
import model.world.Generation.Population

/**
 * Represents a Bunny.
 */
sealed trait Bunny {
  val genotype: CompleteGenotype
  val mom: Option[Bunny]
  val dad: Option[Bunny]
  val gender: Gender
  var age: Int
  var alive: Boolean

  override def toString: String = {
    super.toString + "\n gender: " + gender + "\n alive:" + alive + " \n age: " + age + "\n" +
      genotype.genes
        .map(g => "\t" + g._1 + ": " + g._2.getVisibleTrait.toString.toLowerCase + " (" + g._2.getLetters + ")")
        .reduce(_ + "\n" + _)
        .replace("_", " ") + "\n"
  }

  /**
   * Updates the bunny instance for the next generation, increasing the age and setting the right alive value.
   * @return
   *   a sequence of standard alleles with the parents kind, useful during the generation of children
   */
  def agingBunny(): Unit = {
    age += 1
    if (age >= MAX_BUNNY_AGE) alive = false
  }

}

/**
 * Represents a Bunny that as just been created.
 */
class ChildBunny(
    override val genotype: CompleteGenotype,
    override val mom: Option[Bunny],
    override val dad: Option[Bunny],
    override val gender: Gender,
    override var age: Int = 0,
    override var alive: Boolean = true
) extends Bunny

/**
 * Represents the first Bunny which appears in the world, so it does not have a mom and a dad.
 */
class FirstBunny(genotype: CompleteGenotype, gender: Gender) extends ChildBunny(genotype, Option.empty, Option.empty, gender)

object Bunny {
  type baseBunnies = Seq[Bunny]
  type mutatedBunnies = Seq[Bunny]

  /**
   * @return
   *   a FirstBunny with the "base" allele for each gene
   */
  val generateBaseFirstBunny: (Gender) =>  FirstBunny = gender => new FirstBunny(
    CompleteGenotype(
      Genes.values.unsorted.map(gk => (gk, Gene(gk, StandardAllele(gk.base), StandardAllele(gk.base)))).toMap
    ), gender)

  /**
   * @return
   *   a FirstBunny with a random allele for each gene
   */
  val generateRandomFirstBunny: () => FirstBunny = () => {
    new FirstBunny(
      CompleteGenotype(
        Genes.values.unsorted
          .map(gk => {
            (gk, Gene(gk, StandardAllele(getRandomAlleleKind(gk)), StandardAllele(getRandomAlleleKind(gk))))
          })
          .toMap
      ), randomGender()
    )
  }

  /**
   * @param geneKind
   *   the kind of Gene we want to split the bunnies by
   * @param bunnies
   *   all the bunnies
   * @return
   *   a tuple with the sequence of bunnies with the base Allele and the sequence of bunnies with the mutated Allele
   */
  def splitBunniesByGene(geneKind: GeneKind, bunnies: Seq[Bunny]): (baseBunnies, mutatedBunnies) =
    bunnies.partition(_.genotype.phenotype(geneKind) == geneKind.base)

  /**
   * Method that filter a population of bunnies with two AlleleKinds together
   * @param bunnies
   *   the bunny population
   * @param allele1
   *   the first allele filtered on the population
   * @param allele2
   *   the second allele filtered in population
   * @return
   *   Population the population with the specified AlleleKinds together
   */
  def filterBunniesWithAlleles(bunnies: Population, allele1: AlleleKind, allele2: AlleleKind): Population =
    bunnies filter { bunny =>
      bunny.genotype.phenotype.values.exists(_ == allele1) &&
      bunny.genotype.phenotype.values.exists(_ == allele2)
    }
}
