package core


trait Page[A] extends (Mutation => Option[Mutation])

object Page:
  given any: Page[EmptyTuple] = Some(_)

  given tuple[H, T <: Tuple](using h: Page[H], t: Page[T]): Page[H *: T] =
    a => h(a).flatMap(t)

  def apply[A](using pa: Page[A]) = pa
end Page