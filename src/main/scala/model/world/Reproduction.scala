package model.world

import engine.SimulationConstants.{CHILDREN_EACH_COUPLE, MAX_BUNNY_AGE}
import model.Bunny.generateBaseFirstBunny
import model._
import model.genome._
import model.mutation.Mutation
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
  def generateChildren(mom: Bunny, dad: Bunny, mutations: Option[List[Mutation]] = None): Population = {
    var childrenGenotypes = List.fill(CHILDREN_EACH_COUPLE)(PartialGenotype(Map()))

    Genes.values.foreach(gk => {
      val grandmaMomAllele = StandardAllele(mom.genotype(gk).momAllele.kind)
      val grandpaMomAllele = StandardAllele(mom.genotype(gk).dadAllele.kind)
      val grandmaDadAllele = StandardAllele(dad.genotype(gk).momAllele.kind)
      val grandpaDadAllele = StandardAllele(dad.genotype(gk).dadAllele.kind)
      val genesOfReproduction : List[Gene]= List( Gene(gk, grandmaMomAllele, grandmaDadAllele),
        Gene(gk, grandpaMomAllele, grandmaDadAllele),
        Gene(gk, grandmaMomAllele, grandpaDadAllele),
        Gene(gk, grandpaMomAllele, grandpaDadAllele) )
      val shuffledGenes = Random.shuffle(genesOfReproduction)
      childrenGenotypes = (for (i <- 0 until CHILDREN_EACH_COUPLE) yield childrenGenotypes(i) + shuffledGenes(i)).toList

      mutations match {
        case None =>
        case Some(ms) => ms filter(_.geneKind == gk) foreach { m => childrenGenotypes = childrenGenotypes(CHILDREN_EACH_COUPLE - 1) +
          Gene(m.geneKind, JustMutatedAllele(gk.mutated), JustMutatedAllele(gk.mutated)) :: childrenGenotypes.take(CHILDREN_EACH_COUPLE - 1)}
      }
    })
    childrenGenotypes.map(cg => new ChildBunny(CompletedGenotype(cg.genes), Option(mom), Option(dad)))
  }

  /**
   * @param bunnies a seq of bunnies
   * @return a seq with the children of the bunnies
   */
  def generateAllChildren(bunnies: Population, mutations: Option[List[Mutation]] = None): Population = {
    val couples = combineCouples(bunnies)
    val (coupleWithMutations, coupleWithoutMutations) = Random.shuffle(couples).splitAt((couples.length / 2) + 1)
    coupleWithoutMutations.flatMap(couple => generateChildren(couple._1, couple._2)) ++
      coupleWithMutations.flatMap(couple => generateChildren(couple._1, couple._2, mutations))
  }

  /**    
   * @return the first two bunnies of the simulation
   * */
  def generateInitialCouple:Population = Seq(generateBaseFirstBunny, generateBaseFirstBunny)

  /**
   * @param bunnies bunnies from the last generation
   * @return        the new bunnies, adding the children and removing the ones who are dead
   */
  def nextGenerationBunnies(bunnies: Population, mutations: Option[List[Mutation]] = None): Population = {
    val children = generateAllChildren(bunnies, mutations)
    bunnies.foreach(_.age+=1)
    bunnies.foreach(b => if (b.age >= MAX_BUNNY_AGE) b.alive = false)
    children ++ bunnies.filter(_.alive)
  }
}
