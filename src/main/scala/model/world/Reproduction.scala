package model.world

import engine.SimulationConstants.{CHILDREN_EACH_COUPLE, MAX_BUNNY_AGE}
import model.Bunny.generateBaseFirstBunny
import model._
import model.genome._
import model.mutation.{Mutation, Mutations}
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
   * First implementation of the introducing mutation selected by user (for now introduced randomly in the creation of
   * the children
   * @param mutations the list of all the mutations introduced, if present
   * @return the list of Gene derived from the application of each mutation, an empty list if there is no mutation introduced
   */
  def introducingMutation(mutations: List[Mutation]): List[Gene] = {
    val genesWithMutations: List[Gene] = List()
    mutations.foreach(m => m match {
      case Mutations.FUR_COLOR_MUTATION_DOMINANT => genesWithMutations :+ Gene( Mutations.FUR_COLOR_MUTATION_DOMINANT.geneKind,
                                                                                JustMutatedAllele(Alleles.BROWN_FUR),
                                                                                JustMutatedAllele(Alleles.BROWN_FUR))
      case Mutations.FUR_COLOR_MUTATION_RECESSIVE => genesWithMutations :+ Gene( Mutations.FUR_COLOR_MUTATION_RECESSIVE.geneKind,
                                                                                 JustMutatedAllele(Alleles.BROWN_FUR),
                                                                                 StandardAllele(Alleles.WHITE_FUR))
      case Mutations.FUR_LENGTH_MUTATION_DOMINANT => genesWithMutations :+ Gene( Mutations.FUR_LENGTH_MUTATION_DOMINANT.geneKind,
                                                                                 JustMutatedAllele(Alleles.LONG_FUR),
                                                                                 JustMutatedAllele(Alleles.LONG_FUR))
      case Mutations.FUR_LENGTH_MUTATION_RECESSIVE => genesWithMutations :+ Gene( Mutations.FUR_LENGTH_MUTATION_RECESSIVE.geneKind,
                                                                                  JustMutatedAllele(Alleles.LONG_FUR),
                                                                                  StandardAllele(Alleles.SHORT_FUR))
      case Mutations.TEETH_MUTATION_DOMINANT => genesWithMutations :+ Gene( Mutations.TEETH_MUTATION_DOMINANT.geneKind,
                                                                            JustMutatedAllele(Alleles.LONG_TEETH),
                                                                            JustMutatedAllele(Alleles.LONG_TEETH))
      case Mutations.TEETH_MUTATION_RECESSIVE => genesWithMutations :+ Gene( Mutations.TEETH_MUTATION_RECESSIVE.geneKind,
                                                                             JustMutatedAllele(Alleles.LONG_TEETH),
                                                                             StandardAllele(Alleles.SHORT_TEETH))
      case Mutations.EARS_MUTATION_DOMINANT => genesWithMutations :+ Gene( Mutations.EARS_MUTATION_DOMINANT.geneKind,
                                                                           JustMutatedAllele(Alleles.HIGH_EARS),
                                                                           JustMutatedAllele(Alleles.HIGH_EARS))
      case Mutations.EARS_MUTATION_RECESSIVE => genesWithMutations :+ Gene( Mutations.EARS_MUTATION_RECESSIVE.geneKind,
                                                                            JustMutatedAllele(Alleles.HIGH_EARS),
                                                                            StandardAllele(Alleles.LOW_EARS))
      case Mutations.JUMP_MUTATION_DOMINANT => genesWithMutations :+ Gene( Mutations.JUMP_MUTATION_DOMINANT.geneKind,
                                                                           JustMutatedAllele(Alleles.HIGH_JUMP),
                                                                           JustMutatedAllele(Alleles.HIGH_JUMP))
      case Mutations.JUMP_MUTATION_RECESSIVE => genesWithMutations :+ Gene( Mutations.JUMP_MUTATION_RECESSIVE.geneKind,
                                                                            JustMutatedAllele(Alleles.HIGH_JUMP),
                                                                            StandardAllele(Alleles.LOW_JUMP))
    })

    genesWithMutations
  }

  /**
   * @param mom a bunny
   * @param dad another bunny
   * @return the 4 children of the couple, one for each cell of the Punnett's square
   */
  def generateChildren(mom: Bunny, dad: Bunny)(mutations: Option[List[Mutation]]): Population = {
    var childrenGenotypes = List.fill(CHILDREN_EACH_COUPLE)(PartialGenotype(Map()))
    var genesWithMutations: List[Gene] = List()
    if( mutations.isDefined ) {
       genesWithMutations = introducingMutation(mutations.get)
    }

    Genes.values.foreach(gk => {
      val grandmaMomAllele = mom.genotype(gk).momAllele
      val grandpaMomAllele = mom.genotype(gk).dadAllele
      val grandmaDadAllele = dad.genotype(gk).momAllele
      val grandpaDadAllele = dad.genotype(gk).dadAllele
      val genesOfReproduction : List[Gene]= List.concat(
        List( Gene(gk, grandmaMomAllele, grandmaDadAllele),
              Gene(gk, grandpaMomAllele, grandmaDadAllele),
              Gene(gk, grandmaMomAllele, grandpaDadAllele),
              Gene(gk, grandpaMomAllele, grandpaDadAllele)
        ),
        genesWithMutations)
      val anotherGene = Random.shuffle(genesOfReproduction)
      childrenGenotypes = (for (i <- 0 until CHILDREN_EACH_COUPLE) yield childrenGenotypes(i) + anotherGene(i)).toList
    })
    childrenGenotypes.map(cg => new ChildBunny(CompletedGenotype(cg.genes), Option(mom), Option(dad)))
  }

  /**
   * @param bunnies a seq of bunnies
   * @return a seq with the children of the bunnies
   */
  def generateAllChildren(bunnies: Population)(mutations: Option[List[Mutation]]): Population =
    combineCouples(bunnies).flatMap(couple => generateChildren(couple._1, couple._2)(mutations))

  /**
   * @return the first two bunnies of the simulation
   * */
  def generateInitialCouple:Population = Seq(generateBaseFirstBunny, generateBaseFirstBunny)

  /**
   * @param bunnies bunnies from the last generation
   * @return        the new bunnies, adding the children and removing the ones who are dead
   */
  def nextGenerationBunnies(bunnies: Population)(mutations: Option[List[Mutation]]): Population = {
    val children = generateAllChildren(bunnies)(mutations)
    bunnies.foreach(_.age+=1)
    bunnies.foreach(b => if (b.age >= MAX_BUNNY_AGE) b.alive = false)
    val stillAlive = bunnies.filter(_.alive)
    children ++ stillAlive
  }
}
