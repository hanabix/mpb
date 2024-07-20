import scala.scalajs.js

type Cast[A] = A & js.Dynamic

class Extract[A, B](f: A => Option[B]):
    final def unapply(a: A) = f(a)

trait MetersPerBeat[A] extends (A => Double):
  extension (a: A) inline def mpb = this(a)

trait HoverText[A] extends (A => String):
  extension (a: A) inline def text = this(a)  