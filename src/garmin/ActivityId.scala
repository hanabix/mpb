package garmin

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.scalajs.js

import org.scalajs.dom.HTMLElement

import core.*
import core.Gauge.{BeatPerMinute as bpm, StepPerMinute as spm}
import core.service.Fetch

opaque type ActivityId = String

object ActivityId:
  def apply(s: String): ActivityId = s

  given (using
    Predicate[(bpm, spm, Intensity), js.Dynamic],
    Fetch[Get, js.Dynamic],
    Inject[History[js.Dynamic]]
  ): Inject[List[ActivityId]] with
    extension (ids: List[ActivityId])
      def inject(e: HTMLElement): Unit =
        for case Some(h) :: t <- Future.sequence(ids.map(splits[(bpm, spm, Intensity)])) do
          (h -> t.map(_.toList).flatten).inject(e)

  private def splits[T](id: ActivityId)(using Fetch[Get, js.Dynamic], Predicate[T, js.Dynamic]) =
    inline def url      = s"https://connect.garmin.cn/activity-service/activity/$id/splits"
    inline def referrer = s"https://connect.garmin.cn/modern/activity/$id"

    for d <- Get(url, referrer).request
    yield d.asInstanceOf[Splits].lapDTOs.toList.filter(Predicate[T, js.Dynamic]) match
      case h :: t => Some(h -> t)
      case _      => None

  private trait Lap extends js.Object:
    def intensityType: js.UndefOr[String]

  private trait Splits extends js.Object:
    def lapDTOs: js.Array[js.Dynamic]

  sealed trait Intensity
  object Intensity:
    given Predicate[Intensity, js.Dynamic] = Predicate: a =>
      val t = a.asInstanceOf[Lap].intensityType
      t == "ACTIVE" || t == "INTERVAL" || t == js.undefined
end ActivityId
