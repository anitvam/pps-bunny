package model

import model.AllGenes.{AlleleKind, FUR_COLOR, FUR_LENGTH}
import model.BunnyUtils.{getChildren, getCouples, getRandomBunny, getStandardBunny}
import org.scalatest.{FlatSpec, Matchers}

class TestBunny extends FlatSpec with Matchers {

  "Any Bunny" should "throw an Exception if its Genotype does not contain all kind of Genes" in {
    assertThrows[IllegalGenotypeException] {
      Genotype(Map( FUR_COLOR ->  Gene(AllGenes.FUR_COLOR, Allele(AlleleKind.WHITE_FUR), Allele(AlleleKind.BROWN_FUR)),
                    FUR_LENGTH -> Gene(AllGenes.FUR_LENGTH, Allele(AlleleKind.SHORT_FUR), Allele(AlleleKind.SHORT_FUR))))
    }
  }

  "Any StandardBunny" should "be instantiated without exceptions" in {
    noException should be thrownBy getStandardBunny
  }

  it should "have all kind of Genes" in {
    assert(getStandardBunny.genotype.genes.size == AllGenes.values.size)
  }

  it should "have a Phenotype with only base attributes" in {
    getStandardBunny.genotype.getPhenotype.attributes.foreach(entry =>
      assert(entry._2 == entry._1.base))
  }

  "Couples of bunnies " should "be generated from any group of Bunnies" in {
    val someBunnies = Seq.fill(9)(getStandardBunny)
    val someCouples = getCouples(someBunnies)
    assert(someCouples.size == someBunnies.size/2)
  }

  "Children of bunnies" should "be four" in {
    assert(getChildren(getRandomBunny, getRandomBunny).size == 4)
  }

  it should "be one for each cell of the Punnett square, for each Gene" in {
    val mom = getRandomBunny
    val dad = getRandomBunny
    val children = getChildren(mom, dad)

    AllGenes.values.foreach(genetype => {
      val grandmaMomAllele= mom.genotype.genes.get(genetype).get.momAllele.kind
      val granpaMomAllele = mom.genotype.genes.get(genetype).get.dadAllele.kind
      val grandmaDadAllele= dad.genotype.genes.get(genetype).get.momAllele.kind
      val granpaDadAllele= dad.genotype.genes.get(genetype).get.dadAllele.kind
      val childrenGenesOfType = children.map(b => b.genotype.genes.get(genetype).get)

      assert(childrenGenesOfType.contains(Gene(genetype, Allele(grandmaMomAllele), Allele(grandmaDadAllele))))
      assert(childrenGenesOfType.contains(Gene(genetype, Allele(grandmaMomAllele), Allele(granpaDadAllele))))
      assert(childrenGenesOfType.contains(Gene(genetype, Allele(granpaMomAllele), Allele(grandmaDadAllele))))
      assert(childrenGenesOfType.contains(Gene(genetype, Allele(granpaMomAllele), Allele(granpaDadAllele))))
    })
  }
}
