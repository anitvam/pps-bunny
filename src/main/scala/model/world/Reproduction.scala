package model.world

import engine.SimulationConstants.{CHILDREN_FOR_EACH_COUPLE, MAX_BUNNY_AGE}
import model.Bunny.generateBaseFirstBunny
import model._
import model.genome._
import model.world.Generation.Population

import scala.util.Random

object Reproduction {

  type Couples = Seq[(Bunny, Bunny)]

  /**
   * @param bunnies a seq of bunnies
   * @return a seq of random couples formed from all of the bunnies (or most of them, if they are odd)
   */
  def combineCouples(bunnies: Population): Couples = {
    val split = Random.shuffle(bunnies).splitAt(bunnies.size / 2)
    split._1.zip(split._2)
  }

  /**
   * @param mom a bunny
   * @param dad another bunny
   * @return the 4 children of the couple, one for each cell of the Punnett's square
   */
  def generateChildren(mom: Bunny, dad: Bunny): Population = {
    var childrenGenotypes = List.fill(CHILDREN_FOR_EACH_COUPLE)(PartialGenotype(Map()))
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
      childrenGenotypes = (for (i <- 0 until CHILDREN_FOR_EACH_COUPLE) yield childrenGenotypes(i) + anotherGene(i)).toList
    })
    childrenGenotypes.map(cg => new ChildBunny(CompletedGenotype(cg.genes), Option(mom), Option(dad)))
  }

  /**
   * @param bunnies a seq of bunnies
   * @return a seq with the children of the bunnies
   */
  def generateAllChildren(bunnies: Population): Population =
    combineCouples(bunnies).flatMap(couple => generateChildren(couple._1, couple._2))

  /**    
   * @return the first two bunnies of the simulation
   * */
  def generateInitialCouple:Population = Seq(generateBaseFirstBunny, generateBaseFirstBunny)

  /**
   * @param bunnies bunnies from the last generation
   * @return        the new bunnies, adding the children and removing the ones who are dead
   */
  def nextGenerationBunnies(bunnies: Population): Population = {
    val children = generateAllChildren(bunnies)
    bunnies.foreach(_.age+=1)
    bunnies.foreach(b => if (b.age >= MAX_BUNNY_AGE) b.alive = false)
    children ++ bunnies.filter(_.alive)
  }
}
