package plotly

import scala.language.implicitConversions
import scala.scalajs.js

import typings.plotlyJs.anon.PartialScatterLine
import typings.plotlyJs.mod.{Gauge as _, *}
import typings.plotlyJs.plotlyJsBooleans.`false`
import typings.plotlyJs.plotlyJsStrings.*

import convs.given
import core.*
import core.Gauge.MeterPerBeat as mpb

opaque type Trace[T, A] = A => List[Data]
object Trace:
  def apply[T, A](a: A)(using t: Trace[T, A]): List[Data] = t(a)

  given [M <: Gauge, A, B <: Datum](using Read[M, A, B], ValueOf[M]): Trace[M, Interval[A]] = (h, t) =>
    inline def head = Data
      .PartialPlotDataAutobinx()
      .setName(valueOf[M].label)
      .setHoverinfo(yPlussignname)
      .setLine(PartialScatterLine().setWidth(1))
      .setY((h :: t).map(Read[M, A, B]))
    head :: Nil

  given [A](using Read[mpb, A, Double], Read[Timestamp, A, Timestamp]): Trace[Box, History[A]] =
    (h, t) =>
      (h :: t).map: (h, t) =>
        Data
          .PartialBoxPlotData()
          .setType(PlotType.box)
          .setY((h :: t).map(Read[mpb, A, Double]))
          .setHoverinfo(y)
          .setName(Read[Timestamp, A, Timestamp](h).gmtDateString("fr-CA"))
          .setBoxpoints(`false`)
          .setLine(PartialScatterLine().setWidth(1))

  given [A]: Trace[EmptyTuple, A] = _ => Nil

  given [H, T <: Tuple, A](using h: Trace[H, A], t: Trace[T, A]): Trace[H *: T, A] = a => h(a) ::: t(a)

end Trace
