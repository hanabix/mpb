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
sealed trait Timestamp
sealed trait Box

opaque type Aka[A] = String
object Aka:
  def of[A](using l: Aka[A]): String = l
  def apply[A](s: String): Aka[A]    = s

sealed trait Gauge
object Gauge:
  sealed trait MeterPerBeat  extends Gauge
  sealed trait BeatPerMinute extends Gauge
  sealed trait StepPerMinute extends Gauge
  sealed trait Pace          extends Gauge
