package it.unibo.pps.bunny.model.bunny

import it.unibo.pps.bunny.engine.SimulationConstants.MAX_BUNNY_AGE
import it.unibo.pps.bunny.model.HistoryBunnyUpdateException
import it.unibo.pps.bunny.model.genome.Alleles.AlleleKind
import it.unibo.pps.bunny.model.genome.Genes.GeneKind
import it.unibo.pps.bunny.model.genome.KindsUtils.randomAlleleKindChooser
import it.unibo.pps.bunny.model.genome._
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.util.PimpScala.RichSeq

sealed trait Gender
case object Male extends Gender
case object Female extends Gender

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
    super.toString +
      "\n gender: " + gender +
      "\n alive:" + alive +
      "\n age: " + age +
      "\n" + genotype.toString
  }

  /**
   * Updates the bunny instance for the next generation, increasing the age and setting the right alive value.
   * @return
   *   a sequence of standard alleles with the parents kind, useful during the generation of children
   */
  def increaseAge(): Unit = {
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
    override val gender: Gender
) extends Bunny {
  override var age: Int = 0
  override var alive: Boolean = true
}

/**
 * Represents the first Bunny which appears in the world, so it does not have a mom and a dad.
 */
class FirstBunny(genotype: CompleteGenotype, gender: Gender)
    extends ChildBunny(genotype, Option.empty, Option.empty, gender)

/**
 * Represents a Bunny in a defined moment in history and is immutable.
 */
class HistoryBunny(bunny: Bunny) extends Bunny {
  override val genotype: CompleteGenotype = bunny.genotype
  override val mom: Option[Bunny] = bunny.mom
  override val dad: Option[Bunny] = bunny.dad
  override val age: Int = bunny.age
  override val alive: Boolean = bunny.alive
  override val gender: Gender = bunny.gender

  def age_=(age: Int): Unit = throw new HistoryBunnyUpdateException
  def alive_=(alive: Boolean): Unit = throw new HistoryBunnyUpdateException
}

object Bunny {
  type baseBunnies = Seq[Bunny]
  type mutatedBunnies = Seq[Bunny]

  /**
   * Function to get a random gender for the Bunny.
   */
  val randomGenderChooser: () => Gender = () => Seq(Male, Female).random

  /**
   * Generator for a Bunny with the "base" allele for each gene.
   */
  val baseBunnyGenerator: Gender => FirstBunny = gender =>
    new FirstBunny(
      CompleteGenotype(
        Genes.values.unsorted.map(gk => (gk, Gene(gk, StandardAllele(gk.base), StandardAllele(gk.base)))).toMap
      ),
      gender
    )

  /**
   * Generator for a Bunny with a random allele for each gene.
   */
  val randomBunnyGenerator: () => FirstBunny = () => {
    new FirstBunny(
      CompleteGenotype(
        Genes.values.unsorted
          .map(gk => {
            (gk, Gene(gk, StandardAllele(randomAlleleKindChooser(gk)), StandardAllele(randomAlleleKindChooser(gk))))
          })
          .toMap
      ),
      randomGenderChooser()
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
  def splitBunniesByGene(geneKind: GeneKind, bunnies: Population): (baseBunnies, mutatedBunnies) =
    bunnies.partition(_.genotype.phenotype(geneKind) == geneKind.base)

  /**
   * Method that filter a population of bunnies with two AlleleKinds together
   * @param bunnies
   *   the bunny population
   * @param alleleKinds
   *   the alleles filter the population with
   * @return
   *   Population the population with the specified AlleleKinds together
   */
  def filterBunniesWithAlleles(bunnies: Population, alleleKinds: AlleleKind*): Population = bunnies filter { bunny =>
    alleleKinds.count(ak => bunny.genotype.phenotype.values.exists(_ == ak)) == alleleKinds.size
  }

}
