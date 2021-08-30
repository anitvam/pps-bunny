package model

class IllegalArgumentExceptionExplained(expl: String) extends IllegalArgumentException {
  print(expl + "\n")
}

class IllegalAlleleException(expl: String) extends IllegalArgumentExceptionExplained(expl)