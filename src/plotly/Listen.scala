package plotly

import scala.scalajs.js

import typings.plotlyJs.mod.PlotlyHTMLElement

opaque type Listen[T, A] = Handler[A]
object Listen:
  def apply[T, A](p: PlotlyHTMLElement, a: A)(using l: Listen[T, A]) = l(p, a)

  def apply[T <: js.Any, D](p: PlotlyHTMLElement)(using e: Event.Aux[T, D])(f: D => Unit): Unit =
    inline def handler(d: js.Any): Unit = f(d.asInstanceOf[D])
    p.asInstanceOf[js.Dynamic].applyDynamic("on")(e.instance, handler)

  given [T <: js.Any, A, D](using Event.Aux[T, D], Registry[A, D]): Listen[T, A] =
    (p, b) => Listen[T, D](p)(b.handler(p, _))

  given [A]: Listen[EmptyTuple, A] = (_, _) => ()
  given [H, T <: Tuple, A](using ha: Listen[H, A], ta: Listen[T, A]): Listen[H *: T, A] = (p, a) =>
    ha(p, a); ta(p, a)

end Listen
