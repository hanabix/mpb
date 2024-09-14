package plotly

import scala.language.implicitConversions
import scala.scalajs.js

import typings.plotlyJs.anon.PartialLayout
import typings.plotlyJs.anon.PartialLayoutAxis
import typings.plotlyJs.anon.PartialLegendBgcolor
import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.mod.Data
import typings.plotlyJs.plotlyJsStrings.legendonly

import convs.given

trait CorrelatePlot[A]:
  extension (a: A) def plot(f: (js.Array[Data], PartialLayout) => Unit): Int => Unit
object CorrelatePlot:

  def apply[X, Y, Y2 <: NonEmptyTuple, A](using
    ColorPalette,
    PartialLayout,
    PartialLegendBgcolor,
    Title[A],
    Share[X, A],
    Trace[Y *: Y2, A],
    Axis[X *: Y *: Y2]
  ): CorrelatePlot[A] = new:
    extension (a: A)
      def plot(f: (js.Array[Data], PartialLayout) => Unit): Int => Unit =
        val common      = Share[X, A](a)
        val p :: ss     = Trace[Y *: Y2, A](a): @unchecked
        val x :: ys     = Axis[X *: Y *: Y2]: @unchecked
        val title       = Title[A](a)
        val primary     = p |> common |> yAxis("y") |> legend(false)
        val secondaries = ss.map(_ |> common |> yAxis("y2"))
        val layout = summon[PartialLayout]
          .setLegend(summon[PartialLegendBgcolor])
          .setTitle(title)
          .setXaxis(x)
          .setYaxis(ys.head.setColorIndex(0))

        i =>
          val elected = secondaries.zipWithIndex.map:
            case (d, n) if n + 1 == i => d |> visible(true)
            case (d, n)               => d |> copy("name", "x", "yaxis") |> visible(legendonly)
          f(primary :: elected, layout.setYaxis2(ys(i).setColorIndex(i)))
  end apply

  inline def legend(value: Boolean): Mutate               = _.asInstanceOf[PartialPlotDataAutobinx].setShowlegend(value)
  inline def visible(value: Boolean | legendonly): Mutate = _.asInstanceOf[PartialPlotDataAutobinx].setVisible(value)
  inline def yAxis(value: String): Mutate                 = _.asInstanceOf[PartialPlotDataAutobinx].setYaxis(value)
  inline def copy(attrs: String*): Mutate                 = _.copy(attrs*)

  extension [A](a: A) def |>[B](f: A => B): B = f(a)

  extension [A <: js.Object](a: A)
    def copy(attrs: String*): A =
      val d = a.asInstanceOf[js.Dynamic]
      val c = js.Dynamic.literal()
      for k <- attrs do c.updateDynamic(k)(d.selectDynamic(k).asInstanceOf[js.Any])
      c.asInstanceOf[A]

  type Mutate = Data => Data

end CorrelatePlot
