package core

opaque type Page[A] = PartialFunction[Mutation, Unit]
object Page:
  def apply[A](pf: PartialFunction[Mutation, Unit]): Page[A] = pf
  def apply[A](m: Mutation)(using pa: Page[A]): Unit         = pa(m)

  given Page[EmptyTuple]                                            = { case _ => }
  given [H, T <: Tuple](using h: Page[H], t: Page[T]): Page[H *: T] = h orElse t
end Page
