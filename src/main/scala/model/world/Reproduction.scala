package model.world

import engine.SimulationConstants.CHILDREN_FOR_EACH_COUPLE
import model.Bunny.generateBaseFirstBunny
import model._
import model.genome._
import model.world.Environment.Mutations
import model.world.Generation.Population
import util.PimpScala._

import scala.util.Random

object Reproduction {

  type Couples = Seq[Couple]
  type Couple = (Bunny, Bunny)

  /**
   * @param bunnies
   *   a seq of bunnies
   * @return
   *   a seq of random couples formed from all of the bunnies (or most of them, if they are odd)
   */
  def combineCouples(bunnies: Population): Couples = {
    val split = Random.shuffle(bunnies).splitAt(bunnies.size / 2)
    split._1 zip split._2
  }

  /**
   * @param mom a bunny
   * @param dad another bunny
   * @return the 4 children of the couple, one for each cell of the Punnett's square
   */
  def generateChildren(mom: Bunny, dad: Bunny, mutations: Mutations = List()): Population = {
    var childrenGenotypes = List.fill(CHILDREN_FOR_EACH_COUPLE)(PartialGenotype(Map()))

    Genes.values.foreach(gk => {
      var childrenGenes: List[Gene] =
        Random.shuffle(
          (for {momAllele <- mom.getStandardAlleles(gk).toSeq
                dadAllele <- dad.getStandardAlleles(gk).toSeq
                } yield Gene(gk, momAllele, dadAllele)).toList)

      if (mutations.get(_.geneKind == gk) ?)
        childrenGenes = Gene(gk, JustMutatedAllele(gk.mutated), JustMutatedAllele(gk.mutated)) :: childrenGenes.take(CHILDREN_FOR_EACH_COUPLE - 1)

      childrenGenotypes =
        (for (i <- 0 until CHILDREN_FOR_EACH_COUPLE)
          yield childrenGenotypes(i) + childrenGenes(i)).toList
    })

    childrenGenotypes.map(cg => new ChildBunny(CompletedGenotype(cg.genes), Option(mom), Option(dad)))
  }

  /**
   * @param bunnies a seq of bunnies
   * @return a seq with the children of the bunnies
   */
  def generateAllChildren(bunnies: Population, mutations: Mutations = List()): Population = {
    val couples = combineCouples(bunnies)
    val (coupleWithMutations, coupleWithoutMutations) = Random.shuffle(couples).splitAt((couples.length / 2) + 1)
    coupleWithoutMutations.flatMap(couple => generateChildren(couple._1, couple._2)) ++
      coupleWithMutations.flatMap(couple => generateChildren(couple._1, couple._2, mutations))
  }

  /**    
   * @return the first two bunnies of the simulation
   * */
  def generateInitialCouple: Couple = (generateBaseFirstBunny, generateBaseFirstBunny)

  /**
   * @param bunnies bunnies from the last generation
   * @return        the new bunnies, adding the children and removing the ones who are dead
   */
  def nextGenerationBunnies(bunnies: Population, mutations: Mutations = List()): Population = {
    bunnies.foreach(_.updateBunny())
    generateAllChildren(bunnies, mutations) ++ bunnies.filter(_.alive)
  }
}
