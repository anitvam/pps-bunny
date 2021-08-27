package model
import com.sun.tools.javac.util.Pair
import model.AlleleKind.AlleleKind
import model.GeneKind.GeneKind

object BunnyUtils {
  def getChildren(mom: Bunny, dad:Bunny): Seq[Bunny] = ???
  def getCouples(bunnies: Seq[Bunny]): Pair[Bunny, Bunny] = ???
  def getStandardBunny(): Bunny = ???
}

case class Bunny(mom: Option[Bunny] = Option.empty,
                 dad: Option[Bunny] = Option.empty,
                 age: Int = 0,
                 isAlive: Boolean = true,
                 genotype: Genotype){
  def getTree(gens: Int) = ???
}

case class Genotype(genes: List[Gene])

case class Allele(kind: AlleleKind,
                  isDominant: Option[Boolean] = Option.empty,
                  isMutated: Option[Boolean] = Option.empty)

case class Gene(kind: GeneKind,
                momAllele: Allele,
                dadAllele: Allele,
                letters: Option[String] = Option.empty) {
  def getPhenotype(): Phenotype = ???

  if (!(momAllele.kind == kind.mutated || momAllele.kind == kind.base) && (dadAllele.kind == kind.mutated || dadAllele.kind == kind.base))
    throw new IllegalAlleleException("Gene initialization: one of the Alleles (momAllele or dadAllele) has a kind which is not suitable with the kind of the Gene!")
}

case class Phenotype(traits: Seq[AlleleKind])