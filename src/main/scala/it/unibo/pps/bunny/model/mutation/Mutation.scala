package it.unibo.pps.bunny.model.mutation

import it.unibo.pps.bunny.model.genome.Genes.GeneKind

/**
 * Model representation of the mutation
 */
trait Mutation {
  val geneKind: GeneKind
  val isDominant: Boolean
}

object Mutation {
  def apply(geneKind: GeneKind, isDominant: Boolean): Mutation = MutationImpl(geneKind, isDominant)
  private case class MutationImpl(override val geneKind: GeneKind, override val isDominant: Boolean) extends Mutation
}
