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
  def apply(geneKind: GeneKind, isDominant: Boolean): Mutation = MutationImpl(geneKind, isDominant)

  private case class MutationImpl( override val geneKind: GeneKind,
                                  override val isDominant: Boolean ) extends Mutation
}

//object Mutations extends Enumeration {
//  type MutationKind = Value
//
//  val FUR_COLOR_MUTATION_DOMINANT: Mutation =  Mutation(Genes.FUR_COLOR, true)
//  val FUR_COLOR_MUTATION_RECESSIVE: Mutation =  Mutation(Genes.FUR_COLOR, false)
//  val FUR_LENGTH_MUTATION_DOMINANT: Mutation =  Mutation(Genes.FUR_LENGTH, true)
//  val FUR_LENGTH_MUTATION_RECESSIVE: Mutation =  Mutation(Genes.FUR_LENGTH, false)
//  val TEETH_MUTATION_DOMINANT: Mutation =  Mutation(Genes.TEETH, true)
//  val TEETH_MUTATION_RECESSIVE: Mutation =  Mutation(Genes.TEETH, false)
//  val EARS_MUTATION_DOMINANT: Mutation =  Mutation(Genes.EARS, true)
//  val EARS_MUTATION_RECESSIVE: Mutation =  Mutation(Genes.EARS, false)
//  val JUMP_MUTATION_DOMINANT: Mutation =  Mutation(Genes.JUMP, true)
//  val JUMP_MUTATION_RECESSIVE: Mutation =  Mutation(Genes.JUMP, false)
//} --> replace con "Anche secondo me questa Enumeration non serve (è un po' una ripetizione). Per indicare quale Allele
// è dominante e quale recessivo per ogni Gene, possiamo usare la funzione setAlleleDominance che trovi in Kinds, che
// assegna la dominanza un'unica volta. Sempre in Kinds potresti farti una funzione tipo getDominantAllele che dato un
// Gene ti ritorna l'Allele dominante, a questo punto ti basta usare l'enumeration Gene e questa funzione per ottenere
// un concetto simile all'enumeration mutations."