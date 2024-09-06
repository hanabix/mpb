package plotly

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import typings.plotlyJs.anon.PartialScatterLine
import typings.plotlyJs.mod.Data
import typings.plotlyJs.mod.PlotType
import typings.plotlyJs.plotlyJsBooleans.`false`
import typings.plotlyJs.plotlyJsStrings.legendonly
import typings.plotlyJs.plotlyJsStrings.y
import typings.plotlyJs.plotlyJsStrings.yPlussignname

import core.DateFormat
import core.metrics.*

trait DataArrayFrom[A] extends (A => js.Array[Data]):
  extension (a: A) inline def darr = this(a)

object DataArrayFrom:

  given intervals(using Performance[Interval]): DataArrayFrom[Intervals] = v =>
    val distances = 
      val (_, r) = v.foldLeft(0.0 -> List.empty[Meter]):
        case ((a, t), i) => (i.distance + a) -> (i.distance + a :: t)
      r.map(_.round).reverse

    def scatterLine[A <: Double](name: String, fy: Interval => A) =
      Data
        .PartialPlotDataAutobinx()
        .setName(name)
        .setLine(PartialScatterLine().setWidth(1))
        .setY(v.map(fy).toJSArray)
        .setX(distances.toJSArray)
        .setHoverinfo(yPlussignname)

    js.Array(
        scatterLine("mpb", _.mpb).setShowlegend(false),
        scatterLine("bpm", _.averageHR.round).setVisible(true).setYaxis("y2"),
        scatterLine("spm", _.averageRunCadence.round).setVisible(legendonly).setYaxis("y2")
        // scatterLine("/km", _.pace).setVisible(legendonly).setYaxis("y2")
    )

  given history(using Performance[Interval], DateFormat[String]): DataArrayFrom[History] = m =>
    inline def box: Intervals => Data = v =>
      Data
        .PartialBoxPlotData()
        .setType(PlotType.box)
        .setY(v.map(_.mpb).toJSArray)
        .setHoverinfo(y)
        .setName(s"${v.head.startTimeGMT}+08:00".ymd("fr-CA"))
        .setBoxpoints(`false`)
        .setLine(PartialScatterLine().setWidth(1))

    m.filter(_.nonEmpty).map(box).toJSArray

  extension (d: Double) inline def round = scala.scalajs.js.Math.round(d)
  extension (i: Intervals)
    inline def distances =
      val (_, r) = i.foldLeft(0.0 -> List.empty[Meter]):
        case ((a, t), d) => (d.distance + a) -> (d.distance + a :: t)
      r.map(_.round).reverse
end DataArrayFrom
