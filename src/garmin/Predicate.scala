package garmin

import core.Read

opaque type Predicate[T, A] = A => Boolean
object Predicate:
  def apply[T, A](f: A => Boolean): Predicate[T, A]        = f
  def apply[T, A](a: A)(using p: Predicate[T, A]): Boolean = p(a)

  given [A]: Predicate[EmptyTuple, A]                                                          = _ => true
  given [H, T <: Tuple, A](using h: Predicate[H, A], t: Predicate[T, A]): Predicate[H *: T, A] = a => h(a) && t(a)

  given [T, A, B](using Read[T, A, B]): Predicate[T, A] = a =>
    try
      Read[T, A, B](a)
      true
    catch case _ => false
end Predicate
