package model.world

import engine.SimulationConstants.CHILDREN_FOR_EACH_COUPLE
import model.Bunny.generateBaseFirstBunny
import model._
import model.genome._
import model.world.Environment.Mutations
import model.world.Generation.Population
import util.PimpScala._

import scala.language.postfixOps
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

    // For each kind of gene
    Genes.values.foreach(gk => {

      // Create 4 new genes from the parents alleles, in random order
      var childrenGenes: List[Gene] =
        Random.shuffle(
          (for {momAllele <- mom.genotype.getStandardAlleles(gk).toSeq
                dadAllele <- dad.genotype.getStandardAlleles(gk).toSeq
                } yield Gene(gk, momAllele, dadAllele)).toList)

      // Check if there is a mutation for this kind of gene and substitute one of the genes with the mutated one
      if (mutations.find(_.geneKind == gk)?)
        childrenGenes = Gene(gk, JustMutatedAllele(gk.mutated), JustMutatedAllele(gk.mutated)) :: childrenGenes.take(CHILDREN_FOR_EACH_COUPLE - 1)

      // Add the 4 new genes to the children genotypes and put the genotype with less mutations at the beginning of the list,
      // so it will include the next mutated gene if there is one
      childrenGenotypes =
        (for (i <- 0 until CHILDREN_FOR_EACH_COUPLE)
          yield childrenGenotypes(i) + childrenGenes(i)).toList
          .sortBy(_.mutatedAllelesQuantity)
    })

    // Creates the bunnies with the complete genotypes
    childrenGenotypes.map(cg => new ChildBunny(CompleteGenotype(cg.genes), Option(mom), Option(dad)))
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
    bunnies.foreach(_.agingBunny())
    generateAllChildren(bunnies, mutations) ++ bunnies.filter(_.alive)
  }
}
