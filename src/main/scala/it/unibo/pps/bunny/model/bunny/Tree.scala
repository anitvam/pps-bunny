package it.unibo.pps.bunny.model.bunny

import it.unibo.pps.bunny.engine.SimulationConstants.MAX_GENEALOGICAL_TREE_GENERATIONS

/**
 * Represents a Tree.
 */
sealed trait BinaryTree[A] {
  val elem: A
  val generations: Int
  override def toString: String = elem.toString
}

/**
 * Represents a Leaf of the Tree, with just an element.
 * @param elem
 *   the element in the leaf
 * @tparam A
 *   the type of the element
 */
case class Leaf[A](elem: A) extends BinaryTree[A] {
  override val generations: Int = 1
}

/**
 * Represents a Node of the Tree, with an element and two branches
 * @param elem
 *   the element in the node
 * @param momTree
 *   one of the branches
 * @param dadTree
 *   the other branch
 * @tparam A
 *   the type of the element
 */
case class Node[A](override val elem: A, momTree: BinaryTree[A], dadTree: BinaryTree[A]) extends BinaryTree[A] {
  override val generations: Int = Math.max(momTree.generations, dadTree.generations) + 1
}

object Tree {

  /**
   * @param generations
   *   the number of older generations to retrieve
   * @param bunny
   *   the subject bunny
   * @return
   *   the genealogical tree of the bunny for the specified generations
   */
  def generateTree(generations: Int, bunny: Bunny): BinaryTree[Bunny] = {
    if (generations == 1 || bunny.mom.isEmpty) Leaf(bunny)
    else Node(bunny, generateTree(generations - 1, bunny.mom.get), generateTree(generations - 1, bunny.dad.get))
  }

  /**
   * @param bunny
   *   the subject bunny
   * @return
   *   the max number of generations present in the tree (the real one or maximum if there are more)
   */
  def actualGenerations(bunny: Bunny): Int = generateTree(MAX_GENEALOGICAL_TREE_GENERATIONS, bunny).generations
}
