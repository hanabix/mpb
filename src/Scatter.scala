import scala.scalajs.js

import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.mod.Data
import typings.plotlyJs.plotlyJsStrings as cs

import Garmin.*

trait Scatter[A] extends (A => Data):
  extension (a: A) def scatter = this(a)

object Scatter:
  given laps(using
    MetersPerBeat[Performace],
    HoverText[Performace]
  ): Scatter[js.Array[Cast[Lap]]] =
    laps =>
      val active = laps.filter(_.intensityType == "ACTIVE")
      Data
        .PartialPlotDataAutobinx()
        .setY(active.map(_.mpb))
        .setX(active.map(_.lapIndex.get))
        .setHovertext(active.map(_.text))
        .setHovertemplate("<b>%{y:.3f}</b><br>%{hovertext}<extra></extra>")

  given activityLaps(using Scatter[js.Array[Cast[Lap]]], DateFormat[String]): Scatter[ActivityLaps] = ga =>
    ga match
      case (a, laps) =>
        laps.scatter
          .asInstanceOf[PartialPlotDataAutobinx]
          .setHovertemplate("<b>%{y:.3f}</b><br>%{hovertext}")
          .setName(a.startTimeLocal.get.md("en-US"))

  given activities(using
    MetersPerBeat[Performace]
  ): Scatter[js.Array[Cast[Activity & Workout & Performace]]] = gas =>
    Data
      .PartialPlotDataAutobinx()
      .setY(gas.map(_.mpb))
      .setX(gas.map(_.startTimeLocal.get))
      .setHovertemplate("%{y:.3f}<extra>%{x|%Y-%m-%d}</extra>")
      .setMode(cs.linesPlussignmarkers)

end Scatter
