package plot

import scala.scalajs.js

import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.mod.Data
import typings.plotlyJs.plotlyJsStrings as cs

import common.*
import garmin.*

trait Scatter[A] extends (A => Data):
  extension (a: A) def scatter = this(a)

object Scatter:

  given laps(using
    MetersPerBeat[Performance],
    HoverText[Performance]
  ): Scatter[Laps] =
    laps =>
      Data
        .PartialPlotDataAutobinx()
        .setY(laps.map(_.mpb))
        .setX(laps.map(_.lapIndex.get))
        .setHovertext(laps.map(_.text))
        .setHovertemplate("<b>%{y:.3f}</b><br>%{hovertext}<extra></extra>")

  given activityLaps(using Scatter[Laps], DateFormat[String]): Scatter[ActivityLaps] = ga =>
    ga match
      case (a, laps) =>
        laps.scatter
          .asInstanceOf[PartialPlotDataAutobinx]
          .setHovertemplate("<b>%{y:.3f}</b><br>%{hovertext}")
          .setName(a.startTimeLocal.get.md("en-US"))

  given activities(using
    MetersPerBeat[Performance]
  ): Scatter[js.Array[ActivityItem]] = gas =>
    Data
      .PartialPlotDataAutobinx()
      .setY(gas.map(_.mpb))
      .setX(gas.map(_.startTimeLocal.get))
      .setHovertemplate("%{y:.3f}<extra>%{x|%Y-%m-%d}</extra>")
      .setMode(cs.linesPlussignmarkers)

end Scatter
