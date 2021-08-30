package model
import scala.util.Random

object BunnyUtils {
  def getStandardBunny: Bunny =
    Bunny(Genotype(AllGenes.values.unsorted.map(gk => (gk, Gene(gk, Allele(gk.base), Allele(gk.base)))).toMap))

  def getCouples(bunnies: Seq[Bunny]): Seq[(Bunny, Bunny)] = {
    val split = Random.shuffle(bunnies).splitAt(bunnies.size/2)
    split._1.zip(split._2)
  }

  def getChildren(mom: Bunny, dad:Bunny): Seq[Bunny] = ???
}

case class Bunny(genotype: Genotype,
                 mom: Option[Bunny] = Option.empty,
                 dad: Option[Bunny] = Option.empty,
                 age: Int = 0,
                 isAlive: Boolean = true){

  def getTree(gens: Int): Nothing = ???

  override def toString: String = {
    super.toString + "\n" + genotype.genes
      .map(g => "\t" + g._1 + ": "+ g._2.getAttribute.toString.toLowerCase + " (" + g._2.getLetters + ")")
      .reduce(_ + "\n" + _)
      .replace("_", " ")
  }
}