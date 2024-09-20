package core

opaque type Proceed[A] = PartialFunction[Mutation, Unit]
object Proceed:
  def apply[A](pf: PartialFunction[Mutation, Unit]): Proceed[A] = pf
  def apply[A](m: Mutation)(using pa: Proceed[A]): Unit         = pa(m)

  given Proceed[EmptyTuple]                                                  = { case _ => }
  given [H, T <: Tuple](using h: Proceed[H], t: Proceed[T]): Proceed[H *: T] = h orElse t
end Proceed
