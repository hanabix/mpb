package garmin

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

import org.scalajs.dom.Request

import core.*
import core.metrics.*
import core.service.Fetch

opaque type ActivityId = String

object ActivityId:
  def apply(s: String): ActivityId = s

  given by( using Fetch[ActivityId, Intervals], Inject[History]): Inject[List[ActivityId]] = (e, ids) =>
  for history <- Future.sequence(ids.map(_.request)) if history.nonEmpty do 
    history.filter(_.nonEmpty).inject(e)

  given fetch(using Fetch[Request, js.Dynamic], Conversion[Get, Request]): Fetch[ActivityId, Intervals] = id =>
    inline def url      = s"https://connect.garmin.cn/activity-service/activity/$id/splits"
    inline def referrer = s"https://connect.garmin.cn/modern/activity/$id"

    inline def isActive(t: js.UndefOr[String]): Boolean =
      t == "ACTIVE" || t == "INTERVAL" || t == js.undefined

    for
      d <- Get(url, referrer).convert.request
      laps = d.asInstanceOf[Splits].lapDTOs.toList
    yield for l <- laps if isActive(l.intensityType)
    yield l.asInstanceOf[Interval]
  end fetch

  private trait Lap extends js.Object:
    def intensityType: js.UndefOr[String]

  private trait Splits extends js.Object:
    def lapDTOs: js.Array[Lap]

end ActivityId
