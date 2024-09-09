package core

import org.scalajs.dom.*

type Mutation = (URL, Seq[HTMLElement])

type NonEmpty[+A] = (A, List[A])
extension [A](a: A) inline def single: NonEmpty[A] = a -> List.empty[A]

type Interval[A] = NonEmpty[A]
type History[A]  = NonEmpty[Interval[A]]

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
