package model

import model.Genes.GeneKind

class ExplainedException(exp: String) extends Exception {
  print(exp + "\n")
}

class InconsistentAlleleException
  extends ExplainedException("Gene initialization EXCEPTION: one of the Alleles (momAllele or dadAllele) " +
    "has a kind which is not suitable with the kind of the Gene!")

class IllegalGenotypeBuildException
  extends ExplainedException("Genotype EXCEPTION: the Genotype must contain all the Genes")

class InconsistentGenotypeException(genes: Map[GeneKind, Gene])
  extends ExplainedException("Genotype EXCEPTION: the GeneType in the key must be coherent " +
    "with the kind in the corresponding Gene\n" + genes.filter(g => g._1 == g._2.kind))

class MultipleDominanceAssignmentException
  extends ExplainedException ("Dominance assignment EXCEPTION: the dominance is already defined for this allele and its corresponding Gene")

class InconsistentMutatedAlleleException
  extends ExplainedException("mutated allele EXCEPTION: the allele has isMutated as true, but the kind of the allele is the base one")