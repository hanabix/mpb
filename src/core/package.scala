package core

import org.scalajs.dom.*

type Mutation = (URL, Seq[HTMLElement])

type NonEmpty[+A] = (A, List[A])
extension [A](a: A) inline def single: NonEmpty[A] = a -> List.empty[A]

type Interval[A] = NonEmpty[A]
type History[A]  = NonEmpty[Interval[A]]

sealed trait Distance
sealed trait Duration
sealed trait Timestamp
sealed trait Box

type Injection[A] = (HTMLElement, A)
trait Inject[A] extends (Injection[A] => Unit):
  extension (a: A) inline def inject(r: HTMLElement) = this(r -> a)
end Inject
