package common

import Generic.*

object Functional:
  private type Read[A, B]    = A => B
  private type Extract[A, B] = A => Option[B]
  private type Predicate[A]  = A => Boolean

  type FlatNorm[T <: Tuple] = Normalize[Flatten[T]]

  extension [A, B](f: Read[A, B])
    inline def |>[C](g: Read[B, C]): Read[A, C] = f andThen g
    inline def ??                               = f |> Option[B]

  extension [A](p: Predicate[A])
    inline def ||(q: Predicate[A]): Predicate[A]      = a => p(a) || q(a)
    inline def &&(q: Predicate[A]): Predicate[A]      = a => p(a) && q(a)
    inline def ?>[B](e: Extract[A, B]): Extract[A, B] = a => if p(a) then e(a) else None

  extension [A, B](e: Extract[A, B])
    inline def ~>[C](f: Extract[A, C]): Extract[A, (B, C)] = a =>
      for
        b <- e(a)
        c <- f(a)
      yield b -> c

  extension [A, B <: Tuple](e: Extract[A, B])
    inline def flatNorm: Extract[A, FlatNorm[B]] =
      @scala.annotation.tailrec
      def flatten(acc: Tuple = EmptyTuple): Tuple => Tuple =
        case EmptyTuple      => acc
        case (h: Tuple) *: t => flatten(acc)(h ++ t)
        case h *: t          => flatten(acc :* h)(t)

      def normalize: Tuple => Any =
        case EmptyTuple      => ()
        case h *: EmptyTuple => h
        case t               => t.take(t.size)

      e |> (_.map(flatten() |> normalize |> (_.asInstanceOf[FlatNorm[B]])))
end Functional
