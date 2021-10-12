package it.unibo.pps.bunny.model.bunny

import it.unibo.pps.bunny.model.genome.Genes.GeneKind

/**
 * Model representation of the mutation
 */
trait Mutation {
  val geneKind: GeneKind
  val isDominant: Boolean
}

object Mutation {

  /**
   * Creates a recessive mutation
   * @param geneKind
   *   the gene mutated
   */
  def recessiveMutation(geneKind: GeneKind): Mutation = MutationImpl(geneKind, isDominant = false)

  /**
   * Creates a dominant mutation
   * @param geneKind
   *   the gene mutated
   */
  def dominantMutation(geneKind: GeneKind): Mutation = MutationImpl(geneKind, isDominant = true)

  private case class MutationImpl(override val geneKind: GeneKind, override val isDominant: Boolean) extends Mutation
}
