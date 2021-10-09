package util

import scala.util.Random

object PimpScala {

  implicit class RichOption[A](option: Option[A]) {
    def --> (consumer: A => Unit): Unit = option.foreach(consumer)
    def ? : Boolean = option.isDefined
  }

  implicit class RichTuple2[A](tuple: (A, A)) {
    def toSeq: Seq[A] = Seq(tuple._1, tuple._2)
  }

  implicit class RichList[A](list: List[A]) {
    def -? (pred: A => Boolean): List[A] = list.filterNot(pred)
  }

  implicit class RichSeq[A](seq: Seq[A]){
    def shuffle: Seq[A] = Random.shuffle(seq)
    def random: A = seq.shuffle.head
  }
}
