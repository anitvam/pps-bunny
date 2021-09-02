package model.world

import model._
import model.genome._

import scala.util.Random

object Reproduction {
  val CHILDREN_EACH_COUPLE = 4
  val MAX_BUNNY_AGE = 4

  /**
   * @param bunnies a seq of bunnies
   * @return a seq of random couples formed from all of the bunnies (or most of them, if they are odd)
   */
  def combineCouples(bunnies: Seq[Bunny]): Seq[(Bunny, Bunny)] = {
    val split = Random.shuffle(bunnies).splitAt(bunnies.size / 2)
    split._1.zip(split._2)
  }

  /**
   * @param mom a bunny
   * @param dad another bunny
   * @return the 4 children of the couple, one for each cell of the Punnett's square
   */
  def generateChildren(mom: Bunny, dad: Bunny): Seq[Bunny] = {
    var childrenGenotypes = List.fill(CHILDREN_EACH_COUPLE)(PartialGenotype(Map()))
    Genes.values.foreach(gk => {
      val grandmaMomAllele = mom.genotype.genes(gk).momAllele
      val grandpaMomAllele = mom.genotype.genes(gk).dadAllele
      val grandmaDadAllele = dad.genotype.genes(gk).momAllele
      val grandpaDadAllele = dad.genotype.genes(gk).dadAllele

      val anotherGene = Random.shuffle(
        List( Gene(gk, grandmaMomAllele, grandmaDadAllele),
              Gene(gk, grandpaMomAllele, grandmaDadAllele),
              Gene(gk, grandmaMomAllele, grandpaDadAllele),
              Gene(gk, grandpaMomAllele, grandpaDadAllele)))
      childrenGenotypes = (for (i <- 0 until CHILDREN_EACH_COUPLE) yield childrenGenotypes(i) + anotherGene(i)).toList
    })
    childrenGenotypes.map(cg => new ChildBunny(CompletedGenotype(cg.genes), Option(mom), Option(dad)))
  }

  /**
   * @param bunnies a seq of bunnies
   * @return a seq with the children of the bunnies
   */
  def generateAllChildren(bunnies: Seq[Bunny]): Seq[Bunny] =
    combineCouples(bunnies).flatMap(couple => generateChildren(couple._1, couple._2))

  /**
   * @param bunnies bunnies from the last generation
   * @return        the new bunnies, adding the children and removing the ones who are dead
   */
  def nextGenerationBunnies(bunnies: Seq[Bunny]): Seq[Bunny] = {
    val children = generateAllChildren(bunnies)
    bunnies.foreach(_.age+=1)
    bunnies.foreach(b => if (b.age >= MAX_BUNNY_AGE) b.alive = false)
    val stillAlive = bunnies.filter(_.alive)
    children ++ stillAlive
  }
}
