package core

import org.scalajs.dom.*

type Mutation = (URL, Seq[HTMLElement])

trait Inject[A]:
  extension (a: A) def inject(e: HTMLElement): Unit

object NonEmpty:
  opaque type Ref[A] = Seq[A]
  object Ref:
    def unapply[A](sa: Seq[A]): Option[Ref[A]] = ref(sa)

  def ref[A](sa: Seq[A]): Option[Ref[A]] = if sa.isEmpty then None else Some(sa)

  extension [A](ref: Ref[A])
    def deref: Seq[A] = ref

type Interval[A] = NonEmpty.Ref[A]
type History[A]  = NonEmpty.Ref[Interval[A]]

sealed trait Distance
sealed trait Duration
sealed trait Timestamp
sealed trait Box

sealed trait Gauge
object Gauge:
  sealed trait MeterPerBeat  extends Gauge
  sealed trait BeatPerMinute extends Gauge
  sealed trait StepPerMinute extends Gauge
  sealed trait Pace          extends Gauge

opaque type Read[T, A, B] = A => B
object Read:
  def apply[T, A, B](a: A)(using r: Read[T, A, B]): B = r(a)
  def apply[T, A, B](f: A => B): Read[T, A, B]        = f
  given [T, A, B]: Conversion[A => B, Read[T, A, B]]  = identity

opaque type Aka[A] = String
object Aka:
  def apply[A](using l: Aka[A]): String = l
  given [A]: Conversion[String, Aka[A]] = identity
