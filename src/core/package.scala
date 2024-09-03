package core

import org.scalajs.dom.*

type Mutation = (URL, Seq[HTMLElement])

trait Page[A] extends (Mutation => Option[Mutation])
object Page:
  given any: Page[EmptyTuple] = Some(_)

  given tuple[H, T <: Tuple](using h: Page[H], t: Page[T]): Page[H *: T] =
    a => h(a).flatMap(t)

  def apply[A](using pa: Page[A]) = pa
end Page

type Injection[A] = (HTMLElement, A)
trait Inject[A] extends (Injection[A] => Unit):
  extension (a: A) inline def inject(r: HTMLElement) = this(r -> a)
end Inject
