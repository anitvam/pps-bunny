package model
import scala.util.Random

object BunnyUtils {
  val CHILDREN_NUMBER = 4

  def getStandardBunny: Bunny =
    Bunny(Genotype(AllGenes.values.unsorted.map(gk => (gk, Gene(gk, Allele(gk.base), Allele(gk.base)))).toMap))

  def getRandomBunny: Bunny =
    Bunny(
      Genotype(
        AllGenes.values.unsorted.map(gk => (gk, Gene(gk,
          Allele(List(gk.base, gk.mutated)(Random.nextInt(2))),
          Allele(List(gk.base, gk.mutated)(Random.nextInt(2)))))
        ).toMap
      )
    )

  def getCouples(bunnies: Seq[Bunny]): Seq[(Bunny, Bunny)] = {
    val split = Random.shuffle(bunnies).splitAt(bunnies.size/2)
    split._1.zip(split._2)
  }

  def getChildren(mom: Bunny, dad:Bunny): List[Bunny] = {
    var children = List.fill(CHILDREN_NUMBER)(Genotype(Map()))
    AllGenes.values.foreach(genetype => {
      val grandmaMomAllele= mom.genotype.genes.get(genetype).get.momAllele.kind
      val granpaMomAllele = mom.genotype.genes.get(genetype).get.dadAllele.kind
      val grandmaDadAllele= dad.genotype.genes.get(genetype).get.momAllele.kind
      val granpaDadAllele= dad.genotype.genes.get(genetype).get.dadAllele.kind

      val childrenGenes = Random.shuffle(
        List( Gene(genetype, Allele(grandmaMomAllele), Allele(grandmaDadAllele)),
              Gene(genetype, Allele(granpaMomAllele), Allele(grandmaDadAllele)),
              Gene(genetype, Allele(grandmaMomAllele), Allele(granpaDadAllele)),
              Gene(genetype, Allele(granpaMomAllele), Allele(granpaDadAllele))))
      children = (for (i <- 0 to 3) yield Genotype(children(i) + childrenGenes(i))).toList
    })
    children.map(Bunny(_))
  }
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
      .replace("_", " ") + "\n"
  }

  genotype.completed
}