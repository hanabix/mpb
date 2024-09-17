package plotly

import scala.scalajs.js

import typings.plotlyJs.mod.PlotlyHTMLElement

opaque type Listen[T, A] = Handler[A]
object Listen:
  def apply[T, A](p: PlotlyHTMLElement, a: A)(using l: Listen[T, A]) = l(p, a)

  def apply[T <: js.Any](p: PlotlyHTMLElement)(using e: Event[T])(f: e.Data => Unit): Unit =
    inline def handler(d: js.Any): Unit = f(d.asInstanceOf[e.Data])
    p.asInstanceOf[js.Dynamic].applyDynamic("on")(e.instance, handler)

  given [T <: js.Any, A](using e: Event[T])(using Registry[A, e.Data]): Listen[T, A] =
    (p, b) => Listen[T](p)(b.handler(p, _))

  given [A]: Listen[EmptyTuple, A] = (_, _) => ()
  given [H, T <: Tuple, A](using ha: Listen[H, A], ta: Listen[T, A]): Listen[H *: T, A] = (p, a) =>
    ha(p, a); ta(p, a)

end Listen
