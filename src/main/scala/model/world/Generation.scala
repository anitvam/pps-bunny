package model.world

import model.{AliveBunny, DeadBunny}

trait GenerationBunnies {
  val aliveBunnies: Map[Int, AliveBunny]
  val deadBunnies: Map[Int, DeadBunny]
}

case class StandardGenerationBunnies(aliveBunnies: Map[Int, AliveBunny], deadBunnies: Map[Int, DeadBunny]) extends GenerationBunnies

trait Generation {
  val bunnies: GenerationBunnies
}

case class StandardGeneration(bunnies: GenerationBunnies) extends Generation
