package model
import com.sun.tools.javac.util.Pair
import model.AlleleKind.AlleleKind
import model.GeneKind.GeneKind

object BunnyUtils {
  def getChildren(mom: Bunny, dad:Bunny): Seq[Bunny] = ???
  def getCouples(bunnies: Seq[Bunny]): Pair[Bunny, Bunny] = ???
}

case class Bunny(mom: Option[Bunny],
                 dad: Option[Bunny],
                 age: Int,
                 isAlive: Boolean,
                 genotype: Genotype){
  def getTree(gens: Int) = ???
}

case class Genotype(genes: List[Gene])
case class Allele(kind: AlleleKind, isDominant: Option[Boolean], isMutated: Option[Boolean])
case class Gene(kind: GeneKind, momAllele: Allele, dadAllele: Allele){
  def getPhenotype(): Phenotype = ???
}

case class Phenotype(traits: Seq[AlleleKind])