package model
import engine.SimulationHistory.introduceMutation
import model.Bunny.generateBaseFirstBunny
import model.genome.Genes
import model.genome.KindsUtils.resetDominance
import model.mutation.Mutation
import model.world.Reproduction.{generateChildren, nextGenerationBunnies}
import org.scalatest.{FlatSpec, Matchers}

class TestMutations extends FlatSpec with Matchers {
  resetDominance()
  val children: Seq[Bunny] = generateChildren(generateBaseFirstBunny, generateBaseFirstBunny)
  val mutationFurColor: Mutation = Mutation(Genes.FUR_COLOR, isDominant = true)
  val mutationFurLength: Mutation = Mutation(Genes.FUR_LENGTH, isDominant = false)
  val mutationTeeth: Mutation = Mutation(Genes.TEETH, isDominant = false)

  "When introducing a Mutation" should "compare to the utmost only half population" in {
    introduceMutation(mutationFurColor)

    val nextGeneration = nextGenerationBunnies(children, List(mutationFurColor))
    val bunnyWithMutation = nextGeneration filter (_.genotype.phenotype(Genes.FUR_COLOR) == Genes.FUR_COLOR.mutated)

    assert( nextGeneration.length == children.length / 2 * 4 + children.length )
    assert( bunnyWithMutation.length <= (nextGeneration.length - children.length)/2 + 1 )
  }

  "When introducing more than one Mutations" should "compare only a Mutation for Bunny" in {
    val mutations = List(mutationFurColor, mutationFurLength, mutationTeeth)
    try {
      mutations.foreach(introduceMutation)
    } catch {
      case _: MultipleDominanceAssignmentException  => println("Allele corresponding to the Mutation set before. Ignoring this method")
    }

    val nextGeneration = nextGenerationBunnies(children, mutations)
    val bunnyWithMutation = nextGeneration filter (b =>
      b.genotype.phenotype(Genes.FUR_COLOR) == Genes.FUR_COLOR.mutated ||
      b.genotype.phenotype(Genes.FUR_COLOR) == Genes.FUR_COLOR.mutated ||
      b.genotype.phenotype(Genes.TEETH) == Genes.TEETH.mutated )

    assert( nextGeneration.length == children.length / 2 * 4 + children.length )
    assert( bunnyWithMutation.length <= (nextGeneration.length - children.length) / 2 + 1 )
    assert( bunnyWithMutation.length <= (nextGeneration.length -  children.length))
  }

}
