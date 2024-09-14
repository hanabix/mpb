package core

opaque type Read[T, A, B] = A => B
object Read:
  def apply[T, A, B](a: A)(using r: Read[T, A, B]): B = r(a)

  def apply[T, A, B](f: A => B): Read[T, A, B] = f
