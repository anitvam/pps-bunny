package model

class IllegalArgumentExceptionExplained(expl: String) extends IllegalArgumentException {
  print(expl + "\n")
}

class IllegalAlleleException(expl: String) extends IllegalArgumentExceptionExplained(expl)
class IllegalGenotypeException(expl: String) extends IllegalArgumentExceptionExplained(expl)