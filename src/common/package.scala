package common

import scala.scalajs.js

type Cast[A] = js.Dynamic & A

trait Predicate[A]:
  def unapply(a: A): Boolean
object Predicate:
  inline def apply[A](p: A => Boolean): Predicate[A] = new:
    def unapply(a: A) = p(a)

/** It is more readable than converting a function to extractor by [[scala.Function.unlift]].
  * @see
  *   [[scala.PartialFunction]]
  */
type Extract[A, B] = PartialFunction[A, B]
object Extract:
  inline def apply[A, B](e: A => Option[B]): Extract[A, B] = e.unlift
