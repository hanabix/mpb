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

  type Required = (bpm, spm, Distance, Duration, Intensity)

  given (using
    Predicate[Required, js.Dynamic],
    Fetch[Get, js.Dynamic],
    Inject[History[js.Dynamic]]
  ): Inject[Seq[ActivityId]] with
    extension (ids: Seq[ActivityId])
      def inject(e: HTMLElement): Unit =
        for r <- Future.sequence(ids.map(splits[Required])) do
          (for case NonEmpty.Ref(i) <- r yield i) match
            case NonEmpty.Ref(history) => history.inject(e)
            case _                     => // ignore
    end extension
  end given

  private def splits[T](id: ActivityId)(using Fetch[Get, js.Dynamic], Predicate[T, js.Dynamic]) =
    inline def url      = s"https://connect.garmin.cn/activity-service/activity/$id/splits"
    inline def referrer = s"https://connect.garmin.cn/modern/activity/$id"

    for d <- Get(url, referrer).request
    yield d.asInstanceOf[Splits].lapDTOs.toList.filter(Predicate[T, js.Dynamic])

  private trait Splits extends js.Object:
    def lapDTOs: js.Array[js.Dynamic]

  sealed trait Intensity
  object Intensity:
    private trait Lap extends js.Object:
      def intensityType: js.UndefOr[String]

    given Predicate[Intensity, js.Dynamic] = Predicate: a =>
      val t = a.asInstanceOf[Lap].intensityType
      t == "ACTIVE" || t == "INTERVAL" || t == js.undefined
end ActivityId
