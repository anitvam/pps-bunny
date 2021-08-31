package model

import model.Alleles.AlleleKind
import model.Genes.{GeneKind}
import model.GenesUtils.getGeneKind

class ExplainedException(exp: String) extends Exception {
  print(exp + "\n")
}

class IllegalAlleleArgumentException
  extends ExplainedException("Gene initialization EXCEPTION: one of the Alleles (momAllele or dadAllele) " +
    "has a kind which is not suitable with the kind of the Gene!")

class IllegalGenotypeCompletedException
  extends ExplainedException("Genotype EXCEPTION: the Genotype must contain all the Genes")

class GenotypeInconsistencyException(genes: Map[GeneKind, Gene])
  extends ExplainedException("Genotype EXCEPTION: the GeneType in the key must be coherent " +
    "with the kind in the corresponding Gene\n" + genes.filter(g => g._1 == g._2.kind))

class MultipleDominanceAssignmentException(alleleKind: AlleleKind)
  extends ExplainedException ("Dominance assignment EXCEPTION: the dominance is already defined for this allele (" +
    alleleKind + ") and its corresponding Gene ("+ getGeneKind(alleleKind) +")")