package garmin

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

import org.scalajs.dom.Request

import core.*
import core.service.Fetch

opaque type ActivityId = String

object ActivityId:
  def apply(s: String): ActivityId = s

  given inject(using
    Fetch[ActivityId, Option[Interval[js.Dynamic]]],
    Inject[History[js.Dynamic]]
  ): Inject[List[ActivityId]] = (e, ids) =>
    for case Some(h) :: t <- Future.sequence(ids.map(_.request)) do 
      (h -> t.map(_.toList).flatten).inject(e)

  given fetch(using
    Fetch[Request, js.Dynamic],
    Conversion[Get, Request]
  ): Fetch[ActivityId, Option[Interval[js.Dynamic]]] = id =>
    inline def url      = s"https://connect.garmin.cn/activity-service/activity/$id/splits"
    inline def referrer = s"https://connect.garmin.cn/modern/activity/$id"

    for d <- Get(url, referrer).convert.request
    yield d.asInstanceOf[Splits].lapDTOs.toList.filter(_.isActive) match
      case h :: t => Some(h -> t)
      case _      => None
    end for
  end fetch

  private trait Lap extends js.Object:
    def intensityType: js.UndefOr[String]

  extension (l: Lap) private inline def isActive =     
    l.intensityType == "ACTIVE" || l.intensityType == "INTERVAL" || l.intensityType == js.undefined

  private trait Splits extends js.Object:
    def lapDTOs: js.Array[Lap & js.Dynamic]

end ActivityId
