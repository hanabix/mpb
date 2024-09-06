package plotly

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import typings.plotlyJs.anon.PartialLayout
import typings.plotlyJs.anon.PartialLegendBgcolor
import typings.plotlyJs.mod.Data
import typings.plotlyJs.plotlyJsBooleans.`false`


trait Select[A]:
  def data(i: Int): DataArrayFrom[A]
  def layout(i: Int): Layout[A]
object Select:
  def apply[A](using s: Select[A]) = s

  given select[A, B <: Data](
    using SharedAxis[A, B],
    Trace["mpb", A, B],
    Trace["bpm", A, B],
    Trace["spm", A, B],
    Trace["/km", A, B],
    Layout[A],
    ColorPalette[Common]
  ): Select[A] with
    private val secondaries = List(
        Trace["bpm", A, B],
        Trace["spm", A, B],
        Trace["/km", A, B]
    )
    def layout(i: Int): Layout[A] = a =>
      a.layout
        .setShowlegend(true)
        .setLegend(PartialLegendBgcolor().setX(1.1).setY(0.5).setItemclick(`false`).setItemdoubleclick(`false`))
        .setXaxis(SharedAxis[A, B].axis)
        .setYaxis(Trace["mpb", A, B].y(0.color))
        .setYaxis2(secondaries(i - 1).y(i.color))

    def data(i: Int): DataArrayFrom[A] = a =>
      val h = Trace["mpb", A, B].data(a)
      val t = secondaries.zipWithIndex.map:
        case (t, n) if n + 1 == i => t.data(a)
        case (t, _)               => t.dummy(a)
      (h :: t).map(SharedAxis[A, B].share(a)).toJSArray
  end select

end Select
