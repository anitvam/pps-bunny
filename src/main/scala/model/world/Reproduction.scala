package model.world


import model.BunnyConstants.{CHILDREN_EACH_COUPLE, MAX_BUNNY_AGE}
import model._
import model.genome._

import scala.util.Random

object Reproduction {
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
      val grandmaMomAllele = mom.genotype(gk).momAllele
      val grandpaMomAllele = mom.genotype(gk).dadAllele
      val grandmaDadAllele = dad.genotype(gk).momAllele
      val grandpaDadAllele = dad.genotype(gk).dadAllele
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
   * @param previousBunnies bunnies from the last generation
   * @return        the new bunnies, adding the children and removing the ones who are dead
   */
  def nextGenerationBunnies(previousBunnies: Seq[Bunny]): Seq[Bunny] = {
    val children = generateAllChildren(previousBunnies)
    previousBunnies.foreach(b => if (b.age == MAX_BUNNY_AGE) b.alive = false)
    previousBunnies.foreach(b => if (b.alive) b.age+=1)
    val stillAlive = previousBunnies.filter(_.alive)
    children ++ stillAlive
  }
}
