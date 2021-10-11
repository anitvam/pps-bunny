package it.unibo.pps.bunny.model.bunny

import it.unibo.pps.bunny.controller.Controller
import it.unibo.pps.bunny.model.bunny.Mutation.{dominantMutation, recessiveMutation}
import it.unibo.pps.bunny.model.genome.Genes
import it.unibo.pps.bunny.model.genome.KindsUtils.resetDominance
import it.unibo.pps.bunny.model.world.Generation.Population
import it.unibo.pps.bunny.model.world.Reproduction._
import org.scalatest.{FlatSpec, Matchers}

class TestMutations extends FlatSpec with Matchers {
  private val couple: Couple = initialCoupleGenerator()
  private val children: Population = generateChildren(couple)
  private val mutationFurColor: Mutation = dominantMutation(Genes.FUR_COLOR)
  private val mutationFurLength: Mutation = recessiveMutation(Genes.FUR_LENGTH)
  private val mutationTeeth: Mutation = recessiveMutation(Genes.TEETH)
  private val mutationEars: Mutation = dominantMutation(Genes.EARS)
  private val mutationJump: Mutation = recessiveMutation(Genes.JUMP)

  "When introducing a Mutation, it" should "compare to the utmost only half population" in {
    resetDominance()
    Controller.insertMutation(mutationFurColor)

    val nextGeneration = nextGenerationBunnies(children, List(mutationFurColor))
    val couplesNum = combineCouples(children).size
    val bunnyWithMutation = nextGeneration filter (_.genotype.phenotype(Genes.FUR_COLOR) == Genes.FUR_COLOR.mutated)

    assert(nextGeneration.length == couplesNum * 4 + children.length)
    assert(bunnyWithMutation.length <= (nextGeneration.length - children.length) / 2 + 1)
  }

  "When introducing more than one Mutation, they" should "compare only a Mutation for Bunny" in {
    resetDominance()
    val mutations = List(mutationFurColor, mutationFurLength, mutationTeeth)
    mutations.foreach(Controller.insertMutation)

    val nextGeneration = nextGenerationBunnies(children, mutations)
    val bunnyWithMutation = nextGeneration filter (b =>
      b.genotype.phenotype(Genes.FUR_COLOR) == Genes.FUR_COLOR.mutated ||
        b.genotype.phenotype(Genes.FUR_COLOR) == Genes.FUR_COLOR.mutated ||
        b.genotype.phenotype(Genes.TEETH) == Genes.TEETH.mutated
    )
    val couplesNum = combineCouples(children).size

    assert(nextGeneration.length == couplesNum * 4 + children.length)
    assert(bunnyWithMutation.length <= (nextGeneration.length - children.length) / 2 + 1)
    assert(bunnyWithMutation.length <= (nextGeneration.length - children.length))
  }

  "When introducing more mutations than children there" should "be two mutations in some bunnies" in {
    resetDominance()
    val mutations = List(mutationFurColor, mutationFurLength, mutationTeeth, mutationEars, mutationJump)
    mutations.foreach(Controller.insertMutation)

    val brothers: Population = generateChildren(couple, mutations)
    val mutationsEachBunny: Seq[Int] = brothers map (b => b.genotype.mutatedAllelesQuantity)

    assert(mutationsEachBunny.count(_ == 1) == 3)
    assert(mutationsEachBunny.count(_ == 2) == 1)
  }

}
