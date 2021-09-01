package model.world

import model._
import model.genome._
import model.world.BunnyIdGenerator.getNextId

import scala.util.Random

object BunnyIdGenerator {
  var nextId: Int = -1

  def getNextId: Int = {
    nextId += 1
    nextId
  }
}

object Reproduction {
  val CHILDREN_EACH_COUPLE = 4
  val MAX_BUNNY_AGE = 4

  /**
   * @param bunnies a seq of bunnies
   * @return a seq of random couples formed from all of the bunnies (or most of them, if they are odd)
   */
  def combineCouples(bunnies: Seq[AliveBunny]): Seq[(AliveBunny, AliveBunny)] = {
    val split = Random.shuffle(bunnies).splitAt(bunnies.size / 2)
    split._1.zip(split._2)
  }

  /**
   * @param mom a bunny
   * @param dad another bunny
   * @return the 4 children of the couple, one for each cell of the Punnett's square
   */
  def generateChildren(mom: Bunny, dad: Bunny): Seq[AliveBunny] = {
    var childrenGenotypes = List.fill(CHILDREN_EACH_COUPLE)(PartialGenotype(Map()))
    Genes.values.foreach(gk => {
      val grandmaMomAllele = mom.genotype.genes(gk).momAllele
      val grandpaMomAllele = mom.genotype.genes(gk).dadAllele
      val grandmaDadAllele = dad.genotype.genes(gk).momAllele
      val grandpaDadAllele = dad.genotype.genes(gk).dadAllele

      val anotherGene = Random.shuffle(
        List(StandardGene(gk, grandmaMomAllele, grandmaDadAllele),
          StandardGene(gk, grandpaMomAllele, grandmaDadAllele),
          StandardGene(gk, grandmaMomAllele, grandpaDadAllele),
          StandardGene(gk, grandpaMomAllele, grandpaDadAllele)))
      childrenGenotypes = (for (i <- 0 until CHILDREN_EACH_COUPLE) yield childrenGenotypes(i) + anotherGene(i)).toList
    })
    childrenGenotypes.map(cg => new ChildBunny(CompletedGenotype(cg.genes), Option(mom), Option(dad)))
  }

  /**
   * @param bunnies a seq of bunnies
   * @return a seq with the children of the bunnies
   */
  def generateAllChildren(bunnies: Seq[AliveBunny]): Seq[AliveBunny] =
    combineCouples(bunnies).flatMap(couple => generateChildren(couple._1, couple._2))

  /**
   * @param bunnies the bunnies in the previous generation, the alive and dead ones
   * @return the bunnies of the next generation:
   *         the alive will contain the children and the bunnies still alive,
   *         the dead will contain all the dead bunnies, the ones which died in the past the ones who died in this generation
   */
  def nextGenerationBunnies(bunnies: GenerationBunnies): GenerationBunnies = {
    val children = generateAllChildren(bunnies.aliveBunnies.values.toSeq)
    val nextBunnies = bunnies.aliveBunnies.map(entry => (entry._1, entry._2.nextBunny))
    val nextAliveBunnies: Map[Int, AliveBunny] = nextBunnies.filter(_._2.isInstanceOf[AliveBunny])
                                                          .map(entry => (entry._1, entry._2.asInstanceOf[AliveBunny]))
    val nextDeadBunnies: Map[Int, DeadBunny] = nextBunnies.filter(_._2.isInstanceOf[DeadBunny])
                                                          .map(entry => (entry._1, entry._2.asInstanceOf[DeadBunny]))
    StandardGenerationBunnies(children.map((getNextId, _)).toMap ++ nextAliveBunnies,
                              bunnies.deadBunnies ++ nextDeadBunnies)
  }
}
