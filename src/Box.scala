import scala.scalajs.js

import typings.plotlyJs.anon.PartialPlotDataAutobinx
import typings.plotlyJs.mod.Data
import typings.plotlyJs.plotlyJsStrings as cs

import Garmin.*
import DateString.*

trait Box[A] extends (A => Data):
  extension (a: A) def box = this(a)

object Box:
  given laps(using MetersPerBeat[Performace]): Box[js.Array[Cast[Lap]]] = laps =>
    val active = laps.filter(_.intensityType == "ACTIVE")
    Data
      .PartialPlotDataAutobinx()
      .setY(active.map(_.mpb))
      .setHoverinfo(cs.y)
      .setWidth(0.1)
      .setType(cs.box)

  given activityLaps(using Box[js.Array[Cast[Lap]]]): Box[ActivityLaps] = ga =>
    ga match
      case (a, laps) =>
        laps.box
          .asInstanceOf[PartialPlotDataAutobinx]
          .setName(a.startTimeLocal.get.md("en-US"))
end Box
