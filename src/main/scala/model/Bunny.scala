package model

import scala.util.Random

/**
 * Represents a Bunny.
 */
sealed trait Bunny{
  val genotype: CompletedGenotype
  val mom: Option[Bunny]
  val dad: Option[Bunny]
  val age: Int
  val isAlive: Boolean

  override def toString: String = {
    super.toString + "\n" + genotype.genes
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
  override val isAlive: Boolean = true
}

/**
 * Represents a Bunny which dead.
 * @param genotype  contains the Bunny genes, it must be completed (contain all the available Genes of the world)
 * @param mom       the bunny's mom
 * @param dad       the bunny's dad
 * @param age       how many generations the Bunny has spent in the world.
 */
case class DeadBunny(genotype: CompletedGenotype, mom:Option[Bunny], dad:Option[Bunny], age:Int) extends Bunny{
  override val isAlive: Boolean = false
}

/**
 * Represents the abstraction of a Bunny that as just been created.
 */
abstract class InitBunny() extends Bunny{
  override val age: Int = 0
  override val isAlive: Boolean = true
}

/**
 * Represents a Bunny that as just been created.
 */
case class ChildBunny(genotype: CompletedGenotype, mom:Option[Bunny], dad:Option[Bunny]) extends InitBunny

/**
 * Represents the first Bunny which appears in the world, so it does not have a mom and a dad.
 */
case class FirstBunny(genotype: CompletedGenotype) extends InitBunny {
  override val mom: Option[Bunny] = Option.empty
  override val dad: Option[Bunny] = Option.empty
}

object BunnyUtils {
  val CHILDREN_NUMBER = 4

  /**
   * @return a FirstBunny with the "base" allele for each gene
   */
  def getBaseFirstBunny: FirstBunny =
    FirstBunny(CompletedGenotype(Genes.values.unsorted.map(gk =>
      (gk, StandardGene(gk, StandardAllele(gk.base), StandardAllele(gk.base)))).toMap))

  /**
   * @return a FirstBunny with a random allele for each gene
   */
  def getRandomFirstBunny: FirstBunny = {
    FirstBunny(
      CompletedGenotype(
        Genes.values.unsorted.map(gk => {
          (gk, StandardGene(gk, StandardAllele(List(gk.base, gk.mutated)(Random.nextInt(2))),
                                StandardAllele(List(gk.base, gk.mutated)(Random.nextInt(2)))))
        }).toMap
      )
    )
  }

  /**
   * @param bunnies   a seq of bunnies
   * @return          a seq of random couples formed from all of the bunnies (or most of them, if they are odd)
   */
  def getCouples(bunnies: Seq[Bunny]): Seq[(Bunny, Bunny)] = {
    val split = Random.shuffle(bunnies).splitAt(bunnies.size/2)
    split._1.zip(split._2)
  }

  /**
   * @param mom a bunny
   * @param dad another bunny
   * @return    the 4 children of the couple, one for each cell of the Punnett's square
   */
  def getChildren(mom: Bunny, dad:Bunny): Seq[Bunny] = {
    var children = List.fill(CHILDREN_NUMBER)(PartialGenotype(Map()))
    Genes.values.foreach(gk => {
      val grandmaMomAllele= mom.genotype.genes(gk).momAllele
      val grandpaMomAllele = mom.genotype.genes(gk).dadAllele
      val grandmaDadAllele= dad.genotype.genes(gk).momAllele
      val grandpaDadAllele= dad.genotype.genes(gk).dadAllele

      val childrenGenes = Random.shuffle(
        List( StandardGene(gk, grandmaMomAllele, grandmaDadAllele),
              StandardGene(gk, grandpaMomAllele, grandmaDadAllele),
              StandardGene(gk, grandmaMomAllele, grandpaDadAllele),
              StandardGene(gk, grandpaMomAllele, grandpaDadAllele)))
      children = (for (i <- 0 until CHILDREN_NUMBER) yield PartialGenotype(children(i) + childrenGenes(i))).toList
    })
    children.map(child => ChildBunny(genotype = CompletedGenotype(child.genes), mom = Option(mom), dad = Option(dad)))
  }

  /**
   * @param bunnies a seq of bunnies
   * @return        a seq with the children of the bunnies
   */
  def getAllChildren(bunnies: Seq[Bunny]): Seq[Bunny] =
    getCouples(bunnies).flatMap(couple => getChildren(couple._1, couple._2))

  /**
   * @param bunnies a list of bunnies
   * @return        a seq with all the bunnies present after the breeding
   */
  def getNextGenerationBunnies(bunnies: Seq[Bunny]): Seq[Bunny] =
    getAllChildren(bunnies) ++ bunnies

  /***
   * @param generations the number of older generations to retrieve
   * @param bunny       the subject bunny
   * @return            the genealogical tree of the bunny for the specified generations
   */
  def getTree(generations: Int, bunny: Bunny) = ???
}