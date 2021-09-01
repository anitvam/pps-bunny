package model
import model.BunnyUtils.{generateBaseFirstBunny, generateRandomFirstBunny}
import model.world.Reproduction.{MAX_BUNNY_AGE, combineCouples, generateAllChildren, generateChildren, nextGenerationBunnies}
import model.genome.{Genes, StandardAllele, StandardGene}
import model.world.BunnyIdGenerator.getNextId
import model.world.{GenerationBunnies, StandardGenerationBunnies}
import org.scalatest.{FlatSpec, Matchers}

class TestBunny extends FlatSpec with Matchers {

  "Any FirstBunny" should "be instantiated without exceptions" in {
    noException should be thrownBy generateBaseFirstBunny
    noException should be thrownBy generateRandomFirstBunny
  }

  it should "have all kind of Genes" in {
    assert(generateBaseFirstBunny.genotype.genes.size == Genes.values.size)
  }

  it should "have a Phenotype with only base attributes" in {
    generateBaseFirstBunny.genotype.getPhenotype.visibleTraits.foreach(entry =>
      assert(entry._2 == entry._1.base))
  }

  "Couples of bunnies " should "be generated from any group of Bunnies" in {
    val someBunnies = Seq.fill(9)(generateRandomFirstBunny)
    val someCouples = combineCouples(someBunnies)
    assert(someCouples.size == someBunnies.size/2)
  }

  it should "contain every Bunny in the original group, if they are even" in {
    val someBunnies = Seq.fill(6)(generateRandomFirstBunny)
    val bunniesInCouples = combineCouples(someBunnies).flatMap(couple => List(couple._1, couple._2))
    someBunnies.foreach(b => assert(bunniesInCouples.contains(b)))
  }

  it should "contain every Bunny in the original group except from one, if they are odd" in {
    val someBunnies = Seq.fill(11)(generateRandomFirstBunny)
    val bunniesInCouples = combineCouples(someBunnies).flatMap(couple => List(couple._1, couple._2))
    assert(someBunnies.count(b => !bunniesInCouples.contains(b)) == 1)
    someBunnies.filter(b => bunniesInCouples.contains(b)).foreach(b => assert(bunniesInCouples.contains(b)))
  }

  val mom: FirstBunny = generateRandomFirstBunny
  val dad: FirstBunny = generateRandomFirstBunny
  val children: Seq[Bunny] = generateChildren(mom, dad)
  "Children of a couple" should "be 4" in {
    assert(children.size == 4)
  }

  it should "have the original bunnies as mom and dad " in {
    children.foreach(child => assert(child.mom.get == mom && child.dad.get == dad))
  }

  it should "be one for each cell of the Punnett square, for each Gene" in {
    Genes.values.foreach(gk => {
      val grandmaMomAllele= mom.genotype.genes(gk).momAllele.kind
      val grandpaMomAllele = mom.genotype.genes(gk).dadAllele.kind
      val grandmaDadAllele= dad.genotype.genes(gk).momAllele.kind
      val grandpaDadAllele= dad.genotype.genes(gk).dadAllele.kind
      val childrenGenesOfType = children.map(b => b.genotype.genes(gk))

      assert(childrenGenesOfType.contains(StandardGene(gk, StandardAllele(grandmaMomAllele), StandardAllele(grandmaDadAllele))))
      assert(childrenGenesOfType.contains(StandardGene(gk, StandardAllele(grandmaMomAllele), StandardAllele(grandpaDadAllele))))
      assert(childrenGenesOfType.contains(StandardGene(gk, StandardAllele(grandpaMomAllele), StandardAllele(grandmaDadAllele))))
      assert(childrenGenesOfType.contains(StandardGene(gk, StandardAllele(grandpaMomAllele), StandardAllele(grandpaDadAllele))))
    })
  }

  val bunniesNum = 7
  val bunnies: Seq[AliveBunny] = List.fill(bunniesNum)(generateRandomFirstBunny)
  "Children of all bunnies" should "be 4 for each couple" in {
    val children = generateAllChildren(bunnies)
    assert(children.size == (bunniesNum/2)*4)
  }

  val firstGenBunnies: GenerationBunnies = StandardGenerationBunnies(bunnies.map(getNextId -> _).toMap, Map.empty)
  val nextGenBunnies: GenerationBunnies = nextGenerationBunnies(firstGenBunnies)
  "Next generation bunnies" should "4 children for each couple and the previous bunnies" in {
    assert(nextGenBunnies.aliveBunnies.values.size == (bunniesNum/2)*4 + bunniesNum)
    assert(nextGenBunnies.deadBunnies.values.isEmpty)
  }

  it should "contain the next alive version of all the original bunnies" in {
    assert(bunnies.map(_.nextBunny).toSet.subsetOf(nextGenBunnies.aliveBunnies.values.toSet))
  }

  it should "contain as many dead bunnies after MAX_AGE generations as there where in the beginning" in {
    var nextGen: GenerationBunnies = firstGenBunnies
    for (_ <- 0 until MAX_BUNNY_AGE) {
      nextGen = nextGenerationBunnies(nextGen)
    }
    assert(nextGen.deadBunnies.values.size == bunniesNum)
  }
}
