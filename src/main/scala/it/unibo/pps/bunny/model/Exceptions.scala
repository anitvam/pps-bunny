package it.unibo.pps.bunny.model

import it.unibo.pps.bunny.model.genome.Gene
import it.unibo.pps.bunny.model.genome.Genes.GeneKind
import it.unibo.pps.bunny.util.ExplainedException

class InconsistentAlleleException
    extends ExplainedException(
      "Gene initialization EXCEPTION: one of the Alleles (momAllele or dadAllele) " +
        "has a kind which is not suitable with the kind of the Gene!"
    )

class IllegalGenotypeBuildException
    extends ExplainedException("Genotype EXCEPTION: the Genotype must contain all the Genes")

class InconsistentGenotypeException(genes: Map[GeneKind, Gene])
    extends ExplainedException(
      "Genotype EXCEPTION: the GeneType in the key must be coherent " +
        "with the kind in the corresponding Gene\n" + genes.filter(g => g._1 == g._2.kind)
    )

class MultipleDominanceAssignmentException
    extends ExplainedException(
      "Dominance assignment EXCEPTION: the dominance is already defined for this allele and its corresponding Gene"
    )

class InconsistentMutatedAlleleException
    extends ExplainedException(
      "Mutated allele EXCEPTION: the allele has isMutated as true, but the kind of the allele is the base one"
    )

class CoupleGendersException
    extends ExplainedException(
      "Couple genders EXCEPTION: the bunnies of a couple must be one Male and one Female"
    )

class InvalidFoodFactor extends ExplainedException("The specified Food Factor is invalid for this operation")

class HistoryBunnyUpdateException
    extends ExplainedException(
      "" +
        "History Bunny Update EXCEPTION: variables of an HistoryBunny cannot be updated ad it is an immutable bunny"
    )
