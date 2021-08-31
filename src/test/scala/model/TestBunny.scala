package model

import model.Genes.{Alleles, FUR_COLOR, FUR_LENGTH}
import model.BunnyUtils.{getAllChildren, getChildren, getCouples, getNextGenerationBunnies, getRandomBunny, getStandardBunny}
import org.scalatest.{FlatSpec, Matchers}

class TestBunny extends FlatSpec with Matchers {
  "Any Bunny" should "throw an Exception if its Genotype does not contain all kind of Genes" in {
    assertThrows[IllegalGenotypeException] {
      Bunny(Genotype(Map( FUR_COLOR ->  Gene(Genes.FUR_COLOR, Allele(Alleles.WHITE_FUR), Allele(Alleles.BROWN_FUR)),
                          FUR_LENGTH -> Gene(Genes.FUR_LENGTH, Allele(Alleles.SHORT_FUR), Allele(Alleles.SHORT_FUR)))))
    }
  }

  "Any StandardBunny" should "be instantiated without exceptions" in {
    noException should be thrownBy getStandardBunny
  }

  it should "have all kind of Genes" in {
    assert(getStandardBunny.genotype.genes.size == Genes.values.size)
  }

  it should "have a Phenotype with only base attributes" in {
    getStandardBunny.genotype.getPhenotype.visibleTraits.foreach(entry =>
      assert(entry._2 == entry._1.base))
  }

  "Couples of bunnies " should "be generated from any group of Bunnies" in {
    val someBunnies = Seq.fill(9)(getStandardBunny)
    val someCouples = getCouples(someBunnies)
    assert(someCouples.size == someBunnies.size/2)
  }

  "Children of a couple" should "be 4" in {
    assert(getChildren(getRandomBunny, getRandomBunny).size == 4)
  }

  it should "be one for each cell of the Punnett square, for each Gene" in {
    val mom = getRandomBunny
    val dad = getRandomBunny
    val children = getChildren(mom, dad)

    Genes.values.foreach(genekind => {
      val grandmaMomAllele= mom.genotype.genes(genekind).momAllele.kind
      val grandpaMomAllele = mom.genotype.genes(genekind).dadAllele.kind
      val grandmaDadAllele= dad.genotype.genes(genekind).momAllele.kind
      val grandpaDadAllele= dad.genotype.genes(genekind).dadAllele.kind
      val childrenGenesOfType = children.map(b => b.genotype.genes(genekind))

      assert(childrenGenesOfType.contains(Gene(genekind, Allele(grandmaMomAllele), Allele(grandmaDadAllele))))
      assert(childrenGenesOfType.contains(Gene(genekind, Allele(grandmaMomAllele), Allele(grandpaDadAllele))))
      assert(childrenGenesOfType.contains(Gene(genekind, Allele(grandpaMomAllele), Allele(grandmaDadAllele))))
      assert(childrenGenesOfType.contains(Gene(genekind, Allele(grandpaMomAllele), Allele(grandpaDadAllele))))
    })
  }

  val bunniesNum = 7
  val bunnies: Seq[Bunny] = List.fill(bunniesNum)(getRandomBunny)
  "Children of all bunnies" should "be 4 for each couple" in {
    val children = getAllChildren(bunnies)
    assert(children.size == (bunniesNum/2)*4)
  }

  val nextGenBunnies: Seq[Bunny] = getNextGenerationBunnies(bunnies)
  "Next generation bunnies" should "4 children for each couple and the previous bunnies" in {
    assert(nextGenBunnies.size == (bunniesNum/2)*4 + bunniesNum)
  }

  it should "contain all the original bunnies" in {
    assert(nextGenBunnies.containsSlice(bunnies))
  }
}
