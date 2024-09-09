package plotly

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import typings.plotlyJs.anon.PartialLayoutAxis
import typings.plotlyJs.anon.PartialPlotDataAutobinx as LineData
import typings.plotlyJs.anon.PartialScatterLine
import typings.plotlyJs.mod.Data
import typings.plotlyJs.mod.Datum
import typings.plotlyJs.plotlyJsStrings.legendonly
import typings.plotlyJs.plotlyJsStrings.reversed
import typings.plotlyJs.plotlyJsStrings.right
import typings.plotlyJs.plotlyJsStrings.y2
import typings.plotlyJs.plotlyJsStrings.yPlussignname

import core.metrics.*

trait Trace[T <: String: ValueOf, A, B <: Data]:
  val name = valueOf[T]
  def data(a: A): B
  def dummy(a: A): B
  def y(color: String): PartialLayoutAxis
object Trace:
  def apply[T <: String: ValueOf, A, B <: Data](using s: Trace[T, A, B]) = s

  given primary(
    using Performance[Interval]
  ): Trace["mpb", Intervals, LineData] with
    def y(color: String) = PartialLayoutAxis()
      .setColor(color)
      .setTickformat(".2f")
      .setOverlaying(y2)
      .setTickmodeSync

    def data(a: Intervals) = Data
      .PartialPlotDataAutobinx()
      .setName(name)
      .setLine(PartialScatterLine().setWidth(1))
      .setHoverinfo(yPlussignname)
      .setYaxis("y")
      .setY(a.map(_.mpb).toJSArray)

    def dummy(a: Intervals) = throw UnsupportedOperationException()
  end primary

  given bpm: Trace["bpm", Intervals, LineData] = new Secondary(_.map(_.averageHR.round))
  given spm: Trace["spm", Intervals, LineData] = new Secondary(_.map(_.averageRunCadence.round))
  given pace: Trace["/km", Intervals, LineData] with
    private val s           = new Secondary["/km", Intervals](_.map(_.`/km`))
    def y(color: String)    = s.y(color).setTickformat("%M:%S").setAutorange(reversed)
    def data(a: Intervals)  = s.data(a)
    def dummy(a: Intervals) = s.dummy(a)
  end pace

  private class Secondary[T <: String: ValueOf, A](f: A => Seq[Datum]) extends Trace[T, A, LineData]:
    def y(color: String) = PartialLayoutAxis()
      .setColor(color)
      .setSide(right)

    def data(a: A) = dummy(a).setVisible(true).setY(f(a).toJSArray)

    def dummy(a: A) = Data
      .PartialPlotDataAutobinx()
      .setVisible(legendonly)
      .setName(name)
      .setLine(PartialScatterLine().setWidth(1))
      .setHoverinfo(yPlussignname)
      .setYaxis("y2")
  end Secondary

  extension (d: Double) private inline def round = scala.scalajs.js.Math.round(d)
  extension (i: Interval)
    private inline def `/km` =
      val s = math.round(i.pace * 1000).intValue
      new js.Date(0, 0, 0, s / (60 * 60), s / 60, s % 60)

  extension (a: PartialLayoutAxis)
    private inline def setTickmodeSync: PartialLayoutAxis =
      a.asInstanceOf[js.Dynamic].tickmode = "sync"
      a
end Trace
