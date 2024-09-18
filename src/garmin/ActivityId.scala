package garmin

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

import org.scalajs.dom.HTMLElement

import core.*
import core.service.Fetch

opaque type ActivityId = String

object ActivityId:
  def apply(s: String): ActivityId = s

  given inject(using
    Fetch[Get, js.Dynamic],
    Inject[History[js.Dynamic]]
  ): Inject[List[ActivityId]] with
    extension (ids: List[ActivityId])
      def inject(e: HTMLElement): Unit =
        for case Some(h) :: t <- Future.sequence(ids.map(splits)) do (h -> t.map(_.toList).flatten).inject(e)

  private def splits(id: ActivityId)(using Fetch[Get, js.Dynamic]) =
    inline def url      = s"https://connect.garmin.cn/activity-service/activity/$id/splits"
    inline def referrer = s"https://connect.garmin.cn/modern/activity/$id"

    for d <- Get(url, referrer).request yield d.asInstanceOf[Splits].lapDTOs.toList.filter(_.isActive) match
      case h :: t => Some(h -> t)
      case _      => None

  private trait Lap extends js.Object:
    def intensityType: js.UndefOr[String]

  extension (l: Lap)
    private inline def isActive =
      l.intensityType == "ACTIVE" || l.intensityType == "INTERVAL" || l.intensityType == js.undefined

  private trait Splits extends js.Object:
    def lapDTOs: js.Array[Lap & js.Dynamic]

end ActivityId
