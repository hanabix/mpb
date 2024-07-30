package plot

import scala.scalajs.js

import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.mod.Data
import typings.plotlyJs.plotlyJsStrings as cs

import common.*
import garmin.*

trait Box[A] extends (A => Data):
  extension (a: A) def box = this(a)

object Box:
  given laps(using MetersPerBeat[Performance]): Box[Laps] = laps =>
    Data
      .PartialPlotDataAutobinx()
      .setY(laps.map(_.mpb))
      .setHoverinfo(cs.y)
      .setWidth(0.3)
      .setType(cs.box)

  given activityLaps(using Box[Laps], DateFormat[String]): Box[ActivityByWorkout] = ga =>
    ga match
      case (a, laps) =>
        laps.box
          .asInstanceOf[PartialPlotDataAutobinx]
          .setName(a.startTimeLocal.get.md("en-US"))
end Box
