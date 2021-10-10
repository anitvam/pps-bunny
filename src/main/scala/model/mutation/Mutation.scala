package model.mutation

import model.genome.Genes.GeneKind

/**
 * Model representation of the mutation
 */
trait Mutation {
  val geneKind: GeneKind
  val isDominant: Boolean
}

object Mutation {

  /**
   * factory to create a recessive mutation
   * @param geneKind
   *   the gene mutated
   */
  def recessiveMutation(geneKind: GeneKind): Mutation = MutationImpl(geneKind, isDominant = false)

  /**
   * factory to create a dominant mutation
   * @param geneKind
   *   the gene mutated
   */
  def dominantMutation(geneKind: GeneKind): Mutation = MutationImpl(geneKind, isDominant = true)

  private case class MutationImpl(override val geneKind: GeneKind, override val isDominant: Boolean) extends Mutation
}
