import scala.scalajs.js

import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.mod.Data
import typings.plotlyJs.plotlyJsStrings as cs

import Garmin.*

trait Box[A] extends (A => Data):
  extension (a: A) def box = this(a)

object Box:
  given laps(using MetersPerBeat[Performace]): Box[js.Array[Cast[Lap]]] = laps =>
    Data
      .PartialPlotDataAutobinx()
      .setY(laps.map(_.mpb))
      .setHoverinfo(cs.y)
      .setWidth(0.1)
      .setType(cs.box)

  given activityLaps(using Box[js.Array[Cast[Lap]]], DateFormat[String]): Box[ActivityLaps] = ga =>
    ga match
      case (a, laps) =>
        laps.box
          .asInstanceOf[PartialPlotDataAutobinx]
          .setName(a.startTimeLocal.get.md("en-US"))
end Box
