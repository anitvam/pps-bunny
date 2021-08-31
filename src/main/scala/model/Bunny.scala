package model

import scala.util.Random

sealed trait Bunny{
  val genotype: CompletedGenotype
  val mom: Option[Bunny]
  val dad: Option[Bunny]
  val age: Int
  val isAlive: Boolean

  override def toString: String = {
    super.toString + "\n" + genotype.genes
      .map(g => "\t" + g._1 + ": "+ g._2.getVisibleTrait.toString.toLowerCase + " (" + g._2.getLetters + ")")
      .reduce(_ + "\n" + _)
      .replace("_", " ") + "\n"
  }
}

case class AliveBunny(genotype: CompletedGenotype, mom:Option[Bunny], dad:Option[Bunny], age:Int) extends Bunny{
  override val isAlive: Boolean = true
}

case class DeadBunny(genotype: CompletedGenotype, mom:Option[Bunny], dad:Option[Bunny], age:Int) extends Bunny{
  override val isAlive: Boolean = false
}

abstract class InitBunny() extends Bunny{
  override val age: Int = 0
  override val isAlive: Boolean = true
}

case class ChildBunny(genotype: CompletedGenotype, mom:Option[Bunny], dad:Option[Bunny]) extends InitBunny

case class FirstBunny(genotype: CompletedGenotype) extends InitBunny {
  override val mom: Option[Bunny] = Option.empty
  override val dad: Option[Bunny] = Option.empty
}

object BunnyUtils {
  val CHILDREN_NUMBER = 4

  def getBaseFirstBunny: FirstBunny =
    FirstBunny(CompletedGenotype(Genes.values.unsorted.map(gk =>
      (gk, StandardGene(gk, StandardAllele(gk.base), StandardAllele(gk.base)))).toMap))

  def getRandomFirstBunny: FirstBunny = {
    FirstBunny(
      CompletedGenotype(
        Genes.values.unsorted.map(gk => {
          (gk, StandardGene(gk, StandardAllele(List(gk.base, gk.mutated)(Random.nextInt(2))),
                                StandardAllele(List(gk.base, gk.mutated)(Random.nextInt(2)))))
        }).toMap
      )
    )
  }

  def getCouples(bunnies: Seq[Bunny]): Seq[(Bunny, Bunny)] = {
    val split = Random.shuffle(bunnies).splitAt(bunnies.size/2)
    split._1.zip(split._2)
  }

  def getChildren(mom: Bunny, dad:Bunny): Seq[Bunny] = {
    var children = List.fill(CHILDREN_NUMBER)(PartialGenotype(Map()))
    Genes.values.foreach(gk => {
      val grandmaMomAllele= mom.genotype.genes(gk).momAllele
      val grandpaMomAllele = mom.genotype.genes(gk).dadAllele
      val grandmaDadAllele= dad.genotype.genes(gk).momAllele
      val grandpaDadAllele= dad.genotype.genes(gk).dadAllele

      val childrenGenes = Random.shuffle(
        List( StandardGene(gk, grandmaMomAllele, grandmaDadAllele),
              StandardGene(gk, grandpaMomAllele, grandmaDadAllele),
              StandardGene(gk, grandmaMomAllele, grandpaDadAllele),
              StandardGene(gk, grandpaMomAllele, grandpaDadAllele)))
      children = (for (i <- 0 until CHILDREN_NUMBER) yield PartialGenotype(children(i) + childrenGenes(i))).toList
    })
    children.map(child => ChildBunny(genotype = CompletedGenotype(child.genes), mom = Option(mom), dad = Option(dad)))
  }

  def getAllChildren(bunnies: Seq[Bunny]): Seq[Bunny] =
    getCouples(bunnies).flatMap(couple => getChildren(couple._1, couple._2))

  def getNextGenerationBunnies(bunnies: Seq[Bunny]): Seq[Bunny] =
    getAllChildren(bunnies) ++ bunnies

  def getTree(generations: Int, bunny: Bunny) = ???
}