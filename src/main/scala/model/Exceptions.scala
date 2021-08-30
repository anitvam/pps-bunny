package model

class IllegalAlleleException(expl: String) extends IllegalArgumentException{
  print(expl + "\n")
}

class IllegalGenotypeException(expl: String) extends IllegalArgumentException{
  print(expl + "\n")
}