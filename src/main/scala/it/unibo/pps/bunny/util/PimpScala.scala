package it.unibo.pps.bunny.util

import scala.util.Random

object PimpScala {

  /** Implicit class that enrich Scala's Option implementation */
  implicit class RichOption[A](option: Option[A]) {

    /**
     * Method that applies a consumer to the Option's value, if non empty
     * @param consumer
     *   the consumer applied to the option
     */
    def -->(consumer: A => Unit): Unit = option.foreach(consumer)

    /** @return true if the Option is defined, otherwise false */
    def ? : Boolean = option.isDefined
  }

  /** Implicit class that enrich Scala's Tuple implementation */
  implicit class RichTuple2[A](tuple: (A, A)) {

    /** @return a [[Seq]] containing the tuple elements */
    def toSeq: Seq[A] = Seq(tuple._1, tuple._2)
  }

  /** Implicit class that enrich Scala's List implementation */
  implicit class RichList[A](list: List[A]) {

    /**
     * Remove elements from list on a given predicate
     * @param pred
     *   the predicate applied to remove elements
     * @return
     *   a list without the elements that satisfy the predicate
     */
    def -?(pred: A => Boolean): List[A] = list.filterNot(pred)
  }

  /** Implicit class that enrich Scala's Seq implementation */
  implicit class RichSeq[A](seq: Seq[A]) {

    /** @return a [[Seq]] with shuffled elements */
    def shuffle: Seq[A] = Random.shuffle(seq)

    /** @return a random element of the [[Seq]] */
    def random: A = seq.shuffle.head
  }

}
