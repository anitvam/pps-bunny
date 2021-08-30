package model
import scala.util.Random

object BunnyUtils {
  def getChildren(mom: Bunny, dad:Bunny): Seq[Bunny] = ???

  def getCouples(bunnies: Seq[Bunny]): Seq[Tuple2[Bunny, Bunny]] = {
    val split = Random.shuffle(bunnies).splitAt(bunnies.size/2)
    split._1.zip(split._2)
  }

  def getStandardBunny(): Bunny =
    Bunny(Genotype(GeneKind.values.unsorted.map(gk => Gene(gk, Allele(gk.base), Allele(gk.base))).toList))
}

case class Bunny(genotype: Genotype,
                 mom: Option[Bunny] = Option.empty,
                 dad: Option[Bunny] = Option.empty,
                 age: Int = 0,
                 isAlive: Boolean = true){
  def getTree(gens: Int) = ???
  override def toString() = ???
}