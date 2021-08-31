package model

class IllegalArgumentExceptionExplained(exp: String) extends IllegalArgumentException {
  print(exp + "\n")
}

class IllegalAlleleException(exp: String) extends IllegalArgumentExceptionExplained(exp)
class IllegalGenotypeException(exp: String) extends IllegalArgumentExceptionExplained(exp)