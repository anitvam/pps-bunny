package model
import engine.SimulationHistory.{getPopulationForNextGeneration, introduceMutation, startNextGeneration}
import model.Bunny.generateRandomFirstBunny
import model.genome.Genes
import model.mutation.Mutation
import model.world.Reproduction.{generateAllChildren, generateChildren, nextGenerationBunnies}
import org.scalatest.{FlatSpec, Matchers}

class TestMutations extends FlatSpec with Matchers {
  val children: Seq[Bunny] = generateChildren(generateRandomFirstBunny, generateRandomFirstBunny)

  "When introducing a Mutation" should "compare in only half population" in {
    introduceMutation(Mutation(Genes.FUR_COLOR, true))
    val nextGeneration = nextGenerationBunnies(children, Some(List(Mutation(Genes.FUR_COLOR, true))))
    var nBunnyWithMutation = 0
    nextGeneration filter(_.genotype.genes(Genes.FUR_COLOR).kind == Genes.FUR_COLOR) foreach {
      nBunnyWithMutation = nBunnyWithMutation + 1
    }
    assert(nBunnyWithMutation == (12/2 + 1))
  }
}
