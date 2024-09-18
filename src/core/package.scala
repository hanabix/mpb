package core

import org.scalajs.dom.*

type Mutation = (URL, Seq[HTMLElement])

trait Inject[A]:
  extension (a: A) def inject(e: HTMLElement): Unit

type NonEmpty[A] = (A, List[A])
type Interval[A] = NonEmpty[A]
type History[A]  = NonEmpty[Interval[A]]

sealed trait Distance
sealed trait Duration
sealed trait Box

