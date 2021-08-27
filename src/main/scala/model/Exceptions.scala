package model

class IllegalAlleleException(expl: String) extends IllegalStateException{
  print(expl + "\n")
}