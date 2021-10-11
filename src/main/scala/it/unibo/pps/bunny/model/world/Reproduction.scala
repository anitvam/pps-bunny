package it.unibo.pps.bunny.model.world

import it.unibo.pps.bunny.engine.SimulationConstants._
import it.unibo.pps.bunny.model._
import it.unibo.pps.bunny.model.bunny.Bunny.baseBunnyGenerator
import it.unibo.pps.bunny.model.bunny._
import it.unibo.pps.bunny.model.genome._
import it.unibo.pps.bunny.model.world.Environment.Mutations
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.util.PimpScala.{ RichOption, RichSeq, RichTuple2 }

import scala.language.postfixOps

object Reproduction {

  case class Couple(mom: Bunny, dad: Bunny) {
    def toSeq: Population = Seq(mom, dad)
    if (mom.gender != Female || dad.gender != Male) throw new CoupleGendersException()
  }

  /**
   * @param bunnies
   *   a [[Population]] of bunnies
   * @return
   *   a [[Seq]] of random couples formed from all of the bunnies (or most of them, if they are odd)
   */
  def combineCouples(bunnies: Population): Seq[Couple] = {
    val split = bunnies partition (_.gender == Female)
    split._1.shuffle zip split._2.shuffle map (c => Couple(c._1, c._2))
  }

  /**
   * @param couple
   *   the couple of bunny
   * @param mutations
   *   list of the mutations
   * @return
   *   the 4 children of the couple, one for each cell of the Punnett's square
   */
  def generateChildren(couple: Couple, mutations: Mutations = List()): Population = {
    var childrenGenotypes = (List fill CHILDREN_FOR_EACH_COUPLE)(PartialGenotype(Map()))

    // For each kind of gene
    Genes.values.foreach(gk => {
      // Create 4 new genes from the parents alleles, in random order
      var childrenGenes: Seq[Gene] = (for {
        momAllele <- couple.mom.genotype.getStandardAlleles(gk).toSeq
        dadAllele <- couple.dad.genotype.getStandardAlleles(gk).toSeq
      } yield Gene(gk, momAllele, dadAllele)).shuffle

      // Check if there is a mutation for this kind of gene and substitute one of the genes with the mutated one
      if (mutations.find(_.geneKind == gk) ?)
        childrenGenes = Gene(gk, JustMutatedAllele(gk.mutated), JustMutatedAllele(gk.mutated)) +:
          childrenGenes.take(CHILDREN_FOR_EACH_COUPLE - 1)

      // Add the 4 new genes to the children genotypes and put the genotype with less mutations at the beginning of the list,
      // so it will include the next mutated gene if there is one
      childrenGenotypes =
        (for (i <- 0 until CHILDREN_FOR_EACH_COUPLE)
          yield childrenGenotypes(i) + childrenGenes(i)).toList sortBy (_.mutatedAllelesQuantity)
    })
    // Creates the bunnies with the complete genotypes, half of them are going to be Males and half Females
    val createBunny: (Genotype, Gender) => Bunny = (genotype, gender) =>
      ChildBunny(CompleteGenotype(genotype.genes), Option(couple.mom), Option(couple.dad), gender)
    val genotypesSplit = childrenGenotypes splitAt CHILDREN_FOR_EACH_COUPLE / 2
    (genotypesSplit._1 map (createBunny(_, Male))) ++ (genotypesSplit._2 map (createBunny(_, Female)))
  }

  /**
   * @param bunnies
   *   a [[Population]] of bunnies
   * @return
   *   the updated [[Population]] with the children of the bunnies
   */
  def generateAllChildren(bunnies: Population, mutations: Mutations = List()): Population = {
    val couples = combineCouples(bunnies)
    val (mutatedCouples, standardCouples) = couples.shuffle.splitAt((couples.length / 2) + 1)
    (standardCouples flatMap (couple => generateChildren(couple))) ++
      (mutatedCouples flatMap (couple => generateChildren(couple, mutations)))
  }

  /**
   * @param bunnies
   *   [[Population]] from the last generation
   * @return
   *   the new [[Population]], adding the children and removing the ones who are dead
   */
  def nextGenerationBunnies(bunnies: Population, mutations: Mutations = List()): Population = {
    bunnies foreach (_.increaseAge())
    generateAllChildren(bunnies, mutations) ++ (bunnies filter (_.alive))
  }

  /**
   * Generator for the first two bunnies of the simulation
   */
  val initialCoupleGenerator: () => Couple =
    () => Couple(mom = baseBunnyGenerator(Female), dad = baseBunnyGenerator(Male))

}
