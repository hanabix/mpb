package plotly

import scala.scalajs.js
import scala.scalajs.js.JSConverters.*

import typings.plotlyJs.anon.PartialScatterLine
import typings.plotlyJs.mod.Data
import typings.plotlyJs.mod.PlotType
import typings.plotlyJs.plotlyJsBooleans.`false`
import typings.plotlyJs.plotlyJsStrings.y

import core.DateFormat
import core.metrics.*

trait DataArrayFrom[A] extends (A => js.Array[Data]):
  extension (a: A) inline def darr = this(a)

object DataArrayFrom:

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


end DataArrayFrom
