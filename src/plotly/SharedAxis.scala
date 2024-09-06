package plotly

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import typings.plotlyJs.anon.PartialLayoutAxis
import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.mod.Data
import typings.plotlyJs.plotlyJsStrings.array

import core.metrics.*

trait SharedAxis[A, B <: Data]:
  def axis: PartialLayoutAxis
  def share(a: A): B => B

object SharedAxis:
  def apply[A, B <: Data](using s: SharedAxis[A, B]) = s

  given SharedAxis[Intervals, PartialPlotDataAutobinx] with
    def axis: PartialLayoutAxis = PartialLayoutAxis().setTickformat("~s").setTicksuffix("m").setTickmode(array)
    def share(a: Intervals): PartialPlotDataAutobinx => PartialPlotDataAutobinx =
      val (_, r) = a.foldLeft(0.0 -> List.empty[Meter]):
        case ((s, t), i) => (i.distance + s) -> (i.distance + s :: t)
      r.map(_.round).reverse
      _.setX(r.toJSArray)
