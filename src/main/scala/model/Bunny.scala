package model

import scala.util.Random

case class Bunny(genotype: Genotype,
                 mom: Option[Bunny] = Option.empty,
                 dad: Option[Bunny] = Option.empty,
                 age: Int = 0,
                 isAlive: Boolean = true){

  def getTree(gens: Int): Nothing = ???

  override def toString: String = {
    super.toString + "\n" + genotype.genes
      .map(g => "\t" + g._1 + ": "+ g._2.getVisibleTrait.toString.toLowerCase + " (" + g._2.getLetters + ")")
      .reduce(_ + "\n" + _)
      .replace("_", " ") + "\n"
  }

  genotype.completed()
}

object BunnyUtils {
  val CHILDREN_NUMBER = 4

  def getStandardBunny: Bunny =
    Bunny(Genotype(Genes.values.unsorted.map(gk => (gk, Gene(gk, Allele(gk.base), Allele(gk.base)))).toMap))

  def getRandomBunny: Bunny =
    Bunny(
      Genotype(
        Genes.values.unsorted.map(gk => {
          (gk, Gene(gk, Allele(List(gk.base, gk.mutated)(Random.nextInt(2))),
                        Allele(List(gk.base, gk.mutated)(Random.nextInt(2)))))
        }).toMap
      )
    )

  def getCouples(bunnies: Seq[Bunny]): Seq[(Bunny, Bunny)] = {
    val split = Random.shuffle(bunnies).splitAt(bunnies.size/2)
    split._1.zip(split._2)
  }

  def getChildren(mom: Bunny, dad:Bunny): Seq[Bunny] = {
    var children = List.fill(CHILDREN_NUMBER)(Genotype(Map()))
    Genes.values.foreach(genekind => {
      val grandmaMomAllele= mom.genotype.genes(genekind).momAllele
      val grandpaMomAllele = mom.genotype.genes(genekind).dadAllele
      val grandmaDadAllele= dad.genotype.genes(genekind).momAllele
      val grandpaDadAllele= dad.genotype.genes(genekind).dadAllele

      val childrenGenes = Random.shuffle(
        List( Gene(genekind, grandmaMomAllele, grandmaDadAllele),
          Gene(genekind, grandpaMomAllele, grandmaDadAllele),
          Gene(genekind, grandmaMomAllele, grandpaDadAllele),
          Gene(genekind, grandpaMomAllele, grandpaDadAllele)))
      children = (for (i <- 0 until CHILDREN_NUMBER) yield Genotype(children(i) + childrenGenes(i))).toList
    })
    children.map(Bunny(_))
  }

  def getAllChildren(bunnies: Seq[Bunny]): Seq[Bunny] =
    getCouples(bunnies).flatMap(couple => getChildren(couple._1, couple._2))

  def getNextGenerationBunnies(bunnies: Seq[Bunny]): Seq[Bunny] =
    getAllChildren(bunnies) ++ bunnies
}